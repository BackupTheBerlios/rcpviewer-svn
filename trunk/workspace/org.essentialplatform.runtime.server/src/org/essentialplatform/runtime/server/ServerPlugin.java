package org.essentialplatform.runtime.server;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.essentialplatform.core.IBundlePeer;
import org.osgi.framework.BundleContext;

/**
 * The main __plugin class to be used in the desktop.
 */
public class ServerPlugin extends AbstractUIPlugin implements IBundlePeer {

	///////////////////////////////////////////
	// Singleton / Constructor
	///////////////////////////////////////////
	
	private static ServerPlugin __plugin;
	
	public ServerPlugin() {
		__plugin = this;
	}

	public static ServerPlugin getDefault() {
		return __plugin;
	}


	/////////////////////////////////////////////////////////////////
	// Id
	/////////////////////////////////////////////////////////////////

	public String getId() {
		return ServerPlugin.class.getPackage().getName();
	}
	

	/////////////////////////////////////////////////////////////////
	// Lifecycle Methods
	/////////////////////////////////////////////////////////////////

	public void start(BundleContext context) throws Exception {
		super.start(context);
	}

	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		__plugin = null;
	}


	/////////////////////////////////////////////////////////////////
	// Localization and Formatting
	/////////////////////////////////////////////////////////////////

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path.
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return AbstractUIPlugin.imageDescriptorFromPlugin(getDefault().getId(), path);
	}
	

}
