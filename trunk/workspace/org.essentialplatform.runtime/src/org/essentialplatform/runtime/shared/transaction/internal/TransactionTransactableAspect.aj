package org.essentialplatform.runtime.shared.transaction.internal;

import org.essentialplatform.runtime.shared.domain.IPojo;
import org.essentialplatform.runtime.shared.transaction.ITransactable;
import org.essentialplatform.runtime.shared.transaction.ITransaction;
import org.essentialplatform.runtime.shared.transaction.TransactionManager;
import org.essentialplatform.runtime.shared.domain.PojoAspect;

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
