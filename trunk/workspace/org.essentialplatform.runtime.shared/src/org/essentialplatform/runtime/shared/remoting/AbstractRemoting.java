package org.essentialplatform.runtime.shared.remoting;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import org.apache.log4j.Logger;
import org.essentialplatform.runtime.shared.remoting.IRemoting;
import org.essentialplatform.runtime.shared.remoting.marshalling.IMarshalling;
import org.essentialplatform.runtime.shared.remoting.transport.ITransport;

public abstract class AbstractRemoting implements IRemoting {

	protected abstract Logger getLogger();

	private IMarshalling _marshalling;
	private ITransport _transport;
	
	
	public IMarshalling getMarshalling() {
		return _marshalling;
	}
	/**
	 * Dependency injection.
	 * 
	 * @param marshalling
	 */
	public void setMarshalling(IMarshalling marshalling) {
		_marshalling = marshalling;
	}

	
	public ITransport getTransport() {
		return _transport;
	}
	/**
	 * Dependency injection.
	 * 
	 * @param transport
	 */
	public void setTransport(ITransport transport) {
		_transport = transport;
	}

	public void send(Object object) {
//		OutputStream os = _transport.getOutputStream();
//		ByteArrayOutputStream baos;
//		baos = new ByteArrayOutputStream();
//		_marshalling.marshalTo(object, baos);
//		_transport.send(baos);
	}
	

}
