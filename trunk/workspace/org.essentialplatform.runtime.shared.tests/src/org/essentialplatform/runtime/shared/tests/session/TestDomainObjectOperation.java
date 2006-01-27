package org.essentialplatform.runtime.shared.tests.session;

import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.fixture.progmodel.essential.standard.operation.CustomerOperationReturningDomainObject;
import org.essentialplatform.core.fixture.progmodel.essential.standard.operation.CustomerOperationReturningVoid;
import org.essentialplatform.core.fixture.progmodel.essential.standard.operation.Order;
import org.essentialplatform.runtime.client.domain.bindings.IObjectOperationClientBinding;
import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.tests.AbstractRuntimeClientTestCase;

public class TestDomainObjectOperation extends AbstractRuntimeClientTestCase {

	public void testCanInvokeOperationReturningVoid() {
		try {
			Class.forName("org.essentialplatform.runtime.client.transaction.TransactionInvokeOperationAspect");
		} catch (ClassNotFoundException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
		IDomainClass domainClass = lookupAny(CustomerOperationReturningVoid.class);

		IDomainObject<CustomerOperationReturningVoid> domainObject = clientSession.create(domainClass);
		CustomerOperationReturningVoid pojo = domainObject.getPojo();
		
		IDomainObject.IObjectOperation op = 
			domainObject.getOperation(domainObject.getIOperationNamed("placeOrder"));
		IObjectOperationClientBinding opBinding = (IObjectOperationClientBinding)op.getBinding();
		assertFalse(pojo.orderPlaced);
		Object retval = opBinding.invokeOperation();
		assertTrue(pojo.orderPlaced);
		assertNull(retval);
	}

 	public void testCanInvokeOperationReturningDomainObject() {
		IDomainClass domainClass = lookupAny(CustomerOperationReturningDomainObject.class);

		IDomainObject<CustomerOperationReturningDomainObject> domainObject = clientSession.create(domainClass);
		
		IDomainObject.IObjectOperation op = 
			domainObject.getOperation(domainObject.getIOperationNamed("placeOrder"));
		IObjectOperationClientBinding opBinding = (IObjectOperationClientBinding)op.getBinding();
		Object retval = opBinding.invokeOperation();
		assertNotNull(retval);
		assertTrue(retval instanceof Order);
	}

 	public void testCanInvokeOperationReturningPrimitive() {
		IDomainClass domainClass = lookupAny(CustomerOperationReturningDomainObject.class);
		
		IDomainObject<CustomerOperationReturningDomainObject> domainObject = clientSession.create(domainClass);

		// place a few orders...
		IDomainObject.IObjectOperation placeOrderOp = 
			domainObject.getOperation(domainObject.getIOperationNamed("placeOrder"));
		IObjectOperationClientBinding placeOrderOpBinding = (IObjectOperationClientBinding)placeOrderOp.getBinding();
		placeOrderOpBinding.invokeOperation();
		placeOrderOpBinding.invokeOperation();
		placeOrderOpBinding.invokeOperation();

		// how many?
		IDomainObject.IObjectOperation numberOfOrdersPlacedOp = 
			domainObject.getOperation(domainObject.getIOperationNamed("numberOfOrdersPlaced"));
		IObjectOperationClientBinding numberOfOrdersPlacedOpBinding = (IObjectOperationClientBinding)numberOfOrdersPlacedOp.getBinding();
		assertNotNull(numberOfOrdersPlacedOp);
		Object retval = numberOfOrdersPlacedOpBinding.invokeOperation();
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
