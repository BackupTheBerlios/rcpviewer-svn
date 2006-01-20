package org.essentialplatform.runtime.shared.tests.domain.handle;

import org.essentialplatform.core.deployment.IDomainClassBinding;
import org.essentialplatform.core.domain.Domain;
import org.essentialplatform.core.domain.DomainConstants;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.fixture.progmodel.essential.extended.CustomerWithSimpleIdFirst;
import org.essentialplatform.runtime.shared.domain.Handle;
import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.domain.handle.CompositeIdHandleAssigner;
import org.essentialplatform.runtime.shared.domain.handle.HandleMap;
import org.essentialplatform.runtime.shared.session.SessionBinding;
import org.essentialplatform.runtime.shared.tests.AbstractRuntimeClientTestCase;

public class TestHandleMap extends AbstractRuntimeClientTestCase {

	private HandleMap handleMap;
	
	private IDomainClass domainClass;
	private IDomainObject<?> dobj;
	

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		handleMap = new HandleMap(new SessionBinding(DomainConstants.DEFAULT_NAME, "bar"));
		domainClass = lookupAny(Department.class);
	}

	@Override
	protected void tearDown() throws Exception {
		domainClass = null;
		handleMap = null;
		super.tearDown();
	}

	public void testAddWhenNotWithin() {
		dobj = clientSession.create(domainClass);
		dobj.assignHandle(new Handle(Department.class, 1));
		assertTrue(handleMap.add(dobj));
	}

	public void testAddWhenAlreadyWithin() {
		dobj = clientSession.create(domainClass);
		dobj.assignHandle(new Handle(Department.class, 1));
		handleMap.add(dobj);
		assertFalse(handleMap.add(dobj));
	}

	public void testAddWhenAlreadyWithinWithADifferentHandle() {
		dobj = clientSession.create(domainClass);
		dobj.assignHandle(new Handle(Department.class, 1));
		handleMap.add(dobj);
		// change the handle
		dobj.assignHandle(new Handle(Department.class, 2));
		try {
			handleMap.add(dobj);
			fail("IllegalStateException should have been thrown.");
		} catch(IllegalStateException ex) {
			//expected
		}
	}

	public void testGetDomainObjectWhenNotWithin() {
		Handle handle = new Handle(Department.class, 1);
		assertNull(handleMap.getDomainObject(handle));
	}

	public void testGetDomainObjectWhenWithin() {
		// first add
		dobj = clientSession.create(domainClass);
		dobj.assignHandle(new Handle(Department.class, 1));
		handleMap.add(dobj);
		
		// now get it
		assertSame(dobj, handleMap.getDomainObject(new Handle(Department.class, 1)));
	}

	public void testGetHandleWhenWithin() {
		// first add it
		dobj = clientSession.create(domainClass);
		dobj.assignHandle(new Handle(Department.class, 1));
		handleMap.add(dobj);
		
		// now get it
		assertEquals(new Handle(Department.class, 1), handleMap.getHandle(dobj));
	}

	public void testGetHandleWhenNotWithin() {
		dobj = clientSession.create(domainClass);
		assertNull(handleMap.getHandle(dobj));
	}

	public void testGetDomainObjectWhenHandleHasPreviousAndWithin() {
		dobj = clientSession.create(domainClass);
		Handle handle = new Handle(Department.class, 1);
		dobj.assignHandle(handle);

		// add the object first
		handleMap.add(dobj);
		
		// we must update a different handle, otherwise
		// aliasing issues (this simulates intended actual usage)
		Handle handle1 = new Handle(Department.class, 1);
		assertNotNull(handleMap.getDomainObject(handle1)); // just confirm it's there  
		Handle handle2 = new Handle(Department.class, 2);
		assertNull(handleMap.getDomainObject(handle2)); // just confirm not there  
		
		// now update handle1 so that it has previous
		handle1.update(2);
		assertEquals(new Handle(Department.class, 1), handle1.getPrevious());

		IDomainObject dobj2 = handleMap.getDomainObject(handle1);
		assertSame(dobj, dobj2);
		assertEquals(handle2, dobj.getHandle());
	}

	public void testGetDomainObjectWhenHandleHasPreviousAndNotWithin() {
		dobj = clientSession.create(domainClass);
		Handle handle = new Handle(Department.class, 1);
		dobj.assignHandle(handle);
		
		// we DON'T add the object to the hash (cf previous test)
		
		// we must update a different handle, otherwise
		// aliasing issues (this simulates intended actual usage)
		Handle handle1 = new Handle(Department.class, 1);
		
		// now update handle1 so that it has previous
		handle1.update(2);
		assertEquals(new Handle(Department.class, 1), handle1.getPrevious());

		// nevertheless, the object isn't in the hash.  Duh
		assertNull(handleMap.getDomainObject(handle1));
	}

	public void testRemoveByDomainObjectWhenNotWithin() {
		dobj = clientSession.create(domainClass);
		Handle handle = new Handle(Department.class, 1);
		dobj.assignHandle(handle);
		
		// we don't add the object to the hash
		assertFalse(handleMap.remove(dobj));
	}

	public void testRemoveByDomainObjectWhenWithin() {
		dobj = clientSession.create(domainClass);
		Handle handle = new Handle(Department.class, 1);
		dobj.assignHandle(handle);
		handleMap.add(dobj);
		
		assertTrue(handleMap.remove(dobj));
	}

	public void testRemoveByHandleWhenNotWithin() {
		assertFalse(handleMap.remove(new Handle(Department.class, 1)));
	}

	public void testRemoveByHandleWhenWithin() {
		dobj = clientSession.create(domainClass);
		dobj.assignHandle(new Handle(Department.class, 1));
		handleMap.add(dobj);
		
		assertTrue(handleMap.remove(new Handle(Department.class, 1)));
	}

	public void testRemoveByHandleWhenHasPrevious() {
		Handle handle = new Handle(Department.class, 1);
		handle.update(2);
		try {
			handleMap.remove(handle);
			fail("IllegalArgumentException should have been thrown");
		} catch(IllegalArgumentException ex) {
			// ok
		}
	}

	public void testAddFailsForDomainObjectWithNoHandle() {
		try {
			dobj = clientSession.create(domainClass);
			dobj.assignHandle(null);
			handleMap.add(dobj);
			fail("IllegalArgumentException should have been thrown");
		} catch(IllegalArgumentException ex) {
			// expected
		}
	}

	public void testRemoveByDomainObjectFailsForDomainObjectWithNoHandle() {
		try {
			dobj = clientSession.create(domainClass);
			dobj.assignHandle(new Handle(Department.class, 1));
			handleMap.add(dobj);
			
			dobj.assignHandle(null);
			handleMap.remove(dobj);
			fail("IllegalArgumentException should have been thrown");
		} catch(IllegalArgumentException ex) {
			// expected
		}
	}
	
	public void testHandlesWhenEmpty() {
		assertEquals(0, handleMap.handles().size());
	}

	public void testHandlesWhenSome() {
		dobj = clientSession.create(domainClass);
		dobj.assignHandle(new Handle(Department.class, 1));
		handleMap.add(dobj);
		
		dobj = clientSession.create(domainClass);
		dobj.assignHandle(new Handle(Department.class, 2));
		handleMap.add(dobj);
		
		dobj = clientSession.create(domainClass);
		dobj.assignHandle(new Handle(Department.class, 3));
		handleMap.add(dobj);
		
		int i = 0;
		for(Handle dummy: handleMap.handles()) {
			i++;
		}
		assertEquals(3, i);
	}

	public void testHandlesAttemptToModify() {
		try {
			handleMap.handles().add(new Handle(Department.class, 4));
			fail("UnsupportedOperationException should have been thrown.");
		} catch(UnsupportedOperationException ex) {
			// expected
		}
	}

}
