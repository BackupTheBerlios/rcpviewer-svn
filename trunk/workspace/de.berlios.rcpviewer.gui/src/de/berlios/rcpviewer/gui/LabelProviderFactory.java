package de.berlios.rcpviewer.gui;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.ILabelProvider;

import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.gui.util.ConfigElementSorter;

/**
 * As it says.
 * <br>Internally works out correct label provider for the passed
 * <code>IDomainClass</code>.
 * @author Mike
 */
class LabelProviderFactory {
	
	private final IDomainObjectLabelProvider[] _labelProviders;
	private final Map<IDomainClass, IDomainObjectLabelProvider> _mappings;
	
	
	/**
	 * Constructor instantiates all implementations of 
	 * <code>IDomainObjectLabelProvider.EXTENSION_POINT_ID </code> extension point.
	 * @throws CoreException
	 */
	LabelProviderFactory() throws CoreException {
        IConfigurationElement[] elems
        	= Platform.getExtensionRegistry()
                  .getConfigurationElementsFor( 
                		  IDomainObjectLabelProvider.EXTENSION_POINT_ID );
		Arrays.sort( elems, new ConfigElementSorter() );
		int num = elems.length;
		_labelProviders = new IDomainObjectLabelProvider[ num ];
		for ( int i=0 ; i < num ; i++ ) {
			Object obj = elems[i].createExecutableExtension( "class" ); //$NON-NLS-1$
			assert obj instanceof IFieldBuilder;
			_labelProviders[i] = (IDomainObjectLabelProvider)obj;
		}
		_mappings = new HashMap<IDomainClass, IDomainObjectLabelProvider>();
	}
	
	/**
	 * Selects and generates <code>ILabelProvider</code> appropriate for 
	 * passed object
	 * @param object
	 * @return
	 */
	ILabelProvider getLabelProvider( IDomainClass clazz ) {
		if ( clazz == null ) throw new IllegalArgumentException();
		IDomainObjectLabelProvider provider = _mappings.get( clazz );
		if ( provider == null ) {
			for ( int i=0, num = _labelProviders.length ; i < num ; i++ ) {
				if ( _labelProviders[i].isApplicable( clazz ) ) {
					provider = _labelProviders[i];
					break;
				}
			}
			if ( provider == null ) {
				provider = new DefaultLabelProvider();
			}
			_mappings.put( clazz, provider );
		}
		return provider;
	}

}
