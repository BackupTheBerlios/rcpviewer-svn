package org.essentialplatform.louis;

import de.berlios.rcpviewer.domain.runtime.IDomainBootstrap;

/**
 * Creates <code>IDomainBootstrap</code> that will initialise the <code>Domain</code>.
 * @see de.berlios.rcpviewer.domain.runtime.IDomainBootstrap
 * @see de.berlios.rcpviewer.domain.Domain;
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