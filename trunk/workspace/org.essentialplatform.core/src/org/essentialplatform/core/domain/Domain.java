package org.essentialplatform.core.domain;

import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

import sun.reflect.generics.repository.ClassRepository;

import org.essentialplatform.core.deployment.Deployment;
import org.essentialplatform.core.deployment.Deployment.IDomainBinding;
import org.essentialplatform.core.domain.builders.IDomainBuilder;
import org.essentialplatform.progmodel.essential.app.InDomain;
import org.essentialplatform.progmodel.essential.app.ProgModelConstants;
import org.essentialplatform.progmodel.essential.core.domain.OppositeReferencesIdentifier;



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
public class Domain implements IDomain {

	protected static Map<String,IDomain> domainsByName = new HashMap<String,IDomain>();

	
	/**
	 * Returns the Domain instance with the given name (creating it if
	 * necessary).
	 * 
	 * @param domainName
	 * @return
	 */
	public synchronized static Domain instance(String domainName) {
		Domain domain = (Domain) domainsByName.get(domainName);
		if (domain == null) {
			Domain concreteDomain = new Domain(domainName);
			concreteDomain.setBinding(Deployment.getDeployment().bindingFor(concreteDomain));
			domain = concreteDomain;
			domainsByName.put(domainName, domain);
		}
		return domain;
	}

	public static Domain instance() {
		return Domain.instance(ProgModelConstants.DEFAULT_DOMAIN_NAME);
	}

	/**
	 * Looks up the {@link IDomainClass} for the supplied Class, registering it
	 * if necessary.
	 * 
	 * <p>
	 * The {@link IDomainClass} will be registered in the {@link Domain}
	 * specified by its {@link InDomain} annotation. If the
	 * {@link Domain} does not yet exist, it too will be created first.
	 * (Hence the name of this method: lookup from any {@link Domain}).
	 * 
	 * @param <V>
	 * @param classRepresentation - deployment-specific representation of the class.
	 * @return
	 */
	public static IDomainClass lookupAny(final Object classRepresentation) {
		InDomain inDomain = Deployment.getDeployment().getInDomainOf(classRepresentation);
		if (inDomain == null) {
			return null;
		}
		Domain domain = instance(inDomain.value());
		return domain.lookup(classRepresentation);
	}
	
	/**
	 * Returns an unmodifiable map of all Domains, keyed by name.
	 * 
	 * @return
	 */
	public static Map<String, IDomain> getDomains() {
		return Collections.unmodifiableMap(domainsByName);
	}

	/**
	 * Resets all {@link Domain}s that have been instantiated (using
	 * {@link Domain#reset()}.
	 * 
	 * <p>
	 * for testing purposes
	 */
	public static void resetAll() {
		for (String domainName : domainsByName.keySet()) {
			Domain.instance(domainName).reset();
		}
	}

	
	//////////////////////////////////////////////////////////
	
	private final IDomainBuilder _primaryBuilder;
	private final String _name;
	private IDomainBinding _binding;
	private List<IDomainBuilder> _builders = new ArrayList<IDomainBuilder>();
	protected final ResourceSet _resourceSet;

	/**
	 * Supports functionality to obtain the {@link IDomainClass} from a
	 * (deployment-specific) representation of the class.
	 * 
	 * <p>
	 * For runtime deployment, the representation will be a {@link java.lang.Class},
	 * whereas for compile-time it would be an Eclipse AST class.
	 * 
	 * <p>
	 * When we perform a lookup (see {@link IDomain#lookup(Object)}) we always
	 * check the {@link ResourceSet} held by the relevant {@link IDomain} for
	 * an {@link EClass} in the {@link EPackage} corresponding to the supplied
	 * class representation.  However, the {@link IDomainClass} cannot
	 * be serialized within EMF itself (at least, not unless we were to store
	 * the information to allow a new one to be instantiated - something we
	 * don't want to do because we want {@link IDomainClass} implementations
	 * to be reference equality rather than value equality semantics).  This 
	 * hash therefore allows the actual {@link IDomainClass} corresponding
	 * to a class representation to be obtained.
	 * 
	 * <p>
	 * Note: hashing on {@link EClass} was tried but doesn't seem to work.  
	 * That's why we use a deployment-specific representation.
	 */
	protected final Map<Object, IDomainClass> _domainClassesByClassRepresentation = new HashMap<Object, IDomainClass>();
	
	/**
	 * Creates a new domain using specified {@link IDomainBuilder}.
	 * 
	 */
	public Domain(final String name){
		if (domainsByName.get(name) != null) {
			throw new IllegalArgumentException("Domain named '" + name + "' already exists.");
		}
		this._name = name;
		this._primaryBuilder = Deployment.getDeployment().getPrimaryBuilder();
		_resourceSet = new ResourceSetImpl(); 
	}
	
	public final String getName() {
		return _name;
	}

	/**
	 * The primary builder is responsible for traversing the graph of
	 * POJOs to build the extent of the meta model.
	 */
	public final IDomainBuilder getPrimaryBuilder() {
		return _primaryBuilder;
	}

	/*
	 * @see org.essentialplatform.domain.IDomain#getBinding()
	 */
	public IDomainBinding getBinding() {
		return _binding;
	}
	public void setBinding(IDomainBinding binding) {
		_binding = binding;
	}


	/**
	 * Add additional {@link IDomainBuilder} to analyze the domain classes and
	 * extract semantics.
	 * 
	 * <p>
	 * All builders registered this way will be asked to do their analysis when
	 * the domain is notified that all classes have been registered in
	 * {@link Domain#done()}.
	 * 
	 * @param builder - if null, then is ignored.
	 */
	public final void addBuilder(IDomainBuilder builder) {
		if (builder == null) {
			return;
		}
		_builders.add(builder);
	}

	/**
	 * Returns a collection of {@link IDomainClass}es, each one parameterized
	 * by a different type.
	 * 
	 * @return
	 */
	public final Collection<IDomainClass> classes() {
		return Collections.unmodifiableCollection((Collection<IDomainClass>)_domainClassesByClassRepresentation.values());
	}
	


	/**
	 * Looks up the {@link DomainClass} for the supplied {@link Class}
	 * from this domain, creating it if not present, <i>provided</i> that the
	 * class in question is annotated with
	 * 
	 * @InDomain with the name of this domain.
	 * 
	 * <p>
	 * If already registered, simply returns, same way as
	 * {@link #lookupNoRegister(Class)}.
	 * 
	 * <p>
	 * If there is no
	 * @InDomain annotation, then returns null. Or, if there is an
	 * @InDomain annotation that indicates (either implicitly or explicitly) a
	 *           domain name that is different from this metamodel's name, then
	 *           again returns null.
	 * 
	 * <p>
	 * To perform a lookup / register that will <i>always</i> return a
	 * {@link IDomainClass}, irrespective of the
	 * @InDomain annotation, then use {@link #lookupAny(Class)}.
	 * 
	 * @param classRepresentation
	 * @return corresponding {@link DomainClass}
	 */
	public final IDomainClass lookup(final Object classRepresentation) {
		InDomain domain = Deployment.getDeployment().getInDomainOf(classRepresentation);
		if (domain == null) {
			return null;
		}
		if (!getName().equals(domain.value())) {
			return null;
		}

		// see if this class has already registered; return it if so.
		// TODO: covariance of lookupNoRegister...
		IDomainClass domainClass = (IDomainClass) lookupNoRegister(classRepresentation);
		if (domainClass != null) {
			//
			// HACK to pick up opposite references for case when
			// the side with @OppositeOf was registered second.
			//
			// the domain class will only actually do the work once, not on
			// every subsequent lookup.
			//
			new OppositeReferencesIdentifier((DomainClass)domainClass).identify();
			return domainClass;
		}

		// check class is in the EMF resource set for this domain ...
		// (but if not then create an EClass for it (and an EPackage too,
		// if necessary)
		String packageName = getBinding().getPackageNameFor(classRepresentation);
		EPackage ePackage = _resourceSet.getPackageRegistry().getEPackage(packageName);
		if (ePackage == null) {
			ePackage = EcoreFactory.eINSTANCE.createEPackage();
			ePackage.setName(packageName);
			_resourceSet.getPackageRegistry().put(packageName, ePackage);
		}
		
		EClass eClass = null;
		String classSimpleName = getBinding().getClassSimpleNameFor(classRepresentation);
		EClassifier eClassifier = ePackage.getEClassifier(classSimpleName);
		if (eClassifier == null) {
			eClass = EcoreFactory.eINSTANCE.createEClass();
			eClass.setName(classSimpleName);
			((List<? super EClass>) ePackage.getEClassifiers()).add(eClass);
			getBinding().processEClass(eClass, classRepresentation);
		} else {
			if (!(eClassifier instanceof EClass)) {
				throw new RuntimeException("EDataType already exists called '" + classSimpleName + "'");
			}
			eClass = (EClass)eClassifier;
		}

		DomainClass concreteDomainClass = new DomainClass(this, eClass);
		concreteDomainClass.setBinding(Deployment.getDeployment().bindingFor(concreteDomainClass, classRepresentation));
		domainClass = concreteDomainClass;

		// ... but store the domain class itself in a local hash
		_domainClassesByClassRepresentation.put(classRepresentation, domainClass);

		getPrimaryBuilder().build(domainClass);

		return domainClass;
	}

	/*
	 * @see org.essentialplatform.domain.IDomain#lookupNoRegister(java.lang.Object)
	 */
	public final IDomainClass lookupNoRegister(final Object classRepresentation) {
		if ( classRepresentation == null ) throw new IllegalArgumentException("Class representation is null");
		getBinding().assertValid(classRepresentation);
		 
		// check class is in the EMF resource set for this domain ...
		String packageName = getBinding().getPackageNameFor(classRepresentation);
		EPackage ePackage = _resourceSet.getPackageRegistry().getEPackage(packageName);
		if (ePackage == null) {
			return null;
		}
		String classSimpleName = getBinding().getClassSimpleNameFor(classRepresentation);
		EClassifier eClassifier = ePackage.getEClassifier(classSimpleName);
		if (eClassifier == null || 
		    !(eClassifier instanceof EClass)) {
			return null;
		}

		// ... but just return from our local hash
		return (IDomainClass)_domainClassesByClassRepresentation.get(classRepresentation);
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
		for(IDomainBuilder builder: _builders) {
			for(IDomainClass domainClass: new ArrayList<IDomainClass>(classes())) {
				builder.build(domainClass);
			}
		}
	}
	

	public void reset() {
		_domainClassesByClassRepresentation.clear();
		_builders.clear();
	}
	
	public final int size() {
		return _domainClassesByClassRepresentation.keySet().size();
	}

	public final IDomainClass domainClassFor(EClass eClass) {
		if (eClass == null) {
			throw new IllegalArgumentException("Supplied EClass is null");
		}
		Object classRepresentation = getBinding().classRepresentationFor(eClass);
		if (classRepresentation == null) {
			return null;
		}
		return lookupNoRegister(classRepresentation);
	}
	/**
	 * The EMF {@link ResourceSet} that will hold the {@link EClass}es that
	 * correspond to the {@link IDomainClass}es of this domain instance.
	 *  
	 * @return
	 */
	public ResourceSet getResourceSet() {
		return _resourceSet;
	}
	
	
	/*
	 * TODO: not yet implemented
	 * 
	 * <p>
	 * Would an ECore2XMLFactoryImpl be able to help us?
	 * 
	 * @see org.essentialplatform.domain.IDomain#serializeTo(java.io.Writer)
	 */
	public void serializeTo(Writer writer) {
		throw new RuntimeException("not yet implemented");
	}

}
