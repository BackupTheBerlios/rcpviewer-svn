package mikespike3;

import mikespike3.gui.FieldBuilderFactory;
import mikespike3.gui.EditorContentBuilderFactory;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 */
public class Plugin extends AbstractUIPlugin {
	//The shared instance.
	private static Plugin plugin;
	
	private EditorContentBuilderFactory editorContentBuilderFactory = null;
	private FieldBuilderFactory fieldBuilderFactory = null;
	
	/**
	 * The constructor.
	 */
	public Plugin() {
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
	public static Plugin getDefault() {
		return plugin;
	}

	public FieldBuilderFactory getFieldBuilderFactory() {
		return fieldBuilderFactory;
	}

	public EditorContentBuilderFactory getEditorContentBuilderFactory() {
		return editorContentBuilderFactory;
	}
	
	

}
