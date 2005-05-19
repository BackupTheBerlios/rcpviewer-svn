package de.berlios.rcpviewer.session.local;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.persistence.IObjectStore;
import de.berlios.rcpviewer.persistence.IObjectStoreAware;
import de.berlios.rcpviewer.session.IDomainObject;
import de.berlios.rcpviewer.session.ISession;
import de.berlios.rcpviewer.session.ISessionListener;
import de.berlios.rcpviewer.session.IWrapper;
import de.berlios.rcpviewer.session.IWrapperAware;
import de.berlios.rcpviewer.session.SessionObjectEvent;

public class Session implements ISession, IWrapperAware, IObjectStoreAware {

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

	private Map<IDomainClass<?>, List<IDomainObject<?>>> 
		domainObjectsByDomainClass = new HashMap<IDomainClass<?>, List<IDomainObject<?>>>();

	private List<IDomainObject<?>> getDomainObjectsFor(IDomainClass<?> domainClass) {
		List<IDomainObject<?>> domainObjects = 
			domainObjectsByDomainClass.get(domainClass);
		if (domainObjects == null) {
			domainObjects = new ArrayList<IDomainObject<?>>();
			domainObjectsByDomainClass.put(domainClass, domainObjects);
		}
		return domainObjects;
	}
	private List<IDomainObject<?>> getDomainObjectsFor(IDomainObject<?> domainObject) {
		return getDomainObjectsFor(domainObject.getDomainClass());
	}

	public <T> IDomainObject<T> createTransient(IDomainClass<T> domainClass) {
		IDomainObject<T> domainObject = domainClass.createTransient();
		attach(domainObject);
		return domainObject;
	}


	public void attach(IDomainObject<?> domainObject) {
		List<IDomainObject<?>> domainObjects = getDomainObjectsFor(domainObject);
		if (domainObjects.contains(domainObject)) {
			throw new IllegalArgumentException("pojo already attached to session.");
		}
		domainObjects.add(domainObject);
		// notify listeners
		SessionObjectEvent event = new SessionObjectEvent(this, domainObject);
		for(ISessionListener listener: listeners) {
			listener.domainObjectAttached(event);
		}
	}
	public void attach(Object pojo) {
		IDomainObject<?> domainObject = getWrapper().wrapped(pojo, pojo.getClass());
		attach(domainObject);
	}

	
	public void detach(IDomainObject<?> domainObject) {
		List<IDomainObject<?>> domainObjects = getDomainObjectsFor(domainObject);
		if (!domainObjects.contains(domainObject)) {
			throw new IllegalArgumentException("pojo not attached to session.");
		}
		domainObjects.remove(domainObject);
		// notify listeners
		SessionObjectEvent event = new SessionObjectEvent(this, domainObject);
		for(ISessionListener listener: listeners) {
			listener.domainObjectDetached(event);
		}
	}
	public void detach(Object pojo) {
		IDomainObject<?> domainObject = getWrapper().wrapped(pojo, pojo.getClass());
		detach(domainObject);
	}

	public boolean isAttached(Object pojo) {
		IDomainObject<?> domainObject = getWrapper().wrapped(pojo, pojo.getClass());
		List<IDomainObject<?>> domainObjects = getDomainObjectsFor(domainObject);
		return domainObjects.contains(domainObject);
	}
	
	public boolean isAttached(IDomainObject<?> domainObject) {
		List<IDomainObject<?>> domainObjects = getDomainObjectsFor(domainObject);
		return domainObjects.contains(domainObject);
	}

	public List<IDomainObject<?>> footprintFor(IDomainClass<?> domainClass) {
		return Collections.unmodifiableList(getDomainObjectsFor(domainClass));
	}

	public void persist(IDomainObject<?> domainObject) {
		if (!isAttached(domainObject)) {
			throw new IllegalArgumentException("pojo not attached to session");
		}
		getObjectStore().persist(domainObject.title(), domainObject.getPojo());
	}

	public void reset() {
		domainObjectsByDomainClass.clear();
	}
	public static void resetCurrent() {
		((Session)instance()).reset();
	}

	private List<ISessionListener> listeners = new ArrayList<ISessionListener>();
	/**
	 * Returns listener only because it simplifies test implementation to do so.
	 */
	public <T extends ISessionListener> T addSessionListener(T listener) {
		synchronized(listeners) {
			if (!listeners.contains(listener)) {
				listeners.add(listener);
			}
		}
		return listener;
	}
	public void removeSessionListener(ISessionListener listener) {
		synchronized(listeners) {
			listeners.remove(listener);
		}
	}
	

	// DEPENDENCY INJECTION START //

	private IWrapper wrapper;
	public IWrapper getWrapper() {
		return wrapper;
	}
	public void setWrapper(IWrapper wrapper) {
		this.wrapper = wrapper;
	}
	
	private IObjectStore objectStore;
	public IObjectStore getObjectStore() {
		return objectStore;
	}
	public void setObjectStore(IObjectStore objectStore) {
		this.objectStore = objectStore;
	}
	
	// DEPENDENCY INJECTION END //


}
