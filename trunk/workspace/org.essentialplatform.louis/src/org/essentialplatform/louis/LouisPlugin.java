package org.essentialplatform.louis;

import java.text.DateFormat;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.essentialplatform.core.deployment.Binding;
import org.essentialplatform.louis.dnd.GlobalDnDTransferProvider;
import org.essentialplatform.louis.dnd.IDndTransferProvider;
import org.essentialplatform.louis.factory.GuiFactories;
import org.essentialplatform.louis.factory.IGuiFactory;
import org.essentialplatform.louis.labelproviders.GlobalLabelProvider;
import org.essentialplatform.louis.log.LogController;
import org.essentialplatform.progmodel.essential.runtime.EssentialProgModelRuntimeBuilder;
import org.essentialplatform.runtime.IDomainBootstrap;
import org.essentialplatform.runtime.RuntimeBinding;
import org.essentialplatform.runtime.persistence.IObjectStore;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.packageadmin.PackageAdmin;


/**
 * The main plugin class for base GUI functionality.
 */
public class LouisPlugin extends AbstractUIPlugin {
	
	/**
	 * Common formatting for all dates.
	 */
	public static final DateFormat DATE_FORMATTER
		= DateFormat.getDateInstance(DateFormat.SHORT);
	
	// the shared instance.
	private static LouisPlugin __plugin = null;
	
	// fields
	private GuiFactories _guiFactories= null;
	private GlobalLabelProvider _labelProvider = null;
	private GlobalDnDTransferProvider _transferProvider = null;
	private PackageAdmin _packageAdmin = null;
	
	/* static methods */
	
	/**
	 * Returns the shared instance.
	 */
	public static LouisPlugin getDefault() {
		return __plugin;
	}

	/**
	 * Returns the string from the plugin's resource bundle,
	 * or the key value if not found.
	 * <br>Note that this implementation is <b>not</b> the default given
	 * by the plugin creation wizard but instead accesses resources
	 * via the Plugin's OSGI bundle.
	 */
	public static String getResourceString(String key) {
		ResourceBundle bundle
			= Platform.getResourceBundle( getDefault().getBundle() );
		try {
			return (bundle != null) ? bundle.getString(key) : key;
		} 
		catch (MissingResourceException e) {
			return key;
		}
	}
	
	/**
	 * Convenience static method that uses global label provider
	 * @param object
	 * @return
	 */
	public static String getText( Object object ) {
		return getDefault().getLabelProvider().getText( object );
	}
	
	/**
	 * Convenience static method that uses global label provider
	 * @param object
	 * @return
	 */
	public static Image getImage( Object object ) {
		return getDefault().getLabelProvider().getImage( object );
	}
	
	/**
	 * Convenience static method that uses global transfer provider
	 * @param object
	 * @return
	 */
	public static Transfer getTransfer( Class clazz ) {
		return getDefault().getTransferProvider().getTransfer( clazz );
	}
	
	/* lifecycle methods */
	
	/**
	 * The constructor.
	 */
	public LouisPlugin() {
		super();
		Binding.setBinding(
			new RuntimeBinding(new EssentialProgModelRuntimeBuilder()));
		__plugin = this;
	}
	

	/**
	 * Initialises the domain and gui factories - any errors cause a
	 * <code>CoreException</code> to be thrown and the bundle will not be started.
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 * @see org.eclipse.core.runtime.CoreException
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		
//		SocketAppender socketAppender = new SocketAppender("localhost", 4445); //$NON-NLS-1$
//		socketAppender.setLocationInfo(true);
//		Logger.getRootLogger().addAppender(socketAppender);
//		Logger.getLogger(GuiPlugin.class).info( "Logging started" ); //$NON-NLS-1$
		new LogController();
		
		// domain initialisation
		IDomainBootstrap bootstrap = DomainBootstrapFactory.createBootstrap();
		DomainBootstrapJob domainJob = new DomainBootstrapJob( bootstrap );
		domainJob.schedule();

		// object store initialisation
		IObjectStore store = ObjectStoreFactory.createObjectStore();
		ObjectStoreBootstrapJob storeJob = new ObjectStoreBootstrapJob( store );
//		storeJob.schedule();

		// session initialisation (default domain & store for now )
		SessionBootstrapJob sessionJob = new SessionBootstrapJob( store );
		sessionJob.schedule();

		// instantiate fields
		_guiFactories = new GuiFactories();
		_labelProvider = new GlobalLabelProvider();
		ServiceReference ref = context.getServiceReference(
				PackageAdmin.class.getName());
		_packageAdmin = (PackageAdmin)context.getService( ref );
		
		// effectively running jobs synchronously at the moment
		waitForJob( domainJob );
//		waitForJob( storeJob );
		waitForJob( sessionJob );
		
		// this must be run once domain classes known
		_transferProvider = new GlobalDnDTransferProvider();
		
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
	
	/* public accessors */
	
	/**
	 * Returns first factory that is applicable for the passed arguments.
	 * @param object
	 * @param context
	 * @return gui factory
	 */
	public IGuiFactory<?> getGuiFactory( Object model, IGuiFactory parent ) {
		return _guiFactories.getFactory( model, parent );
	}
	
	/**
	 * Returns all factories that are applicable for the passed arguments.
	 * <br>Will never be empty as will include the default factory if no
	 * others found.
	 * @param object
	 * @param context
	 * @return list of gui factories
	 */
	public List<IGuiFactory<?>> getGuiFactories( Object model, IGuiFactory parent ) {
		return _guiFactories.getFactories( model, parent );
	}
		
	/**
	 * Accessor to global label provider
	 * @param object
	 * @return label provider
	 */
	public ILabelProvider getLabelProvider() {
		return _labelProvider;
	}
	
	/**
	 * Accessor to global transfer provider
	 * @param object
	 * @return transfer provider
	 */
	public IDndTransferProvider getTransferProvider() {
		return _transferProvider;
	}	
	
	/**
	 * Accessor to platform utility class.
	 * @return
	 */
	public PackageAdmin getPackageAdmin() {
		assert _packageAdmin != null;
		return _packageAdmin;
	}
	
	/* private methods */
	
	// as it says
	private void waitForJob( Job job ) throws InterruptedException, CoreException {
		assert job != null;
		job.join();
		if ( !job.getResult().isOK() ) {
			getLog().log( job.getResult() );
			throw new CoreException( job.getResult() );
		}
	}


}
