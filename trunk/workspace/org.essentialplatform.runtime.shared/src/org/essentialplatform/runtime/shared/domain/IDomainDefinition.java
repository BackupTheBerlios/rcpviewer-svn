package org.essentialplatform.runtime.shared.domain;

import org.essentialplatform.core.domain.builders.IDomainBuilder;
import org.osgi.framework.Bundle;

/**
 * Encapsulates the definition of an domain to Essential.
 */
public interface IDomainDefinition  {

	
	/**
	 * The name of this domain.
	 * 
	 * <p>
	 * All classes registered by this domain should indicate this domain.
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
	 * capable of building a domain out of the registered classes.
	 * 
	 * <p>
	 * Implementations are expected to provide a corresponding setter such that
	 * this value might be injected. 
	 *   
	 * @return
	 */
	IDomainBuilder getDomainBuilder();

	/**
	 * Register all (or enough) classes in the domain.
	 * 
	 * <p>
	 * Previously there was a design to make the registration pluggable,
	 * obtaining an <i>IDomainRegistrar</i> from the <tt>RuntimeBinding</tt>.
	 * However, this isn't needed - the implementation can be wired up
	 * directly (through Spring) to interact with an appropriate component.
	 * In the case of a server-side runtime using Hibernate, for example, this 
	 * is the <tt>HibernateServerSessionFactory</tt> (wrapping a Hibernate
	 * <tt>AnnotationConfiguration</tt>).
	 */
	void registerClasses() throws DomainBootstrapException;

}
