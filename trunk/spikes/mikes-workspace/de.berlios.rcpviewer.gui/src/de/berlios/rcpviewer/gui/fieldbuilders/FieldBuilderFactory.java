package de.berlios.rcpviewer.gui.fieldbuilders;

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
public class FieldBuilderFactory {
	
	private final IFieldBuilder[] _builders;
	private final Map<ETypedElement, IFieldBuilder> _mappings;
	
	
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
		
		_builders = new IFieldBuilder[ num + 8 ];
		_builders[0] = new BooleanFieldBuilder();
		_builders[1] = new PrimitiveFieldBuilder( byte.class );
		_builders[2] = new PrimitiveFieldBuilder( char.class );
		_builders[3] = new PrimitiveFieldBuilder( short.class );
		_builders[4] = new PrimitiveFieldBuilder( int.class );
		_builders[5] = new PrimitiveFieldBuilder( long.class );
		_builders[6] = new PrimitiveFieldBuilder( float.class );
		_builders[7] = new PrimitiveFieldBuilder( double.class );
		for ( int i=0 ; i < num ; i++ ) {
			Object obj = elems[i].createExecutableExtension( "class" ); //$NON-NLS-1$
			assert obj instanceof IFieldBuilder;
			_builders[i+8] = (IFieldBuilder)obj;
		}
		_mappings = new HashMap<ETypedElement, IFieldBuilder>();
	}
	
	/**
	 * Selects and generates IFieldBuilder appropriate for passed element
	 * @param element
	 * @return
	 */
	public IFieldBuilder getFieldBuilder( ETypedElement element ) {
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
