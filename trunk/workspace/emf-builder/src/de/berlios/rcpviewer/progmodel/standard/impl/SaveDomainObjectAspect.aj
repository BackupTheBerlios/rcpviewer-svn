package de.berlios.rcpviewer.progmodel.standard.impl;

import de.berlios.rcpviewer.session.local.Session;
import de.berlios.rcpviewer.metamodel.IDomainObject;

public aspect SaveDomainObjectAspect {
	pointcut saveDomainObject(DomainMarker domainMarker):
		execution(public void *..DomainMarker+.save()) && this(domainMarker);
	
	before(DomainMarker domainMarker): saveDomainObject(domainMarker) {
		IDomainObject domainObject = 
			Session.instance().getWrapper().wrapped(domainMarker);
		domainObject.persist();
	}

}
