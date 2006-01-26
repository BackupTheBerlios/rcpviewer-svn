package org.essentialplatform.runtime.client.transaction;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.essentialplatform.runtime.client.remoting.packaging.IPackager;
import org.essentialplatform.runtime.client.remoting.packaging.standard.StandardPackager;
import org.essentialplatform.runtime.client.transaction.changes.Interaction;
import org.essentialplatform.runtime.client.transaction.event.ITransactionManagerListener;
import org.essentialplatform.runtime.client.transaction.event.TransactionManagerEvent;
import org.essentialplatform.runtime.client.transaction.noop.NoopTransaction;
import org.essentialplatform.runtime.shared.domain.IPojo;
import org.essentialplatform.runtime.shared.remoting.IRemoting;
import org.essentialplatform.runtime.shared.remoting.noop.NoopRemoting;
import org.essentialplatform.runtime.shared.remoting.packaging.ITransactionPackage;

/**
 * Manages {@link ITransaction}s, stored local to thread.
 */
public final class TransactionManager implements ITransactionManager {
	
	final static Logger logger = Logger.getLogger(TransactionManager.class);

	private Logger getLogger() {
		return logger;
	}

	////////////////////////////////////////////////////////////////////
	// Singleton, Constructor
	////////////////////////////////////////////////////////////////////

	private final static ITransactionManager __instance = new TransactionManager();
	/**
	 * Pending the use of dependency injection, we expose a singleton.
	 * 
	 * <p>
	 * This is used by the TransactionAspect.
	 */
	public final static ITransactionManager instance() { return __instance; }

	/**
	 * Public constructor so that this component can be instantiated and
	 * injected using dependency injection.
	 */
	public TransactionManager() {
	}


	////////////////////////////////////////////////////////////////////
	// State
	////////////////////////////////////////////////////////////////////

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
	

	////////////////////////////////////////////////////////////////////
	// createTransaction()
	////////////////////////////////////////////////////////////////////

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
			return new NoopTransaction(this);
		}
		ITransaction transaction = new Transaction(this);
		_currentTransactions.add(transaction);
		
		TransactionManagerEvent event = new TransactionManagerEvent(this, transaction); 
		for(ITransactionManagerListener listener: _listeners) {
			listener.createdTransaction(event);
		}
		return transaction;
	}


	////////////////////////////////////////////////////////////////////
	// getCurrentTransactionFor()
	////////////////////////////////////////////////////////////////////


	public ITransaction getCurrentTransactionFor(final Object obj) {
		return getCurrentTransactionFor(obj, true);
	}

	public synchronized ITransaction getCurrentTransactionFor(final Object obj, final boolean autoEnlist) {
		final IPojo pojo = (IPojo)obj;
		ITransaction transaction = _currentTransactionByEnlistedPojo.get(pojo);
		if (transaction == null && autoEnlist) {
			transaction = createTransaction();
			_currentTransactionByEnlistedPojo.put(pojo, transaction);
		} 
		// fail early if (for any reason) we get out of whack. 
		if (transaction != null && !_currentTransactions.contains(transaction)) {
			throw new IllegalStateException("Transaction is in _currentTransactionByEnlistedPojo but not in _currentTransactions");
		}
		return transaction;
	}



	////////////////////////////////////////////////////////////////////
	// CurrentTransactions, isCurrent
	////////////////////////////////////////////////////////////////////

	/**
	 * Whether the specified {@link ITransaction} is in the list of transactions
	 * known by the transaction manager as still in progress.
	 * 
	 * @param transaction
	 * @return
	 */
	public boolean isCurrent(ITransaction transaction) {
		return _currentTransactions.contains(transaction);
	}
	
	
	/**
	 * {@link ITransaction}s that are in-progress.
	 * 
	 */
	private List<ITransaction> _currentTransactions = new ArrayList<ITransaction>();
	/*
	 * @see org.essentialplatform.transaction.ITransactionManager#getCurrentTransactions()
	 */
	public List<ITransaction> getCurrentTransactions() {
		return Collections.unmodifiableList(_currentTransactions);
	}


	////////////////////////////////////////////////////////////////////
	// Enlisting
	// enlist()
	////////////////////////////////////////////////////////////////////

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
	private Map<IPojo, ITransaction> _currentTransactionByEnlistedPojo = 
		new HashMap<IPojo, ITransaction>();

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
	public synchronized boolean enlist(ITransaction transaction, Set<IPojo> enlistedPojos) {
		
		// first pass; look for any issues before we do anything
		for(IPojo pojo: enlistedPojos) {
			ITransaction pojoTransaction = _currentTransactionByEnlistedPojo.get(pojo);
			if (pojoTransaction != null && pojoTransaction != transaction) {
				// already enlisted elsewhere.
				
				throw new PojoAlreadyEnlistedException(pojo, pojoTransaction);
			}
		}
		
		// second pass; do the work
		for(IPojo transactable: enlistedPojos) {
			ITransaction pojoTransaction = _currentTransactionByEnlistedPojo.get(transactable);
			if (pojoTransaction == null) {
				_currentTransactionByEnlistedPojo.put(transactable, transaction);
			}
		}
		return true;
	}


	
	////////////////////////////////////////////////////////////////////
	// Pending Changes:
	// undonePendingChange()
	////////////////////////////////////////////////////////////////////


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
	public void undonePendingChange(ITransaction transaction, Interaction change) {
		// need to unenlist pojos, though potentially not all of them (since some
		// pojos may have been changed as the result of a change still in a "done" state.
		Set<IPojo> pojosToUnenlist = new HashSet<IPojo>(change.getModifiedPojos());

		// get the pojos still unlisted, and eject from our list (an asymmetric set difference)
		Set<IPojo> stillEnlistedPojos = transaction.getEnlistedPojos();
		pojosToUnenlist.removeAll(stillEnlistedPojos);
		
		for(IPojo transactable: pojosToUnenlist) {
			_currentTransactionByEnlistedPojo.remove(transactable);
		}
		
		if (transaction.getUndoableChanges().size() == 0) {
			transaction.discard();
		}
	}

	
	////////////////////////////////////////////////////////////////////
	// discarded 
	////////////////////////////////////////////////////////////////////

	/**
	 * All pending changes have been undone from this transaction; it is
	 * discarded and any pojos unenlisted.
	 *  
	 * @param transaction
	 */
	public void discarded(ITransaction transaction) {
		
		unenlistPojosInTransactionAndRemoveTransaction(transaction);
		
		// notify listeners
		TransactionManagerEvent event = new TransactionManagerEvent(this, transaction); 
		for(ITransactionManagerListener listener: _listeners) {
			listener.discardedTransaction(event);
		}
	}
	


	////////////////////////////////////////////////////////////////////
	// Commit Transaction:
	// commit(), committed(), getCommittedTransactions() 
	////////////////////////////////////////////////////////////////////

	public synchronized void commit(Object pojo) throws IllegalStateException {
		
		// get the transaction for this transactable.
		ITransaction transaction = getCurrentTransactionFor(pojo);
		if (transaction == null) {
			throw new IllegalArgumentException("No transaction for pojo '" + pojo + "'");
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
	public synchronized void committed(ITransaction transaction) {
		
		sendTransactionToServer(transaction);
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
	 * {@link ITransaction}s that have been committed.
	 * 
	 * <p>
	 * Transactions are added to this map even if they cannot be reversed.
	 */
	private final Map<String, ITransaction> _committedTransactionById = new LinkedHashMap<String, ITransaction>();

	/*
	 * @see org.essentialplatform.transaction.ITransactionManager#getCommittedTransactions()
	 */
	public List<ITransaction> getCommittedTransactions() {
		return unmodifiableValues(_committedTransactionById);
	}

	/**
	 * Sends the transaction via the configured remoting mechanism such 
	 * that all enlisted pojos will be persisted (saved/updated) in the 
	 * appropriate object store. 
	 *   
	 * <p>
	 * Each enlisted pojo is persisted to the object store for the session
	 * to which it is bound.
	 * 
	 * @param transaction
	 */
	private void sendTransactionToServer(ITransaction transaction) {
		ITransactionPackage transactionPackage = _packager.pack(transaction);
		_remoting.send(transactionPackage);
	}

	/**
	 * Helper method that removes each transactable (enlisted pojo) from the
	 * map of enlisted pojo -> transaction and then removes the transaction
	 * itself.
	 *  
	 * @param transaction
	 */
	private void unenlistPojosInTransactionAndRemoveTransaction(ITransaction transaction) {
		Set<IPojo> enlistedPojos = transaction.getEnlistedPojos();
		for(IPojo enlistedPojo: enlistedPojos) {
			_currentTransactionByEnlistedPojo.remove(enlistedPojo);
		}
		_currentTransactions.remove(transaction);
	}
	

	
	////////////////////////////////////////////////////////////////////
	// Reverse Transaction:
	// reverse(), reversed(), ReversedTransactions 
	////////////////////////////////////////////////////////////////////

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
	 * @see org.essentialplatform.transaction.ITransactionManager#getReversedTransactions()
	 */
	public List<ITransaction> getReversedTransactions() {
		return unmodifiableValues(_reappliableTransactionById);
	}

	/**
	 * Used by {@link Transaction} to inform the manager that it has been
	 * reversed.
	 * 
	 * @param transaction
	 */
	public synchronized void reversed(ITransaction transaction) {
		_committedTransactionById.remove(transaction.id());
		_reappliableTransactionById.put(transaction.id(), transaction);
		TransactionManagerEvent event = new TransactionManagerEvent(this, transaction); 
		for(ITransactionManagerListener listener: _listeners) {
			listener.reversedTransaction(event);
		}
	}
	
	
	////////////////////////////////////////////////////////////////////
	// Reapply Transaction:
	// reapply(), reapplied() 
	////////////////////////////////////////////////////////////////////

	
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
	 * {@link ITransaction}s that have been committed but then
	 * subsequently reversed({@link ITransaction#reverse()}) and are ready 
	 * to be reapplied again if necessary.
	 */
	private final Map<String, ITransaction> _reappliableTransactionById = new LinkedHashMap<String, ITransaction>();

	/**
	 * Used by {@link Transaction} to inform the manager that it has been
	 * reversed.
	 * 
	 * @param transaction
	 */
	public synchronized void reapplied(ITransaction transaction) {
		_reappliableTransactionById.remove(transaction.id());
		_committedTransactionById.put(transaction.id(), transaction);
		TransactionManagerEvent event = new TransactionManagerEvent(this, transaction); 
		for(ITransactionManagerListener listener: _listeners) {
			listener.reappliedTransaction(event);
		}
	}



	////////////////////////////////////////////////////////////////////
	// suspend, isSuspended
	// resume
	////////////////////////////////////////////////////////////////////

	
	/*
	 * @see org.essentialplatform.runtime.transaction.ITransactionManager#suspend()
	 */
	public void suspend() {
		_state = State.SUSPENDED;
	}
	private boolean isSuspended() {
		return _state == State.SUSPENDED;
	}

	/*
	 * @see org.essentialplatform.runtime.transaction.ITransactionManager#resume()
	 */
	public void resume() {
		_state = State.STANDARD_OPERATION;
	}

	
	////////////////////////////////////////////////////////////////////
	// Listeners
	////////////////////////////////////////////////////////////////////

	private List<ITransactionManagerListener> _listeners = new ArrayList<ITransactionManagerListener>();

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

	////////////////////////////////////////////////////////////////////
	// Injected Dependencies:
	// Packager, Remoting 
	////////////////////////////////////////////////////////////////////

	
	private IRemoting _remoting = new NoopRemoting();
	public IRemoting getRemoting() { 
		return _remoting;
	}
	/**
	 * For dependency injection.
	 * 
	 * <p>
	 * If not injected, then defaults to {@link NoopRemoting}.
	 */
	public void setRemoting(org.essentialplatform.runtime.shared.remoting.IRemoting distribution) {
		_remoting = distribution;
	}

	

	private IPackager _packager = new StandardPackager();
	public IPackager getPackager() {
		return _packager;
	}
	/**
	 * For dependency injection.
	 * 
	 * <p>
	 * If not injected, then defaults to {@link StandardPackager}.
	 * 
	 * @param packager
	 */
	public void setPackager(IPackager packager) {
		_packager = packager;
	}


	
	////////////////////////////////////////////////////////////////////
	// Helper methods
	////////////////////////////////////////////////////////////////////


	private List<ITransaction> unmodifiableValues(Map<String, ITransaction> transactionsById) {
		List<ITransaction> transactions = 
			new ArrayList<ITransaction>(transactionsById.values());
		return Collections.unmodifiableList(transactions);

	}
	
	////////////////////////////////////////////////////////////////////
	// Testing Only
	// reset
	////////////////////////////////////////////////////////////////////


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


}
