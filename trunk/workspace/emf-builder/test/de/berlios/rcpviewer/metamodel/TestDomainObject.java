package de.berlios.rcpviewer.metamodel;

import org.eclipse.emf.ecore.EAttribute;

import de.berlios.rcpviewer.AbstractTestCase;
import de.berlios.rcpviewer.progmodel.standard.impl.Department;

public class TestDomainObject extends AbstractTestCase  {

	private MetaModel metaModel;
	protected void setUp() throws Exception {
		super.setUp();
		metaModel = new MetaModel();
	}

	protected void tearDown() throws Exception {
		metaModel = null;
		super.tearDown();
		
		getObjectStore().reset();
		getSession().reset();
	}

	
	/**
	 * 
	 */
	public void testCanPersistThroughDomainObject() {
		IDomainClass<Department> domainClass = 
			metaModel.register(Department.class);
		IDomainObject<Department> domainObject = 
			(IDomainObject<Department>)getSession().createTransient(domainClass);
		domainObject.persist();
		assertTrue(domainObject.isPersistent());
	}

	/**
	 * Depending upon the programming model, the pojo may request that it is
	 * persisted.
	 * 
	 * <p>
	 * The means for doing this will be dependent on the programming model.
	 * In the standard programming model, we pick up on a method called 'save'.
	 */
	public void testCanPersistThroughPojo() {
		IDomainClass<Department> domainClass = 
			metaModel.register(Department.class);
		IDomainObject<Department> domainObject = 
			(IDomainObject<Department>)getSession().createTransient(domainClass);
		domainObject.getPojo().save();
		assertTrue(domainObject.isPersistent());
	}

	/**
	 * Create directly from DomainClass rather than from Session. 
	 */
	public void testCannotPersistIfNotAttachedToSession() {
		IDomainClass<Department> domainClass = 
			metaModel.register(Department.class);
		IDomainObject<Department> domainObject = domainClass.createTransient();
		assertFalse(getSession().isAttached(domainObject));
		try {
			domainObject.persist();
			fail("IllegalArgumentException should have been thrown.");
		} catch(IllegalArgumentException ex) {
			// expected
		}
	}


	public void testCannotPersistMoreThanOnce() {
		IDomainClass<Department> domainClass = 
			metaModel.register(Department.class);
		IDomainObject<Department> domainObject = 
			(IDomainObject<Department>)getSession().createTransient(domainClass);
		domainObject.persist();
		try {
			domainObject.persist();
			fail("IllegalStateException should have been thrown.");
		} catch(IllegalStateException ex) {
			// expected
		}
	}


	public void testCanSetAttribute() {
		IDomainClass<Department> domainClass = 
			metaModel.register(Department.class);
		IDomainObject<Department> domainObject = 
			(IDomainObject<Department>)getSession().createTransient(domainClass);
		EAttribute nameAttribute = domainObject.getEAttributeNamed("name");
		domainObject.set(nameAttribute, "HR");
		assertEquals("HR", domainObject.getPojo().getName());
	}

	public void testCannotSetAttributeToInvalidValue() {
		IDomainClass<Department> domainClass = 
			metaModel.register(Department.class);
		IDomainObject<Department> domainObject = 
			(IDomainObject<Department>)getSession().createTransient(domainClass);
		EAttribute nameAttribute = domainObject.getEAttributeNamed("name");
		try {
			domainObject.set(nameAttribute, new Integer(1));
			fail("Expected IllegalArgumentException to have been thrown");
		} catch(IllegalArgumentException ex) {
			// expected.
		}
	}

	public void testCanGetAttribute() {
		IDomainClass<Department> domainClass = 
			metaModel.register(Department.class);
		IDomainObject<Department> domainObject = 
			(IDomainObject<Department>)getSession().createTransient(domainClass);
		domainObject.getPojo().setName("HR");
		EAttribute nameAttribute = domainObject.getEAttributeNamed("name");
		String value = (String)domainObject.get(nameAttribute);
		assertEquals("HR", value);
	}
	

}
