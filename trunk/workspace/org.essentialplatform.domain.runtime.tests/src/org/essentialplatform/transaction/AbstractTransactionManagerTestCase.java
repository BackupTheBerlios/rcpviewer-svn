package org.essentialplatform.transaction;

import java.util.Iterator;
import java.util.Set;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EOperation;

import org.essentialplatform.AbstractRuntimeTestCase;
import org.essentialplatform.domain.Domain;
import org.essentialplatform.domain.IDomainClass;
import org.essentialplatform.persistence.IObjectStore;
import org.essentialplatform.persistence.inmemory.InMemoryObjectStore;
import org.essentialplatform.progmodel.standard.EssentialProgModelExtendedSemanticsDomainBuilder;
import org.essentialplatform.progmodel.standard.ProgModelConstants;
import org.essentialplatform.session.IDomainObject;
import org.essentialplatform.session.ISession;
import org.essentialplatform.session.local.SessionManager;
import org.essentialplatform.transaction.internal.TransactionManager;
import junit.framework.TestCase;

public abstract class AbstractTransactionManagerTestCase extends AbstractRuntimeTestCase {

	protected IDomainClass calculatorDomainClass;
	protected IDomainClass customerDomainClass;
	protected IDomainClass emailAddressDomainClass;
	protected IDomainClass orderDomainClass;

	protected IDomainObject<?> calculatorDomainObject;
	protected IDomainObject<?> customerDomainObject;
	protected IDomainObject<?> emailAddressDomainObject;
	protected IDomainObject<?> emailAddress2DomainObject;
	protected IDomainObject<?> emailAddress3DomainObject;
	protected IDomainObject<?> orderDomainObject;
	protected IDomainObject<?> order2DomainObject;
	protected IDomainObject<?> order3DomainObject;

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
		domain.addBuilder(new EssentialProgModelExtendedSemanticsDomainBuilder());
		domain.done();
		
		if (_setupObjects) {
			calculatorDomainObject = session.recreate(calculatorDomainClass);
			customerDomainObject = session.recreate(customerDomainClass);
			emailAddressDomainObject = session.recreate(emailAddressDomainClass);
			emailAddress2DomainObject = session.recreate(emailAddressDomainClass);
			emailAddress3DomainObject = session.recreate(emailAddressDomainClass);
			orderDomainObject = session.recreate(orderDomainClass);
			order2DomainObject = session.recreate(orderDomainClass);
			order3DomainObject = session.recreate(orderDomainClass);

			calculator = (Calculator)calculatorDomainObject.getPojo();
			customer = (Customer)customerDomainObject.getPojo();
			emailAddress = (EmailAddress)emailAddressDomainObject.getPojo();
			emailAddress2 = (EmailAddress)emailAddress2DomainObject.getPojo();
			emailAddress3 = (EmailAddress)emailAddress3DomainObject.getPojo();
			order = (Order)orderDomainObject.getPojo();
			order2 = (Order)order2DomainObject.getPojo();
			order3 = (Order)order3DomainObject.getPojo();
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
