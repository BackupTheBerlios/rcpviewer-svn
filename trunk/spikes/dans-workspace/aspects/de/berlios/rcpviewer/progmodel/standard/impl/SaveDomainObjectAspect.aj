package de.berlios.rcpviewer.progmodel.standard.impl;

import de.berlios.rcpviewer.session.IDomainObject;
import de.berlios.rcpviewer.session.ISession;
import de.berlios.rcpviewer.session.local.Session;

public aspect SaveDomainObjectAspect /* implements ISessionAware */ {
	pointcut saveDomainObject(DomainMarker domainMarker):
		execution(public void *..DomainMarker+.save()) && this(domainMarker);
	
	before(DomainMarker domainMarker): saveDomainObject(domainMarker) {
		IDomainObject domainObject = 
			getSession().getWrapper().wrapped(domainMarker, domainMarker.getClass());
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
