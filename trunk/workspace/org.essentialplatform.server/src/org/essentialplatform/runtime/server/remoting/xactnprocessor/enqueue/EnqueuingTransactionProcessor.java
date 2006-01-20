package org.essentialplatform.runtime.server.remoting.xactnprocessor.enqueue;

import java.util.concurrent.SynchronousQueue;

import org.apache.log4j.Logger;
import org.essentialplatform.runtime.server.remoting.xactnprocessor.AbstractTransactionProcessor;
import org.essentialplatform.runtime.shared.remoting.packaging.ITransactionPackage;
import org.essentialplatform.runtime.shared.transaction.ITransaction;

/**
 * A stub implementation that just adds each transaction package to a synchronous 
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
	private final SynchronousQueue<ITransactionPackage> _processedTransactions;

	public EnqueuingTransactionProcessor(SynchronousQueue<ITransactionPackage> processedTransactions) {
		_processedTransactions = processedTransactions;
	}
	
	public void process(ITransactionPackage transaction) {
		getLogger().debug("processing transaction: " + transaction);
		try {
			_processedTransactions.put(transaction);
		} catch (InterruptedException ex) {
			getLogger().warn("Failed to enqueue transaction " + transaction);
		}
	}

}
