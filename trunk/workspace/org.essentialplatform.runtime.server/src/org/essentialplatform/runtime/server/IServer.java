package org.essentialplatform.runtime.server;

public interface IServer {

	public boolean isStarted();
	
	public void start()  throws IllegalStateException;
	public void shutdown() throws IllegalStateException;

}
