package de.berlios.rcpviewer.session;

import org.easymock.MockControl;
import org.eclipse.emf.ecore.EAttribute;

import sun.reflect.ReflectionFactory.GetReflectionFactoryAction;

import de.berlios.rcpviewer.AbstractRuntimeTestCase;
import de.berlios.rcpviewer.authorization.IAuthorizationManager;
import de.berlios.rcpviewer.domain.IRuntimeDomainClass;
import de.berlios.rcpviewer.progmodel.extended.Prerequisites;
import de.berlios.rcpviewer.progmodel.extended.ExtendedDomainObject;
import de.berlios.rcpviewer.progmodel.extended.ExtendedProgModelDomainBuilder;
import de.berlios.rcpviewer.progmodel.extended.IPrerequisites;
import de.berlios.rcpviewer.progmodel.standard.FeatureId;
import de.berlios.rcpviewer.progmodel.standard.IFeatureId;

public class TestDomainObjectAttribute extends AbstractRuntimeTestCase  {

	public TestDomainObjectAttribute() {
		super(new ExtendedProgModelDomainBuilder());
	}

	public void testCanGetAttribute() {
		IRuntimeDomainClass<Department> domainClass = 
			(IRuntimeDomainClass<Department>)lookupAny(Department.class);
		
		IDomainObject<Department> domainObject = 
			(IDomainObject<Department>)session.createTransient(domainClass);
		domainObject.getPojo().setName("HR");
		EAttribute nameAttribute = domainObject.getEAttributeNamed("name");
		String value = (String)domainObject.get(nameAttribute);
		assertEquals("HR", value);
	}

	public void testCanSetAttribute() {
		IRuntimeDomainClass<Department> domainClass = 
			(IRuntimeDomainClass<Department>)lookupAny(Department.class);
		getDomainInstance().addBuilder(new ExtendedProgModelDomainBuilder());
		getDomainInstance().done();

		IDomainObject<Department> domainObject = 
			(IDomainObject<Department>)session.createTransient(domainClass);
		EAttribute nameAttribute = domainObject.getEAttributeNamed("name");
		domainObject.set(nameAttribute, "HR");
		assertEquals("HR", domainObject.getPojo().getName());
	}

	public void testSettingAttributeNotifiesListeners() {
		IRuntimeDomainClass<Department> domainClass = 
			(IRuntimeDomainClass<Department>)lookupAny(Department.class);
		getDomainInstance().addBuilder(new ExtendedProgModelDomainBuilder());
		getDomainInstance().done();
		
		IDomainObject<Department> domainObject = 
			(IDomainObject<Department>)session.createTransient(domainClass);
		MyDomainObjectListener l =
			domainObject.addDomainObjectListener(new MyDomainObjectListener());
		EAttribute nameAttribute = domainObject.getEAttributeNamed("name");
		domainObject.set(nameAttribute, "HR");
		assertTrue(l.attributeChangedCallbackCalled);
		assertFalse(l.persistedCallbackCalled);
	}

	public void testCannotSetAttributeToInvalidValue() {
		IRuntimeDomainClass<Department> domainClass = 
			(IRuntimeDomainClass<Department>)lookupAny(Department.class);
		getDomainInstance().addBuilder(new ExtendedProgModelDomainBuilder());
		getDomainInstance().done();
		
		IDomainObject<Department> domainObject = 
			(IDomainObject<Department>)session.createTransient(domainClass);
		EAttribute nameAttribute = domainObject.getEAttributeNamed("name");
		try {
			domainObject.set(nameAttribute, new Integer(1));
			fail("Expected IllegalArgumentException to have been thrown");
		} catch(IllegalArgumentException ex) {
			// expected.
		}
	}
	
	public void testCanSetAttributeIfPrerequisitesAllow() {
		IRuntimeDomainClass<OrderConstrained> domainClass = 
			(IRuntimeDomainClass<OrderConstrained>)lookupAny(OrderConstrained.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		IDomainObject<OrderConstrained> domainObject = 
			(IDomainObject<OrderConstrained>)session.createTransient(domainClass);
		EAttribute nameAttribute = domainObject.getEAttributeNamed("quantity");

		ExtendedDomainObject<OrderConstrained> edc = 
			domainObject.getAdapter(ExtendedDomainObject.class);

		IPrerequisites prerequisites = edc.prerequisiteFor(nameAttribute);
		assertSame(IPrerequisites.Constraint.NONE, prerequisites.getConstraint());
	}

	public void testCannotSetAttributeIfPrerequisitesMakesUnusable() {
		IRuntimeDomainClass<OrderConstrained> domainClass = 
			(IRuntimeDomainClass<OrderConstrained>)lookupAny(OrderConstrained.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		IDomainObject<OrderConstrained> domainObject = 
			(IDomainObject<OrderConstrained>)session.createTransient(domainClass);
		EAttribute nameAttribute = domainObject.getEAttributeNamed("quantity");
		
		domainObject.getPojo().ship();
		
		ExtendedDomainObject<OrderConstrained> edc = 
			domainObject.getAdapter(ExtendedDomainObject.class);
		
		IPrerequisites prerequisites = edc.prerequisiteFor(nameAttribute);
		assertNotNull(edc.getExtendedRuntimeDomainClass().getAttributePre(nameAttribute));
		assertSame(IPrerequisites.Constraint.UNUSABLE, prerequisites.getConstraint());
		assertEquals("Cannot change quantity once shipped", prerequisites.getDescription());
	}

	public void testCannotSetAttributeIfPrerequisitesMakesInvisible() {
		IRuntimeDomainClass<OrderConstrained> domainClass = 
			(IRuntimeDomainClass<OrderConstrained>)lookupAny(OrderConstrained.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		IDomainObject<OrderConstrained> domainObject = 
			(IDomainObject<OrderConstrained>)session.createTransient(domainClass);
		EAttribute nameAttribute = domainObject.getEAttributeNamed("quantity");
		
		domainObject.getPojo().shipAndRestrict();
		
		ExtendedDomainObject<OrderConstrained> edc = 
			domainObject.getAdapter(ExtendedDomainObject.class);
		
		IPrerequisites prerequisites = edc.prerequisiteFor(nameAttribute);
		assertNotNull(edc.getExtendedRuntimeDomainClass().getAttributePre(nameAttribute));
		assertSame(IPrerequisites.Constraint.INVISIBLE, prerequisites.getConstraint());
	}

	public void testCanSetAttributeIfDefaultAuthorizationManagerConfigured() {
		IRuntimeDomainClass<OrderConstrained> domainClass = 
			(IRuntimeDomainClass<OrderConstrained>)lookupAny(OrderConstrained.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		IDomainObject<OrderConstrained> domainObject = 
			(IDomainObject<OrderConstrained>)session.createTransient(domainClass);
		EAttribute nameAttribute = domainObject.getEAttributeNamed("quantity");

		ExtendedDomainObject<OrderConstrained> edc = 
			domainObject.getAdapter(ExtendedDomainObject.class);

		IPrerequisites prerequisites = edc.prerequisiteFor(nameAttribute);
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
		EAttribute nameAttribute = domainObject.getEAttributeNamed("quantity");

		ExtendedDomainObject<OrderConstrained> edc = 
			domainObject.getAdapter(ExtendedDomainObject.class);

		// set expectations
		IFeatureId feature = 
			domainClass.attributeIdFor(domainClass.getEAttributeNamed("quantity"));
		authorizationManager.preconditionsFor(feature);
		control.setMatcher(MockControl.EQUALS_MATCHER);
		control.setReturnValue(Prerequisites.noop());
		control.replay();
		
		IPrerequisites prerequisites = edc.prerequisiteFor(nameAttribute);
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
		EAttribute nameAttribute = domainObject.getEAttributeNamed("quantity");

		ExtendedDomainObject<OrderConstrained> edc = 
			domainObject.getAdapter(ExtendedDomainObject.class);

		// set expectations
		IFeatureId featureId = 
			domainClass.attributeIdFor(domainClass.getEAttributeNamed("quantity"));
		authorizationManager.preconditionsFor(featureId);
		control.setMatcher(MockControl.EQUALS_MATCHER);
		IPrerequisites returnPrerequisites = Prerequisites.require(false, "Cannot edit quantity"); 
		control.setReturnValue(returnPrerequisites);
		control.replay();
		
		IPrerequisites prerequisites = edc.prerequisiteFor(nameAttribute);
		assertSame(IPrerequisites.Constraint.UNUSABLE, prerequisites.getConstraint());
		
		// verify
		control.verify();

	}


}
