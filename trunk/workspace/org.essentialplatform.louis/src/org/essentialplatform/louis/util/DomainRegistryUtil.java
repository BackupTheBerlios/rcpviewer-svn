package org.essentialplatform.louis.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.essentialplatform.core.domain.IDomain;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.domain.filters.IFilter;
import org.essentialplatform.runtime.RuntimePlugin;
import org.essentialplatform.runtime.domain.adapters.IDomainRegistry;

/**
 * Static helper methods domain registry functions.
 * @author Mike
 */
public class DomainRegistryUtil {
	
	// prevent instantiation
	private DomainRegistryUtil() {
		super();
	}
	
	public static Collection<IDomainClass> allClasses(IFilter<IDomainClass> filter) {
		Collection<IDomainClass> allClasses = new ArrayList<IDomainClass>();
		for(IDomain domain: getDomains()) {
			allClasses.addAll(domain.classes(filter));
		}
		return allClasses;
	}

	/**
	 * Returns whether the passed class is a the pojo class of a domain object
	 * @param clazz
	 * @return
	 */
	public static boolean isDomainClass( Class clazz ) {
		if ( clazz == null ) throw new IllegalArgumentException();
		for ( IDomain domain: getDomains() ) {
			for ( IDomainClass dClass : domain.classes() ) {
				if ( dClass.getEClass().getInstanceClass().equals( clazz ) ) {
					return true;
				}
			}
    	}
		return false;
	}
	
	// seperate method for tidiness
	private static Collection<IDomain> getDomains() {
    	RuntimePlugin runtimePlugin= RuntimePlugin.getDefault();
    	IDomainRegistry domainRegistry= runtimePlugin.getDomainRegistry();
    	Map<String, IDomain> domains= domainRegistry.getDomains();
    	return domains.values();
	}
	

	

}
