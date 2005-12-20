package org.essentialplatform.remoting.server;

import org.apache.log4j.Logger;
import org.essentialplatform.remoting.AbstractRemoting;
import org.essentialplatform.remoting.marshalling.XStreamMarshalling;

public final class ServerRemoting extends AbstractRemoting {

	protected Logger getLogger() {
		return Logger.getLogger(ServerRemoting.class);
	}


	public ServerRemoting() {
		setMarshalling(new XStreamMarshalling());
	}


}
