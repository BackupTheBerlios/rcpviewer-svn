package de.berlios.rcpviewer.transaction.internal;

import java.lang.reflect.Field;

import de.berlios.rcpviewer.progmodel.standard.InDomain;
import de.berlios.rcpviewer.transaction.ITransactable;
import de.berlios.rcpviewer.transaction.ITransaction;
import de.berlios.rcpviewer.transaction.ITransactionManager;
import de.berlios.rcpviewer.session.IDomainObject;
import de.berlios.rcpviewer.session.PojoAspect;
import de.berlios.rcpviewer.session.IPojo;
import de.berlios.rcpviewer.session.ISession;
import de.berlios.rcpviewer.session.IObservedFeature;


public abstract aspect TransactionChangeAspect extends TransactionAspect {

	/**
	 * Keeps track of the current transaction for this thread (if any)
	 */
	private static ThreadLocal<ITransaction> __transactionByThread;
	static {
		__transactionByThread = new ThreadLocal<ITransaction>() {
	        protected synchronized ITransaction initialValue() {
	            return null;
	        }
		};
	}
	
	Object around(IPojo pojo, Object postValue): changeAttributeOnPojo(pojo, postValue) {
		ITransactable transactable = (ITransactable)pojo;
		ITransaction transaction = currentTransaction(transactable);
		
		IDomainObject domainObject = pojo.getDomainObject();
		if (domainObject != null) {
			Field field = getFieldFor(thisJoinPointStaticPart);
			if (field != null) {
				AttributeChange change = new AttributeChange(transactable, field, postValue);
				transaction.addToChange(change);
			}
		}
		return proceed(pojo, postValue); // equivalent to executing the fmwa	
	}

	/**
	 * Intent is for this to be called when a change (work group) has been completed.
	 * 
	 * <p>
	 * However, trying to invoke when change is complete was resulting in an
	 * infinite loop.
	 */
	protected void externalStateChangedForObservedFeatures(IPojo pojo) {
		
		IDomainObject domainObject = pojo.getDomainObject();
		if (domainObject != null) {
			ISession session = domainObject.getSession();
			for(IObservedFeature observedFeature: session.getObservedFeatures()) {
				observedFeature.externalStateChanged();
			}
		}
	}


	
	/**
	 * Returns either the current transaction for this thread or the transaction
	 * in which the {@link ITransactable} has already been enlisted.
	 * 
	 * <p>
	 * If there is already a current transaction for both the thread and for
	 * the transactable object <b>and they don't match</b>, then discards the 
	 * change on the current transaction and throws a runtime exception.
	 * 
	 * <p>
	 * Before returning, also ensures that the transaction is in a state of
	 * either in progress or building state.
	 */
	protected ITransaction currentTransaction(ITransactable transactable) throws RuntimeException {
		ITransaction transaction = getTransactionForThreadIfAny();
		ITransaction pojoTransaction = transactable.getTransaction(false);
		if (transaction != null && 
		    pojoTransaction != null) {
			if (transaction != pojoTransaction) {
				transaction.discardChange();
				throw new RuntimeException("Object already enlisted in another transaction - discarding change.");
			}
			return transaction;
		} else if (transaction     != null &&
		           pojoTransaction == null) {
			// nothing to do; will use transaction on thread already
		} else if (transaction     == null &&
		           pojoTransaction != null) {
			transaction = pojoTransaction;
		} else {
			// ... if transaction == null && pojoTransaction == null
			transaction = getTransactionManager().createTransaction();
		}
		transaction.checkInState(ITransaction.State.IN_PROGRESS, ITransaction.State.BUILDING_CHANGE);
		return transaction;
	}
	
	protected ITransaction getTransactionForThreadIfAny() {
		return __transactionByThread.get();
	}

	/**
	 * Whether there is already a transaction for this thread.
	 */
	protected boolean hasTransactionForThread() {
		return getTransactionForThreadIfAny() != null;
	}

	protected void clearTransactionForThread() {
		__transactionByThread.set(null);
	}

	protected void setTransactionForThread(final ITransaction transaction) {
		__transactionByThread.set(transaction);
	}

}
