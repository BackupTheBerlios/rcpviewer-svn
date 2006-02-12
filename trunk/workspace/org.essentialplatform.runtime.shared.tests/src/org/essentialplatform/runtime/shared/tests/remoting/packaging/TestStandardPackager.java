package org.essentialplatform.runtime.shared.tests.remoting.packaging;

import java.io.CharArrayReader;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.easymock.MockControl;
import org.essentialplatform.core.deployment.Binding;
import org.essentialplatform.core.domain.DomainConstants;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.progmodel.essential.runtime.EssentialProgModelRuntimeBuilder;
import org.essentialplatform.runtime.client.domain.bindings.RuntimeClientBinding;
import org.essentialplatform.runtime.client.remoting.packaging.standard.StandardPackager;
import org.essentialplatform.runtime.client.transaction.ITransaction;
import org.essentialplatform.runtime.client.transaction.TransactionManager;
import org.essentialplatform.runtime.server.domain.bindings.RuntimeServerBinding;
import org.essentialplatform.runtime.server.session.IServerSession;
import org.essentialplatform.runtime.server.session.IServerSessionFactory;
import org.essentialplatform.runtime.server.session.noop.NoopServerSessionFactory;
import org.essentialplatform.runtime.shared.domain.Handle;
import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.domain.IPojo;
import org.essentialplatform.runtime.shared.domain.handle.DefaultDomainObjectFactory;
import org.essentialplatform.runtime.shared.domain.handle.AutoAddingHandleMap;
import org.essentialplatform.runtime.shared.domain.handle.GuidHandleAssigner;
import org.essentialplatform.runtime.shared.domain.handle.IHandleAssigner;
import org.essentialplatform.runtime.shared.domain.handle.IHandleMap;
import org.essentialplatform.runtime.shared.domain.handle.SequentialHandleAssigner;
import org.essentialplatform.runtime.shared.persistence.IPersistable.PersistState;
import org.essentialplatform.runtime.shared.persistence.IResolvable.ResolveState;
import org.essentialplatform.runtime.shared.remoting.marshalling.IMarshalling;
import org.essentialplatform.runtime.shared.remoting.marshalling.xstream.XStreamMarshalling;
import org.essentialplatform.runtime.shared.remoting.packaging.IPojoPackage;
import org.essentialplatform.runtime.shared.remoting.packaging.standard.StandardUnpackager;
import org.essentialplatform.runtime.shared.session.SessionBinding;
import org.essentialplatform.runtime.shared.tests.AbstractRuntimeClientTestCase;
import java.io.Serializable;
import java.util.Set;

/**
 * Note that this test subclasses from AbstractRuntimeClientTestCase which 
 * means that it automatically installs a client-side binding 
 * <pre> 
 * 	protected Binding getBinding() {
 *		return new RuntimeClientBinding(new EssentialProgModelRuntimeBuilder());
 *	}
 * </pre>
 * 
 * Therefore the server-side unpackaging resets the binding to simulate the
 * server-side bindings.
 * 
 * @author Dan Haywood
 */
public class TestStandardPackager extends AbstractRuntimeClientTestCase {

	private StandardPackager packager;
	private StandardUnpackager unpackager;
	private IMarshalling marshalling;

	// this stuff is commented out because it is testing at the wrong level
	// (end-to-end rather than just the packager).  Still, it might be useful
	// for other tests that do need to check out end-to-end functionality
	
//	private IServerSessionFactory serverSessionFactory;
//	private IServerSession serverSession;
//
//	/**
//	 * The default binding is client-side, so only needs to be called if
//	 * {@link #setServerSideBinding()} has been called previously.
//	 */
//	private RuntimeClientBinding setClientSideBinding() {
//		RuntimeClientBinding binding = new RuntimeClientBinding(new EssentialProgModelRuntimeBuilder());
//		Binding.reset();
//		Binding.setBinding(binding);
//		return binding;
//	}
//	private RuntimeServerBinding setServerSideBinding() {
//		RuntimeServerBinding binding = new RuntimeServerBinding(new EssentialProgModelRuntimeBuilder());
//		Binding.reset();
//		Binding.setBinding(binding);
//		return binding;
//	}


	public TestStandardPackager() {
		super(null);
	}
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		packager = new StandardPackager();
		unpackager = new StandardUnpackager();
		marshalling = new XStreamMarshalling();

		// not needed here, but commented out rather than deleted because
		// other end-to-end tests might want to copy it.
//		serverSessionFactory = new NoopServerSessionFactory();
//		serverSessionFactory.init(new SessionBinding(DomainConstants.DEFAULT_NAME, "bar")));
//		serverSession = serverSessionFactory.open();
		
		TransactionManager.instance().suspend();
	}


	@Override
	public void tearDown() throws Exception {
		// not needed here, but commented out rather than deleted because
		// other end-to-end tests might want to copy it.
//		serverSession.close();
//		serverSession = null;
//		serverSessionFactory = null;
		
		marshalling = null;
		unpackager = null;
		packager = null;
		TransactionManager.instance().resume();
		super.tearDown();
	}

	public void testPackage() {
		IDomainClass departmentDC = lookupAny(DepartmentAttributesOnly.class);
		
		IDomainObject<DepartmentAttributesOnly> departmentDO = clientSession.create(departmentDC);
		DepartmentAttributesOnly departmentPojo = departmentDO.getPojo();
		departmentPojo.setName("HR");
		departmentPojo.setRank(33);

		Object packedPojo = packager.pack(departmentPojo);

		String marshalledPackedPojo = marshalling.marshal(packedPojo);
	}

	public void testUnpackSessionBinding() {
		IDomainClass departmentDC = lookupAny(DepartmentAttributesOnly.class);
		
		IDomainObject<DepartmentAttributesOnly> departmentDO = clientSession.create(departmentDC);
		DepartmentAttributesOnly departmentPojo = departmentDO.getPojo();
		departmentPojo.setName("HR");
		departmentPojo.setRank(33);

		IPojoPackage packedPojo = packager.pack(departmentPojo);

		String marshalledPackedPojo = marshalling.marshal(packedPojo);
		IPojoPackage unmarshalledPackedPojo = (IPojoPackage) marshalling.unmarshal(marshalledPackedPojo);
		
		SessionBinding sessionBinding = unpackager.unpackSessionBinding(unmarshalledPackedPojo);
		assertNotSame(clientSession.getSessionBinding(), sessionBinding); // different object, but...
		assertEquals(clientSession.getSessionBinding(), sessionBinding); // ... same value.
		
	}

	public void testUnpackHandle() {
		IDomainClass departmentDC = lookupAny(DepartmentAttributesOnly.class);
		
		IDomainObject<DepartmentAttributesOnly> departmentDO = clientSession.create(departmentDC);
		DepartmentAttributesOnly departmentPojo = departmentDO.getPojo();
		departmentPojo.setName("HR");
		departmentPojo.setRank(33);

		IPojoPackage packedPojo = packager.pack(departmentPojo);

		String marshalledPackedPojo = marshalling.marshal(packedPojo);
		IPojoPackage unmarshalledPackedPojo = (IPojoPackage) marshalling.unmarshal(marshalledPackedPojo);
		
		Handle handle = unpackager.unpackHandle(unmarshalledPackedPojo);
		assertNotSame(departmentDO.getHandle(), handle); // different object, but...
		assertEquals(departmentDO.getHandle(), handle); // ... same value.
	}
	
	public void testMergeAttributeWhenChanged() {
		IDomainClass departmentDC = lookupAny(DepartmentAttributesOnly.class);
		
		IDomainObject<DepartmentAttributesOnly> departmentDO = clientSession.create(departmentDC);
		DepartmentAttributesOnly departmentPojo = departmentDO.getPojo();
		departmentPojo.setName("HR");
		departmentPojo.setRank(33);

		IPojoPackage packedPojo = packager.pack(departmentPojo);

		String marshalledPackedPojo = marshalling.marshal(packedPojo);
		IPojoPackage unmarshalledPackedPojo = (IPojoPackage) marshalling.unmarshal(marshalledPackedPojo);

		departmentPojo.setRank(34);
		assertEquals(34, departmentPojo.getRank()); // reset back
		unpackager.merge(departmentDO, unmarshalledPackedPojo, clientSession);
		assertEquals(33, departmentPojo.getRank()); // reset back
	}
	
	public void testMergeAttributeWhenChangedFromNullToValue() {
		IDomainClass departmentDC = lookupAny(DepartmentAttributesOnly.class);
		
		IDomainObject<DepartmentAttributesOnly> departmentDO = clientSession.create(departmentDC);
		DepartmentAttributesOnly departmentPojo = departmentDO.getPojo();
		departmentPojo.setName("HR");
		departmentPojo.setRank(33);

		IPojoPackage packedPojo = packager.pack(departmentPojo);

		String marshalledPackedPojo = marshalling.marshal(packedPojo);
		IPojoPackage unmarshalledPackedPojo = (IPojoPackage) marshalling.unmarshal(marshalledPackedPojo);

		departmentPojo.setName(null);
		assertNull(departmentPojo.getName()); // set to null
		unpackager.merge(departmentDO, unmarshalledPackedPojo, clientSession);
		assertEquals("HR", departmentPojo.getName()); // reset back
	}
	
	public void testMergeAttributeWhenChangedFromValueToNull() {
		IDomainClass departmentDC = lookupAny(DepartmentAttributesOnly.class);
		
		IDomainObject<DepartmentAttributesOnly> departmentDO = clientSession.create(departmentDC);
		DepartmentAttributesOnly departmentPojo = departmentDO.getPojo();
		departmentPojo.setName(null);
		departmentPojo.setRank(33);

		IPojoPackage packedPojo = packager.pack(departmentPojo);

		String marshalledPackedPojo = marshalling.marshal(packedPojo);
		IPojoPackage unmarshalledPackedPojo = (IPojoPackage) marshalling.unmarshal(marshalledPackedPojo);

		departmentPojo.setName("HR");
		assertEquals("HR", departmentPojo.getName());
		unpackager.merge(departmentDO, unmarshalledPackedPojo, clientSession);
		assertNull(departmentPojo.getName()); // reset back
	}
	
	public void testMergeAttributeWhenNotChanged() {
		IDomainClass departmentDC = lookupAny(DepartmentAttributesOnly.class);
		
		IDomainObject<DepartmentAttributesOnly> departmentDO = clientSession.create(departmentDC);
		DepartmentAttributesOnly departmentPojo = departmentDO.getPojo();
		departmentPojo.setName("HR");
		departmentPojo.setRank(33);

		IPojoPackage packedPojo = packager.pack(departmentPojo);

		String marshalledPackedPojo = marshalling.marshal(packedPojo);
		IPojoPackage unmarshalledPackedPojo = (IPojoPackage) marshalling.unmarshal(marshalledPackedPojo);

		unpackager.merge(departmentDO, unmarshalledPackedPojo, clientSession);
		assertEquals(33, departmentPojo.getRank()); // same
	}
	
	public void testMergeAttributeWhenNoChangeAndIsNull() {
		IDomainClass departmentDC = lookupAny(DepartmentAttributesOnly.class);
		
		IDomainObject<DepartmentAttributesOnly> departmentDO = clientSession.create(departmentDC);
		DepartmentAttributesOnly departmentPojo = departmentDO.getPojo();
		departmentPojo.setName(null);

		IPojoPackage packedPojo = packager.pack(departmentPojo);

		String marshalledPackedPojo = marshalling.marshal(packedPojo);
		IPojoPackage unmarshalledPackedPojo = (IPojoPackage) marshalling.unmarshal(marshalledPackedPojo);

		assertNull(departmentPojo.getName());
		unpackager.merge(departmentDO, unmarshalledPackedPojo, clientSession);
		assertNull(departmentPojo.getName()); // same
	}

	public void testMergeReferenceWhenNoChangeAndIsNull() {
		IDomainClass departmentDC = lookupAny(DepartmentReferencesOnly.class);
		IDomainClass cityDC = lookupAny(City.class);
		
		IDomainObject<DepartmentReferencesOnly> departmentDO = clientSession.create(departmentDC);
		DepartmentReferencesOnly departmentPojo = departmentDO.getPojo();

		assertNull(departmentPojo.getCity());

		IPojoPackage packedPojo = packager.pack(departmentPojo);

		String marshalledPackedPojo = marshalling.marshal(packedPojo);
		IPojoPackage unmarshalledPackedPojo = (IPojoPackage) marshalling.unmarshal(marshalledPackedPojo);

		assertNull(departmentPojo.getCity());
		
		SessionBinding sessionBinding = clientSession.getSessionBinding();
		IHandleAssigner handleAssigner = new GuidHandleAssigner();
		DefaultDomainObjectFactory domainObjectFactory = 
			new DefaultDomainObjectFactory(sessionBinding, PersistState.UNKNOWN, ResolveState.UNRESOLVED, handleAssigner);
		AutoAddingHandleMap autoAddingHandleMap = new AutoAddingHandleMap(clientSession, domainObjectFactory);
		
		unpackager.merge(departmentDO, unmarshalledPackedPojo, autoAddingHandleMap);
		assertNull(departmentPojo.getCity());
		
		assertEquals(0, autoAddingHandleMap.getAdditions().handles().size()); // nothing added
	}


	public void testMergeReferenceWhenNoChange() {
		IDomainClass departmentDC = lookupAny(DepartmentReferencesOnly.class);
		IDomainClass cityDC = lookupAny(City.class);
		
		IDomainObject<DepartmentReferencesOnly> departmentDO = clientSession.create(departmentDC);
		DepartmentReferencesOnly departmentPojo = departmentDO.getPojo();

		// create 1 cities
		IDomainObject<City> londonDO = clientSession.create(cityDC);
		City londonPojo = londonDO.getPojo();
		londonPojo.setName("London");

		departmentPojo.associateCity(londonPojo); // associate with london

		IPojoPackage packedPojo = packager.pack(departmentPojo);

		String marshalledPackedPojo = marshalling.marshal(packedPojo);
		IPojoPackage unmarshalledPackedPojo = (IPojoPackage) marshalling.unmarshal(marshalledPackedPojo);

		SessionBinding sessionBinding = clientSession.getSessionBinding();
		IHandleAssigner handleAssigner = new GuidHandleAssigner();
		DefaultDomainObjectFactory domainObjectFactory = 
			new DefaultDomainObjectFactory(sessionBinding, PersistState.UNKNOWN, ResolveState.UNRESOLVED, handleAssigner);
		AutoAddingHandleMap autoAddingHandleMap = new AutoAddingHandleMap(clientSession, domainObjectFactory);
		
		unpackager.merge(departmentDO, unmarshalledPackedPojo, autoAddingHandleMap);
		assertEquals(londonPojo, departmentPojo.getCity()); // no change
		
		assertEquals(0, autoAddingHandleMap.getAdditions().handles().size()); // nothing added, since was backed by client session.
	}

	public void testMergeReferenceChangedKnownToKnown() {
		IDomainClass departmentDC = lookupAny(DepartmentReferencesOnly.class);
		IDomainClass cityDC = lookupAny(City.class);
		
		IDomainObject<DepartmentReferencesOnly> departmentDO = clientSession.create(departmentDC);
		DepartmentReferencesOnly departmentPojo = departmentDO.getPojo();

		// create 2 cities
		IDomainObject<City> londonDO = clientSession.create(cityDC);
		City londonPojo = londonDO.getPojo();
		londonPojo.setName("London");

		IDomainObject<City> oxfordDO = clientSession.create(cityDC);
		City oxfordPojo = oxfordDO.getPojo();
		oxfordPojo.setName("Oxford");

		departmentPojo.associateCity(londonPojo); // associate with london

		IPojoPackage packedPojo = packager.pack(departmentPojo);

		String marshalledPackedPojo = marshalling.marshal(packedPojo);
		IPojoPackage unmarshalledPackedPojo = (IPojoPackage) marshalling.unmarshal(marshalledPackedPojo);

		departmentPojo.associateCity(oxfordPojo); // change to oxford
		
		SessionBinding sessionBinding = clientSession.getSessionBinding();
		IHandleAssigner handleAssigner = new GuidHandleAssigner();
		DefaultDomainObjectFactory domainObjectFactory = 
			new DefaultDomainObjectFactory(sessionBinding, PersistState.UNKNOWN, ResolveState.UNRESOLVED, handleAssigner);
		AutoAddingHandleMap autoAddingHandleMap = new AutoAddingHandleMap(clientSession, domainObjectFactory);
		
		unpackager.merge(departmentDO, unmarshalledPackedPojo, autoAddingHandleMap);
		assertEquals(londonPojo, departmentPojo.getCity()); // reset back
		
		assertEquals(0, autoAddingHandleMap.getAdditions().handles().size()); // nothing added, since was backed by client session.
	}
	
	/**
	 * Ie from known -> null
	 *
	 */
	public void testMergeReferenceWhenDissociated() {
		IDomainClass departmentDC = lookupAny(DepartmentReferencesOnly.class);
		IDomainClass cityDC = lookupAny(City.class);
		
		IDomainObject<DepartmentReferencesOnly> departmentDO = clientSession.create(departmentDC);
		DepartmentReferencesOnly departmentPojo = departmentDO.getPojo();

		IPojoPackage packedPojo = packager.pack(departmentPojo);

		String marshalledPackedPojo = marshalling.marshal(packedPojo);
		IPojoPackage unmarshalledPackedPojo = (IPojoPackage) marshalling.unmarshal(marshalledPackedPojo);

		IDomainObject<City> londonDO = clientSession.create(cityDC);
		City londonPojo = londonDO.getPojo();
		londonPojo.setName("London");

		departmentPojo.associateCity(londonPojo); // change to known
		
		SessionBinding sessionBinding = clientSession.getSessionBinding();
		IHandleAssigner handleAssigner = new GuidHandleAssigner();
		DefaultDomainObjectFactory domainObjectFactory = 
			new DefaultDomainObjectFactory(sessionBinding, PersistState.UNKNOWN, ResolveState.UNRESOLVED, handleAssigner);
		AutoAddingHandleMap autoAddingHandleMap = new AutoAddingHandleMap(clientSession, domainObjectFactory);
		
		unpackager.merge(departmentDO, unmarshalledPackedPojo, autoAddingHandleMap);
		assertNull(departmentPojo.getCity()); // reset back to null
		
		assertEquals(0, autoAddingHandleMap.getAdditions().handles().size()); // nothing added
	}

	/**
	 * Ie from null -> known
	 *
	 */
	public void testMergeReferenceWhenAssociatedToKnown() {
		IDomainClass departmentDC = lookupAny(DepartmentReferencesOnly.class);
		IDomainClass cityDC = lookupAny(City.class);
		
		IDomainObject<DepartmentReferencesOnly> departmentDO = clientSession.create(departmentDC);
		DepartmentReferencesOnly departmentPojo = departmentDO.getPojo();
		
		IDomainObject<City> londonDO = clientSession.create(cityDC);
		City londonPojo = londonDO.getPojo();
		londonPojo.setName("London");

		departmentPojo.associateCity(londonPojo);

		IPojoPackage packedPojo = packager.pack(departmentPojo);

		String marshalledPackedPojo = marshalling.marshal(packedPojo);
		IPojoPackage unmarshalledPackedPojo = (IPojoPackage) marshalling.unmarshal(marshalledPackedPojo);

		departmentPojo.dissociateCity(null); // change to null
		
		SessionBinding sessionBinding = clientSession.getSessionBinding();
		IHandleAssigner handleAssigner = new GuidHandleAssigner();
		DefaultDomainObjectFactory domainObjectFactory = 
			new DefaultDomainObjectFactory(sessionBinding, PersistState.UNKNOWN, ResolveState.UNRESOLVED, handleAssigner);
		AutoAddingHandleMap autoAddingHandleMap = new AutoAddingHandleMap(clientSession, domainObjectFactory);
		
		unpackager.merge(departmentDO, unmarshalledPackedPojo, autoAddingHandleMap);
		assertEquals(londonPojo, departmentPojo.getCity()); // reset back
		
		assertEquals(0, autoAddingHandleMap.getAdditions().handles().size()); // nothing added, since was backed by client session.
	}
	/**
	 * Ie from null -> unknown
	 *
	 */
	public void testMergeReferenceWhenAssociatedToUnknown() {
		IDomainClass departmentDC = lookupAny(DepartmentReferencesOnly.class);
		IDomainClass cityDC = lookupAny(City.class);
		
		IDomainObject<DepartmentReferencesOnly> departmentDO = clientSession.create(departmentDC);
		DepartmentReferencesOnly departmentPojo = departmentDO.getPojo();
		
		IDomainObject<City> londonDO = clientSession.create(cityDC);
		City londonPojo = londonDO.getPojo();
		londonPojo.setName("London");

		// remove london from the clientSession
		assertNotNull(clientSession.getDomainObject(londonDO.getHandle()));
		clientSession.detach(londonDO);
		assertNull(clientSession.getDomainObject(londonDO.getHandle()));

		departmentPojo.associateCity(londonPojo);

		IPojoPackage packedPojo = packager.pack(departmentPojo);

		String marshalledPackedPojo = marshalling.marshal(packedPojo);
		IPojoPackage unmarshalledPackedPojo = (IPojoPackage) marshalling.unmarshal(marshalledPackedPojo);

		departmentPojo.dissociateCity(null); // change
		
		SessionBinding sessionBinding = clientSession.getSessionBinding();
		IHandleAssigner handleAssigner = new GuidHandleAssigner();
		DefaultDomainObjectFactory domainObjectFactory = 
			new DefaultDomainObjectFactory(sessionBinding, PersistState.UNKNOWN, ResolveState.UNRESOLVED, handleAssigner);
		AutoAddingHandleMap autoAddingHandleMap = new AutoAddingHandleMap(clientSession, domainObjectFactory);
		
		unpackager.merge(departmentDO, unmarshalledPackedPojo, autoAddingHandleMap);
		// the department should now references a city whose DO has the same handle as that for london Pojo
		final IDomainObject updatedReferenceDO = ((IPojo)departmentPojo.getCity()).domainObject();
		assertEquals(londonDO.getHandle(), updatedReferenceDO.getHandle()); // set back.
		
		// new domain object for london should have been added
		final Set<Handle> addedHandles = autoAddingHandleMap.getAdditions().handles();
		assertEquals(1, addedHandles.size());
		Handle addedHandle = addedHandles.iterator().next();
		assertFalse(londonDO.getHandle() == addedHandle); 
		assertEquals(londonDO.getHandle(), (addedHandle)); // same value as london
		final IDomainObject addedDomainObject = autoAddingHandleMap.getDomainObject(addedHandle);
		assertSame(updatedReferenceDO, addedDomainObject);
		assertEquals(ResolveState.UNRESOLVED, addedDomainObject.getResolveState()); // tested elsewhere too
		assertEquals(PersistState.UNKNOWN, addedDomainObject.getPersistState()); // tested elsewhere too
	}

	public void testMergeCollectionWhenNoChangeAndIsEmpty() {
		IDomainClass departmentDC = lookupAny(DepartmentCollectionsOnly.class);
		IDomainClass employeeDC = lookupAny(Employee.class);
		
		IDomainObject<DepartmentCollectionsOnly> departmentDO = clientSession.create(departmentDC);
		DepartmentCollectionsOnly departmentPojo = departmentDO.getPojo();

		IPojoPackage packedPojo = packager.pack(departmentPojo);

		String marshalledPackedPojo = marshalling.marshal(packedPojo);
		IPojoPackage unmarshalledPackedPojo = (IPojoPackage) marshalling.unmarshal(marshalledPackedPojo);

		SessionBinding sessionBinding = clientSession.getSessionBinding();
		IHandleAssigner handleAssigner = new GuidHandleAssigner();
		DefaultDomainObjectFactory domainObjectFactory = 
			new DefaultDomainObjectFactory(sessionBinding, PersistState.UNKNOWN, ResolveState.UNRESOLVED, handleAssigner);
		AutoAddingHandleMap autoAddingHandleMap = new AutoAddingHandleMap(clientSession, domainObjectFactory);
		
		assertEquals(0, departmentPojo.getEmployees().size());
		unpackager.merge(departmentDO, unmarshalledPackedPojo, autoAddingHandleMap);
		// the department should now references the employee
		assertEquals(0, departmentPojo.getEmployees().size());
	}



	public void testMergeCollectionWhenNoChange() {
		IDomainClass departmentDC = lookupAny(DepartmentCollectionsOnly.class);
		IDomainClass employeeDC = lookupAny(Employee.class);
		
		IDomainObject<DepartmentCollectionsOnly> departmentDO = clientSession.create(departmentDC);
		DepartmentCollectionsOnly departmentPojo = departmentDO.getPojo();
		
		IDomainObject<Employee> joeEmployeeDO = clientSession.create(employeeDC);
		Employee joeEmployeePojo = joeEmployeeDO.getPojo();
		joeEmployeePojo.setFirstName("Joe");

		IDomainObject<Employee> maryEmployeeDO = clientSession.create(employeeDC);
		Employee maryEmployeePojo = maryEmployeeDO.getPojo();
		maryEmployeePojo.setFirstName("Mary");

		// add joe and mary to employees collection
		departmentPojo.addToEmployees(joeEmployeePojo);
		departmentPojo.addToEmployees(maryEmployeePojo); 
		
		IPojoPackage packedPojo = packager.pack(departmentPojo);

		String marshalledPackedPojo = marshalling.marshal(packedPojo);
		IPojoPackage unmarshalledPackedPojo = (IPojoPackage) marshalling.unmarshal(marshalledPackedPojo);
		
		SessionBinding sessionBinding = clientSession.getSessionBinding();
		IHandleAssigner handleAssigner = new GuidHandleAssigner();
		DefaultDomainObjectFactory domainObjectFactory = 
			new DefaultDomainObjectFactory(sessionBinding, PersistState.UNKNOWN, ResolveState.UNRESOLVED, handleAssigner);
		AutoAddingHandleMap autoAddingHandleMap = new AutoAddingHandleMap(clientSession, domainObjectFactory);
		
		assertEquals(2, departmentPojo.getEmployees().size());
		unpackager.merge(departmentDO, unmarshalledPackedPojo, autoAddingHandleMap);
		assertEquals(2, departmentPojo.getEmployees().size());
		
		final Set<Handle> addedHandles = autoAddingHandleMap.getAdditions().handles();
		assertEquals(0, addedHandles.size()); // no change
	}

	public void testMergeCollectionWhenAddedWithKnown() {
		IDomainClass departmentDC = lookupAny(DepartmentCollectionsOnly.class);
		IDomainClass employeeDC = lookupAny(Employee.class);
		
		IDomainObject<DepartmentCollectionsOnly> departmentDO = clientSession.create(departmentDC);
		DepartmentCollectionsOnly departmentPojo = departmentDO.getPojo();
		
		IDomainObject<Employee> maryEmployeeDO = clientSession.create(employeeDC);
		Employee maryEmployeePojo = maryEmployeeDO.getPojo();
		maryEmployeePojo.setFirstName("Mary");

		departmentPojo.addToEmployees(maryEmployeePojo);

		IPojoPackage packedPojo = packager.pack(departmentPojo);

		String marshalledPackedPojo = marshalling.marshal(packedPojo);
		IPojoPackage unmarshalledPackedPojo = (IPojoPackage) marshalling.unmarshal(marshalledPackedPojo);

		departmentPojo.removeFromEmployees(maryEmployeePojo); // remove employee.
		
		SessionBinding sessionBinding = clientSession.getSessionBinding();
		IHandleAssigner handleAssigner = new GuidHandleAssigner();
		DefaultDomainObjectFactory domainObjectFactory = 
			new DefaultDomainObjectFactory(sessionBinding, PersistState.UNKNOWN, ResolveState.UNRESOLVED, handleAssigner);
		AutoAddingHandleMap autoAddingHandleMap = new AutoAddingHandleMap(clientSession, domainObjectFactory);
		
		assertEquals(0, departmentPojo.getEmployees().size());
		unpackager.merge(departmentDO, unmarshalledPackedPojo, autoAddingHandleMap);
		// the department should now references the employee
		assertEquals(1, departmentPojo.getEmployees().size());
		final Employee newlyReferencedEmployeePojo = departmentPojo.getEmployees().iterator().next();
		assertSame(maryEmployeePojo, newlyReferencedEmployeePojo);
		
		assertEquals(0, autoAddingHandleMap.getAdditions().handles().size()); // none added since already known.
	}
	
	public void testMergeCollectionWhenAddedWithUnknown() {
		IDomainClass departmentDC = lookupAny(DepartmentCollectionsOnly.class);
		IDomainClass employeeDC = lookupAny(Employee.class);
		
		IDomainObject<DepartmentCollectionsOnly> departmentDO = clientSession.create(departmentDC);
		DepartmentCollectionsOnly departmentPojo = departmentDO.getPojo();
		
		IDomainObject<Employee> joeEmployeeDO = clientSession.create(employeeDC);
		Employee joeEmployeePojo = joeEmployeeDO.getPojo();
		joeEmployeePojo.setFirstName("Joe");

		IDomainObject<Employee> maryEmployeeDO = clientSession.create(employeeDC);
		Employee maryEmployeePojo = maryEmployeeDO.getPojo();
		maryEmployeePojo.setFirstName("Mary");

		// add both joe and mary to employees collection
		departmentPojo.addToEmployees(joeEmployeePojo);
		departmentPojo.addToEmployees(maryEmployeePojo);
		
		// remove mary from client session, so will be unknown
		assertEquals(3, clientSession.handles().size()); // dept + 2 employees
		clientSession.detach(maryEmployeeDO); 
		assertEquals(2, clientSession.handles().size());

		IPojoPackage packedPojo = packager.pack(departmentPojo);

		String marshalledPackedPojo = marshalling.marshal(packedPojo);
		IPojoPackage unmarshalledPackedPojo = (IPojoPackage) marshalling.unmarshal(marshalledPackedPojo);

		departmentPojo.removeFromEmployees(maryEmployeePojo); // remove employee.
		
		SessionBinding sessionBinding = clientSession.getSessionBinding();
		IHandleAssigner handleAssigner = new GuidHandleAssigner();
		DefaultDomainObjectFactory domainObjectFactory = 
			new DefaultDomainObjectFactory(sessionBinding, PersistState.UNKNOWN, ResolveState.UNRESOLVED, handleAssigner);
		AutoAddingHandleMap autoAddingHandleMap = new AutoAddingHandleMap(clientSession, domainObjectFactory);
		
		assertEquals(1, departmentPojo.getEmployees().size());
		unpackager.merge(departmentDO, unmarshalledPackedPojo, autoAddingHandleMap);
		// the department should now references the employee
		assertEquals(2, departmentPojo.getEmployees().size());
		
		final Set<Handle> addedHandles = autoAddingHandleMap.getAdditions().handles();
		assertEquals(1, addedHandles.size()); // added since not known.
		final Handle addedHandle = addedHandles.iterator().next();
		assertFalse(maryEmployeeDO.getHandle() == addedHandle);
		assertEquals(maryEmployeeDO.getHandle(), addedHandle);
		
	}
	
	/**
	 * removed a handle that was known.
	 */
	public void testMergeCollectionWhenRemovedKnown() {
		IDomainClass departmentDC = lookupAny(DepartmentCollectionsOnly.class);
		IDomainClass employeeDC = lookupAny(Employee.class);
		
		IDomainObject<DepartmentCollectionsOnly> departmentDO = clientSession.create(departmentDC);
		DepartmentCollectionsOnly departmentPojo = departmentDO.getPojo();
		
		IDomainObject<Employee> joeEmployeeDO = clientSession.create(employeeDC);
		Employee joeEmployeePojo = joeEmployeeDO.getPojo();
		joeEmployeePojo.setFirstName("Joe");

		IDomainObject<Employee> maryEmployeeDO = clientSession.create(employeeDC);
		Employee maryEmployeePojo = maryEmployeeDO.getPojo();
		maryEmployeePojo.setFirstName("Mary");

		// add just joe to employees collection
		departmentPojo.addToEmployees(joeEmployeePojo);
		
		IPojoPackage packedPojo = packager.pack(departmentPojo);

		String marshalledPackedPojo = marshalling.marshal(packedPojo);
		IPojoPackage unmarshalledPackedPojo = (IPojoPackage) marshalling.unmarshal(marshalledPackedPojo);

		departmentPojo.addToEmployees(maryEmployeePojo); // add an employee
		
		SessionBinding sessionBinding = clientSession.getSessionBinding();
		IHandleAssigner handleAssigner = new GuidHandleAssigner();
		DefaultDomainObjectFactory domainObjectFactory = 
			new DefaultDomainObjectFactory(sessionBinding, PersistState.UNKNOWN, ResolveState.UNRESOLVED, handleAssigner);
		AutoAddingHandleMap autoAddingHandleMap = new AutoAddingHandleMap(clientSession, domainObjectFactory);
		
		assertEquals(2, departmentPojo.getEmployees().size());
		assertTrue(departmentPojo.getEmployees().contains(maryEmployeePojo));
		unpackager.merge(departmentDO, unmarshalledPackedPojo, autoAddingHandleMap);
		// the department should no longer references the employee
		assertEquals(1, departmentPojo.getEmployees().size());
		assertFalse(departmentPojo.getEmployees().contains(maryEmployeePojo));
		
		final Set<Handle> addedHandles = autoAddingHandleMap.getAdditions().handles();
		assertEquals(0, addedHandles.size()); // nothing added
	}

	
	/**
	 * removed a handle that was not known.
	 * 
	 * <p>
	 * Does this make sense, though?  Perhaps for client side, but not for server. 
	 */
	public void testMergeCollectionWhenRemovedUnknown() {
		IDomainClass departmentDC = lookupAny(DepartmentCollectionsOnly.class);
		IDomainClass employeeDC = lookupAny(Employee.class);
		
		IDomainObject<DepartmentCollectionsOnly> departmentDO = clientSession.create(departmentDC);
		DepartmentCollectionsOnly departmentPojo = departmentDO.getPojo();
		
		IDomainObject<Employee> joeEmployeeDO = clientSession.create(employeeDC);
		Employee joeEmployeePojo = joeEmployeeDO.getPojo();
		joeEmployeePojo.setFirstName("Joe");

		IDomainObject<Employee> maryEmployeeDO = clientSession.create(employeeDC);
		Employee maryEmployeePojo = maryEmployeeDO.getPojo();
		maryEmployeePojo.setFirstName("Mary");

		// add just joe mary to employees collection
		departmentPojo.addToEmployees(joeEmployeePojo);
		
		IPojoPackage packedPojo = packager.pack(departmentPojo);

		String marshalledPackedPojo = marshalling.marshal(packedPojo);
		IPojoPackage unmarshalledPackedPojo = (IPojoPackage) marshalling.unmarshal(marshalledPackedPojo);

		// add mary as an employee ...
		departmentPojo.addToEmployees(maryEmployeePojo); 
		
		// ... but remove mary from client session, so will be unknown
		assertEquals(3, clientSession.handles().size()); // dept + 2 employees
		clientSession.detach(maryEmployeeDO); 
		assertEquals(2, clientSession.handles().size());

		
		SessionBinding sessionBinding = clientSession.getSessionBinding();
		IHandleAssigner handleAssigner = new GuidHandleAssigner();
		DefaultDomainObjectFactory domainObjectFactory = 
			new DefaultDomainObjectFactory(sessionBinding, PersistState.UNKNOWN, ResolveState.UNRESOLVED, handleAssigner);
		AutoAddingHandleMap autoAddingHandleMap = new AutoAddingHandleMap(clientSession, domainObjectFactory);
		
		assertEquals(2, departmentPojo.getEmployees().size());
		assertTrue(departmentPojo.getEmployees().contains(maryEmployeePojo));
		unpackager.merge(departmentDO, unmarshalledPackedPojo, autoAddingHandleMap);
		// so, ideally, the department should no longer references the employee
		// however, since we remove items by domain object rather than by handle, 
		// and because the domain object is unknown to us, then in fact the
		// collection is unchanged.  IS THIS OKAY???
		assertEquals(2, departmentPojo.getEmployees().size());
		assertTrue(departmentPojo.getEmployees().contains(maryEmployeePojo));
		
		final Set<Handle> addedHandles = autoAddingHandleMap.getAdditions().handles();
		assertEquals(1, addedHandles.size()); // a DO was added for the handle that we didn't previously know about...
	}



	private void dumpTo(String marshalledObject, String filename) {
		
		try {
			FileWriter fw = new FileWriter(filename);
			IOUtils.copy(new CharArrayReader(marshalledObject.toCharArray()), fw);
			fw.flush();
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
		
	}

	
}
