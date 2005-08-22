package de.berlios.rcpviewer.transaction;

import de.berlios.rcpviewer.transaction.ITransaction;


/**
 * Listeners will be notified of any changes to a specific 
 * {@link ITransaction}.
 * 
 * @author Dan Haywood
 */
public interface ITransactionListener {

	/**
	 * The {@link ITransaction} has been committed.
	 * 
	 * @param event
	 */
	public void committed(TransactionEvent event);

	/**
	 * The {@link ITransaction} has been reversed.
	 * 
	 * @param event
	 */
	public void reversed(TransactionEvent event);


	/**
	 * The {@link ITransaction} has been reapplied.
	 * 
	 * @param event
	 */
	public void reapplied(TransactionEvent event);
}
