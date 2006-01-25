package org.essentialplatform.runtime.shared.remoting;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import org.apache.log4j.Logger;
import org.essentialplatform.runtime.shared.remoting.IRemoting;
import org.essentialplatform.runtime.shared.remoting.marshalling.IMarshalling;
import org.essentialplatform.runtime.shared.remoting.marshalling.xstream.XStreamMarshalling;
import org.essentialplatform.runtime.shared.remoting.packaging.IPackager;
import org.essentialplatform.runtime.shared.remoting.packaging.standard.StandardPackager;
import org.essentialplatform.runtime.shared.remoting.transport.ITransport;

public abstract class AbstractRemoting implements IRemoting {

	protected abstract Logger getLogger();

	
	
	private IMarshalling _marshalling = new XStreamMarshalling();
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


	
	private IPackager _packager = new StandardPackager();
	public IPackager getPackager() {
		return _packager;
	}
	public void setPackager(IPackager packager) {
		_packager = packager;
	}
	


	/*
	 * @see org.essentialplatform.runtime.shared.remoting.IRemoting#send(java.lang.Object)
	 */
	public void send(Object object) {
	}

	
	
	private ITransport _transport;
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
	

}
