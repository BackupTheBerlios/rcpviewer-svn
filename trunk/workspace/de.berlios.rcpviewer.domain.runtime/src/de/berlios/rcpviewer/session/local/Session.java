package de.berlios.rcpviewer.session.local;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.berlios.rcpviewer.domain.IDomain;
import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.domain.IRuntimeDomainClass;
import de.berlios.rcpviewer.persistence.IObjectStore;
import de.berlios.rcpviewer.persistence.IObjectStoreAware;
import de.berlios.rcpviewer.progmodel.standard.DomainObject;
import de.berlios.rcpviewer.session.IDomainObject;
import de.berlios.rcpviewer.session.ISession;
import de.berlios.rcpviewer.session.ISessionListener;
import de.berlios.rcpviewer.session.SessionObjectEvent;

public class Session implements ISession, IObjectStoreAware {
	
	Session(String id, IDomain domain, IObjectStore objectStore) {
		this.id = id;
		this.domain = domain;
		this.objectStore = objectStore;
	}

	private String id;
	public String getId() {
		return id;
	}

	private IDomain domain;
	public IDomain getDomain() {
		return domain;
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

	public <T> IDomainObject<T> createTransient(IRuntimeDomainClass<T> domainClass) {
		IDomainObject<T> domainObject = domainClass.createTransient();
		attach(domainObject);
		return domainObject;
	}


	public <T> void attach(IDomainObject<T> iDomainObject) {
		assert iDomainObject instanceof DomainObject; // make sure own implementation
		DomainObject domainObject = (DomainObject)iDomainObject;
		synchronized(domainObject) {
			// make sure session id is compatible, or if not set then is in
			// the same domain
			{
				String domainObjectSessionId = domainObject.getSessionId(); 
				if (domainObjectSessionId != null) {
					if (!this.getId().equals(domainObject.getSessionId())) {
					throw new IllegalArgumentException(
							"Incompatible session id " +
							"(this.id = '" + getId() + "', " +
							"domainObject.sessionId = '" + 
									domainObject.getSessionId() + "')");
					}
				} else {
					
					String domainObjectDomainName = 
						domainObject.getDomainClass().getDomain().getName();
					String sessionDomainName = this.getDomain().getName();
					if (!domainObjectDomainName.equals(sessionDomainName)) {
						throw new IllegalArgumentException(
							"Incorrect domain " +
							"this.domainName = '" + sessionDomainName + "', " +
							"domainObject.domainName = ' " + 
								domainObjectDomainName + ")");
					}
				}
			}
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
				domainObject.attached(this);
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

	
	public <T> void detach(IDomainObject<T> iDomainObject) {
		assert iDomainObject instanceof DomainObject; // make sure own implementation
		DomainObject<T> domainObject = (DomainObject)iDomainObject;
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
				domainObject.detached();
			}
		}
		// notify listeners
		SessionObjectEvent event = new SessionObjectEvent(this, domainObject);
		for(ISessionListener listener: listeners) {
			listener.domainObjectDetached(event);
		}
	}

	public <T> boolean isAttached(IDomainObject<T> domainObject) {
		return pojoByDomainObject.get(domainObject) != null;
	}

	public boolean isAttached(Object pojo) {
		return domainObjectByPojo.get(pojo) != null;
	}

	public <T> List<IDomainObject<T>> footprintFor(IDomainClass<T> domainClass) {
		return (List)Collections.unmodifiableList(getDomainObjectsFor(domainClass));
	}

	public <T> void persist(IDomainObject<T> domainObject) {
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
