package org.essentialplatform.progmodel.essential.core.tests;

import org.eclipse.emf.ecore.EOperation;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.domain.OperationKind;
import org.essentialplatform.core.domain.IDomainClass.IOperation;
import org.essentialplatform.core.fixture.progmodel.essential.standard.operation.Appointment;
import org.essentialplatform.core.fixture.progmodel.essential.standard.operation.AppointmentWithAccessor;
import org.essentialplatform.core.fixture.progmodel.essential.standard.operation.AppointmentWithCollection;
import org.essentialplatform.core.fixture.progmodel.essential.standard.operation.AppointmentWithMutator;
import org.essentialplatform.core.fixture.progmodel.essential.standard.operation.AppointmentWithSingleReference;
import org.essentialplatform.core.fixture.progmodel.essential.standard.operation.CustomerWithNoArgOperation;
import org.essentialplatform.core.fixture.progmodel.essential.standard.operation.CustomerWithPackageLocalVisibilityOperation;
import org.essentialplatform.core.fixture.progmodel.essential.standard.operation.CustomerWithPrimitiveArgOperation;
import org.essentialplatform.core.fixture.progmodel.essential.standard.operation.CustomerWithPrivateVisibilityOperation;
import org.essentialplatform.core.fixture.progmodel.essential.standard.operation.CustomerWithProgrammaticPublicVisibilityOperation;
import org.essentialplatform.core.fixture.progmodel.essential.standard.operation.CustomerWithProtectedVisibilityOperation;
import org.essentialplatform.core.fixture.progmodel.essential.standard.operation.CustomerWithPublicVisibilityOperation;
import org.essentialplatform.core.fixture.progmodel.essential.standard.operation.Man;
import org.essentialplatform.core.fixture.progmodel.essential.standard.operation.OperationsCustomerPositionedOnMap;
import org.essentialplatform.core.fixture.progmodel.essential.standard.operation.Priest;
import org.essentialplatform.core.fixture.progmodel.essential.standard.operation.Woman;
import org.essentialplatform.core.tests.AbstractTestCase;
import org.essentialplatform.runtime.RuntimeDeployment.RuntimeClassBinding;


/**
 * We use the {@link RuntimeDomain} to register classes since the operations are
 * only identified via {@link RuntimeDomain#done()}.
 * 
 * @author Dan Haywood
 *
 */
public abstract class TestDomainClassOperations extends AbstractTestCase {

	private IDomainClass domainClass;

	/**
	 * Methods which are public are implicitly picked up as a user-invokable
	 * operation.
	 * 
	 * <p>
	 * Tested for both instance and static methods.
	 */
	public void testPublicVisibilityMethodPickedUpAsOperation() {
		domainClass = lookupAny(CustomerWithPublicVisibilityOperation.class);
		
		EOperation eOperation = domainClass.getEOperationNamed("placeOrder");
		IDomainClass.IOperation operation = domainClass.getIOperation(eOperation);
		assertNotNull(eOperation);
		assertEquals("placeOrder", eOperation.getName());
		assertFalse(operation.isStatic());
		
		eOperation = domainClass.getEOperationNamed("create");
		operation = domainClass.getIOperation(eOperation);
		assertNotNull(eOperation);
		assertEquals("create", eOperation.getName());
		assertTrue(operation.isStatic());

		assertEquals(2, domainClass.eOperations().size());
		assertEquals(1, domainClass.eOperations(OperationKind.INSTANCE, true).size());
		assertEquals(1, domainClass.eOperations(OperationKind.STATIC, true).size());
	}
	
	/**
	 * However, protected visibility methods are not picked up.
	 *  
	 * <p>
	 * Tested for both instance and static methods.
	 */
	public void testProtectedVisibilityMethodNotPickedUpAsOperation() {
		domainClass = lookupAny(CustomerWithProtectedVisibilityOperation.class);
		
		EOperation eOperation = domainClass.getEOperationNamed("placeOrder");
		assertNull(eOperation);
		
		eOperation = domainClass.getEOperationNamed("create");
		assertNull(eOperation);

		assertEquals(0, domainClass.eOperations(OperationKind.INSTANCE, false).size());
		assertEquals(0, domainClass.eOperations(OperationKind.STATIC, false).size());
	}
	
	/**
	 * Nor are package-local visibility methods.
	 * 
	 * <p>
	 * Tested for both instance and static methods.
	 */
	public void testPackageLocalVisibilityMethodNotPickedUpAsOperation() {
		domainClass = lookupAny(CustomerWithPackageLocalVisibilityOperation.class);
		
		EOperation eOperation = domainClass.getEOperationNamed("placeOrder");
		assertNull(eOperation);
		
		eOperation = domainClass.getEOperationNamed("create");
		assertNull(eOperation);
	}


	/**
	 * Nor are private visibility methods.
	 * 
	 * <p>
	 * Tested for both instance and static methods.
	 */
	public void testPrivateVisibilityMethodNotPickedUpAsOperation() {
		domainClass = lookupAny(CustomerWithPrivateVisibilityOperation.class);
		
		EOperation eOperation = domainClass.getEOperationNamed("placeOrder");
		assertNull(eOperation);
		
		eOperation = domainClass.getEOperationNamed("create");
		assertNull(eOperation);
	}

	/**
	 * Although methods which are public are picked up as operations by
	 * default, this can be suppressed using the <tt>Programmatic</tt> 
	 * annotation.
	 * 
	 * <p>
	 * Tested for both instance and static methods.
	 */
	public void testSuppressedPublicVisibilityMethodNotPickedUpAsOperation() {
		domainClass = lookupAny(CustomerWithProgrammaticPublicVisibilityOperation.class);
		
		EOperation eOperation = domainClass.getEOperationNamed("placeOrder");
		assertNull(eOperation);
		
		eOperation = domainClass.getEOperationNamed("create");
		assertNull(eOperation);
	}

	public void testMethodWithNoArgumentsAPickedUpAsOperation() {
		domainClass = lookupAny(CustomerWithNoArgOperation.class);

		EOperation eOperation = domainClass.getEOperationNamed("placeOrder");
		IDomainClass.IOperation operation = domainClass.getIOperation(eOperation);
		assertNotNull(eOperation);
		assertEquals("placeOrder", eOperation.getName());
		assertEquals(0, eOperation.getEParameters().size());
		assertFalse(operation.isStatic());
		
		eOperation = domainClass.getEOperationNamed("create");
		operation = domainClass.getIOperation(eOperation);
		assertNotNull(eOperation);
		assertEquals("create", eOperation.getName());
		assertEquals(0, eOperation.getEParameters().size());
		assertTrue(operation.isStatic());
	}

	public void testMethodWithPrimitiveArgumentsPickedUpAsOperation1Arg() {
		domainClass = lookupAny(CustomerWithPrimitiveArgOperation.class);

		EOperation eOperation = domainClass.getEOperationNamed("rankAs");
		IOperation operation = domainClass.getIOperation(eOperation); 
		assertNotNull(eOperation);
		assertEquals("rankAs", eOperation.getName());
		assertEquals(1, eOperation.getEParameters().size());
		assertTrue(operation.isParameterAValue(0));
		assertEquals("int", operation.getNameFor(0));
		assertEquals("EInt", operation.getEDataTypeFor(0).getName());
		assertFalse(operation.isStatic());
		
		eOperation = domainClass.getEOperationNamed("createWithRank");
		operation = domainClass.getIOperation(eOperation);
		assertNotNull(eOperation);
		assertEquals("createWithRank", eOperation.getName());
		assertEquals(1, eOperation.getEParameters().size());
		assertTrue(operation.isParameterAValue(0));
		assertEquals("long", operation.getNameFor(0));
		assertEquals("ELong", operation.getEDataTypeFor(0).getName());
		assertTrue(operation.isStatic());
	}

	public void testMethodWithPrimitiveArgumentsPickedUpAsOperation2Arg() {
		domainClass = lookupAny(OperationsCustomerPositionedOnMap.class);

		EOperation eOperation = domainClass.getEOperationNamed("positionAt");
		IOperation operation = domainClass.getIOperation(eOperation); 
		assertNotNull(eOperation);
		assertEquals("positionAt", eOperation.getName());
		assertEquals(2, eOperation.getEParameters().size());
		assertTrue(operation.isParameterAValue(0));
		assertEquals("float", operation.getNameFor(0));
		assertEquals("EFloat", operation.getEDataTypeFor(0).getName());
		assertTrue(operation.isParameterAValue(1));
		assertEquals("float1", operation.getNameFor(1));
		assertEquals("EFloat", operation.getEDataTypeFor(1).getName());
		assertFalse(operation.isStatic());
		
		eOperation = domainClass.getEOperationNamed("createAtPosition");
		operation = domainClass.getIOperation(eOperation); 
		assertNotNull(eOperation);
		assertEquals("createAtPosition", eOperation.getName());
		assertEquals(2, eOperation.getEParameters().size());
		assertTrue(operation.isParameterAValue(0));
		assertEquals("double", operation.getNameFor(0));
		assertEquals("EDouble", operation.getEDataTypeFor(0).getName());
		assertTrue(operation.isParameterAValue(1));
		assertEquals("double1", operation.getNameFor(1));
		assertEquals("EDouble", operation.getEDataTypeFor(1).getName());
		assertTrue(operation.isStatic());
	}

	public void testMethodWithValueObjectArgumentsPickedUpAsOperation() {
		// 2 arg
		domainClass = lookupAny(Appointment.class);
		
		EOperation eOperation = domainClass.getEOperationNamed("moveTo");
		IOperation operation = domainClass.getIOperation(eOperation); 
		assertNotNull(eOperation);
		assertEquals("moveTo", eOperation.getName());
		assertEquals(2, eOperation.getEParameters().size());
		assertTrue(operation.isParameterAValue(0));
		assertEquals("timePeriod", operation.getNameFor(0));
		assertEquals("org.essentialplatform.core.fixture.progmodel.essential.standard.operation.TimePeriod", operation.getEDataTypeFor(0).getName());
		assertTrue(operation.isParameterAValue(1));
		assertEquals("string", operation.getNameFor(1));
		assertEquals("EString", operation.getEDataTypeFor(1).getName());
		assertFalse(operation.isStatic());
		
		eOperation = domainClass.getEOperationNamed("createAt");
		operation = domainClass.getIOperation(eOperation); 
		assertNotNull(eOperation);
		assertEquals("createAt", eOperation.getName());
		assertEquals(2, eOperation.getEParameters().size());
		assertTrue(operation.isParameterAValue(0));
		assertEquals("timePeriod", operation.getNameFor(0));
		assertEquals("org.essentialplatform.core.fixture.progmodel.essential.standard.operation.TimePeriod", operation.getEDataTypeFor(0).getName());
		assertTrue(operation.isParameterAValue(1));
		assertEquals("string", operation.getNameFor(1));
		assertEquals("EString", operation.getEDataTypeFor(1).getName());
		assertTrue(operation.isStatic());
	}

	/**
	 * Must register with metaModel rather than just instantiate since we need
	 * to lookup other {@link IDomainClass}es.
	 *
	 */
	public void testMethodWithDomainObjectArgumentsPickedUpAsOperation() {
		
		lookupAny(Man.class);
		lookupAny(Woman.class);
		domainClass = lookupAny(Priest.class);
		domainClass.getDomain().done();

		EOperation eOperation = domainClass.getEOperationNamed("marry");
		IOperation operation = domainClass.getIOperation(eOperation); 
		assertNotNull(eOperation);
		assertEquals("marry", eOperation.getName());
		assertEquals(2, eOperation.getEParameters().size());
		assertTrue(operation.isParameterADomainObject(0));
		// HACK
		IDomainClass eMarryFirstArgClass = (IDomainClass)operation.getDomainClassFor(0);
		assertSame(Man.class, ((RuntimeClassBinding)eMarryFirstArgClass.getBinding()).getJavaClass());
		
	}

	/**
	 * If a method has invalid arguments (not a value, not a domain object),
	 * then it is not exposed as an operation.
	 *
	 */
	public void incompletetestMethodWithInvalidArgumentsNotPickedUpAsOperation() {
		// TODO
	}

	/**
	 * Tested for both instance and static methods.
	 *
	 */
	public void incompletetestMethodWithMixedValidArgumentsPickedUpAsOperation() {
		// TODO
	}

	/**
	 * Tested for both instance and static methods.
	 *
	 */
	public void incompletetestMethodReturningVoidPickedUpAsOperation() {
		// TODO
	}

	/**
	 * This is following the lead from the original NO framework.  An 
	 * alternative would be that it <i>is</i> picked up, and rendered as some
	 * sort of dialog box???
	 *
	 * <p>
	 * Tested for both instance and static methods.
	 */
	public void incompletetestMethodReturningPrimitiveNotPickedUpAsOperation() {
		// TODO
	}

	/**
	 * This is following the lead from the original NO framework.  An 
	 * alternative would be that it <i>is</i> picked up, and rendered as some
	 * sort of dialog box???
	 *
	 * <p>
	 * Tested for both instance and static methods.
	 */
	public void incompletetestMethodReturningValueNotPickedUpAsOperation() {
		// TODO
	}

	/**
	 * Tested for both instance and static methods.
	 *
	 */
	public void incompletetestMethodReturningDomainObjectPickedUpAsOperation() {
		// TODO
	}

	public void testAccessorsNotPickedUpAsOperation() {
		domainClass = lookupAny(AppointmentWithAccessor.class);
		assertEquals(2, domainClass.eOperations().size());
	}

	public void testMutatorsNotPickedUpAsOperation() {
		domainClass = lookupAny(AppointmentWithMutator.class);
		assertEquals(2, domainClass.eOperations().size());
	}

	public void testSingleReferencessNotPickedUpAsOperation() {
		domainClass = lookupAny(AppointmentWithSingleReference.class);
		assertEquals(2, domainClass.eOperations().size());
	}

	public void testCollectionsNotPickedUpAsOperation() {
		domainClass = lookupAny(AppointmentWithCollection.class);
		assertEquals(2, domainClass.eOperations().size());
	}

}
