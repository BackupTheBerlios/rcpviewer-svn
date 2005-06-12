package de.berlios.rcpviewer.domain;

import java.util.Map;

import de.berlios.rcpviewer.domain.IDomain;

public interface IDomainRegistry {
	
	/**
	 * Return a map of IDomains indexed by thier ids
	 */
	Map<String, IDomain>  getDomains();
	
	IDomain getDomain(String domainId);
}
