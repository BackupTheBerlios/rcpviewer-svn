package de.berlios.rcpviewer.domain;

import java.io.Writer;
import java.util.Collection;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.resource.ResourceSet;

/**
 * A registry of {@link IDomainClass}es within a given domain (or meta-model, 
 * or schema).
 *
 * <p>
 * This interface defines the (instance-level) responsibility of domains.  By
 * convention, every implementation also provides static (class-level) 
 * access to all instantiated Domains, keyed by the domain name.
 * 
 * @author Dan Haywood
 */
public interface IDomain {

	public ResourceSet getResourceSet();

	public String getName();
	

	/**
	 * The primary builder is responsible for traversing the graph of
	 * POJOs to build the extent of the domain's meta model.
	 * 
	 * <p>
	 * To get things started, normally one or two domain classes need to be
	 * registered implicitly by looking them up, see {@link #lookup(Class)}.
	 */
	public IDomainBuilder getPrimaryBuilder();

	/**
	 * Perform additional analysis of domain classes.
	 *
	 * <p>
	 * The analysis is not performed until {@link IDomain#done()} is
	 * called.
	 * 
	 * @param builder
	 */
	public void addBuilder(IDomainBuilder builder);

	/**
	 * Returns a collection of {@link IDomainClass}es, each one parameterized
	 * by a different type.
	 * 
	 * @return
	 */
	public Collection<IDomainClass<?>> classes();
	
	
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
	public <V> IDomainClass<V> lookup(final Class<V> javaClass);
	
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
	public <V> IDomainClass<V> lookupNoRegister(final Class<V> javaClass);

	/**
	 * Indicates that all classes have been registered / created, and that
	 * any additionally installed analyzers should do their stuff.
	 * 
	 */
	public void done();
	

	/**
	 * For testing purposes, clear out any {@link IDomainClass}es and
	 * installed {@link IDomainBuilder}s.
	 */
	public void reset();
	
	/**
	 * Useful for testing: the number of registered classes.
	 */
	public int size();

	/**
	 * Reverse lookup of {@link IDomainClass} from an EMF EClass.
	 */
	public <V> IDomainClass<V> domainClassFor(EClass eClass);


	/**
	 * Serializes the EMF {@link ResourceSet} to the specified writer.
	 * 
	 * @param out
	 */
	public void serializeTo(Writer writer);
	
}
