package org.essentialplatform.louis;

import org.essentialplatform.runtime.IDomainBootstrap;

/**
 * Creates <code>IDomainBootstrap</code> that will initialise the <code>Domain</code>.
 * @see org.essentialplatform.runtime.IDomainBootstrap
 * @see org.essentialplatform.domain.Domain;
 * @author Mike
 */
class DomainBootstrapFactory {
	
	/**
	 * Creates the bootstrap instance to use.
	 * @return
	 */
	static IDomainBootstrap createBootstrap() {
		return new DefaultDomainBootstrap();
	}
	
	/**
	 * Prevent instantiation
	 */
	private DomainBootstrapFactory(){
		super();
	}
	
}