package de.berlios.rcpviewer.progmodel.standard;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EReference;

import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.domain.IRuntimeDomainClass;
import de.berlios.rcpviewer.session.DomainObjectAttributeEvent;
import de.berlios.rcpviewer.session.DomainObjectReferenceEvent;
import de.berlios.rcpviewer.session.IDomainObject;
import de.berlios.rcpviewer.session.IDomainObjectAttributeListener;
import de.berlios.rcpviewer.session.IDomainObjectListener;
import de.berlios.rcpviewer.session.IDomainObjectOperationListener;
import de.berlios.rcpviewer.session.IDomainObjectReferenceListener;
import de.berlios.rcpviewer.session.IPojo;
import de.berlios.rcpviewer.session.ISession;
import de.berlios.rcpviewer.session.IPersistable;
import de.berlios.rcpviewer.session.IPersistable.PersistState;
import de.berlios.rcpviewer.session.IResolvable;
import de.berlios.rcpviewer.session.IResolvable.ResolveState;
import de.berlios.rcpviewer.session.IDomainObject.IAttribute;
import de.berlios.rcpviewer.session.IDomainObject.IOperation;

/**
 * Wrapper for a POJO that also knows its {@link IDomainClass} and the
 * {@link ISession} (if any) that owns it.
 * 
 * <p>
 * Implementation note: created by {@link DomainAspect} (perthis aspect for 
 * pojos).
 * 
 * @author Dan Haywood
 */
public final class DomainObject<T> implements IDomainObject<T> {

	private IRuntimeDomainClass<T> _domainClass;
	private final T _pojo;
	private Map<Class, Object> _adaptersByClass = new HashMap<Class, Object>();
	private List<IDomainObjectListener> _domainObjectListeners = new ArrayList<IDomainObjectListener>();
	private ISession _session;
	private WeakHashMap<EAttribute, IAttribute> _attributesByEAttribute = new WeakHashMap<EAttribute, IAttribute>();
	private WeakHashMap<EReference, IReference> _referencesByEReference = new WeakHashMap<EReference, IReference>();
	private WeakHashMap<EOperation, IOperation> _operationsByEOperation = new WeakHashMap<EOperation, IOperation>();
	
	private PersistState _persistState = PersistState.UNKNOWN;
	private ResolveState _resolveState = ResolveState.UNKNOWN;
	
	/**
	 * Creates a domain object to represent a pojo that cannot be persisted, and 
	 * attaches to the specified session.
	 * 
	 * @param domainClass
	 * @param pojo    - the pojo that this domain object wraps and represents the state of
	 * @param session - to attach to
	 */
	public static <V> DomainObject createTransient(final IRuntimeDomainClass<V> domainClass, final V pojo, final ISession session) {
		//DomainObject domainObject = new DomainObject(pojo);
		DomainObject domainObject = (DomainObject)((IPojo)pojo).getDomainObject();
		domainObject.init(domainClass, session, PersistState.TRANSIENT, ResolveState.RESOLVED);
		return domainObject;
	}
	
	/**
	 * Creates a domain object to represent a pojo that will be persisted when
	 * the transaction commits, and attaches to the specified session.
	 * 
	 * @param domainClass
	 * @param pojo    - the pojo that this domain object wraps and represents the state of
	 * @param session - to attach to
	 */
	public static <V> DomainObject createPersistent(final IRuntimeDomainClass<V> domainClass, final V pojo, final ISession session) {
		//DomainObject domainObject = new DomainObject(pojo);
		DomainObject domainObject = (DomainObject)((IPojo)pojo).getDomainObject();
		domainObject.init(domainClass, session, PersistState.PERSISTED, ResolveState.RESOLVED);
		return domainObject;
	}
	
	/**
	 * Creates a domain object to represent a pojo that has been persisted, 
	 * but not yet resolved, and attaches to the specified session.
	 * 
	 * @param domainClass
	 * @param pojo    - the pojo that this domain object wraps and represents the state of
	 * @param session - to attach to
	 */
	public static <V> DomainObject recreatePersistent(final IRuntimeDomainClass<V> domainClass, final V pojo, final ISession session) {
		//DomainObject domainObject = new DomainObject(pojo);
		DomainObject domainObject = (DomainObject)((IPojo)pojo).getDomainObject();
		domainObject.init(domainClass, session, PersistState.PERSISTED, ResolveState.UNRESOLVED);
		return domainObject;
	}
	
	/**
	 * Creates a domain object attached to the session.
	 * 
	 * <p>
	 * The domain object will not be attached to any session.
	 * 
	 * <p>
	 * TODO: public only so that PojoAspect can instantiate; need to address.
	 */
	public DomainObject(final T pojo) {
		this._pojo = pojo;
	}

	
	/**
	 * Initializes the domain object.
	 * 
	 * @param _domainClass
	 * @param _pojo
	 */
	private void init(final IRuntimeDomainClass<T> domainClass, final ISession session, PersistState persistState, ResolveState resolveState) {
		this._domainClass = domainClass;
		this._session = session;
		this._persistState = persistState;
		this._resolveState = resolveState;
	}

	/*
	 * @see de.berlios.rcpviewer.session.IDomainObject#getDomainClass()
	 */
	public IRuntimeDomainClass<T> getDomainClass() {
		return _domainClass;
	}
	/**
	 * Returns the concrete implementation of {@link #getDomainClass()}.
	 * @return
	 */
	RuntimeDomainClass<T> getDomainClassImpl() {
		return (RuntimeDomainClass<T>)_domainClass;
	}
	
	/*
	 * @see de.berlios.rcpviewer.session.IDomainObject#getPojo()
	 */
	public T getPojo() {
		return _pojo;
	}


	/**
	 * Returns the adapter of the specified class (if any).
	 */
	public <V> V getAdapter(Class<V> domainObjectClass) {
		Object adapter = _adaptersByClass.get(domainObjectClass);
		if (adapter == null) {
			adapter = getDomainClass().getObjectAdapterFor(this, domainObjectClass);
			_adaptersByClass.put(adapter.getClass(), adapter);
		}
		return (V)adapter;
	}


	/*
	 * @see de.berlios.rcpviewer.session.IDomainObject#isPersistent()
	 */
	public boolean isPersistent() {
		return _persistState.isPersistent();
	}


	// in process of removing; performed instead through transactions.
//	public void persist() {
//		if (isPersistent()) {
//			throw new IllegalStateException("Already persisted.");
//		}
//		if (getSession() == null) {
//			throw new IllegalStateException("Not attached to a _session");
//		}
//		getSession().persist(this);
//		_persistent = true;
//	}
	

	// in process of removing; performed instead through transactions.
//	public void save() {
//		if (!isPersistent()) {
//			throw new IllegalStateException("Not yet persisted.");
//		}
//		if (getSession() == null) {
//			throw new IllegalStateException("Not attached to a session");
//		}
//		getSession().save(this);
//	}

	/*
	 * For the title we just return the POJO's <code>toString()</code>.
	 * 
	 * @see de.berlios.rcpviewer.session.IDomainObject#title()
	 */
	public String title() {
		return _pojo.toString();
	}


	/*
	 * @see de.berlios.rcpviewer.session.IDomainObject#getEAttributeNamed(java.lang.String)
	 */
	public EAttribute getEAttributeNamed(String attributeName) {
		return getDomainClass().getEAttributeNamed(attributeName);
	}

	/*
	 * @see de.berlios.rcpviewer.session.IDomainObject#getEReferenceNamed(java.lang.String)
	 */
	public EReference getEReferenceNamed(final String referenceName) {
		return getDomainClass().getEReferenceNamed(referenceName);
	}

	/*
	 * @see de.berlios.rcpviewer.session.IDomainObject#getEOperationNamed(java.lang.String)
	 */
	public EOperation getEOperationNamed(final String operationName) {
		return getDomainClass().getEOperationNamed(operationName);
	}

	
	/*
	 * @see de.berlios.rcpviewer.session.IDomainObject#getSession()
	 */
	public ISession getSession() {
		return _session;
	}

	/**
	 * Ensures that the _session id is compatible.
	 */
	public void attached(ISession session) {
		if (session == null) {
			throw new IllegalArgumentException("Session is null");
		}
		if (this.getSessionId() == null) {
			this.sessionId = session.getId();
		} else {
			if (!session.getId().equals(this.getSessionId())) {
				throw new IllegalArgumentException(
					"Session id does not match " +
					"(_session.id = '" + session.getId() + "', " +
					"this.sessionId = '" + this.getSessionId() + "')");
			}
		}
		this._session = session;
	}
	
	public void detached() {
		this._session = null;
	}
	/*
	 * 
	 * @see de.berlios.rcpviewer.session.IDomainObject#isAttached()
	 */
	public boolean isAttached() {
		return _session != null;
	}

	private String sessionId;
	/**
	 * The identifier of the {@link ISession} that originally managed this
	 * domain object.
	 * 
	 * <p>
	 * If set to <code>null</code>, then indicates that the object is not
	 * currently attached.
	 */
	public String getSessionId() {
		return sessionId;
	}

	public void clearSessionId() {
		if (isAttached()) {
			throw new IllegalStateException("Cannot clear _session id when attached to _session");
		}
		sessionId = null;
	}

	public synchronized IAttribute getAttribute(final EAttribute eAttribute) {
		IAttribute attribute = _attributesByEAttribute.get(eAttribute);
		if (attribute == null) {
			attribute = new Attribute(eAttribute);
			_attributesByEAttribute.put(eAttribute, attribute);
		}
		return attribute;
	}

	/**
	 * Returns an {@link IReference} for any EReference, whether it represents
	 * a 1:1 reference or a collection.
	 *  
	 * @param eReference
	 * @return
	 */
	synchronized IReference getReference(final EReference eReference) {
		if (eReference == null) {
			return null;
		}
		IReference reference = _referencesByEReference.get(eReference);
		if (reference == null) {
			if (eReference.isMany()) {
				reference = new CollectionReference(eReference);
			} else {
				reference = new OneToOneReference(eReference);
			}
			_referencesByEReference.put(eReference, reference);
		}
		return reference;
	}

	/*
	 * @see de.berlios.rcpviewer.session.IDomainObject#getOneToOneReference(org.eclipse.emf.ecore.EReference)
	 */
	public synchronized IOneToOneReference getOneToOneReference(final EReference eReference) {
		if (eReference == null) {
			return null;
		}
		if (eReference.isMany()) {
			throw new IllegalArgumentException("EMF reference represents a collection (ref='" + eReference + "'");
		}
		IOneToOneReference reference = (IOneToOneReference)_referencesByEReference.get(eReference);
		if (reference == null) {
				reference = new OneToOneReference(eReference);
			_referencesByEReference.put(eReference, reference);
		}
		return reference;
	}

	/*
	 * @see de.berlios.rcpviewer.session.IDomainObject#getCollectionReference(org.eclipse.emf.ecore.EReference)
	 */
	public synchronized ICollectionReference getCollectionReference(final EReference eReference) {
		if (eReference == null) {
			return null;
		}
		if (!eReference.isMany()) {
			throw new IllegalArgumentException("EMF reference represents a 1:1 reference (ref='" + eReference + "'");
		}
		ICollectionReference reference = (ICollectionReference)_referencesByEReference.get(eReference);
		if (reference == null) {
				reference = new CollectionReference(eReference);
			_referencesByEReference.put(eReference, reference);
		}
		return reference;
	}

	/*
	 * @see de.berlios.rcpviewer.session.IDomainObject#getOperation(org.eclipse.emf.ecore.EOperation)
	 */
	public synchronized IOperation getOperation(EOperation eOperation) {
		if (eOperation == null) {
			return null;
		}
		IOperation operation = _operationsByEOperation.get(eOperation);
		if (operation == null) {
			operation = new Operation(eOperation);
			_operationsByEOperation.put(eOperation, operation);
		}
		return operation;
	}

	
	private class Attribute implements IDomainObject.IAttribute {

		private EAttribute _eAttribute;
		private List<IDomainObjectAttributeListener> _domainObjectAttributeListeners = 
			new ArrayList<IDomainObjectAttributeListener>();

		/**
		 * Do not instantiate directly, instead use {@link DomainObject#getAttribute(EAttribute)}
		 * 
		 * @param eAttribute
		 */
		private Attribute(final EAttribute eAttribute) {
			this._eAttribute = eAttribute;
		}

		/*
		 * @see de.berlios.rcpviewer.session.IDomainObject.IAttribute#getDomainObject()
		 */
		public <T> IDomainObject<T> getDomainObject() {
			return (IDomainObject)DomainObject.this; // JAVA_5_FIXME
		}

		/*
		 * @see de.berlios.rcpviewer.session.IDomainObject.IAttribute#getEAttribute()
		 */
		public EAttribute getEAttribute() {
			return _eAttribute;
		}

		/*
		 * @see de.berlios.rcpviewer.session.IDomainObject.IAttribute#get()
		 */
		public Object get() {
			Method accessorMethod = getDomainClass().getAccessorFor(_eAttribute);
			if (accessorMethod == null) {
				throw new UnsupportedOperationException("Accesor method '" + accessorMethod + "' not accessible / could not be found");
			}
			String accessorMethodName = accessorMethod.getName();
			try {
				return accessorMethod.invoke(getDomainObject().getPojo());
			} catch (SecurityException e) {
				throw new UnsupportedOperationException("Accessor method '" + accessorMethodName + "' not accessible", e);
			} catch (IllegalAccessException e) {
				throw new UnsupportedOperationException("Could not invoke accessor method '" + accessorMethodName + "'", e);
			} catch (InvocationTargetException e) {
				throw new UnsupportedOperationException("Could not invoke accessor method '" + accessorMethodName + "'", e);
			}
		}

		/*
		 * @see de.berlios.rcpviewer.session.IDomainObject.IAttribute#set(java.lang.Object)
		 */
		public void set(Object newValue) {
			Method mutatorMethod = getDomainClass().getMutatorFor(_eAttribute);
			if (mutatorMethod == null) {
				throw new UnsupportedOperationException("Mutator method '" + mutatorMethod + "' not accessible / could not be found");
			}
			String mutatorMethodName = mutatorMethod.getName();
			try {
				mutatorMethod.invoke(getDomainObject().getPojo(), newValue);
				
				// think this is superfluous because the aspect will do our bidding for us...
//				notifyAttributeListeners(newValue);
			} catch (SecurityException e) {
				throw new UnsupportedOperationException("Mutator method '" + mutatorMethodName + "' not accessible");
			} catch (IllegalAccessException e) {
				throw new UnsupportedOperationException("Could not invoke mutator method '" + mutatorMethodName + "'", e);
			} catch (InvocationTargetException e) {
				throw new UnsupportedOperationException("Could not invoke mutator method '" + mutatorMethodName + "'", e);
			}
		}
		
		/**
		 * Returns listener only because it simplifies test implementation to do so.
		 */
		public <T extends IDomainObjectAttributeListener> T addListener(T listener) {
			synchronized(_domainObjectAttributeListeners) {
				if (!_domainObjectAttributeListeners.contains(listener)) {
					_domainObjectAttributeListeners.add(listener);
				}
			}
			return listener;
		}
		public void removeListener(IDomainObjectAttributeListener listener) {
			synchronized(_domainObjectAttributeListeners) {
				_domainObjectAttributeListeners.remove(listener);
			}
		}
		
		/**
		 * public so that it can be invoked by NotifyListenersAspect.
		 * 
		 * @param attribute
		 * @param newValue
		 */
		public void notifyListeners(Object newValue) {
			DomainObjectAttributeEvent event = 
				new DomainObjectAttributeEvent(this, newValue);
			for(IDomainObjectAttributeListener listener: _domainObjectAttributeListeners) {
				listener.attributeChanged(event);
			}
		}

	}

	private class Reference implements IDomainObject.IReference {
		EReference _eReference;
		ResolveState _resolveState = ResolveState.UNRESOLVED;

		List<IDomainObjectReferenceListener> _listeners = 
			new ArrayList<IDomainObjectReferenceListener>();

		/**
		 * Do not instantiate directly, instead use {@link DomainObject#getReferenceNamed(String)}
		 * 
		 * @param eReference
		 */
		private Reference(final EReference eReference) {
			this._eReference = eReference;
		}

		/*
		 * @see de.berlios.rcpviewer.session.IDomainObject.IReference#getEReference()
		 */
		public EReference getEReference() {
			return _eReference;
		}

		/*
		 * @see de.berlios.rcpviewer.session.IDomainObject.IReference#getResolveState()
		 */
		public ResolveState getResolveState() {
			return _resolveState;
		}

		/*
		 * @see de.berlios.rcpviewer.session.IDomainObject.IReference#getDomainObject()
		 */
		public <T> IDomainObject<T> getDomainObject() {
			return (IDomainObject)DomainObject.this; // JAVA_5_FIXME
		}


		/**
		 * Returns listener only because it simplifies test implementation to do so.
		 */
		public <T extends IDomainObjectReferenceListener> T addListener(T listener) {
			synchronized(_listeners) {
				if (!_listeners.contains(listener)) {
					_listeners.add(listener);
				}
			}
			return listener;
		}
		public void removeListener(IDomainObjectReferenceListener listener) {
			synchronized(_listeners) {
				_listeners.remove(listener);
			}
		}

		/*
		 * @see de.berlios.rcpviewer.session.IResolvable#nowResolved()
		 */
		public void nowResolved() {
			checkInState(ResolveState.UNRESOLVED);
			_resolveState = ResolveState.RESOLVED;
		}

	}
	
	private class OneToOneReference extends Reference
	                                  implements IDomainObject.IOneToOneReference {

		private OneToOneReference(final EReference eReference) {
			super(eReference);
			assert !getDomainClass().isMultiple(_eReference);
		}

		public <Q> Q get() {
			Method accessorMethod = getDomainClass().getAccessorFor(_eReference);
			if (accessorMethod == null) {
				throw new UnsupportedOperationException("Accesor method '" + accessorMethod + "' not accessible / could not be found");
			}
			String accessorMethodName = accessorMethod.getName();

			try {
				return (Q)accessorMethod.invoke(getDomainObject().getPojo());
			} catch (SecurityException e) {
				throw new UnsupportedOperationException("Accessor method '" + accessorMethodName + "' not accessible", e);
			} catch (IllegalAccessException e) {
				throw new UnsupportedOperationException("Could not invoke accessor method '" + accessorMethodName + "'", e);
			} catch (InvocationTargetException e) {
				throw new UnsupportedOperationException("Could not invoke accessor method '" + accessorMethodName + "'", e);
			}
		}

		/*
		 * @see de.berlios.rcpviewer.session.IDomainObject.IOneToOneReference#set(de.berlios.rcpviewer.session.IDomainObject, java.lang.Object)
		 */
		public <Q> void set(IDomainObject<Q> domainObject) {
			Method associatorOrDissociatorMethod = null;
		
			if (domainObject != null) {
				associatorOrDissociatorMethod = getDomainClass().getAssociatorFor(_eReference);
			} else {
				associatorOrDissociatorMethod = getDomainClass().getDissociatorFor(_eReference); 
			}
			if (associatorOrDissociatorMethod == null) {
				throw new UnsupportedOperationException("Associator/dissociator method '" + associatorOrDissociatorMethod + "' not accessible / could not be found");
			}
			String associatorOrDissociatorMethodName = associatorOrDissociatorMethod.getName();
			try {
				Object referencedObjectOrNull = (domainObject != null? domainObject.getPojo(): null);
				associatorOrDissociatorMethod.invoke(getDomainObject().getPojo(), referencedObjectOrNull );
				
				// TODO: the notifyListeners aspect should be doing this for us.
				notifyListeners(referencedObjectOrNull);
			} catch (SecurityException e) {
				throw new UnsupportedOperationException("Associator/dissociator method '" + associatorOrDissociatorMethodName + "' not accessible");
			} catch (IllegalAccessException e) {
				throw new UnsupportedOperationException("Could not invoke associator/dissociator method '" + associatorOrDissociatorMethodName + "'", e);
			} catch (InvocationTargetException e) {
				throw new UnsupportedOperationException("Could not invoke associator/dissociator method '" + associatorOrDissociatorMethodName + "'", e);
			}
		}

		/*
		 * @see de.berlios.rcpviewer.session.IDomainObject.IOneToOneReference#notifyListeners(java.lang.Object)
		 */
		public void notifyListeners(Object referencedObject) {
			DomainObjectReferenceEvent event = new DomainObjectReferenceEvent(this, referencedObject); 
			for(IDomainObjectReferenceListener listener: _listeners) {
				listener.referenceChanged(event);
			}
		}
		
	}
	
	private class CollectionReference extends Reference
	                                  implements IDomainObject.ICollectionReference {

		private CollectionReference(final EReference eReference) {
			super(eReference);
			assert getDomainClass().isMultiple(_eReference);
		}
		
		public <V> Collection<IDomainObject<V>> getCollection() {
			Method collectionAccessorMethod = getDomainClass().getAccessorFor(_eReference);
			Collection<IDomainObject<V>> collection;
			try {
				collection = (Collection<IDomainObject<V>>)collectionAccessorMethod.invoke(getPojo(), new Object[]{});
				return Collections.unmodifiableCollection(collection);
			} catch (IllegalArgumentException ex) {
				// TODO - log?
				return null;
			} catch (IllegalAccessException ex) {
				// TODO - log?
				return null;
			} catch (InvocationTargetException ex) {
				// TODO - log?
				return null;
			}
		}

		public <Q> void addToCollection(IDomainObject<Q> domainObject) {
			assert getDomainClass().isMultiple(_eReference);
			assert getDomainClass().getReferencedClass(_eReference) == 
				domainObject.getDomainClass();
			Method collectionAssociatorMethod = getDomainClass().getAssociatorFor(_eReference);
			try {
				collectionAssociatorMethod.invoke(getPojo(), new Object[]{domainObject.getPojo()});

				// TODO: ideally the notifyListeners aspect should do this for us? 
				// notify _domainObjectListeners
				DomainObjectReferenceEvent event = 
					new DomainObjectReferenceEvent(this, getPojo());
				for(IDomainObjectReferenceListener listener: _listeners) {
					listener.collectionAddedTo(event);
				}
			} catch (IllegalArgumentException ex) {
				// TODO - log?
				throw new RuntimeException(ex);
			} catch (IllegalAccessException ex) {
				// TODO - log?
				throw new RuntimeException(ex);
			} catch (InvocationTargetException ex) {
				// TODO - log?
				throw new RuntimeException(ex);
			}
		}
		public <Q> void removeFromCollection(IDomainObject<Q> domainObject) {
			assert getDomainClass().isMultiple(_eReference);
			assert getDomainClass().getReferencedClass(_eReference) == 
				domainObject.getDomainClass();
			Method collectionDissociatorMethod = getDomainClass().getDissociatorFor(_eReference);
			try {
				collectionDissociatorMethod.invoke(getPojo(), new Object[]{domainObject.getPojo()});

				// TODO: ideally the notifyListeners aspect should do this for us? 
				// notify _domainObjectListeners
				DomainObjectReferenceEvent event = 
					new DomainObjectReferenceEvent(this, getPojo());
				for(IDomainObjectReferenceListener listener: _listeners) {
					listener.collectionRemovedFrom(event);
				}
			} catch (IllegalArgumentException ex) {
				// TODO - log?
				throw new RuntimeException(ex);
			} catch (IllegalAccessException ex) {
				// TODO - log?
				throw new RuntimeException(ex);
			} catch (InvocationTargetException ex) {
				// TODO - log?
				throw new RuntimeException(ex);
			}
		}

		
		public void notifyListeners(Object referencedObject, boolean beingAdded) {
			DomainObjectReferenceEvent event = 
				new DomainObjectReferenceEvent(this, referencedObject);
			for(IDomainObjectReferenceListener listener: _listeners) {
				if (beingAdded) {
					listener.collectionAddedTo(event);
				} else {
					listener.collectionRemovedFrom(event);
				}
			}
		}
	}

	
	private class Operation implements IDomainObject.IOperation {
		
		private EOperation _eOperation;
		private List<IDomainObjectOperationListener> _listeners = 
			new ArrayList<IDomainObjectOperationListener>();

		/**
		 * Do not instantiate directly, instead use {@link DomainObject#getOperationNamed(String)}
		 * 
		 * @param eOperation
		 */
		Operation(final EOperation eOperation) {
			this._eOperation = eOperation;
		}
		
		public <T> IDomainObject<T> getDomainObject() {
			return (IDomainObject)DomainObject.this; // JAVA_5_FIXME
		}

		public EOperation getEOperation() {
			return _eOperation;
		}

		public Object invokeOperation(final Object[] args) {
			Method operationMethod = getDomainClass().getInvokerFor(_eOperation);
			if (operationMethod == null) {
				throw new UnsupportedOperationException("Operation method '" + operationMethod + "' not accessible / could not be found");
			}
			String operationMethodName = operationMethod.getName();
			try {
				return operationMethod.invoke(getDomainObject().getPojo(), args);
			} catch (SecurityException e) {
				throw new UnsupportedOperationException("Operation invoker method '" + operationMethodName + "' not accessible");
			} catch (IllegalAccessException e) {
				throw new UnsupportedOperationException("Operation invoker method '" + operationMethodName + "' not accessible");
			} catch (InvocationTargetException e) {
				throw new RuntimeException("Operation method threw an exception '" + operationMethodName + "'", e.getCause());
			}
			
		}

		/**
		 * Returns listener only because it simplifies test implementation to do so.
		 */
		public <T extends IDomainObjectOperationListener> T addListener(T listener) {
			synchronized(_listeners) {
				if (!_listeners.contains(listener)) {
					_listeners.add(listener);
				}
			}
			return listener;
		}
		/*
		 * @see de.berlios.rcpviewer.session.IDomainObject.IOperation#removeListener(de.berlios.rcpviewer.session.IDomainObjectOperationListener)
		 */
		public void removeListener(IDomainObjectOperationListener listener) {
			synchronized(_listeners) {
				_listeners.remove(listener);
			}
		}

	}

	/*
	 * @see de.berlios.rcpviewer.session.IPersistable#getPersistState()
	 */
	public PersistState getPersistState() {
		return _persistState;
	}

	/*
	 * @see de.berlios.rcpviewer.session.IResolvable#nowTransient()
	 */
	public void nowTransient() {
		checkInState(PersistState.PERSISTED);
		_persistState = PersistState.TRANSIENT;
	}

	/*
	 * @see de.berlios.rcpviewer.session.IPersistable#nowPersisted()
	 */
	public void nowPersisted() {
		checkInState(PersistState.TRANSIENT);
		_persistState = PersistState.PERSISTED;
	}


	/*
	 * @see de.berlios.rcpviewer.session.IResolvable#getResolveState()
	 */
	public ResolveState getResolveState() {
		return _resolveState;
	}

	/*
	 * @see de.berlios.rcpviewer.session.IResolvable#nowResolved()
	 */
	public void nowResolved() {
		checkInState(ResolveState.UNRESOLVED);
		_resolveState = ResolveState.RESOLVED;
	}

	private void checkInState(ResolveState... resolveStates) {
		for(int i=0; i<resolveStates.length; i++) {
			if (getResolveState() == resolveStates[i]) {
				return;
			}
		}
		throw new IllegalStateException(
				"Current resolve state is '" + getResolveState() + "', "
				+ "should be in state of '" + Arrays.asList(resolveStates) + "'");
	}

	private void checkInState(PersistState... persistStates) {
		for(int i=0; i<persistStates.length; i++) {
			if (getPersistState() == persistStates[i]) {
				return;
			}
		}
		throw new IllegalStateException(
				"Current persist state is '" + getPersistState() + "', "
				+ "should be in state of '" + Arrays.asList(persistStates) + "'");
	}

	/*
	 * @see de.berlios.rcpviewer.session.IDomainObject#addListener(null)
	 */
	public <T extends IDomainObjectListener> T addListener(T listener) {
		synchronized(_domainObjectListeners) {
			if (!_domainObjectListeners.contains(listener)) {
				_domainObjectListeners.add(listener);
			}
		}
		return listener;
	}
	/*
	 * @see de.berlios.rcpviewer.session.IDomainObject#removeListener(de.berlios.rcpviewer.session.IDomainObjectListener)
	 */
	public void removeListener(IDomainObjectListener listener) {
		synchronized(_domainObjectListeners) {
			_domainObjectListeners.remove(listener);
		}
	}
	
}
