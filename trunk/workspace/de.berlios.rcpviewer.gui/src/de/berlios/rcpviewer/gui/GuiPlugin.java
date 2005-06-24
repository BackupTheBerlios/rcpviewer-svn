package de.berlios.rcpviewer.gui;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import de.berlios.rcpviewer.gui.editors.EditorContentBuilderFactory;
import de.berlios.rcpviewer.gui.editors.FieldBuilderFactory;


/**
 * The main plugin class for base GUI functionality.
 */
public class GuiPlugin extends AbstractUIPlugin {
	
	
	// the shared instance.
	private static GuiPlugin plugin = null;

	
	private EditorContentBuilderFactory editorContentBuilderFactory = null;
	private FieldBuilderFactory fieldBuilderFactory = null;
	
	/* static methods */
	
	/**
	 * Returns the shared instance.
	 */
	public static GuiPlugin getDefault() {
		return plugin;
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
		plugin = this;
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
		Job job = DomainInitialiserFactory.getInitialiserJob();
		job.schedule();
		
		// initialise gui factories
		editorContentBuilderFactory = new EditorContentBuilderFactory();
		fieldBuilderFactory = new FieldBuilderFactory();
		
		// wait for domain initialisation to end
		job.join();
		if ( !job.getResult().isOK() ) {
			getLog().log( job.getResult() );
			throw new CoreException( job.getResult() );
		}
	}

	/**
	 * Clears all references
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		plugin = null;
	}
	
	/* accessors */
	
	/**
	 * @return
	 */
	public FieldBuilderFactory getFieldBuilderFactory() {
		return fieldBuilderFactory;
	}

	/**
	 * @return
	 */
	public EditorContentBuilderFactory getEditorContentBuilderFactory() {
		return editorContentBuilderFactory;
	}
}
