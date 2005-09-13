package de.berlios.rcpviewer.gui;

import java.text.DateFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.apache.log4j.net.SocketAppender;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.packageadmin.PackageAdmin;

import de.berlios.rcpviewer.domain.runtime.IDomainBootstrap;
import de.berlios.rcpviewer.gui.fieldbuilders.FieldBuilderFactory;
import de.berlios.rcpviewer.gui.fieldbuilders.IFieldBuilder;
import de.berlios.rcpviewer.gui.jobs.DomainBootstrapJob;
import de.berlios.rcpviewer.gui.jobs.ObjectStoreBootstrapJob;
import de.berlios.rcpviewer.gui.jobs.SessionBootstrapJob;
import de.berlios.rcpviewer.persistence.IObjectStore;


/**
 * The main plugin class for base GUI functionality.
 */
public class GuiPlugin extends AbstractUIPlugin {
	
	/**
	 * Common formatting for all dates.
	 */
	public static final DateFormat DATE_FORMATTER
		= DateFormat.getDateInstance(DateFormat.SHORT);
	
	// the shared instance.
	private static GuiPlugin __plugin = null;
	
	// fields
	private FieldBuilderFactory _fieldBuilderFactory = null;
	private UniversalLabelProvider _universalLabelProvider = null;
	private PackageAdmin _packageAdmin = null;
	
	/* static methods */
	
	/**
	 * Returns the shared instance.
	 */
	public static GuiPlugin getDefault() {
		return __plugin;
	}

	/**
	 * Returns the string from the plugin's resource bundle,
	 * or 'key' if not found.
	 * <br>Note that this implementation is <b>not</b> the default given
	 * by the plugin creation wizard but instead accesses resources
	 * vai the Plugin's OSGI bundle.
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
	
	
	/* lifecycle methods */
	
	/**
	 * The constructor.
	 */
	public GuiPlugin() {
		super();
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
		
		SocketAppender socketAppender = new SocketAppender("localhost", 4445); //$NON-NLS-1$
		socketAppender.setLocationInfo(true);
		Logger.getRootLogger().addAppender(socketAppender);
//		Logger.getLogger(GuiPlugin.class).info( "Logging started" ); //$NON-NLS-1$
		
		// domain initialisation
		IDomainBootstrap bootstrap = DomainBootstrapFactory.createBootstrap();
		DomainBootstrapJob domainJob = new DomainBootstrapJob( bootstrap );
		domainJob.schedule();
		
		// object store initialisation
		IObjectStore store = ObjectStoreFactory.createObjectStore();
		ObjectStoreBootstrapJob storeJob = new ObjectStoreBootstrapJob( store );
		storeJob.schedule();
		
		// session initialisation (default domain & store for now )
		SessionBootstrapJob sessionJob = new SessionBootstrapJob( store );
		sessionJob.schedule();
		
		// instantiate fields
		_fieldBuilderFactory = new FieldBuilderFactory();
		_universalLabelProvider = new UniversalLabelProvider();
		ServiceReference ref = context.getServiceReference(
				PackageAdmin.class.getName());
		_packageAdmin = (PackageAdmin)context.getService( ref );
		
		// effectively running jobs synchronously
		waitForJob( domainJob );
		waitForJob( sessionJob );
	}

	/**
	 * Clears all references
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		__plugin = null;
	}
	
	/* public accessors */
	
	/**
	 * Accessor to field builders
	 * @param element
	 * @return field builder
	 */
	public IFieldBuilder getFieldBuilder(  ETypedElement element ) {
		return _fieldBuilderFactory.getFieldBuilder( element );
	}
		
	/**
	 * Accessor to universal label providers
	 * @return Returns the label provider
	 */
	public ILabelProvider getLabelProvider() {
		return _universalLabelProvider;
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
