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
import de.berlios.rcpviewer.domain.runtime.RuntimeDeployment.RuntimeClassBinding;
import de.berlios.rcpviewer.persistence.IObjectStore;
import de.berlios.rcpviewer.persistence.IObjectStoreAware;
import de.berlios.rcpviewer.progmodel.standard.DomainObject;
import de.berlios.rcpviewer.session.IDomainObject;
import de.berlios.rcpviewer.session.IObservedFeature;
import de.berlios.rcpviewer.session.IPojo;
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
	private Map<IDomainClass, List<IDomainObject<?>>> 
		domainObjectsByDomainClass = new HashMap<IDomainClass, List<IDomainObject<?>>>();

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

	// POJO LIFECYCLE (CREATE/DELETE) //

	/*
	 * The TransactionInstantionChangeAspect picks up on this method, and adds
	 * to the transaction as necessary.
	 *
	 * @see de.berlios.rcpviewer.session.ISession#create(de.berlios.rcpviewer.domain.IDomainClass)
	 */
	public <T> IDomainObject<T> create(IDomainClass domainClass) {
		IDomainObject<T> domainObject = create(this, domainClass);
		attach(domainObject);
		if (domainClass.isTransientOnly()) {
			createdTransient((IPojo)domainObject.getPojo());
		} else {
			createdPersistent((IPojo)domainObject.getPojo());
		}
		return domainObject;
	}
	
	private <T> IDomainObject<T> create(final Session session, IDomainClass domainClass) {
		if (domainClass.isTransientOnly()) {
			return session.createTransient(session, domainClass);
		} else {
			return session.createPersistent(session, domainClass);
		}
	}

	/**
	 * @param session
	 * @return
	 */
	private <T> IDomainObject<T> createTransient(final Session session, IDomainClass runtimeDomainClass) {
		T pojo = ((RuntimeClassBinding<T>)runtimeDomainClass.getBinding()).newInstance();
		IDomainObject<T> domainObject = DomainObject.createTransient(runtimeDomainClass, pojo, session);
		return domainObject;
	}

	/**
	 * @param session
	 * @return
	 */
	private <T> IDomainObject<T> createPersistent(final Session session, IDomainClass runtimeDomainClass) {
		T pojo = ((RuntimeClassBinding<T>)runtimeDomainClass.getBinding()).newInstance();
		IDomainObject<T> domainObject = DomainObject.createPersistent(runtimeDomainClass, pojo, session);
		return domainObject;
	}


	
	/**
	 * Does nothing, but exists as a hook for aspects.
	 * 
	 * @param pojo
	 */
	private void createdTransient(final IPojo pojo) {
	}

	/**
	 * Does nothing, but exists as a hook for aspects.
	 * 
	 * <p>
	 * The TransactionalInstantiationChangeAspect picks up on this.
	 * 
	 * @param pojo
	 */
	private <T> void createdPersistent(final IPojo pojo) {
	}

	/*
	 * @see de.berlios.rcpviewer.session.ISession#recreate(de.berlios.rcpviewer.domain.IDomainClass)
	 */
	public <T> IDomainObject<T> recreate(IDomainClass domainClass) {
		IDomainObject<T> domainObject = recreatePersistent(this, domainClass);
		attach(domainObject);
		return domainObject;
	}

	private <T> IDomainObject<T> recreatePersistent(ISession session, IDomainClass runtimeDomainClass) {
		T pojo = ((RuntimeClassBinding<T>)runtimeDomainClass.getBinding()).newInstance();
		IDomainObject<T> domainObject = DomainObject.recreatePersistent(runtimeDomainClass, pojo, session);
		return domainObject;
	}


	/*
	 * Deletes the object.
	 * 
	 * <p>
	 * There isn't really anything for the session to do other than to ensure
	 * that the domain object is attached, and thus to detach.
	 * 
	 * @see de.berlios.rcpviewer.session.ISession#delete(de.berlios.rcpviewer.session.IDomainObject)
	 */
	public <T> void delete(IDomainObject<T> domainObject) {
		if(domainObject.getSession() != this) {
			throw new IllegalStateException("Domain object is not attached to this session.");
		}
		
		detach(domainObject);
	}


	/*
	 * Deletes the object.
	 * 
	 * <p>
	 * There isn't really anything for the session to do other than to ensure
	 * that the domain object is attached, and thus to detach.
	 * 
	 * @see de.berlios.rcpviewer.session.ISession#delete(java.lang.Object)
	 */
	public void delete(Object pojo) {
		delete(getDomainObjectFor(pojo, pojo.getClass()));
	}

	// ATTACH/DETACH //

	/*
	 * @see de.berlios.rcpviewer.session.ISession#attach(de.berlios.rcpviewer.session.IDomainObject)
	 */
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
							"Incorrect domain " +
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

	// DOMAIN OBJECT HASHES //

	/*
	 * @see de.berlios.rcpviewer.session.ISession#footprintFor(de.berlios.rcpviewer.domain.IDomainClass)
	 */
	public <T> List<IDomainObject<T>> footprintFor(IDomainClass domainClass) {
		return (List)Collections.unmodifiableList(getDomainObjectsFor(domainClass));
	}

	/*
	 * @see de.berlios.rcpviewer.session.ISession#reset()
	 */
	public void reset() {
		domainObjectsByDomainClass.clear();
	}


	/*
	 * 
	 * @see de.berlios.rcpviewer.session.ISession#hasDomainObjectFor(java.lang.Object)
	 */
	public boolean hasDomainObjectFor(Object pojo) {
		return _domainObjectByPojo.get(pojo) != null;
	}

	/*
	 * @see de.berlios.rcpviewer.session.ISession#getDomainObjectFor(java.lang.Object, java.lang.Class)
	 */
	public <T> IDomainObject<T> getDomainObjectFor(Object pojo, Class<T> pojoClass) {
		IDomainObject<T> domainObject = _domainObjectByPojo.get(pojo);
		if (domainObject == null) {
			throw new IllegalStateException("Not attached to session");
		}
		return domainObject;
	}


	private synchronized List<IDomainObject<?>> getDomainObjectsFor(IDomainClass domainClass) {
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

	
	// OBSERVED FEATURES //

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


	// SESSION LISTENERS //

	/*
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
	 * @see de.berlios.rcpviewer.session.ISession#removeSessionListener(de.berlios.rcpviewer.session.ISessionListener)
	 */
	public void removeSessionListener(ISessionListener listener) {
		synchronized(_listeners) {
			_listeners.remove(listener);
		}
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
