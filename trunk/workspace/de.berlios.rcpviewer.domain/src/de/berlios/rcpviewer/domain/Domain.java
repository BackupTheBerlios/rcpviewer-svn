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
	 * Creates a new domain using {@link StandardProgModelExtension} as the
	 * a primary extension.
	 * 
	 * @see #getPrimaryExtension()
	 */
	private Domain(final String name) {
		super(name, new StandardProgModelExtension()); 
	}

}
