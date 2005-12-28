package org.essentialplatform.runtime.domain;

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
import org.essentialplatform.core.deployment.Binding.IAttributeBinding;
import org.essentialplatform.core.deployment.Binding.ICollectionReferenceBinding;
import org.essentialplatform.core.deployment.Binding.IOneToOneReferenceBinding;
import org.essentialplatform.core.deployment.Binding.IOperationBinding;
import org.essentialplatform.core.deployment.Binding.IReferenceBinding;
import org.essentialplatform.core.domain.Domain;
import org.essentialplatform.core.domain.DomainClass;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.domain.IDomainClass.IAttribute;
import org.essentialplatform.core.domain.IDomainClass.IOperation;
import org.essentialplatform.core.domain.IDomainClass.IReference;
import org.essentialplatform.core.domain.adapters.IDomainClassAdapter;
import org.essentialplatform.progmodel.essential.app.IPrerequisites;
import org.essentialplatform.runtime.RuntimeBinding.RuntimeAttributeBinding;
import org.essentialplatform.runtime.RuntimeBinding.RuntimeOperationBinding;
import org.essentialplatform.runtime.domain.adapters.IRuntimeDomainClassAdapter;
import org.essentialplatform.runtime.domain.event.DomainObjectAttributeEvent;
import org.essentialplatform.runtime.domain.event.DomainObjectReferenceEvent;
import org.essentialplatform.runtime.domain.event.ExtendedDomainObjectAttributeEvent;
import org.essentialplatform.runtime.domain.event.ExtendedDomainObjectOperationEvent;
import org.essentialplatform.runtime.domain.event.ExtendedDomainObjectReferenceEvent;
import org.essentialplatform.runtime.domain.event.IDomainObjectAttributeListener;
import org.essentialplatform.runtime.domain.event.IDomainObjectListener;
import org.essentialplatform.runtime.domain.event.IDomainObjectOperationListener;
import org.essentialplatform.runtime.domain.event.IDomainObjectReferenceListener;
import org.essentialplatform.runtime.persistence.PersistenceId;
import org.essentialplatform.runtime.session.ISession;
import org.essentialplatform.runtime.session.SessionBinding;

/**
 * Wrapper for a POJO that implements the choreography between the rest of the
 * platform and the pojo itself.
 * 
 * <p>
 * Additionally, knows its {@link IDomainClass} and also {@link ISession} (if
 * any) to which it is currently attached.
 * 
 * <p>
 * Implementation note: introduced by {@link PojoAspect}.
 * 
 * @author Dan Haywood
 */
public final class DomainObject<T> implements IDomainObject<T> {

	/**
	 * Marked as <tt>transient</tt> so that it is not distributed.
	 */
	private transient IDomainClass _domainClass;

	private final T _pojo;

	/**
	 * Marked as <tt>transient</tt> so that it is not distributed.
	 */
	private transient Map<Class, Object> _adaptersByClass = new HashMap<Class, Object>();

	/**
	 * Marked as <tt>transient</tt> so that it is not distributed.
	 */
	private transient List<IDomainObjectListener> _domainObjectListeners = new ArrayList<IDomainObjectListener>();

	/**
	 * Marked as <tt>transient</tt> so that it is not distributed.
	 */
	private transient ISession _session;
	
	/**
	 * The identifier to the session within which this domain object
	 * resides.
	 * 
	 * <p>
	 * Note that this field is <i>not</i> marked as <tt>transient</tt>, in 
	 * other words it is distributed up to the server.  This is safe because
	 * session identifiers are globally unique (thanks to {@link java.util.GUID}).
	 */
	private String sessionId;

	/**
	 * Marked as <tt>transient</tt> so that it is not distributed.
	 */
	private transient WeakHashMap<EAttribute, IObjectAttribute> _attributesByEAttribute = new WeakHashMap<EAttribute, IObjectAttribute>();

	/**
	 * Marked as <tt>transient</tt> so that it is not distributed.
	 */
	private transient WeakHashMap<EReference, IObjectReference> _referencesByEReference = new WeakHashMap<EReference, IObjectReference>();

	/**
	 * Marked as <tt>transient</tt> so that it is not distributed.
	 */
	private transient WeakHashMap<EOperation, IObjectOperation> _operationsByEOperation = new WeakHashMap<EOperation, IObjectOperation>();

	private PersistenceId _persistenceId = null;

	private PersistState _persistState = PersistState.UNKNOWN;

	private ResolveState _resolveState = ResolveState.UNKNOWN;

	/**
	 * Creates a domain object to represent a pojo that cannot be persisted, and
	 * attaches to the specified session.
	 * 
	 * @param domainClass
	 * @param pojo -
	 *            the pojo that this domain object wraps and represents the
	 *            state of
	 * @param session -
	 *            to attach to
	 */
	public static <V> DomainObject<V> createTransient(final V pojo, final ISession session) {
		DomainObject<V> domainObject = (DomainObject<V>) ((IPojo) pojo).domainObject();
		domainObject.init(session, PersistState.TRANSIENT, 
				ResolveState.RESOLVED);
		return domainObject;
	}

	/**
	 * Creates a domain object to represent a pojo that will be persisted when
	 * the transaction commits, and attaches to the specified session.
	 * 
	 * @param domainClass
	 * @param pojo -
	 *            the pojo that this domain object wraps and represents the
	 *            state of
	 * @param session -
	 *            to attach to
	 */
	public static <V> DomainObject<V> createPersistent(final V pojo, final ISession session) {
		DomainObject<V> domainObject = (DomainObject<V>)((IPojo) pojo).domainObject();
		domainObject.init(
				session, PersistState.PERSISTED, 
				ResolveState.RESOLVED);
		return domainObject;
	}

	/**
	 * Creates a domain object to represent a pojo that has been persisted, but
	 * not yet resolved, and attaches to the specified session.
	 * 
	 * @param domainClass
	 * @param pojo -
	 *            the pojo that this domain object wraps and represents the
	 *            state of
	 * @param session -
	 *            to attach to
	 */
	public static <V> DomainObject recreatePersistent(final V pojo, final ISession session) {
		DomainObject<V> domainObject = (DomainObject<V>) ((IPojo) pojo).domainObject();
		domainObject.init(session, PersistState.PERSISTED, 
				ResolveState.UNRESOLVED);
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
	 * @param _domainClass
	 * @param _pojo
	 */
	private void init(final ISession session, PersistState persistState, ResolveState resolveState) {
		_session = session;
		_sessionBinding = session.getSessionBinding();
		_persistState = persistState;
		_resolveState = resolveState;
	}

	/*
	 * Lazily looks up domain class if necessary.
	 * 
	 * @see org.essentialplatform.session.IDomainObject#getDomainClass()
	 */
	public synchronized IDomainClass getDomainClass() {
		if (_domainClass == null) {
			_domainClass = lookupDomainClass(_pojo);
		}
		return _domainClass;
	}

	/**
	 * Returns the concrete implementation of {@link #getDomainClass()}.
	 * 
	 * @return
	 */
	DomainClass getDomainClassImpl() {
		return (DomainClass) getDomainClass();
	}

	/*
	 * @see org.essentialplatform.session.IDomainObject#getPojo()
	 */
	public T getPojo() {
		return _pojo;
	}

	/*
	 * @see org.essentialplatform.session.IDomainObject#getPersistenceId()
	 */
	public PersistenceId getPersistenceId() {
		return _persistenceId;
	}

	public void assignPersistenceId(PersistenceId persistenceId) {
		_persistenceId = persistenceId;
	}

	/*
	 * @see org.essentialplatform.session.IDomainObject#getAdapter(java.lang.Class)
	 */
	public <V> V getAdapter(Class<V> objectAdapterClass) {
		Object adapter = _adaptersByClass.get(objectAdapterClass);
		if (adapter == null) {
			List<IDomainClassAdapter> classAdapters = getDomainClass().getAdapters();
			for (IDomainClassAdapter classAdapter : classAdapters) {
				// JAVA5_FIXME: should be using covariance of getAdapters()
				IRuntimeDomainClassAdapter runtimeClassAdapter = (IRuntimeDomainClassAdapter) classAdapter;
				adapter = runtimeClassAdapter.getObjectAdapterFor(this, objectAdapterClass);
				if (adapter != null) {
					_adaptersByClass.put(adapter.getClass(), adapter);
					break;
				}
			}
		}
		return (V) adapter; // may still be null.
	}

	/*
	 * @see org.essentialplatform.session.IDomainObject#isPersistent()
	 */
	public boolean isPersistent() {
		return _persistState.isPersistent();
	}

	/*
	 * For the title we just return the POJO's <code>toString()</code>.
	 * 
	 * @see org.essentialplatform.session.IDomainObject#title()
	 */
	public String title() {
		return _pojo.toString();
	}

	/*
	 * @see org.essentialplatform.session.IDomainObject#getIAttributeNamed(java.lang.String)
	 */
	public IAttribute getIAttributeNamed(String attributeName) {
		return getDomainClass().getIAttributeNamed(attributeName);
	}

	/*
	 * @see org.essentialplatform.session.IDomainObject#getIReferenceNamed(java.lang.String)
	 */
	public IReference getIReferenceNamed(final String referenceName) {
		return getDomainClass().getIReferenceNamed(referenceName);
	}

	/*
	 * @see org.essentialplatform.session.IDomainObject#getIOperationNamed(java.lang.String)
	 */
	public IOperation getIOperationNamed(final String operationName) {
		return getDomainClass().getIOperationNamed(operationName);
	}

	/*
	 * @see org.essentialplatform.session.IDomainObject#getSession()
	 */
	public ISession getSession() {
		return _session;
	}

	/**
	 * Required to be serialized, so not <tt>transient</tt>.
	 */
	private SessionBinding _sessionBinding;
	/*
	 * @see org.essentialplatform.runtime.domain.IDomainObject#getSessionBinding()
	 */
	public SessionBinding getSessionBinding() {
		return _sessionBinding;
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
				throw new IllegalArgumentException("Session id does not match "
						+ "(_session.id = '" + session.getId() + "', "
						+ "this.sessionId = '" + this.getSessionId() + "')");
			}
		}
		this._session = session;
	}

	public void detached() {
		this._session = null;
	}

	/*
	 * 
	 * @see org.essentialplatform.session.IDomainObject#isAttached()
	 */
	public boolean isAttached() {
		return _session != null;
	}

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
			throw new IllegalStateException(
					"Cannot clear _session id when attached to _session");
		}
		sessionId = null;
	}

	public synchronized IObjectAttribute getAttribute(
			final IDomainClass.IAttribute iAttribute) {
		if (iAttribute == null) {
			return null;
		}
		EAttribute eAttribute = iAttribute.getEAttribute();
		IObjectAttribute attribute = _attributesByEAttribute.get(eAttribute);
		if (attribute == null) {
			attribute = new ObjectAttribute(eAttribute);
			_attributesByEAttribute.put(eAttribute, attribute);
		}
		getSession().addObservedFeature(attribute);
		return attribute;
	}

	/**
	 * Returns an {@link IObjectReference} for any EReference, whether it
	 * represents a 1:1 reference or a collection.
	 * 
	 * @param eReference
	 * @return
	 */
	synchronized IObjectReference getReference(final IDomainClass.IReference iReference) {
		if (iReference == null) {
			return null;
		}
		EReference eReference = iReference.getEReference(); 
		IObjectReference reference = _referencesByEReference.get(eReference);
		if (reference == null) {
			if (eReference.isMany()) {
				reference = new ObjectCollectionReference(eReference);
			} else {
				reference = new ObjectOneToOneReference(eReference);
			}
			_referencesByEReference.put(eReference, reference);
		}
		getSession().addObservedFeature(reference);
		return reference;
	}

	/*
	 * @see org.essentialplatform.session.IDomainObject#getOneToOneReference(org.eclipse.emf.ecore.EReference)
	 */
	public synchronized IObjectOneToOneReference getOneToOneReference(
			final IDomainClass.IReference iReference) {
		if (iReference == null) {
			return null;
		}
		EReference eReference = iReference.getEReference(); 
		if (eReference.isMany()) {
			throw new IllegalArgumentException(
				"EMF reference represents a collection (ref='" + eReference + "'");
		}
		IObjectOneToOneReference reference = 
			(IObjectOneToOneReference) _referencesByEReference.get(eReference);
		if (reference == null) {
			reference = new ObjectOneToOneReference(eReference);
			_referencesByEReference.put(eReference, reference);
		}
		getSession().addObservedFeature(reference);
		return reference;
	}

	/*
	 * @see org.essentialplatform.session.IDomainObject#getCollectionReference(org.eclipse.emf.ecore.EReference)
	 */
	public synchronized IObjectCollectionReference getCollectionReference(
			final IDomainClass.IReference iReference) {
		if (iReference == null) {
			return null;
		}
		EReference eReference = iReference.getEReference();
		if (!eReference.isMany()) {
			throw new IllegalArgumentException(
				"EMF reference represents a 1:1 reference (ref='" + eReference + "'");
		}
		IObjectCollectionReference reference = 
			(IObjectCollectionReference) _referencesByEReference.get(eReference);
		if (reference == null) {
			reference = new ObjectCollectionReference(eReference);
			_referencesByEReference.put(eReference, reference);
		}
		getSession().addObservedFeature(reference);
		return reference;
	}

	/*
	 * @see org.essentialplatform.session.IDomainObject#getOperation(org.eclipse.emf.ecore.EOperation)
	 */
	public synchronized IObjectOperation getOperation(IDomainClass.IOperation iOperation) {
		if (iOperation == null) {
			return null;
		}
		EOperation eOperation = iOperation.getEOperation();
		IObjectOperation operation = _operationsByEOperation.get(eOperation);
		if (operation == null) {
			operation = new ObjectOperation(eOperation);
			_operationsByEOperation.put(eOperation, operation);
		}
		getSession().addObservedFeature(operation);
		return operation;
	}

	private class ObjectAttribute implements IDomainObject.IObjectAttribute {

		private final EAttribute _eAttribute;

		private final IDomainClass.IAttribute _attribute;

		private final IAttributeBinding _runtimeBinding;
		
		/**
		 * Holds onto the current accessor prerequisites, if known.
		 * 
		 * <p>
		 * Used to determine whether listeners should be notified (see
		 * {@link #notifyListeners(IPrerequisites)}) whenever there is some
		 * external state change ({@link #externalStateChanged()}).
		 * 
		 * <p>
		 * Extended semantics.
		 */
		private IPrerequisites _currentPrerequisites;

		private final List<IDomainObjectAttributeListener> _listeners = new ArrayList<IDomainObjectAttributeListener>();

		/**
		 * Do not instantiate directly, instead use
		 * {@link DomainObject#getAttribute(EAttribute)}
		 * 
		 * @param eAttribute
		 */
		private ObjectAttribute(final EAttribute eAttribute) {
			_eAttribute = eAttribute;
			_attribute = getDomainClass().getIAttribute(eAttribute);
			_runtimeBinding = (IAttributeBinding) _attribute.getBinding(); // JAVA5_FIXME
		}

		/*
		 * @see org.essentialplatform.session.IDomainObject.IObjectAttribute#getDomainObject()
		 */
		public IDomainObject<?> getDomainObject() {
			return (IDomainObject) DomainObject.this; // JAVA_5_FIXME
		}

		/*
		 * @see
		 */
		public IAttribute getAttribute() {
			return _attribute;
		}

		/*
		 * @see org.essentialplatform.session.IDomainObject.IObjectAttribute#get()
		 */
		public Object get() {
			return _runtimeBinding.invokeAccessor(getDomainObject().getPojo());
		}

		/*
		 * @see org.essentialplatform.session.IDomainObject.IObjectAttribute#set(java.lang.Object)
		 */
		public void set(Object newValue) {
			_runtimeBinding.invokeMutator(getDomainObject().getPojo(), newValue);
		}

		/*
		 * Extended semantics.
		 */
		public IPrerequisites accessorPrerequisitesFor() {
			return _runtimeBinding.accessorPrerequisitesFor(
						getDomainObject().getPojo());
		}

		/*
		 * Extended semantics.
		 */
		public IPrerequisites mutatorPrerequisitesFor(
				final Object candidateValue) {
			return _runtimeBinding.mutatorPrerequisitesFor(
						getDomainObject().getPojo(), candidateValue);
		}

		/*
		 * Extended semantics.
		 */
		public IPrerequisites prerequisitesFor(final Object candidateValue) {
			return accessorPrerequisitesFor().andRequire(
					authorizationPrerequisitesFor()).andRequire(
					mutatorPrerequisitesFor(candidateValue));
		}

		/*
		 * Extended semantics.
		 */
		public IPrerequisites authorizationPrerequisitesFor() {
			IDomainClass rdc = getDomainClass();
			IDomainClass.IAttribute attribute = rdc.getIAttribute(_eAttribute);
			RuntimeAttributeBinding binding = (RuntimeAttributeBinding) attribute
					.getBinding();
			return binding.authorizationPrerequisites();
		}

		/*
		 * Extended semantics.
		 */
		public void externalStateChanged() {
			IPrerequisites prerequisitesPreviously = _currentPrerequisites;
			IPrerequisites prerequisitesNow = accessorPrerequisitesFor();

			boolean notifyListeners = prerequisitesPreviously == null
					|| !prerequisitesPreviously.equals(prerequisitesNow);

			_currentPrerequisites = prerequisitesNow;
			if (notifyListeners) {
				notifyListeners(_currentPrerequisites);
			}
		}

		/**
		 * Returns listener only because it simplifies test implementation to do
		 * so.
		 */
		public <T extends IDomainObjectAttributeListener> T addListener(
				T listener) {
			synchronized (_listeners) {
				if (!_listeners.contains(listener)) {
					_listeners.add(listener);
				}
			}
			return listener;
		}

		public void removeListener(IDomainObjectAttributeListener listener) {
			synchronized (_listeners) {
				_listeners.remove(listener);
			}
		}

		/*
		 * @see org.essentialplatform.session.IDomainObject.IObjectAttribute#notifyListeners(java.lang.Object)
		 */
		public void notifyListeners(Object newValue) {
			DomainObjectAttributeEvent event = new DomainObjectAttributeEvent(
					this, newValue);
			for (IDomainObjectAttributeListener listener : _listeners) {
				listener.attributeChanged(event);
			}
		}

		/**
		 * public so that it can be invoked by NotifyListenersAspect.
		 * 
		 * <p>
		 * Extended semantics.
		 * 
		 * @param attribute
		 * @param newPrerequisites
		 */
		public void notifyListeners(IPrerequisites newPrerequisites) {
			ExtendedDomainObjectAttributeEvent event = new ExtendedDomainObjectAttributeEvent(
					this, newPrerequisites);
			for (IDomainObjectAttributeListener listener : _listeners) {
				listener.attributePrerequisitesChanged(event);
			}
		}

		@Override
		public String toString() {
			return getAttribute().toString() + ", " + _listeners.size()
					+ " listeners" + ", prereqs=" + _currentPrerequisites;
		}

	}

	private class ObjectReference implements IDomainObject.IObjectReference {

		final EReference _eReference;

		final IDomainClass.IReference _reference;

		/**
		 * Holds onto the current accessor prerequisites, if known.
		 * 
		 * <p>
		 * Used to determine whether listeners should be notified (see
		 * {@link #notifyReferenceListeners(IPrerequisites)}) whenever there is
		 * some external state change ({@link #externalStateChanged()}).
		 */
		private IPrerequisites _currentPrerequisites;

		ResolveState _resolveState = ResolveState.UNRESOLVED;

		final IReferenceBinding _runtimeBinding;

		final List<IDomainObjectReferenceListener> _listeners = new ArrayList<IDomainObjectReferenceListener>();

		/**
		 * Do not instantiate directly, instead use
		 * {@link DomainObject#getReferenceNamed(String)}
		 * 
		 * @param eReference
		 */
		private ObjectReference(final EReference eReference) {
			this._eReference = eReference;
			this._reference = getDomainClass().getIReference(eReference);
			this._runtimeBinding = (IReferenceBinding) _reference.getBinding(); // JAVA5_FIXME
		}

		/*
		 * @see org.essentialplatform.session.IDomainObject.IObjectReference#getReference()
		 */
		public IReference getReference() {
			return _reference;
		}

		/*
		 * @see org.essentialplatform.session.IDomainObject.IObjectReference#getResolveState()
		 */
		public ResolveState getResolveState() {
			return _resolveState;
		}

		/*
		 * @see org.essentialplatform.session.IDomainObject.IObjectReference#getDomainObject()
		 */
		public <Q> IDomainObject<Q> getDomainObject() {
			return (IDomainObject) DomainObject.this; // JAVA_5_FIXME
		}

		/*
		 * @see org.essentialplatform.session.IResolvable#nowResolved()
		 */
		public void nowResolved() {
			checkInState(ResolveState.UNRESOLVED);
			_resolveState = ResolveState.RESOLVED;
		}

		/*
		 * @see org.essentialplatform.session.IDomainObject.IObjectMember#authorizationPrerequisitesFor()
		 */
		public IPrerequisites authorizationPrerequisitesFor() {
			return _runtimeBinding.authorizationPrerequisites();
		}

		/*
		 * @see org.essentialplatform.session.IDomainObject.IObjectReference#accessorPrerequisitesFor()
		 */
		public IPrerequisites accessorPrerequisitesFor() {
			return _runtimeBinding.accessorPrerequisitesFor(getPojo());
		}

		/*
		 * @see org.essentialplatform.session.IObservedFeature#externalStateChanged()
		 */
		public void externalStateChanged() {
			IPrerequisites prerequisitesPreviously = _currentPrerequisites;
			IPrerequisites prerequisitesNow = accessorPrerequisitesFor();

			boolean notifyListeners = 
				prerequisitesPreviously == null || 
				!prerequisitesPreviously.equals(prerequisitesNow);

			_currentPrerequisites = prerequisitesNow;
			if (notifyListeners) {
				notifyReferenceListeners(_currentPrerequisites);
			}
		}

		public <T extends IDomainObjectReferenceListener> T addListener(T listener) {
			_listeners.add(listener);
			return listener;
		}

		public void removeListener(IDomainObjectReferenceListener listener) {
			_listeners.remove(listener);
		}
		
		/**
		 * public so that it can be invoked by NotifyListenersAspect.
		 * 
		 * <p>
		 * Extended semantics.
		 * 
		 * @param attribute
		 * @param newPrerequisites
		 */
		public void notifyReferenceListeners(IPrerequisites newPrerequisites) {
			ExtendedDomainObjectReferenceEvent event = new ExtendedDomainObjectReferenceEvent(
					this, newPrerequisites);
			for (IDomainObjectReferenceListener listener : _listeners) {
				listener.referencePrerequisitesChanged(event);
			}
		}

		@Override
		public String toString() {
			return _eReference.toString() + ", " + _listeners.size()
					+ " listeners" + ", prereqs=" + _currentPrerequisites;
		}

	}

	private class ObjectOneToOneReference extends ObjectReference implements
			IDomainObject.IObjectOneToOneReference {

		private final IDomainClass.IOneToOneReference _oneToOneReference;

		private final IOneToOneReferenceBinding _runtimeBinding;

		private ObjectOneToOneReference(final EReference eReference) {
			super(eReference);
			assert !_reference.isMultiple();
			_oneToOneReference = (IDomainClass.IOneToOneReference) _reference;
			_runtimeBinding = (IOneToOneReferenceBinding) _oneToOneReference.getBinding();
		}

		public <Q> IDomainObject<Q> get() {
			Object referencedObject = _runtimeBinding.invokeAccessor(getDomainObject().getPojo());
			if (referencedObject == null) return null;
			if (!(referencedObject instanceof IPojo)) return null;
			IPojo referencedPojo = (IPojo)referencedObject;
			return referencedPojo.domainObject();
		}

		/*
		 * @see org.essentialplatform.session.IDomainObject.IObjectOneToOneReference#set(org.essentialplatform.session.IDomainObject,
		 *      java.lang.Object)
		 */
		public <Q> void set(IDomainObject<Q> domainObject) {
			if (domainObject != null) {
				_runtimeBinding.invokeAssociator(getDomainObject().getPojo(), domainObject.getPojo());
				// TODO: the notifyListeners aspect should be doing this for us.
				notifyListeners(domainObject.getPojo());
			} else {
				_runtimeBinding.invokeDissociator(getDomainObject().getPojo(), null);
				// TODO: the notifyListeners aspect should be doing this for us.
				notifyListeners(null);
			}
		}

		/*
		 * Extended semantics.
		 */
		public IPrerequisites prerequisitesFor(Object candidateValue) {
			return accessorPrerequisitesFor().andRequire(
					authorizationPrerequisitesFor()).andRequire(
					mutatorPrerequisitesFor(candidateValue));
		}

		/*
		 * Extended semantics.
		 */
		public IPrerequisites mutatorPrerequisitesFor(final Object candidateValue) {
			return _runtimeBinding.mutatorPrerequisitesFor(getPojo(), candidateValue);
		}

		/*
		 * @see org.essentialplatform.session.IDomainObject.IObjectReference#addListener(org.essentialplatform.session.IDomainObjectReferenceListener)
		 */
		public <Q extends IDomainObjectReferenceListener> Q addListener(Q listener) {
			synchronized (_listeners) {
				if (!_listeners.contains(listener)) {
					_listeners.add(listener);
				}
			}
			return listener;
		}

		/*
		 * @see org.essentialplatform.session.IDomainObject.IObjectReference#removeListener(org.essentialplatform.session.IDomainObjectReferenceListener)
		 */
		public void removeListener(IDomainObjectReferenceListener listener) {
			synchronized (_listeners) {
				_listeners.remove(listener);
			}
		}

		/*
		 * @see org.essentialplatform.session.IDomainObject.IObjectOneToOneReference#notifyListeners(java.lang.Object)
		 */
		public void notifyListeners(Object referencedObject) {
			DomainObjectReferenceEvent event = new DomainObjectReferenceEvent(this, referencedObject);
			for (IDomainObjectReferenceListener listener : _listeners) {
				listener.referenceChanged(event);
			}
		}

	}

	private class ObjectCollectionReference extends ObjectReference implements
			IDomainObject.IObjectCollectionReference {

		private final IDomainClass.ICollectionReference _collectionReference;
		private final ICollectionReferenceBinding _runtimeBinding;

		private ObjectCollectionReference(final EReference eReference) {
			super(eReference);
			assert _reference.isMultiple();
			_collectionReference = (IDomainClass.ICollectionReference) _reference;
			_runtimeBinding = (ICollectionReferenceBinding) _collectionReference.getBinding();
		}

		public <V> Collection<IDomainObject<V>> getCollection() {
			Collection<IPojo> pojoCollection = 
				(Collection<IPojo>) _runtimeBinding.invokeAccessor(getPojo());
			Collection<IDomainObject<V>> collection = new ArrayList<IDomainObject<V>>();
			for(IPojo pojo: pojoCollection) {
				collection.add(pojo.domainObject());
			}
			return Collections.unmodifiableCollection(collection);
		}

		/*
		 * @see org.essentialplatform.session.IDomainObject.IObjectCollectionReference#addToCollection(org.essentialplatform.session.IDomainObject)
		 */
		public <Q> void addToCollection(IDomainObject<Q> domainObject) {
			assert _collectionReference.getReferencedDomainClass() == domainObject
					.getDomainClass();
			assert _eReference.isChangeable();
			_runtimeBinding.invokeAssociator(getPojo(), domainObject.getPojo());
			// TODO: ideally the notifyListeners aspect should do this for us?
			// notify _domainObjectListeners
			DomainObjectReferenceEvent event = new DomainObjectReferenceEvent(
					this, getPojo());
			for (IDomainObjectReferenceListener listener : _listeners) {
				listener.collectionAddedTo(event);
			}
		}

		/*
		 * @see org.essentialplatform.session.IDomainObject.IObjectCollectionReference#removeFromCollection(org.essentialplatform.session.IDomainObject)
		 */
		public <Q> void removeFromCollection(IDomainObject<Q> domainObject) {
			assert _collectionReference.getReferencedDomainClass() == domainObject
					.getDomainClass();
			assert _eReference.isChangeable();
			_runtimeBinding.invokeDissociator(getPojo(), domainObject.getPojo());
			// TODO: ideally the notifyListeners aspect should do this for us?
			// notify _domainObjectListeners
			DomainObjectReferenceEvent event = new DomainObjectReferenceEvent(
					this, getPojo());
			for (IDomainObjectReferenceListener listener : _listeners) {
				listener.collectionRemovedFrom(event);
			}
		}

		/*
		 * Extended semantics.
		 */
		public IPrerequisites mutatorPrerequisitesFor(Object candidateValue, boolean beingAdded) {
			return _runtimeBinding.mutatorPrerequisitesFor(getPojo(), candidateValue, beingAdded);
		}
		
		/*
		 * Extended semantics.
		 */
		public IPrerequisites prerequisitesFor(Object candidateValue,
				boolean beingAdded) {
			return accessorPrerequisitesFor().andRequire(
					authorizationPrerequisitesFor()).andRequire(
					mutatorPrerequisitesFor(candidateValue, beingAdded));
		}

		/*
		 * @see org.essentialplatform.session.IDomainObject.IObjectCollectionReference#notifyListeners(java.lang.Object,
		 *      boolean)
		 */
		public void notifyListeners(Object referencedObject, boolean beingAdded) {
			DomainObjectReferenceEvent event = 
				new DomainObjectReferenceEvent(this, referencedObject);
			for (IDomainObjectReferenceListener listener : _listeners) {
				if (beingAdded) {
					listener.collectionAddedTo(event);
				} else {
					listener.collectionRemovedFrom(event);
				}
			}
		}

	}

	private class ObjectOperation implements IDomainObject.IObjectOperation {

		private final EOperation _eOperation;
		private final IDomainClass.IOperation _operation;
		private final IOperationBinding _runtimeBinding;
		private final List<IDomainObjectOperationListener> _listeners = new ArrayList<IDomainObjectOperationListener>();

		/*
		 * Extended semantics support.
		 */
		private final Map<Integer, Object> _argsByPosition = new HashMap<Integer, Object>();

		/**
		 * Holds onto the current (invoker) prerequisites, if known.
		 * 
		 * <p>
		 * Used to determine whether listeners should be notified (see
		 * {@link #notifyOperationListeners(IPrerequisites)}) whenever there is
		 * some external state change ({@link #externalStateChanged()}).
		 */
		private IPrerequisites _currentPrerequisites;

		
		/**
		 * Will reset the arguments, according to {@link #reset()}.
		 * 
		 * @param eOperation
		 */
		ObjectOperation(final EOperation eOperation) {
			this._eOperation = eOperation;
			this._operation = getDomainClass().getIOperation(eOperation);
			_runtimeBinding = (RuntimeOperationBinding) _operation.getBinding();
			
			reset();
		}

		public IDomainObject getDomainObject() {
			return (IDomainObject) DomainObject.this; // JAVA_5_FIXME
		}

		public IOperation getOperation() {
			return _operation;
		}

		public Object invokeOperation(final Object[] args) {
			return _runtimeBinding.invokeOperation(
					getDomainObject().getPojo(),args);
		}

		/*
		 * Extended semantics.
		 */
		public IPrerequisites authorizationPrerequisitesFor() {
			IDomainClass dc = getDomainClass();
			IDomainClass.IOperation operation = dc.getIOperation(_eOperation);
			RuntimeOperationBinding binding = (RuntimeOperationBinding) operation.getBinding();
			return binding.authorizationPrerequisites();
		}

		/*
		 * Extended semantics.
		 */
		public void setArg(final int position, final Object arg) {
			_runtimeBinding.assertIsValid(position, arg);
			_argsByPosition.put(position, arg);
		}

		/*
		 * Extended semantics.
		 */
		public Object[] getArgs() {
			return _runtimeBinding.getArgs(_argsByPosition);
		}

		/*
		 * Extended semantics.
		 */
		public IPrerequisites prerequisitesFor() {
			return _runtimeBinding.prerequisitesFor(getPojo(), getArgs());
		}
		
		/*
		 * Extended semantics.
		 */
		public Object[] reset() {
			_argsByPosition.clear();
			return _runtimeBinding.reset(getPojo(), getArgs(), _argsByPosition); 
		}

		/*
		 * Extended semantics.
		 */
		public void externalStateChanged() {
			IPrerequisites prerequisitesPreviously = _currentPrerequisites;
			IPrerequisites prerequisitesNow = prerequisitesFor();

			boolean notifyListeners = prerequisitesPreviously == null
					|| !prerequisitesPreviously.equals(prerequisitesNow);

			_currentPrerequisites = prerequisitesNow;
			if (notifyListeners) {
				notifyOperationListeners(_currentPrerequisites);
			}
		}

		/**
		 * Returns listener only because it simplifies test implementation to do
		 * so.
		 */
		public <T extends IDomainObjectOperationListener> T addListener(
				T listener) {
			synchronized (_listeners) {
				if (!_listeners.contains(listener)) {
					_listeners.add(listener);
				}
			}
			return listener;
		}

		/*
		 * @see org.essentialplatform.session.IDomainObject.IObjectOperation#removeListener(org.essentialplatform.session.IDomainObjectOperationListener)
		 */
		public void removeListener(IDomainObjectOperationListener listener) {
			synchronized (_listeners) {
				_listeners.remove(listener);
			}
		}

		/**
		 * public so that it can be invoked by NotifyListenersAspect.
		 * 
		 * @param attribute
		 * @param newPrerequisites
		 */
		public void notifyOperationListeners(IPrerequisites newPrerequisites) {
			ExtendedDomainObjectOperationEvent event = new ExtendedDomainObjectOperationEvent(
					this, newPrerequisites);
			for (IDomainObjectOperationListener listener : _listeners) {
				listener.operationPrerequisitesChanged(event);
			}
		}

		public String toString() {
			return _eOperation.toString() + ", " + _listeners.size()
					+ " listeners" + ", args=" + getArgs() + ", prereqs="
					+ _currentPrerequisites;
		}

	}

	/*
	 * @see org.essentialplatform.session.IPersistable#getPersistState()
	 */
	public PersistState getPersistState() {
		return _persistState;
	}

	/*
	 * @see org.essentialplatform.session.IResolvable#nowTransient()
	 */
	public void nowTransient() {
		checkInState(PersistState.PERSISTED);
		_persistState = PersistState.TRANSIENT;
	}

	/*
	 * @see org.essentialplatform.session.IPersistable#nowPersisted()
	 */
	public void nowPersisted() {
		checkInState(PersistState.TRANSIENT);
		_persistState = PersistState.PERSISTED;
	}

	/*
	 * @see org.essentialplatform.session.IResolvable#getResolveState()
	 */
	public ResolveState getResolveState() {
		return _resolveState;
	}

	/*
	 * @see org.essentialplatform.session.IResolvable#nowResolved()
	 */
	public void nowResolved() {
		checkInState(ResolveState.UNRESOLVED);
		_resolveState = ResolveState.RESOLVED;
	}

	private void checkInState(ResolveState... resolveStates) {
		for (int i = 0; i < resolveStates.length; i++) {
			if (getResolveState() == resolveStates[i]) {
				return;
			}
		}
		throw new IllegalStateException("Current resolve state is '"
				+ getResolveState() + "', " + "should be in state of '"
				+ Arrays.asList(resolveStates) + "'");
	}

	private void checkInState(PersistState... persistStates) {
		for (int i = 0; i < persistStates.length; i++) {
			if (getPersistState() == persistStates[i]) {
				return;
			}
		}
		throw new IllegalStateException("Current persist state is '"
				+ getPersistState() + "', " + "should be in state of '"
				+ Arrays.asList(persistStates) + "'");
	}

	/*
	 * @see org.essentialplatform.session.IDomainObject#addListener(null)
	 */
	public <T extends IDomainObjectListener> T addListener(T listener) {
		synchronized (_domainObjectListeners) {
			if (!_domainObjectListeners.contains(listener)) {
				_domainObjectListeners.add(listener);
			}
		}
		return listener;
	}

	/*
	 * @see org.essentialplatform.session.IDomainObject#removeListener(org.essentialplatform.session.IDomainObjectListener)
	 */
	public void removeListener(IDomainObjectListener listener) {
		synchronized (_domainObjectListeners) {
			_domainObjectListeners.remove(listener);
		}
	}

	public boolean isInitialized() {
		return _resolveState != null && !_resolveState.isUnknown() &&
		       _persistState != null && !_persistState.isUnknown();
	}

	/**
	 * Factored out to allow impact to be called when undo/redo changes.
	 */
	public void externalStateChanged() {
		ISession session = this.getSession();
		for(IObservedFeature observedFeature: session.getObservedFeatures()) {
			observedFeature.externalStateChanged();
		}
	}

	private static <V> IDomainClass lookupDomainClass(final V pojo) {
		Class<?> javaClass = pojo.getClass();
		IDomainClass domainClass = Domain.domainFor(javaClass).lookup(javaClass);
		return domainClass;
	}



}
