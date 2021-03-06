package org.essentialplatform.louis;

import org.essentialplatform.runtime.server.persistence.IObjectStore;
import org.essentialplatform.runtime.server.persistence.NoopObjectStore;
import org.essentialplatform.runtime.shared.persistence.PersistenceConstants;


/**
 * Creates <code>IObjectStore</code>.
 * @author Mike
 */
public class ObjectStoreFactory {
	
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