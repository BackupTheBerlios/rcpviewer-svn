package de.berlios.rcpviewer.gui.editors;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EAttribute;

import de.berlios.rcpviewer.gui.util.ConfigElementSorter;


class FieldBuilderFactory {
	
	private final IFieldBuilder[] _builders;
	private final Map<EAttribute, IFieldBuilder> _mappings;
	
	/**
	 * Constructor instantiates all implementations of 
	 * <code>IFieldBuilder.EXTENSION_POINT_ID </code> extension point.
	 * @throws CoreException
	 */
	FieldBuilderFactory() throws CoreException {
        IConfigurationElement[] elems
        	= Platform.getExtensionRegistry()
                  .getConfigurationElementsFor( IFieldBuilder.EXTENSION_POINT_ID );
		Arrays.sort( elems, new ConfigElementSorter() );
		int num = elems.length;
		_builders = new IFieldBuilder[ num ];
		for ( int i=0 ; i < num ; i++ ) {
			Object obj = elems[i].createExecutableExtension( "class" );
			assert obj instanceof IFieldBuilder;
			_builders[i] = (IFieldBuilder)obj;
		}
		_mappings = new HashMap<EAttribute, IFieldBuilder>();
	}

	/**
	 * Selects and generates IFieldBuilder appropriate for passed attribute
	 * @param attribute
	 * @return
	 */
	IFieldBuilder getInstance( EAttribute attribute ) {
		IFieldBuilder builder = _mappings.get( attribute );
		if ( builder == null ) {
			for ( int i=0, num = _builders.length ; i < num ; i++ ) {
				if ( _builders[i].isApplicable( attribute ) ) {
					builder = _builders[i];
					break;
				}
			}
			if ( builder == null ) {
				builder = new DefaultFieldBuilder();
			}
			_mappings.put( attribute, builder );
		}
		return builder;
	}

}
