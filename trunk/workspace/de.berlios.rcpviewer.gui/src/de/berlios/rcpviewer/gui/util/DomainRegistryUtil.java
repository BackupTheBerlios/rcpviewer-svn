package de.berlios.rcpviewer.gui.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.berlios.rcpviewer.domain.IDomain;
import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.domain.IDomainRegistry;
import de.berlios.rcpviewer.domain.runtime.RuntimePlugin;

/**
 * Static helper methods domain registry functions.
 * @author Mike
 */
public class DomainRegistryUtil {
	
	/**
	 * Return an iterator for all classes in all domains.
	 * @return
	 */
	public static Iterator<IDomainClass> iterateAllClasses() {
    	RuntimePlugin runtimePlugin= RuntimePlugin.getDefault();
    	IDomainRegistry domainRegistry= runtimePlugin.getDomainRegistry();
    	Map<String, IDomain> domains= domainRegistry.getDomains();
		ClassIterator it = new ClassIterator();
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
		
		private final List <IDomainClass> _classes;
		private Iterator<IDomainClass> _iterator = null ;
		
		ClassIterator() {
			_classes = new ArrayList<IDomainClass>();
		}
		
		void addDomain( IDomain domain ) {
			if ( domain == null ) throw new IllegalArgumentException();
			if ( _iterator != null ) throw new IllegalStateException();
			_classes.addAll( domain.classes() );
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

}
