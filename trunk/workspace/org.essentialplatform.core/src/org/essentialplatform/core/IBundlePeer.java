package org.essentialplatform.core;

import org.osgi.framework.Bundle;

/**
 * Implemented by all Essential Platform plugins to abstract away from 
 * obtaining resources (eg Spring configuration files or ClassLoaders)
 * relative to a specific plugin.
 * 
 * <p>
 * The name of this class alludes to the fact that in Eclipse all plugins have
 * a 1:1 correspondence with OSGi bundles - they are peers of each other.
 * 
 * @author Dan Haywood
 */
public interface IBundlePeer {

	Bundle getBundle();
	
	/**
	 * Should match the plugin Id in its manifest.
	 * 
	 * <p>
	 * An easy way to implement this is:
	 * </p>
	 * <pre>
	 * public String getId() {
	 *     return this.getClass().getPackage().getName();
	 * }
	 * </pre>
	 * <p>
	 * and ensure that the plugin's class resides in a package that 
	 * matches the plugin Id.
	 * </p>
	 * 
	 * @return
	 */
	String getId();
}
