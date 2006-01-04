package org.essentialplatform.runtime.server.remoting;

import org.essentialplatform.remoting.marshalling.IMarshalling;
import org.essentialplatform.runtime.server.IService;
import org.essentialplatform.runtime.server.remoting.xactnprocessor.ITransactionProcessor;

public interface IRemotingServer extends IService {

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
