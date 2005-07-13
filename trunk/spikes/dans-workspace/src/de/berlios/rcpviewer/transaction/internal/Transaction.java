package de.berlios.rcpviewer.transaction.internal;



import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import de.berlios.rcpviewer.transaction.*;


/**
 * Implementation of {@link Transaction} as used by {@link de.berlios.rcpviewer.transaction.TransactionManager}
 */
public final class Transaction implements ITransaction {

	private Stack workAtomStack = new Stack();
	private List workAtomList = new LinkedList();

	private WorkChain workChain;

	private State state;

	/**
	 * needs reference to actual implementation to get a hold
	 * of {@link TransactionManager#committed(Transaction)}.
	 */
	private final TransactionManager transactionManager;
	
	Transaction(final TransactionManager transactionManager) {
		this.transactionManager   = transactionManager;
		this.state = ITransaction.State.NOT_STARTED;
	}

	public void startWorkGroup() {
		checkInProgressOrNotStarted();
		
		workAtomStack.push(IWorkAtom.NULL);
		this.state = ITransaction.State.IN_PROGRESS;
	}

	public void completeWorkGroup() {
		checkInProgress();
		IWorkAtom workAtom = (IWorkAtom)workAtomStack.pop();
		addWorkAtom(workAtom);
	}

	public void addWorkAtom(final IWorkAtom workAtom) {
		checkInProgressOrNotStarted();
		if (!workAtom.equals(IWorkAtom.NULL)) {
			workAtomList.add(workAtom);
		}
		if (workAtomStack.isEmpty()) {
			committed();
		}
	}

	public WorkChain getWorkChain() {
		checkCommitted();
		return workChain;
	}

	public void commit() {
		checkInProgressOrNotStarted();
		unpopAllInteractions();
		committed();
	}

	public void rollback() {
		checkInProgressOrNotStarted();
		unpopAllInteractions();
		
		completed(ITransaction.State.ROLLED_BACK);
		workChain.undo();
		transactionManager.rolledBack(this);
	}
	
	void undo() throws IllegalStateException {
		if (!state.equals(ITransaction.State.COMMITTED)) {
			throw new IllegalStateException("Transaction must be COMMITTED, instead is " + state);
		}
		workChain.undo();
		this.state = ITransaction.State.UNDONE;
	}

	void redo() throws IllegalStateException {
		if (!state.equals(ITransaction.State.UNDONE)) {
			throw new IllegalStateException("Transaction must be UNDONE, instead is " + state);
		}
		workChain.execute();
		this.state = ITransaction.State.COMMITTED;
	}

	private void unpopAllInteractions() {
		while(!workAtomStack.isEmpty()) {
			IWorkAtom workAtom = (IWorkAtom)workAtomStack.pop();
			if (!workAtom.equals(IWorkAtom.NULL)) {
				workAtomList.add(0, workAtom);
			}
		}
		workChain = new WorkChain(workAtomList);
	}
	
	private void completed(final ITransaction.State state) {
		this.state = state;
		this.workChain = new WorkChain(workAtomList);
	}

	private void committed() {
		completed(ITransaction.State.COMMITTED);
		workChain.execute();
		transactionManager.committed(this);
	}
	
	private void checkInProgress() throws IllegalStateException {
		if (!this.state.equals(ITransaction.State.IN_PROGRESS)) {
			throw new IllegalStateException("Transaction must be IN_PROGRESS, instead is " + state);
		}
	}
	
	private void checkCommitted() throws IllegalStateException {
		if (!this.state.equals(ITransaction.State.COMMITTED)) {
			throw new IllegalStateException("Transaction must be COMMITTED, instead is " + state);
		}
	}
	
	private void checkInProgressOrNotStarted() throws IllegalStateException {
		if (!this.state.equals(ITransaction.State.IN_PROGRESS) &&
			!this.state.equals(ITransaction.State.NOT_STARTED)	) {
			throw new IllegalStateException("Transaction must be IN_PROGRESS or NOT_STARTED, instead is " + state);
		}
	}

    public State getState() {
		return state;
	}
	
}
