package org.essentialplatform.runtime.transaction;

import java.util.List;
import java.util.Set;

import org.essentialplatform.runtime.transaction.ITransactionManager;
import org.essentialplatform.runtime.transaction.changes.AttributeChange;
import org.essentialplatform.runtime.transaction.changes.ChangeSet;
import org.essentialplatform.runtime.transaction.changes.IChange;
import org.essentialplatform.runtime.transaction.event.ITransactionListener;

/**
 * Represents a transaction either in progress, committed or (potentially)
 * rolled back.
 * 
 * <p>
 * For a single user (client-side instance) there can be several transactions 
 * in progress at one time.  Most commonly there will be a single transaction
 * for each object that has been modified and for which the user has not yet
 * committed the change in the UI (eg through File>Save).  For example, the
 * user might edit an attribute of one pojo, then modify a set of attributes of
 * some other pojo by invoking some operation, then edit a couple of attributes
 * on a third pojo.  Here there would be three concurrent transactions, all
 * in progress.
 * 
 * <p>
 * However, the relationship between transactions and pojos is not strictly
 * 1:1.  The classic example is a transfer of money between two bank accounts.
 * This might result in the instantiation of a new object (a Transfer object),
 * and a modification of two existing objects (the Accounts from which the 
 * money is being removed/added).  Thus this transaction has <i>enlisted</i>
 * three objects; all of the changes must be made together, or none at all.
 *
 * <p>
 * One responsibility of the {@link ITransactionManager} is to ensure that
 * any given pojo can be enlisted in no more than one transaction at a time.
 * Any operation that would cause this to occur is prevented.  
 * 
 * <p>
 * What this means in practice is that the user may have a set of 
 * in-progress transactions, and each will have a set of enlisted pojos 
 * (typically only one), and the set of pojos will be disjoint.
 * 
 * <p>
 * If a user makes a further change on a pojo that is already in a transaction,
 * then that change will be added to the same transaction.  If the "change" is
 * invoking an operation (rather than editing an attribute) then it's possible
 * that further pojos will be enlisted in the same transaction.
 *  
 * <p>
 * Each user's interaction or change is internally represented as a
 * {@link ChangeSet}.  This in turn is a collection of {@link IChange}s.  If
 * the user simply edits a attribute, then the {@link ChangeSet} will consist
 * of a single work atom - a {@link AttributeChange} in fact.  
 * On the other hand, if the user has invoked an operation then several 
 * attributes in potentially different pojos might have been modified; in 
 * this case the {@link ChangeSet} will contain several {@link IChange}s.
 * 
 * <p>
 * The user's view of a transaction is as this set of changes, and it's important
 * to realize that these changes are pending - they have not been committed to 
 * any persistent object store.  As such, the user can undo or redo these
 * changes as they wish.  Any implementation of {@link IChange} must be able
 * to be undone.
 * 
 * <p>
 * Whenever the user chooses, the transaction may be committed.  At this point
 * all pending changes are made persistent in the persistent object store
 * through {@link #commit()}.  However, the commit could fail, for example
 * as a result of a concurrency failure (another user changing the same data
 * at the same time) or a uniqueness constraint or referential integrity 
 * failing.  In such a caes the transaction will move to a state of
 * {@link ITransaction.State#ABORTED}. 
 * 
 * <p>
 * Assuming the transaction is committed successfully however, it is 
 * effectively immutable and can only be treated as a single unit.  The user 
 * may be able to reverse (back out or undo) even a committed transaction, 
 * ({@link ITransaction#reverse()}), provided that the transaction is 
 * reversible (@link ITransaction#isIrreversible()} returns <code>false</code>).
 * However, the user will not be able to undo individual changes of a committed 
 * transaction.  What's important to realize is that performing a reverse of 
 * an transaction is in fact a new transaction so far as the persistent object 
 * store is concerned (eg an RDBMS transaction).  It is similarly possible for 
 * such a reverse to fail; again the transaction will move to a state of 
 * {@link ITransaction.State#ABORTED}.
 * 
 * <p>
 * Given that a (committed) transaction can be reversed, it's also possible 
 * (through {@link ITransaction#reapply()} for the user to reapply it if they 
 * decide that they do - after all - want to have that transaction committed to 
 * the persistent object store.  Again though an attempt to reapply a reversed 
 * transaction may fail, moving the state to {@link ITransaction.State#ABORTED}.
 * If the transaction is reapplied then it moves back to 
 * {@link ITransation.TransactionalState#COMMITTED}. 
 * 
 * <p>
 * Superficially the undo/redo of pending changes vs the reverse/reapply of 
 * committed transations sound similar, however there are some important
 * differences to bear in mind:
 * <ul>
 * <li> undo/redo of pending changes applies to in-memory changes of an
 *      still in-progress (uncommitted) transation; reverse/reapply 
 *      relates to entire transactions that have been committed to the 
 *      persistent object store.
 * <li> undo and redo will always succeed (even if any of the 
 *      {@link IChange}s are irreversible).  On the other hand a transaction
 *      may not be reversible - precisely if any of its {@link IChange}s are 
 *      irreversible). 
 * <li> it is possible to undo/redo individual changes of a transaction, however
 *      a transaction many only be reversed/reapplied in its entirety.
 * <li> the state of a transaction both before and after undo/redo remains 
 *      unchanged (as {@link ITransaction.State#IN_PROGRESS}.  On the other 
 *      hand a reverse/reapply (or even just a commit) may fail; the state 
 *      will move (to {@link ITransaction.State#ABORTED}.
 * <li> undo/redo are held in stacks - it is only possible to undo the most
 *      recent change, and then the change before that, and so on.  In
 *      contrast, the user can (attempt to) reverse any reversible transaction
 *      in any order.  (Remember that other users are potentially making 
 *      changes to the persistent object store at the same time; we don't expose
 *      transactions made by others to the current user, and there's no need
 *      to insist that changes are reversed in any given order.  Of course,
 *      the older the transaction the more chance of a reverse of it failing
 *      (due to concurrency checks and so forth). 
 * </ul>
 */
public interface ITransaction {

	/**
	 * A unique identifier for this transaction (unique for the current user, 
	 * at least).
	 * 
	 * <p>
	 * Since the user can reverse/reapply any transaction, they need some way
	 * of identifying them.  The expected implementation is just the date and
	 * time.  The user can always look at the changes of the transaction 
	 * (via {@link #getCommittedChanges()} to confirm the exact changes made.
	 */
	public String id();
	
	
	/**
	 * @return the current state
	 */
	public State getState();

	/**
	 * Start a new change in state, eg as a result of the user editing a
	 * attribute in the UI or of invoking an operation.
	 *
	 * <p>
	 * While a change is underway (until {@link #completingInteraction()} is called)
	 * the transaction temporarily changes its state.
	 *
	 * <p>
	 * Preconditions
	 * <ul>
	 * <li> either in a state of {@link ITransaction.State#IN_PROGRESS}, or
	 *      can already be in state of {@link ITransaction.State#BUILDING_CHANGE}.
	 *      Use {@link #getState()} to check that this precondition is met.
	 * <li> must be the current transaction (as returned by
	 *      {@link ITransactionManager#getCurrentTransaction()}.
	 * </ul>
	 * 
	 * <p>
	 * Postconditions
	 * <ul>
	 * <li> state of {@link ITransaction.State#BUILDING_CHANGE}.
	 * <li> internally, a new {@link IChange} (in fact, a {@link ChangeSet} 
	 *      to hold a collection of atoms) is added to the current stack.
	 * <li> any changes that had been undone and were ready to be redone
	 *      (using {@link #redoPendingChange()}) will be cleared from the
	 *      redo stack.
	 * <li> Returns <code>true</code> if was state was previously
	 *      {@link ITransaction.State#IN_PROGRESS}, indicating that an
	 *      interaction was indeed started, otherwise returns <code>false</code>.
	 * </ul>
	 * 
	 * @return <code>true</code> if moved into {@link ITransaction.State#BUILDING_CHANGE}, 
	 *         <code>false</code> if was already there.
	 */
	public boolean startingInteraction() throws IllegalStateException;

	/**
	 * Add a {@link IChange} in the current change.
	 * 
	 * <p>
	 * The {@link IChange} in turn indicates the pojos that it affects, so 
	 * this is effectively the way in which pojos are enlisted into the
	 * transaction.
	 *  
	 * <p>
	 * Preconditions
	 * <ul>
	 * <li> in a state of {@link ITransaction.State#BUILDING_CHANGE}, else throws
	 *      an {@link IllegalStateException}.  Use {@link #getState()} to check
	 *      that this precondition is met.
	 * <li> must be the current transaction (as returned by
	 *      {@link ITransactionManager#getCurrentTransaction()}.
	 * </ul>
	 * 
	 * <p>
	 * Postconditions
	 * <ul>
	 * <li> state of {@link ITransaction.State#BUILDING_CHANGE}.
	 * <li> internally, the {@link IChange} is added in the current change
	 *      ({@link ChangeSet})
	 * <li> any pojos affected by the change are enlisted into the transaction.
	 * </ul>
	 * 
	 * @param workAtom
	 * @return <code>true</code> if the change can be added to the transaction,
	 *         <code>false</code> otherwise (ie if would enlist a pojo already
	 *         enlisted in some other current transaction).
	 */
	public boolean addingToInteractionChangeSet(IChange change) throws IllegalStateException;


	/**
	 * Indicate that the set of modifications to pojo(s) that make up a single 
	 * change are completed.
	 * 
	 * <p>
	 * Preconditions
	 * <ul>
	 * <li> in a state of {@link ITransaction.State#BUILDING_CHANGE}, else throws
	 *      an {@link IllegalStateException}.  Use {@link #getState()} to check
	 *      that this precondition is met.
	 * <li> must be the current transaction (as returned by
	 *      {@link ITransactionManager#getCurrentTransaction()}.
	 * </ul>
	 * 
	 * <p>
	 * Postconditions
	 * <ul>
	 * <li> state of {@link ITransaction.State#IN_PROGRESS}.
	 * <li> current changes (added through {@link #addingToInteractionChangeSet(IChange)} 
	 *      popped to stack as a new change set.
	 * <li> listeners notified through {@link ITransactionListener#addedChange(TransactionEvent)}
	 * <li> current change set reset.
	 * </ul>
	 * 
	 * @throws IllegalStateException
	 * @return this transaction
	 */
	public ITransaction completingInteraction() throws IllegalStateException;

	
	/**
	 * Undo most recent change made within this transaction.
	 * 
	 * <p>
	 * Preconditions
	 * <ul>
	 * <li> in a state of {@link ITransaction.State#IN_PROGRESS}, else throws
	 *      an {@link IllegalStateException}.  Use {@link #getState()} to check
	 *      that this precondition is met.
	 * <li> must have at least one change, again else throws an 
	 *      {@link IllegalStateException}.  Check the size of the list
	 *      returned from {@link #getUndoableChanges()} to ensure that this
	 *      precondition is met.
	 * <li> must be the current transaction (as returned by
	 *      {@link ITransactionManager#getCurrentTransaction()}.
	 * </ul>
	 * 
	 * <p>
	 * Postconditions
	 * <ul>
	 * <li> state of {@link ITransaction.State#IN_PROGRESS}.
	 * <li> the change undone (a {@link ChangeSet} is removed from the undo
	 *      stack and added to the redo stack such that it may be redone using
	 *      {@link #redoPendingChange()} 
	 * <li> any pojos are un-enlisted
	 * </ul>
	 * 
	 * @throws IllegalStateException - if this transaction is not currently in progress,
	 *                                 or if there are no pending changes.
	 * @return this transaction
	 */
	public ITransaction undoPendingChange() throws IllegalStateException;
	

	/**
	 * Redo most recent undone change for this transaction. 
	 * 
	 * <p>
	 * Preconditions
	 * <ul>
	 * <li> in a state of {@link ITransaction.State#IN_PROGRESS}, else throws
	 *      an {@link IllegalStateException}.  Use {@link #getState()} to check
	 *      that this precondition is met.
	 * <li> must have had at least one change undone and no changes added
	 *      subsequently (in other words, have at least one change in the redo
	 *      stack).  Otherwise, throws an {@link IllegalStateException}.  Check 
	 *      the size of the list returned from {@link #getRedoableChanges()} to 
	 *      ensure that this precondition is met.
	 * <li> must be the current transaction (as returned by
	 *      {@link ITransactionManager#getCurrentTransaction()}.
	 * </ul>
	 * 
	 * <p>
	 * Postconditions
	 * <ul>
	 * <li> state of {@link ITransaction.State#IN_PROGRESS}.
	 * <li> the change redone (a {@link ChangeSet} is removed from the redo
	 *      stack and added to a undo stack such that it may be undone using
	 *      {@link #undoPendingChange()} 
	 * <li> any pojos are re-enlisted
	 * </ul>
	 * 
	 * @throws IllegalStateException - if there are no changes to redo, or if 
	 *         the transaction is not currently in progress.
	 * @return this transaction
	 */
	public ITransaction redoPendingChange() throws IllegalStateException;


	/**
	 * Undoes all changes (or groups of changes) in this in-progress 
	 * {@link ITransaction} and then notifies the {@link ITransactionManager}
	 * to discard this transaction (effectively deleting it).
	 * 
	 * <p>
	 * A user may make several discrete interactions as part of an overall
	 * {@link ITransaction} - change a field on this object, invoke an operation
	 * there, change another field, then save.  This would be three changes, 
	 * with the middle one (the invoking of an operation) potentially causing
	 * a set of modifications changes in its own right.
	 * 
	 * <p>
	 * Whereas {@link #undoPendingChange()} will undone only the most recent change 
	 * (unedit a field, or un-invoke an operation), this method will undo
	 * all changes, effectively taking the state of the objects back as they
	 * where at the last commit.  It then informs the {@link ITransactionManager}
	 * to discards it completely.
	 * 
	 * <p>
	 * Preconditions
	 * <ul>
	 * <li> in a state of {@link ITransaction.State#IN_PROGRESS}, else throws
	 *      an {@link IllegalStateException}.  Use {@link #getState()} to check
	 *      that this precondition is met.
	 * <li> must have at least one change, again else throws an 
	 *      {@link IllegalStateException}.  Check the size of the list
	 *      returned from {@link #getUndoableChanges()} to ensure that this
	 *      precondition is met.
	 * <li> must be the current transaction (as returned by
	 *      {@link ITransactionManager#getCurrentTransaction()}.
	 * </ul>
	 * 
	 * <p>
	 * Postconditions
	 * <ul>
	 * <li> state of {@link ITransaction.State#DISCARDED}.
	 * <li> all changes are undone (a {@link ChangeSet} and removed from the undo
	 *      stack
	 * <li> any pojos are un-enlisted
	 * <li> the {@link ITransactionManager} discards the transaction. 
	 * </ul>
	 * 
	 * @throws IllegalStateException - if this transaction is not currently in 
	 *                                 progress, or if there are no pending changes.
	 * @return this transaction
	 */
	public ITransaction discard() throws IllegalStateException;
	

	/**
	 * Redo all and any undone changes. 
	 *
	 * <p>
	 * If there have been any changes that have been undone, either by 
	 * {@link #discard()} <b>or</b> by multiple calls to 
	 * {@link #undoPendingChange()}, then this method will allow them all to be
	 * redone.
	 * 
	 * <p>
	 * Preconditions
	 * <ul>
	 * <li> in a state of {@link ITransaction.State#IN_PROGRESS}, else throws
	 *      an {@link IllegalStateException}.  Use {@link #getState()} to check
	 *      that this precondition is met.
	 * <li> must have had at least one change undone and no changes added
	 *      subsequently (in other words, have at least one change in the redo
	 *      stack).  Otherwise, throws an {@link IllegalStateException}.  Check 
	 *      the size of the list returned from {@link #getRedoableChanges()} to 
	 *      ensure that this precondition is met.
	 * <li> must be the current transaction (as returned by
	 *      {@link ITransactionManager#getCurrentTransaction()}.
	 * </ul>
	 * 
	 * <p>
	 * Postconditions
	 * <ul>
	 * <li> state of {@link ITransaction.State#IN_PROGRESS}.
	 * <li> the changes redone (a {@link ChangeSet} are removed from the redo
	 *      stack and added to the undo stack such that they may be undone using
	 *      {@link #undoPendingChange()} 
	 * </ul>
	 * 
	 * @return this transaction
	 * @throws IllegalStateException - if cannot redo, or if there the 
	 *         transaction currently in progress.
	 */
	public ITransaction redoPendingChanges() throws IllegalStateException;
	

	/**
	 * Commit this transaction.
	 * 
	 * <p>
	 * Preconditions
	 * <ul>
	 * <li> in a state of {@link ITransaction.State#IN_PROGRESS}, else throws
	 *      an {@link IllegalStateException}.
	 * <li> must be the current transaction (as returned by
	 *      {@link ITransactionManager#getCurrentTransaction()}.
	 * </ul>
	 * 
	 * <p>
	 * Postconditions
	 * <ul>
	 * <li> state of either {@link ITransaction.State#COMMITTED} or 
	 *      {@link ITransaction.State#ABORTED} depending on whether the
	 *      changes encapsulated by the transaction were successfully applied 
	 *      to the persistent object store.
	 * <li> changes made to the persistent object store if successful. 
	 * </ul>
	 * 
	 * @return this transaction
	 */
	public ITransaction commit() throws IllegalStateException;

	
	/**
	 * Reverse (back out or undo) this transaction.
	 * 
	 * <p>
	 * Preconditions
	 * <ul>
	 * <li> in a state of {@link ITransaction.State#COMMITTED}, else throws
	 *      an {@link IllegalStateException}.
	 * </ul>
	 * 
	 * <p>
	 * Postconditions
	 * <ul>
	 * <li> state of either {@link ITransaction.State#REVERSED} or 
	 *      {@link ITransaction.State#ABORTED} depending on whether the
	 *      changes encapsulated by the transaction were successfully applied 
	 *      to the persistent object store. 
	 * <li> changes made to the persistent object store if successful. 
	 * </ul>
	 * 
	 * @return this transaction
	 */
	public ITransaction reverse() throws IllegalStateException;

	
	/**
	 * Reapply (un-reverse) this transaction.
	 * 
	 * <p>
	 * Preconditions
	 * <ul>
	 * <li> in a state of {@link ITransaction.State#REVERSED}, else throws
	 *      an {@link IllegalStateException}.
	 * </ul>
	 * 
	 * <p>
	 * Postconditions
	 * <ul>
	 * <li> state of either {@link ITransaction.State#COMMITTED} or 
	 *      {@link ITransaction.State#ABORTED} depending on whether the
	 *      changes encapsulated by the transaction were successfully applied 
	 *      to the persistent object store. 
	 * <li> changes made to the persistent object store if successful. 
	 * </ul>
	 * 
	 * @return this transaction
	 */
	public ITransaction reapply()  throws IllegalStateException;


	/**
	 * Whether the transaction contains a ({@link IChange} in a) change that 
	 * indicates that it is irreversible.
	 * 
	 * <p>
	 * Although "irreversible" work atoms can be undone while the transaction
	 * is still pending, they cannot be rolled back once the transation is 
	 * committed.
	 * 
	 * @return
	 */
	public boolean isIrreversible();
	
	/**
	 * Exposes all changes that have been performed by the user, are pending 
	 * commit but which are also potentially undoable by the user.
	 * 
	 * <p>
	 * The returned list is immutable.
	 * 
	 * @throws IllegalStateException if called when not in state of IN_PROGRESS.
	 * @return
	 */
	public List<ChangeSet> getUndoableChanges();
	
	/**
	 * Exposes all changes that have been undone but that may potentially be
	 * redone by the user.
	 * 
	 * <p>
	 * The returned list is immutable.
	 * 
	 * @throws IllegalStateException if called when not in state of IN_PROGRESS.
	 * @return
	 */
	public List<ChangeSet> getRedoableChanges();

	/**
	 * Exposes all changes that have been commited in this transaction. 
	 * 
	 * <p>
	 * The changes are aggregated into a single immutable {@link ChangeSet}.
	 * 
	 * @throws IllegalStateException if called when not in state of COMMITTED or REVERSED
	 * @return
	 */
	public ChangeSet getCommittedChanges() throws IllegalStateException;
	

		
	/**
	 * The collection of pojos that have been modified by any of the 
	 * {@link IChange}s that have been added to this transaction.
	 * 
	 * @return
	 */
	public Set<ITransactable> getEnlistedPojos();
	
	/**
	 * Asserts that the transaction is in one of the supplied states.
	 * 
	 * <p>
	 * Throws an IllegalStateException otherwise.
	 * 
	 * @param requiredStates
	 * @throws IllegalStateException
	 * 
	 * @return this transaction
	 */
	public ITransaction checkInState(ITransaction.State... requiredStates) throws IllegalStateException;
	
	/**
	 * Returns whether the transaction is in one of the supplied states.
	 * 
	 * @param requiredStates
	 */
	public boolean isInState(ITransaction.State... requiredStates);

	/**
	 * Add a listener.
	 * 
	 * @param listener
	 */
	public <T extends ITransactionListener> T addTransactionListener(T listener);

	/**
	 * Remove a listener.
	 * 
	 * @param listener
	 */
	public void removeTransactionListener(ITransactionListener listener);

	/**
	 * The state of a {@link ITransaction}.
	 */
	public static enum State {

		/**
		 * not yet committed, moreover in the middle of making a change
		 * ({@link ITransaction#startingInteraction()} has been invoked but
		 * {@link ITransaction#completingInteraction()} has not.
		 * 
		 * <p>
		 * This is a temporary state that should be short-lived; cannot 
		 * undo/redo pending changes. 
		 */
		BUILDING_CHANGE("BUILDING_CHANGE"),
		/**
		 * Not yet committed, though there is no change currently being
		 * built up.
		 * 
		 * <p>
		 * Can undo/redo changes that have already been completed in this
		 * transaction.
		 */
		IN_PROGRESS("IN_PROGRESS"),
		/**
		 * prior to commit, all pending changes were undone and the transaction 
		 * discarded; any further attempt to use will fail.
		 */
		DISCARDED("DISCARDED"),
		/**
		 * committed (perhaps as the result of a re-apply.
		 */
		COMMITTED("COMMITTED"),
		/**
		 * reversed, could potentially be re-applied.
		 */
		REVERSED("REVERSED"),
		/**
		 * an attempt to commit, reverse or reapply a transaction has failed; 
		 * the transaction can no longer be used.
		 */
		ABORTED("ABORTED"),
		;
		

		private String _name;
		private State(final String name) {
			this._name = name;
		}
		public String toString() {
			return _name;
		}
	}

}
