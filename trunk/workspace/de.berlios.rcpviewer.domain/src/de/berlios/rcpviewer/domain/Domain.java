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
 * Registery of all discovered {@link DomainClass}es.
 * 
 * <p>
 * An alternative way of getting hold of classes is via EMF, see
 * {@link EmfFacade}.  However, usually it is easier to deal in
 * {@link DomainClass}es rather than EMF <tt>EClass</tt>es.
 * 
 * @author Dan Haywood
 */
public final class Domain {

	public final static String DEFAULT_DOMAIN_NAME = "default";
	private static Map<String,Domain> domainsByName = new HashMap<String,Domain>();
	public static Domain instance() {
		return instance(DEFAULT_DOMAIN_NAME);
	}
	/**
	 * Returns the Domain instance with the given name (creating it if
	 * necessary).
	 * 
	 * @param domainName
	 * @return
	 */
	public synchronized static Domain instance(String domainName) {
		Domain domain = domainsByName.get(domainName);
		if (domain == null) {
			domain = new Domain(domainName);
			domainsByName.put(domainName, domain);
		}
		return domain;
	}

	/**
	 * Looks up the {@link IDomainClass} for the supplied Class, registering it
	 * if necessary.
	 * 
	 * <p>
	 * The {@link IDomainClass} will be registered in the {@link Domain} 
	 * specified by its {@link InDomain} annotation.  If the {@link Domain} 
	 * does not yet exist, it too will be created first.  (Hence the name of
	 * this method: lookup from any {@link Domain}).
	 * 
	 * @param <V>
	 * @param javaClass
	 * @return
	 */
	public static <V> IDomainClass<V> lookupAny(final Class<V> javaClass) {
		InDomain inDomain = javaClass.getAnnotation(InDomain.class);
		if (inDomain == null) {
			return null;
		}
		Domain domain = instance(inDomain.value());
		return domain.lookup(javaClass);
	}

	
	/**
	 * Creates a new metamodel using {@link StandardProgModelExtension} as the
	 * a primary extension.
	 * 
	 * @see #getPrimaryExtension()
	 */
	private Domain(final String name) {
		this(name, new StandardProgModelExtension()); 
	}

	/**
	 * Creates a new metamodel using specified {@link IDomainAnalyzer} as 
	 * the a primary extension.
	 * 
	 * @see #getPrimaryExtension()
	 */
	private Domain(final String name, final IDomainAnalyzer primaryExtension) {
		if (domainsByName.get(name) != null) {
			throw new IllegalArgumentException("Domain named '" + name + "' already exists.");
		}
		this.name = name;
		this.primaryExtension = primaryExtension; 
	}

	private final String name;
	public String getName() {
		return name;
	}
	

	private final IDomainAnalyzer primaryExtension;
	/**
	 * The primary extension is responsible for traversing the graph of
	 * POJOs to build the extent of the meta model.
	 */
	public IDomainAnalyzer getPrimaryExtension() {
		return primaryExtension;
	}

	private List<IDomainAnalyzer> extensions = new ArrayList<IDomainAnalyzer>();
	/**
	 * Call after registering all classes.
	 * 
	 * @param extension
	 */
	public void addExtension(IDomainAnalyzer extension) {
		extensions.add(extension);
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
	public <V> IDomainClass<V> lookup(final Class<V> javaClass) {
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
	
	public <V> IDomainClass<V> lookup(final DomainClass<V> domainClass) {
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
	public <V> IDomainClass<V> lookupNoRegister(final Class<V> javaClass) {
		return (DomainClass<V>)domainClassesByjavaClass.get(javaClass);
	}

	/**
	 * Indicates that all classes have been registered / created.
	 * 
	 * <p>
	 * The metamodel uses this to determine any references.
	 */
	public void done() {
		for(IDomainAnalyzer extension: extensions) {
			for(IDomainClass domainClass: new ArrayList<IDomainClass>(classes())) {
				extension.analyze(domainClass);
			}
		}
	}
	

	/**
	 * Resets all {@link Domain}s that have been instantiated (using
	 * {@link Domain#reset()}.
	 * 
	 * <p>
	 * for testing purposes
	 */
	public static void resetAll() {
		for(String domainName: domainsByName.keySet()) {
			Domain.instance(domainName).reset();
		}
	}
	
	public void reset() {
		domainClassesByjavaClass.clear();
		extensions.clear();
	}
	
	public int size() {
		return domainClassesByjavaClass.keySet().size();
	}

	private final Map<Class<?>, IDomainClass<?>> domainClassesByjavaClass = 
		new HashMap<Class<?>, IDomainClass<?>>();
	public <V> IDomainClass<V> domainClassFor(EClass eClass) {
		Class<V> javaClass = (Class<V>)eClass.getInstanceClass();
		return lookupNoRegister(javaClass);
	}
	


}
