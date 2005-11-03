package org.essentialplatform.runtime.tests.transaction;

import org.essentialplatform.core.domain.Domain;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.runtime.session.IDomainObject;
import org.essentialplatform.runtime.tests.AbstractRuntimeTestCase;
import org.essentialplatform.transaction.Calculator;
import org.essentialplatform.transaction.Customer;
import org.essentialplatform.transaction.EmailAddress;
import org.essentialplatform.transaction.Order;

public abstract class AbstractTransactionManagerTestCase extends AbstractRuntimeTestCase {

	protected IDomainClass calculatorDomainClass;
	protected IDomainClass customerDomainClass;
	protected IDomainClass emailAddressDomainClass;
	protected IDomainClass orderDomainClass;

	protected IDomainObject<Calculator> calculatorDomainObject;
	protected IDomainObject<Customer> customerDomainObject;
	protected IDomainObject<EmailAddress> emailAddressDomainObject;
	protected IDomainObject<EmailAddress> emailAddress2DomainObject;
	protected IDomainObject<EmailAddress> emailAddress3DomainObject;
	protected IDomainObject<Order> orderDomainObject;
	protected IDomainObject<Order> order2DomainObject;
	protected IDomainObject<Order> order3DomainObject;

	protected Calculator calculator;
	protected Customer customer;
	protected EmailAddress emailAddress;
	protected EmailAddress emailAddress2;
	protected EmailAddress emailAddress3;
	protected Order order;
	protected Order order2;
	protected Order order3;
	
	private final boolean _setupObjects;
	public AbstractTransactionManagerTestCase() {
		this(true);
	}
	public AbstractTransactionManagerTestCase(final boolean setupObjects) {
		super(null);
		this._setupObjects = setupObjects;
	}


	/**
	 * Creates (and commits) pojos for a calculator and a customer. 
	 */
	protected void setUp() throws Exception {
		super.setUp();
		calculatorDomainClass = Domain.lookupAny(Calculator.class);
		customerDomainClass = Domain.lookupAny(Customer.class);
		emailAddressDomainClass = Domain.lookupAny(EmailAddress.class);
		orderDomainClass = Domain.lookupAny(Order.class);
		
		if (_setupObjects) {
			calculatorDomainObject = session.recreate(calculatorDomainClass);
			customerDomainObject = session.recreate(customerDomainClass);
			emailAddressDomainObject = session.recreate(emailAddressDomainClass);
			emailAddress2DomainObject = session.recreate(emailAddressDomainClass);
			emailAddress3DomainObject = session.recreate(emailAddressDomainClass);
			orderDomainObject = session.recreate(orderDomainClass);
			order2DomainObject = session.recreate(orderDomainClass);
			order3DomainObject = session.recreate(orderDomainClass);

			calculator = calculatorDomainObject.getPojo();
			customer = customerDomainObject.getPojo();
			emailAddress = emailAddressDomainObject.getPojo();
			emailAddress2 = emailAddress2DomainObject.getPojo();
			emailAddress3 = emailAddress3DomainObject.getPojo();
			order = orderDomainObject.getPojo();
			order2 = order2DomainObject.getPojo();
			order3 = order3DomainObject.getPojo();
		}
	}

	protected void tearDown() throws Exception {
		
		calculator = null;
		customer = null;
		emailAddress = null;
		emailAddress2 = null;
		emailAddress3 = null;
		order = null;
		order2 = null;
		order3 = null;
		
		calculatorDomainObject = null;
		customerDomainObject = null;
		
		calculatorDomainClass = null;
		customerDomainClass = null;
		emailAddressDomainClass = null;
		orderDomainClass = null;
		
		super.tearDown();
	}

}