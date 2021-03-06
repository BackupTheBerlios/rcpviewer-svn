package de.berlios.rcpviewer.gui.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.berlios.rcpviewer.domain.IDomain;
import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.domain.IDomainRegistry;
import de.berlios.rcpviewer.domain.runtime.RuntimePlugin;
import de.berlios.rcpviewer.progmodel.extended.IExtendedDomainClass;

/**
 * Static helper methods domain registry functions.
 * @author Mike
 */
public class DomainRegistryUtil {
	
	public enum Filter { ALL, INSTANTIABLE }
	
	
	/**
	 * Return an iterator for all classes in all domains, passed through
	 * one of the following filters:
	 * <ul>
	 * <li><code>ALL</code> : all classes
	 * <li><code>INSTANTIABLE</code> : only those that are instantiable 
	 * </ul>
	 * @return
	 */
	public static Iterator<IDomainClass> iterateAllClasses( Filter filter ) {
		if ( filter == null ) throw new IllegalArgumentException();
    	RuntimePlugin runtimePlugin= RuntimePlugin.getDefault();
    	IDomainRegistry domainRegistry= runtimePlugin.getDomainRegistry();
    	Map<String, IDomain> domains= domainRegistry.getDomains();
    	IClassFilter classFilter;
    	switch( filter ) {
    		case ALL:
    			classFilter = null;
    			break;
    		case INSTANTIABLE:
    			classFilter = new InstantiableFilter();
    			break;
    		default:
    			throw new IllegalArgumentException();
    	}
		ClassIterator it = new ClassIterator( classFilter );
		for (IDomain domain: domains.values()) {
			it.addDomain( domain );
    	}
		return it;
	}
	

	// prevent instantiation
	private DomainRegistryUtil() {
		super();
	}
	
	// iterator - add domains before first public call.
	private static class ClassIterator implements Iterator<IDomainClass> {
		
		private final IClassFilter _filter;
		private final List <IDomainClass> _classes;
		private Iterator<IDomainClass> _iterator = null ;
		
		ClassIterator( IClassFilter filter ) {
			// filter can be null
			_filter = filter;
			_classes = new ArrayList<IDomainClass>();
		}
		
		void addDomain( IDomain domain ) {
			if ( domain == null ) throw new IllegalArgumentException();
			if ( _iterator != null ) throw new IllegalStateException();
			if ( _filter == null ) {
				_classes.addAll( domain.classes() );
			}
			else {
				for ( IDomainClass<?> clazz : domain.classes() ) {
					if ( _filter.isApplicable( clazz ) ) {
						_classes.add( clazz );
					}
				}
			}
		}

		/* (non-Javadoc)
		 * @see java.util.Iterator#hasNext()
		 */
		public boolean hasNext() {
			if ( _iterator == null ) _iterator = _classes.iterator();
			return _iterator.hasNext();
		}

		/* (non-Javadoc)
		 * @see java.util.Iterator#next()
		 */
		public IDomainClass next() {
			if ( _iterator == null ) _iterator = _classes.iterator();
			return _iterator.next();
		}

		/**
		 * Unsupported.
		 * @see java.util.Iterator#remove()
		 */
		public void remove() {
			throw new UnsupportedOperationException();
		}	
	}
	
	private static interface IClassFilter {
		boolean isApplicable( IDomainClass<?> clazz );
	}
	
	private static class InstantiableFilter implements IClassFilter {
		public boolean isApplicable( IDomainClass<?> clazz ){
			assert clazz != null;
			IExtendedDomainClass<?> extClazz = (IExtendedDomainClass)
				clazz.getAdapter(IExtendedDomainClass.class);
			if ( extClazz == null ) return false;
			return extClazz.isInstantiable();
		}
	}

}
