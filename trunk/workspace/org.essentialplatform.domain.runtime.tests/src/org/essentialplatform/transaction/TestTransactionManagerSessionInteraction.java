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
