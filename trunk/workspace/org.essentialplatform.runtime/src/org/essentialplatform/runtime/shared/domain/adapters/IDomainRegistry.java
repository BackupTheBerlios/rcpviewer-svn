package org.essentialplatform.runtime.shared.domain.adapters;

import java.util.Map;

import org.essentialplatform.core.domain.IDomain;

public interface IDomainRegistry {
	
	/**
	 * Return a map of IDomains indexed by thier ids
	 */
	Map<String, IDomain>  getDomains();
	
	IDomain getDomain(String domainId);
}
