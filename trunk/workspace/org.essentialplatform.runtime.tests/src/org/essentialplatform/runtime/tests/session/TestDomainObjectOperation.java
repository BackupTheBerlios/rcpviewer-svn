package org.essentialplatform.runtime.tests.session;

import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.fixture.progmodel.essential.standard.operation.CustomerOperationReturningDomainObject;
import org.essentialplatform.core.fixture.progmodel.essential.standard.operation.CustomerOperationReturningVoid;
import org.essentialplatform.core.fixture.progmodel.essential.standard.operation.Order;
import org.essentialplatform.runtime.domain.IDomainObject;
import org.essentialplatform.runtime.tests.AbstractRuntimeTestCase;

public class TestDomainObjectOperation extends AbstractRuntimeTestCase {

	public void testCanInvokeOperationReturningVoid() {
		IDomainClass domainClass = lookupAny(CustomerOperationReturningVoid.class);

		IDomainObject<CustomerOperationReturningVoid> domainObject = session.create(domainClass);
		CustomerOperationReturningVoid pojo = domainObject.getPojo();
		
		IDomainObject.IObjectOperation placeOrderOperation = domainObject.getOperation(domainObject.getEOperationNamed("placeOrder"));
		assertFalse(pojo.orderPlaced);
		Object retval = placeOrderOperation.invokeOperation(new Object[] {});
		assertTrue(pojo.orderPlaced);
		assertNull(retval);
	}

 	public void testCanInvokeOperationReturningDomainObject() {
		IDomainClass domainClass = lookupAny(CustomerOperationReturningDomainObject.class);

		IDomainObject<CustomerOperationReturningDomainObject> domainObject = session.create(domainClass);
		
		IDomainObject.IObjectOperation placeOrderOperation = domainObject.getOperation(domainObject.getEOperationNamed("placeOrder"));
		Object retval = placeOrderOperation.invokeOperation(new Object[] {});
		assertNotNull(retval);
		assertTrue(retval instanceof Order);
	}

 	public void testCanInvokeOperationReturningPrimitive() {
		IDomainClass domainClass = lookupAny(CustomerOperationReturningDomainObject.class);
		
		IDomainObject<CustomerOperationReturningDomainObject> domainObject = session.create(domainClass);

		// place a few orders...
		IDomainObject.IObjectOperation placeOrderOperation = domainObject.getOperation(domainObject.getEOperationNamed("placeOrder"));
		placeOrderOperation.invokeOperation(new Object[] {});
		placeOrderOperation.invokeOperation(new Object[] {});
		placeOrderOperation.invokeOperation(new Object[] {});

		// how many?
		IDomainObject.IObjectOperation numberOfOrdersPlacedOperation = domainObject.getOperation(domainObject.getEOperationNamed("numberOfOrdersPlaced"));
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
