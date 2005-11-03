package org.essentialplatform.louis;

import org.essentialplatform.runtime.persistence.IObjectStore;
import org.essentialplatform.runtime.persistence.inmemory.InMemoryObjectStore;

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
		return new InMemoryObjectStore();
	}
	
	/**
	 * Prevent instantiation
	 */
	private ObjectStoreFactory(){
		super();
	}
	
}