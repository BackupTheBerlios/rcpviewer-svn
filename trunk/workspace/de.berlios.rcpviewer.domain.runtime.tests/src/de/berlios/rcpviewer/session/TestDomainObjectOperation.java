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
		
		IDomainObject.IOperation placeOrderOperation = domainObject.getOperation(domainObject.getEOperationNamed("placeOrder"));
		assertFalse(pojo.orderPlaced);
		Object retval = placeOrderOperation.invokeOperation(new Object[] {});
		assertTrue(pojo.orderPlaced);
		assertNull(retval);
	}

 	public void testCanInvokeOperationReturningDomainObject() {
		IRuntimeDomainClass<CustomerOperationReturningDomainObject> domainClass = 
			(IRuntimeDomainClass<CustomerOperationReturningDomainObject>) lookupAny(CustomerOperationReturningDomainObject.class);

		IDomainObject<CustomerOperationReturningDomainObject> domainObject = 
			(IDomainObject<CustomerOperationReturningDomainObject>) session.createTransient(domainClass);
		
		IDomainObject.IOperation placeOrderOperation = domainObject.getOperation(domainObject.getEOperationNamed("placeOrder"));
		Object retval = placeOrderOperation.invokeOperation(new Object[] {});
		assertNotNull(retval);
		assertTrue(retval instanceof Order);
	}

 	public void testCanInvokeOperationReturningPrimitive() {
		IRuntimeDomainClass<CustomerOperationReturningDomainObject> domainClass = 
			(IRuntimeDomainClass<CustomerOperationReturningDomainObject>) lookupAny(CustomerOperationReturningDomainObject.class);

		IDomainObject<CustomerOperationReturningDomainObject> domainObject = 
			(IDomainObject<CustomerOperationReturningDomainObject>) session.createTransient(domainClass);

		// place a few orders...
		IDomainObject.IOperation placeOrderOperation = domainObject.getOperation(domainObject.getEOperationNamed("placeOrder"));
		placeOrderOperation.invokeOperation(new Object[] {});
		placeOrderOperation.invokeOperation(new Object[] {});
		placeOrderOperation.invokeOperation(new Object[] {});

		// how many?
		IDomainObject.IOperation numberOfOrdersPlacedOperation = domainObject.getOperation(domainObject.getEOperationNamed("numberOfOrdersPlaced"));
		assertNotNull(numberOfOrdersPlacedOperation);
		Object retval = numberOfOrdersPlacedOperation.invokeOperation(new Object[] {});
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
