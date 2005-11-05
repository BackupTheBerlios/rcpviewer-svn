/**
 * 
 */
package org.essentialplatform.runtime;

import org.eclipse.core.runtime.CoreException;
import org.essentialplatform.runtime.persistence.IObjectStore;

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
