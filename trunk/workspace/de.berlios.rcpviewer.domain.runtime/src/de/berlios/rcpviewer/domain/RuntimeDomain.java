package de.berlios.rcpviewer.domain;

import java.io.Writer;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;

import de.berlios.rcpviewer.authorization.IAuthorizationManager;
import de.berlios.rcpviewer.domain.runtime.IRuntimeDomain;
import de.berlios.rcpviewer.progmodel.standard.InDomain;
import de.berlios.rcpviewer.progmodel.standard.OppositeOf;
import de.berlios.rcpviewer.progmodel.standard.OppositeReferencesIdentifier;
import de.berlios.rcpviewer.progmodel.standard.ProgModelConstants;
import de.berlios.rcpviewer.progmodel.standard.RuntimeDomainClass;
import de.berlios.rcpviewer.progmodel.standard.StandardProgModelDomainBuilder;

/**
 * Implementation of {@link IDomain} for the runtime (RCP) environment.
 * 
 * <p>
 * The runtime instantiation of {@link IDomain} holds {@link IDomainClass}es
 * that are built up using the Java reflection API. Together with the
 * annotations defined in the programming model plugins, the {@link IDomain}
 * instance (aka meta-model) can be built up.
 * 
 * <p>
 * This class also acts (courtesy of its superclass) as a singleton of all
 * instantiated {@link IDomain}s within the runtime environment, keyed by its
 * domain name.
 * 
 * @author Dan Haywood
 */
public final class RuntimeDomain extends AbstractDomain 
	implements IRuntimeDomain {

	/**
	 * Defaults to {@link IAuthorizationManager#NOOP} but can be overridden
	 * by dependency injection if required.
	 */
	private IAuthorizationManager _authorizationManager = IAuthorizationManager.NOOP;

	/**
	 * Returns the Domain instance with the given name (creating it if
	 * necessary).
	 * 
	 * @param domainName
	 * @return
	 */
	public synchronized static RuntimeDomain instance(String domainName) {
		RuntimeDomain domain = (RuntimeDomain) domainsByName.get(domainName);
		if (domain == null) {
			domain = new RuntimeDomain(domainName);
			domainsByName.put(domainName, domain);
		}
		return domain;
	}

	public static RuntimeDomain instance() {
		return RuntimeDomain.instance(ProgModelConstants.DEFAULT_DOMAIN_NAME);
	}

	/**
	 * Looks up the {@link IDomainClass} for the supplied Class, registering it
	 * if necessary.
	 * 
	 * <p>
	 * The {@link IDomainClass} will be registered in the {@link RuntimeDomain}
	 * specified by its {@link InDomain} annotation. If the
	 * {@link RuntimeDomain} does not yet exist, it too will be created first.
	 * (Hence the name of this method: lookup from any {@link RuntimeDomain}).
	 * 
	 * @param <V>
	 * @param javaClass
	 * @return
	 */
	public static <V> IRuntimeDomainClass<V> lookupAny(final Class<V> javaClass) {
		InDomain inDomain = javaClass.getAnnotation(InDomain.class);
		if (inDomain == null) {
			return null;
		}
		RuntimeDomain domain = instance(inDomain.value());
		return domain.lookup(javaClass);
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
	 * Resets all {@link RuntimeDomain}s that have been instantiated (using
	 * {@link RuntimeDomain#reset()}.
	 * 
	 * <p>
	 * for testing purposes
	 */
	public static void resetAll() {
		for (String domainName : domainsByName.keySet()) {
			RuntimeDomain.instance(domainName).reset();
		}
	}

	/**
	 * Creates a new domain using {@link StandardProgModelDomainBuilder} as the
	 * a primary builder.
	 * 
	 * @see #getPrimaryBuilder()
	 */
	public RuntimeDomain(final String name) {
		super(name, new StandardProgModelDomainBuilder());
	}

	/**
	 * Looks up the {@link RuntimeDomainClass} for the supplied {@link Class}
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
	 * @param javaClass
	 * @return corresponding {@link RuntimeDomainClass}
	 */
	public final <V> IRuntimeDomainClass<V> lookup(final Class<V> javaClass) {

		// make sure that the Domain matches
		InDomain domain = javaClass.getAnnotation(InDomain.class);
		if (domain == null) {
			return null;
		}
		if (!getName().equals(domain.value())) {
			return null;
		}

		// see if this class has already registered; return it if so.
		// TODO: covariance of lookupNoRegister...
		IRuntimeDomainClass<V> domainClass = (IRuntimeDomainClass<V>) lookupNoRegister(javaClass);
		if (domainClass != null) {
			//
			// HACK to pick up opposite references for case when
			// the side with @OppositeOf was registered second.
			//
			// the domain class will only actually do the work once, not on
			// every subsequent lookup.
			//
			new OppositeReferencesIdentifier((RuntimeDomainClass<V>)domainClass).identify();
			return domainClass;
		}

		// check class is in the EMF resource set for this domain ...

		// (but if not then create an EClass for it (and an EPackage too,
		// if necessary)
		Package javaPackage = javaClass.getPackage();
		EPackage ePackage = resourceSet.getPackageRegistry().getEPackage(
				javaPackage.getName());
		if (ePackage == null) {
			ePackage = EcoreFactory.eINSTANCE.createEPackage();
			ePackage.setName(javaPackage.getName());
			resourceSet.getPackageRegistry().put(ePackage.getName(), ePackage);
		}

		EClass eClass = null;
		EClassifier eClassifier = ePackage.getEClassifier(javaClass
				.getSimpleName());
		if (eClassifier == null) {
			eClass = EcoreFactory.eINSTANCE.createEClass();
			eClass.setName(javaClass.getSimpleName());
			((List<? super EClass>) ePackage.getEClassifiers()).add(eClass);
			eClass.setInstanceClass(javaClass);
		} else {
			if (!(eClassifier instanceof EClass)) {
				throw new RuntimeException("EDataType already exists called '"
						+ domainClass.getName() + "'");
			}
			eClass = (EClass)eClassifier;
		}

		domainClass = new RuntimeDomainClass<V>(this, eClass, javaClass);

		// ... but store the domain class itself in a local hash
		domainClassesByJavaClass.put(javaClass, domainClass);

		getPrimaryBuilder().build(domainClass);

		return domainClass;
	}

	/*
	 * TODO: not yet implemented
	 * 
	 * <p>
	 * Would an ECore2XMLFactoryImpl be able to help us?
	 * 
	 * @see de.berlios.rcpviewer.domain.IDomain#serializeTo(java.io.Writer)
	 */
	public void serializeTo(Writer writer) {
		throw new RuntimeException("not yet implemented");
	}

	/**
	 * Authorization manager to enforce constraints.
	 * 
	 * <p> 
	 * Defaults to {@link IAuthorizationManager#NOOP} but can be overridden
	 * using dependency injection, see {@link #setAuthorizationManager(IAuthorizationManager)}.
	 * 
	 * @see de.berlios.rcpviewer.domain.runtime.IRuntimeDomain#getAuthorizationManager()
	 */
	public IAuthorizationManager getAuthorizationManager() {
		return _authorizationManager;
	}
	/*
	 * @see de.berlios.rcpviewer.domain.runtime.IRuntimeDomain#setAuthorizationManager(de.berlios.rcpviewer.authorization.IAuthorizationManager)
	 */
	public void setAuthorizationManager(IAuthorizationManager authorizationManager) {
		if (authorizationManager == null) {
			throw new IllegalArgumentException("IAuthorizationManager may not be null - consider IAuthorizationManager.NOOP instead");
		}
		_authorizationManager = authorizationManager;
	}

}
