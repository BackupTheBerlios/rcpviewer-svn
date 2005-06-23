package de.berlios.rcpviewer.domain;

import java.util.Collections;
import java.util.Map;

import de.berlios.rcpviewer.progmodel.standard.DomainClass;
import de.berlios.rcpviewer.progmodel.standard.InDomain;
import de.berlios.rcpviewer.progmodel.standard.ProgModelConstants;
import de.berlios.rcpviewer.progmodel.standard.StandardProgModelDomainBuilder;

/**
 * Implementation of {@link IDomain} for the runtime (RCP) environment.
 * 
 * <p>
 * The runtime instantiation of {@link IDomain} holds {@link IDomainClass}es
 * that are built up using the Java reflection API.  Together with the
 * annotations defined in the programming model plugins, the {@link IDomain}
 * instance (aka meta-model) can be built up.
 *
 * <p>
 * This class also acts (courtesy of its superclass) as a singleton of all 
 * instantiated {@link IDomain}s within the runtime environment, keyed by 
 * its domain name.
 * 
 * @author Dan Haywood
 */
public final class Domain extends AbstractDomain {

	/**
	 * Returns the Domain instance with the given name (creating it if
	 * necessary).
	 * 
	 * @param domainName
	 * @return
	 */
	public synchronized static Domain instance(String domainName) {
		Domain domain = (Domain)domainsByName.get(domainName);
		if (domain == null) {
			domain = new Domain(domainName);
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
	 * specified by its {@link InDomain} annotation.  If the {@link Domain} 
	 * does not yet exist, it too will be created first.  (Hence the name of
	 * this method: lookup from any {@link Domain}).
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
		Domain domain = instance(inDomain.value());
		return domain.lookup(javaClass);
	}
	
	/**
	 * Returns an unmodifiable map of all Domains, keyed by name.
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
		for(String domainName: domainsByName.keySet()) {
			Domain.instance(domainName).reset();
		}
	}
	
	/**
	 * Creates a new domain using {@link StandardProgModelDomainBuilder} as the
	 * a primary builder.
	 * 
	 * @see #getPrimaryBuilder()
	 */
	public Domain(final String name) {
		super(name, new StandardProgModelDomainBuilder()); 
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
	public final <V> IRuntimeDomainClass<V> lookup(final Class<V> javaClass) {
		InDomain domain = javaClass.getAnnotation(InDomain.class);
		if (domain == null) {
			return null;
		}
		if (!getName().equals(domain.value())) {
			return null;
		}
		
		// HACK
		IRuntimeDomainClass<V> domainClass = (IRuntimeDomainClass<V>)lookupNoRegister(javaClass);
		if (domainClass == null) {
			domainClass = new DomainClass<V>(this, javaClass);
			domainClassesByjavaClass.put(javaClass, domainClass);
			getPrimaryBuilder().build(domainClass);
		}
		return domainClass;
	}

	public final <V> IDomainClass<V> lookup(final IDomainClass<V> domainClass) {
		// HACK
		Class<V> javaClass = ((IRuntimeDomainClass<V>)domainClass).getJavaClass();
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



}
