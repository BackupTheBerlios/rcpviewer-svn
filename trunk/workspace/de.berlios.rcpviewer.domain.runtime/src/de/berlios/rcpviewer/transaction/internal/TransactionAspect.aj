package de.berlios.rcpviewer.transaction.internal;

import de.berlios.rcpviewer.session.PojoAspect;
import de.berlios.rcpviewer.transaction.ITransaction;
import de.berlios.rcpviewer.transaction.ITransactionManager;

public abstract aspect TransactionAspect extends PojoAspect {
	
	/**
	 * (Will be) Injected.
	 */
	private ITransactionManager _transactionManager = TransactionManager.INSTANCE;

	
	/**
	 * The {@link ITransactionManager} from which to obtain the current 
	 * {@link ITransaction}.
	 * 
	 * <p>
	 * TODO: until we are using dependency injection, falls back and uses
	 * the (temporary) {@link de.berlios.rcpviewer.transaction.internal.TransactionManager#INSTANCE}
	 * 
	 */
	public ITransactionManager getTransactionManager() {
		return _transactionManager;
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
