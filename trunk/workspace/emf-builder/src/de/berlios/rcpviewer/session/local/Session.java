package de.berlios.rcpviewer.session.local;

import de.berlios.rcpviewer.metamodel.IDomainObject;
import de.berlios.rcpviewer.persistence.IObjectStore;
import de.berlios.rcpviewer.persistence.inmemory.InMemoryObjectStore;
import de.berlios.rcpviewer.progmodel.standard.impl.DomainAspect;
import de.berlios.rcpviewer.session.ISession;

public class Session implements ISession {

	// TODO: make into an aspect
	private static ThreadLocal<Session> sessionForThisThread = 
			new ThreadLocal<Session>() {
		protected synchronized Session initialValue() {
			return new Session();
		}
	};
	public static ISession instance() {
		return sessionForThisThread.get();
	}


	public IDomainObject<?> getDomainObjectFor(Object pojo) {
		DomainAspect domainAspect = DomainAspect.aspectOf(pojo);
		if (domainAspect == null) {
			return null;
		}
		return domainAspect.getDomainObject();
	}



}
