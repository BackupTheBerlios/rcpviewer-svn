package de.berlios.rcpviewer;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

public class CorePlugin extends Plugin {

	private static CorePlugin instance;
	
	public static final String ID = "de.berlios.rcpviewer.domain";
	
	private ResourceBundle resourceBundle;
	public static CorePlugin getInstance() {
		return instance;
	}

	public CorePlugin() {
		super();
		instance = this;
	}

	public void start(BundleContext context) throws Exception {
		super.start(context);
	}

	public void stop(BundleContext context) throws Exception {
		super.stop(context);
	}

	/**
	 * Returns the plugin's resource bundle,
	 */
	public ResourceBundle getResourceBundle() {
		try {
			if (resourceBundle == null)
				resourceBundle = ResourceBundle.getBundle("de.berlios.rcpviewer.core.CorePluginResources");
		} catch (MissingResourceException x) {
			resourceBundle = null;
		}
		return resourceBundle;
	}


}
