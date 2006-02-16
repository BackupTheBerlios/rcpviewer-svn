package org.essentialplatform.runtime.client.tests;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.essentialplatform.core.IBundlePeer;
import org.osgi.framework.BundleContext;

public class ClientTestsPlugin extends AbstractUIPlugin implements IBundlePeer {


	///////////////////////////////////////////
	// Singleton / Constructor
	///////////////////////////////////////////
	
	// the shared instance.
	private static ClientTestsPlugin __plugin = null;
	/**
	 * Returns the shared instance.
	 */
	public static ClientTestsPlugin getDefault() {
		return __plugin;
	}

	public ClientTestsPlugin() {
		super();
		__plugin = this;
	}


	/////////////////////////////////////////////////////////////////
	// Id
	/////////////////////////////////////////////////////////////////

	public String getId() {
		return getClass().getPackage().getName();
	}

	
	/////////////////////////////////////////////////////////////////
	// Lifecycle Methods
	/////////////////////////////////////////////////////////////////


	/**
	 * Initialises the domain and gui factories - any errors cause a
	 * <code>CoreException</code> to be thrown and the bundle will not be started.
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 * @see org.eclipse.core.runtime.CoreException
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
	}

	/**
	 * Clears all references
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		__plugin = null;
	}

}
