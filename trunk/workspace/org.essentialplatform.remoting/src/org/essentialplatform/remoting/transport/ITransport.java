package org.essentialplatform.remoting.transport;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

/**
 * Facade for different transport mechanisms.
 * 
 * 
 * @author Dan Haywood
 */
public interface ITransport {

	/**
	 * 
	 * @param os
	 */
	void send(OutputStream os);

}
