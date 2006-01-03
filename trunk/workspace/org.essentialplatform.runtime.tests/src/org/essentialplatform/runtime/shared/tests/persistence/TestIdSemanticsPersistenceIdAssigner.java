package org.essentialplatform.runtime.shared.tests.persistence;

import java.math.BigInteger;

import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.fixture.progmodel.essential.extended.CustomerWithCompositeId;
import org.essentialplatform.core.fixture.progmodel.essential.extended.CustomerWithNoIdentifier;
import org.essentialplatform.core.fixture.progmodel.essential.extended.CustomerWithSimpleBigIntegerId;
import org.essentialplatform.core.fixture.progmodel.essential.extended.CustomerWithSimpleBigIntegerIdAssignedByApplication;
import org.essentialplatform.core.fixture.progmodel.essential.extended.CustomerWithSimpleByteId;
import org.essentialplatform.core.fixture.progmodel.essential.extended.CustomerWithSimpleByteIdAssignedByApplication;
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
import org.essentialplatform.runtime.server.persistence.IdSemanticsPersistenceIdAssigner;
import org.essentialplatform.runtime.server.persistence.SequentialPersistenceIdAssigner;
import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.persistence.PersistenceId;
import org.essentialplatform.runtime.shared.tests.AbstractRuntimeClientTestCase;
import org.essentialplatform.runtime.shared.tests.AbstractRuntimeServerTestCase;

public class TestIdSemanticsPersistenceIdAssigner extends AbstractRuntimeServerTestCase {

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

	public void incompletetestAssignedPersistenceIdHasCorrectJavaClass() {
		domainClass = lookupAny(CustomerWithSimpleStringId.class);
		assigner = new IdSemanticsPersistenceIdAssigner(domainClass, sequentialAssigner);

		// TODO: need to (a) create session, and (b) create the dobj too
		session.attach(dobj);

		persistenceId = assigner.assignPersistenceIdFor(dobj);
		assertEquals(CustomerWithSimpleStringId.class, persistenceId.getJavaClass());
	}

	public void incompletetestSimpleIntegerIdAndDefaultedAssignmentTypeShouldBeAssignedBySequential() {
		domainClass = lookupAny(CustomerWithSimpleIdFirst.class);
		assigner = new IdSemanticsPersistenceIdAssigner(domainClass, sequentialAssigner);

		// TODO: need to (a) create session, and (b) create the dobj too
		session.attach(dobj);

		persistenceId = assigner.assignPersistenceIdFor(dobj);
		componentValues = persistenceId.getComponentValues();
		assertEquals(1, componentValues.length);
		assertEquals(1, componentValues[0]);
	}

	public void incompletetestNoIdentifierUsesSequential() {
		domainClass = lookupAny(CustomerWithNoIdentifier.class);
		assigner = new IdSemanticsPersistenceIdAssigner(domainClass, sequentialAssigner);
		
		// TODO: need to (a) create session, and (b) create the dobj too
		session.attach(dobj);

		persistenceId = assigner.assignPersistenceIdFor(dobj);
		componentValues = persistenceId.getComponentValues();
		assertEquals(1, componentValues.length);
		assertEquals(1, componentValues[0]);
	}
		

	public void incompletetestIfSimpleStringId() {
		domainClass = lookupAny(CustomerWithSimpleStringId.class);
		assigner = new IdSemanticsPersistenceIdAssigner(domainClass, sequentialAssigner);
		
		// TODO: need to (a) create session, and (b) create the dobj too
		session.attach(dobj);
		
		((CustomerWithSimpleStringId)dobj.getPojo()).setId("foobar");

		persistenceId = assigner.assignPersistenceIdFor(dobj);

		componentValues = persistenceId.getComponentValues();
		assertEquals(1, componentValues.length);
		assertEquals("foobar", componentValues[0]);
	}

	public void incompletetestIfCompositeIdWithImplicitAssignmentType() {
		domainClass = lookupAny(CustomerWithCompositeId.class);
		assigner = new IdSemanticsPersistenceIdAssigner(domainClass, sequentialAssigner);
		
		// TODO: need to (a) create session, and (b) create the dobj too
		session.attach(dobj);

		CustomerWithCompositeId pojo = ((CustomerWithCompositeId)dobj.getPojo());
		pojo.setFirstName("foo");
		pojo.setLastName("bar");

		persistenceId = assigner.assignPersistenceIdFor(dobj);

		componentValues = persistenceId.getComponentValues();
		assertEquals(2, componentValues.length);
		assertEquals("bar", componentValues[0]);
		assertEquals("foo", componentValues[1]);
	}
	

	public void incompletetestIdImplicitAssignmentTypeForSimpleByteIdentifier() {
		domainClass = lookupAny(CustomerWithSimpleByteId.class);
		assigner = new IdSemanticsPersistenceIdAssigner(domainClass, sequentialAssigner);
		
		// TODO: need to (a) create session, and (b) create the dobj too
		session.attach(dobj);

		persistenceId = assigner.assignPersistenceIdFor(dobj);
		componentValues = persistenceId.getComponentValues();
		assertEquals(1, componentValues.length);
		assertEquals(1, componentValues[0]);
	}
	
	public void incompletetestIdImplicitAssignmentTypeForSimpleShortIdentifier() {
		domainClass = lookupAny(CustomerWithSimpleShortId.class);
		assigner = new IdSemanticsPersistenceIdAssigner(domainClass, sequentialAssigner);
		
		// TODO: need to (a) create session, and (b) create the dobj too
		session.attach(dobj);

		persistenceId = assigner.assignPersistenceIdFor(dobj);
		componentValues = persistenceId.getComponentValues();
		assertEquals(1, componentValues.length);
		assertEquals(1, componentValues[0]);
	}
	
	public void incompletetestIdImplicitAssignmentTypeForSimpleIntegerIdentifier() {
		domainClass = lookupAny(CustomerWithSimpleIntegerId.class);
		assigner = new IdSemanticsPersistenceIdAssigner(domainClass, sequentialAssigner);

		// TODO: need to (a) create session, and (b) create the dobj too
		session.attach(dobj);

		persistenceId = assigner.assignPersistenceIdFor(dobj);
		componentValues = persistenceId.getComponentValues();
		assertEquals(1, componentValues.length);
		assertEquals(1, componentValues[0]);
	}
	
	public void incompletetestIdImplicitAssignmentTypeForSimpleLongIdentifier() {
		domainClass = lookupAny(CustomerWithSimpleLongId.class);
		assigner = new IdSemanticsPersistenceIdAssigner(domainClass, sequentialAssigner);
		
		// TODO: need to (a) create session, and (b) create the dobj too
		session.attach(dobj);

		persistenceId = assigner.assignPersistenceIdFor(dobj);
		componentValues = persistenceId.getComponentValues();
		assertEquals(1, componentValues.length);
		assertEquals(1, componentValues[0]);
	}
	
	public void incompletetestIdImplicitAssignmentTypeForSimplePrimitiveByteIdentifier() {
		domainClass = lookupAny(CustomerWithSimplePrimitiveByteId.class);
		assigner = new IdSemanticsPersistenceIdAssigner(domainClass, sequentialAssigner);
		
		// TODO: need to (a) create session, and (b) create the dobj too
		session.attach(dobj);

		persistenceId = assigner.assignPersistenceIdFor(dobj);
		componentValues = persistenceId.getComponentValues();
		assertEquals(1, componentValues.length);
		assertEquals(1, componentValues[0]);
	}
	
	public void incompletetestIdImplicitAssignmentTypeForSimplePrimitiveShortIdentifier() {
		domainClass = lookupAny(CustomerWithSimplePrimitiveShortId.class);
		assigner = new IdSemanticsPersistenceIdAssigner(domainClass, sequentialAssigner);

		// TODO: need to (a) create session, and (b) create the dobj too
		session.attach(dobj);

		persistenceId = assigner.assignPersistenceIdFor(dobj);
		componentValues = persistenceId.getComponentValues();
		assertEquals(1, componentValues.length);
		assertEquals(1, componentValues[0]);
	}
	
	public void incompletetestIdImplicitAssignmentTypeForSimplePrimitiveIntIdentifier() {
		domainClass = lookupAny(CustomerWithSimplePrimitiveIntId.class);
		assigner = new IdSemanticsPersistenceIdAssigner(domainClass, sequentialAssigner);
		
		// TODO: need to (a) create session, and (b) create the dobj too
		session.attach(dobj);

		persistenceId = assigner.assignPersistenceIdFor(dobj);
		componentValues = persistenceId.getComponentValues();
		assertEquals(1, componentValues.length);
		assertEquals(1, componentValues[0]);
	}
	
	public void incompletetestIdImplicitAssignmentTypeForSimplePrimitiveLongIdentifier() {
		domainClass = lookupAny(CustomerWithSimplePrimitiveLongId.class);
		assigner = new IdSemanticsPersistenceIdAssigner(domainClass, sequentialAssigner);

		// TODO: need to (a) create session, and (b) create the dobj too
		session.attach(dobj);

		persistenceId = assigner.assignPersistenceIdFor(dobj);
		componentValues = persistenceId.getComponentValues();
		assertEquals(1, componentValues.length);
		assertEquals(1, componentValues[0]);
	}
	
	public void incompletetestIdImplicitAssignmentTypeForSimpleBigIntegerIdentifier() {
		domainClass = lookupAny(CustomerWithSimpleBigIntegerId.class);
		assigner = new IdSemanticsPersistenceIdAssigner(domainClass, sequentialAssigner);

		// TODO: need to (a) create session, and (b) create the dobj too
		session.attach(dobj);

		persistenceId = assigner.assignPersistenceIdFor(dobj);
		componentValues = persistenceId.getComponentValues();
		assertEquals(1, componentValues.length);
		assertEquals(1, componentValues[0]);
	}
	
	public void incompletetestIdExplicitAssignmentTypeForSimpleByteIdentifier() {
		domainClass = lookupAny(CustomerWithSimpleByteIdAssignedByApplication.class);
		assigner = new IdSemanticsPersistenceIdAssigner(domainClass, sequentialAssigner);
		
		// TODO: need to (a) create session, and (b) create the dobj too
		session.attach(dobj);

		((CustomerWithSimpleByteIdAssignedByApplication)dobj.getPojo()).setId(new Byte((byte)25));
		
		persistenceId = assigner.assignPersistenceIdFor(dobj);
		componentValues = persistenceId.getComponentValues();
		assertEquals(1, componentValues.length);
		assertEquals(new Byte((byte)25), componentValues[0]);
	}
	
	public void incompletetestIdExplicitAssignmentTypeForSimpleShortIdentifier() {
		domainClass = lookupAny(CustomerWithSimpleShortIdAssignedByApplication.class);
		assigner = new IdSemanticsPersistenceIdAssigner(domainClass, sequentialAssigner);

		// TODO: need to (a) create session, and (b) create the dobj too
		session.attach(dobj);

		((CustomerWithSimpleShortIdAssignedByApplication)dobj.getPojo()).setId(new Short((short)25));
		
		persistenceId = assigner.assignPersistenceIdFor(dobj);
		componentValues = persistenceId.getComponentValues();
		assertEquals(1, componentValues.length);
		assertEquals(new Short((short)25), componentValues[0]);
	}
	
	public void incompletetestIdExplicitAssignmentTypeForSimpleIntegerIdentifier() {
		domainClass = lookupAny(CustomerWithSimpleIntegerIdAssignedByApplication.class);
		assigner = new IdSemanticsPersistenceIdAssigner(domainClass, sequentialAssigner);

		// TODO: need to (a) create session, and (b) create the dobj too
		session.attach(dobj);

		((CustomerWithSimpleIntegerIdAssignedByApplication)dobj.getPojo()).setId(25);
		
		persistenceId = assigner.assignPersistenceIdFor(dobj);
		componentValues = persistenceId.getComponentValues();
		assertEquals(1, componentValues.length);
		assertEquals(25, componentValues[0]);
	}
	
	public void incompletetestIdExplicitAssignmentTypeForSimpleLongIdentifier() {
		domainClass = lookupAny(CustomerWithSimpleLongIdAssignedByApplication.class);
		assigner = new IdSemanticsPersistenceIdAssigner(domainClass, sequentialAssigner);

		// TODO: need to (a) create session, and (b) create the dobj too
		session.attach(dobj);

		((CustomerWithSimpleLongIdAssignedByApplication)dobj.getPojo()).setId(25L);
		
		persistenceId = assigner.assignPersistenceIdFor(dobj);
		componentValues = persistenceId.getComponentValues();
		assertEquals(1, componentValues.length);
		assertEquals(25L, componentValues[0]);
	}
	
	public void incompletetestIdExplicitAssignmentTypeForSimplePrimitiveByteIdentifier() {
		domainClass = lookupAny(CustomerWithSimplePrimitiveByteIdAssignedByApplication.class);
		assigner = new IdSemanticsPersistenceIdAssigner(domainClass, sequentialAssigner);

		// TODO: need to (a) create session, and (b) create the dobj too
		session.attach(dobj);

		((CustomerWithSimplePrimitiveByteIdAssignedByApplication)dobj.getPojo()).setId((byte)25);
		
		persistenceId = assigner.assignPersistenceIdFor(dobj);
		componentValues = persistenceId.getComponentValues();
		assertEquals(1, componentValues.length);
		assertEquals((byte)25, componentValues[0]);
	}
	
	public void incompletetestIdExplicitAssignmentTypeForSimplePrimitiveShortIdentifier() {
		domainClass = lookupAny(CustomerWithSimplePrimitiveShortIdAssignedByApplication.class);
		assigner = new IdSemanticsPersistenceIdAssigner(domainClass, sequentialAssigner);

		// TODO: need to (a) create session, and (b) create the dobj too
		session.attach(dobj);

		((CustomerWithSimplePrimitiveShortIdAssignedByApplication)dobj.getPojo()).setId((short)25);
		
		persistenceId = assigner.assignPersistenceIdFor(dobj);
		componentValues = persistenceId.getComponentValues();
		assertEquals(1, componentValues.length);
		assertEquals((short)25, componentValues[0]);
	}
	
	public void incompletetestIdExplicitAssignmentTypeForSimplePrimitiveIntIdentifier() {
		domainClass = lookupAny(CustomerWithSimplePrimitiveIntIdAssignedByApplication.class);
		assigner = new IdSemanticsPersistenceIdAssigner(domainClass, sequentialAssigner);

		// TODO: need to (a) create session, and (b) create the dobj too
		session.attach(dobj);

		((CustomerWithSimplePrimitiveIntIdAssignedByApplication)dobj.getPojo()).setId(25);
		
		persistenceId = assigner.assignPersistenceIdFor(dobj);
		componentValues = persistenceId.getComponentValues();
		assertEquals(1, componentValues.length);
		assertEquals(25, componentValues[0]);
	}
	
	public void incompletetestIdExplicitAssignmentTypeForSimplePrimitiveLongIdentifier() {
		domainClass = lookupAny(CustomerWithSimplePrimitiveLongIdAssignedByApplication.class);
		assigner = new IdSemanticsPersistenceIdAssigner(domainClass, sequentialAssigner);

		// TODO: need to (a) create session, and (b) create the dobj too
		session.attach(dobj);

		((CustomerWithSimplePrimitiveLongIdAssignedByApplication)dobj.getPojo()).setId(25L);
		
		persistenceId = assigner.assignPersistenceIdFor(dobj);
		componentValues = persistenceId.getComponentValues();
		assertEquals(1, componentValues.length);
		assertEquals(25L, componentValues[0]);
	}
	
	public void incompletetestIdExplicitAssignmentTypeForSimpleBigIntegerIdentifier() {
		domainClass = lookupAny(CustomerWithSimpleBigIntegerIdAssignedByApplication.class);
		assigner = new IdSemanticsPersistenceIdAssigner(domainClass, sequentialAssigner);

		// TODO: need to (a) create session, and (b) create the dobj too
		session.attach(dobj);

		((CustomerWithSimpleBigIntegerIdAssignedByApplication)dobj.getPojo()).setId(BigInteger.valueOf(25));
		
		persistenceId = assigner.assignPersistenceIdFor(dobj);
		componentValues = persistenceId.getComponentValues();
		assertEquals(1, componentValues.length);
		assertEquals(BigInteger.valueOf(25), componentValues[0]);
	}


}
