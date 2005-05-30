package de.berlios.rcpviewer.session;

import org.eclipse.emf.ecore.EAttribute;

import de.berlios.rcpviewer.AbstractRuntimeTestCase;
import de.berlios.rcpviewer.AbstractTestCase;
import de.berlios.rcpviewer.domain.Domain;
import de.berlios.rcpviewer.domain.IRuntimeDomainClass;

public class TestDomainObjectAttribute extends AbstractRuntimeTestCase  {

	public TestDomainObjectAttribute() {
		super(null);
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
		
		IDomainObject<Department> domainObject = 
			(IDomainObject<Department>)session.createTransient(domainClass);
		EAttribute nameAttribute = domainObject.getEAttributeNamed("name");
		domainObject.set(nameAttribute, "HR");
		assertEquals("HR", domainObject.getPojo().getName());
	}

	public void testSettingAttributeNotifiesListeners() {
		IRuntimeDomainClass<Department> domainClass = 
			(IRuntimeDomainClass<Department>)lookupAny(Department.class);
		
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


}
