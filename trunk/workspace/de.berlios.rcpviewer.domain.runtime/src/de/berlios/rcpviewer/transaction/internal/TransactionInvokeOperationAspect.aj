package de.berlios.rcpviewer.transaction.internal;

import de.berlios.rcpviewer.session.IDomainObject;
import de.berlios.rcpviewer.session.PojoAspect;
import de.berlios.rcpviewer.session.ISession;
import de.berlios.rcpviewer.session.IObservedFeature;
import de.berlios.rcpviewer.session.IPojo;
import de.berlios.rcpviewer.transaction.IChange;
import de.berlios.rcpviewer.transaction.ITransactable;
import de.berlios.rcpviewer.transaction.ITransaction;
import de.berlios.rcpviewer.transaction.ITransactionManager;
import de.berlios.rcpviewer.progmodel.standard.InDomain;

import java.lang.reflect.Field;

import org.apache.log4j.*;

/**
 * One change per invoked operation that hasn't been called from another
 * invoked operation.
 * 
 * <p>
 * TODO: this aspect may be wholly redundant?? 
 */
public aspect TransactionInvokeOperationAspect extends TransactionChangeAspect
	percflow(transactionalChange(IPojo)) {

	private final static Logger LOG = Logger.getLogger(TransactionInvokeOperationAspect.class);
	private static boolean ENABLED = true;
	protected Logger getLogger() { return LOG; }
	protected boolean isEnabled() { return ENABLED; }
	
	declare precedence: TransactionInvokeOperationAspect, TransactionAttributeChangeAspect; 
	declare precedence: TransactionInvokeOperationAspect, TransactionOneToOneReferenceChangeAspect; 
	declare precedence: TransactionInvokeOperationAspect, TransactionCollectionChangeAspect; 
	
	protected pointcut changingPojo(IPojo pojo): invokeOperationOnPojo(pojo) ;


}
