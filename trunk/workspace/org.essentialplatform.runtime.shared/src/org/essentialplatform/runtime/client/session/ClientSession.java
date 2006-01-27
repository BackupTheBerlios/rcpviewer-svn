package org.essentialplatform.runtime.client.session;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import org.essentialplatform.core.domain.Domain;
import org.essentialplatform.core.domain.IDomain;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.runtime.client.domain.bindings.IDomainObjectClientBinding;
import org.essentialplatform.runtime.client.session.event.ISessionListener;
import org.essentialplatform.runtime.client.session.event.SessionObjectEvent;
import org.essentialplatform.runtime.shared.domain.DomainObject;
import org.essentialplatform.runtime.shared.domain.Handle;
import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.domain.IObservedFeature;
import org.essentialplatform.runtime.shared.domain.IPojo;
import org.essentialplatform.runtime.shared.domain.bindings.IDomainClassRuntimeBinding;
import org.essentialplatform.runtime.shared.domain.handle.DefaultDomainObjectFactory;
import org.essentialplatform.runtime.shared.domain.handle.GuidHandleAssigner;
import org.essentialplatform.runtime.shared.domain.handle.HandleMap;
import org.essentialplatform.runtime.shared.domain.handle.IDomainObjectFactory;
import org.essentialplatform.runtime.shared.domain.handle.IHandleAssigner;
import org.essentialplatform.runtime.shared.domain.handle.IHandleMap;
import org.essentialplatform.runtime.shared.persistence.IPersistable.PersistState;
import org.essentialplatform.runtime.shared.persistence.IResolvable.ResolveState;
import org.essentialplatform.runtime.shared.session.SessionBinding;

/**
 * 
 * Note that instances of this object are never serialized (specifically, 
 * references from {@link IDomainObject} are marked as transient); therefore 
 * whether the fields of this implementation are transient or not is moot.
 * 
 * @see IClientSession
 * @author Dan Haywood
 */
public final class ClientSession implements IClientSession {


	ClientSession(SessionBinding sessionBinding) {
		this._domain = Domain.instance(sessionBinding.getDomainName());
		this._sessionBinding = sessionBinding;
		this._handleMap = new HandleMap(sessionBinding);
	}


	////////////////////////////////////////////////////////////////
	// Domain, SessionBinding
	////////////////////////////////////////////////////////////////


	/**
	 * Transient because not required for serialization; instead the binding
	 * stores the name of the domain. 
	 */
	private transient IDomain _domain;
	/*
	 * @see org.essentialplatform.session.ISession#getDomain()
	 */
	public IDomain getDomain() {
		return _domain;
	}


	private final SessionBinding _sessionBinding;
	/*
	 * @see org.essentialplatform.runtime.client.session.IClientSession#getSessionBinding()
	 */
	public SessionBinding getSessionBinding() {
		return _sessionBinding;
	}
	/*
	 * @see org.essentialplatform.runtime.shared.session.IObjectStoreHandle#getObjectStoreId()
	 */
	public String getObjectStoreId() {
		return _sessionBinding.getObjectStoreId();
	}


	
	////////////////////////////////////////////////////////////////
	// POJO LIFECYCLE (CREATE/DELETE)
	// + hooks
	////////////////////////////////////////////////////////////////

	/*
	 * The TransactionInstantionChangeAspect picks up on this method (or rather, the
	 * hook createdXxx methods), and adds to the transaction as necessary.
	 *
	 * @see org.essentialplatform.session.ISession#create(org.essentialplatform.domain.IDomainClass)
	 */
	public <T> IDomainObject<T> create(IDomainClass domainClass) {
		IDomainObject<T> domainObject = 
			(IDomainObject<T>)
				(domainClass.isTransientOnly()? 
					createTransient(domainClass):
					createPersistent(domainClass));
		attach(domainObject);
		return domainObject;
	}
	
	/**
	 * @return
	 */
	private <T> IDomainObject<T> createTransient(IDomainClass domainClass) {
		IDomainObjectFactory domainObjectFactory = 
			new DefaultDomainObjectFactory(_sessionBinding, PersistState.TRANSIENT, ResolveState.RESOLVED, getHandleAssigner());
		IDomainObject<T> domainObject = domainObjectFactory.createDomainObject(domainClass);
		IPojo pojo = (IPojo)domainObject.getPojo();
		attach(pojo); // move to attach(IDomainObject)
		createdTransient(pojo);
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
	 * @return
	 */
	private <T> IDomainObject<T> createPersistent(IDomainClass domainClass) {
		IDomainObjectFactory domainObjectFactory = 
			new DefaultDomainObjectFactory(_sessionBinding, PersistState.PERSISTED, ResolveState.RESOLVED, getHandleAssigner());
		IDomainObject<T> domainObject = domainObjectFactory.createDomainObject(domainClass);
		IPojo pojo = (IPojo)domainObject.getPojo();
		attach(pojo);  // move to attach(IDomainObject)
		createdPersistent(pojo);
		return domainObject;
	}
	/**
	 * Does nothing, but exists as a hook for aspects.
	 * 
	 * <p>
	 * The TransactionalInstantiationChangeAspect picks up on this.
	 * 
	 * @param pojo
	 */
	private void createdPersistent(final IPojo pojo) {
	}

	/*
	 * @see org.essentialplatform.session.ISession#recreate(org.essentialplatform.domain.IDomainClass)
	 */
	public <T> IDomainObject<T> recreate(IDomainClass domainClass) {
		IDomainObjectFactory domainObjectFactory = 
			new DefaultDomainObjectFactory(_sessionBinding, PersistState.PERSISTED, ResolveState.UNRESOLVED, getHandleAssigner());
		IDomainObject<T> domainObject = domainObjectFactory.createDomainObject(domainClass);
		IPojo pojo = (IPojo)domainObject.getPojo();
		attach(domainObject);
		attach(pojo); // move to attach(IDomainObject)
		recreatedPersistent(pojo);
		return domainObject;
	}
	/**
	 * Does nothing, but exists as a hook for aspects.
	 * 
	 * <p>
	 * The TransactionalInstantiationChangeAspect picks up on this.
	 * 
	 * @param pojo
	 */
	private void recreatedPersistent(final IPojo pojo) {
	}


	/*
	 * TODO: move into create or the attach(IDomainObject) method.
	 */
	private <T> IDomainObject<T> attach(T pojo) {
		IDomainObject<T> domainObject = (DomainObject<T>)((IPojo) pojo).domainObject();
		IDomainObjectClientBinding<T> binding = (IDomainObjectClientBinding<T>)domainObject.getBinding();
		binding.attached(this);
		return domainObject;
	}


	/*
	 * Deletes the object.
	 * 
	 * <p>
	 * There isn't really anything for the session to do other than to ensure
	 * that the domain object is attached, and thus to detach.
	 * 
	 * @see org.essentialplatform.session.ISession#delete(org.essentialplatform.session.IDomainObject)
	 */
	public <T> void delete(IDomainObject<T> domainObject) {
		final IDomainObjectClientBinding<T> objBinding = (IDomainObjectClientBinding<T>) domainObject.getBinding();
		if(objBinding.getSession() != this) {
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
	 * @see org.essentialplatform.session.ISession#delete(java.lang.Object)
	 */
	public void delete(Object pojo) {
		delete(getDomainObjectFor(pojo, pojo.getClass()));
	}

	
	////////////////////////////////////////////////////////////////
	// HandleAssigner 
	////////////////////////////////////////////////////////////////
	
	
	/**
	 * Defaults to the {@link GuidHandleAssigner}.
	 */
	private IHandleAssigner _handleAssigner = new GuidHandleAssigner();
	/*
	 * @see org.essentialplatform.runtime.client.session.IClientSession#getHandleAssigner()
	 */
	public IHandleAssigner getHandleAssigner() {
		return _handleAssigner;
	}
	/*
	 * As per the interface, if not called then will default to using
	 * the GuidHandleAssigner.
	 * 
	 * @see org.essentialplatform.runtime.client.session.IClientSession#setHandleAssigner(org.essentialplatform.runtime.shared.domain.handle.IHandleAssigner)
	 */
	public void setHandleAssigner(IHandleAssigner handleAssigner) {
		_handleAssigner = handleAssigner;
	}
	

	////////////////////////////////////////////////////////////////
	// ATTACH/DETACH 
	////////////////////////////////////////////////////////////////

	/*
	 * @see org.essentialplatform.session.ISession#attach(org.essentialplatform.session.IDomainObject)
	 */
	public <T> void attach(IDomainObject<T> iDomainObject) {
		DomainObject<T> domainObject = (DomainObject<T>)iDomainObject;
		synchronized(domainObject) {
			// if binding set, make sure is same.  If not set, then make
			// sure domain is same. 
			if (domainObject.getSessionBinding() != null) {
				// binding set
				if (!getSessionBinding().equals(domainObject.getSessionBinding())) {
					throw new IllegalArgumentException(
							"Incompatible session binding " +
							"(this.binding = '" + getSessionBinding() + "', " +
							"domainObject.binding = '" + 
									domainObject.getSessionBinding() + "')");
					}
			} else {
				// binding not set
				String domainObjectDomainName = 
					domainObject.getDomainClass().getDomain().getName();
				String sessionDomainName = getDomain().getName();
				if (!domainObjectDomainName.equals(sessionDomainName)) {
					throw new IllegalArgumentException(
						"Incorrect domain " +
						"this.domainName = '" + sessionDomainName + "', " +
						"domainObject.domainName = ' " + 
							domainObjectDomainName + ")");
				}
			}
			// make sure has a handle
			if (domainObject.getHandle() == null) {
				getHandleAssigner().assignHandleFor(domainObject);
			}
			
			// add to session hashes
			_handleMap.add(domainObject);

			// add to partitioned hash of objects of this class
			List<IDomainObject<?>> domainObjects = getDomainObjectsFor(domainObject);
			if (domainObjects.contains(domainObject)) {
				throw new IllegalArgumentException("pojo already attached to session.");
			}
			domainObjects.add(domainObject);

			// tell _domain object the session it is attached to
			IDomainObjectClientBinding<T> runtimeBinding = (IDomainObjectClientBinding<T>)domainObject.getBinding();
			runtimeBinding.attached(this);
		}
		// notify _listeners
		SessionObjectEvent<T> event = new SessionObjectEvent(this, domainObject);
		for(ISessionListener listener: _listeners) {
			listener.domainObjectAttached(event);
		}
	}

	
	
	/*
	 * @see org.essentialplatform.session.ISession#detach(org.essentialplatform.session.IDomainObject)
	 */
	public <T> void detach(IDomainObject<T> iDomainObject) {
		DomainObject<T> domainObject = (DomainObject<T>)iDomainObject;
		final IDomainObjectClientBinding<T> objBinding = (IDomainObjectClientBinding<T>) domainObject.getBinding();
		synchronized(domainObject) {
			// remove from partitioned hash of objects of this class
			List<IDomainObject<?>> domainObjects = getDomainObjectsFor(domainObject);
			if (!domainObjects.contains(domainObject)) {
				throw new IllegalArgumentException("pojo not attached to session.");
			}
			domainObjects.remove(domainObject);

			// remove from global hash
			_handleMap.remove(domainObject.getHandle());

			// tell _domain object it is no longer attached to a session
			objBinding.detached();
		}
		// notify _listeners
		SessionObjectEvent event = new SessionObjectEvent(this, domainObject);
		for(ISessionListener listener: _listeners) {
			listener.domainObjectDetached(event);
		}
	}

	public <T> boolean isAttached(IDomainObject<T> domainObject) {
		final Handle handle = domainObject.getHandle();
		return _handleMap.getDomainObject(handle) != null;
	}

	/*
	 * 
	 * @see org.essentialplatform.session.ISession#isAttached(java.lang.Object)
	 */
	public boolean isAttached(Object pojo) {
		return _domainObjectByPojo.get(pojo) != null;
	}

	
	////////////////////////////////////////////////////////////////
	// DOMAIN OBJECT HASHES
	// IHandleMap implementation
	////////////////////////////////////////////////////////////////

	private final IHandleMap _handleMap;
	
	
	/*
	 * @see org.essentialplatform.runtime.shared.domain.handle.IHandleMap#getDomainObject(org.essentialplatform.runtime.shared.domain.Handle)
	 */
	public IDomainObject getDomainObject(Handle handle) {
		return _handleMap.getDomainObject(handle);
	}
	/*
	 * @see org.essentialplatform.runtime.shared.domain.handle.IHandleMap#getHandle(org.essentialplatform.runtime.shared.domain.IDomainObject)
	 */
	public Handle getHandle(IDomainObject domainObject) {
		return _handleMap.getHandle(domainObject);
	}
	/*
	 * @see org.essentialplatform.runtime.shared.domain.handle.IHandleMap#add(org.essentialplatform.runtime.shared.domain.IDomainObject)
	 */
	public boolean add(IDomainObject domainObject) throws IllegalStateException {
		return _handleMap.add(domainObject);
	}
	/*
	 * @see org.essentialplatform.runtime.shared.domain.handle.IHandleMap#remove(org.essentialplatform.runtime.shared.domain.IDomainObject)
	 */
	public boolean remove(IDomainObject domainObject) throws IllegalStateException {
		return _handleMap.remove(domainObject);
	}
	/*
	 * @see org.essentialplatform.runtime.shared.domain.handle.IHandleMap#remove(org.essentialplatform.runtime.shared.domain.Handle)
	 */
	public boolean remove(Handle handle) throws IllegalStateException {
		return _handleMap.remove(handle);
	}

	/*
	 * @see org.essentialplatform.runtime.shared.domain.handle.IHandleMap#handles()
	 */
	public Set<Handle> handles() {
		return _handleMap.handles();
	}

	/**
	 * Mapping of {@link IDomainObject} by the pojo that it wraps.
	 * 
	 * <p>
	 * Reciprocal of {@link #_pojoByDomainObject} 
	 * 
	 * @see #_pojoByDomainObject
	 */
	private Map<Object, IDomainObject<?>> _domainObjectByPojo = 
		new HashMap<Object, IDomainObject<?>>();

	/*
	 * @see org.essentialplatform.session.ISession#footprintFor(org.essentialplatform.domain.IDomainClass)
	 */
	public List<IDomainObject<?>> footprintFor(IDomainClass domainClass) {
		return Collections.unmodifiableList(getDomainObjectsFor(domainClass));
	}

	/*
	 * @see org.essentialplatform.session.ISession#reset()
	 */
	public void reset() {
		domainObjectsByDomainClass.clear();
	}


	/*
	 * 
	 * @see org.essentialplatform.session.ISession#hasDomainObjectFor(java.lang.Object)
	 */
	public boolean hasDomainObjectFor(Object pojo) {
		return _domainObjectByPojo.get(pojo) != null;
	}

	/*
	 * @see org.essentialplatform.session.ISession#getDomainObjectFor(java.lang.Object, java.lang.Class)
	 */
	public <T> IDomainObject<T> getDomainObjectFor(Object pojo, Class<T> pojoClass) {
		IDomainObject<T> domainObject = (IDomainObject<T>)_domainObjectByPojo.get(pojo);
		if (domainObject == null) {
			throw new IllegalStateException("Not attached to session");
		}
		return domainObject;
	}


	/**
	 * Partitioned hash of objects by their class.
	 */
	private Map<IDomainClass, List<IDomainObject<?>>> 
		domainObjectsByDomainClass = new HashMap<IDomainClass, List<IDomainObject<?>>>();

	private synchronized List<IDomainObject<?>> getDomainObjectsFor(IDomainClass domainClass) {
		List<IDomainObject<?>> domainObjects = 
			domainObjectsByDomainClass.get(domainClass);
		if (domainObjects == null) {
			domainObjects = new ArrayList<IDomainObject<?>>();
			domainObjectsByDomainClass.put(domainClass, domainObjects);
		}
		return domainObjects;
	}
	 
	private <T> List<IDomainObject<?>> getDomainObjectsFor(IDomainObject<T> domainObject) {
		return getDomainObjectsFor(domainObject.getDomainClass());
	}

	
	////////////////////////////////////////////////////////////////
	// OBSERVED FEATURES
	////////////////////////////////////////////////////////////////

	/**
	 * All {@link IObservedFeature}s that are currently being referenced
	 * (elsewhere).
	 * 
	 * <p>
	 * Although a map is being used, the value is always null (if a WeakHashSet
	 * existed, we would be using it...).  
	 */
	private WeakHashMap<IObservedFeature, Object> _observedFeatures = new WeakHashMap<IObservedFeature, Object>();

	/*
	 * @see org.essentialplatform.session.ISession#getObservedFeatures()
	 */
	public Set<IObservedFeature> getObservedFeatures() {
		return _observedFeatures.keySet();
	}
	
	/*
	 * 
	 * @see org.essentialplatform.session.ISession#addObservedFeature(org.essentialplatform.session.IObservedFeature)
	 */
	public void addObservedFeature(IObservedFeature observedFeature) {
		_observedFeatures.put(observedFeature, null);
	}


	////////////////////////////////////////////////////////////////
	// SESSION LISTENERS 
	////////////////////////////////////////////////////////////////

	private List<ISessionListener> _listeners = new ArrayList<ISessionListener>();
	
	/*
	 * @see org.essentialplatform.session.ISession#addSessionListener(null)
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
	 * @see org.essentialplatform.session.ISession#removeSessionListener(org.essentialplatform.session.ISessionListener)
	 */
	public void removeSessionListener(ISessionListener listener) {
		synchronized(_listeners) {
			_listeners.remove(listener);
		}
	}

}
