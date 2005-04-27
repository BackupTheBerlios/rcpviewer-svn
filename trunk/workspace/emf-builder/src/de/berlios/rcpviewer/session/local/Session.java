package de.berlios.rcpviewer.session.local;

import de.berlios.rcpviewer.metamodel.IDomainClass;
import de.berlios.rcpviewer.metamodel.IDomainObject;
import de.berlios.rcpviewer.persistence.IObjectStore;
import de.berlios.rcpviewer.persistence.inmemory.InMemoryObjectStore;
import de.berlios.rcpviewer.progmodel.standard.impl.DomainAspect;
import de.berlios.rcpviewer.session.ISession;
import de.berlios.rcpviewer.session.IWrapper;
import de.berlios.rcpviewer.session.IWrapperAware;

public class Session implements ISession, IWrapperAware {

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



	public IDomainObject<?> createTransient(IDomainClass<?> domainClass) {
		IDomainObject domainObject = domainClass.createTransient();
		return domainObject;
	}

	// DEPENDENCY INJECTION START //

	private IWrapper wrapper;
	public IWrapper getWrapper() {
		return wrapper;
	}
	public void setWrapper(IWrapper wrapper) {
		this.wrapper = wrapper;
	}

	// DEPENDENCY INJECTION START //


}
