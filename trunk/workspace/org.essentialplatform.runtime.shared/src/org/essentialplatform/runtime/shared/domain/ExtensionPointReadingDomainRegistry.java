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

/**
 * THIS IMPLEMENTATION IS CURRENTLY NOT SUPPORTED NOR INTEGRATED WITH THE
 * REST OF THE BOOTSTRAP IMPLEMENTATION.
 *  
 * 
 * 
 * <p>
 * If it is set up to be the IDomainRegistry (as RuntimePlugin#setDomainRegistry)
 * then it will read an extension point to locate <tt>IDomainBootstrap</tt>s, and
 * run 'em.  However, the DomainBootstrap is only part of the picture - there is
 * other stuff needed to define a domain, see <tt>IDomainDefinition</tt>.
 * 
 * <p>
 * For now, this registry is not being used.  Instead we are using 
 * SingleDomainRegistry that is simply given (by LouisPlugin) an IDomainBootstrap
 * from the IDomainDefinition provided by the domain application plugin (eg acme).
 *
 * @author Dan Haywood
 * @deprecated
 * 
 */
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
				final String domainName = configurationElement.getAttribute("domain");
				IDomainBootstrap domainBootstrap = 
					(IDomainBootstrap)configurationElement.createExecutableExtension(CLASS_PROPERTY);
				domainBootstrap.registerClasses(domainName);
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
