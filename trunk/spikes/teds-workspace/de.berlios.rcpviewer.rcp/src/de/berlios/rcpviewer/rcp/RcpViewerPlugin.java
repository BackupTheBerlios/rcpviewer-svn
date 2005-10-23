package de.berlios.rcpviewer.rcp;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import de.berlios.rcpviewer.gui.EditorContentBuilderFactory;
import de.berlios.rcpviewer.gui.FieldBuilderFactory;

/**
 * The main plugin class to be used in the desktop.
 */
public class RcpViewerPlugin extends AbstractUIPlugin {
	//The shared instance.
	private static RcpViewerPlugin plugin;
	
	private EditorContentBuilderFactory editorContentBuilderFactory = null;
	private FieldBuilderFactory fieldBuilderFactory = null;
	
	/**
	 * The constructor.
	 */
	public RcpViewerPlugin() {
		super();
		plugin = this;
	}

	/**
	 * This method is called upon plug-in activation
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		editorContentBuilderFactory = new EditorContentBuilderFactory();
		fieldBuilderFactory = new FieldBuilderFactory();
	}

	/**
	 * Returns the shared instance.
	 */
	public static RcpViewerPlugin getDefault() {
		return plugin;
	}

	public FieldBuilderFactory getFieldBuilderFactory() {
		return fieldBuilderFactory;
	}

	public EditorContentBuilderFactory getEditorContentBuilderFactory() {
		return editorContentBuilderFactory;
	}
	
	

}
