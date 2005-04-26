package de.berlios.rcpviewer.session;

import de.berlios.rcpviewer.metamodel.IDomainObject;

public interface ISession {

	IDomainObject getDomainObjectFor(Object pojo);


}
