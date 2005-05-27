package de.berlios.rcpviewer.session;

import de.berlios.rcpviewer.AbstractTestCase;

import de.berlios.rcpviewer.domain.Domain;
import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.domain.IRuntimeDomainClass;

public class TestDomainObjectPersist extends AbstractTestCase  {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}


	
	public void testCanPersistThroughDomainObject() {
		IRuntimeDomainClass<Department> domainClass = 
			Domain.lookupAny(Department.class);
		
		IDomainObject<Department> domainObject = 
			(IDomainObject<Department>)session.createTransient(domainClass);
		domainObject.persist();
		assertTrue(domainObject.isPersistent());
	}

	/**
	 * Depending upon the programming model, the pojo may request that it is
	 * persisted.
	 * 
	 * <p>
	 * The means for doing this will be dependent on the programming model.
	 * In the standard programming model, we require that the Session is
	 * asked to persist. 
	 * 
	 * <p>
	 * TODO: the original design was to have an aspect pick up on the 'save'
	 * method.
	 */
	public void incompletetestCanPersistThroughPojo() {
		IRuntimeDomainClass<Department> domainClass = 
			Domain.lookupAny(Department.class);
		
		IDomainObject<Department> domainObject = 
			(IDomainObject<Department>)session.createTransient(domainClass);
		session.persist(domainObject.getPojo());
		assertTrue(domainObject.isPersistent());
	}

	/**
	 * Create directly from DomainClass rather than from Session. 
	 */
	public void testCannotPersistIfNotAttachedToSession() {
		IRuntimeDomainClass<Department> domainClass = 
			Domain.lookupAny(Department.class);
		
		IDomainObject<Department> domainObject = domainClass.createTransient();
		assertFalse(session.isAttached(domainObject));
		try {
			domainObject.persist();
			fail("IllegalStateException should have been thrown.");
		} catch(IllegalStateException ex) {
			// expected
		}
	}


	public void testCannotPersistMoreThanOnce() {
		IRuntimeDomainClass<Department> domainClass = 
			Domain.lookupAny(Department.class);
		
		IDomainObject<Department> domainObject = 
			(IDomainObject<Department>)session.createTransient(domainClass);
		domainObject.persist();
		try {
			domainObject.persist();
			fail("IllegalStateException should have been thrown.");
		} catch(IllegalStateException ex) {
			// expected
		}
	}


}
