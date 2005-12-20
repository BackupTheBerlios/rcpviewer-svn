package org.essentialplatform.remoting;

import org.essentialplatform.remoting.marshalling.IMarshalling;
import org.essentialplatform.remoting.transport.ITransport;

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
	 * 
	 * @param object
	 */
	void send(Object object);
}
