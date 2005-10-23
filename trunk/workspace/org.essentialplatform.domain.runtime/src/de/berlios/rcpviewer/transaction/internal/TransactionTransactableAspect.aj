package de.berlios.rcpviewer.transaction.internal;

import de.berlios.rcpviewer.session.IPojo;
import de.berlios.rcpviewer.transaction.ITransactable;
import de.berlios.rcpviewer.transaction.ITransaction;

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
