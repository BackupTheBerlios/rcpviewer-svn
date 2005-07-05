package de.berlios.rcpviewer.session;

import org.eclipse.emf.ecore.EOperation;

import de.berlios.rcpviewer.AbstractRuntimeTestCase;
import de.berlios.rcpviewer.domain.IRuntimeDomainClass;
import de.berlios.rcpviewer.progmodel.standard.operation.CustomerOperationReturningDomainObject;
import de.berlios.rcpviewer.progmodel.standard.operation.CustomerOperationReturningVoid;
import de.berlios.rcpviewer.progmodel.standard.operation.Order;

public class TestDomainObjectOperation extends AbstractRuntimeTestCase {

	public TestDomainObjectOperation() {
		super(null);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testCanInvokeOperationReturningVoid() {
		IRuntimeDomainClass<CustomerOperationReturningVoid> domainClass = 
			(IRuntimeDomainClass<CustomerOperationReturningVoid>) lookupAny(CustomerOperationReturningVoid.class);

		IDomainObject<CustomerOperationReturningVoid> domainObject = 
			(IDomainObject<CustomerOperationReturningVoid>) session.createTransient(domainClass);
		CustomerOperationReturningVoid pojo = domainObject.getPojo();
		
		EOperation placeOrderOperation = domainObject.getEOperationNamed("placeOrder");
		assertFalse(pojo.orderPlaced);
		Object retval = domainObject.invokeOperation(placeOrderOperation, new Object[] {});
		assertTrue(pojo.orderPlaced);
		assertNull(retval);
	}

 	public void testCanInvokeOperationReturningDomainObject() {
		IRuntimeDomainClass<CustomerOperationReturningDomainObject> domainClass = 
			(IRuntimeDomainClass<CustomerOperationReturningDomainObject>) lookupAny(CustomerOperationReturningDomainObject.class);

		IDomainObject<CustomerOperationReturningDomainObject> domainObject = 
			(IDomainObject<CustomerOperationReturningDomainObject>) session.createTransient(domainClass);
		CustomerOperationReturningDomainObject pojo = domainObject.getPojo();
		
		EOperation placeOrderOperation = domainObject.getEOperationNamed("placeOrder");
		Object retval = domainObject.invokeOperation(placeOrderOperation, new Object[] {});
		assertNotNull(retval);
		assertTrue(retval instanceof Order);
	}

 	public void testCanInvokeOperationReturningPrimitive() {
		IRuntimeDomainClass<CustomerOperationReturningDomainObject> domainClass = 
			(IRuntimeDomainClass<CustomerOperationReturningDomainObject>) lookupAny(CustomerOperationReturningDomainObject.class);

		IDomainObject<CustomerOperationReturningDomainObject> domainObject = 
			(IDomainObject<CustomerOperationReturningDomainObject>) session.createTransient(domainClass);
		CustomerOperationReturningDomainObject pojo = domainObject.getPojo();

		// place a few orders...
		EOperation placeOrderOperation = domainObject.getEOperationNamed("placeOrder");
		domainObject.invokeOperation(placeOrderOperation, new Object[] {});
		domainObject.invokeOperation(placeOrderOperation, new Object[] {});
		domainObject.invokeOperation(placeOrderOperation, new Object[] {});

		// how many?
		EOperation numberOfOrdersPlacedOperation = domainObject.getEOperationNamed("numberOfOrdersPlaced");
		assertNotNull(numberOfOrdersPlacedOperation);
		Object retval = domainObject.invokeOperation(numberOfOrdersPlacedOperation, new Object[] {});
		assertNotNull(retval);
		assertTrue(retval instanceof Integer);
		assertEquals(3, ((Integer)retval).intValue());
	}

 	public void incompletetestCanInvokeOperationReturningValueObject() {
		// TODO
	}

 	public void incompletetestCanInvokeOperationWithNoArgs() {
		// TODO
	}

	public void incompletetestCanInvokeOperationWithPrimitiveArgs() {
		// TODO
	}

	public void incompletetestCanInvokeOperationWithValueArgs() {
		// TODO
	}

	public void incompletetestCanInvokeOperationWithDomainObjectArgs() {
		// TODO
	}

	public void incompletetestCanInvokeOperationWithMixOfArgs() {
		// TODO
	}

}
