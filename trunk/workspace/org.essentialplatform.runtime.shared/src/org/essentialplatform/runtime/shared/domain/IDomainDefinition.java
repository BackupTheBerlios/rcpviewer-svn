package org.essentialplatform.runtime.shared.domain;

import org.essentialplatform.core.domain.builders.IDomainBuilder;
import org.osgi.framework.Bundle;

/**
 * Encapsulates the definition of an domain to Essential.
 */
public interface IDomainDefinition extends IDomainClassRegistrar {
	
	/**
	 * The name of this domain.
	 * 
	 * <p>
	 * All classes registered by this domain (by the {@link IDomainRegistrar} 
	 * returned by {@link #getDomainRegistrar()}) should indicate this domain.
	 * 
	 * @return
	 */
	String getDomainName();

	/**
	 * Configures the {@link IDomainRegistrar} to use (client- or server-side)
	 * to actually register the classes in the domain.
	 * 
	 * @param clientSideDomainRegistrar
	 */
	void setDomainRegistrar(IDomainRegistrar clientSideDomainRegistrar);

	/**
	 * Provided by Essential platform itself, is the {@link Bundle} that 
	 * represents the domain plugin.
	 * 
	 * @param domainBundle
	 */
	void setBundle(Bundle domainBundle);

	/**
	 * The (runtime) implementation of a {@link IDomainBuilder} which is
	 * capable of building a domain out of the classes registed by the 
	 * {@link IDomainRegistrar} returned by {@link #getDomainRegistrar()}).
	 *   
	 * @return
	 */
	IDomainBuilder getDomainBuilder();
	
	/**
	 * Bootstraps the domain.
	 * 
	 * @return
	 */
	IDomainRegistrar getDomainRegistrar();




}
