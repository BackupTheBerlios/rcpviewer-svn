package org.essentialplatform.transaction;

import java.util.Iterator;
import java.util.Set;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EOperation;

import org.essentialplatform.AbstractRuntimeTestCase;
import org.essentialplatform.domain.IDomainClass;
import org.essentialplatform.domain.Domain;
import org.essentialplatform.persistence.IObjectStore;
import org.essentialplatform.persistence.inmemory.InMemoryObjectStore;
import org.essentialplatform.progmodel.standard.EssentialProgModelExtendedSemanticsDomainBuilder;
import org.essentialplatform.progmodel.standard.ProgModelConstants;
import org.essentialplatform.session.IDomainObject;
import org.essentialplatform.session.ISession;
import org.essentialplatform.session.local.SessionManager;
import org.essentialplatform.transaction.internal.TransactionManager;
import junit.framework.TestCase;
import org.essentialplatform.transaction.TestTransactionManagerStartTransaction;

/**
 * @see TestTransactionManagerStartTransaction
 * @author Dan Haywood
 */
public class TestTransactionManagerStartTransactionInstantiate extends AbstractTransactionManagerTestCase {

	public TestTransactionManagerStartTransactionInstantiate() {
		super(false); // don't bother to setup any objects.
	}

	public void testInstantiatingAnObjectImplicitlyStartsATransaction() {
		IDomainObject<?> domainObject = session.create(customerDomainClass);
		Customer customer = (Customer)domainObject.getPojo();
		assertEquals(1, transactionManager.getCurrentTransactions().size());
		ITransaction transaction = transactionManager.getCurrentTransactionFor(customer);
		assertNotNull(transaction);
		assertSame(ITransaction.State.IN_PROGRESS, transaction.getState());
	}


}
