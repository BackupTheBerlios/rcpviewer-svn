package de.berlios.rcpviewer.transaction.internal;


import java.util.Stack;

import de.berlios.rcpviewer.transaction.*;

/**
 * Manages transactions, stored local to thread
 */
public final class TransactionManager implements ITransactionManager {

	private static ThreadLocal transactionByThread;
	static {
		transactionByThread = new ThreadLocal() {
	        protected synchronized Object initialValue() {
	            return null;
	        }
		};
	}

	private final Stack<Transaction> undoStack = new Stack<Transaction>();
	private final Stack<Transaction> redoStack = new Stack<Transaction>();

	
	public TransactionManager() { }
	
	public ITransaction beginTransaction() {
		ITransaction transaction = getTransaction();
		transaction.startWorkGroup();
		return transaction;
	}

	public ITransaction getTransaction() {
		ITransaction transaction = (ITransaction)transactionByThread.get();
		if (transaction == null) {
			transaction = new Transaction(this);
			transactionByThread.set(transaction);
		}
		return transaction;
	}
	
	public void committed(Transaction transaction) {
		undoStack.push(transaction);
		clearCurrentTransaction();
	}
	
	public void rolledBack(ITransaction transaction) {
		clearCurrentTransaction();
	}
	
	private void clearCurrentTransaction() {
		transactionByThread.set(null);
	}

	public void undo() {
		if (!canUndo()) {
			throw new IllegalStateException("cannot undo");
		}
		Transaction transaction = undoStack.pop();
		transaction.undo();
		redoStack.push(transaction);
	}
	public boolean canUndo() {
		return !undoStack.empty();
	}

	public void redo() {
		if (!canRedo()) {
			throw new IllegalStateException("cannot redo");
		}
		Transaction transaction = redoStack.pop();
		transaction.redo();
		undoStack.push(transaction);
	}
	public boolean canRedo() {
		return !redoStack.empty();
	}
}
