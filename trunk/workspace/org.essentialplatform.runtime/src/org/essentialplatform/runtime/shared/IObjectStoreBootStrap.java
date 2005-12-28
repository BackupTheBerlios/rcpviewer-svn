/**
 * 
 */
package org.essentialplatform.runtime.shared;

import org.eclipse.core.runtime.CoreException;
import org.essentialplatform.runtime.shared.persistence.IObjectStore;


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
