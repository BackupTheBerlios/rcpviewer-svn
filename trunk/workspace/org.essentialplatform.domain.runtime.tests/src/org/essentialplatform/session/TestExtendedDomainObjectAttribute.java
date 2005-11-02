package org.essentialplatform.session;

import org.easymock.MockControl;
import org.eclipse.emf.ecore.EAttribute;

import org.essentialplatform.AbstractRuntimeTestCase;
import org.essentialplatform.authorization.IAuthorizationManager;
import org.essentialplatform.domain.IDomainClass;
import org.essentialplatform.progmodel.extended.IPrerequisites;
import org.essentialplatform.progmodel.extended.Prerequisites;
import org.essentialplatform.progmodel.extended.IPrerequisites.Constraint;
import org.essentialplatform.progmodel.standard.EssentialProgModelExtendedSemanticsDomainBuilder;
import org.essentialplatform.progmodel.standard.IFeatureId;

public class TestExtendedDomainObjectAttribute extends AbstractRuntimeTestCase  {

	public TestExtendedDomainObjectAttribute() {
		super(new EssentialProgModelExtendedSemanticsDomainBuilder());
	}

	public void testCanSetAttributeIfAccessorPrerequisitesAllow() {
		IDomainClass domainClass = 
			(IDomainClass)lookupAny(OrderConstrained.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		IDomainObject<?> domainObject = session.create(domainClass);

		EAttribute eAttrib = domainObject.getEAttributeNamed("quantity");
		IDomainClass.IAttribute classAttrib = domainClass.getAttribute(eAttrib);
		IDomainObject.IObjectAttribute attrib = domainObject.getAttribute(eAttrib);
		
		IPrerequisites prerequisites = attrib.accessorPrerequisitesFor();
		assertSame(IPrerequisites.Constraint.NONE, prerequisites.getConstraint());
	}

	public void testCannotSetAttributeIfAccessorPrerequisitesMakesUnusable() {
		IDomainClass domainClass = 
			(IDomainClass)lookupAny(OrderConstrained.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		IDomainObject<?> domainObject = session.create(domainClass);
		OrderConstrained pojo = (OrderConstrained)domainObject.getPojo();
		pojo.ship();
		
		EAttribute eAttrib = domainObject.getEAttributeNamed("quantity");
		IDomainClass.IAttribute classAttrib = domainClass.getAttribute(eAttrib);
		IDomainObject.IObjectAttribute attrib = domainObject.getAttribute(eAttrib);
		
		IPrerequisites prerequisites = attrib.accessorPrerequisitesFor();
		assertSame(IPrerequisites.Constraint.UNUSABLE, prerequisites.getConstraint());
		assertEquals("Cannot change quantity once shipped", prerequisites.getDescription());
	}

	public void testCannotSetAttributeIfAccessorPrerequisitesMakesInvisible() {
		IDomainClass domainClass = 
			(IDomainClass)lookupAny(OrderConstrained.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		IDomainObject<?> domainObject = session.create(domainClass);
		OrderConstrained pojo = (OrderConstrained)domainObject.getPojo();
		pojo.shipAndRestrict();
		
		EAttribute eAttrib = domainObject.getEAttributeNamed("quantity");
		IDomainClass.IAttribute classAttrib = domainClass.getAttribute(eAttrib);
		IDomainObject.IObjectAttribute attrib = domainObject.getAttribute(eAttrib);
		
		IPrerequisites prerequisites = attrib.accessorPrerequisitesFor();
		assertSame(IPrerequisites.Constraint.INVISIBLE, prerequisites.getConstraint());
	}

	/**
	 * Test of set...Pre(..) prerequisites.
	 *
	 */
	public void testCannotSetAttributeIfMutatorPrerequisitesPrevent() {
		IDomainClass domainClass = 
			(IDomainClass)lookupAny(OrderConstrained.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		IDomainObject<?> domainObject = session.create(domainClass);
		
		EAttribute eAttrib = domainObject.getEAttributeNamed("quantity");
		IDomainClass.IAttribute classAttrib = domainClass.getAttribute(eAttrib);
		IDomainObject.IObjectAttribute attrib = domainObject.getAttribute(eAttrib);

		IPrerequisites prerequisites = attrib.mutatorPrerequisitesFor(new Integer(-1));
		assertSame(IPrerequisites.Constraint.UNUSABLE, prerequisites.getConstraint());
	}


	/**
	 * 
	 *
	 */
	public void testAttributePrerequisitesChangedViaExternalStateChanged() {
		
		IDomainClass pingDomainClass = 
			(IDomainClass)lookupAny(Ping.class);
		IDomainClass pongDomainClass = 
			(IDomainClass)lookupAny(Pong.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		IDomainObject<?> pingDomainObject = session.recreate(pingDomainClass);
		IDomainObject<?> pongDomainObject = session.recreate(pongDomainClass);

		Ping ping = (Ping)pingDomainObject.getPojo();
		Pong pong = (Pong)pongDomainObject.getPojo();
		ping.setPong(pong);

		EAttribute pingVisibleEAttrib = pingDomainObject.getEAttributeNamed("visibleOnlyIfPongPositive");
		IDomainClass.IAttribute pingVisibleClassAttrib = pingDomainClass.getAttribute(pingVisibleEAttrib);
		IDomainObject.IObjectAttribute pingVisibleAttrib = pingDomainObject.getAttribute(pingVisibleEAttrib);

		EAttribute pingUsableEAttrib = pingDomainObject.getEAttributeNamed("usableOnlyIfPongPositive");
		IDomainClass.IAttribute pingUsableClassAttrib = pingDomainClass.getAttribute(pingUsableEAttrib);
		IDomainObject.IObjectAttribute pingUsableAttrib = pingDomainObject.getAttribute(pingUsableEAttrib);

		// should now be two observed features held by the session.
		assertEquals(2, session.getObservedFeatures().size());
		
		IPrerequisites pingVisibleIfAttribPrereqs = pingVisibleAttrib.accessorPrerequisitesFor();
		IPrerequisites pingUsableIfAttribPrereqs = pingUsableAttrib.accessorPrerequisitesFor();
		MyDomainObjectAttributeListener pingVisibleIfAttribListener =
			pingVisibleAttrib.addListener(new MyDomainObjectAttributeListener());
		MyDomainObjectAttributeListener pingUsableIfAttribListener =
			pingUsableAttrib.addListener(new MyDomainObjectAttributeListener());
		
		// our listeners should not yet have been called
		assertNull(pingVisibleIfAttribListener.extendedEvent);
		assertNull(pingUsableIfAttribListener.extendedEvent);
		
		// set a number; we should be notified because the "attribute" doesn't know any better.
		pong.setNumber(2);
		assertNotNull(pingVisibleIfAttribListener.extendedEvent);
		assertNotNull(pingUsableIfAttribListener.extendedEvent);
		assertSame(
				Constraint.NONE, 
				pingVisibleIfAttribListener.extendedEvent.getNewPrerequisites().getConstraint()); // no constraint yet
		assertSame(
				Constraint.NONE, 
				pingUsableIfAttribListener.extendedEvent.getNewPrerequisites().getConstraint()); // no constraint yet

		// reset
		pingVisibleIfAttribListener.extendedEvent = null;
		pingUsableIfAttribListener.extendedEvent = null;

		// set to another +ve number; our listeners shouldn't be called
		pong.setNumber(5);
		assertNull(pingVisibleIfAttribListener.extendedEvent);
		assertNull(pingUsableIfAttribListener.extendedEvent);

		// set to a -ve number; our listeners should now be called because the prereqs have changed
		pong.setNumber(-5);
		assertNotNull(pingVisibleIfAttribListener.extendedEvent);
		assertNotNull(pingUsableIfAttribListener.extendedEvent);
		assertSame(
				Constraint.INVISIBLE, 
				pingVisibleIfAttribListener.extendedEvent.getNewPrerequisites().getConstraint()); // now invisible
		assertSame(
				Constraint.UNUSABLE, 
				pingUsableIfAttribListener.extendedEvent.getNewPrerequisites().getConstraint()); // now invisible
	}

}
