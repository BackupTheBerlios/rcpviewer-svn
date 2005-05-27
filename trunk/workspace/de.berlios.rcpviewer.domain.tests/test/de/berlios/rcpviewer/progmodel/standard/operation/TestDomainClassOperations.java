package de.berlios.rcpviewer.progmodel.standard.operation;


import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EOperation;

import de.berlios.rcpviewer.AbstractTestCase;
import de.berlios.rcpviewer.domain.Domain;
import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.domain.IRuntimeDomainClass;
import de.berlios.rcpviewer.domain.OperationKind;
import de.berlios.rcpviewer.progmodel.standard.operation.Appointment;
import de.berlios.rcpviewer.progmodel.standard.operation.AppointmentWithAccessor;
import de.berlios.rcpviewer.progmodel.standard.operation.OperationsCustomerPositionedOnMap;
import de.berlios.rcpviewer.progmodel.standard.operation.CustomerWithNoArgOperation;
import de.berlios.rcpviewer.progmodel.standard.operation.CustomerWithPackageLocalVisibilityOperation;
import de.berlios.rcpviewer.progmodel.standard.operation.CustomerWithPrimitiveArgOperation;
import de.berlios.rcpviewer.progmodel.standard.operation.CustomerWithPrivateVisibilityOperation;
import de.berlios.rcpviewer.progmodel.standard.operation.CustomerWithProgrammaticPublicVisibilityOperation;
import de.berlios.rcpviewer.progmodel.standard.operation.CustomerWithProtectedVisibilityOperation;
import de.berlios.rcpviewer.progmodel.standard.operation.CustomerWithPublicVisibilityOperation;


/**
 * We use the {@link Domain} to register classes since the operations are
 * only identified via {@link Domain#done()}.
 * 
 * @author Dan Haywood
 *
 */
public class TestDomainClassOperations extends AbstractTestCase {

	private IRuntimeDomainClass<?> domainClass;
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Methods which are public are implicitly picked up as a user-invokable
	 * operation.
	 * 
	 * <p>
	 * Tested for both instance and static methods.
	 */
	public void testPublicVisibilityMethodPickedUpAsOperation() {
		domainClass = Domain.lookupAny(CustomerWithPublicVisibilityOperation.class);
		
		EOperation eOperation = domainClass.getEOperationNamed("placeOrder");
		assertNotNull(eOperation);
		assertEquals("placeOrder", eOperation.getName());
		assertFalse(domainClass.isStatic(eOperation));
		
		eOperation = domainClass.getEOperationNamed("create");
		assertNotNull(eOperation);
		assertEquals("create", eOperation.getName());
		assertTrue(domainClass.isStatic(eOperation));

		assertEquals(2, domainClass.operations().size());
		assertEquals(1, domainClass.operations(OperationKind.INSTANCE, true).size());
		assertEquals(1, domainClass.operations(OperationKind.STATIC, true).size());
	}
	
	/**
	 * However, protected visibility methods are not picked up.
	 *  
	 * <p>
	 * Tested for both instance and static methods.
	 */
	public void testProtectedVisibilityMethodNotPickedUpAsOperation() {
		domainClass = Domain.lookupAny(CustomerWithProtectedVisibilityOperation.class);
		
		EOperation eOperation = domainClass.getEOperationNamed("placeOrder");
		assertNull(eOperation);
		
		eOperation = domainClass.getEOperationNamed("create");
		assertNull(eOperation);

		assertEquals(0, domainClass.operations(OperationKind.INSTANCE, false).size());
		assertEquals(0, domainClass.operations(OperationKind.STATIC, false).size());
	}
	
	/**
	 * Nor are package-local visibility methods.
	 * 
	 * <p>
	 * Tested for both instance and static methods.
	 */
	public void testPackageLocalVisibilityMethodNotPickedUpAsOperation() {
		domainClass = Domain.lookupAny(CustomerWithPackageLocalVisibilityOperation.class);
		
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
		domainClass = Domain.lookupAny(CustomerWithPrivateVisibilityOperation.class);
		
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
		domainClass = Domain.lookupAny(CustomerWithProgrammaticPublicVisibilityOperation.class);
		
		EOperation eOperation = domainClass.getEOperationNamed("placeOrder");
		assertNull(eOperation);
		
		eOperation = domainClass.getEOperationNamed("create");
		assertNull(eOperation);
	}

	public void testMethodWithNoArgumentsAPickedUpAsOperation() {
		domainClass = Domain.lookupAny(CustomerWithNoArgOperation.class);

		EOperation eOperation = domainClass.getEOperationNamed("placeOrder");
		assertNotNull(eOperation);
		assertEquals("placeOrder", eOperation.getName());
		assertEquals(0, eOperation.getEParameters().size());
		assertFalse(domainClass.isStatic(eOperation));
		
		eOperation = domainClass.getEOperationNamed("create");
		assertNotNull(eOperation);
		assertEquals("create", eOperation.getName());
		assertEquals(0, eOperation.getEParameters().size());
		assertTrue(domainClass.isStatic(eOperation));
	}

	public void testMethodWithPrimitiveArgumentsPickedUpAsOperation1Arg() {
		domainClass = Domain.lookupAny(CustomerWithPrimitiveArgOperation.class);

		EOperation eOperation = domainClass.getEOperationNamed("rankAs");
		assertNotNull(eOperation);
		assertEquals("rankAs", eOperation.getName());
		assertEquals(1, eOperation.getEParameters().size());
		assertTrue(domainClass.isParameterAValue(eOperation, 0));
		EDataType eDataType = domainClass.getEDataTypeFor(eOperation, 0);
		assertEquals("int", domainClass.getNameFor(eOperation, 0));
		assertEquals("EInt", eDataType.getName());
		assertFalse(domainClass.isStatic(eOperation));
		
		eOperation = domainClass.getEOperationNamed("createWithRank");
		assertNotNull(eOperation);
		assertEquals("createWithRank", eOperation.getName());
		assertEquals(1, eOperation.getEParameters().size());
		assertTrue(domainClass.isParameterAValue(eOperation, 0));
		eDataType = domainClass.getEDataTypeFor(eOperation, 0);
		assertEquals("long", domainClass.getNameFor(eOperation, 0));
		assertEquals("ELong", eDataType.getName());
		assertTrue(domainClass.isStatic(eOperation));
	}

	public void testMethodWithPrimitiveArgumentsPickedUpAsOperation2Arg() {
		domainClass = Domain.lookupAny(OperationsCustomerPositionedOnMap.class);

		EOperation eOperation = domainClass.getEOperationNamed("positionAt");
		assertNotNull(eOperation);
		assertEquals("positionAt", eOperation.getName());
		assertEquals(2, eOperation.getEParameters().size());
		assertTrue(domainClass.isParameterAValue(eOperation, 0));
		EDataType eDataType = domainClass.getEDataTypeFor(eOperation, 0);
		assertEquals("float", domainClass.getNameFor(eOperation, 0));
		assertEquals("EFloat", eDataType.getName());
		assertTrue(domainClass.isParameterAValue(eOperation, 1));
		eDataType = domainClass.getEDataTypeFor(eOperation, 1);
		assertEquals("float1", domainClass.getNameFor(eOperation, 1));
		assertEquals("EFloat", eDataType.getName());
		assertFalse(domainClass.isStatic(eOperation));
		
		eOperation = domainClass.getEOperationNamed("createAtPosition");
		assertNotNull(eOperation);
		assertEquals("createAtPosition", eOperation.getName());
		assertEquals(2, eOperation.getEParameters().size());
		assertTrue(domainClass.isParameterAValue(eOperation, 0));
		eDataType = domainClass.getEDataTypeFor(eOperation, 0);
		assertEquals("double", domainClass.getNameFor(eOperation, 0));
		assertEquals("EDouble", eDataType.getName());
		assertTrue(domainClass.isParameterAValue(eOperation, 1));
		eDataType = domainClass.getEDataTypeFor(eOperation, 1);
		assertEquals("double1", domainClass.getNameFor(eOperation, 1));
		assertEquals("EDouble", eDataType.getName());
		assertTrue(domainClass.isStatic(eOperation));
	}

	public void testMethodWithValueObjectArgumentsPickedUpAsOperation() {
		// 2 arg
		domainClass = Domain.lookupAny(Appointment.class);
		
		EOperation eOperation = domainClass.getEOperationNamed("moveTo");
		assertNotNull(eOperation);
		assertEquals("moveTo", eOperation.getName());
		assertEquals(2, eOperation.getEParameters().size());
		assertTrue(domainClass.isParameterAValue(eOperation, 0));
		EDataType eDataType = domainClass.getEDataTypeFor(eOperation, 0);
		assertEquals("timePeriod", domainClass.getNameFor(eOperation, 0));
		assertEquals("de.berlios.rcpviewer.progmodel.standard.operation.TimePeriod", eDataType.getName());
		assertTrue(domainClass.isParameterAValue(eOperation, 1));
		eDataType = domainClass.getEDataTypeFor(eOperation, 1);
		assertEquals("string", domainClass.getNameFor(eOperation, 1));
		assertEquals("EString", eDataType.getName());
		assertFalse(domainClass.isStatic(eOperation));
		
		eOperation = domainClass.getEOperationNamed("createAt");
		assertNotNull(eOperation);
		assertEquals("createAt", eOperation.getName());
		assertEquals(2, eOperation.getEParameters().size());
		assertTrue(domainClass.isParameterAValue(eOperation, 0));
		eDataType = domainClass.getEDataTypeFor(eOperation, 0);
		assertEquals("timePeriod", domainClass.getNameFor(eOperation, 0));
		assertEquals("de.berlios.rcpviewer.progmodel.standard.operation.TimePeriod", eDataType.getName());
		assertTrue(domainClass.isParameterAValue(eOperation, 1));
		eDataType = domainClass.getEDataTypeFor(eOperation, 1);
		assertEquals("string", domainClass.getNameFor(eOperation, 1));
		assertEquals("EString", eDataType.getName());
		assertTrue(domainClass.isStatic(eOperation));
	}

	/**
	 * Must register with metaModel rather than just instantiate since we need
	 * to lookup other {@link IDomainClass}es.
	 *
	 */
	public void testMethodWithDomainObjectArgumentsPickedUpAsOperation() {
		
		Domain.lookupAny(Man.class);
		Domain.lookupAny(Woman.class);
		domainClass = Domain.lookupAny(Priest.class);
		domainClass.getDomain().done();

		EOperation eOperation = domainClass.getEOperationNamed("marry");
		assertNotNull(eOperation);
		assertEquals("marry", eOperation.getName());
		assertEquals(2, eOperation.getEParameters().size());
		assertTrue(domainClass.isParameterADomainObject(eOperation, 0));
		// HACK
		IRuntimeDomainClass<?> eMarryFirstArgClass = (IRuntimeDomainClass)domainClass.getDomainClassFor(eOperation, 0);
		assertSame(Man.class, eMarryFirstArgClass.getJavaClass());
		
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

	public void incompletetestCanInvokeOperationReturningVoidWithNoArgs() {
		// TODO
	}
	
	public void incompletetestCanInvokeOperationReturningDomainObjectWithNoArgs() {
		// TODO
	}
	
	public void incompletetestCanInvokeOperationReturningVoidWithPrimitiveArgs() {
		// TODO
	}
	
	public void incompletetestCanInvokeOperationReturningDomainObjectWithPrimitiveArgs() {
		// TODO
	}
	
	public void incompletetestCanInvokeOperationReturningVoidWithDomainObjectArgs() {
		// TODO
	}
	
	public void incompletetestCanInvokeOperationReturningDomainObjectWithDomainObjectArgs() {
		// TODO
	}
	
	public void testAccessorsNotPickedUpAsOperation() {
		domainClass = Domain.lookupAny(AppointmentWithAccessor.class);
		assertEquals(2, domainClass.operations().size());
	}

	public void testMutatorsNotPickedUpAsOperation() {
		domainClass = Domain.lookupAny(AppointmentWithAccessor.class);
		assertEquals(2, domainClass.operations().size());
	}

	public void testSingleReferencessNotPickedUpAsOperation() {
		domainClass = Domain.lookupAny(AppointmentWithAccessor.class);
		assertEquals(2, domainClass.operations().size());
	}

	public void testCollectionsNotPickedUpAsOperation() {
		domainClass = Domain.lookupAny(AppointmentWithAccessor.class);
		assertEquals(2, domainClass.operations().size());
	}

}
