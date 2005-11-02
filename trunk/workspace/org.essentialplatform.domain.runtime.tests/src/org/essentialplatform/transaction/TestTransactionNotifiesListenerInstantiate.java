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
import org.essentialplatform.transaction.internal.AttributeChange;
import org.essentialplatform.transaction.internal.TransactionManager;
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
