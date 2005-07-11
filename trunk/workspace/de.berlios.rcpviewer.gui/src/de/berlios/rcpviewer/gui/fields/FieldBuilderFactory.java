package de.berlios.rcpviewer.gui.fields;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EAttribute;

import de.berlios.rcpviewer.gui.util.ConfigElementSorter;

/**
 *As it says.
 * @author Mike
 */
public class FieldBuilderFactory {
	
	private final IFieldBuilder[] _builders;
	private final Map<EAttribute, IFieldBuilder> _mappings;
	
	
	/**
	 * Constructor instantiates all implementations of 
	 * <code>IFieldBuilder.EXTENSION_POINT_ID </code> extension point.
	 * @throws CoreException
	 */
	public FieldBuilderFactory() throws CoreException {
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
		_mappings = new HashMap<EAttribute, IFieldBuilder>();
	}
	
	/**
	 * Selects and generates IFieldBuilder appropriate for passed attribute
	 * @param attribute
	 * @return
	 */
	public IFieldBuilder getInstance( EAttribute attribute ) {
		if ( attribute == null ) throw new IllegalArgumentException();
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
