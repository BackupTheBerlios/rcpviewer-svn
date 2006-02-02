package org.essentialplatform.runtime.shared;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

import org.essentialplatform.runtime.shared.domain.adapters.IDomainRegistry;

/**
 * The main plugin class to be used in the desktop.
 */
public class RuntimePlugin extends Plugin {
	
	/**
	 * ResourceBundle identifier.
	 */
	public static final String RUNTIME_PLUGIN_RESOURCES = 
		"org.essentialplatform.runtime.RuntimePluginResources";

	
	////////////////////////////////////////////////////////////////////
	// Singleton, constructor
	////////////////////////////////////////////////////////////////////

	private static RuntimePlugin plugin;
	
	
	/**
	 * The constructor.
	 */
	public RuntimePlugin() {
		super();
		plugin = this;
	}

	/**
	 * Returns the shared instance.
	 */
	public static RuntimePlugin getDefault() {
		return plugin;
	}

	
	////////////////////////////////////////////////////////////////////
	// Lifecycle methods
	////////////////////////////////////////////////////////////////////

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



	////////////////////////////////////////////////////////////////////
	// ResourceBundle
	////////////////////////////////////////////////////////////////////

	private ResourceBundle resourceBundle;
	
	/**
	 * Returns the plugin's resource bundle,
	 */
	public ResourceBundle getResourceBundle() {
		try {
			if (resourceBundle == null)
				resourceBundle = ResourceBundle.getBundle(RUNTIME_PLUGIN_RESOURCES);
		} catch (MissingResourceException x) {
			resourceBundle = null;
		}
		return resourceBundle;
	}

	/**
	 * Convenience method that returns the string from the {@link #getDefault()}'s 
	 * plugin's resource bundle, or the <tt>key</tt> originally provided if not 
	 * found.
	 */
	public static String getResourceString(String key) {
		ResourceBundle bundle = getDefault().getResourceBundle();
		try {
			return (bundle != null) ? bundle.getString(key) : key;
		} catch (MissingResourceException e) {
			return key;
		}
	}


	////////////////////////////////////////////////////////////////////
	// DomainRegistry
	////////////////////////////////////////////////////////////////////
	
	private IDomainRegistry _domainRegistry;
	
	/**
	 * Returns the domain registry of the {@link #getDefault()}'s plugin.
	 * 
	 * <p>
	 * Will throw an {@link IllegalStateException} if still null.  That is, 
	 * the {@link IDomainRegistry} must have previously been provided using
	 * {@link #setDomainRegistry(IDomainRegistry)}.
	 * 
	 * <p>
	 * This method is <tt>static</tt> as a convenience, to save having to
	 * write <tt>getDefault().getDomainRegistry()</tt>.  Admittedly this does
	 * introduce an asymmetry with the setter.
	 */
	public static IDomainRegistry getDomainRegistry() {
		final IDomainRegistry domainRegistry = getDefault()._domainRegistry;
		if (domainRegistry == null) {
			throw new IllegalStateException("DomainRegistry is null");
		}
		return domainRegistry;
	}
	/**
	 * Provides an implementation of the {@link IDomainRegistry} for the
	 * plugin to hold onto.
	 * 
	 * <p>
	 * This design allows us to set up the runtime in different ways as
	 * appropriate.  For example, client vs server vs test framework vs prototyping. 
	 * 
	 * @param domainRegistry
	 */
	public void setDomainRegistry(IDomainRegistry domainRegistry) {
		_domainRegistry = domainRegistry;
	}
	
}
