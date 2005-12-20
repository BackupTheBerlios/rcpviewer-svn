package org.essentialplatform.remoting.client;

import java.io.ByteArrayOutputStream;

import org.apache.log4j.Logger;
import org.essentialplatform.remoting.AbstractRemoting;
import org.essentialplatform.remoting.marshalling.XStreamMarshalling;

public final class ClientRemoting extends AbstractRemoting {

	protected Logger getLogger() {
		return Logger.getLogger(ClientRemoting.class);
	}


	public ClientRemoting() {
		setMarshalling(new XStreamMarshalling());
	}


}
