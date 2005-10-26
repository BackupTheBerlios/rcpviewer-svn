package de.berlios.rcpviewer.transaction;

import java.util.Iterator;
import java.util.Set;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EOperation;

import de.berlios.rcpviewer.AbstractRuntimeTestCase;
import de.berlios.rcpviewer.domain.Domain;
import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.persistence.IObjectStore;
import de.berlios.rcpviewer.persistence.inmemory.InMemoryObjectStore;
import de.berlios.rcpviewer.progmodel.extended.ExtendedProgModelDomainBuilder;
import de.berlios.rcpviewer.progmodel.standard.ProgModelConstants;
import de.berlios.rcpviewer.session.IDomainObject;
import de.berlios.rcpviewer.session.ISession;
import de.berlios.rcpviewer.session.local.SessionManager;
import de.berlios.rcpviewer.transaction.internal.TransactionManager;
import junit.framework.TestCase;

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
		calculatorDomainClass = 
			(IDomainClass)Domain.lookupAny(Calculator.class);
		customerDomainClass = 
			(IDomainClass)Domain.lookupAny(Customer.class);
		emailAddressDomainClass = 
			(IDomainClass)Domain.lookupAny(EmailAddress.class);
		orderDomainClass = 
			(IDomainClass)Domain.lookupAny(Order.class);
		domain.addBuilder(new ExtendedProgModelDomainBuilder());
		domain.done();
		
		if (_setupObjects) {
			calculatorDomainObject = 
				(IDomainObject<Calculator>)session.recreate(calculatorDomainClass);
			customerDomainObject = 
				(IDomainObject<Customer>)session.recreate(customerDomainClass);
			emailAddressDomainObject = 
				(IDomainObject<EmailAddress>)session.recreate(emailAddressDomainClass);
			emailAddress2DomainObject = 
				(IDomainObject<EmailAddress>)session.recreate(emailAddressDomainClass);
			emailAddress3DomainObject = 
				(IDomainObject<EmailAddress>)session.recreate(emailAddressDomainClass);
			orderDomainObject = 
				(IDomainObject<Order>)session.recreate(orderDomainClass);
			order2DomainObject = 
				(IDomainObject<Order>)session.recreate(orderDomainClass);
			order3DomainObject = 
				(IDomainObject<Order>)session.recreate(orderDomainClass);

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
