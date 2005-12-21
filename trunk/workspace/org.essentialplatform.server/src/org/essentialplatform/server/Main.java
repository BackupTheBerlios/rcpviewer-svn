package org.essentialplatform.server;

/**
 * Simple wrapper for server-side components.  
 * 
 * <p>
 * For development use only.
 * 
 * @author Dan Haywood
 */
public class Main {

	public static void main(String[] args) {
		StandaloneServer server = new StandaloneServer();
		server.start();
	}
	
}
