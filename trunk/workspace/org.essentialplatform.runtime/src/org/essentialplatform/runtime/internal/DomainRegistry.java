package org.essentialplatform.runtime.internal;

import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;

import org.essentialplatform.core.domain.Domain;
import org.essentialplatform.core.domain.IDomain;
import org.essentialplatform.runtime.domain.IDomainRegistry;
import org.essentialplatform.runtime.domain.runtime.IDomainBootstrap;

public class DomainRegistry
implements IDomainRegistry {
	public DomainRegistry() throws CoreException {
		registerClassesInDomains();
	}
	
	
	private void registerClassesInDomains() throws CoreException {
		IExtensionPoint extensionPoint=  
			Platform.getExtensionRegistry().getExtensionPoint("org.essentialplatform.domain.runtime.domains");
		for (IConfigurationElement configurationElement: extensionPoint.getConfigurationElements()) {
			// although the 'id' attribute is available to us, we don't need it.
			IDomainBootstrap domainBootstrap= (IDomainBootstrap)configurationElement.createExecutableExtension("class");
			domainBootstrap.registerClasses();
		}
	}


	public IDomain getDomain(final String domainName) {
		return Domain.instance(domainName);
	}

	public Map<String, IDomain> getDomains() {
		return Domain.getDomains();
	}

}
