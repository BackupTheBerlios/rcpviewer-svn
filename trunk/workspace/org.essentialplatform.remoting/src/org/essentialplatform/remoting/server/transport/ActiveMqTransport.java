package org.essentialplatform.remoting.server.transport;

import java.io.OutputStream;

import org.essentialplatform.remoting.marshalling.IMarshalling;
import org.essentialplatform.remoting.transport.ITransport;

public final class ActiveMqTransport implements ITransport {

	private IMarshalling _marshalling;

	/*
	 * @see org.essentialplatform.remoting.transport.ITransport#setMarshalling(org.essentialplatform.remoting.marshalling.IMarshalling)
	 */
	public void setMarshalling(IMarshalling marshalling) {
		_marshalling = marshalling;
	}

	
	/*
	 * @see org.essentialplatform.remoting.transport.ITransport#send(java.io.OutputStream)
	 */
	public void send(OutputStream os) {
		// TODO Auto-generated method stub

	}


}
