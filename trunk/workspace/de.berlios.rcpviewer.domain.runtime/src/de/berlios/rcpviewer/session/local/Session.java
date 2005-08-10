package de.berlios.rcpviewer.session.local;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import de.berlios.rcpviewer.domain.IDomain;
import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.domain.IRuntimeDomainClass;
import de.berlios.rcpviewer.persistence.IObjectStore;
import de.berlios.rcpviewer.persistence.IObjectStoreAware;
import de.berlios.rcpviewer.progmodel.standard.DomainObject;
import de.berlios.rcpviewer.session.IDomainObject;
import de.berlios.rcpviewer.session.IObservedFeature;
import de.berlios.rcpviewer.session.ISession;
import de.berlios.rcpviewer.session.ISessionListener;
import de.berlios.rcpviewer.session.SessionObjectEvent;

public class Session implements ISession, IObjectStoreAware {
	
	private String _id;
	private IDomain _domain;
	/**
	 * Mapping of pojo by its wrapping {@link IDomainObject}.
	 * 
	 * <p> 
	 * An alternative implementation would be a perthis association between a
	 * pojo and an IDomainObject (indeed this was the initial design).
	 * 
	 * <p>
	 * Reciprocal of {@link #_domainObjectByPojo}
	 *  
	 * @see #_domainObjectByPojo
	 */
	private Map<IDomainObject, Object> _pojoByDomainObject = 
		new HashMap<IDomainObject, Object>();

	/**
	 * Mapping of {@link IDomainObject} by the pojo that it wraps.
	 * 
	 * <p>
	 * Reciprocal of {@link #_pojoByDomainObject} 
	 * 
	 * @see #_pojoByDomainObject
	 */
	private Map<Object, IDomainObject> _domainObjectByPojo = 
		new HashMap<Object, IDomainObject>();

	/**
	 * Partitioned hash of objects by their class.
	 */
	private Map<IDomainClass<?>, List<IDomainObject<?>>> 
		domainObjectsByDomainClass = new HashMap<IDomainClass<?>, List<IDomainObject<?>>>();

	private List<ISessionListener> _listeners = new ArrayList<ISessionListener>();
	
	/**
	 * All {@link IObservedFeature}s that are currently being referenced
	 * (elsewhere).
	 * 
	 * <p>
	 * Although a map is being used, the value is always null (if a WeakHashSet
	 * existed, we would be using it...).  
	 */
	private WeakHashMap<IObservedFeature, Object> _observedFeatures = new WeakHashMap<IObservedFeature, Object>();

	Session(String id, IDomain domain, IObjectStore objectStore) {
		this._id = id;
		this._domain = domain;
		this.objectStore = objectStore;
	}

	/*
	 * 
	 * @see de.berlios.rcpviewer.session.ISession#getId()
	 */
	public String getId() {
		return _id;
	}

	/*
	 * 
	 * @see de.berlios.rcpviewer.session.ISession#getDomain()
	 */
	public IDomain getDomain() {
		return _domain;
	}
	
	/*
	 * @see de.berlios.rcpviewer.session.ISession#createTransient(de.berlios.rcpviewer.domain.IRuntimeDomainClass)
	 */
	public <T> IDomainObject<T> createTransient(IRuntimeDomainClass<T> domainClass) {
		IDomainObject<T> domainObject = domainClass.createTransient();
		attach(domainObject);
		return domainObject;
	}

	/*
	 * 
	 * @see de.berlios.rcpviewer.session.ISession#attach(de.berlios.rcpviewer.session.IDomainObject)
	 */
	public <T> void attach(IDomainObject<T> iDomainObject) {
		assert iDomainObject instanceof DomainObject; // make sure own implementation
		DomainObject domainObject = (DomainObject)iDomainObject;
		synchronized(domainObject) {
			// make sure session _id is compatible, or if not set then is in
			// the same _domain
			{
				String domainObjectSessionId = domainObject.getSessionId(); 
				if (domainObjectSessionId != null) {
					if (!this.getId().equals(domainObject.getSessionId())) {
					throw new IllegalArgumentException(
							"Incompatible session _id " +
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
							"Incorrect _domain " +
							"this.domainName = '" + sessionDomainName + "', " +
							"domainObject.domainName = ' " + 
								domainObjectDomainName + ")");
					}
				}
			}
			// add to session hashes 
			{
				_pojoByDomainObject.put(domainObject, domainObject.getPojo());
				_domainObjectByPojo.put(domainObject.getPojo(), domainObject);
			}
			// add to partitioned hash of objects of this class
			{
				List<IDomainObject<?>> domainObjects = getDomainObjectsFor(domainObject);
				if (domainObjects.contains(domainObject)) {
					throw new IllegalArgumentException("pojo already attached to session.");
				}
				domainObjects.add(domainObject);
			}
			// tell _domain object the session it is attached to
			{
				domainObject.attached(this);
			}
		}
		// notify _listeners
		{
			SessionObjectEvent event = new SessionObjectEvent(this, domainObject);
			for(ISessionListener listener: _listeners) {
				listener.domainObjectAttached(event);
			}
		}
	}

	/*
	 * @see de.berlios.rcpviewer.session.ISession#detach(de.berlios.rcpviewer.session.IDomainObject)
	 */
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
				_domainObjectByPojo.remove(domainObject.getPojo());
				_pojoByDomainObject.remove(domainObject);
			}
			// tell _domain object it is no longer attached to a session
			{
				domainObject.detached();
			}
		}
		// notify _listeners
		SessionObjectEvent event = new SessionObjectEvent(this, domainObject);
		for(ISessionListener listener: _listeners) {
			listener.domainObjectDetached(event);
		}
	}

	public <T> boolean isAttached(IDomainObject<T> domainObject) {
		return _pojoByDomainObject.get(domainObject) != null;
	}

	/*
	 * 
	 * @see de.berlios.rcpviewer.session.ISession#isAttached(java.lang.Object)
	 */
	public boolean isAttached(Object pojo) {
		return _domainObjectByPojo.get(pojo) != null;
	}

	/*
	 * 
	 * @see de.berlios.rcpviewer.session.ISession#footprintFor(de.berlios.rcpviewer.domain.IDomainClass)
	 */
	public <T> List<IDomainObject<T>> footprintFor(IDomainClass<T> domainClass) {
		return (List)Collections.unmodifiableList(getDomainObjectsFor(domainClass));
	}

	/*
	 * 
	 * @see de.berlios.rcpviewer.session.ISession#persist(de.berlios.rcpviewer.session.IDomainObject)
	 */
	public <T> void persist(IDomainObject<T> domainObject) {
		if (!isAttached(domainObject)) {
			throw new IllegalArgumentException("pojo not attached to session");
		}
		getObjectStore().persist(domainObject);
	}

	/*
	 * 
	 * @see de.berlios.rcpviewer.session.ISession#persist(java.lang.Object)
	 */
	public void persist(Object pojo) {
		IDomainObject<?> domainObject = getDomainObjectFor(pojo, pojo.getClass());
		persist(domainObject);
	}
	
	/*
	 * 
	 * @see de.berlios.rcpviewer.session.ISession#save(de.berlios.rcpviewer.session.IDomainObject)
	 */
	public <T> void save(IDomainObject<T> domainObject) {
		if (!isAttached(domainObject)) {
			throw new IllegalArgumentException("pojo not attached to session");
		}
		getObjectStore().save(domainObject);
	}
	/*
	 * 
	 * @see de.berlios.rcpviewer.session.ISession#save(java.lang.Object)
	 */
	public void save(Object pojo) {
		IDomainObject<?> domainObject = getDomainObjectFor(pojo, pojo.getClass());
		save(domainObject);
	}

	/*
	 * 
	 * @see de.berlios.rcpviewer.session.ISession#reset()
	 */
	public void reset() {
		domainObjectsByDomainClass.clear();
	}


	/*
	 * 
	 * @see de.berlios.rcpviewer.session.ISession#addSessionListener(null)
	 */
	public <T extends ISessionListener> T addSessionListener(T listener) {
		synchronized(_listeners) {
			if (!_listeners.contains(listener)) {
				_listeners.add(listener);
			}
		}
		return listener;
	}
	/*
	 * 
	 * @see de.berlios.rcpviewer.session.ISession#removeSessionListener(de.berlios.rcpviewer.session.ISessionListener)
	 */
	public void removeSessionListener(ISessionListener listener) {
		synchronized(_listeners) {
			_listeners.remove(listener);
		}
	}

	/*
	 * 
	 * @see de.berlios.rcpviewer.session.ISession#hasDomainObjectFor(java.lang.Object)
	 */
	public boolean hasDomainObjectFor(Object pojo) {
		return _domainObjectByPojo.get(pojo) != null;
	}

	/*
	 * 
	 * @see de.berlios.rcpviewer.session.ISession#getDomainObjectFor(java.lang.Object, java.lang.Class)
	 */
	public <T> IDomainObject<T> getDomainObjectFor(Object pojo, Class<T> pojoClass) {
		IDomainObject<T> domainObject = _domainObjectByPojo.get(pojo);
		if (domainObject == null) {
			throw new IllegalStateException("Not attached to session");
		}
		return domainObject;
	}


	/*
	 * @see de.berlios.rcpviewer.session.ISession#getObservedFeatures()
	 */
	public Set<IObservedFeature> getObservedFeatures() {
		return _observedFeatures.keySet();
	}
	
	/*
	 * 
	 * @see de.berlios.rcpviewer.session.ISession#addObservedFeature(de.berlios.rcpviewer.session.IObservedFeature)
	 */
	public void addObservedFeature(IObservedFeature observedFeature) {
		_observedFeatures.put(observedFeature, null);
	}


	private synchronized List<IDomainObject<?>> getDomainObjectsFor(IDomainClass<?> domainClass) {
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
