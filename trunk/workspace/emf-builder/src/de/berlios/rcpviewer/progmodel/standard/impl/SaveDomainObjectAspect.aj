package de.berlios.rcpviewer.progmodel.standard.impl;

import de.berlios.rcpviewer.session.*;
import de.berlios.rcpviewer.session.local.*;
import de.berlios.rcpviewer.metamodel.IDomainObject;


public aspect SaveDomainObjectAspect /* implements ISessionAware */ {
	pointcut saveDomainObject(DomainMarker domainMarker):
		execution(public void *..DomainMarker+.save()) && this(domainMarker);
	
	before(DomainMarker domainMarker): saveDomainObject(domainMarker) {
		IDomainObject domainObject = 
			getSession().getWrapper().wrapped(domainMarker);
		domainObject.persist();
	}

	// DEPENDENCY INJECTION START
	
	private ISession session;
	public ISession getSession() {
		return Session.instance();
	}
	public void setSession(ISession session) {
		this.session = session;
	}

	// DEPENDENCY INJECTION END

}