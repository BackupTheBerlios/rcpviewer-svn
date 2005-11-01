package org.essentialplatform.transaction.internal;

import org.essentialplatform.session.PojoAspect;
import org.essentialplatform.transaction.ITransaction;
import org.essentialplatform.transaction.ITransactionManager;

public abstract aspect TransactionAspect extends PojoAspect {
	
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