package org.essentialplatform.runtime.shared.tests.session;

import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.progmodel.essential.app.IPrerequisites;
import org.essentialplatform.progmodel.essential.app.IPrerequisites.Constraint;
import org.essentialplatform.runtime.client.domain.bindings.IObjectAttributeClientBinding;
import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.tests.AbstractRuntimeClientTestCase;
import org.essentialplatform.runtime.shared.tests.session.fixture.OrderConstrained;
import org.essentialplatform.runtime.shared.tests.session.fixture.Ping;
import org.essentialplatform.runtime.shared.tests.session.fixture.Pong;

public class TestExtendedDomainObjectAttribute extends AbstractRuntimeClientTestCase  {

	public void testCanSetAttributeIfAccessorPrerequisitesAllow() {
		IDomainClass domainClass = lookupAny(OrderConstrained.class);
		
		IDomainObject<OrderConstrained> domainObject = clientSession.create(domainClass);

		IDomainClass.IAttribute iAttrib = domainObject.getIAttributeNamed("quantity");
		IDomainObject.IObjectAttribute attrib = domainObject.getAttribute(iAttrib);
		
		IObjectAttributeClientBinding atBinding = (IObjectAttributeClientBinding)attrib.getBinding();
		IPrerequisites prerequisites = atBinding.accessorPrerequisitesFor();
		assertSame(IPrerequisites.Constraint.NONE, prerequisites.getConstraint());
	}

	public void testCannotSetAttributeIfAccessorPrerequisitesMakesUnusable() {
		IDomainClass domainClass = lookupAny(OrderConstrained.class);
		
		IDomainObject<OrderConstrained> domainObject = clientSession.create(domainClass);
		OrderConstrained pojo = domainObject.getPojo();
		pojo.ship();
		
		IDomainClass.IAttribute iAttrib = domainObject.getIAttributeNamed("quantity");
		IDomainObject.IObjectAttribute attrib = domainObject.getAttribute(iAttrib);
		
		IObjectAttributeClientBinding atBinding = (IObjectAttributeClientBinding)attrib.getBinding();
		IPrerequisites prerequisites = atBinding.accessorPrerequisitesFor();
		assertSame(IPrerequisites.Constraint.UNUSABLE, prerequisites.getConstraint());
		assertEquals("Cannot change quantity once shipped", prerequisites.getDescription());
	}

	public void testCannotSetAttributeIfAccessorPrerequisitesMakesInvisible() {
		IDomainClass domainClass = lookupAny(OrderConstrained.class);
		
		IDomainObject<OrderConstrained> domainObject = clientSession.create(domainClass);
		OrderConstrained pojo = domainObject.getPojo();
		pojo.shipAndRestrict();
		
		IDomainClass.IAttribute iAttrib = domainObject.getIAttributeNamed("quantity");
		IDomainObject.IObjectAttribute attrib = domainObject.getAttribute(iAttrib);
		
		IObjectAttributeClientBinding atBinding = (IObjectAttributeClientBinding)attrib.getBinding();
		IPrerequisites prerequisites = atBinding.accessorPrerequisitesFor();
		assertSame(IPrerequisites.Constraint.INVISIBLE, prerequisites.getConstraint());
	}

	/**
	 * Test of set...Pre(..) prerequisites.
	 *
	 */
	public void testCannotSetAttributeIfMutatorPrerequisitesPrevent() {
		IDomainClass domainClass = lookupAny(OrderConstrained.class);
		
		IDomainObject<OrderConstrained> domainObject = clientSession.create(domainClass);
		
		IDomainClass.IAttribute iAttrib = domainObject.getIAttributeNamed("quantity");
		IDomainObject.IObjectAttribute attrib = domainObject.getAttribute(iAttrib);
		IObjectAttributeClientBinding atBinding = (IObjectAttributeClientBinding)attrib.getBinding();
		
		IPrerequisites prerequisites = atBinding.mutatorPrerequisitesFor(new Integer(-1));
		assertSame(IPrerequisites.Constraint.UNUSABLE, prerequisites.getConstraint());
	}


	/**
	 * 
	 *
	 */
	public void testAttributePrerequisitesChangedViaExternalStateChanged() {
		
		IDomainClass pingDomainClass = lookupAny(Ping.class);
		IDomainClass pongDomainClass = lookupAny(Pong.class);
		
		IDomainObject<Ping> pingDomainObject = clientSession.recreate(pingDomainClass);
		IDomainObject<Pong> pongDomainObject = clientSession.recreate(pongDomainClass);

		Ping ping = pingDomainObject.getPojo();
		Pong pong = pongDomainObject.getPojo();
		ping.setPong(pong);

		IDomainClass.IAttribute pingVisibleEAttrib = pingDomainObject.getIAttributeNamed("visibleOnlyIfPongPositive");
		IDomainObject.IObjectAttribute pingVisibleAttrib = pingDomainObject.getAttribute(pingVisibleEAttrib);

		IDomainClass.IAttribute pingUsableEAttrib = pingDomainObject.getIAttributeNamed("usableOnlyIfPongPositive");
		IDomainObject.IObjectAttribute pingUsableAttrib = pingDomainObject.getAttribute(pingUsableEAttrib);

		// should now be two observed features held by the session.
		assertEquals(2, clientSession.getObservedFeatures().size());
		
		IObjectAttributeClientBinding pingVisibleAtBinding = (IObjectAttributeClientBinding)pingVisibleAttrib.getBinding();
		IObjectAttributeClientBinding pingUsableAtBinding = (IObjectAttributeClientBinding)pingUsableAttrib.getBinding();

		MyDomainObjectAttributeListener pingVisibleIfAttribListener =
			pingVisibleAtBinding.addListener(new MyDomainObjectAttributeListener());
		MyDomainObjectAttributeListener pingUsableIfAttribListener =
			pingUsableAtBinding.addListener(new MyDomainObjectAttributeListener());
		
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
