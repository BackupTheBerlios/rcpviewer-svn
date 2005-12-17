package org.essentialplatform.runtime.transaction.internal;

import org.essentialplatform.runtime.domain.IPojo;
import org.essentialplatform.runtime.transaction.*;
import org.essentialplatform.runtime.transaction.changes.*;

/**
 * Makes {@link IPojo}s implement {@link ITransactable}.
 */
public aspect TransactionTransactableAspect {

	declare parents: IPojo implements ITransactable;

	declare precedence: TransactionTransactableAspect, TransactionInvokeOperationAspect;
	declare precedence: TransactionTransactableAspect, TransactionAttributeChangeAspect;
	declare precedence: TransactionTransactableAspect, TransactionOneToOneReferenceChangeAspect;
	declare precedence: TransactionTransactableAspect, TransactionAddToCollectionChangeAspect;
	declare precedence: TransactionTransactableAspect, TransactionRemoveFromCollectionChangeAspect;
	declare precedence: TransactionTransactableAspect, org.essentialplatform.runtime.domain.NotifyListenersAspect;

	/**
	 * Introduce implementation of {@link ITransactable#currentTransaction()}.
	 * 
	 */
	public ITransaction ITransactable.currentTransaction() {
		return TransactionManager.instance().getCurrentTransactionFor(this);
	}
	
	/**
	 * Introduce implementation of {@link ITransactable#currentTransaction(boolean)}.
	 */
	public ITransaction ITransactable.currentTransaction(final boolean autoEnlist) {
		return TransactionManager.instance().getCurrentTransactionFor(this, autoEnlist);
	}
	
}
