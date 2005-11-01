package org.essentialplatform.domain;

import java.util.Map;

public interface IDomainRegistry {
	
	/**
	 * Return a map of IDomains indexed by thier ids
	 */
	Map<String, IDomain>  getDomains();
	
	IDomain getDomain(String domainId);
}
