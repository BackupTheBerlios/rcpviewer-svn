package de.berlios.rcpviewer.progmodel.standard;

import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EOperation;

import de.berlios.rcpviewer.AbstractTestCase;
import de.berlios.rcpviewer.metamodel.*;
import de.berlios.rcpviewer.progmodel.standard.DomainClass;

public class TestDomainClassOperations extends AbstractTestCase {

	private IDomainClass domainClass;
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	

	public static class CustomerWithPublicVisibilityOperation {
		public void placeOrder() {}
		public static void create() {}
	}
	public static class CustomerWithProtectedVisibilityOperation {
		protected void placeOrder() {}
		protected static void create() {}
	}
	public static class CustomerWithPackageLocalVisibilityOperation {
		void placeOrder() {}
		static void create() {}
	}
	public static class CustomerWithPrivateVisibilityOperation {
		private void placeOrder() {}
		private static void create() {}
	}
	public static class CustomerWithProgrammaticPublicVisibilityOperation {
		@Programmatic
		public void placeOrder() {}
		@Programmatic
		public static void create() {}
	}
	public static class CustomerWithNoArgOperation {
		public void placeOrder() {}
		public static void create() {}
	}
	public static class CustomerWithPrimitiveArgOperation {
		public void rankAs(int rank) {}
		public static void createWithRank(long rank) {}
	}
	public static class CustomerPositionedOnMap {
		public void positionAt(float x, float y) {}
		public static void createAtPosition(double x, double y) {}
	}


	/**
	 * Methods which are public are implicitly picked up as a user-invokable
	 * operation.
	 * 
	 * <p>
	 * Tested for both instance and static methods.
	 */
	public void testPublicVisibilityMethodPickedUpAsOperation() {
		domainClass = new DomainClass(CustomerWithPublicVisibilityOperation.class);
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
		domainClass = new DomainClass(CustomerWithProtectedVisibilityOperation.class);
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
		domainClass = new DomainClass(CustomerWithPackageLocalVisibilityOperation.class);
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
		domainClass = new DomainClass(CustomerWithPrivateVisibilityOperation.class);
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
		domainClass = new DomainClass(CustomerWithProgrammaticPublicVisibilityOperation.class);
		EOperation eOperation = domainClass.getEOperationNamed("placeOrder");
		assertNull(eOperation);
		
		eOperation = domainClass.getEOperationNamed("create");
		assertNull(eOperation);
	}

	public void testMethodWithNoArgumentsAPickedUpAsOperation() {
		domainClass = new DomainClass(CustomerWithNoArgOperation.class);
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

	public void testMethodWithPrimitiveArgumentsPickedUpAsOperation() {
		// 1 arg
		domainClass = new DomainClass(CustomerWithPrimitiveArgOperation.class);
		EOperation eOperation = domainClass.getEOperationNamed("rankAs");
		assertNotNull(eOperation);
		assertEquals("rankAs", eOperation.getName());
		assertEquals(1, eOperation.getEParameters().size());
		assertTrue(domainClass.isParameterAValue(eOperation, 0));
		EDataType eDataType = domainClass.getEDataTypeFor(eOperation, 0);
		assertEquals("EInt", eDataType.getName());
		assertFalse(domainClass.isStatic(eOperation));
		
		eOperation = domainClass.getEOperationNamed("createWithRank");
		assertNotNull(eOperation);
		assertEquals("createWithRank", eOperation.getName());
		assertEquals(1, eOperation.getEParameters().size());
		assertTrue(domainClass.isParameterAValue(eOperation, 0));
		eDataType = domainClass.getEDataTypeFor(eOperation, 0);
		assertEquals("ELong", eDataType.getName());
		assertTrue(domainClass.isStatic(eOperation));

		// 2 arg
		domainClass = new DomainClass(CustomerPositionedOnMap.class);
		eOperation = domainClass.getEOperationNamed("positionAt");
		assertNotNull(eOperation);
		assertEquals("positionAt", eOperation.getName());
		assertEquals(2, eOperation.getEParameters().size());
		assertTrue(domainClass.isParameterAValue(eOperation, 0));
		eDataType = domainClass.getEDataTypeFor(eOperation, 0);
		assertEquals("EFloat", eDataType.getName());
		assertTrue(domainClass.isParameterAValue(eOperation, 1));
		eDataType = domainClass.getEDataTypeFor(eOperation, 1);
		assertEquals("EFloat", eDataType.getName());
		assertFalse(domainClass.isStatic(eOperation));
		
		eOperation = domainClass.getEOperationNamed("createAtPosition");
		assertNotNull(eOperation);
		assertEquals("createAtPosition", eOperation.getName());
		assertEquals(2, eOperation.getEParameters().size());
		assertTrue(domainClass.isParameterAValue(eOperation, 0));
		eDataType = domainClass.getEDataTypeFor(eOperation, 0);
		assertEquals("EDouble", eDataType.getName());
		assertTrue(domainClass.isParameterAValue(eOperation, 1));
		eDataType = domainClass.getEDataTypeFor(eOperation, 1);
		assertEquals("EDouble", eDataType.getName());
		assertTrue(domainClass.isStatic(eOperation));
	}

	public void xtestMethodWithValueObjectArgumentsPickedUpAsOperation() {
		// TODO
	}

	public void xtestMethodWithDomainObjectArgumentsPickedUpAsOperation() {
		// TODO
	}

	/**
	 * If a method has invalid arguments (not a value, not a domain object),
	 * then it is not exposed as an operation.
	 *
	 */
	public void xtestMethodWithInvalidArgumentsNotPickedUpAsOperation() {
		// TODO
	}

	/**
	 * Tested for both instance and static methods.
	 *
	 */
	public void xtestMethodWithMixedValidArgumentsPickedUpAsOperation() {
		// TODO
	}

	/**
	 * Tested for both instance and static methods.
	 *
	 */
	public void xtestMethodReturningVoidPickedUpAsOperation() {
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
	public void xtestMethodReturningPrimitiveNotPickedUpAsOperation() {
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
	public void xtestMethodReturningValueNotPickedUpAsOperation() {
		// TODO
	}

	/**
	 * Tested for both instance and static methods.
	 *
	 */
	public void xtestMethodReturningDomainObjectPickedUpAsOperation() {
		// TODO
	}

	public void xtestCanInvokeOperationReturningVoidWithNoArgs() {
		// TODO
	}
	
	public void xtestCanInvokeOperationReturningDomainObjectWithNoArgs() {
		// TODO
	}
	
	public void xtestCanInvokeOperationReturningVoidWithPrimitiveArgs() {
		// TODO
	}
	
	public void xtestCanInvokeOperationReturningDomainObjectWithPrimitiveArgs() {
		// TODO
	}
	
	public void xtestCanInvokeOperationReturningVoidWithDomainObjectArgs() {
		// TODO
	}
	
	public void xtestCanInvokeOperationReturningDomainObjectWithDomainObjectArgs() {
		// TODO
	}
	
}
