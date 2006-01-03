/**
 * 
 */
package org.essentialplatform.runtime.shared;

import org.eclipse.core.runtime.CoreException;
import org.essentialplatform.runtime.server.persistence.IObjectStore;

/**
 * Does nowt.
 * @author Mike
 *
 */
public class NoopObjectStoreBootStrap implements IObjectStoreBootStrap {

	/* (non-Javadoc)
	 * @see org.essentialplatform.persistence.IObjectStoreBootStrap#populate(org.essentialplatform.persistence.IObjectStore)
	 */
	public void populate(IObjectStore store) throws CoreException {
		assert store != null;
	}

}
