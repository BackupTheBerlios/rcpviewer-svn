package org.essentialplatform.transaction.internal;

import org.essentialplatform.session.IPojo;
import org.essentialplatform.transaction.ITransactable;
import org.essentialplatform.transaction.ITransaction;

/**
 * Makes {@link IPojo}s implement {@link ITransactable}.
 */
public aspect TransactionTransactableAspect extends TransactionAspect {

	declare parents: IPojo implements ITransactable;

	declare precedence: TransactionTransactableAspect, TransactionInvokeOperationAspect;
	declare precedence: TransactionTransactableAspect, TransactionAttributeChangeAspect;
	declare precedence: TransactionTransactableAspect, TransactionOneToOneReferenceChangeAspect;
	declare precedence: TransactionTransactableAspect, TransactionAddToCollectionChangeAspect;
	declare precedence: TransactionTransactableAspect, TransactionRemoveFromCollectionChangeAspect;
	declare precedence: TransactionTransactableAspect, org.essentialplatform.session.NotifyListenersAspect;

	/**
	 * Introduce implementation of {@link ITransactable#getTransaction()}.
	 */
	public ITransaction ITransactable.getTransaction() {
		return TransactionTransactableAspect.aspectOf().getTransactionManager().getCurrentTransactionFor(this);
	}
	
	/**
	 * Introduce implementation of {@link ITransactable#getTransaction(boolean)}.
	 */
	public ITransaction ITransactable.getTransaction(final boolean autoEnlist) {
		return TransactionTransactableAspect.aspectOf().getTransactionManager().getCurrentTransactionFor(this, autoEnlist);
	}
	
}
