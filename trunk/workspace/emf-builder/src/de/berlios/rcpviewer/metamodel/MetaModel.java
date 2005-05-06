package de.berlios.rcpviewer.metamodel;

import de.berlios.rcpviewer.progmodel.standard.DomainClass;
import de.berlios.rcpviewer.progmodel.standard.impl.DomainAspect;
import de.berlios.rcpviewer.metamodel.Constants;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClass;

import sun.security.krb5.internal.crypto.d;

/**
 * Registery of all discovered {@link DomainClass}es.
 * 
 * <p>
 * An alternative way of getting hold of classes is via EMF, see
 * {@link EmfFacade}.  However, usually it is easier to deal in
 * {@link DomainClass}es rather than EMF <tt>EClass</tt>es.
 * 
 * @author Dan Haywood
 */
public final class MetaModel {
	
	// TODO: should convert this into an aspect.
	private static ThreadLocal<MetaModel> registryForThisThread = 
			new ThreadLocal<MetaModel>() {
		protected synchronized MetaModel initialValue() {
			return new MetaModel();
		}
	};
	public static MetaModel instance() {
		return registryForThisThread.get();
	}

	/**
	 * managed using {@link Container}
	 */
	public MetaModel() {
	}
	
	public Collection classes() {
		return Collections.unmodifiableCollection(domainClassesByjavaClass.values());
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
	public DomainClass<?> register(final Class<?> clazz) {
		// TODO: for some reason, not working even though AspectJ says that
		// the introduction will be applied???
//		if (!DomainObject.class.isAssignableFrom(clazz)) {
//			return null;
//		}
		DomainClass<?> domainClass = lookup(clazz);
		if (domainClass == null) {
			domainClass = new DomainClass(clazz);
			domainClassesByjavaClass.put(clazz, domainClass);
		}
		return domainClass;
	}
	
	/**
	 * looks up the {@link DomainClass} for the supplied {@link Class}. 
	 * @param clazz
	 * @return corresponding {@link DomainClass}, or <tt>null</tt>
	 */
	public DomainClass lookup(final Class javaClass) {
		return (DomainClass)domainClassesByjavaClass.get(javaClass);
	}

	/**
	 * Indicates that all classes have been registered.  The registry
	 * uses this to determine any links.
	 */
	public void done() {
//		for(DomainClass dc: domainClassesByjavaClass.values()) {
//			dc.identifyLinks();
//		}
		
	}
	

	/**
	 * for testing purposes
	 */
	public void clear() {
		domainClassesByjavaClass.clear();
	}
	
	public int size() {
		return domainClassesByjavaClass.keySet().size();
	}

	private final Map<Class, DomainClass> domainClassesByjavaClass = new HashMap<Class, DomainClass>();
	public IDomainClass domainClassFor(EClass eClass) {
		Class javaClass = eClass.getInstanceClass();
		return MetaModel.instance().lookup(javaClass);
	}
	


}
