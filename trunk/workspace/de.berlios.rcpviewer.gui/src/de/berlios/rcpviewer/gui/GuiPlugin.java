package de.berlios.rcpviewer.gui;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.packageadmin.PackageAdmin;

import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.domain.runtime.IDomainBootstrap;
import de.berlios.rcpviewer.gui.fieldbuilders.FieldBuilderFactory;
import de.berlios.rcpviewer.gui.fieldbuilders.IFieldBuilder;
import de.berlios.rcpviewer.gui.jobs.DomainBootstrapJob;
import de.berlios.rcpviewer.gui.jobs.SessionBootstrapJob;
import de.berlios.rcpviewer.session.IDomainObject;


/**
 * The main plugin class for base GUI functionality.
 */
public class GuiPlugin extends AbstractUIPlugin {
	
	
	// the shared instance.
	private static GuiPlugin __plugin = null;
	
	// fields
	private FieldBuilderFactory _fieldBuilderFactory = null;
	private LabelProviderFactory _labelProviderFactory = null;
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
		
		// start domain initialisation
		IDomainBootstrap bootstrap = DomainBootstrapFactory.createBootstrap();
		DomainBootstrapJob domainJob = new DomainBootstrapJob( bootstrap );
		domainJob.schedule();
		
		// start session initialisation (default domain & store for now )
		SessionBootstrapJob sessionJob = new SessionBootstrapJob();
		sessionJob.schedule();
		
		// instantiate fields
		_fieldBuilderFactory = new FieldBuilderFactory();
		_labelProviderFactory = new LabelProviderFactory();
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
	 * Accessor to label providers  for domain objects
	 * @param object
	 * @return Returns the fieldBuilderFactory.
	 */
	public ILabelProvider getLabelProvider(  IDomainObject<?> object  ) {
		if ( object == null ) throw new IllegalArgumentException();
		return getLabelProvider( object.getDomainClass() );
	}
	
	/**
	 * Accessor to label providers for domain classes
	 * @param object
	 * @return Returns the fieldBuilderFactory.
	 */
	public ILabelProvider getLabelProvider(  IDomainClass<?> clazz  ) {
		if ( clazz == null ) throw new IllegalArgumentException();
		return _labelProviderFactory.getLabelProvider( clazz ); 
	}
	
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
