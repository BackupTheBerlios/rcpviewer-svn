package de.berlios.rcpviewer.transaction;

import de.berlios.rcpviewer.transaction.ITransaction;


/**
 * Listeners will be notified of any changes to a specific 
 * {@link ITransaction}.
 * 
 * @author Dan Haywood
 */
public interface ITransactionManagerListener {

	/**
	 * A {@link ITransaction} has been committed.
	 * 
	 * @param event
	 */
	public void committedTransaction(TransactionManagerEvent event);

	/**
	 * A {@link ITransaction} has been reversed.
	 * 
	 * @param event
	 */
	public void reversedTransaction(TransactionManagerEvent event);


	/**
	 * A {@link ITransaction} has been reapplied.
	 * 
	 * @param event
	 */
	public void reappliedTransaction(TransactionManagerEvent event);
}
