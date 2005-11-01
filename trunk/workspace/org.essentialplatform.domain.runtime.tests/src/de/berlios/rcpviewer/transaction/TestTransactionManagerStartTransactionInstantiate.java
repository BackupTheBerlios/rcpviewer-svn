package de.berlios.rcpviewer.transaction;

import java.util.Iterator;
import java.util.Set;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EOperation;

import de.berlios.rcpviewer.AbstractRuntimeTestCase;
import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.domain.Domain;
import de.berlios.rcpviewer.persistence.IObjectStore;
import de.berlios.rcpviewer.persistence.inmemory.InMemoryObjectStore;
import de.berlios.rcpviewer.progmodel.standard.EssentialProgModelExtendedSemanticsDomainBuilder;
import de.berlios.rcpviewer.progmodel.standard.ProgModelConstants;
import de.berlios.rcpviewer.session.IDomainObject;
import de.berlios.rcpviewer.session.ISession;
import de.berlios.rcpviewer.session.local.SessionManager;
import de.berlios.rcpviewer.transaction.internal.TransactionManager;
import junit.framework.TestCase;
import de.berlios.rcpviewer.transaction.TestTransactionManagerStartTransaction;

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
