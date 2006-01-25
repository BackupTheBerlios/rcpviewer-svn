package org.essentialplatform.runtime.shared.remoting.transport;

import java.io.Serializable;

import org.essentialplatform.runtime.shared.remoting.marshalling.IMarshalling;
import org.essentialplatform.runtime.shared.remoting.packaging.IPackager;

/**
 * Facade for different transport mechanisms.
 * 
 * 
 * @author Dan Haywood
 */
public interface ITransport {

	void start();
	
	void shutdown();
	
	void send(Serializable object);

	/**
	 * If sending an object known to be a string.
	 * 
	 * <p>
	 * Implementations that have no specific optimization should simply 
	 * delegate to {@link #send(Serializable)}.
	 * 
	 * @param string
	 */
	void send(String string);

}
