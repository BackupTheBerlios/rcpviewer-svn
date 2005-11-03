package org.essentialplatform.runtime.transaction;

import org.essentialplatform.runtime.transaction.ITransaction;


/**
 * Listeners will be notified of any changes to a specific 
 * {@link ITransaction}.
 * 
 * <p>
 * The callbacks in this interface reflect the {@link ITransactionManager}'s
 * primary responsibility of dealing with {@link ITransaction}s as units of
 * work.  It is also possible to listen to a {@link ITransaction} directly and
 * obtain more fine-grained information while still in-progress transactions.  
 * There is some duplication at the boundaries between the two listener
 * interfaces: on commit, reverse and reapply.  If a client were listening to 
 * both the transaction manager and a transaction then it would receive two 
 * callbacks ((eg  {@link #committedTransaction(TransactionManagerEvent)} and
 * {@link ITransactionListener#committed(TransactionEvent)}).
 * 
 * @author Dan Haywood
 */
public interface ITransactionManagerListener {


	/**
	 * A {@link ITransaction} has been created.
	 * 
	 * @param event
	 * @see ITransactionListener#committed(TransactionEvent)
	 */
	public void createdTransaction(TransactionManagerEvent event);

	/**
	 * A {@link ITransaction} has been committed.
	 * 
	 * 
	 * @param event
	 * @see ITransactionListener#committed(TransactionEvent)
	 */
	public void committedTransaction(TransactionManagerEvent event);

	/**
	 * A {@link ITransaction} has been reversed.
	 * 
	 * @param event
	 * @see ITransactionListener#reversed(TransactionEvent)
	 */
	public void reversedTransaction(TransactionManagerEvent event);


	/**
	 * A {@link ITransaction} has been reapplied.
	 * 
	 * @param event
	 * @see ITransactionListener#reapplied(TransactionEvent)
	 */
	public void reappliedTransaction(TransactionManagerEvent event);

	/**
	 * All pending changes in a {@link ITransaction} have been undone and the
	 * transaction effectively discarded (deleted).
	 * 
	 * @param event
	 * @see ITransactionListener#discarded(TransactionEvent)
	 */
	public void discardedTransaction(TransactionManagerEvent event);


}
