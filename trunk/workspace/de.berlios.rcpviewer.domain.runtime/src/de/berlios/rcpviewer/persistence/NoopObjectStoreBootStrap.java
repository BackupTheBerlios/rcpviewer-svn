/**
 * 
 */
package de.berlios.rcpviewer.persistence;

import org.eclipse.core.runtime.CoreException;

/**
 * Does nowt.
 * @author Mike
 *
 */
public class NoopObjectStoreBootStrap implements IObjectStoreBootStrap {

	/* (non-Javadoc)
	 * @see de.berlios.rcpviewer.persistence.IObjectStoreBootStrap#populate(de.berlios.rcpviewer.persistence.IObjectStore)
	 */
	public void populate(IObjectStore store) throws CoreException {
		assert store != null;
	}

}
