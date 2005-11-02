package org.essentialplatform;

import org.essentialplatform.domain.Domain;
import org.essentialplatform.domain.IDomainClass;

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
