package de.berlios.rcpviewer.gui;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;

import de.berlios.rcpviewer.RCPViewer;
import de.berlios.rcpviewer.util.ConfigElementSorter;


public class FieldBuilderFactory {
	
	private final IFieldBuilder[] builders;
	private final Map mappings;
	
	
	/**
	 * Constructor instantiates all implementations of 
	 * "mikespike3.fieldbuilder" extension point.
	 * @throws CoreException
	 */
	public FieldBuilderFactory() throws CoreException {
        IConfigurationElement[] elems
        	= Platform.getExtensionRegistry()
                  .getConfigurationElementsFor( RCPViewer.FIELDBUILDERS_EXTENSION_POINT_ID);
		Arrays.sort( elems, new ConfigElementSorter() );
		int num = elems.length;
		builders = new IFieldBuilder[ num ];
		for ( int i=0 ; i < num ; i++ ) {
			Object obj = elems[i].createExecutableExtension( "class" );
			assert obj instanceof IFieldBuilder;
			builders[i] = (IFieldBuilder)obj;
		}
		mappings = new HashMap();
	}

	/**
	 * Selects and generates IFieldBuilder appropriate for passed method on the
	 * passed instance
	 * @param instance
	 * @return
	 */
	public IFieldBuilder getInstance( Class clazz, Object value ) {
		// ignoring value for now for simplicity
		IFieldBuilder builder = (IFieldBuilder)mappings.get( clazz );
		if ( builder == null ) {
			for ( int i=0, num = builders.length ; i < num ; i++ ) {
				if ( builders[i].isApplicable( clazz, value ) ) {
					builder = builders[i];
					break;
				}
			}
			if ( builder == null ) {
				builder = new DefaultFieldBuilder();
			}
			mappings.put( clazz, builder );
		}
		return builder;
	}

}
