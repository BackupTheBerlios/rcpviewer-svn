package org.essentialplatform.runtime.transaction.internal;

import org.essentialplatform.runtime.domain.IDomainObject.IObjectCollectionReference;
import org.essentialplatform.runtime.transaction.ITransaction;

public final class ThreadLocals {

	/**
	 * cannot be instantiated
	 *
	 */
	private ThreadLocals() {}

	
	////////////////////////////////////////////////////////////////////////
	// TransactionByThread
	////////////////////////////////////////////////////////////////////////
	
	/**
	 * Keeps track of the current transaction for this thread (if any)
	 */
	static ThreadLocal<ITransaction> __transactionByThread;
	static {
		__transactionByThread = new ThreadLocal<ITransaction>() {
	        protected synchronized ITransaction initialValue() {
	            return null;
	        }
		};
	}
	/**
	 * Whether there is already a transaction for this thread.
	 */
	static boolean hasTransactionForThread() {
		return getTransactionForThreadIfAny() != null;
	}
	static void clearTransactionForThread() {
		__transactionByThread.set(null);
	}
	static void setTransactionForThread(final ITransaction transaction) {
		__transactionByThread.set(transaction);
	}
	static ITransaction getTransactionForThreadIfAny() {
		return __transactionByThread.get();
	}


	////////////////////////////////////////////////////////////////////////
	// CollectionReferenceByThread
	//
	// yes, a bit of a hack.  We have a pointcut on the addTo or removeFrom,
	// from which we can figure out the name of the collection, and then
	// lexically after we have a pointcut on the modification of the collection
	// itself.  This second pointcut looks at this threadlocal to guess the
	// name of the collection.  Quite a nasty hack, really.
	////////////////////////////////////////////////////////////////////////
	
	/**
	 * Keeps track of the current collection reference for this thread (if any)
	 */
	private static ThreadLocal<IObjectCollectionReference> __collectionReferenceByThread;
	
	static {
		__collectionReferenceByThread = new ThreadLocal<IObjectCollectionReference>() {
	        protected synchronized IObjectCollectionReference initialValue() {
	            return null;
	        }
		};
	}

	/**
	 * Whether there is already a CollectionReference for this thread.
	 */
	static boolean hasCollectionReferenceForThread() {
		return getTransactionForThreadIfAny() != null;
	}

	static void clearCollectionReferenceForThread() {
		__collectionReferenceByThread.set(null);
	}

	static void setCollectionReferenceForThread(final IObjectCollectionReference collectionReference) {
		__collectionReferenceByThread.set(collectionReference);
	}

	static IObjectCollectionReference getCollectionReferenceForThreadIfAny() {
		return __collectionReferenceByThread.get();
	}


	
}
