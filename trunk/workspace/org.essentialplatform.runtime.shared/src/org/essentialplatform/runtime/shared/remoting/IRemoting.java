package org.essentialplatform.runtime.shared.remoting;

import org.essentialplatform.runtime.shared.remoting.marshalling.IMarshalling;
import org.essentialplatform.runtime.shared.remoting.transport.ITransport;

public interface IRemoting  {
	
	/**
	 * Returns the configured marshalling service.
	 * 
	 * @return
	 */
	IMarshalling getMarshalling();
	

	/**
	 * Returns the configured transport.
	 * 
	 * @return
	 */
	ITransport getTransport();


	/**
	 * Starts the remoting service.
	 *
	 */
	void start();

	
	/**
	 * Stops (shuts down) the remoting service. 
	 *
	 */
	void stop();
	
	/**
	 * 
	 * @param object
	 */
	void send(Object object);

}
