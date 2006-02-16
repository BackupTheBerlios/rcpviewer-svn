package org.essentialplatform.compiletime;

import org.eclipse.core.runtime.Plugin;
import org.essentialplatform.core.IBundlePeer;
import org.osgi.framework.BundleContext;
import java.util.*;

/**
 * The main plugin class to be used in the desktop.
 */
public class CompiletimePlugin extends Plugin implements IBundlePeer 
{
	//////////////////////////////////////////////////////////////////////
	// Constructor, instance
	//////////////////////////////////////////////////////////////////////

	public CompiletimePlugin() {
		super();
		plugin = this;
	}
	private static CompiletimePlugin plugin;
	/**
	 * Returns the shared instance.
	 */
	public static CompiletimePlugin getDefault() {
		return plugin;
	}

	
	//////////////////////////////////////////////////////////////////////
	// Id
	//////////////////////////////////////////////////////////////////////

	public String getId() {
		return this.getClass().getPackage().getName();
	}


	//////////////////////////////////////////////////////////////////////
	// Lifecycle
	//////////////////////////////////////////////////////////////////////
	
	public void start(BundleContext context) throws Exception {
		super.start(context);
	}

	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		plugin = null;
		resourceBundle = null;
	}

	
	//////////////////////////////////////////////////////////////////////
	// ResourceBundle
	//////////////////////////////////////////////////////////////////////
	
	private ResourceBundle resourceBundle;
	/**
	 * @return the plugin's {@link ResourceBundle}.
	 */
	public ResourceBundle getResourceBundle() {
		try {
			if (resourceBundle == null)
				resourceBundle = ResourceBundle.getBundle("org.essentialplatform.domain.compiletime.CompiletimePluginResources");
		} catch (MissingResourceException x) {
			resourceBundle = null;
		}
		return resourceBundle;
	}

	/**
	 * Returns the string from the plugin's {@link ResourceBundle},
	 * or the provided <tt>key</tt> if not found.
	 */
	public static String getResourceString(String key) {
		ResourceBundle bundle = CompiletimePlugin.getDefault().getResourceBundle();
		try {
			return (bundle != null) ? bundle.getString(key) : key;
		} catch (MissingResourceException e) {
			return key;
		}
	}



}
