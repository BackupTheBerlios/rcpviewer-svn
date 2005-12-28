package org.essentialplatform.server;

public interface IService {

	public boolean isStarted();
	
	public void start()  throws IllegalStateException;
	public void shutdown() throws IllegalStateException;

}
