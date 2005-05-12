package de.berlios.rcpviewer.progmodel.standard;

import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EOperation;

import de.berlios.rcpviewer.AbstractTestCase;
import de.berlios.rcpviewer.metamodel.IDomainClass;
import de.berlios.rcpviewer.metamodel.MetaModel;
import de.berlios.rcpviewer.progmodel.standard.impl.DomainMarker;
import de.berlios.rcpviewer.progmodel.standard.impl.ValueMarker;

import de.berlios.rcpviewer.metamodel.OperationKind;

/**
 * We use the {@link MetaModel} to register classes since the operations are
 * only identified via {@link MetaModel#done()}.
 * 
 * @author Dan Haywood
 *
 */
public class TestDomainClassOperations extends AbstractTestCase {

	private MetaModel metaModel;
	private IDomainClass<?> domainClass;
	protected void setUp() throws Exception {
		super.setUp();
		metaModel = new MetaModel();
	}

	protected void tearDown() throws Exception {
		metaModel = null;
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
	public static class TimePeriod implements ValueMarker {
		private java.util.Date from;
		public java.util.Date getFrom() {
			return from;
		}
		public void setFrom(java.util.Date from) {
			this.from = from;
		}
		private java.util.Date to;
		public java.util.Date getTo() {
			return to;
		}
		public void setTo(java.util.Date to) {
			this.to = to;
		}
	}
	public static class Appointment {
		public void moveTo(
				TimePeriod newPeriod, 
				String agenda) {
		}
		public static void createAt(
				TimePeriod timePeriod,
				String rationale) {
		}
	}


	/**
	 * Methods which are public are implicitly picked up as a user-invokable
	 * operation.
	 * 
	 * <p>
	 * Tested for both instance and static methods.
	 */
	public void testPublicVisibilityMethodPickedUpAsOperation() {
		domainClass = metaModel.register(CustomerWithPublicVisibilityOperation.class);
		metaModel.done();
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
		domainClass = new DomainClass<CustomerWithProtectedVisibilityOperation>(CustomerWithProtectedVisibilityOperation.class);
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
		domainClass = new DomainClass<CustomerWithPackageLocalVisibilityOperation>(CustomerWithPackageLocalVisibilityOperation.class);
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
		domainClass = new DomainClass<CustomerWithPrivateVisibilityOperation>(CustomerWithPrivateVisibilityOperation.class);
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
		domainClass = new DomainClass<CustomerWithProgrammaticPublicVisibilityOperation>(CustomerWithProgrammaticPublicVisibilityOperation.class);
		EOperation eOperation = domainClass.getEOperationNamed("placeOrder");
		assertNull(eOperation);
		
		eOperation = domainClass.getEOperationNamed("create");
		assertNull(eOperation);
	}

	public void testMethodWithNoArgumentsAPickedUpAsOperation() {
		domainClass = metaModel.register(CustomerWithNoArgOperation.class);
		metaModel.done();

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
		domainClass = metaModel.register(CustomerWithPrimitiveArgOperation.class);
		metaModel.done();

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
		domainClass = metaModel.register(CustomerPositionedOnMap.class);
		metaModel.done();

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
		domainClass = metaModel.register(Appointment.class);
		metaModel.done();
		
		EOperation eOperation = domainClass.getEOperationNamed("moveTo");
		assertNotNull(eOperation);
		assertEquals("moveTo", eOperation.getName());
		assertEquals(2, eOperation.getEParameters().size());
		assertTrue(domainClass.isParameterAValue(eOperation, 0));
		EDataType eDataType = domainClass.getEDataTypeFor(eOperation, 0);
		assertEquals("timePeriod", domainClass.getNameFor(eOperation, 0));
		assertEquals("de.berlios.rcpviewer.progmodel.standard.TestDomainClassOperations$TimePeriod", eDataType.getName());
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
		assertEquals("de.berlios.rcpviewer.progmodel.standard.TestDomainClassOperations$TimePeriod", eDataType.getName());
		assertTrue(domainClass.isParameterAValue(eOperation, 1));
		eDataType = domainClass.getEDataTypeFor(eOperation, 1);
		assertEquals("string", domainClass.getNameFor(eOperation, 1));
		assertEquals("EString", eDataType.getName());
		assertTrue(domainClass.isStatic(eOperation));
	}

	public static abstract class Person {
		private String name;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
	}
	public static class Man extends Person implements DomainMarker { }
	public static class Woman extends Person implements DomainMarker{ }
	public static class Priest implements DomainMarker {
		public void marry(Man m, Woman w) {
		}
	}
	/**
	 * Must register with metaModel rather than just instantiate since we need
	 * to lookup other {@link IDomainClass}es.
	 *
	 */
	public void testMethodWithDomainObjectArgumentsPickedUpAsOperation() {
		
		metaModel.register(Man.class);
		metaModel.register(Woman.class);
		domainClass = metaModel.register(Priest.class);
		metaModel.done();

		EOperation eOperation = domainClass.getEOperationNamed("marry");
		assertNotNull(eOperation);
		assertEquals("marry", eOperation.getName());
		assertEquals(2, eOperation.getEParameters().size());
		assertTrue(domainClass.isParameterADomainObject(eOperation, 0));
		IDomainClass<?> eMarryFirstArgClass = domainClass.getDomainClassFor(eOperation, 0);
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
	
}
