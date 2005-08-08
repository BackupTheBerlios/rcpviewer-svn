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
			(IDomainObject<OrderConstrained>)session.createTransient(domainClass);
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
			(IDomainObject<OrderConstrained>)session.createTransient(domainClass);
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
			(IDomainObject<OrderConstrained>)session.createTransient(domainClass);
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
			(IDomainObject<OrderConstrained>)session.createTransient(domainClass);
		EAttribute nameEAttribute = domainObject.getEAttributeNamed("quantity");
		
		IExtendedDomainObject<OrderConstrained> edo = 
			domainObject.getAdapter(IExtendedDomainObject.class);
		
		IExtendedDomainObject.IExtendedAttribute attrib = edo.getAttribute(nameEAttribute); 
		IPrerequisites prerequisites = attrib.mutatorPrerequisitesFor(new Integer(-1));
		assertNotNull(edo.getExtendedRuntimeDomainClass().getMutatorPre(nameEAttribute));
		assertSame(IPrerequisites.Constraint.UNUSABLE, prerequisites.getConstraint());
	}

	public void testCanSetAttributeIfDefaultAuthorizationManagerConfigured() {
		IRuntimeDomainClass<OrderConstrained> domainClass = 
			(IRuntimeDomainClass<OrderConstrained>)lookupAny(OrderConstrained.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		IDomainObject<OrderConstrained> domainObject = 
			(IDomainObject<OrderConstrained>)session.createTransient(domainClass);
		EAttribute nameEAttribute = domainObject.getEAttributeNamed("quantity");

		IExtendedDomainObject<OrderConstrained> edo = 
			domainObject.getAdapter(IExtendedDomainObject.class);

		IExtendedDomainObject.IExtendedAttribute attrib = edo.getAttribute(nameEAttribute); 
		IPrerequisites prerequisites = attrib.accessorPrerequisitesFor();
		assertSame(IPrerequisites.Constraint.NONE, prerequisites.getConstraint());
	}

	public void incompletetestCanSetAttributeIfAuthorizationManagerDoesNotVeto() {
		
	}

	public void testCanSetAttributeIfAuthorizationManagerDoesNotVetoUsability() {
		
		MockControl control = 
			MockControl.createStrictControl(IAuthorizationManager.class);
		IAuthorizationManager authorizationManager = 
			(IAuthorizationManager)control.getMock();
		
		IRuntimeDomainClass<OrderConstrained> domainClass = 
			(IRuntimeDomainClass<OrderConstrained>)lookupAny(OrderConstrained.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getRuntimeDomainInstance().setAuthorizationManager(authorizationManager);
		getDomainInstance().done();
		
		IDomainObject<OrderConstrained> domainObject = 
			(IDomainObject<OrderConstrained>)session.createTransient(domainClass);
		EAttribute nameEAttribute = domainObject.getEAttributeNamed("quantity");

		IExtendedDomainObject<OrderConstrained> edo = 
			domainObject.getAdapter(IExtendedDomainObject.class);

		// set expectations
		IFeatureId feature = 
			domainClass.attributeIdFor(domainClass.getEAttributeNamed("quantity"));
		authorizationManager.preconditionsFor(feature);
		control.setMatcher(MockControl.EQUALS_MATCHER);
		control.setReturnValue(Prerequisites.none());
		control.replay();
		
		IExtendedDomainObject.IExtendedAttribute attrib = edo.getAttribute(nameEAttribute); 
		IPrerequisites prerequisites = attrib.authorizationPrerequisitesFor();
		assertSame(IPrerequisites.Constraint.NONE, prerequisites.getConstraint());
		
		// verify
		control.verify();
	}
	public void testCannotSetAttributeIfAuthorizationManagerVetoesUsability() {
		MockControl control = 
			MockControl.createStrictControl(IAuthorizationManager.class);
		IAuthorizationManager authorizationManager = 
			(IAuthorizationManager)control.getMock();
		
		IRuntimeDomainClass<OrderConstrained> domainClass = 
			(IRuntimeDomainClass<OrderConstrained>)lookupAny(OrderConstrained.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getRuntimeDomainInstance().setAuthorizationManager(authorizationManager);
		getDomainInstance().done();
		
		IDomainObject<OrderConstrained> domainObject = 
			(IDomainObject<OrderConstrained>)session.createTransient(domainClass);
		EAttribute nameEAttribute = domainObject.getEAttributeNamed("quantity");

		IExtendedDomainObject<OrderConstrained> edo = 
			domainObject.getAdapter(IExtendedDomainObject.class);

		// set expectations
		IFeatureId featureId = 
			domainClass.attributeIdFor(domainClass.getEAttributeNamed("quantity"));
		authorizationManager.preconditionsFor(featureId);
		control.setMatcher(MockControl.EQUALS_MATCHER);
		IPrerequisites returnPrerequisites = Prerequisites.require(false, "Cannot edit quantity"); 
		control.setReturnValue(returnPrerequisites);
		control.replay();
		
		IExtendedDomainObject.IExtendedAttribute attrib = edo.getAttribute(nameEAttribute); 
		IPrerequisites prerequisites = attrib.authorizationPrerequisitesFor();
		assertSame(IPrerequisites.Constraint.UNUSABLE, prerequisites.getConstraint());
		
		// verify
		control.verify();

	}



}
