package de.berlios.rcpviewer.metamodel;

import de.berlios.rcpviewer.metamodel.annotations.*;
import de.berlios.rcpviewer.metamodel.impl.*;
import de.berlios.rcpviewer.progmodel.standard.DomainClass;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Register class, working out its structure.
 * 
 * TODO: may not be needed, since can go to EMF (see {@link EmfFacade})
 */
public final class DomainClassRegistry {
	
	// TODO: should convert this into an aspect.
	private static ThreadLocal<DomainClassRegistry> registryForThisThread = 
			new ThreadLocal<DomainClassRegistry>() {
		protected synchronized DomainClassRegistry initialValue() {
			return new DomainClassRegistry();
		}
	};
	public static DomainClassRegistry instance() {
		return registryForThisThread.get();
	}

	/**
	 * managed using {@link Container}
	 */
	public DomainClassRegistry() {
	}
	
	public Collection classes() {
		return Collections.unmodifiableCollection(domainClassesByClazz.values());
	}
	
	public IDomainObject getDomainObjectFor(Object pojo) {
		DomainAspect domainAspect = DomainAspect.aspectOf(pojo);
		if (domainAspect == null) {
			return null;
		}
		return domainAspect.getDomainObject();
	}
	
	/**
	 * creates a {@link DomainClass} for the supplied {@link Class} if not present,
	 * provided that the class in question implements DomainObject (else
	 * returns null *** this functionality commented out ***).
	 * 
	 * <p>
	 * If already registered, simply returns, same way as {@link #lookup(Class)}.
	 * @param clazz
	 * @return corresponding {@link DomainClass}
	 */
	public DomainClass register(final Class clazz) {
		// TODO: for some reason, not working even though AspectJ says that
		// the introduction will be applied???
//		if (!DomainObject.class.isAssignableFrom(clazz)) {
//			return null;
//		}
		DomainClass domainClass = lookup(clazz);
		if (domainClass == null) {
			domainClass = new DomainClass(clazz);
			domainClassesByClazz.put(clazz, domainClass);
		}
		return domainClass;
	}
	
	/**
	 * looks up the {@link DomainClass} for the supplied {@link Class}. 
	 * @param clazz
	 * @return corresponding {@link DomainClass}, or <tt>null</tt>
	 */
	public DomainClass lookup(final Class clazz) {
		return (DomainClass)domainClassesByClazz.get(clazz);
	}

	/**
	 * Indicates that all classes have been registered.  The registry
	 * uses this to determine any links.
	 */
	public void done() {
//		for(DomainClass dc: domainClassesByClazz.values()) {
//			dc.identifyLinks();
//		}
		
	}
	

	/**
	 * for testing purposes
	 */
	public void clear() {
		domainClassesByClazz.clear();
	}
	
	public int size() {
		return domainClassesByClazz.keySet().size();
	}

	private final Map<Class, DomainClass> domainClassesByClazz = new HashMap<Class, DomainClass>();
	


}
