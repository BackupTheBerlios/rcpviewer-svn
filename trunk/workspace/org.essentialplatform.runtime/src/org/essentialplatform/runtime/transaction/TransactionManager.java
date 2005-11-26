package org.essentialplatform.runtime.transaction;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.essentialplatform.progmodel.essential.app.AppContainer;
import org.essentialplatform.progmodel.essential.app.IAppContainer;
import org.essentialplatform.runtime.domain.IDomainObject;
import org.essentialplatform.runtime.domain.IPojo;
import org.essentialplatform.runtime.persistence.IObjectStore;
import org.essentialplatform.runtime.transaction.changes.ChangeSet;
import org.essentialplatform.runtime.transaction.event.ITransactionManagerListener;
import org.essentialplatform.runtime.transaction.event.TransactionManagerEvent;

/**
 * Manages {@link ITransaction}s, stored local to thread.
 */
public final class TransactionManager implements ITransactionManager {

	private final static ITransactionManager __instance = new TransactionManager();
	
	private enum State {
		/**
		 * creation of transactions is suspended.
		 */
		SUSPENDED,
		/**
		 * standard operations
		 */
		STANDARD_OPERATION
	}
	
	private State _state = State.STANDARD_OPERATION;

	/**
	 * Pending the use of dependency injection, we expose a singleton.
	 * 
	 * <p>
	 * This is used by the TransactionAspect.
	 */
	public final static ITransactionManager instance() { return __instance; }
	
	
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
	public IAppContainer _appContainer = AppContainer.instance();

	private List<ITransactionManagerListener> _listeners = new ArrayList<ITransactionManagerListener>();

	/**
	 * Public constructor so that this component can be instantiated and
	 * injected using dependency injection.
	 */
	public TransactionManager() {
	}

	/**
	 * Creates a new in-progress {@link ITransaction}.
	 *  
	 * <p>
	 * Note that this method is not part of the {@link ITransactionManager}
	 * interface.  Transactions are only ever created implicitly as the result
	 * of the user doing some (inter)action through the GUI.  An aspect
	 * notices the change on the pojo, asks for a transaction and - in finding
	 * none - a transaction is created automatically.
	 * 
	 * <p>
	 * This design is akin to the ANSI <i>chained mode</i> for databases 
	 * (whereby a <code>begin transaction</code> is never needed and 
	 * <code>commit</code>s simply indicates the end of the transaction.  If
	 * this sounds familiar then that might be because it is reflected in the
	 * design of the JDBC API).
	 *  
	 * <p>
	 * For use only by aspects.  
	 *  
	 * @return
	 */
	public ITransaction createTransaction() {
		if (isSuspended()) {
			return null;
		}
		ITransaction transaction = new Transaction(this, getAppContainer());
		_currentTransactions.add(transaction);
		
		TransactionManagerEvent event = new TransactionManagerEvent(this, transaction); 
		for(ITransactionManagerListener listener: _listeners) {
			listener.createdTransaction(event);
		}
		return transaction;
	}


	/*
	 * @see org.essentialplatform.transaction.ITransactionManager#getCurrentTransaction()
	 */
	public ITransaction getCurrentTransactionFor(final ITransactable transactable) {
		return getCurrentTransactionFor(transactable, true);
	}

	/*
	 * @see org.essentialplatform.transaction.ITransactionManager#getCurrentTransaction(boolean)
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
	 * @see org.essentialplatform.transaction.ITransactionManager#commit(org.essentialplatform.transaction.ITransactable)
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
	 * @see org.essentialplatform.transaction.ITransactionManager#reverse(java.lang.String)
	 */
	public synchronized void reverse(String transactionId) throws IllegalStateException {
		ITransaction transaction = _committedTransactionById.get(transactionId);
		if (transaction == null) {
			throw new IllegalArgumentException("No committed transaction for id '" + transactionId + "'");
		}
		transaction.reverse(); // will callback reversed()
	}

	/*
	 * @see org.essentialplatform.transaction.ITransactionManager#reapply(java.lang.String)
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
		
		persistChangesForEnlistedPojos(transaction);
		unenlistPojosInTransactionAndRemoveTransaction(transaction);

		// add xactn to the _committedTransactions hash
		_committedTransactionById.put(transaction.id(), transaction);

		// notify listeners
		TransactionManagerEvent event = new TransactionManagerEvent(this, transaction); 
		for(ITransactionManagerListener listener: _listeners) {
			listener.committedTransaction(event);
		}
	}
	

	/**
	 * Persists changes to the appropriate object store for all pojos 
	 * enlisted in the supplied tranasction.
	 *   
	 * <p>
	 * Each enlisted pojo is persisted to the object store for the session
	 * to which it is bound.
	 * 
	 * <p>
	 * TODO: there is a downcast to IPojo for ITransactable; need to figure out.
	 * 
	 * @param transaction
	 */
	private void persistChangesForEnlistedPojos(Transaction transaction) {
		Set<ITransactable> enlistedPojos = transaction.getEnlistedPojos();
		for(ITransactable enlistedPojo: enlistedPojos) {
			IPojo pojo = (IPojo)enlistedPojo;
			IDomainObject domainObject = pojo.getDomainObject();
			IObjectStore objectStore = domainObject.getSession().getObjectStore();
		    objectStore.saveOrUpdate(domainObject);
		}
	}

	/**
	 * Helper method that removes each transactable (enlisted pojo) from the
	 * map of enlisted pojo -> transaction and then removes the transaction
	 * itself.
	 *  
	 * @param transaction
	 */
	private void unenlistPojosInTransactionAndRemoveTransaction(Transaction transaction) {
		Set<ITransactable> enlistedPojos = transaction.getEnlistedPojos();
		for(ITransactable enlistedPojo: enlistedPojos) {
			_currentTransactionByEnlistedPojo.remove(enlistedPojo);
		}
		_currentTransactions.remove(transaction);
	}
	

	/**
	 * Called by an in-progress {@link ITransaction} when a pending change is 
	 * undone.
	 * 
	 * <p>
	 * If a change has undone then potentially a pojo or pojos may need to 
	 * be unenlisted.
	 *  
	 * @param transaction
	 * @param change
	 */
	void undonePendingChange(Transaction transaction, ChangeSet change) {
		// need to unenlist pojos, though potentially not all of them (since some
		// pojos may have been changed as the result of a change still in a "done" state.
		Set<ITransactable> pojosToUnenlist = new HashSet<ITransactable>(change.getModifiedPojos());

		// get the pojos still unlisted, and eject from our list (an asymmetric set difference)
		Set<ITransactable> stillEnlistedPojos = transaction.getEnlistedPojos();
		pojosToUnenlist.removeAll(stillEnlistedPojos);
		
		for(ITransactable transactable: pojosToUnenlist) {
			_currentTransactionByEnlistedPojo.remove(transactable);
		}
		
		if (transaction.getUndoableChanges().size() == 0) {
			transaction.discard();
		}
	}

	/**
	 * All pending changes have been undone from this transaction; it is
	 * discarded and any pojos unenlisted.
	 *  
	 * @param transaction
	 */
	void discarded(Transaction transaction) {
		
		unenlistPojosInTransactionAndRemoveTransaction(transaction);
		
		// notify listeners
		TransactionManagerEvent event = new TransactionManagerEvent(this, transaction); 
		for(ITransactionManagerListener listener: _listeners) {
			listener.discardedTransaction(event);
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
	 * @see org.essentialplatform.transaction.ITransactionManager#getCurrentTransactions()
	 */
	public List<ITransaction> getCurrentTransactions() {
		return Collections.unmodifiableList(_currentTransactions);
	}

	/*
	 * @see org.essentialplatform.transaction.ITransactionManager#getCommittedTransactions()
	 */
	public List<ITransaction> getCommittedTransactions() {
		return unmodifiableValues(_committedTransactionById);
	}

	/*
	 * @see org.essentialplatform.transaction.ITransactionManager#getReversedTransactions()
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
	

	/**
	 * Ensure that the pojo(s) are enlisted in the transaction.
	 * 
	 * <p>
	 * It is possible that some will already have been enlisted.  If any have
	 * already been enlisted in some other transaction, then indicates through
	 * return value.
	 * 
	 * @param transaction
	 * @param enlistedPojos - that have been modified in a recent change and so must be enlisted.
	 * @return <code>true</code> if all ok, or <code>false</code> if any of the 
	 *         pojos are enlisted in some other transaction.  The calling 
	 *         transaction will undone this change.
	 */
	synchronized boolean enlist(Transaction transaction, Set<ITransactable> enlistedPojos) {
		
		// first pass; look for any issues before we do anything
		for(ITransactable transactable: enlistedPojos) {
			ITransaction pojoTransaction = _currentTransactionByEnlistedPojo.get(transactable);
			if (pojoTransaction != null && pojoTransaction != transaction) {
				// already enlisted elsewhere.
				
				// REVIEW_CHANGE MikeE 20051014 
				// throw new PojoAlreadyEnlistedException(transactable, pojoTransaction);
				return false;
			}
		}
		
		// second pass; do the work
		for(ITransactable transactable: enlistedPojos) {
			ITransaction pojoTransaction = _currentTransactionByEnlistedPojo.get(transactable);
			if (pojoTransaction == null) {
				_currentTransactionByEnlistedPojo.put(transactable, transaction);
			}
		}
		return true;
	}


	/*
	 * @see org.essentialplatform.transaction.ITransactionManager#reset()
	 */
	public void reset() {
		_currentTransactions.clear();
		_currentTransactionByEnlistedPojo.clear();
		_committedTransactionById.clear();
		_reappliableTransactionById.clear();
		_listeners.clear();
	}
	
	/*
	 * @see org.essentialplatform.transaction.ITransactionManager#addTransactionManagerListener(org.essentialplatform.transaction.ITransactionManagerListener)
	 */
	public void addTransactionManagerListener(ITransactionManagerListener listener) {
		_listeners.add(listener);
	}

	/*
	 * @see org.essentialplatform.transaction.ITransactionManager#removeTransactionManagerListener(org.essentialplatform.transaction.ITransactionManagerListener)
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

	/*
	 * @see org.essentialplatform.runtime.transaction.ITransactionManager#suspend()
	 */
	public void suspend() {
		_state = State.SUSPENDED;
	}

	/*
	 * @see org.essentialplatform.runtime.transaction.ITransactionManager#resume()
	 */
	public void resume() {
		_state = State.STANDARD_OPERATION;
	}

	private boolean isSuspended() {
		return _state == State.SUSPENDED;
	}


}
