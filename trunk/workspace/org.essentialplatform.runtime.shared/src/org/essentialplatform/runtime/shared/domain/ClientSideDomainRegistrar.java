package org.essentialplatform.runtime.shared.domain;

import org.apache.log4j.Logger;
import org.essentialplatform.core.domain.Domain;

/**
 * 
 * @author Dan
 */
public final class ClientSideDomainRegistrar extends AbstractDomainRegistrar {
	
	protected Logger getLogger() { return Logger.getLogger(ClientSideDomainRegistrar.class); }

	public void registerClass(Class<?> javaClass) {
		if (!(Domain.lookupAny(javaClass) != null)) {
			String msg= "Failed to register class in domain:"+javaClass.getName();
			throw new DomainBootstrapException(msg);
		}
	} 

	
}
