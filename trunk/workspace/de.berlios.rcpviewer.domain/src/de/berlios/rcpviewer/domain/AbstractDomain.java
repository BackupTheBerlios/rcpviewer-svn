package de.berlios.rcpviewer.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;



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
public abstract class AbstractDomain implements IDomain {

	protected static Map<String,IDomain> domainsByName = 
		                                new HashMap<String,IDomain>();

	/**
	 * Creates a new domain using specified {@link IDomainAnalyzer} as 
	 * the a primary extension.
	 * 
	 * @see #getPrimaryExtension()
	 */
	protected AbstractDomain(final String name, final IDomainAnalyzer primaryExtension){
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
	 * Looks up the {@link DomainClass} for the supplied {@link Class}.
	 * 
	 * <p>
	 * If the domain class has not yet been looked up (via either 
	 * {@link Domain#lookup(Class)} or {@link Domain#lookupAny(Class)}, then
	 * will return <code>null</code>.
	 *  
	 * <P>
	 * TODO: covariance of return type required for runtime impl.
	 * 
	 * @param javaClass
	 * @return corresponding {@link DomainClass}, or <tt>null</tt>
	 */
	public final <V> IDomainClass<V> lookupNoRegister(final Class<V> javaClass) {
		return (IDomainClass<V>)domainClassesByjavaClass.get(javaClass);
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

	protected final Map<Class<?>, IDomainClass<?>> domainClassesByjavaClass = 
		new HashMap<Class<?>, IDomainClass<?>>();
	public final <V> IDomainClass<V> domainClassFor(EClass eClass) {
		Class<V> javaClass = (Class<V>)eClass.getInstanceClass();
		return lookupNoRegister(javaClass);
	}
}
