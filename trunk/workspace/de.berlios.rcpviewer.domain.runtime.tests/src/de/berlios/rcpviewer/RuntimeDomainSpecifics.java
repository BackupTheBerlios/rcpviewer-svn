package de.berlios.rcpviewer;

import de.berlios.rcpviewer.domain.RuntimeDomain;
import de.berlios.rcpviewer.domain.IDomainBuilder;
import de.berlios.rcpviewer.domain.IDomainClass;

public class RuntimeDomainSpecifics implements IDeploymentSpecifics {

	public RuntimeDomain getDomainInstance() {
		return RuntimeDomain.instance();
	}
	public RuntimeDomain getDomainInstance(final String domainName) {
		return RuntimeDomain.instance(domainName);
	}
	public <T> IDomainClass<T> lookupAny(Class<T> domainClassIdentifier) {
		return RuntimeDomain.lookupAny(domainClassIdentifier);
	}
	public void resetAll() {
		RuntimeDomain.resetAll();
	}


}
