package org.essentialplatform.runtime.shared.domain;

import org.essentialplatform.core.domain.builders.IDomainBuilder;
import org.osgi.framework.Bundle;

/**
 * Encapsulates the definition of an domain to Essential.
 */
public interface IDomainDefinition extends IDomainClassRegistrar {

	
	/**
	 * Initialization performed by Essential platform itself (or 
	 * ILouisDefinition which delegates to same).
	 * 
	 * <p>
	 * Although in theory it might be possible to inject this information
	 * using Spring, we choose to do it manually in order to minimise the
	 * complexity of the Spring configuration files that must be written by
	 * the domain programmer. 
	 * 
	 * @param Bundle - bundle through whose classloader the classes may be loaded
	 * @param domainRegistrar - actually registers the classes in the domain.   
	 */
	void init(Bundle Bundle, IDomainRegistrar domainRegistrar);

	/**
	 * As provided by {@link #init(IDomainRegistrar)}, to delegate to to
	 * perform the actual registration.
	 * 
	 * @return
	 */
	IDomainRegistrar getDomainRegistrar();

	
	/**
	 * The name of this domain.
	 * 
	 * <p>
	 * All classes registered by this domain (by the {@link IDomainRegistrar} 
	 * returned by {@link #getDomainRegistrar()}) should indicate this domain.
	 * 
	 * <p>
	 * Implementations are expected to provide a corresponding setter such that
	 * this value might be injected. 
	 * 
	 * @return
	 */
	String getDomainName();

	/**
	 * The (runtime) implementation of a {@link IDomainBuilder} which is
	 * capable of building a domain out of the classes registed by the 
	 * {@link IDomainRegistrar} returned by {@link #getDomainRegistrar()}).
	 * 
	 * <p>
	 * Implementations are expected to provide a corresponding setter such that
	 * this value might be injected. 
	 *   
	 * @return
	 */
	IDomainBuilder getDomainBuilder();
	


}
