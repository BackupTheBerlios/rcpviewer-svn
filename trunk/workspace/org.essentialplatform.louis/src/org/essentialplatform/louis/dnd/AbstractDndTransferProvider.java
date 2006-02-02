package org.essentialplatform.louis.dnd;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.dnd.Transfer;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.domain.filters.NoopClassFilter;
import org.essentialplatform.louis.util.DomainRegistryUtil;
import org.essentialplatform.runtime.shared.domain.bindings.IDomainClassRuntimeBinding;

public abstract class AbstractDndTransferProvider implements
		IDndTransferProvider {

	/*
	 * Initializes all primitives and all registered {@link IDomainClass}es.
	 * 
	 * @see org.essentialplatform.louis.dnd.IDndTransferProvider#init()
	 */
	public final void init() {
		// causes providers to be instantiated for primitives etc...
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
		
		// ... and for domain classes
		for(IDomainClass domainClass: DomainRegistryUtil.allClasses(new NoopClassFilter())) {
			Class javaClass = ((IDomainClassRuntimeBinding<?>)domainClass.getBinding()).getJavaClass();
			getTransfer(javaClass);
		}
	}

	
	private final List<IDndTransferProvider> _declaredProviders = new ArrayList<IDndTransferProvider>();
	protected final boolean addDeclaredProvider(IDndTransferProvider dndTransferProvider) {
		return _declaredProviders.add ( dndTransferProvider );
	}
	


	private final Map<Class,Transfer> _transfersByClass = new HashMap<Class,Transfer>();
	/*
	 * @see org.essentialplatform.louis.dnd.IDndTransferProvider#getTransfer(java.lang.Class)
	 */
	public final Transfer getTransfer(Class clazz) {
		if( clazz == null ) throw new IllegalArgumentException();
		
		// previously cached?
		Transfer transfer = _transfersByClass.get( clazz );
		if ( transfer != null ) return transfer;
		
		// hunt through declared providers
		for( IDndTransferProvider focus : _declaredProviders ) {
			transfer = focus.getTransfer( clazz );
			if ( transfer != null ) {
				_transfersByClass.put( clazz, transfer );
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
		_transfersByClass.put( clazz, transfer );
		return transfer;
	}
	
	/*
	 * @see org.essentialplatform.louis.dnd.IDndTransferProvider#getAllTransfers()
	 */
	public final Transfer[] getAllTransfers() {
		return _transfersByClass.values().toArray( new Transfer[0] );
	}


}
