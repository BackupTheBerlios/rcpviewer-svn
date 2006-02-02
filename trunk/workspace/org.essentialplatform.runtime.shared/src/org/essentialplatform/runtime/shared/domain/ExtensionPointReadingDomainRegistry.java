package org.essentialplatform.runtime.shared.domain;

import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;

import org.essentialplatform.core.domain.Domain;
import org.essentialplatform.core.domain.IDomain;
import org.essentialplatform.runtime.shared.domain.adapters.IDomainRegistry;

public final class ExtensionPointReadingDomainRegistry implements IDomainRegistry {
	
	protected Logger getLogger() { return Logger.getLogger(ExtensionPointReadingDomainRegistry.class); }
	
	private static final String CLASS_PROPERTY = "class"; //$NON-NLS-1$
	public static final String ORG_ESSENTIALPLATFORM_RUNTIME_SHARED_DOMAINS = "org.essentialplatform.runtime.shared.domains"; //$NON-NLS-1$

	public ExtensionPointReadingDomainRegistry() throws DomainRegistryException {
		registerClassesInDomains();
	}
	
	/*
	 * @see org.essentialplatform.runtime.shared.domain.adapters.IDomainRegistry#registerClassesInDomains()
	 */
	public void registerClassesInDomains() throws DomainRegistryException {
		IExtensionPoint extensionPoint=  
			Platform.getExtensionRegistry().getExtensionPoint(ORG_ESSENTIALPLATFORM_RUNTIME_SHARED_DOMAINS);
		for (IConfigurationElement configurationElement: extensionPoint.getConfigurationElements()) {
			try {
				// although the 'id' attribute is available to us, we don't need it.
				IDomainBootstrap domainBootstrap = 
					(IDomainBootstrap)configurationElement.createExecutableExtension(CLASS_PROPERTY);
				domainBootstrap.registerClasses();
			} catch (CoreException ex) {
				String msg = String.format(
						"IConfigurationElement#createExecutableExtension(\"%s\") failed for '%s'", CLASS_PROPERTY, configurationElement.getValue());   //$NON-NLS-1$
				getLogger().error(msg);
				throw new DomainRegistryException(msg, ex);
			}
		}
	}


	/*
	 * @see org.essentialplatform.runtime.shared.domain.adapters.IDomainRegistry#getDomain(java.lang.String)
	 */
	public IDomain getDomain(final String domainName) {
		return Domain.instance(domainName);
	}

	/*
	 * @see org.essentialplatform.runtime.shared.domain.adapters.IDomainRegistry#getDomains()
	 */
	public Map<String, IDomain> getDomains() {
		return Domain.getDomains();
	}

}
