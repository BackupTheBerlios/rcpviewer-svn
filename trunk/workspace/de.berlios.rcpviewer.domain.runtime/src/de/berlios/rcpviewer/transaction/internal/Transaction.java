package de.berlios.rcpviewer.transaction.internal;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import org.apache.log4j.Logger;

import de.berlios.rcpviewer.progmodel.extended.IAppContainer;
import de.berlios.rcpviewer.transaction.*;


/**
 * Implementation of {@link Transaction} as used by {@link de.berlios.rcpviewer.transaction.TransactionManager}
 */
public final class Transaction implements ITransaction {

	private final static Logger LOG = Logger.getLogger(Transaction.class);

	/**
	 * All {@link IChange}s in the current change.
	 * 
	 * <p>
	 * If there is no change currently being built up (if state is not
	 * {@link ITransaction.State#BUILDING_CHANGE}, then will be empty.
	 */
	private List<IChange> _changesInCurrentChange = new LinkedList<IChange>();
	/**
	 * List of all changes ({@link ChangeSet}s) that have been 
	 * performed in the context of this transaction.
	 * 
	 * <p>
	 * This stack is available to be undone.
	 */
	private Stack<ChangeSet> _changes = new Stack<ChangeSet>();
	/**
	 * Stack of all changes ({@link IWorkChain}s) that have been undone.
	 * 
	 * <p>
	 * This stack is available to be redone.
	 */
	private Stack<ChangeSet> _undoneChanges = new Stack<ChangeSet>();
	/**
	 * The {@link #_changes} is converted into an immutable work chain 
	 * when the transaction is {@link #commit()}ted.
	 * 
	 * <p>
	 * Will be set to <code>null</code> until the transaction has been
	 * committed.
	 */
	private ChangeSet _committedChanges;

	/**
	 * The current state of this transaction.
	 */
	private State _state;

	/**
	 * needs reference to actual implementation to get a hold
	 * of {@link TransactionManager#committed(Transaction)}.
	 */
	private final TransactionManager _transactionManager;
	
	private final IAppContainer _appContainer;
	
	Transaction(final TransactionManager transactionManager, final IAppContainer appContainer) {
		this._transactionManager   = transactionManager;
		this._appContainer = appContainer;
		this._state = ITransaction.State.IN_PROGRESS;
	}

	/**
	 * Listeners of this transaction.
	 */
    private final List<ITransactionListener> _listeners = new ArrayList<ITransactionListener>();
    

	/*
	 * @see de.berlios.rcpviewer.transaction.ITransaction#getState()
	 */
    public State getState() {
		return _state;
	}

	/*
	 * @see de.berlios.rcpviewer.transaction.ITransaction#startChange()
	 */
	public void startChange() {
		checkCurrentTransactionOfTransactionManager();
		checkInState(ITransaction.State.IN_PROGRESS);
		this._state = ITransaction.State.BUILDING_CHANGE;
	}

	/*
	 * @see de.berlios.rcpviewer.transaction.ITransaction#addToChange(de.berlios.rcpviewer.transaction.IChange)
	 */
	public void addToChange(final IChange change) {
		checkCurrentTransactionOfTransactionManager();
		checkInState(ITransaction.State.BUILDING_CHANGE);
		if (!change.equals(IChange.NULL)) {
			LOG.debug("adding change; change=" + change);
			_changesInCurrentChange.add(change);
			_transactionManager.enlist(this, change.getModifiedPojos());
		} else {
			LOG.debug("adding change - ignoring since NULL change");
		}
	}
	
	public void discardChange() {
		checkCurrentTransactionOfTransactionManager();
		checkInState(ITransaction.State.BUILDING_CHANGE);
		ChangeSet changesToDiscard = new ChangeSet(_changesInCurrentChange);
		LOG.debug("discarding " + _changesInCurrentChange.size() + " changes in undo stack");
		changesToDiscard.undo();
		_changesInCurrentChange.clear();
		this._state = ITransaction.State.IN_PROGRESS;
	}

	/*
	 * Creates a work chain out of the atoms in the current change, pops onto
	 * the stack of changes, and then clears down the work atoms list in
	 * readiness for the next change.
	 * 
	 * @see de.berlios.rcpviewer.transaction.ITransaction#completeChange()
	 */
	public void completeChange() {
		checkCurrentTransactionOfTransactionManager();
		checkInState(ITransaction.State.BUILDING_CHANGE);
		_changes.push(new ChangeSet(_changesInCurrentChange));
		LOG.debug("pushing new ChangeSet of " + _changesInCurrentChange.size() + " changes onto undo stack");
		_changesInCurrentChange.clear();
		this._state = ITransaction.State.IN_PROGRESS;
	}

    /*
     * @see de.berlios.rcpviewer.transaction.ITransaction#commit()
     */
	public void commit() {
		checkCurrentTransactionOfTransactionManager();
		checkInState(ITransaction.State.IN_PROGRESS);
		_committedChanges = new ChangeSet(_changes.toArray(new ChangeSet[]{}));
		LOG.debug("committing xactn of " + _changes.size() + " separate ChangeSets");
		_changes.clear();
		_state = ITransaction.State.COMMITTED;
		TransactionEvent event = new TransactionEvent(this);
		for(ITransactionListener listener: _listeners) {
			listener.committed(event);
		}
		_transactionManager.committed(this);
	}

	/*
	 * @see de.berlios.rcpviewer.transaction.ITransaction#reverse()
	 */
	public void reverse() {
		checkInState(ITransaction.State.COMMITTED);
		_committedChanges.undo();
		_state = ITransaction.State.REVERSED;
		TransactionEvent event = new TransactionEvent(this);
		for(ITransactionListener listener: _listeners) {
			listener.reversed(event);
		}
		_transactionManager.reversed(this);
	}

	/*
	 * @see de.berlios.rcpviewer.transaction.ITransaction#reapply()
	 */
	public void reapply() {
		checkInState(ITransaction.State.REVERSED);
		_committedChanges.execute();
		_state = ITransaction.State.COMMITTED;
		TransactionEvent event = new TransactionEvent(this);
		for(ITransactionListener listener: _listeners) {
			listener.reapplied(event);
		}
		_transactionManager.reapplied(this);
	}

	/*
	 * @see de.berlios.rcpviewer.transaction.ITransaction#getWorkChain()
	 */
	public ChangeSet getCommittedChanges() throws IllegalStateException {
		checkInState(ITransaction.State.COMMITTED);
		return _committedChanges;
	}
 
	/*
	 * @see de.berlios.rcpviewer.transaction.ITransaction#undoPendingChange()
	 */
	public void undoPendingChange() throws IllegalStateException {
		checkCurrentTransactionOfTransactionManager();
		checkInState(ITransaction.State.IN_PROGRESS);
		if(_changes.isEmpty()) {
			LOG.error("No pending changes to undo (_changes undo stack is empty)");
			throw new IllegalStateException("No pending changes to undo.");
		}
		ChangeSet change = _changes.pop();
		change.undo();
		_undoneChanges.push(change);
	}

	/*
	 * @see de.berlios.rcpviewer.transaction.ITransaction#redoPendingChange()
	 */
	public void redoPendingChange() throws IllegalStateException {
		checkCurrentTransactionOfTransactionManager();
		checkInState(ITransaction.State.IN_PROGRESS);
		if(_undoneChanges.isEmpty()) {
			LOG.error("No pending changes to redo (_undoChanges redo stack is empty)");
			throw new IllegalStateException("No undone changes to redo.");
		}
		ChangeSet change = _undoneChanges.pop();
		change.execute();
		_changes.push(change);
	}


	/*
	 * @see de.berlios.rcpviewer.transaction.ITransaction#undoPendingChanges()
	 */
	public void undoPendingChanges() throws IllegalStateException {
		while(!_changes.isEmpty()) {
			undoPendingChange();
		}
	}

	/*
	 * @see de.berlios.rcpviewer.transaction.ITransaction#redoPendingChanges()
	 */
	public void redoPendingChanges() throws IllegalStateException {
		while(!_undoneChanges.isEmpty()) {
			redoPendingChange();
		}
	}

	/*
	 * @see de.berlios.rcpviewer.transaction.ITransaction#getUndoableChanges()
	 */
	public List<ChangeSet> getUndoableChanges() {
		checkCurrentTransactionOfTransactionManager();
		checkInState(ITransaction.State.IN_PROGRESS);
		return Collections.unmodifiableList(_changes);
	}

	/*
	 * @see de.berlios.rcpviewer.transaction.ITransaction#getRedoableChanges()
	 */
	public List<ChangeSet> getRedoableChanges() {
		checkCurrentTransactionOfTransactionManager();
		checkInState(ITransaction.State.IN_PROGRESS);
		return Collections.unmodifiableList(_undoneChanges);
	}


	/*
	 * Derived from the modified pojos of each changeset.
	 *  
	 * @see de.berlios.rcpviewer.transaction.ITransaction#getEnlistedPojos()
	 */
	public Set<ITransactable> getEnlistedPojos() {
		return Collections.unmodifiableSet(getEnlistedPojosInternal());
	}

	private Set<ITransactable> getEnlistedPojosInternal() {
		Set<ITransactable> enlistedPojos = new LinkedHashSet<ITransactable>();
		for(ChangeSet changeSet: _changes) {
			enlistedPojos.addAll(changeSet.getModifiedPojos());
		}
		return enlistedPojos;
	}

	/*
	 * @see de.berlios.rcpviewer.transaction.ITransaction#checkInState(de.berlios.rcpviewer.transaction.ITransaction.State[])
	 */
	public void checkInState(ITransaction.State... requiredStates) throws IllegalStateException {
		if (!isInState(requiredStates)) {
			throw new IllegalStateException("Transaction must be in state of " + Arrays.asList(requiredStates) + ", instead is " + _state);
		}
	}

	/*
	 * @see de.berlios.rcpviewer.transaction.ITransaction#isInState(de.berlios.rcpviewer.transaction.ITransaction.State[])
	 */
	public boolean isInState(ITransaction.State... requiredStates)  {
		for(int i=0; i<requiredStates.length; i++) {
			if (_state.equals(requiredStates[i])) {
				return true;
			}
		}
		return false;
	}

	/*
	 * @see de.berlios.rcpviewer.transaction.ITransaction#id()
	 */
	public String id() {
		return TranMgmtConstants.TRANSACTION_ID_FORMAT.format(_appContainer.now());
	}

	/*
	 * @see de.berlios.rcpviewer.transaction.ITransaction#isIrreversible()
	 */
	public boolean isIrreversible() {
		for(IChange workAtom: _changes) {
			if (workAtom.isIrreversible()) {
				return false;
			}
		}
		return true;
	}

	private void checkCurrentTransactionOfTransactionManager() {
		if (!_transactionManager.isCurrent(this)) {
			throw new IllegalStateException("Not current transaction.");
		}
	}

    /*
     * @see de.berlios.rcpviewer.transaction.ITransaction#addTransactionListener(de.berlios.rcpviewer.transaction.ITransactionListener)
     */
	public void addTransactionListener(ITransactionListener listener) {
		_listeners.add(listener);
	}

	/*
	 * @see de.berlios.rcpviewer.transaction.ITransaction#removeTransactionListener(de.berlios.rcpviewer.transaction.ITransactionListener)
	 */
	public void removeTransactionListener(ITransactionListener listener) {
		_listeners.remove(listener);
	}

	/*
     * @see java.lang.Object#toString()
     */
    public String toString() {
    	Set<ITransactable> enlistedPojos = getEnlistedPojosInternal();
    	return "xactn@" + System.identityHashCode(this) + ": " 
	    	+ _state.name() + ", " + enlistedPojos.size() + " pojos "
	    	+ (enlistedPojos.size() > 0?enlistedPojos:"") 
	    	+ ", " + _changes.size() + " atoms: " + _changes;
    }


}
