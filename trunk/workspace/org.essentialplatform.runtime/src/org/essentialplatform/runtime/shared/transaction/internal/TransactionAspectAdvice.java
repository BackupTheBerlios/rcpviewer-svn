package org.essentialplatform.runtime.shared.transaction.internal;

import java.lang.reflect.Field;

import org.apache.log4j.Logger;
import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.domain.IPojo;
import org.essentialplatform.runtime.shared.transaction.ITransactable;
import org.essentialplatform.runtime.shared.transaction.ITransaction;
import org.essentialplatform.runtime.shared.transaction.ITransactionManager;
import org.essentialplatform.runtime.shared.transaction.PojoAlreadyEnlistedException;
import org.essentialplatform.runtime.shared.transaction.TransactionManager;

/**
 * Mirroring capabilities of TransactionAspect super-aspect.
 * 
 * @author Dan Haywood
 */
abstract class TransactionAspectAdvice extends AbstractAspectAdvice {


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
		ITransaction transaction = ThreadLocals.getTransactionForThreadIfAny();
		ITransaction pojoTransaction = transactable.currentTransaction(false);
		if (transaction != null && 
		    pojoTransaction != null) {
			if (transaction != pojoTransaction) {
				// this mustn't happen
				throw new PojoAlreadyEnlistedException(transactable, pojoTransaction);
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
		if (transaction != null) {
			// a bit of a hack, but createTransaction won't return a transaction if the
			// xact mgr has been suspended.
			transaction.checkInState(ITransaction.State.IN_PROGRESS, ITransaction.State.BUILDING_CHANGE);
		}
		return transaction;
	}
	

	//////////////////////////////////////////////////////////////////


	/**
	 * Whether the pojo can be enlisted in a transaction.
	 * 
	 * <p>
	 * We mustn't attempt to enlist a pojo that is being constructed.  The
	 * implementation of {@link DomainObject} is such we only set up its 
	 * persistence state and its resolve state at the end of its constructor.
	 * We will ignore objects whose state is not yet fully specified.
	 */
	protected boolean canBeEnlisted(final IPojo pojo) {
		IDomainObject domainObject = pojo.domainObject();
		if (domainObject == null) {
			return false;
		}
		return domainObject.getResolveState() != null &&
		       !domainObject.getResolveState().isUnknown() &&
		       domainObject.getPersistState() != null &&
		       !domainObject.getPersistState().isUnknown();
	}

	//////////////////////////////////////////////////////////////////



	/**
	 * (Will be) Injected.
	 */
	private ITransactionManager _transactionManager = TransactionManager.instance();

	
	/**
	 * The {@link ITransactionManager} from which to obtain the current 
	 * {@link ITransaction}.
	 * 
	 * <p>
	 * TODO: until we are using dependency injection, falls back and uses
	 * the (temporary) {@link org.essentialplatform.transaction.internal.TransactionManager#__instance}
	 * 
	 */
	public ITransactionManager getTransactionManager() {
		return _transactionManager;
	}
	TransactionManager getTransactionManagerImpl() {
		return (TransactionManager)_transactionManager;
	}
	/**
	 * TODO: In readiness for dependency injection.
	 */
	public void setTransactionManager(final ITransactionManager transactionManager) {
		if (transactionManager == null) {
			throw new IllegalArgumentException("ITransactionManager cannot be null");
		}
		_transactionManager = transactionManager;
	}
	

}
