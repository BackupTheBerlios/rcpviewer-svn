package de.berlios.rcpviewer.metamodel;

import java.awt.Container;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;

import de.berlios.rcpviewer.progmodel.standard.DomainClass;

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
	public static MetaModel threadInstance() {
		return registryForThisThread.get();
	}

	private List<IMetaModelExtension> extensions = new ArrayList<IMetaModelExtension>();
	/**
	 * Call after registering all classes.
	 * 
	 * @param extension
	 */
	public void addExtension(IMetaModelExtension extension) {
		extensions.add(extension);
		for(IDomainClass domainClass: classes()) {
			extension.analyze(domainClass);
		}
	}
	
	/**
	 * managed using {@link Container}
	 */
	public MetaModel() {
	}
	
	/**
	 * Returns a collection of {@link IDomainClass}es, each one parameterized
	 * by a different type.
	 * 
	 * @return
	 */
	public Collection<IDomainClass<?>> classes() {
		return Collections.unmodifiableCollection((Collection<IDomainClass<?>>)domainClassesByjavaClass.values());
	}
	
	/**
	 * creates a {@link DomainClass} for the supplied {@link Class} if not present,
	 * provided that the class in question implements DomainObject (else
	 * returns null *** this functionality commented out ***).
	 * 
	 * <p>
	 * If already registered, simply returns, same way as {@link #lookup(Class)}.
	 * 
	 * <p>
	 * TODO: should make sure class is indeed a domain class.
	 * 
	 * @param clazz
	 * @return corresponding {@link DomainClass}
	 */
	public <V> IDomainClass<V> register(final Class<V> clazz) {
		// TODO: for some reason, not working even though AspectJ says that
		// the introduction will be applied???
//		if (!DomainObject.class.isAssignableFrom(clazz)) {
//			return null;
//		}
		IDomainClass<V> domainClass = lookup(clazz);
		if (domainClass == null) {
			domainClass = new DomainClass<V>(this, clazz);
			domainClassesByjavaClass.put(clazz, domainClass);
		}
		
		return domainClass;
	}
	
	public <V> IDomainClass<V> register(final DomainClass<V> domainClass) {
		Class<V> javaClass = domainClass.getJavaClass();
		IDomainClass<V> existingDomainClass = lookup(javaClass);
		if (existingDomainClass != null) {
			if (domainClass != existingDomainClass) {
				throw new RuntimeException("Domain class already registered, '" + domainClass.getName() + "'");
			}
		} else {
			domainClassesByjavaClass.put(javaClass, domainClass);
		}
		return domainClass;
	}

	/**
	 * looks up the {@link DomainClass} for the supplied {@link Class}. 
	 * @param clazz
	 * @return corresponding {@link DomainClass}, or <tt>null</tt>
	 */
	public <V> IDomainClass<V> lookup(final Class<V> javaClass) {
		return (DomainClass<V>)domainClassesByjavaClass.get(javaClass);
	}

	/**
	 * Indicates that all classes have been registered / created.
	 * 
	 * <p>
	 * The metamodel uses this to determine any references.
	 */
	public void done() {
		for(IDomainClass<?> dc: domainClassesByjavaClass.values()) {
			dc.identifyReferences();
		}
		
	}
	

	/**
	 * for testing purposes
	 */
	public void clear() {
		domainClassesByjavaClass.clear();
		this.extensions.clear();
	}
	
	public int size() {
		return domainClassesByjavaClass.keySet().size();
	}

	private final Map<Class<?>, IDomainClass<?>> domainClassesByjavaClass = 
		new HashMap<Class<?>, IDomainClass<?>>();
	public <V> IDomainClass<V> domainClassFor(EClass eClass) {
		Class<V> javaClass = (Class<V>)eClass.getInstanceClass();
		return lookup(javaClass);
	}
	


}
