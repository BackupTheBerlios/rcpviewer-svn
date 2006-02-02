package org.essentialplatform.runtime.shared.domain.adapters;

import java.util.Map;

import org.essentialplatform.core.domain.IDomain;
import org.essentialplatform.runtime.shared.domain.DomainRegistryException;

public interface IDomainRegistry {
	
	void registerClassesInDomains() throws DomainRegistryException;
	
	/**
	 * Return a map of IDomains indexed by thier ids
	 */
	Map<String, IDomain>  getDomains();
	
	IDomain getDomain(String domainId);
}
