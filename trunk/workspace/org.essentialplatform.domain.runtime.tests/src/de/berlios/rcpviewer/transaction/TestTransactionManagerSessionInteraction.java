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
import de.berlios.rcpviewer.transaction.internal.TransactionManager;
import junit.framework.TestCase;

/**
 * Modifying pojos enrols them in the transaction.
 * 
 * @author Dan Haywood
 *
 */
public class TestTransactionManagerSessionInteraction extends AbstractTransactionManagerTestCase {

	public TestTransactionManagerSessionInteraction() {
		super(); // setup objects, please.
	}


	public void testDummy() {
	}
	
	public void incompletetestInstantiatedObjectIsAttachedToSessionWhenCommitted() {
		
	}
	
	public void incompletetestDeletedObjectIsDetachedFromSessionWhenCommitted() {
		
	}
	

}
