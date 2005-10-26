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
import de.berlios.rcpviewer.progmodel.extended.ExtendedProgModelDomainBuilder;
import de.berlios.rcpviewer.progmodel.standard.ProgModelConstants;
import de.berlios.rcpviewer.session.IDomainObject;
import de.berlios.rcpviewer.session.ISession;
import de.berlios.rcpviewer.session.local.SessionManager;
import de.berlios.rcpviewer.transaction.internal.AttributeChange;
import de.berlios.rcpviewer.transaction.internal.TransactionManager;
import junit.framework.TestCase;

/**
 * 
 * @author Dan Haywood
 */
public class TestTransactionNotifiesListenerInstantiate extends AbstractTransactionManagerTestCase {

	private MyTransactionListener listener = new MyTransactionListener(); 

	public TestTransactionNotifiesListenerInstantiate() {
		super(true);
	}

	@Override
	public void setUp() throws Exception {
		super.setUp();
		listener = new MyTransactionListener(); 
	}
	
	@Override
	public void tearDown() throws Exception {
		listener = null;
		super.tearDown();
	}
	
	/**
	 * This cannot be tested because we are not able to install the listener
	 * on the transaction
	 *
	 */
	public void testInstantiatingObjectNotifiesTransactionListenersOfAddingChange() {
	}


}
