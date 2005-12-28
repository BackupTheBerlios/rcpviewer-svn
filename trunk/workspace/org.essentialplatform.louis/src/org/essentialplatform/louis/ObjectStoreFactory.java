package org.essentialplatform.louis;

import org.essentialplatform.runtime.persistence.IObjectStore;
import org.essentialplatform.runtime.persistence.NoopObjectStore;
import org.essentialplatform.runtime.persistence.PersistenceConstants;


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
		return new NoopObjectStore(PersistenceConstants.DEFAULT_OBJECT_STORE_ID);
	}
	
	/**
	 * Prevent instantiation
	 */
	private ObjectStoreFactory(){
		super();
	}
	
}