package org.essentialplatform.runtime.client.transaction;

import java.util.List;

import org.essentialplatform.runtime.client.transaction.event.ITransactionManagerListener;
import org.essentialplatform.runtime.shared.domain.IPojo;

/**
 * Creates, tracks and records both in-progress and committed 
 * {@link ITransaction}s.
 * 
 * <p>
 * There can be multiple {@link ITransaction}s in progress at any one time, 
 * though the sets of pojos that each will affect (@link ITransaction#getEnlistedPojos()}
 * will be disjoint.  It is the responsibility of the transaction manager to
 * keep track of which {@link ITransaction} (if any) applies to which pojo and
 * for ensuring that the enlisted sets remain disjoint. 
 * 
 * <p>
 * It is possible to reverse or reapply {@link ITransaction}s once they are
 * committed; this interface specifies methods to do this using the id of the
 * transaction ((@link ITransaction#id()}).
 *  
 * 
 */
public interface ITransactionManager {

	/**
	 * All currently in-progress {@link ITransaction}s.
	 * 
	 * <p>
	 * The returned list is immutable.
	 * 
	 * @return
	 */
	public List<ITransaction> getCurrentTransactions();
	
	/**
	 * All committed {@link ITransaction}s.
	 * 
	 * <p>
	 * Note that committed transactions are not necessarily reversible; use
	 * {@link ITransaction#isIrreversible()} to check.
	 * 
	 * <p>
	 * The returned list is immutable.  
	 * 
	 * @return
	 */
	public List<ITransaction> getCommittedTransactions();
	
	/**
	 * All {@link ITransaction}s that have previously been committed and then
	 * successfully reversed.
	 * 
	 * <p>
	 * The returned list is immutable.
	 * 
	 * @return
	 */
	public List<ITransaction> getReversedTransactions();
	
	
	/**
	 * Returns the current {@link ITransaction} for the specified pojo, creating
	 * one if necessary and enlisting the pojo into it.
	 * 
	 * <p>
	 * If a transaction is created, then it will have a state of
	 * {@link ITransaction.State#IN_PROGRESS}.
	 * 
	 * @param transactable - the object enlisted in a transaction.
	 * @return
	 */
	public ITransaction getCurrentTransactionFor(Object pojo);
	
	/**
	 * Returns the current {@link ITransaction} for the specified pojo, creating
	 * one if necessary provided that <code>autoEnlist</code> is set to <code>true</code>.
	 * 
	 * <p>
	 * If a transaction is created, then it will have a state of
	 * {@link ITransaction.State#IN_PROGRESS}.  If there is no transaction and
	 * the <code>autoEnlist</code> is set to <code>false</code>, will simply
	 * return <code>null</code>.
	 * 
	 * @param transactable - the object enlisted in a transaction.
	 * @param autoEnlist - whether to automatically create a transaction if none exists.
	 * @return
	 */
	public ITransaction getCurrentTransactionFor(Object pojo, final boolean autoEnlist);

	/**
	 * Commit a transaction for an enlisted pojo.
	 * 
	 * <p>
	 * The pojo must have been enlisted within an 
	 * {@link ITransaction.State#IN_PROGRESS} transaction, otherwise an
	 * exception will be thrown.
	 * 
	 * <p>
	 * Implementations are expected to delegate to {@link ITransaction#commit()}.
	 *   
	 * @param transactable - the object enlisted in a transaction.
	 * 
	 * @throws IllegalStateException - if the current transaction is not
	 *         in progress.
	 * @throws IllegalArgumentException - if no transaction could be located
	 *         for the pojo
	 * @throws RuntimeException if transaction could not be reversed.
	 */
	public void commit(Object pojo) throws IllegalStateException, IllegalArgumentException;

	/**
	 * Reverse a transaction.
	 * 
	 * <p>
	 * The transaction must be in a state of {@link ITransaction.State#COMMITTED}.
	 * 
	 * <p>
	 * Implementations are expected to delegate to {@link ITransaction#reverse()()}.
	 *   
	 * @param transactionId - the unique identifier of this transaction.
	 * 
	 * @throws IllegalStateException - if the current transaction is still
	 *         in progress.
	 * @throws RuntimeException if transaction could not be reversed.
	 */
	public void reverse(final String transactionId) throws IllegalStateException;


	/**
	 * Reapply a transaction.
	 * 
	 * <p>
	 * The transaction must be in a state of {@link ITransaction.State#REVERSED}.
	 * 
	 * <p>
	 * Implementations are expected to delegate to {@link ITransaction#reapply()}).
	 *   
	 * @param transactionId - the unique identifier of the transaction to reapply.
	 * 
	 * @throws IllegalStateException - if the transaction has not just
	 *         been reversed (is in a state of {@link ITransaction.State#REVERSED})
	 *         or there is a transaction in progress).
	 * @throws RuntimeException if transaction could not be reapplied.
	 */
	public void reapply(final String transactionId) throws IllegalStateException;


	/**
	 * Add a listener.
	 * 
	 * @param listener
	 */
	public void addTransactionManagerListener(ITransactionManagerListener listener);

	/**
	 * Remove a listener.
	 * 
	 * @param listener
	 */
	public void removeTransactionManagerListener(ITransactionManagerListener listener);

	/**
	 * Discard all state.
	 * 
	 * <p>
	 * For testing purposes.
	 */
	public void reset();

	/**
	 * A bit of a hack, but suspends the real transactions (no-op transactions
	 * are created instead).
	 * 
	 * <p>
	 * Used while the bootstrapping of domain classes is being done, but also
	 * useful for unit tests.
	 *
	 * @see #resume()
	 */
	public void suspend();


	/**
	 * A bit of a hack, but resumes the creation of real transactions following 
	 * an earlier call to {@link #suspend()}.
	 * 
	 * <p>
	 * Used while the bootstrapping of domain classes is being done, but also
	 * useful for unit tests.
	 *
	 * @see #suspend()
	 */
	public void resume();

}
