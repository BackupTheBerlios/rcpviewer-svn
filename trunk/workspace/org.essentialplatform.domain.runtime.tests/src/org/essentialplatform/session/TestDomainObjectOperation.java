package org.essentialplatform.session;

import org.eclipse.emf.ecore.EOperation;

import org.essentialplatform.AbstractRuntimeTestCase;
import org.essentialplatform.domain.IDomainClass;
import org.essentialplatform.progmodel.standard.EssentialProgModelExtendedSemanticsDomainBuilder;
import org.essentialplatform.progmodel.standard.operation.CustomerOperationReturningDomainObject;
import org.essentialplatform.progmodel.standard.operation.CustomerOperationReturningVoid;
import org.essentialplatform.progmodel.standard.operation.Order;

public class TestDomainObjectOperation extends AbstractRuntimeTestCase {

	public TestDomainObjectOperation() {
		super(new EssentialProgModelExtendedSemanticsDomainBuilder());
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testCanInvokeOperationReturningVoid() {
		IDomainClass domainClass = 
			(IDomainClass) lookupAny(CustomerOperationReturningVoid.class);
		domain.addBuilder(getDomainBuilder());
		domain.done();

		IDomainObject<?> domainObject = session.create(domainClass);
		CustomerOperationReturningVoid pojo = (CustomerOperationReturningVoid)domainObject.getPojo();
		
		IDomainObject.IObjectOperation placeOrderOperation = domainObject.getOperation(domainObject.getEOperationNamed("placeOrder"));
		assertFalse(pojo.orderPlaced);
		Object retval = placeOrderOperation.invokeOperation(new Object[] {});
		assertTrue(pojo.orderPlaced);
		assertNull(retval);
	}

 	public void testCanInvokeOperationReturningDomainObject() {
		IDomainClass domainClass = 
			(IDomainClass) lookupAny(CustomerOperationReturningDomainObject.class);
		domain.addBuilder(getDomainBuilder());
		domain.done();

		IDomainObject<?> domainObject = session.create(domainClass);
		
		IDomainObject.IObjectOperation placeOrderOperation = domainObject.getOperation(domainObject.getEOperationNamed("placeOrder"));
		Object retval = placeOrderOperation.invokeOperation(new Object[] {});
		assertNotNull(retval);
		assertTrue(retval instanceof Order);
	}

 	public void testCanInvokeOperationReturningPrimitive() {
		IDomainClass domainClass = 
			(IDomainClass) lookupAny(CustomerOperationReturningDomainObject.class);
		domain.addBuilder(getDomainBuilder());
		domain.done();
		
		IDomainObject<?> domainObject = session.create(domainClass);

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
