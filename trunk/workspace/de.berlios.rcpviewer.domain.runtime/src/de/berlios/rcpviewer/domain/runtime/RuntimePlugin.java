package de.berlios.rcpviewer.domain.runtime;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

import de.berlios.rcpviewer.domain.IDomainRegistry;
import de.berlios.rcpviewer.internal.DomainRegistry;
import de.berlios.rcpviewer.session.ISessionManager;
import de.berlios.rcpviewer.session.local.SessionManager;

/**
 * The main plugin class to be used in the desktop.
 */
public class RuntimePlugin extends Plugin {
	//The shared instance.
	private static RuntimePlugin plugin;
	//Resource bundle.
	private ResourceBundle resourceBundle;
	
	private IDomainRegistry _domainRegistry;
	private ISessionManager _sessionManager;
	
	/**
	 * The constructor.
	 */
	public RuntimePlugin() {
		super();
		plugin = this;
	}

	/**
	 * This method is called upon plug-in activation
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		plugin = null;
		resourceBundle = null;
	}

	/**
	 * Returns the shared instance.
	 */
	public static RuntimePlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns the string from the plugin's resource bundle,
	 * or 'key' if not found.
	 */
	public static String getResourceString(String key) {
		ResourceBundle bundle = RuntimePlugin.getDefault().getResourceBundle();
		try {
			return (bundle != null) ? bundle.getString(key) : key;
		} catch (MissingResourceException e) {
			return key;
		}
	}

	/**
	 * Returns the plugin's resource bundle,
	 */
	public ResourceBundle getResourceBundle() {
		try {
			if (resourceBundle == null)
				resourceBundle = ResourceBundle.getBundle("de.berlios.rcpviewer.domain.runtime.RuntimePluginResources");
		} catch (MissingResourceException x) {
			resourceBundle = null;
		}
		return resourceBundle;
	}

//	REVIEW_CHANGE Added this method.  ted 
	public IDomainRegistry getDomainRegistry() {
		try {
			if (_domainRegistry == null) {
				synchronized (this) {
					if (_domainRegistry == null)
						_domainRegistry= new DomainRegistry();
				}
			}
			return _domainRegistry;
		}
		catch (CoreException x) {
			getLog().log(x.getStatus());
			throw new RuntimeException(x);
		}
	}

//	REVIEW_CHANGE Added this method.  ted 
	public ISessionManager getSessionManager() throws CoreException {
		if (_sessionManager == null) {
			synchronized (this) {
				if (_sessionManager == null)
					_sessionManager= new SessionManager();
			}
		}
		return _sessionManager;
	}
}
