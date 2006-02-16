package org.essentialplatform.louis;

import java.text.DateFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.essentialplatform.core.IBundlePeer;
import org.essentialplatform.louis.app.SecureApplication;
import org.essentialplatform.louis.log.LogController;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.packageadmin.PackageAdmin;


/**
 * The main plugin class for base GUI functionality.
 */
public class LouisPlugin extends AbstractUIPlugin implements IBundlePeer {


	///////////////////////////////////////////
	// Singleton / Constructor
	///////////////////////////////////////////
	
	private static LouisPlugin __plugin = null;
	/**
	 * Returns the shared instance.
	 */
	public static LouisPlugin getDefault() {
		return __plugin;
	}

	public LouisPlugin() {
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

		// set up log4j
		new LogController();

		ServiceReference ref = context.getServiceReference(
				PackageAdmin.class.getName());
		_packageAdmin = (PackageAdmin)context.getService( ref );
	}

	/**
	 * Clears all references
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		Platform.getJobManager().cancel( null );
		super.stop(context);
		__plugin = null;
	}
	


	/////////////////////////////////////////////////////////////////
	// Application.
	// Once the application is running, it registers itself with
	// the LouisPlugin.  This provides a way for other code to
	// locate it.
	/////////////////////////////////////////////////////////////////
	
	private SecureApplication _application;
	/**
	 * Note that this is <tt>static</tt> as a convenience.
	 * 
	 * <p>
	 * Rather than <tt>LouisPlugin.getDefault().getApplication()</tt>, it 
	 * is only necessary to write <tt>LouisPlugin.getApplication()</tt>.  The
	 * asymettry with the setter is a little unusual though, granted.
	 * 
	 * @return
	 */
	public static SecureApplication getApplication() {
		return getDefault()._application;
	}
	/**
	 * TODO: this needs to be removed.  The LouisPlugin should be able to support
	 * multiple domain applications at the same time.  It should look at the
	 * object it is interacting with, look up the IClientDomainBinding from its
	 * IDomainClass, and then get the GuiFactories etc that it needs.
	 * 
	 * @param application
	 */
	public void setApplication(SecureApplication application) {
		_application = application;
	}

	
	/**
	 * Convenience static method that uses global label provider
	 * @param object
	 * @return
	 */
	public static String getText( Object object ) {
		return getApplication().getGlobalLabelProvider().getText( object );
	}
	
	/**
	 * Convenience static method that uses global label provider
	 * @param object
	 * @return
	 */
	public static Image getImage( Object object ) {
		return getApplication().getGlobalLabelProvider().getImage( object );
	}
	
	/**
	 * Convenience static method that uses global transfer provider
	 * @param object
	 * @return
	 */
	public static Transfer getTransfer( Class clazz ) {
		return getApplication().getGlobalTransferProvider().getTransfer( clazz );
	}

	
	/////////////////////////////////////////////////////////////////
	// PackageAdmin
	/////////////////////////////////////////////////////////////////
	
	private PackageAdmin _packageAdmin = null;

	/**
	 * Accessor to platform utility class.
	 * @return
	 */
	public PackageAdmin getPackageAdmin() {
		assert _packageAdmin != null;
		return _packageAdmin;
	}
	

	/////////////////////////////////////////////////////////////////
	// Localization and Formatting
	/////////////////////////////////////////////////////////////////
	
	/**
	 * Common formatting for all dates.
	 */
	public static final DateFormat DATE_FORMATTER
		= DateFormat.getDateInstance(DateFormat.SHORT);

	
	/**
	 * Returns the string from the plugin's resource bundle,
	 * or the key value if not found.
	 * <br>Note that this implementation is <b>not</b> the default given
	 * by the plugin creation wizard but instead accesses resources
	 * via the Plugin's OSGI bundle.
	 */
	public static String getResourceString(String key) {
		ResourceBundle bundle =
			Platform.getResourceBundle( getDefault().getBundle() );
		try {
			return (bundle != null) ? bundle.getString(key) : key;
		} 
		catch (MissingResourceException e) {
			return key;
		}
	}



}
