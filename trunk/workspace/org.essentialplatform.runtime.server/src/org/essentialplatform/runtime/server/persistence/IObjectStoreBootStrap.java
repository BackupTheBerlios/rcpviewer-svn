/**
 * 
 */
package org.essentialplatform.runtime.server.persistence;

import org.eclipse.core.runtime.CoreException;


/**
 * Ugh - Mike pretending to be Dan.
 * <br>Following a common pattern for domain and object store 
 * initialisation.
 * @author Mike
 */
public interface IObjectStoreBootStrap {
	
	public static final String EXTENSION_POINT_ID
		= "org.essentialplatform.runtime.objectstorebootstrap"; //$NON-NLS-1$
	
	/**
	 * Populate as you wish.
	 * @param store
	 * @throws CoreException
	 */
	public void populate( IObjectStore store ) throws CoreException;

}
