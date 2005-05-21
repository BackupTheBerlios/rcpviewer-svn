package de.berlios.rcpviewer.progmodel.standard.impl;

import de.berlios.rcpviewer.session.IDomainObject;
import de.berlios.rcpviewer.session.IWrapper;

public final class Wrapper implements IWrapper {

	public <T> IDomainObject<T> wrapped(Object pojo, Class<T> pojoClass) {
		DomainAspect domainAspect = DomainAspect.aspectOf(pojo);
		if (domainAspect == null) {
			return null;
		}
		return (IDomainObject<T>)domainAspect.getDomainObject();
	}


}
