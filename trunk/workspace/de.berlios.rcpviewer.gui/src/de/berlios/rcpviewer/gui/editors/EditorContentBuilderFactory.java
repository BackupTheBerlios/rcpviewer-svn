package de.berlios.rcpviewer.gui.editors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;



public class EditorContentBuilderFactory {
	
	private final IEditorContentBuilder[] builders;
	private final Map<Class, IEditorContentBuilder[]> allApplicable;
	private final Map<Class, IEditorContentBuilder> defaults;
	
	/**
	 * Constructor instantiates all implementations of 
	 * IEditorContentBuilder extension point.
	 * @throws CoreException
	 */
	public EditorContentBuilderFactory() throws CoreException {
        IConfigurationElement[] elems
        	= Platform.getExtensionRegistry().getConfigurationElementsFor( 
					IEditorContentBuilder.EXTENSION_POINT );
		int num = elems.length;
		builders = new IEditorContentBuilder[ num ];
		for ( int i=0 ; i < num ; i++ ) {
			Object obj = elems[i].createExecutableExtension( 
					IEditorContentBuilder.CLASS_PROPERTY );
			assert obj instanceof IEditorContentBuilder;
			builders[i] = (IEditorContentBuilder)obj;
		}
		allApplicable = new HashMap<Class, IEditorContentBuilder[]>();
		defaults = new HashMap<Class, IEditorContentBuilder>();
	}

	/**
	 * Selects and generates the default content builder for the passed instance.
	 * @param instance
	 * @return
	 */
	public IEditorContentBuilder getDefaultInstance( Class clazz ) {
		IEditorContentBuilder builder = defaults.get( clazz ) ;
		if ( builder == null ) builder = new DefaultEditorContentBuilder();
		return builder;
	}
	
	/**
	 * Allow external parties to set the default editor
	 * @param clazz
	 * @param builder
	 */
	public void setDefaultInstance( Class clazz, IEditorContentBuilder builder ) {
		defaults.put( clazz, builder );
	}
	
	/**
	 * Selects and generates the all content builder options for the passed instance.
	 * @param instance
	 * @return
	 */
	public IEditorContentBuilder[] getInstances( Class clazz ) {
		
		IEditorContentBuilder[] applicable = allApplicable.get( clazz ) ;
		if ( applicable == null ) {
			List<IEditorContentBuilder> list = null;
			for ( int i=0, num = builders.length ; i < num ; i++ ) {
				if ( builders[i].isApplicable( clazz ) ) {
					if ( list == null ) list = new ArrayList<IEditorContentBuilder>();
					list.add( builders[i] );
				}
			}
			if ( list != null ) {
				list.add( 0,  new DefaultEditorContentBuilder() );
				applicable = list.toArray( new IEditorContentBuilder[0] );
			}
			else {
				applicable =  new IEditorContentBuilder[]{ 
						new DefaultEditorContentBuilder() };
			}
			allApplicable.put( clazz, applicable );
		}
		return applicable;
	}	

}
