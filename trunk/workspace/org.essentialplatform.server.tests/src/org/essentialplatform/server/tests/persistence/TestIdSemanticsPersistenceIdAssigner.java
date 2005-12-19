package org.essentialplatform.server.tests.persistence;

import java.math.BigInteger;

import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.fixture.progmodel.essential.extended.CustomerWithCompositeId;
import org.essentialplatform.core.fixture.progmodel.essential.extended.CustomerWithNoIdentifier;
import org.essentialplatform.core.fixture.progmodel.essential.extended.CustomerWithSimpleBigIntegerId;
import org.essentialplatform.core.fixture.progmodel.essential.extended.CustomerWithSimpleBigIntegerIdAssignedByApplication;
import org.essentialplatform.core.fixture.progmodel.essential.extended.CustomerWithSimpleByteId;
import org.essentialplatform.core.fixture.progmodel.essential.extended.CustomerWithSimpleByteIdAssignedByApplication;
import org.essentialplatform.core.fixture.progmodel.essential.extended.CustomerWithSimpleId;
import org.essentialplatform.core.fixture.progmodel.essential.extended.CustomerWithSimpleIdFirst;
import org.essentialplatform.core.fixture.progmodel.essential.extended.CustomerWithSimpleIntegerId;
import org.essentialplatform.core.fixture.progmodel.essential.extended.CustomerWithSimpleIntegerIdAssignedByApplication;
import org.essentialplatform.core.fixture.progmodel.essential.extended.CustomerWithSimpleLongId;
import org.essentialplatform.core.fixture.progmodel.essential.extended.CustomerWithSimpleLongIdAssignedByApplication;
import org.essentialplatform.core.fixture.progmodel.essential.extended.CustomerWithSimplePrimitiveByteId;
import org.essentialplatform.core.fixture.progmodel.essential.extended.CustomerWithSimplePrimitiveByteIdAssignedByApplication;
import org.essentialplatform.core.fixture.progmodel.essential.extended.CustomerWithSimplePrimitiveIntId;
import org.essentialplatform.core.fixture.progmodel.essential.extended.CustomerWithSimplePrimitiveIntIdAssignedByApplication;
import org.essentialplatform.core.fixture.progmodel.essential.extended.CustomerWithSimplePrimitiveLongId;
import org.essentialplatform.core.fixture.progmodel.essential.extended.CustomerWithSimplePrimitiveLongIdAssignedByApplication;
import org.essentialplatform.core.fixture.progmodel.essential.extended.CustomerWithSimplePrimitiveShortId;
import org.essentialplatform.core.fixture.progmodel.essential.extended.CustomerWithSimplePrimitiveShortIdAssignedByApplication;
import org.essentialplatform.core.fixture.progmodel.essential.extended.CustomerWithSimpleShortId;
import org.essentialplatform.core.fixture.progmodel.essential.extended.CustomerWithSimpleShortIdAssignedByApplication;
import org.essentialplatform.core.fixture.progmodel.essential.extended.CustomerWithSimpleStringId;
import org.essentialplatform.runtime.CompositeIdPersistenceIdAssigner;
import org.essentialplatform.runtime.IdSemanticsPersistenceIdAssigner;
import org.essentialplatform.runtime.domain.IDomainObject;
import org.essentialplatform.runtime.tests.AbstractRuntimeTestCase;
import org.essentialplatform.server.persistence.PersistenceId;
import org.essentialplatform.server.persistence.SequentialPersistenceIdAssigner;

public class TestIdSemanticsPersistenceIdAssigner extends AbstractRuntimeTestCase {

	private IdSemanticsPersistenceIdAssigner assigner;
	private SequentialPersistenceIdAssigner sequentialAssigner;
	
	private IDomainClass domainClass;
	private IDomainObject<?> dobj;
	private PersistenceId persistenceId;
	private Object[] componentValues;
	
	public TestIdSemanticsPersistenceIdAssigner() {
		super(null);
	}
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		sequentialAssigner  = new SequentialPersistenceIdAssigner();
	}

	@Override
	public void tearDown() throws Exception {
		sequentialAssigner = null;
		assigner = null;
		domainClass = null;
		dobj = null;
		persistenceId = null;
		componentValues = null;
		super.tearDown();
	}

	public void testAssignedPersistenceIdHasCorrectJavaClass() {
		domainClass = lookupAny(CustomerWithSimpleStringId.class);
		assigner = new IdSemanticsPersistenceIdAssigner(domainClass, sequentialAssigner);
		
		dobj = session.create(domainClass);

		persistenceId = assigner.assignPersistenceIdFor(dobj);
		assertEquals(CustomerWithSimpleStringId.class, persistenceId.getJavaClass());
	}

	public void testSimpleIntegerIdAndDefaultedAssignmentTypeShouldBeAssignedBySequential() {
		domainClass = lookupAny(CustomerWithSimpleIdFirst.class);
		assigner = new IdSemanticsPersistenceIdAssigner(domainClass, sequentialAssigner);

		dobj = session.create(domainClass);

		persistenceId = assigner.assignPersistenceIdFor(dobj);
		componentValues = persistenceId.getComponentValues();
		assertEquals(1, componentValues.length);
		assertEquals(1, componentValues[0]);
	}

	public void testNoIdentifierUsesSequential() {
		domainClass = lookupAny(CustomerWithNoIdentifier.class);
		assigner = new IdSemanticsPersistenceIdAssigner(domainClass, sequentialAssigner);
		
		dobj = session.create(domainClass);

		persistenceId = assigner.assignPersistenceIdFor(dobj);
		componentValues = persistenceId.getComponentValues();
		assertEquals(1, componentValues.length);
		assertEquals(1, componentValues[0]);
	}
		

	public void testIfSimpleStringId() {
		domainClass = lookupAny(CustomerWithSimpleStringId.class);
		assigner = new IdSemanticsPersistenceIdAssigner(domainClass, sequentialAssigner);
		
		dobj = session.create(domainClass);
		((CustomerWithSimpleStringId)dobj.getPojo()).setId("foobar");

		persistenceId = assigner.assignPersistenceIdFor(dobj);

		componentValues = persistenceId.getComponentValues();
		assertEquals(1, componentValues.length);
		assertEquals("foobar", componentValues[0]);
	}

	public void testIfCompositeIdWithImplicitAssignmentType() {
		domainClass = lookupAny(CustomerWithCompositeId.class);
		assigner = new IdSemanticsPersistenceIdAssigner(domainClass, sequentialAssigner);
		
		dobj = session.create(domainClass);
		CustomerWithCompositeId pojo = ((CustomerWithCompositeId)dobj.getPojo());
		pojo.setFirstName("foo");
		pojo.setLastName("bar");

		persistenceId = assigner.assignPersistenceIdFor(dobj);

		componentValues = persistenceId.getComponentValues();
		assertEquals(2, componentValues.length);
		assertEquals("bar", componentValues[0]);
		assertEquals("foo", componentValues[1]);
	}
	

	public void testIdImplicitAssignmentTypeForSimpleByteIdentifier() {
		domainClass = lookupAny(CustomerWithSimpleByteId.class);
		assigner = new IdSemanticsPersistenceIdAssigner(domainClass, sequentialAssigner);
		
		dobj = session.create(domainClass);

		persistenceId = assigner.assignPersistenceIdFor(dobj);
		componentValues = persistenceId.getComponentValues();
		assertEquals(1, componentValues.length);
		assertEquals(1, componentValues[0]);
	}
	
	public void testIdImplicitAssignmentTypeForSimpleShortIdentifier() {
		domainClass = lookupAny(CustomerWithSimpleShortId.class);
		assigner = new IdSemanticsPersistenceIdAssigner(domainClass, sequentialAssigner);
		
		dobj = session.create(domainClass);

		persistenceId = assigner.assignPersistenceIdFor(dobj);
		componentValues = persistenceId.getComponentValues();
		assertEquals(1, componentValues.length);
		assertEquals(1, componentValues[0]);
	}
	
	public void testIdImplicitAssignmentTypeForSimpleIntegerIdentifier() {
		domainClass = lookupAny(CustomerWithSimpleIntegerId.class);
		assigner = new IdSemanticsPersistenceIdAssigner(domainClass, sequentialAssigner);

		dobj = session.create(domainClass);

		persistenceId = assigner.assignPersistenceIdFor(dobj);
		componentValues = persistenceId.getComponentValues();
		assertEquals(1, componentValues.length);
		assertEquals(1, componentValues[0]);
	}
	
	public void testIdImplicitAssignmentTypeForSimpleLongIdentifier() {
		domainClass = lookupAny(CustomerWithSimpleLongId.class);
		assigner = new IdSemanticsPersistenceIdAssigner(domainClass, sequentialAssigner);
		
		dobj = session.create(domainClass);

		persistenceId = assigner.assignPersistenceIdFor(dobj);
		componentValues = persistenceId.getComponentValues();
		assertEquals(1, componentValues.length);
		assertEquals(1, componentValues[0]);
	}
	
	public void testIdImplicitAssignmentTypeForSimplePrimitiveByteIdentifier() {
		domainClass = lookupAny(CustomerWithSimplePrimitiveByteId.class);
		assigner = new IdSemanticsPersistenceIdAssigner(domainClass, sequentialAssigner);
		
		dobj = session.create(domainClass);

		persistenceId = assigner.assignPersistenceIdFor(dobj);
		componentValues = persistenceId.getComponentValues();
		assertEquals(1, componentValues.length);
		assertEquals(1, componentValues[0]);
	}
	
	public void testIdImplicitAssignmentTypeForSimplePrimitiveShortIdentifier() {
		domainClass = lookupAny(CustomerWithSimplePrimitiveShortId.class);
		assigner = new IdSemanticsPersistenceIdAssigner(domainClass, sequentialAssigner);

		dobj = session.create(domainClass);

		persistenceId = assigner.assignPersistenceIdFor(dobj);
		componentValues = persistenceId.getComponentValues();
		assertEquals(1, componentValues.length);
		assertEquals(1, componentValues[0]);
	}
	
	public void testIdImplicitAssignmentTypeForSimplePrimitiveIntIdentifier() {
		domainClass = lookupAny(CustomerWithSimplePrimitiveIntId.class);
		assigner = new IdSemanticsPersistenceIdAssigner(domainClass, sequentialAssigner);
		
		dobj = session.create(domainClass);

		persistenceId = assigner.assignPersistenceIdFor(dobj);
		componentValues = persistenceId.getComponentValues();
		assertEquals(1, componentValues.length);
		assertEquals(1, componentValues[0]);
	}
	
	public void testIdImplicitAssignmentTypeForSimplePrimitiveLongIdentifier() {
		domainClass = lookupAny(CustomerWithSimplePrimitiveLongId.class);
		assigner = new IdSemanticsPersistenceIdAssigner(domainClass, sequentialAssigner);

		dobj = session.create(domainClass);

		persistenceId = assigner.assignPersistenceIdFor(dobj);
		componentValues = persistenceId.getComponentValues();
		assertEquals(1, componentValues.length);
		assertEquals(1, componentValues[0]);
	}
	
	public void testIdImplicitAssignmentTypeForSimpleBigIntegerIdentifier() {
		domainClass = lookupAny(CustomerWithSimpleBigIntegerId.class);
		assigner = new IdSemanticsPersistenceIdAssigner(domainClass, sequentialAssigner);

		dobj = session.create(domainClass);

		persistenceId = assigner.assignPersistenceIdFor(dobj);
		componentValues = persistenceId.getComponentValues();
		assertEquals(1, componentValues.length);
		assertEquals(1, componentValues[0]);
	}
	
	public void testIdExplicitAssignmentTypeForSimpleByteIdentifier() {
		domainClass = lookupAny(CustomerWithSimpleByteIdAssignedByApplication.class);
		assigner = new IdSemanticsPersistenceIdAssigner(domainClass, sequentialAssigner);
		
		dobj = session.create(domainClass);

		((CustomerWithSimpleByteIdAssignedByApplication)dobj.getPojo()).setId(new Byte((byte)25));
		
		persistenceId = assigner.assignPersistenceIdFor(dobj);
		componentValues = persistenceId.getComponentValues();
		assertEquals(1, componentValues.length);
		assertEquals(new Byte((byte)25), componentValues[0]);
	}
	
	public void testIdExplicitAssignmentTypeForSimpleShortIdentifier() {
		domainClass = lookupAny(CustomerWithSimpleShortIdAssignedByApplication.class);
		assigner = new IdSemanticsPersistenceIdAssigner(domainClass, sequentialAssigner);

		dobj = session.create(domainClass);

		((CustomerWithSimpleShortIdAssignedByApplication)dobj.getPojo()).setId(new Short((short)25));
		
		persistenceId = assigner.assignPersistenceIdFor(dobj);
		componentValues = persistenceId.getComponentValues();
		assertEquals(1, componentValues.length);
		assertEquals(new Short((short)25), componentValues[0]);
	}
	
	public void testIdExplicitAssignmentTypeForSimpleIntegerIdentifier() {
		domainClass = lookupAny(CustomerWithSimpleIntegerIdAssignedByApplication.class);
		assigner = new IdSemanticsPersistenceIdAssigner(domainClass, sequentialAssigner);

		dobj = session.create(domainClass);

		((CustomerWithSimpleIntegerIdAssignedByApplication)dobj.getPojo()).setId(25);
		
		persistenceId = assigner.assignPersistenceIdFor(dobj);
		componentValues = persistenceId.getComponentValues();
		assertEquals(1, componentValues.length);
		assertEquals(25, componentValues[0]);
	}
	
	public void testIdExplicitAssignmentTypeForSimpleLongIdentifier() {
		domainClass = lookupAny(CustomerWithSimpleLongIdAssignedByApplication.class);
		assigner = new IdSemanticsPersistenceIdAssigner(domainClass, sequentialAssigner);

		dobj = session.create(domainClass);

		((CustomerWithSimpleLongIdAssignedByApplication)dobj.getPojo()).setId(25L);
		
		persistenceId = assigner.assignPersistenceIdFor(dobj);
		componentValues = persistenceId.getComponentValues();
		assertEquals(1, componentValues.length);
		assertEquals(25L, componentValues[0]);
	}
	
	public void testIdExplicitAssignmentTypeForSimplePrimitiveByteIdentifier() {
		domainClass = lookupAny(CustomerWithSimplePrimitiveByteIdAssignedByApplication.class);
		assigner = new IdSemanticsPersistenceIdAssigner(domainClass, sequentialAssigner);

		dobj = session.create(domainClass);

		((CustomerWithSimplePrimitiveByteIdAssignedByApplication)dobj.getPojo()).setId((byte)25);
		
		persistenceId = assigner.assignPersistenceIdFor(dobj);
		componentValues = persistenceId.getComponentValues();
		assertEquals(1, componentValues.length);
		assertEquals((byte)25, componentValues[0]);
	}
	
	public void testIdExplicitAssignmentTypeForSimplePrimitiveShortIdentifier() {
		domainClass = lookupAny(CustomerWithSimplePrimitiveShortIdAssignedByApplication.class);
		assigner = new IdSemanticsPersistenceIdAssigner(domainClass, sequentialAssigner);

		dobj = session.create(domainClass);

		((CustomerWithSimplePrimitiveShortIdAssignedByApplication)dobj.getPojo()).setId((short)25);
		
		persistenceId = assigner.assignPersistenceIdFor(dobj);
		componentValues = persistenceId.getComponentValues();
		assertEquals(1, componentValues.length);
		assertEquals((short)25, componentValues[0]);
	}
	
	public void testIdExplicitAssignmentTypeForSimplePrimitiveIntIdentifier() {
		domainClass = lookupAny(CustomerWithSimplePrimitiveIntIdAssignedByApplication.class);
		assigner = new IdSemanticsPersistenceIdAssigner(domainClass, sequentialAssigner);

		dobj = session.create(domainClass);

		((CustomerWithSimplePrimitiveIntIdAssignedByApplication)dobj.getPojo()).setId(25);
		
		persistenceId = assigner.assignPersistenceIdFor(dobj);
		componentValues = persistenceId.getComponentValues();
		assertEquals(1, componentValues.length);
		assertEquals(25, componentValues[0]);
	}
	
	public void testIdExplicitAssignmentTypeForSimplePrimitiveLongIdentifier() {
		domainClass = lookupAny(CustomerWithSimplePrimitiveLongIdAssignedByApplication.class);
		assigner = new IdSemanticsPersistenceIdAssigner(domainClass, sequentialAssigner);

		dobj = session.create(domainClass);

		((CustomerWithSimplePrimitiveLongIdAssignedByApplication)dobj.getPojo()).setId(25L);
		
		persistenceId = assigner.assignPersistenceIdFor(dobj);
		componentValues = persistenceId.getComponentValues();
		assertEquals(1, componentValues.length);
		assertEquals(25L, componentValues[0]);
	}
	
	public void testIdExplicitAssignmentTypeForSimpleBigIntegerIdentifier() {
		domainClass = lookupAny(CustomerWithSimpleBigIntegerIdAssignedByApplication.class);
		assigner = new IdSemanticsPersistenceIdAssigner(domainClass, sequentialAssigner);

		dobj = session.create(domainClass);

		((CustomerWithSimpleBigIntegerIdAssignedByApplication)dobj.getPojo()).setId(BigInteger.valueOf(25));
		
		persistenceId = assigner.assignPersistenceIdFor(dobj);
		componentValues = persistenceId.getComponentValues();
		assertEquals(1, componentValues.length);
		assertEquals(BigInteger.valueOf(25), componentValues[0]);
	}


}
