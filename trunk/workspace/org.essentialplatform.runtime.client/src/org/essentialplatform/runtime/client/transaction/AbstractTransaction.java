package org.essentialplatform.runtime.client.transaction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.essentialplatform.runtime.client.transaction.changes.ChangeSet;
import org.essentialplatform.runtime.client.transaction.changes.IChange;
import org.essentialplatform.runtime.client.transaction.changes.Interaction;
import org.essentialplatform.runtime.client.transaction.event.ITransactionListener;
import org.essentialplatform.runtime.client.transaction.event.TransactionEvent;
import org.essentialplatform.runtime.client.transaction.noop.NoopTransaction;
import org.essentialplatform.runtime.shared.domain.IPojo;
/**
 * Complete (but overridable) implementation of {@link ITransaction} as used 
 * by {@link org.essentialplatform.transaction.TransactionManager}.
 * 
 * <p>
 * Introduced to make it easier to provide alternative implementations
 * (specifically, of {@link NoopTransaction}.
 */
public abstract class AbstractTransaction implements ITransaction {

	protected abstract Logger getLogger(); 


	///////////////////////////////////////////////////////////////
    

	public AbstractTransaction(final TransactionManager transactionManager) {
		init(transactionManager, ITransaction.State.IN_PROGRESS, new Date());
	}

	/**
	 * Separated out from constructor to allow for re-initialization during
	 * serialization.
	 * 
	 * @param transactionManager
	 * @param state
	 * @param startedAt
	 */
	private void init(final TransactionManager transactionManager, final ITransaction.State state, final Date startedAt) {
		this._transactionManager   = transactionManager;
		this._state = state;
		this._startedAt = startedAt;
		this._id = UUID.randomUUID().toString();
	}

	///////////////////////////////////////////////////////////////
	// TransactionManager 
	///////////////////////////////////////////////////////////////
	
	/**
	 * needs reference to actual implementation to get a hold
	 * of {@link TransactionManager#committed(AbstractTransaction)}.
	 * 
	 * <p>
	 * Marked as <tt>transient</tt> for serialization.
	 */
	private transient TransactionManager _transactionManager;
	protected void checkCurrentTransactionOfTransactionManager() {
		if (!_transactionManager.isCurrent(this)) {
			throw new IllegalStateException("Not current transaction.");
		}
	}



	///////////////////////////////////////////////////////////////
	// Id, State Management
	// startAt, endedAt
	///////////////////////////////////////////////////////////////
	
	
	/**
	 * When this transaction was started (instantiated).
	 */
	private Date _startedAt;
	/**
	 * When this transaction was completed (may be null if still in progress).
	 */
	private Date _endedAt;
	
	private String _id;
	/*
	 * @see org.essentialplatform.transaction.ITransaction#id()
	 */
	public String id() {
		return _id;
	}


	/**
	 * The current state of this transaction.
	 */
	private State _state;
	/*
	 * @see org.essentialplatform.transaction.ITransaction#getState()
	 */
    public State getState() {
		return _state;
	}
	/*
	 * @see org.essentialplatform.transaction.ITransaction#checkInState(org.essentialplatform.transaction.ITransaction.State[])
	 */
	public ITransaction checkInState(ITransaction.State... requiredStates) throws IllegalStateException {
		if (!isInState(requiredStates)) {
			throw new IllegalStateException("Transaction must be in state of " + Arrays.asList(requiredStates) + ", instead is " + _state);
		}
		return this;
	}
	/*
	 * @see org.essentialplatform.transaction.ITransaction#isInState(org.essentialplatform.transaction.ITransaction.State[])
	 */
	public boolean isInState(ITransaction.State... requiredStates)  {
		for(int i=0; i<requiredStates.length; i++) {
			if (_state.equals(requiredStates[i])) {
				return true;
			}
		}
		return false;
	}

	
	
	///////////////////////////////////////////////////////////////
	// startingInteraction
	// addingToInteractionChangeSet
	// completingInteraction 
	///////////////////////////////////////////////////////////////

	/**
	 * All {@link IChange}s in the current change.
	 * 
	 * <p>
	 * If there is no change currently being built up (if state is not
	 * {@link ITransaction.State#BUILDING_CHANGE}, then will be empty.
	 * 
	 * <p>
	 * Implementation note: using ArrayList rather than LinkedList since 
	 * easier to inspect in variables view :-)
	 * 
	 * <p>
	 * Marked as <tt>transient</tt> for serialization.
	 */
	private transient List<IChange> _changesInCurrentInteraction = new ArrayList<IChange>();

	
	/*
	 * @see org.essentialplatform.transaction.ITransaction#startingInteraction()
	 */
	public boolean startingInteraction() {
		checkCurrentTransactionOfTransactionManager();
		checkInState(ITransaction.State.IN_PROGRESS, ITransaction.State.BUILDING_CHANGE);
		clearRedoStack();
		boolean alreadyStarted = (getState() == ITransaction.State.BUILDING_CHANGE);
		if (alreadyStarted) {
			return false;
		} else {
			this._state = ITransaction.State.BUILDING_CHANGE;
			TransactionEvent event = new TransactionEvent(this);
			for(ITransactionListener listener: _listeners) {
				listener.buildingChanges(event);
			}
			return true;
		}
	}

	/*
	 * Note that the listeners are <i>not</i> notified since this is merely
	 * one change in a potentially larger set of changes.
	 * 
	 * <p>
	 * @see org.essentialplatform.transaction.ITransaction#addingToInteraction(org.essentialplatform.transaction.IChange)
	 */
	public boolean addingToInteraction(final IChange change) {
		checkCurrentTransactionOfTransactionManager();

		// will be BUILDING_CHANGE if originally executing, but
		// will be IN_PROGRESS if redoing.  
		checkInState(ITransaction.State.BUILDING_CHANGE, Transaction.State.IN_PROGRESS);
		
		if (change.equals(IChange.NULL)) {
			getLogger().debug("addingToInteractionChangeSet: ignoring since NULL change");
			return true;
		}
		getLogger().debug("addingToInteractionChangeSet; change=" + change);
		boolean enlistedSuccessfully = _transactionManager.enlist(this, change.getModifiedPojos());
		if (!enlistedSuccessfully) {
			getLogger().info("addingToInteractionChangeSet: pojo(s) already enlisted in another xactn");
			return false;
		}
		
		_changesInCurrentInteraction.add(change);
		
		// listeners are NOT notified here; instead they are notified when
		// the interaction completes.
		
		return true;
	}

	/*
	 * Creates a change set out of the individual changes in the current change, pops onto
	 * the stack of changes, and then clears down the work atoms list in
	 * readiness for the next change.
	 * 
	 * @see org.essentialplatform.transaction.ITransaction#completingInteraction()
	 */
	public ITransaction completingInteraction() {
		checkCurrentTransactionOfTransactionManager();
		checkInState(ITransaction.State.BUILDING_CHANGE);
		
		// copy current set of changes, add to the list of all changes in 
		// this transaction.
		Interaction interaction = new Interaction(this, _changesInCurrentInteraction);
		_changes.push(interaction);
		getLogger().debug("pushing new ChangeSet of " + _changesInCurrentInteraction.size() + " changes onto undo stack");
		
		_changesInCurrentInteraction.clear();
		this._state = ITransaction.State.IN_PROGRESS;
		
		// notify listeners.
		TransactionEvent event = new TransactionEvent(this, interaction);
		for(ITransactionListener listener: _listeners) {
			listener.addedChange(event);
		}
		
		return this;
	}

	
	///////////////////////////////////////////////////////////////
	// hasUndoableChanges, getUndoableChanges, 
	// hasRedoableChanges, getRedoableChanges, 
	// undoPendingChange, redoPendingChange
	// undoPendingChanges, redoPendingChanges
	///////////////////////////////////////////////////////////////

	/**
	 * List of all changes ({@link Interaction}s) that have been 
	 * performed in the context of this transaction.
	 * 
	 * <p>
	 * This stack is available to be undone.
	 * 
	 * <p>
	 * Marked as <tt>transient</tt> for serialization.
	 */
	private transient Stack<Interaction> _changes = new Stack<Interaction>();
	/**
	 * Stack of all changes ({@link IWorkChain}s) that have been undone.
	 * 
	 * <p>
	 * This stack is available to be redone.
	 * 
	 * <p>
	 * Marked as <tt>transient</tt> for serialization.
	 */
	private transient Stack<Interaction> _undoneChanges = new Stack<Interaction>();
	

	/*
	 * @see org.essentialplatform.transaction.ITransaction#undoPendingChange()
	 */
	public ITransaction undoPendingChange() throws IllegalStateException {

		checkCurrentTransactionOfTransactionManager();
		checkInState(ITransaction.State.IN_PROGRESS);
		if(_changes.isEmpty()) {
			getLogger().error("No pending changes to undo (_changes undo stack is empty)");
			throw new IllegalStateException("No pending changes to undo.");
		}
		Interaction change = _changes.pop();
		change.undo();
		_undoneChanges.push(change);

		// allow the TM to recompute the pojos that should be enlisted.
		_transactionManager.undonePendingChange(this, change);
		
		// notify listeners
		TransactionEvent event = new TransactionEvent(this, change);
		for(ITransactionListener listener: _listeners) {
			listener.undonePendingChange(event);
		}

		return this;
	}
	
	/*
	 * @see org.essentialplatform.transaction.ITransaction#redoPendingChange()
	 */
	public ITransaction redoPendingChange() throws IllegalStateException {
		IChange redoneChange = redoPendingChangeNoNotifyListeners();

		// no need to ask TM to check which pojos are enlisted; they will have 
		// automatically enlisted themselves as necessary

		// notify listeners
		TransactionEvent event = new TransactionEvent(this, redoneChange);
		for(ITransactionListener listener: _listeners) {
			listener.redonePendingChange(event);
		}
		
		return this;
	}
	

	/*
	 * @see org.essentialplatform.transaction.ITransaction#redoPendingChanges()
	 */
	public ITransaction redoPendingChanges() throws IllegalStateException {
		while(!_undoneChanges.isEmpty()) {
			redoPendingChange();
		}
		TransactionEvent event = new TransactionEvent(this);
		for(ITransactionListener listener: _listeners) {
			listener.redonePendingChanges(event);
		}
		// no need to ask TM to check which pojos are enlisted; they will have 
		// automatically enlisted themselves as necessary
		
		return this;
	}

	/**
	 * Delegated to by both {@link #redoPendingChange()} and 
	 * {@link #redoPendingChanges()}, both of which do different things for
	 * notifing listeners.
	 * 
	 * @throws IllegalStateException
	 */
	private Interaction redoPendingChangeNoNotifyListeners() throws IllegalStateException {
		checkCurrentTransactionOfTransactionManager();
		checkInState(ITransaction.State.IN_PROGRESS);
		if(_undoneChanges.isEmpty()) {
			getLogger().error("No pending changes to redo (_undoChanges redo stack is empty)");
			throw new IllegalStateException("No undone changes to redo.");
		}
		Interaction change = _undoneChanges.pop();
		change.execute();
		_changes.push(change);
		return change;
	}

	/*
	 * Previously checked:
	 * <ul>
	 * <li> was in state of IN_PROGRESS, 
	 * <li> was current transaction.
	 * </ul>
	 * No longer does this since may be called through TransactionManager view.
	 * 
	 * @see org.essentialplatform.transaction.ITransaction#getUndoableChanges()
	 */
	public List<Interaction> getUndoableChanges() {
		return Collections.unmodifiableList(_changes);
	}


	/*
	 * @see org.essentialplatform.runtime.transaction.ITransaction#hasUndoableChanges()
	 */
	public boolean hasUndoableChanges() {
		return _changes.size() > 0;
	}

	/*
	 * Previously checked:
	 * <ul>
	 * <li> was in state of IN_PROGRESS, 
	 * <li> was current transaction.
	 * </ul>
	 * No longer does this since may be called through TransactionManager view.
	 * 
	 * @see org.essentialplatform.transaction.ITransaction#getRedoableChanges()
	 */
	public List<Interaction> getRedoableChanges() {
		return Collections.unmodifiableList(_undoneChanges);
	}

	/*
	 * @see org.essentialplatform.runtime.transaction.ITransaction#hasRedoableChanges()
	 */
	public boolean hasRedoableChanges() {
		return _undoneChanges.size() > 0;
	}

	protected void clearRedoStack() {
		this._undoneChanges.clear();
	}


	///////////////////////////////////////////////////////////////
	// commit, discard (xactns still in progress)
	///////////////////////////////////////////////////////////////

	/**
	 * The {@link #_changes} is converted into an immutable work chain 
	 * when the transaction is {@link #commit()}ted.
	 * 
	 * <p>
	 * Will be set to <code>null</code> until the transaction has been
	 * committed.
	 */
	private List<Interaction> _committedChanges;



    /*
     * @see org.essentialplatform.transaction.ITransaction#commit()
     */
	public ITransaction commit() {
		checkCurrentTransactionOfTransactionManager();
		checkInState(ITransaction.State.IN_PROGRESS, ITransaction.State.BUILDING_CHANGE);
		_committedChanges = Collections.unmodifiableList(new ArrayList(_changes));
		getLogger().debug("committing xactn of " + _changes.size() + " separate ChangeSets");

		_state = ITransaction.State.COMMITTED;

		// TODO: remove this stuff, once happy that the IPackager approach 
		// will give us what we want.
//		// populate for serialization.
//		_instantiatedPojos = Collections.unmodifiableSet(getInstantiatedPojos());
//		
//		Set<IDomainObject> enlistedPojoDOs = new LinkedHashSet<IDomainObject>(); 
//		for(ITransactable enlistedPojo: getEnlistedPojos()) {
//			enlistedPojoDOs.add(((IPojo)enlistedPojo).domainObject());
//		}
//		_enlistedPojoDOs = Collections.unmodifiableSet(enlistedPojoDOs);

		// we notify the transaction manager before we clear _changes because
		// the transaction manager is going to ask this transaction which pojos
		// it had enlisted in order that it can mark them as clean.
		_transactionManager.committed(this);
		
		// okay, now we can tidy up.
		_changes.clear();
		
		// notify listeners
		TransactionEvent event = new TransactionEvent(this);
		for(ITransactionListener listener: _listeners) {
			listener.committed(event);
		}
		
		return this;
	}

	/*
	 * @see org.essentialplatform.transaction.ITransaction#discard()
	 */
	public ITransaction discard() throws IllegalStateException {
		TransactionEvent event = new TransactionEvent(this);
		for(ITransactionListener listener: _listeners) {
			listener.discarded(event);
		}
		_state = ITransaction.State.DISCARDED;
		_transactionManager.discarded(this);
		
		return this;
	}

	
	///////////////////////////////////////////////////////////////
	// committedChanges, flattenedCommittedChanges
	///////////////////////////////////////////////////////////////

	/*
	 * @see org.essentialplatform.transaction.ITransaction#getCommittedChanges()
	 */
	public List<Interaction> getCommittedChanges() throws IllegalStateException {
		checkInState(ITransaction.State.COMMITTED);
		return _committedChanges;
	}
	public void traverseCommittedChanges(IChange.IVisitor visitor) {
		this.checkInState(ITransaction.State.COMMITTED);
		for(IChange change: _committedChanges) {
			change.accept(visitor);
		}
	}


	/*
	 * @see org.essentialplatform.runtime.transaction.ITransaction#flattenedCommittedChanges()
	 */
	public List<IChange> flattenedCommittedChanges() {
		final List<IChange> flattenedChanges = new ArrayList<IChange>();
		IChange.IVisitor flatteningVisitor = new IChange.IVisitor() {
			/**
			 * Ignore ChangeSets, but add otherwise.
			 */
			public void visit(IChange change) {
				if (change instanceof ChangeSet) return;
				flattenedChanges.add(change);
			}
		};
		traverseCommittedChanges(flatteningVisitor);
		return flattenedChanges;
	}


	///////////////////////////////////////////////////////////////
	// Enlisted Pojos, EnlistedPojoDOs, Instantiated Pojos
	///////////////////////////////////////////////////////////////



	/*
	 * Derived from the modified pojos of each changeset.
	 *  
	 * @see org.essentialplatform.transaction.ITransaction#getEnlistedPojos()
	 */
	public Set<IPojo> getEnlistedPojos() {
		Set<IPojo> enlistedPojos = new LinkedHashSet<IPojo>();
		for(Interaction interaction: isInState(ITransaction.State.COMMITTED)?_committedChanges:_changes) {
			enlistedPojos.addAll(interaction.getModifiedPojos());
		}
		return enlistedPojos;
	}


	// TODO: remove this stuff, once happy that the IPackager approach 
	// will give us what we want.

//	private Set<IDomainObject> _enlistedPojoDOs; 
//
//	/*
//	 * Populated only when the transaction is committed.
//	 * 
//	 * @see org.essentialplatform.runtime.transaction.ITransaction#getEnlistedPojoDOs()
//	 */
//	public Set<IDomainObject> getEnlistedPojoDOs() {
//		return _enlistedPojoDOs;
//	}



	// TODO: remove this stuff, once happy that the IPackager approach 
	// will give us what we want.
//	/**
//	 * Only populated when the transaction is committed. 
//	 * 
//	 * <p>
//	 * Required for serialization; populated on commit.
//	 * 
//	 * <p>
//	 * Implementation note: this appears lexically before <tt>_committedChanges</tt>
//	 * so that XStream serialization lists all the pojos and then the
//	 * changes just reference them.  There is no functional difference, but it
//	 * is easier to review the serialized form with pojos dumped out first.
//	 */
//	private Set<ITransactable> _instantiatedPojos;
//
//	/*
//	 * If the transaction has not been serialized, then this method determines 
//	 * the set of pojos on the fly (from the <tt>_changes</tt> collection.
//	 * But if the transaction HAS been serialized, then <tt>_changes</tt> will
//	 * be empty, and so the method just returns the 
//	 * <tt>_instantiatedPojos<tt> collection, populated when the 
//	 * transaction is committed.
//	 * 
//	 * @see org.essentialplatform.runtime.transaction.ITransaction#getInstantiatedPojos()
//	 */
//	public Set<ITransactable> getInstantiatedPojos() {
//		if (_changes == null) {
//			return _instantiatedPojos;
//		}
//		final Set<ITransactable> instantiatedPojos = new LinkedHashSet<ITransactable>();
//		IChange.IVisitor changeVisitor = new IChange.IVisitor(){
//			public void visit(IChange change) {
//				if (!(change instanceof InstantiationChange)) {
//					return;
//				}
//				instantiatedPojos.addAll(change.getModifiedPojos());
//			}};
//		for(Interaction interaction: _committedChanges) {
//			interaction.accept(changeVisitor);
//		}
//		return instantiatedPojos; 
//	}


	///////////////////////////////////////////////////////////////
	// reverse, reapply (xactns that have been committed)
	///////////////////////////////////////////////////////////////

	/*
	 * @see org.essentialplatform.transaction.ITransaction#reverse()
	 */
	public ITransaction reverse() {
		checkInState(ITransaction.State.COMMITTED);
		for(IChange eachChange: _committedChanges) {
			eachChange.undo(); // will throw exception if irreversible.
		}
		_state = ITransaction.State.REVERSED;
		TransactionEvent event = new TransactionEvent(this);
		for(ITransactionListener listener: _listeners) {
			listener.reversed(event);
		}
		_transactionManager.reversed(this);
		
		return this;
	}

	/*
	 * @see org.essentialplatform.transaction.ITransaction#reapply()
	 */
	public ITransaction reapply() {
		checkInState(ITransaction.State.REVERSED);
		for(IChange eachChange: _committedChanges) {
			eachChange.execute();
		}
		_transactionManager.reapplied(this);
		_state = ITransaction.State.COMMITTED;
		
		// notify listeners
		TransactionEvent event = new TransactionEvent(this);
		for(ITransactionListener listener: _listeners) {
			listener.reapplied(event);
		}
		
		return this;
	}

	/*
	 * @see org.essentialplatform.transaction.ITransaction#isIrreversible()
	 */
	public boolean isIrreversible() {
		for(IChange change: _changes) {
			if (change.isIrreversible()) {
				return false;
			}
		}
		return true;
	}


	///////////////////////////////////////////////////////////////
	// Listeners
	///////////////////////////////////////////////////////////////

	/**
	 * Listeners of this transaction.
	 * 
	 * <p>
	 * Marked as <tt>transient</tt> for serialization.
	 */
    private transient List<ITransactionListener> _listeners = new ArrayList<ITransactionListener>();

    /*
     * @see org.essentialplatform.transaction.ITransaction#addTransactionListener(org.essentialplatform.transaction.ITransactionListener)
     */
	public <T extends ITransactionListener> T addTransactionListener(T listener) {
		_listeners.add(listener);
		return listener;
	}

	/*
	 * @see org.essentialplatform.transaction.ITransaction#removeTransactionListener(org.essentialplatform.transaction.ITransactionListener)
	 */
	public void removeTransactionListener(ITransactionListener listener) {
		_listeners.remove(listener);
	}


	///////////////////////////////////////////////////////////////
	// toString
	///////////////////////////////////////////////////////////////

	/*
     * @see java.lang.Object#toString()
     */
	@Override
    public String toString() {
    	StringBuffer buf = new StringBuffer();
    	buf.append(TranMgmtConstants.TRANSACTION_START_END_FORMAT.format(_startedAt));
    	if (_endedAt != null) {
    		buf.append("->")
    		   .append(TranMgmtConstants.TRANSACTION_START_END_FORMAT.format(_endedAt));
    	} 
    	buf.append(" ").append(_state.name().toLowerCase()).append(" ");
    	
    	if (_state != State.COMMITTED) {
        	Set<IPojo> enlistedPojos = getEnlistedPojos();
        	if (enlistedPojos.size() > 0) {
    	    	buf.append(enlistedPojos.size()).append(" objects, ")
    	    	   .append(_changes.size()).append(" changes");
        	}
    	} else {
    		buf.append(", ")
    		   .append(getCommittedChanges().size()).append(" committed changes, ")
    		   .append(getEnlistedPojos().size()).append(" enlisted pojos");
    	}
    	
    	return buf.toString();
    }


}
