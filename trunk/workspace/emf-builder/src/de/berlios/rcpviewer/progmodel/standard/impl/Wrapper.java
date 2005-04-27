package de.berlios.rcpviewer.progmodel.standard.impl;

import de.berlios.rcpviewer.metamodel.IDomainObject;
import de.berlios.rcpviewer.session.IWrapper;

public final class Wrapper implements IWrapper {

	public IDomainObject<?> getDomainObjectFor(Object pojo) {
		DomainAspect domainAspect = DomainAspect.aspectOf(pojo);
		if (domainAspect == null) {
			return null;
		}
		return domainAspect.getDomainObject();
	}


}
