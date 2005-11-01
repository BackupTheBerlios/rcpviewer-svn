/**
 * 
 */
package org.essentialplatform.persistence;

import org.eclipse.core.runtime.CoreException;

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
