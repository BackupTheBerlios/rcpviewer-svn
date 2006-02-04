package org.essentialplatform.runtime.shared.domain;

import java.util.Map;

import org.apache.log4j.Logger;
import org.essentialplatform.core.domain.Domain;
import org.essentialplatform.core.domain.IDomain;
import org.essentialplatform.runtime.shared.domain.adapters.IDomainRegistry;

public final class SingleDomainRegistry implements IDomainRegistry {
	
	protected Logger getLogger() { return Logger.getLogger(SingleDomainRegistry.class); }
	
	public SingleDomainRegistry(IDomainBootstrap domainBootstrap, String domainName) {
		_domainBootstrap = domainBootstrap;
		_domainName = domainName;
	}

	private final IDomainBootstrap _domainBootstrap;
	public IDomainBootstrap getDomainBootstrap() {
		return _domainBootstrap;
	}

	private String _domainName;

	/*
	 * @see org.essentialplatform.runtime.shared.domain.adapters.IDomainRegistry#registerClassesInDomains()
	 */
	public void registerClassesInDomains() throws DomainRegistryException {
		_domainBootstrap.registerClasses(_domainName);
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
