package org.essentialplatform.runtime.server.remoting.xactnprocessor.enqueue;

import java.util.concurrent.SynchronousQueue;

import org.apache.log4j.Logger;
import org.essentialplatform.runtime.server.remoting.xactnprocessor.AbstractTransactionProcessor;
import org.essentialplatform.runtime.shared.transaction.ITransaction;

/**
 * A dummy implementation that just adds each transaction to a synchronous 
 * queue.
 * 
 * <p>
 * Allows tests to check that the transaction has been processed.
 * 
 * <tt>= new SynchronousQueue<ITransaction>();</tt>
 * 
 * @author Dan Haywood
 *
 */
public final class EnqueuingTransactionProcessor extends AbstractTransactionProcessor {

	protected Logger getLogger() {
		return Logger.getLogger(EnqueuingTransactionProcessor.class);
	}

	/**
	 * Only written to if in test mode.
	 * @see #setTestMode(boolean)
	 */
	private final SynchronousQueue<ITransaction> _processedTransactions;

	public EnqueuingTransactionProcessor(SynchronousQueue<ITransaction> processedTransactions) {
		_processedTransactions = processedTransactions;
	}
	
	public void process(ITransaction transaction) {
		getLogger().debug("processing transaction: " + transaction);
		try {
			_processedTransactions.put(transaction);
		} catch (InterruptedException ex) {
			getLogger().warn("Failed to enqueue transaction " + transaction);
		}
	}



}
