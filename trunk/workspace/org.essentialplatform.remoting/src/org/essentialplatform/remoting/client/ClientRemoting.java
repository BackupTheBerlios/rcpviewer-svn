package org.essentialplatform.remoting.client;

import java.io.ByteArrayOutputStream;

import org.apache.log4j.Logger;
import org.essentialplatform.remoting.AbstractRemoting;
import org.essentialplatform.remoting.marshalling.xstream.XStreamMarshalling;

public final class ClientRemoting extends AbstractRemoting {

	protected Logger getLogger() {
		return Logger.getLogger(ClientRemoting.class);
	}


	public ClientRemoting() {
		setMarshalling(new XStreamMarshalling());
	}


	public void start() {
		// TODO Auto-generated method stub
		
	}


	public void stop() {
		// TODO Auto-generated method stub
		
	}


}
