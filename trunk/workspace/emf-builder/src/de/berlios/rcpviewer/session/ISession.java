package de.berlios.rcpviewer.session;

import de.berlios.rcpviewer.metamodel.IDomainClass;
import de.berlios.rcpviewer.metamodel.IDomainObject;

public interface ISession {

	IWrapper getWrapper();
	
	IDomainObject<?> createTransient(IDomainClass<?> domainClass);
}
