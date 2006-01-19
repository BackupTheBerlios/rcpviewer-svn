package org.essentialplatform.runtime.shared.tests.domain.handle;

import org.essentialplatform.core.deployment.IDomainClassBinding;
import org.essentialplatform.core.domain.Domain;
import org.essentialplatform.core.domain.DomainConstants;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.fixture.progmodel.essential.extended.CustomerWithSimpleIdFirst;
import org.essentialplatform.runtime.shared.domain.Handle;
import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.domain.handle.CompositeIdHandleAssigner;
import org.essentialplatform.runtime.shared.domain.handle.DefaultDomainObjectFactory;
import org.essentialplatform.runtime.shared.domain.handle.AutoAddingHandleMap;
import org.essentialplatform.runtime.shared.domain.handle.GuidHandleAssigner;
import org.essentialplatform.runtime.shared.domain.handle.HandleMap;
import org.essentialplatform.runtime.shared.domain.handle.IDomainObjectFactory;
import org.essentialplatform.runtime.shared.domain.handle.IHandleAssigner;
import org.essentialplatform.runtime.shared.domain.handle.IHandleMap;
import org.essentialplatform.runtime.shared.persistence.IPersistable.PersistState;
import org.essentialplatform.runtime.shared.persistence.IResolvable.ResolveState;
import org.essentialplatform.runtime.shared.session.SessionBinding;
import org.essentialplatform.runtime.shared.tests.AbstractRuntimeClientTestCase;

public class TestAutoAddingHandleMap extends AbstractRuntimeClientTestCase {

	private IHandleMap underlyingHandleMap;
	private AutoAddingHandleMap autoAddingHandleMap;
	private IDomainObjectFactory domainObjectFactory;
	private IHandleAssigner handleAssigner;
	
	private IDomainClass domainClass;
	private IDomainObject<?> dobj;
	

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		SessionBinding sessionBinding = new SessionBinding(DomainConstants.DEFAULT_NAME, "bar");
		handleAssigner = new GuidHandleAssigner();
		domainObjectFactory = new DefaultDomainObjectFactory(null, PersistState.TRANSIENT, ResolveState.UNRESOLVED, handleAssigner);
		underlyingHandleMap = new HandleMap(sessionBinding);
		autoAddingHandleMap = new AutoAddingHandleMap(underlyingHandleMap, domainObjectFactory);
		domainClass = lookupAny(Department.class);
	}

	@Override
	protected void tearDown() throws Exception {
		domainObjectFactory = null;
		handleAssigner = null;
		underlyingHandleMap = null;
		autoAddingHandleMap = null;
		domainClass = null;
		super.tearDown();
	}

	public void testAddWhenNotWithinUnderlying() {
		dobj = clientSession.create(domainClass);
		Handle handle = new Handle(Department.class, 1);
		dobj.assignHandle(handle);
		
		assertTrue(autoAddingHandleMap.add(dobj));
		
		IHandleMap deltas = autoAddingHandleMap.getAdditions();
		assertEquals(1, deltas.handles().size());
		assertSame(dobj, deltas.getDomainObject(handle));
	}

	public void testAddWhenAlreadyWithinUnderlying() {
		dobj = clientSession.create(domainClass);
		dobj.assignHandle(new Handle(Department.class, 1));
		underlyingHandleMap.add(dobj);
		
		assertFalse(autoAddingHandleMap.add(dobj));
		
		IHandleMap deltas = autoAddingHandleMap.getAdditions();
		assertEquals(0, deltas.handles().size());
	}

	public void testGetDomainObjectWhenNotWithinUnderlying() {
		Handle handle = new Handle(Department.class, 1);
		assertNull(underlyingHandleMap.getDomainObject(handle));
		
		IDomainObject domainObject = autoAddingHandleMap.getDomainObject(handle);
		assertNotNull(domainObject);
		assertEquals(handle, domainObject.getHandle());
		assertEquals(domainObjectFactory.getSessionBinding(), domainObject.getSessionBinding());
		assertEquals(domainObjectFactory.getInitialPersistState(), domainObject.getPersistState());
		assertEquals(domainObjectFactory.getInitialResolveState(), domainObject.getResolveState());
		
		IHandleMap deltas = autoAddingHandleMap.getAdditions();
		assertEquals(1, deltas.handles().size());
		assertEquals(handle, deltas.handles().iterator().next());
	}

	public void testGetDomainObjectWhenWithinUnderlying() {
		// first add
		dobj = clientSession.create(domainClass);
		dobj.assignHandle(new Handle(Department.class, 1));
		underlyingHandleMap.add(dobj);
		
		// now get it via deltaHashMap
		assertSame(dobj, autoAddingHandleMap.getDomainObject(new Handle(Department.class, 1)));

		// not added to the delta
		IHandleMap deltas = autoAddingHandleMap.getAdditions();
		assertEquals(0, deltas.handles().size());
	}

	public void testGetHandleWhenNotWithinUnderlying() {
		dobj = clientSession.create(domainClass);
		assertNull(underlyingHandleMap.getHandle(dobj));

		Handle handle = autoAddingHandleMap.getHandle(dobj);
		assertNotNull(handle);
		assertEquals(handle, dobj.getHandle());
		
		IHandleMap deltas = autoAddingHandleMap.getAdditions();
		assertEquals(1, deltas.handles().size());
		assertEquals(handle, deltas.handles().iterator().next());
	}


	public void testGetHandleWhenWithinUnderlying() {
		// first add it
		dobj = clientSession.create(domainClass);
		dobj.assignHandle(new Handle(Department.class, 1));
		underlyingHandleMap.add(dobj);
		
		// now get it
		assertEquals(new Handle(Department.class, 1), autoAddingHandleMap.getHandle(dobj));
		
		// not a delta
		IHandleMap deltas = autoAddingHandleMap.getAdditions();
		assertEquals(0, deltas.handles().size());
		
	}

}
