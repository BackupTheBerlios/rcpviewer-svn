package mikespike3.gui;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import mikespike3.util.ConfigElementSorter;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;


public class FieldBuilderFactory {
	
	private final IFieldBuilder[] builders;
	private final Map mappings;
	
	public FieldBuilderFactory() throws CoreException {
        IConfigurationElement[] elems
        	= Platform.getExtensionRegistry()
                  .getConfigurationElementsFor( "mikespike3.fieldbuilder" );
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
	public IFieldBuilder create( Class clazz, Object value ) {
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
