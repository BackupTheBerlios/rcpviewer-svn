package de.berlios.rcpviewer.transaction.internal;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import de.berlios.rcpviewer.progmodel.extended.AppContainer;
import de.berlios.rcpviewer.progmodel.extended.IAppContainer;
import de.berlios.rcpviewer.transaction.*;
import de.berlios.rcpviewer.transaction.ITransaction.State;

/**
 * Manages {@link ITransaction}s, stored local to thread.
 */
public final class TransactionManager implements ITransactionManager {

	/**
	 * Pending the use of dependency injection, we expose a singleton.
	 * 
	 * <p>
	 * This is used by the TransactionAspect.
	 */
	public final static ITransactionManager INSTANCE = new TransactionManager();

	/**
	 * {@link ITransaction}s that are in-progress.
	 * 
	 */
	private List<ITransaction> _currentTransactions = new ArrayList<ITransaction>();

	/**
	 * {@link ITransaction}s that are in-progress, keyed by the pojos that are
	 * enlisted within them.
	 * 
	 * <p>
	 * This is a m:1 mapping - any given transaction could appear as the value
	 * for multiple pojos.  Moreover, the transactions are precisely the same
	 * as in {@link #_currentTransactions}.
	 * 
	 */
	private Map<ITransactable, ITransaction> _currentTransactionByEnlistedPojo = 
		new HashMap<ITransactable, ITransaction>();

	/**
	 * {@link ITransaction}s that have been committed.
	 * 
	 * <p>
	 * Transactions are added to this map even if they cannot be reversed.
	 */
	private final Map<String, Transaction> _committedTransactionById = new LinkedHashMap<String, Transaction>();
	/**
	 * {@link ITransaction}s that have been committed but then
	 * subsequently reversed({@link ITransaction#reverse()}) and are ready 
	 * to be reapplied again if necessary.
	 */
	private final Map<String, Transaction> _reappliableTransactionById = new LinkedHashMap<String, Transaction>();

	// TODO: pending dependency injection.
	private IAppContainer _appContainer = AppContainer.INSTANCE;

	private List<ITransactionManagerListener> _listeners = new ArrayList<ITransactionManagerListener>();

	/**
	 * Public constructor so that this component can be instantiated and
	 * injected using dependency injection.
	 */
	public TransactionManager() { }
	
	/*
	 * @see de.berlios.rcpviewer.transaction.ITransactionManager#getCurrentTransaction()
	 */
	public ITransaction getCurrentTransactionFor(final ITransactable transactable) {
		return getCurrentTransactionFor(transactable, true);
	}

	/*
	 * @see de.berlios.rcpviewer.transaction.ITransactionManager#getCurrentTransaction(boolean)
	 */
	public synchronized ITransaction getCurrentTransactionFor(final ITransactable transactable, final boolean autoEnlist) {
		ITransaction transaction = _currentTransactionByEnlistedPojo.get(transactable);
		if (transaction == null && autoEnlist) {
			transaction = createTransaction();
			_currentTransactions.add(transaction);
			_currentTransactionByEnlistedPojo.put(transactable, transaction);
		} 
		// fail early if (for any reason) we get out of whack. 
		if (transaction != null && !_currentTransactions.contains(transaction)) {
			throw new IllegalStateException("Transaction is in _currentTransactionByEnlistedPojo but not in _currentTransactions");
		}
		return transaction;
	}

	/*
	 * @see de.berlios.rcpviewer.transaction.ITransactionManager#commit(de.berlios.rcpviewer.transaction.ITransactable)
	 */
	public synchronized void commit(ITransactable transactable) throws IllegalStateException {
		
		// get the transaction for this transactable.
		ITransaction transaction = getCurrentTransactionFor(transactable);
		if (transaction == null) {
			throw new IllegalArgumentException("No transaction for pojo '" + transactable + "'");
		}
		
		// just check that the Id that this transaction wishes to be identified by
		// is unique with respect to any other transactions that we currently hold
		String transactionId = transaction.id();
		if (_committedTransactionById.containsKey(transactionId)) {
			throw new IllegalArgumentException("Already committed a transaction with id '" + transactionId + "'");
		}
		if (_reappliableTransactionById.containsKey(transactionId)) {
			throw new IllegalArgumentException("Already commited & then reversed a transaction with id '" + transactionId + "'");
		}
		
		// tell the transaction to go commit itself
		// will callback committed() which will remove from the hash and list
		transaction.commit();  
	}

	/*
	 * @see de.berlios.rcpviewer.transaction.ITransactionManager#reverse(java.lang.String)
	 */
	public synchronized void reverse(String transactionId) throws IllegalStateException {
		ITransaction transaction = _committedTransactionById.get(transactionId);
		if (transaction == null) {
			throw new IllegalArgumentException("No committed transaction for id '" + transactionId + "'");
		}
		transaction.reverse(); // will callback reversed()
	}

	/*
	 * @see de.berlios.rcpviewer.transaction.ITransactionManager#reapply(java.lang.String)
	 */
	public synchronized void reapply(String transactionId) throws IllegalStateException {
		ITransaction transaction = _reappliableTransactionById.get(transactionId);
		if (transaction == null) {
			throw new IllegalArgumentException("No reversed transaction for id '" + transactionId + "'");
		}
		transaction.reapply(); // will callback reapplied()
	}

	/**
	 * Used by {@link Transaction} to inform the manager that it has been
	 * committed.
	 * 
	 * <p>
	 * The transaction is removed as a current transaction and added to the
	 * list of committed transactoins (such that it can (potentially) be revervsed
	 * using {@link #reverse(String)}).
	 * 
	 * <p>
	 * Note that even irreversible transactions are added to the committed
	 * transactions hash.
	 * 
	 * @param transaction
	 */
	synchronized void committed(Transaction transaction) {
		
		// remove from the _currentTransactionByEnlistedPojo hash
		for(ITransactable enlistedTransactable: transaction.getEnlistedPojos()) {
			_currentTransactionByEnlistedPojo.remove(enlistedTransactable);	
		}
		
		// remove from the _currentTransactions list
		_currentTransactions.remove(transaction);

		// add to the _committedTransactions hash
		_committedTransactionById.put(transaction.id(), transaction);

		// notify listeners
		TransactionManagerEvent event = new TransactionManagerEvent(this, transaction); 
		for(ITransactionManagerListener listener: _listeners) {
			listener.committedTransaction(event);
		}
	}
	
	/**
	 * Used by {@link Transaction} to inform the manager that it has been
	 * reversed.
	 * 
	 * @param transaction
	 */
	synchronized void reversed(Transaction transaction) {
		_committedTransactionById.remove(transaction.id());
		_reappliableTransactionById.put(transaction.id(), transaction);
		TransactionManagerEvent event = new TransactionManagerEvent(this, transaction); 
		for(ITransactionManagerListener listener: _listeners) {
			listener.reversedTransaction(event);
		}
	}
	
	
	/**
	 * Used by {@link Transaction} to inform the manager that it has been
	 * reversed.
	 * 
	 * @param transaction
	 */
	synchronized void reapplied(Transaction transaction) {
		_reappliableTransactionById.remove(transaction.id());
		_committedTransactionById.put(transaction.id(), transaction);
		TransactionManagerEvent event = new TransactionManagerEvent(this, transaction); 
		for(ITransactionManagerListener listener: _listeners) {
			listener.reappliedTransaction(event);
		}
	}

	/*
	 * @see de.berlios.rcpviewer.transaction.ITransactionManager#getCurrentTransactions()
	 */
	public List<ITransaction> getCurrentTransactions() {
		return Collections.unmodifiableList(_currentTransactions);
	}

	/*
	 * @see de.berlios.rcpviewer.transaction.ITransactionManager#getCommittedTransactions()
	 */
	public List<ITransaction> getCommittedTransactions() {
		return unmodifiableValues(_committedTransactionById);
	}

	/*
	 * @see de.berlios.rcpviewer.transaction.ITransactionManager#getReversedTransactions()
	 */
	public List<ITransaction> getReversedTransactions() {
		return unmodifiableValues(_reappliableTransactionById);
	}
	
	private List<ITransaction> unmodifiableValues(Map<String, Transaction> transactionsById) {
		List<ITransaction> transactions = 
			new ArrayList<ITransaction>(transactionsById.values());
		return Collections.unmodifiableList(transactions);

	}
	

	/**
	 * Whether the specified {@link ITransaction} is in the list of transactions
	 * known by the transaction manager as still in progress.
	 * 
	 * @param transaction
	 * @return
	 */
	boolean isCurrent(Transaction transaction) {
		return _currentTransactions.contains(transaction);
	}

	/*
	 * @see de.berlios.rcpviewer.transaction.ITransactionManager#addTransactionManagerListener(de.berlios.rcpviewer.transaction.ITransactionManagerListener)
	 */
	public void addTransactionManagerListener(ITransactionManagerListener listener) {
		_listeners.add(listener);
	}

	/*
	 * @see de.berlios.rcpviewer.transaction.ITransactionManager#removeTransactionManagerListener(de.berlios.rcpviewer.transaction.ITransactionManagerListener)
	 */
	public void removeTransactionManagerListener(ITransactionManagerListener listener) {
		_listeners.remove(listener);
	}
	

	// TODO: pending dependency injection.
	public IAppContainer getAppContainer() {
		return _appContainer;
	}
	// TODO: pending dependency injection.
	public void setAppContainer(IAppContainer appContainer) {
		_appContainer = appContainer;
	}

	public ITransaction createTransaction() {
		ITransaction transaction = new Transaction(this, getAppContainer());
		_currentTransactions.add(transaction);
		return transaction;
	}

	/**
	 * Ensure that the pojo(s) are enlisted in the transaction.
	 * 
	 * <p>
	 * It is possible that some will already have been enlisted.  If any have
	 * already been enlisted in some other transaction, then an exception
	 * is thrown.
	 * 
	 * @param transaction
	 * @param enlistedPojos
	 */
	public void enlist(Transaction transaction, Set<ITransactable> enlistedPojos) {
		
		// first pass; look for any issues before we do anything
		for(ITransactable transactable: enlistedPojos) {
			ITransaction pojoTransaction = _currentTransactionByEnlistedPojo.get(transactable);
			if (pojoTransaction != null && pojoTransaction != transaction) {
				throw new IllegalStateException(
						"Pojo already enlisted in another transaction " 
						+ "(pojo='" + transactable + "'" 
						+ ", other xactn = '" + pojoTransaction + "'" 
						+ ", this xact='" + transaction + "'");
			}
		}
		
		// second pass; do the work
		for(ITransactable transactable: enlistedPojos) {
			ITransaction pojoTransaction = _currentTransactionByEnlistedPojo.get(transactable);
			if (pojoTransaction == null) {
				_currentTransactionByEnlistedPojo.put(transactable, transaction);
			}
		}
	}


}
