package org.essentialplatform.runtime.client.remoting;

import org.apache.log4j.Logger;
import org.essentialplatform.runtime.shared.remoting.AbstractRemoting;
import org.essentialplatform.runtime.shared.remoting.marshalling.xstream.XStreamMarshalling;

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
