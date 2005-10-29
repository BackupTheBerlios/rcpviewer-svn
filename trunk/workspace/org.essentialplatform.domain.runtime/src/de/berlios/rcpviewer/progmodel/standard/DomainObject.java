package de.berlios.rcpviewer.progmodel.standard;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.EReference;

import de.berlios.rcpviewer.domain.DomainClass;
import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.domain.IDomainClassAdapter;
import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.domain.IDomainClassAdapter;
import de.berlios.rcpviewer.domain.IRuntimeDomainClassAdapter;
import de.berlios.rcpviewer.domain.IDomainClass.IAttribute;
import de.berlios.rcpviewer.domain.IDomainClass.IOneToOneReference;
import de.berlios.rcpviewer.domain.runtime.RuntimeDeployment.AbstractRuntimeReferenceBinding;
import de.berlios.rcpviewer.domain.runtime.RuntimeDeployment.RuntimeAttributeBinding;
import de.berlios.rcpviewer.domain.runtime.RuntimeDeployment.RuntimeCollectionReferenceBinding;
import de.berlios.rcpviewer.domain.runtime.RuntimeDeployment.RuntimeOneToOneReferenceBinding;
import de.berlios.rcpviewer.domain.runtime.RuntimeDeployment.RuntimeOperationBinding;
import de.berlios.rcpviewer.persistence.PersistenceId;
import de.berlios.rcpviewer.progmodel.extended.IPrerequisites;
import de.berlios.rcpviewer.progmodel.extended.Prerequisites;
import de.berlios.rcpviewer.session.DomainObjectAttributeEvent;
import de.berlios.rcpviewer.session.DomainObjectReferenceEvent;
import de.berlios.rcpviewer.session.ExtendedDomainObjectAttributeEvent;
import de.berlios.rcpviewer.session.ExtendedDomainObjectOperationEvent;
import de.berlios.rcpviewer.session.ExtendedDomainObjectReferenceEvent;
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
import de.berlios.rcpviewer.session.IDomainObject.IObjectAttribute;
import de.berlios.rcpviewer.session.IDomainObject.IObjectOperation;
import de.berlios.rcpviewer.transaction.internal.OneToOneReferenceChange;

/**
 * Wrapper for a POJO that implements the choreography between the rest of the
 * platform and the pojo itself.
 * 
 * <p>
 * Additionally, knows its {@link IDomainClass} and also {@link ISession} (if
 * any) to which it is currently attached.
 * 
 * <p>
 * Implementation note: created by {@link DomainAspect} (perthis aspect for
 * pojos).
 * 
 * <p>
 * TODO: should implement the choreography of interacting with the underlying
 * POJOs.
 * 
 * @author Dan Haywood
 */
public final class DomainObject<T> implements IDomainObject<T> {

	private IDomainClass _domainClass;

	private final T _pojo;

	private Map<Class, Object> _adaptersByClass = new HashMap<Class, Object>();

	private List<IDomainObjectListener> _domainObjectListeners = new ArrayList<IDomainObjectListener>();

	private ISession _session;

	private WeakHashMap<EAttribute, IObjectAttribute> _attributesByEAttribute = new WeakHashMap<EAttribute, IObjectAttribute>();

	private WeakHashMap<EReference, IObjectReference> _referencesByEReference = new WeakHashMap<EReference, IObjectReference>();

	private WeakHashMap<EOperation, IObjectOperation> _operationsByEOperation = new WeakHashMap<EOperation, IObjectOperation>();

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
	public static <V> DomainObject<V> createTransient(
			final IDomainClass domainClass, final V pojo, final ISession session) {
		// DomainObject domainObject = new DomainObject(pojo);
		DomainObject<V> domainObject = (DomainObject<V>) ((IPojo) pojo)
				.getDomainObject();
		domainObject.init(domainClass, session, PersistState.TRANSIENT,
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
	public static <V> DomainObject<V> createPersistent(
			final IDomainClass domainClass, final V pojo, final ISession session) {
		// DomainObject domainObject = new DomainObject(pojo);
		DomainObject<V> domainObject = (DomainObject<V>) ((IPojo) pojo)
				.getDomainObject();
		domainObject.init(domainClass, session, PersistState.PERSISTED,
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
	public static <V> DomainObject recreatePersistent(
			final IDomainClass domainClass, final V pojo, final ISession session) {
		// DomainObject domainObject = new DomainObject(pojo);
		DomainObject<V> domainObject = (DomainObject<V>) ((IPojo) pojo)
				.getDomainObject();
		domainObject.init(domainClass, session, PersistState.PERSISTED,
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
	 * 
	 * @param _domainClass
	 * @param _pojo
	 */
	private void init(final IDomainClass domainClass, final ISession session,
			PersistState persistState, ResolveState resolveState) {
		this._domainClass = domainClass;
		this._session = session;
		this._persistState = persistState;
		this._resolveState = resolveState;
	}

	/*
	 * @see de.berlios.rcpviewer.session.IDomainObject#getDomainClass()
	 */
	public IDomainClass getDomainClass() {
		return _domainClass;
	}

	/**
	 * Returns the concrete implementation of {@link #getDomainClass()}.
	 * 
	 * @return
	 */
	DomainClass getDomainClassImpl() {
		return (DomainClass) _domainClass;
	}

	/*
	 * @see de.berlios.rcpviewer.session.IDomainObject#getPojo()
	 */
	public T getPojo() {
		return _pojo;
	}

	/*
	 * @see de.berlios.rcpviewer.session.IDomainObject#getPersistenceId()
	 */
	public PersistenceId getPersistenceId() {
		return _persistenceId;
	}

	/**
	 * Set the persistence Id.
	 * 
	 * <p>
	 * Note that this is not configured using
	 * {@link #init(IDomainClass, ISession, PersistState, ResolveState)}.
	 * Instead, it will be derived from the values set directly by the
	 * application (application-assigned), or it will be set by the object store
	 * (objectstore-assigned).
	 * 
	 * @param persistenceId
	 */
	public void setPersistenceId(PersistenceId persistenceId) {
		_persistenceId = persistenceId;
	}

	/*
	 * @see de.berlios.rcpviewer.session.IDomainObject#getAdapter(java.lang.Class)
	 */
	public <V> V getAdapter(Class<V> objectAdapterClass) {
		Object adapter = _adaptersByClass.get(objectAdapterClass);
		if (adapter == null) {
			List<IDomainClassAdapter> classAdapters = getDomainClass()
					.getAdapters();
			for (IDomainClassAdapter classAdapter : classAdapters) {
				// JAVA5_FIXME: should be using covariance of getAdapters()
				IRuntimeDomainClassAdapter runtimeClassAdapter = (IRuntimeDomainClassAdapter) classAdapter;
				adapter = runtimeClassAdapter.getObjectAdapterFor(this,
						objectAdapterClass);
				if (adapter != null) {
					_adaptersByClass.put(adapter.getClass(), adapter);
					break;
				}
			}
		}
		return (V) adapter; // may still be null.
	}

	/*
	 * @see de.berlios.rcpviewer.session.IDomainObject#isPersistent()
	 */
	public boolean isPersistent() {
		return _persistState.isPersistent();
	}

	// in process of removing; performed instead through transactions.
	// public void persist() {
	// if (isPersistent()) {
	// throw new IllegalStateException("Already persisted.");
	// }
	// if (getSession() == null) {
	// throw new IllegalStateException("Not attached to a _session");
	// }
	// getSession().persist(this);
	// _persistent = true;
	// }

	// in process of removing; performed instead through transactions.
	// public void save() {
	// if (!isPersistent()) {
	// throw new IllegalStateException("Not yet persisted.");
	// }
	// if (getSession() == null) {
	// throw new IllegalStateException("Not attached to a session");
	// }
	// getSession().save(this);
	// }

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
			throw new IllegalStateException(
					"Cannot clear _session id when attached to _session");
		}
		sessionId = null;
	}

	public synchronized IObjectAttribute getAttribute(
			final EAttribute eAttribute) {
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
	synchronized IObjectReference getReference(final EReference eReference) {
		if (eReference == null) {
			return null;
		}
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
	 * @see de.berlios.rcpviewer.session.IDomainObject#getOneToOneReference(org.eclipse.emf.ecore.EReference)
	 */
	public synchronized IObjectOneToOneReference getOneToOneReference(
			final EReference eReference) {
		if (eReference == null) {
			return null;
		}
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
	 * @see de.berlios.rcpviewer.session.IDomainObject#getCollectionReference(org.eclipse.emf.ecore.EReference)
	 */
	public synchronized IObjectCollectionReference getCollectionReference(
			final EReference eReference) {
		if (eReference == null) {
			return null;
		}
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
	 * @see de.berlios.rcpviewer.session.IDomainObject#getOperation(org.eclipse.emf.ecore.EOperation)
	 */
	public synchronized IObjectOperation getOperation(EOperation eOperation) {
		if (eOperation == null) {
			return null;
		}
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

		private final RuntimeAttributeBinding _runtimeBinding;

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
			_attribute = getDomainClass().getAttribute(eAttribute);
			_runtimeBinding = (RuntimeAttributeBinding) _attribute.getBinding(); // JAVA5_FIXME
		}

		/*
		 * @see de.berlios.rcpviewer.session.IDomainObject.IObjectAttribute#getDomainObject()
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
		 * @see de.berlios.rcpviewer.session.IDomainObject.IObjectAttribute#get()
		 */
		public Object get() {
			return _runtimeBinding.invokeAccessor(getDomainObject().getPojo());
		}

		/*
		 * @see de.berlios.rcpviewer.session.IDomainObject.IObjectAttribute#set(java.lang.Object)
		 */
		public void set(Object newValue) {
			_runtimeBinding
					.invokeMutator(getDomainObject().getPojo(), newValue);
		}

		/*
		 * Extended semantics.
		 */
		public IPrerequisites accessorPrerequisitesFor() {
			return _runtimeBinding.accessorPrerequisitesFor(getDomainObject()
					.getPojo());
		}

		/*
		 * Extended semantics.
		 */
		public IPrerequisites mutatorPrerequisitesFor(
				final Object candidateValue) {
			return _runtimeBinding.mutatorPrerequisitesFor(getDomainObject()
					.getPojo(), candidateValue);
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
			IDomainClass.IAttribute attribute = rdc.getAttribute(_eAttribute);
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
		 * @see de.berlios.rcpviewer.session.IDomainObject.IObjectAttribute#notifyListeners(java.lang.Object)
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

		final AbstractRuntimeReferenceBinding _runtimeBinding;

		final List<IDomainObjectReferenceListener> _listeners = new ArrayList<IDomainObjectReferenceListener>();

		/**
		 * Do not instantiate directly, instead use
		 * {@link DomainObject#getReferenceNamed(String)}
		 * 
		 * @param eReference
		 */
		private ObjectReference(final EReference eReference) {
			this._eReference = eReference;
			this._reference = getDomainClass().getReference(eReference);
			this._runtimeBinding = (AbstractRuntimeReferenceBinding) _reference
					.getBinding(); // JAVA5_FIXME
		}

		/*
		 * @see de.berlios.rcpviewer.session.IDomainObject.IObjectReference#getEReference()
		 */
		public EReference getEReference() {
			return _eReference;
		}

		/*
		 * @see de.berlios.rcpviewer.session.IDomainObject.IObjectReference#getResolveState()
		 */
		public ResolveState getResolveState() {
			return _resolveState;
		}

		/*
		 * @see de.berlios.rcpviewer.session.IDomainObject.IObjectReference#getDomainObject()
		 */
		public <Q> IDomainObject<Q> getDomainObject() {
			return (IDomainObject) DomainObject.this; // JAVA_5_FIXME
		}

		/*
		 * @see de.berlios.rcpviewer.session.IResolvable#nowResolved()
		 */
		public void nowResolved() {
			checkInState(ResolveState.UNRESOLVED);
			_resolveState = ResolveState.RESOLVED;
		}

		/*
		 * @see de.berlios.rcpviewer.session.IDomainObject.IObjectMember#authorizationPrerequisitesFor()
		 */
		public IPrerequisites authorizationPrerequisitesFor() {
			return _runtimeBinding.authorizationPrerequisites();
		}

		/*
		 * @see de.berlios.rcpviewer.session.IDomainObject.IObjectReference#accessorPrerequisitesFor()
		 */
		public IPrerequisites accessorPrerequisitesFor() {
			return _runtimeBinding.accessorPrerequisitesFor(getPojo());
		}

		/*
		 * @see de.berlios.rcpviewer.session.IObservedFeature#externalStateChanged()
		 */
		public void externalStateChanged() {
			IPrerequisites prerequisitesPreviously = _currentPrerequisites;
			IPrerequisites prerequisitesNow = accessorPrerequisitesFor();

			boolean notifyListeners = prerequisitesPreviously == null
					|| !prerequisitesPreviously.equals(prerequisitesNow);

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
			return getEReference().toString() + ", " + _listeners.size()
					+ " listeners" + ", prereqs=" + _currentPrerequisites;
		}

	}

	private class ObjectOneToOneReference extends ObjectReference implements
			IDomainObject.IObjectOneToOneReference {

		private final IDomainClass.IOneToOneReference _oneToOneReference;

		private final RuntimeOneToOneReferenceBinding _runtimeBinding;

		private ObjectOneToOneReference(final EReference eReference) {
			super(eReference);
			assert !_reference.isMultiple();
			_oneToOneReference = (IDomainClass.IOneToOneReference) _reference;
			_runtimeBinding = (RuntimeOneToOneReferenceBinding) _oneToOneReference
					.getBinding();
		}

		public <Q> Q get() {
			return (Q) _runtimeBinding.invokeAccessor(getDomainObject().getPojo());
		}

		/*
		 * @see de.berlios.rcpviewer.session.IDomainObject.IObjectOneToOneReference#set(de.berlios.rcpviewer.session.IDomainObject,
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
		 * @see de.berlios.rcpviewer.session.IDomainObject.IObjectReference#addListener(de.berlios.rcpviewer.session.IDomainObjectReferenceListener)
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
		 * @see de.berlios.rcpviewer.session.IDomainObject.IObjectReference#removeListener(de.berlios.rcpviewer.session.IDomainObjectReferenceListener)
		 */
		public void removeListener(IDomainObjectReferenceListener listener) {
			synchronized (_listeners) {
				_listeners.remove(listener);
			}
		}

		/*
		 * @see de.berlios.rcpviewer.session.IDomainObject.IObjectOneToOneReference#notifyListeners(java.lang.Object)
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
		private final RuntimeCollectionReferenceBinding _runtimeBinding;

		private ObjectCollectionReference(final EReference eReference) {
			super(eReference);
			assert _reference.isMultiple();
			_collectionReference = (IDomainClass.ICollectionReference) _reference;
			_runtimeBinding = (RuntimeCollectionReferenceBinding) _collectionReference.getBinding();
		}

		public <V> Collection<IDomainObject<V>> getCollection() {
			Collection<IDomainObject<V>> collection = 
				(Collection<IDomainObject<V>>) _runtimeBinding.invokeAccessor(getPojo());
			return Collections.unmodifiableCollection(collection);
		}

		/*
		 * @see de.berlios.rcpviewer.session.IDomainObject.IObjectCollectionReference#addToCollection(de.berlios.rcpviewer.session.IDomainObject)
		 */
		public <Q> void addToCollection(IDomainObject<Q> domainObject) {
			assert _collectionReference.getReferencedClass() == domainObject
					.getDomainClass();
			assert _eReference.isChangeable();
			_runtimeBinding.invokeAddTo(getPojo(), domainObject.getPojo());
			// TODO: ideally the notifyListeners aspect should do this for us?
			// notify _domainObjectListeners
			DomainObjectReferenceEvent event = new DomainObjectReferenceEvent(
					this, getPojo());
			for (IDomainObjectReferenceListener listener : _listeners) {
				listener.collectionAddedTo(event);
			}
		}

		/*
		 * @see de.berlios.rcpviewer.session.IDomainObject.IObjectCollectionReference#removeFromCollection(de.berlios.rcpviewer.session.IDomainObject)
		 */
		public <Q> void removeFromCollection(IDomainObject<Q> domainObject) {
			assert _collectionReference.getReferencedClass() == domainObject
					.getDomainClass();
			assert _eReference.isChangeable();
			_runtimeBinding.invokeRemoveFrom(getPojo(), domainObject.getPojo());
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
		 * @see de.berlios.rcpviewer.session.IDomainObject.IObjectCollectionReference#notifyListeners(java.lang.Object,
		 *      boolean)
		 */
		public void notifyListeners(Object referencedObject, boolean beingAdded) {
			DomainObjectReferenceEvent event = new DomainObjectReferenceEvent(
					this, referencedObject);
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
		private final RuntimeOperationBinding _runtimeBinding;
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

		
		/*
		 * Extended semantics support (move to binding)
		 */
		private final Class<?>[] _parameterTypes;

		
		/**
		 * Will reset the arguments, according to {@link #reset()}.
		 * 
		 * @param eOperation
		 */
		ObjectOperation(final EOperation eOperation) {
			this._eOperation = eOperation;
			this._operation = getDomainClass().getOperation(eOperation);
			_runtimeBinding = (RuntimeOperationBinding) _operation.getBinding();
			
			// extended semantics: move to binding
			EList eParameters = _eOperation.getEParameters();
			_parameterTypes = new Class<?>[eParameters.size()];
			for (int i = 0; i < _parameterTypes.length; i++) {
				EParameter eParameter = (EParameter) eParameters.get(i);
				_parameterTypes[i] = eParameter.getEType().getInstanceClass();
			}
			
			reset();
		}

		public IDomainObject getDomainObject() {
			return (IDomainObject) DomainObject.this; // JAVA_5_FIXME
		}

		public EOperation getEOperation() {
			return _eOperation;
		}

		public Object invokeOperation(final Object[] args) {
			return _runtimeBinding.invokeOperation(getDomainObject().getPojo(),
					args);
		}

		/*
		 * Extended semantics.
		 */
		public IPrerequisites authorizationPrerequisitesFor() {
			IDomainClass dc = getDomainClass();
			IDomainClass.IOperation operation = dc.getOperation(_eOperation);
			RuntimeOperationBinding binding = (RuntimeOperationBinding) operation.getBinding();
			return binding.authorizationPrerequisites();
		}

		/*
		 * Extended semantics.
		 */
		public void setArg(final int position, final Object arg) {
			
			// todo: need to delegate to runtime binding
			if (position < 0 || position >= _parameterTypes.length) {
				throw new IllegalArgumentException(
						"Invalid position: 0 <= position < "
								+ _parameterTypes.length);
			}
			if (arg != null) {
				if (!_runtimeBinding.isAssignableFromIncludingAutoboxing(
						_parameterTypes[position], arg.getClass())) {
					throw new IllegalArgumentException(
							"Incompatible argument for position '" + position
									+ "'; formal='"
									+ _parameterTypes[position].getName()
									+ ", actual='" + arg.getClass().getName()
									+ "'");
				}
			}
			
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
		 * @see de.berlios.rcpviewer.session.IDomainObject.IObjectOperation#removeListener(de.berlios.rcpviewer.session.IDomainObjectOperationListener)
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
			return getEOperation().toString() + ", " + _listeners.size()
					+ " listeners" + ", args=" + getArgs() + ", prereqs="
					+ _currentPrerequisites;
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
	 * @see de.berlios.rcpviewer.session.IDomainObject#addListener(null)
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
	 * @see de.berlios.rcpviewer.session.IDomainObject#removeListener(de.berlios.rcpviewer.session.IDomainObjectListener)
	 */
	public void removeListener(IDomainObjectListener listener) {
		synchronized (_domainObjectListeners) {
			_domainObjectListeners.remove(listener);
		}
	}

}
