package org.essentialplatform.louis.dnd;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.dnd.Transfer;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.domain.filters.NoopClassFilter;
import org.essentialplatform.louis.util.ConfigElementSorter;
import org.essentialplatform.louis.util.DomainRegistryUtil;
import org.essentialplatform.runtime.shared.domain.bindings.IDomainClassRuntimeBinding;

/**
 * A global <code>IDndTransferProvider</code> that can handle any object.
 * <br>Collects all declared <code>IDndTransferProvider</code>'s and 
 * loops through these using the first that can handle the passed object.
 * If none found creates a default transfer provider that will work only
 * within Louis.
 * @author Mike
 * @see org.essentialplatform.louis.dnd.IDndTransferProvider
 */
public class GlobalDnDTransferProvider implements IDndTransferProvider {
	
	
	private final List<IDndTransferProvider> _declaredProviders;
	private final Map<Class,Transfer> _allTransfers;

	/**
	 * Constructor instantiates all implementations of 
	 * <code>IDndTransferProvider.EXTENSION_POINT_ID </code> extension point.
	 * @throws CoreException
	 */
	public GlobalDnDTransferProvider() throws CoreException {
        
		IConfigurationElement[] elems
    		= Platform.getExtensionRegistry()
              .getConfigurationElementsFor( 
            		  IDndTransferProvider.EXTENSION_POINT_ID );
        Arrays.sort( elems, new ConfigElementSorter() );
        
        _declaredProviders = new ArrayList<IDndTransferProvider>();
        for ( IConfigurationElement elem : elems ) {
			Object obj = elem.createExecutableExtension( "class" ); //$NON-NLS-1$
			assert obj instanceof IDndTransferProvider;
			_declaredProviders.add ( (IDndTransferProvider)obj );
        }
        _allTransfers = new HashMap<Class,Transfer>();
        
        // bootstrap all domain & standard classes
        bootstrap();
	}
	
	/* (non-Javadoc)
	 * @see org.essentialplatform.louis.dnd.IDndTransferProvider#getTransfer(java.lang.Class)
	 */
	public Transfer getTransfer(Class clazz) {
		if( clazz == null ) throw new IllegalArgumentException();
		
		// previously cached?
		Transfer transfer = _allTransfers.get( clazz );
		if ( transfer != null ) return transfer;
		
		// hunt through declared providers
		for( IDndTransferProvider focus : _declaredProviders ) {
			transfer = focus.getTransfer( clazz );
			if ( transfer != null ) {
				_allTransfers.put( clazz, transfer );
				return transfer;
			}
		}
		
		// still here..? 
		// must create new generic transfer for class
		if ( clazz.isPrimitive() ) {
			transfer = new PrimitiveTransfer( clazz );
		}
		else if ( DomainRegistryUtil.isDomainClass( clazz ) ) {
			transfer = new DomainClassTransfer( clazz );
		}
		else {
			transfer = new GenericTransfer( clazz );
		}
		assert transfer != null;
		_allTransfers.put( clazz, transfer );
		return transfer;
	}
	
	/* (non-Javadoc)
	 * @see org.essentialplatform.louis.dnd.IDndTransferProvider#getAllTransfers()
	 */
	public Transfer[] getAllTransfers() {
		return _allTransfers.values().toArray( new Transfer[0] );
	}
	
	// causes providers to be instantiated for all common & domain classes
	private void bootstrap() {
		getTransfer( Boolean.class );
		getTransfer( Byte.class );
		getTransfer( Character.class );
		getTransfer( Short.class );
		getTransfer( Integer.class );
		getTransfer( Long.class );
		getTransfer( Float.class );
		getTransfer( Double.class );
		getTransfer( Date.class );
		getTransfer( String.class );
		getTransfer( BigDecimal.class );
		
		// domain classes
		for(IDomainClass domainClass: DomainRegistryUtil.allClasses(
											new NoopClassFilter())) {
			Class javaClass = ((IDomainClassRuntimeBinding<?>)domainClass.getBinding()).getJavaClass();
			getTransfer(javaClass);
		}
		
	}
}
