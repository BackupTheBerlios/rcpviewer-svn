package org.essentialplatform.runtime.shared.domain;

import org.apache.log4j.Logger;
import org.osgi.framework.Bundle;


public abstract class AbstractDomainRegistrar implements IDomainRegistrar {

	protected abstract Logger getLogger(); 

	
	//////////////////////////////////////////////////////////////////////
	// Bundle
	//////////////////////////////////////////////////////////////////////

	private Bundle _bundle;
	/**
	 * Populated by owning DomainDefinition.
	 */
	public void setBundle(Bundle bundle) {
		_bundle = bundle;
	}
	public Bundle getBundle() {
		return _bundle;
	}
	

}
