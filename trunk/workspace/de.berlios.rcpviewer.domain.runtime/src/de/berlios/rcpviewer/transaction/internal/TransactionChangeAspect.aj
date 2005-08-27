package de.berlios.rcpviewer.transaction.internal;

import java.lang.reflect.Field;
import java.util.EnumSet;

import de.berlios.rcpviewer.progmodel.standard.InDomain;
import de.berlios.rcpviewer.transaction.ITransactable;
import de.berlios.rcpviewer.transaction.ITransaction;
import de.berlios.rcpviewer.transaction.ITransactionManager;
import de.berlios.rcpviewer.session.IDomainObject;
import de.berlios.rcpviewer.session.PojoAspect;
import de.berlios.rcpviewer.session.IPojo;
import de.berlios.rcpviewer.session.ISession;
import de.berlios.rcpviewer.session.IObservedFeature;
import de.berlios.rcpviewer.session.IResolvable.ResolveState;

import org.apache.log4j.Logger;

public abstract aspect TransactionChangeAspect extends TransactionAspect {

	/**
	 * Sub-aspects implement to control where logging goes.
	 */
	protected abstract Logger getLogger();
	
	/**
	 * Subaspects specify what constitutes "changing" the pojo
	 */
	abstract pointcut changingPojo(IPojo pojo); 

	protected pointcut transactionalChange(IPojo pojo): 
		changingPojo(pojo) &&
		if(resolveStateAppropriate(pojo)) &&
		!cflowbelow(invokeOperationOnPojo(IPojo)) ; 

	/**
	 * The states that a domain object must be in in order to be enrolled into
	 * a transaction.
	 * 
	 * TODO: should have a ResolvingAspect that dominates this aspect and 
	 * ensures that pojos are resolved before being interacted with.
	 */
	private final static EnumSet<ResolveState> TRANSACTIONAL_STATES = 
		EnumSet.of(ResolveState.TRANSIENT, ResolveState.RESOLVED);
	

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
				// this shouldn't happen
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
			transaction = getTransactionManagerImpl().createTransaction();
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

	/**
	 * Whether the state of this the {@link IDomainObject} corresponding to
	 * this pojo indicates that it has been resolved such that the pojo may
	 * be enrolled within a transaction.
	 * 
	 * <p>
	 * Note that although the resolve state of {@link IDomainObject} is set up
	 * in its constructor, we do have to check for a null reference.  That's
	 * because the aspect will fire even as the object is being constructed.
	 * If the resolve state is <code>null</code>, we therefore interpret as 
	 * not being in a state ready to be involved in transactions.
	 * 
	 * <p>
	 * In fact, since the _domainObject instance variable is introduced into
	 * the pojo Object, we have to check whether it exists. 
	 */
	protected static boolean resolveStateAppropriate(final IPojo pojo) {
		IDomainObject domainObject = pojo.getDomainObject();
		if (domainObject == null) {
			return false;
		}
		ResolveState resolveState = domainObject.getResolveState();
		return resolveState != null && TRANSACTIONAL_STATES.contains(resolveState);
	}
}
