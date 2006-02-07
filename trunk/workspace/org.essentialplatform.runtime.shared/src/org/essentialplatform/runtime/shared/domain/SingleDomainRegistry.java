package org.essentialplatform.runtime.shared.domain;

import java.util.Map;

import org.apache.log4j.Logger;
import org.essentialplatform.core.domain.Domain;
import org.essentialplatform.core.domain.IDomain;
import org.essentialplatform.runtime.shared.domain.adapters.IDomainRegistry;

public final class SingleDomainRegistry implements IDomainRegistry {
	
	protected Logger getLogger() { return Logger.getLogger(SingleDomainRegistry.class); }
	
	public SingleDomainRegistry(IDomainClassRegistrar domainClassRegistrar) {
		_domainClassRegistrar = domainClassRegistrar;
	}

	private final IDomainClassRegistrar _domainClassRegistrar;
	public IDomainClassRegistrar getDomainClassRegistrar() {
		return _domainClassRegistrar;
	}


	/*
	 * @see org.essentialplatform.runtime.shared.domain.adapters.IDomainRegistry#registerClassesInDomains()
	 */
	public void registerClassesInDomains() throws DomainRegistryException {
		_domainClassRegistrar.registerClasses();
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
