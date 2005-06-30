package de.berlios.rcpviewer.gui;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import de.berlios.rcpviewer.domain.runtime.IDomainBootstrap;
import de.berlios.rcpviewer.gui.editors.EditorContentBuilderFactory;
import de.berlios.rcpviewer.gui.editors.FieldBuilderFactory;
import de.berlios.rcpviewer.gui.jobs.DomainBootstrapJob;
import de.berlios.rcpviewer.gui.jobs.SessionBootstrapJob;


/**
 * The main plugin class for base GUI functionality.
 */
public class GuiPlugin extends AbstractUIPlugin {
	
	
	// the shared instance.
	private static GuiPlugin __plugin = null;

	
	private EditorContentBuilderFactory _editorContentBuilderFactory = null;
	private FieldBuilderFactory __fieldBuilderFactory = null;
	
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
		
		// initialise gui factories
		_editorContentBuilderFactory = new EditorContentBuilderFactory();
		__fieldBuilderFactory = new FieldBuilderFactory();
		
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
	
	/* accessors */
	
	/**
	 * @return
	 */
	public FieldBuilderFactory getFieldBuilderFactory() {
		return __fieldBuilderFactory;
	}

	/**
	 * @return
	 */
	public EditorContentBuilderFactory getEditorContentBuilderFactory() {
		return _editorContentBuilderFactory;
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
