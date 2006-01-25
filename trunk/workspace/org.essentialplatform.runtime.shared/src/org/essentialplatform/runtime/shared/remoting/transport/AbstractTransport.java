package org.essentialplatform.runtime.shared.remoting.transport;

import java.io.Serializable;

import org.apache.log4j.Logger;

public abstract class AbstractTransport implements ITransport {

	protected abstract Logger getLogger(); 
	
	public abstract void send(String stringMessage);

	public abstract void send(Serializable serializableMessage);

}
