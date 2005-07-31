package de.berlios.rcpviewer.gui;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.ETypedElement;

import de.berlios.rcpviewer.gui.util.ConfigElementSorter;

/**
 *As it says.
 * @author Mike
 */
class FieldBuilderFactory {
	
	private final IFieldBuilder[] _builders;
	private final Map<ETypedElement, IFieldBuilder> _mappings;
	
	
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
			Object obj = elems[i].createExecutableExtension( "class" ); //$NON-NLS-1$
			assert obj instanceof IFieldBuilder;
			_builders[i] = (IFieldBuilder)obj;
		}
		_mappings = new HashMap<ETypedElement, IFieldBuilder>();
	}
	
	/**
	 * Selects and generates IFieldBuilder appropriate for passed element
	 * @param element
	 * @return
	 */
	IFieldBuilder getFieldBuilder( ETypedElement element ) {
		if ( element == null ) throw new IllegalArgumentException();
		IFieldBuilder builder = _mappings.get( element );
		if ( builder == null ) {
			for ( int i=0, num = _builders.length ; i < num ; i++ ) {
				if ( _builders[i].isApplicable( element ) ) {
					builder = _builders[i];
					break;
				}
			}
			if ( builder == null ) {
				builder = new DefaultFieldBuilder();
			}
			_mappings.put( element, builder );
		}
		return builder;
	}

}