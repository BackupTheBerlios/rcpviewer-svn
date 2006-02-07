package org.essentialplatform.runtime.server.remoting;

import org.essentialplatform.runtime.server.IServer;
import org.essentialplatform.runtime.server.remoting.xactnprocessor.ITransactionProcessor;
import org.essentialplatform.runtime.shared.remoting.marshalling.IMarshalling;

public interface IRemotingServer extends IServer {

	/**
	 * Override the default marshalling.
	 * 
	 * @param marshalling
	 */
	public void setMarshalling(IMarshalling marshalling);

	/**
	 * Override the default transaction processor.
	 * 
	 * @param transactionProcessor
	 */
	public void setTransactionProcessor(ITransactionProcessor transactionProcessor);
}
