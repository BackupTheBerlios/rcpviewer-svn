package org.essentialplatform.runtime.shared.remoting;

import org.apache.log4j.Logger;
import org.essentialplatform.runtime.shared.remoting.marshalling.IMarshalling;
import org.essentialplatform.runtime.shared.remoting.marshalling.xstream.XStreamMarshalling;
import org.essentialplatform.runtime.shared.remoting.transport.ITransport;

public abstract class AbstractRemoting implements IRemoting {

	protected abstract Logger getLogger();

	
	
	private IMarshalling _marshalling = new XStreamMarshalling();
	public IMarshalling getMarshalling() {
		return _marshalling;
	}
	/**
	 * For dependency injection.
	 * 
	 * <p>
	 * Optional; if not specified then defaults to {@link XStreamMarshalling}.
	 * 
	 * @param marshalling
	 */
	public void setMarshalling(IMarshalling marshalling) {
		_marshalling = marshalling;
	}



	/*
	 * @see org.essentialplatform.runtime.shared.remoting.IRemoting#send(java.lang.Object)
	 */
	public abstract void send(Object object);

	
	
	private ITransport _transport;
	public ITransport getTransport() {
		return _transport;
	}
	/**
	 * For dependency injection.
	 * 
	 * <p>
	 * Mandatory.
	 * 
	 * @param transport
	 */
	public void setTransport(ITransport transport) {
		_transport = transport;
	}
	

}
