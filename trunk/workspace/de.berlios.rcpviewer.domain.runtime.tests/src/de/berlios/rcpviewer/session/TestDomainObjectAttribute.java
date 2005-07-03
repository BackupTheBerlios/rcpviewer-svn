package de.berlios.rcpviewer.session;

import org.eclipse.emf.ecore.EAttribute;

import sun.security.action.GetBooleanAction;

import de.berlios.rcpviewer.AbstractRuntimeTestCase;
import de.berlios.rcpviewer.AbstractTestCase;
import de.berlios.rcpviewer.domain.RuntimeDomain;
import de.berlios.rcpviewer.domain.IRuntimeDomainClass;
import de.berlios.rcpviewer.progmodel.extended.ExtendedDomainClass;
import de.berlios.rcpviewer.progmodel.extended.ExtendedDomainObject;
import de.berlios.rcpviewer.progmodel.extended.ExtendedProgModelDomainBuilder;
import de.berlios.rcpviewer.progmodel.extended.IConstraintSet;

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
	
	public void testCanSetAttributeIfConstraintAllows() {
		IRuntimeDomainClass<OrderConstrained> domainClass = 
			(IRuntimeDomainClass<OrderConstrained>)lookupAny(OrderConstrained.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		IDomainObject<OrderConstrained> domainObject = 
			(IDomainObject<OrderConstrained>)session.createTransient(domainClass);
		EAttribute nameAttribute = domainObject.getEAttributeNamed("quantity");

		ExtendedDomainObject<OrderConstrained> edc = 
			domainObject.getAdapter(ExtendedDomainObject.class);

		IConstraintSet constraint = edc.constraintFor(nameAttribute);
		assertFalse(constraint.invisible().applies());
		assertFalse(constraint.unusable().applies());
	}

	public void testCannotSetAttributeIfConstraintMakesUnusable() {
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
		
		IConstraintSet constraintSet = edc.constraintFor(nameAttribute);
		assertNotNull(edc.getExtendedRuntimeDomainClass().getAttributePre(nameAttribute));
		assertFalse(constraintSet.invisible().applies());
		assertTrue(constraintSet.unusable().applies());
		assertEquals("Cannot change quantity once shipped", constraintSet.unusable().getMessage());
	}

	public void testCannotSetAttributeIfConstraintMakesInvisible() {
		IRuntimeDomainClass<OrderConstrained> domainClass = 
			(IRuntimeDomainClass<OrderConstrained>)lookupAny(OrderConstrained.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		IDomainObject<OrderConstrained> domainObject = 
			(IDomainObject<OrderConstrained>)session.createTransient(domainClass);
		EAttribute nameAttribute = domainObject.getEAttributeNamed("quantity");
		
		domainObject.getPojo().shipAndHide();
		
		ExtendedDomainObject<OrderConstrained> edc = 
			domainObject.getAdapter(ExtendedDomainObject.class);
		
		IConstraintSet constraintSet = edc.constraintFor(nameAttribute);
		assertNotNull(edc.getExtendedRuntimeDomainClass().getAttributePre(nameAttribute));
		assertTrue(constraintSet.invisible().applies());
		assertTrue(constraintSet.unusable().applies());
	}


}
