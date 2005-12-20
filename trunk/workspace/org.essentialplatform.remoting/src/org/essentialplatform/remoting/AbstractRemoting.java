package org.essentialplatform.remoting;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import org.apache.log4j.Logger;
import org.essentialplatform.remoting.IRemoting;
import org.essentialplatform.remoting.marshalling.IMarshalling;
import org.essentialplatform.remoting.transport.ITransport;

public abstract class AbstractRemoting implements IRemoting {

	protected abstract Logger getLogger();

	private IMarshalling _marshalling;
	private ITransport _transport;
	
	
	/*
	 * @see org.essentialplatform.remoting.IRemoting#getMarshalling()
	 */
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

	
	/*
	 * @see org.essentialplatform.remoting.IRemoting#getTransport()
	 */
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
