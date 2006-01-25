package org.essentialplatform.runtime.shared.transaction.event;

import org.essentialplatform.runtime.shared.transaction.ITransaction;
import org.essentialplatform.runtime.shared.transaction.changes.IChange;


/**
 * Listeners will be notified of any changes to a specific 
 * {@link ITransaction}.
 * 
 * @author Dan Haywood
 */
public interface ITransactionListener {


	/**
	 * Just started to add changes to the {@link ITransaction} as a result of
	 * an interaction.
	 * 
	 * <p>
	 * This event is triggered by {@link ITransaction#startingInteraction()};
	 * the transaction remains in this state for a short period of time.
	 *
	 * <p>
	 * The change set of the event will be null.
	 */
	public void buildingChanges(TransactionEvent event);

	/**
	 * An {@link IChange} has been added to the {@link ITransaction} as the
	 * result of an interaction.
	 * 
	 * <p>
	 * This event is triggered by {@link ITransaction#completingInteraction()},
	 * signalling that the set of changes as a result of some user's
	 * interaction has been completed.
	 * 
	 * <p>
	 * Use {@link TransactionEvent#getChange()} will be return the change.
	 * 
	 * @param event
	 */
	public void addedChange(TransactionEvent event);
	
	/**
	 * The most recent pending change has been undone for the {@link ITransaction}.
	 * 
	 * <p>
	 * Use {@link TransactionEvent#getChange()} will be return the change.
	 * 
	 * @param event
	 */
	public void undonePendingChange(TransactionEvent event);

	/**
	 * The most recent undone change has been redone for the {@link ITransaction}.
	 * 
	 * <p>
	 * Use {@link TransactionEvent#getChange()} will be return the change.
	 * 
	 * @param event
	 */
	public void redonePendingChange(TransactionEvent event);

	/**
	 * All undone changes have been redone for the {@link ITransaction}.
	 * 
	 * <p>
	 * Use {@link TransactionEvent#getChange()} will be return <code>null</code>.
	 * 
	 * @param event
	 */
	public void redonePendingChanges(TransactionEvent event);

	/**
	 * All pending changes have been undone for the {@link ITransaction} and
	 * the transaction has been discarded (effectively deleted).
	 * 
	 * <p>
	 * Use {@link TransactionEvent#getChange()} will be return <code>null</code>.
	 * 
	 * @param event
	 */
	public void discarded(TransactionEvent event);

	/**
	 * The {@link ITransaction} has been committed.
	 * 
	 * <p>
	 * Use {@link TransactionEvent#getChange()} will return <code>null</code>.
	 * 
	 * @param event
	 */
	public void committed(TransactionEvent event);

	/**
	 * The {@link ITransaction} has been reversed.
	 * 
	 * <p>
	 * Use {@link TransactionEvent#getChange()} will return <code>null</code>.
	 * 
	 * @param event
	 */
	public void reversed(TransactionEvent event);


	/**
	 * The {@link ITransaction} has been reapplied.
	 * 
	 * <p>
	 * Use {@link TransactionEvent#getChange()} will return <code>null</code>.
	 * 
	 * @param event
	 */
	public void reapplied(TransactionEvent event);
}
