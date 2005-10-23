package de.berlios.rcpviewer.session;

import org.easymock.MockControl;
import org.eclipse.emf.ecore.EAttribute;

import de.berlios.rcpviewer.AbstractRuntimeTestCase;
import de.berlios.rcpviewer.authorization.IAuthorizationManager;
import de.berlios.rcpviewer.domain.IRuntimeDomainClass;
import de.berlios.rcpviewer.progmodel.extended.ExtendedProgModelDomainBuilder;
import de.berlios.rcpviewer.progmodel.extended.IExtendedDomainObject;
import de.berlios.rcpviewer.progmodel.extended.IPrerequisites;
import de.berlios.rcpviewer.progmodel.extended.Prerequisites;
import de.berlios.rcpviewer.progmodel.extended.IPrerequisites.Constraint;
import de.berlios.rcpviewer.progmodel.standard.IFeatureId;

public class TestExtendedDomainObjectAttribute extends AbstractRuntimeTestCase  {

	public TestExtendedDomainObjectAttribute() {
		super(new ExtendedProgModelDomainBuilder());
	}

	public void testCanSetAttributeIfAccessorPrerequisitesAllow() {
		IRuntimeDomainClass<OrderConstrained> domainClass = 
			(IRuntimeDomainClass<OrderConstrained>)lookupAny(OrderConstrained.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		IDomainObject<OrderConstrained> domainObject = 
			(IDomainObject<OrderConstrained>)session.create(domainClass);
		EAttribute nameEAttribute = domainObject.getEAttributeNamed("quantity");

		IExtendedDomainObject<OrderConstrained> edo = 
			domainObject.getAdapter(IExtendedDomainObject.class);

		IExtendedDomainObject.IExtendedAttribute nameAttrib = edo.getAttribute(nameEAttribute);
		IPrerequisites prerequisites = nameAttrib.accessorPrerequisitesFor();
		assertSame(IPrerequisites.Constraint.NONE, prerequisites.getConstraint());
	}

	public void testCannotSetAttributeIfAccessorPrerequisitesMakesUnusable() {
		IRuntimeDomainClass<OrderConstrained> domainClass = 
			(IRuntimeDomainClass<OrderConstrained>)lookupAny(OrderConstrained.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		IDomainObject<OrderConstrained> domainObject = 
			(IDomainObject<OrderConstrained>)session.create(domainClass);
		EAttribute nameEAttribute = domainObject.getEAttributeNamed("quantity");
		
		domainObject.getPojo().ship();
		
		IExtendedDomainObject<OrderConstrained> edo = 
			domainObject.getAdapter(IExtendedDomainObject.class);
		
		IExtendedDomainObject.IExtendedAttribute attrib = edo.getAttribute(nameEAttribute);
		IPrerequisites prerequisites = attrib.accessorPrerequisitesFor();
		assertNotNull(edo.getExtendedRuntimeDomainClass().getAccessorPre(nameEAttribute));
		assertSame(IPrerequisites.Constraint.UNUSABLE, prerequisites.getConstraint());
		assertEquals("Cannot change quantity once shipped", prerequisites.getDescription());
	}

	public void testCannotSetAttributeIfAccessorPrerequisitesMakesInvisible() {
		IRuntimeDomainClass<OrderConstrained> domainClass = 
			(IRuntimeDomainClass<OrderConstrained>)lookupAny(OrderConstrained.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		IDomainObject<OrderConstrained> domainObject = 
			(IDomainObject<OrderConstrained>)session.create(domainClass);
		EAttribute nameEAttribute = domainObject.getEAttributeNamed("quantity");
		
		domainObject.getPojo().shipAndRestrict();
		
		IExtendedDomainObject<OrderConstrained> edo = 
			domainObject.getAdapter(IExtendedDomainObject.class);
		
		IExtendedDomainObject.IExtendedAttribute attrib = edo.getAttribute(nameEAttribute); 
		IPrerequisites prerequisites = attrib.accessorPrerequisitesFor();
		assertNotNull(edo.getExtendedRuntimeDomainClass().getAccessorPre(nameEAttribute));
		assertSame(IPrerequisites.Constraint.INVISIBLE, prerequisites.getConstraint());
	}

	/**
	 * Test of set...Pre(..) prerequisites.
	 *
	 */
	public void testCannotSetAttributeIfMutatorPrerequisitesPrevent() {
		IRuntimeDomainClass<OrderConstrained> domainClass = 
			(IRuntimeDomainClass<OrderConstrained>)lookupAny(OrderConstrained.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		IDomainObject<OrderConstrained> domainObject = 
			(IDomainObject<OrderConstrained>)session.create(domainClass);
		EAttribute nameEAttribute = domainObject.getEAttributeNamed("quantity");
		
		IExtendedDomainObject<OrderConstrained> edo = 
			domainObject.getAdapter(IExtendedDomainObject.class);
		
		IExtendedDomainObject.IExtendedAttribute attrib = edo.getAttribute(nameEAttribute); 
		IPrerequisites prerequisites = attrib.mutatorPrerequisitesFor(new Integer(-1));
		assertNotNull(edo.getExtendedRuntimeDomainClass().getMutatorPre(nameEAttribute));
		assertSame(IPrerequisites.Constraint.UNUSABLE, prerequisites.getConstraint());
	}


	/**
	 * 
	 *
	 */
	public void testAttributePrerequisitesChangedViaExternalStateChanged() {
		IRuntimeDomainClass<Ping> pingDomainClass = 
			(IRuntimeDomainClass<Ping>)lookupAny(Ping.class);
		IRuntimeDomainClass<Pong> pongDomainClass = 
			(IRuntimeDomainClass<Pong>)lookupAny(Pong.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		IDomainObject<Ping> pingDomainObject = 
			(IDomainObject<Ping>)session.recreate(pingDomainClass);
		IDomainObject<Pong> pongDomainObject = 
			(IDomainObject<Pong>)session.recreate(pongDomainClass);

		Ping ping = pingDomainObject.getPojo();
		Pong pong = pongDomainObject.getPojo();
		ping.setPong(pong);

		EAttribute pingVisibleIfEAttribute = pingDomainObject.getEAttributeNamed("visibleOnlyIfPongPositive");
		EAttribute pingUsableIfEAttribute = pingDomainObject.getEAttributeNamed("usableOnlyIfPongPositive");
		
		IExtendedDomainObject<OrderConstrained> pingExtendedDomainObject = 
			pingDomainObject.getAdapter(IExtendedDomainObject.class);
		IExtendedDomainObject.IExtendedAttribute pingVisibleIfAttrib = 
			pingExtendedDomainObject.getAttribute(pingVisibleIfEAttribute); 
		IExtendedDomainObject.IExtendedAttribute pingUsableIfAttrib = 
			pingExtendedDomainObject.getAttribute(pingUsableIfEAttribute);
		
		// should now be two observed features held by the session.
		assertEquals(2, session.getObservedFeatures().size());
		
		IPrerequisites pingVisibleIfAttribPrereqs = pingVisibleIfAttrib.accessorPrerequisitesFor();
		IPrerequisites pingUsableIfAttribPrereqs = pingUsableIfAttrib.accessorPrerequisitesFor();
		MyExtendedDomainObjectAttributeListener pingVisibleIfAttribListener =
			pingVisibleIfAttrib.addExtendedDomainObjectAttributeListener(new MyExtendedDomainObjectAttributeListener());
		MyExtendedDomainObjectAttributeListener pingUsableIfAttribListener =
			pingUsableIfAttrib.addExtendedDomainObjectAttributeListener(new MyExtendedDomainObjectAttributeListener());
		
		// our listeners should not yet have been called
		assertNull(pingVisibleIfAttribListener.event);
		assertNull(pingUsableIfAttribListener.event);
		
		// set a number; we should be notified because the "attribute" doesn't know any better.
		pong.setNumber(2);
		assertNotNull(pingVisibleIfAttribListener.event);
		assertNotNull(pingUsableIfAttribListener.event);
		assertSame(
				Constraint.NONE, 
				pingVisibleIfAttribListener.event.getNewPrerequisites().getConstraint()); // no constraint yet
		assertSame(
				Constraint.NONE, 
				pingUsableIfAttribListener.event.getNewPrerequisites().getConstraint()); // no constraint yet

		// reset
		pingVisibleIfAttribListener.event = null;
		pingUsableIfAttribListener.event = null;

		// set to another +ve number; our listeners shouldn't be called
		pong.setNumber(5);
		assertNull(pingVisibleIfAttribListener.event);
		assertNull(pingUsableIfAttribListener.event);

		// set to a -ve number; our listeners should now be called because the prereqs have changed
		pong.setNumber(-5);
		assertNotNull(pingVisibleIfAttribListener.event);
		assertNotNull(pingUsableIfAttribListener.event);
		assertSame(
				Constraint.INVISIBLE, 
				pingVisibleIfAttribListener.event.getNewPrerequisites().getConstraint()); // now invisible
		assertSame(
				Constraint.UNUSABLE, 
				pingUsableIfAttribListener.event.getNewPrerequisites().getConstraint()); // now invisible
	}

}
