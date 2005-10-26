package de.berlios.rcpviewer;

import de.berlios.rcpviewer.domain.Domain;
import de.berlios.rcpviewer.domain.IDomainClass;

public class RuntimeDomainSpecifics implements IDeploymentSpecifics {

	public Domain getDomainInstance() {
		return Domain.instance();
	}
	public Domain getDomainInstance(final String domainName) {
		return Domain.instance(domainName);
	}
	public <T> IDomainClass lookupAny(Class<T> domainClassIdentifier) {
		return Domain.lookupAny(domainClassIdentifier);
	}
	public void resetAll() {
		Domain.resetAll();
	}


}
