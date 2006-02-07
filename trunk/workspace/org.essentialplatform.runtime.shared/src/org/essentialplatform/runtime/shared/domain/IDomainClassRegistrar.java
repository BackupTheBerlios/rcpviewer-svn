package org.essentialplatform.runtime.shared.domain;

import java.util.List;

import org.essentialplatform.core.deployment.Binding;
import org.essentialplatform.core.domain.builders.IDomainBuilder;
import org.osgi.framework.Bundle;

/**
 * Registers classes within a domain.
 */
public interface IDomainClassRegistrar {
	
	/**
	 * Register all (or enough) classes in the domain.
	 * 
	 * <p>
	 * Delegates to the configured {@link #getDomainRegistrar()} to actually
	 * do the registration, relative to {@link Binding}.
	 */
	void registerClasses() throws DomainBootstrapException;
}
