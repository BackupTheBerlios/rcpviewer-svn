package de.berlios.rcpviewer.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;

import de.berlios.rcpviewer.progmodel.standard.InDomain;
import de.berlios.rcpviewer.progmodel.standard.DomainClass;
import de.berlios.rcpviewer.progmodel.standard.StandardProgModelExtension;

/**
 * Mostly complete implementation of {@link IDomain}.
 * 
 * <p>
 * Concrete implementations need to nominate their primary 
 * {@link IDomainAnalyzer} used to actually build the domain's meta-model.
 * For example, the runtime (RCP) implementation uses an analyzer that uses
 * Java reflection, whereas the compile-time (IDE) implementation uses an
 * analyzer that uses Eclipse's Java AST API.
 *
 * <p>
 * Since all implementations of {@link IDomain} are expected to provide a
 * mechanism to obtain (as a singleton) all instances of instantiated 
 * {@link IDomain}s keyed by their domain name, this class provides some of
 * this functionality.
 * 
 * @author Dan Haywood
 */
public abstract class AbstractDomain {

	protected static Map<String,Domain> domainsByName = 
		                                new HashMap<String,Domain>();

	/**
	 * Creates a new domain using specified {@link IDomainAnalyzer} as 
	 * the a primary extension.
	 * 
	 * @see #getPrimaryExtension()
	 */
	protected Domain(final String name, final IDomainAnalyzer primaryExtension){
		if (domainsByName.get(name) != null) {
			throw new IllegalArgumentException("Domain named '" + name + "' already exists.");
		}
		this.name = name;
		this.primaryExtension = primaryExtension; 
	}

	private final String name;
	public final String getName() {
		return name;
	}
	

	private final IDomainAnalyzer primaryExtension;
	/**
	 * The primary extension is responsible for traversing the graph of
	 * POJOs to build the extent of the meta model.
	 */
	public final IDomainAnalyzer getPrimaryExtension() {
		return primaryExtension;
	}

	private List<IDomainAnalyzer> extensions = new ArrayList<IDomainAnalyzer>();
	/**
	 * Call after registering all classes.
	 * 
	 * @param extension
	 */
	public final void addExtension(IDomainAnalyzer extension) {
		extensions.add(extension);
	}

	/**
	 * Returns a collection of {@link IDomainClass}es, each one parameterized
	 * by a different type.
	 * 
	 * @return
	 */
	public final Collection<IDomainClass<?>> classes() {
		return Collections.unmodifiableCollection((Collection<IDomainClass<?>>)domainClassesByjavaClass.values());
	}
	
	
	/**
	 * Looks up the {@link DomainClass} for the supplied {@link Class} from 
	 * this domain, creating it if not present, <i>provided</i> that the class 
	 * in question is annotated with @InDomain with the name of this domain.
	 * 
	 * <p>
	 * If already registered, simply returns, same way as 
	 * {@link #lookupNoRegister(Class)}.
	 * 
	 * <p>
	 * If there is no @InDomain annotation, then returns null.  Or, if there is
	 * an @InDomain annotation that indicates (either implicitly or explicitly)
	 * a domain name that is different from this metamodel's name, then again
	 * returns null. 
	 * 
	 * <p>
	 * To perform a lookup / register that will <i>always</i> return a
	 * {@link IDomainClass}, irrespective of the @InDomain annotation, then use
	 * {@link #lookupAny(Class)}. 
	 * 
	 * @param javaClass
	 * @return corresponding {@link DomainClass}
	 */
	public final <V> IDomainClass<V> lookup(final Class<V> javaClass) {
		InDomain domain = javaClass.getAnnotation(InDomain.class);
		if (domain == null) {
			return null;
		}
		if (!name.equals(domain.value())) {
			return null;
		}
		
		IDomainClass<V> domainClass = lookupNoRegister(javaClass);
		if (domainClass == null) {
			domainClass = new DomainClass<V>(this, javaClass);
			domainClassesByjavaClass.put(javaClass, domainClass);
			primaryExtension.analyze(domainClass);
		}
		return domainClass;
	}
	
	public final <V> IDomainClass<V> lookup(final DomainClass<V> domainClass) {
		Class<V> javaClass = domainClass.getJavaClass();
		IDomainClass<V> existingDomainClass = lookupNoRegister(javaClass);
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
	 * Looks up the {@link DomainClass} for the supplied {@link Class}.
	 * 
	 * <p>
	 * If the domain class has not yet been looked up (via either 
	 * {@link Domain#lookup(Class)} or {@link Domain#lookupAny(Class)}, then
	 * will return <code>null</code>.
	 *  
	 * @param javaClass
	 * @return corresponding {@link DomainClass}, or <tt>null</tt>
	 */
	public final <V> IDomainClass<V> lookupNoRegister(final Class<V> javaClass) {
		return (DomainClass<V>)domainClassesByjavaClass.get(javaClass);
	}

	/**
	 * Indicates that all classes have been registered / created, so that
	 * any additionally installed {@link IDomainAnalyzer}s can do their stuff.
	 * 
	 */
	public void done() {
		for(IDomainAnalyzer extension: extensions) {
			for(IDomainClass domainClass: new ArrayList<IDomainClass>(classes())) {
				extension.analyze(domainClass);
			}
		}
	}
	

	public void reset() {
		domainClassesByjavaClass.clear();
		extensions.clear();
	}
	
	public final int size() {
		return domainClassesByjavaClass.keySet().size();
	}

	private final Map<Class<?>, IDomainClass<?>> domainClassesByjavaClass = 
		new HashMap<Class<?>, IDomainClass<?>>();
	public final <V> IDomainClass<V> domainClassFor(EClass eClass) {
		Class<V> javaClass = (Class<V>)eClass.getInstanceClass();
		return lookupNoRegister(javaClass);
	}
}
