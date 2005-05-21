package de.berlios.rcpviewer.session.local;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.persistence.IObjectStore;
import de.berlios.rcpviewer.persistence.IObjectStoreAware;
import de.berlios.rcpviewer.persistence.inmemory.InMemoryObjectStore;
import de.berlios.rcpviewer.progmodel.standard.impl.Department;
import de.berlios.rcpviewer.session.IDomainObject;
import de.berlios.rcpviewer.session.ISession;
import de.berlios.rcpviewer.session.ISessionListener;
import de.berlios.rcpviewer.session.SessionObjectEvent;

public class Session implements ISession, IObjectStoreAware {

	// TODO: wire back to an IDomain.
	private static ThreadLocal<Session> sessionForThisThread = 
			new ThreadLocal<Session>() {
		protected synchronized Session initialValue() {
			return new Session();
		}
	};
	public static ISession instance() {
		return sessionForThisThread.get();
	}
	
	public Session() {
		// TODO: Session should be created by Domain.
		// HACK: should be injected into Domain and propogated.
		this.objectStore = new InMemoryObjectStore();
	}

	/**
	 * Mapping of pojo to wrapping {@link IDomainObject}.
	 * 
	 * <p> 
	 * An alternative implementation would be a perthis association between a
	 * pojo and an IDomainObject (indeed this was the initial design).
	 * 
	 * @see #domainObjectByPojo
	 */
	private Map<IDomainObject, Object> pojoByDomainObject = 
		new HashMap<IDomainObject, Object>();
	
	/**
	 * Mapping of pojo to wrapping {@link IDomainObject}.
	 * 
	 * @see #pojoByDomainObject
	 */
	private Map<Object, IDomainObject> domainObjectByPojo = 
		new HashMap<Object, IDomainObject>();

	/**
	 * Partitioned hash of objects by their class.
	 */
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
		synchronized(domainObject) {
			// add to session hashes 
			{
				pojoByDomainObject.put(domainObject, domainObject.getPojo());
				domainObjectByPojo.put(domainObject.getPojo(), domainObject);
			}
			// add to partitioned hash of objects of this class
			{
				List<IDomainObject<?>> domainObjects = getDomainObjectsFor(domainObject);
				if (domainObjects.contains(domainObject)) {
					throw new IllegalArgumentException("pojo already attached to session.");
				}
				domainObjects.add(domainObject);
			}
			// tell domain object the session it is attached to
			{
				domainObject.setSession(this);
			}
		}
		// notify listeners
		{
			SessionObjectEvent event = new SessionObjectEvent(this, domainObject);
			for(ISessionListener listener: listeners) {
				listener.domainObjectAttached(event);
			}
		}
	}

	
	public void detach(IDomainObject<?> domainObject) {
		synchronized(domainObject) {
			// remove from partitioned hash of objects of this class
			{
				List<IDomainObject<?>> domainObjects = getDomainObjectsFor(domainObject);
				if (!domainObjects.contains(domainObject)) {
					throw new IllegalArgumentException("pojo not attached to session.");
				}
				domainObjects.remove(domainObject);
			}
			// remove from global hash
			{
				domainObjectByPojo.remove(domainObject.getPojo());
				pojoByDomainObject.remove(domainObject);
			}
			// tell domain object it is no longer attached to a session
			{
				domainObject.setSession(null);
			}
		}
		// notify listeners
		SessionObjectEvent event = new SessionObjectEvent(this, domainObject);
		for(ISessionListener listener: listeners) {
			listener.domainObjectDetached(event);
		}
	}

	public boolean isAttached(IDomainObject<?> domainObject) {
		return pojoByDomainObject.get(domainObject) != null;
	}

	public boolean isAttached(Object pojo) {
		return domainObjectByPojo.get(pojo) != null;
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
	public void persist(Object pojo) {
		IDomainObject<?> domainObject = getDomainObjectFor(pojo, pojo.getClass());
		persist(domainObject);
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
	
	public <T> IDomainObject<T> getDomainObjectFor(Object pojo, Class<T> pojoClass) {
		IDomainObject<T> domainObject = domainObjectByPojo.get(pojo);
		if (domainObject == null) {
			throw new IllegalStateException("Not attached to session");
		}
		return domainObject;
	}


	// DEPENDENCY INJECTION START //

	private IObjectStore objectStore;
	public IObjectStore getObjectStore() {
		return objectStore;
	}
	public void setObjectStore(IObjectStore objectStore) {
		this.objectStore = objectStore;
	}

	// DEPENDENCY INJECTION END //


}
