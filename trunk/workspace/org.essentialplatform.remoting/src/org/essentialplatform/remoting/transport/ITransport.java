package org.essentialplatform.remoting.transport;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import org.essentialplatform.remoting.marshalling.IMarshalling;

/**
 * Facade for different transport mechanisms.
 * 
 * 
 * @author Dan Haywood
 */
public interface ITransport {

	/**
	 * The marshalling mechanism to use.
	 * 
	 * <p>
	 * Provided using dependency injection.
	 * 
	 * @param marshalling
	 */
	void setMarshalling(IMarshalling marshalling);

	/**
	 * 
	 * @param os
	 */
	void send(OutputStream os);

}
