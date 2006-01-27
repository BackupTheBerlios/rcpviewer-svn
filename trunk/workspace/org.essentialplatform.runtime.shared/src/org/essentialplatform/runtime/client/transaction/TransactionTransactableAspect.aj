package org.essentialplatform.runtime.client.transaction;

import org.essentialplatform.runtime.shared.domain.IPojo;
import org.essentialplatform.runtime.client.transaction.ITransactable;
import org.essentialplatform.runtime.client.transaction.ITransaction;
import org.essentialplatform.runtime.client.transaction.TransactionManager;

public aspect TransactionTransactableAspect {

	//declare parents: IPojo implements ITransactable;
//	/**
//	 * All pojos that are have an {@link InDomain} annotation should implement 
//	 * {@link IPojo}. 
//	 */
//	declare parents: (@InDomain *) implements IPojo;


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
