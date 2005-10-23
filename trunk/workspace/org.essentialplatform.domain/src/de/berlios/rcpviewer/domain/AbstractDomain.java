package de.berlios.rcpviewer.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;



/**
 * Mostly complete implementation of {@link IDomain}.
 * 
 * <p>
 * Concrete implementations need to nominate their primary 
 * {@link IDomainBuilder} used to actually build the domain's meta-model.
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
	 * Creates a new domain using specified {@link IDomainBuilder} as 
	 * the a primary builder.
	 * 
	 * @see #getPrimaryBuilder()
	 */
	protected AbstractDomain(final String name, final IDomainBuilder primaryBuilder){
		if (domainsByName.get(name) != null) {
			throw new IllegalArgumentException("Domain named '" + name + "' already exists.");
		}
		this.name = name;
		this.primaryBuilder = primaryBuilder;
		resourceSet = new ResourceSetImpl(); 
	}
	
	private String name;
	public final String getName() {
		return name;
	}
	protected void setName(String name) {
		this.name= name;
	}
	

	private final IDomainBuilder primaryBuilder;
	/**
	 * The primary builder is responsible for traversing the graph of
	 * POJOs to build the extent of the meta model.
	 */
	public final IDomainBuilder getPrimaryBuilder() {
		return primaryBuilder;
	}

	private List<IDomainBuilder> builders = new ArrayList<IDomainBuilder>();
	/**
	 * Add additional {@link IDomainBuilder} to analyze the domain classes and
	 * extract semantics.
	 * 
	 * <p>
	 * All builders registered this way will be asked to do their analysis when
	 * the domain is notified that all classes have been registered in
	 * {@link AbstractDomain#done()}.
	 * 
	 * @param builder
	 */
	public final void addBuilder(IDomainBuilder builder) {
		builders.add(builder);
	}

	/**
	 * Returns a collection of {@link IDomainClass}es, each one parameterized
	 * by a different type.
	 * 
	 * @return
	 */
	public final Collection<IDomainClass<?>> classes() {
		return Collections.unmodifiableCollection((Collection<IDomainClass<?>>)domainClassesByJavaClass.values());
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
		
		if ( javaClass == null ) throw new IllegalArgumentException();
		if ( javaClass.isPrimitive() ) throw new IllegalArgumentException();
		
		// check class is in the EMF resource set for this domain ...
		Package javaPackage = javaClass.getPackage();
		EPackage ePackage = resourceSet.getPackageRegistry().getEPackage(javaPackage.getName());
		if (ePackage == null) {
			return null;
		}
		
		EClassifier eClassifier = ePackage.getEClassifier(javaClass.getSimpleName());
		if (eClassifier == null) {
			return null;
		}
		if  ( ! (eClassifier instanceof EClass) ) {
			return null;
		}
		
		// ... but just return from our local hash
		return (IDomainClass<V>)domainClassesByJavaClass.get(javaClass);
	}

	/**
	 * Indicates that all classes have been registered / created, so that
	 * any additionally installed {@link IDomainBuilder}s can do their stuff.
	 * 
	 * <p>
	 * Note that the primary builder (see {@link #getPrimaryBuilder()} is not
	 * included in this.
	 */
	public void done() {
		for(IDomainBuilder builder: builders) {
			for(IDomainClass domainClass: new ArrayList<IDomainClass>(classes())) {
				builder.build(domainClass);
			}
		}
	}
	

	public void reset() {
		domainClassesByJavaClass.clear();
		builders.clear();
	}
	
	public final int size() {
		return domainClassesByJavaClass.keySet().size();
	}

	/**
	 * Supports functionality to obtain the {@link IDomainClass} from a
	 * {@link EClass}.
	 * 
	 * <p>
	 * When we perform a lookup (see {@link IDomain#lookup(Class)}) we always
	 * check the {@link ResourceSet} held by the relevant {@link IDomain} for
	 * an {@link EClass} in the {@link EPackage} corresponding to the supplied
	 * {@link java.lang.Class}.  However, the {@link IDomainClass} cannot
	 * be serialized within EMF itself (at least, not unless we were to store
	 * the information to allow a new one to be instantiated - something we
	 * don't want to do because we want {@link IDomainClass} implementations
	 * to be reference equality rather than value equality semantics.
	 * 
	 * <p>
	 * This hash therefore allows the actual {@link IDomainClass} corresponding
	 * to an {@link EClass} to be obtained.
	 * 
	 * <p>
	 * Note: the eagle-eyed will have noticed that in fact this hash is keyed
	 * on {@link java.lang.Class} rather than {@link EClass}.  For whatever 
	 * reason, hashing on {@link EClass} doesn't seem to work.
	 * 
	 */
	protected final Map<Class<?>, IDomainClass<?>> domainClassesByJavaClass = 
		new HashMap<Class<?>, IDomainClass<?>>();
	
	
	protected final ResourceSet resourceSet;
	public final <V> IDomainClass<V> domainClassFor(EClass eClass) {
		Class<V> javaClass = (Class<V>)eClass.getInstanceClass();
		return lookupNoRegister(javaClass);
	}
	/**
	 * The EMF {@link ResourceSet} that will hold the {@link EClass}es that
	 * correspond to the {@link IDomainClass}es of this domain instance.
	 *  
	 * @return
	 */
	public ResourceSet getResourceSet() {
		return resourceSet;
	}
}
