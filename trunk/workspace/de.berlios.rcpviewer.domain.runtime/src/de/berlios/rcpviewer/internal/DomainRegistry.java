package de.berlios.rcpviewer.internal;

import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;

import de.berlios.rcpviewer.domain.Domain;
import de.berlios.rcpviewer.domain.IDomain;
import de.berlios.rcpviewer.domain.IDomainRegistry;
import de.berlios.rcpviewer.domain.runtime.IDomainBootstrap;

public class DomainRegistry
implements IDomainRegistry
{
	public DomainRegistry() throws CoreException
	{
		registerClassesInDomains();
	}
	
	
	private void registerClassesInDomains() throws CoreException
	{
		IExtensionPoint extensionPoint=  
			Platform.getExtensionRegistry().getExtensionPoint("de.berlios.rcpviewer.domain.runtime.domains");
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
