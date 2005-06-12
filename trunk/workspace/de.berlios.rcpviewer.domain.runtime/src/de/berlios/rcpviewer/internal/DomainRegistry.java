package de.berlios.rcpviewer.internal;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;

import de.berlios.rcpviewer.domain.IDomain;
import de.berlios.rcpviewer.domain.IDomainRegistry;

public class DomainRegistry
implements IDomainRegistry
{
	private Map<String, IDomain> _domains= new HashMap<String, IDomain>();
	
	public DomainRegistry()
	throws CoreException
	{
		buildRegistry();
	}
	
	
	private void buildRegistry()
	throws CoreException
	{
		IExtensionPoint extensionPoint=  
			Platform.getExtensionRegistry().getExtensionPoint("de.berlios.rcpviewer.domain.runtime.domains");
		for (IConfigurationElement configurationElement: extensionPoint.getConfigurationElements()) {
			String id= configurationElement.getDeclaringExtension().getUniqueIdentifier();
			if (id == null)
				id= configurationElement.getDeclaringExtension().getNamespace();
			id+= configurationElement.getAttribute("id");
			IDomain domain= (IDomain)configurationElement.createExecutableExtension("class");
			_domains.put(id, domain);
		}
	}


	public IDomain getDomain(String pDomainId) {
		return _domains.get(pDomainId);
	}

	public Map<String, IDomain> getDomains() {
		return Collections.unmodifiableMap(_domains);
	}

}
