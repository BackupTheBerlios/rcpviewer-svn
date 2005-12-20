package org.essentialplatform.louis;

import org.essentialplatform.runtime.persistence.IObjectStore;
import org.essentialplatform.runtime.persistence.NoopObjectStore;


/**
 * Creates <code>IObjectStore</code>.
 * @author Mike
 */
class ObjectStoreFactory {
	
	/**
	 * Creates the objects store for the application.
	 * @return
	 */
	static IObjectStore createObjectStore() {
		return new NoopObjectStore();
	}
	
	/**
	 * Prevent instantiation
	 */
	private ObjectStoreFactory(){
		super();
	}
	
}