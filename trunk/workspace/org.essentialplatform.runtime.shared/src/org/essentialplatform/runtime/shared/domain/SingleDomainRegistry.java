package org.essentialplatform.runtime.shared.domain;

import java.util.Map;

import org.apache.log4j.Logger;
import org.essentialplatform.core.domain.Domain;
import org.essentialplatform.core.domain.IDomain;
import org.essentialplatform.runtime.shared.domain.adapters.IDomainRegistry;

/**
 * Simple implementation of {@link IDomainRegistry} that can only deal with
 * a single domain.
 * 
 * @author Dan Haywood
 *
 */
public final class SingleDomainRegistry implements IDomainRegistry {
	
	protected Logger getLogger() { return Logger.getLogger(SingleDomainRegistry.class); }
	
	
	
	private IDomainDefinition _domainDefinition;
	public IDomainDefinition getDomainDefinition() {
		return _domainDefinition;
	}
	/**
	 * For dependency injection.
	 * 
	 * @param domainDefinition
	 */
	public void setDomainDefinition(IDomainDefinition domainDefinition) {
		_domainDefinition = domainDefinition;
	}


	/*
	 * @see org.essentialplatform.runtime.shared.domain.adapters.IDomainRegistry#registerClassesInDomains()
	 */
	public void registerClassesInDomains() throws DomainRegistryException {
		if (_domainDefinition == null) {
			throw new IllegalStateException("Domain definition is required");
		}
		_domainDefinition.registerClasses();
	}


	/*
	 * @see org.essentialplatform.runtime.shared.domain.adapters.IDomainRegistry#getDomain(java.lang.String)
	 */
	public IDomain getDomain(final String domainName) {
		return Domain.instance(domainName);
	}

	/*
	 * @see org.essentialplatform.runtime.shared.domain.adapters.IDomainRegistry#getDomains()
	 */
	public Map<String, IDomain> getDomains() {
		return Domain.getDomains();
	}

}
