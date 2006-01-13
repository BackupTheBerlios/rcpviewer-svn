package org.essentialplatform.runtime.shared.domain;

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
import org.essentialplatform.core.domain.Domain;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.domain.IDomainClass.IAttribute;
import org.essentialplatform.core.domain.IDomainClass.IOperation;
import org.essentialplatform.core.domain.IDomainClass.IReference;
import org.essentialplatform.core.domain.adapters.IDomainClassAdapter;
import org.essentialplatform.runtime.shared.domain.bindings.ICollectionReferenceRuntimeBinding;
import org.essentialplatform.runtime.shared.domain.bindings.IObjectCollectionReferenceRuntimeBinding;
import org.essentialplatform.runtime.shared.domain.bindings.IObjectOneToOneReferenceRuntimeBinding;
import org.essentialplatform.runtime.shared.domain.bindings.IObjectOperationRuntimeBinding;
import org.essentialplatform.runtime.shared.domain.bindings.IOneToOneReferenceRuntimeBinding;
import org.essentialplatform.runtime.shared.domain.bindings.IOperationRuntimeBinding;
import org.essentialplatform.runtime.shared.domain.bindings.IReferenceRuntimeBinding;
import org.essentialplatform.runtime.shared.domain.adapters.IRuntimeDomainClassAdapter;
import org.essentialplatform.runtime.shared.domain.bindings.IAttributeRuntimeBinding;
import org.essentialplatform.runtime.shared.domain.bindings.IDomainClassRuntimeBinding;
import org.essentialplatform.runtime.shared.domain.bindings.IDomainObjectRuntimeBinding;
import org.essentialplatform.runtime.shared.domain.bindings.IObjectAttributeRuntimeBinding;
import org.essentialplatform.runtime.shared.session.SessionBinding;

/**
 * Wrapper for a POJO that implements the choreography between the rest of the
 * platform and the pojo itself.
 * 
 * <p>
 * Additionally, knows its {@link IDomainClass} and also its 
 * {@link SessionBinding} (if any) to which it is currently attached.
 * 
 * <p>
 * Implementation note: introduced by {@link PojoAspect}.
 * 
 * @author Dan Haywood
 */
public final class DomainObject<T> implements IDomainObject<T> {

	//////////////////////////////////////////////////////////////////////////
	// Constructor
	//////////////////////////////////////////////////////////////////////////

	/**
	 * Creates a domain object attached to the session.
	 * 
	 * <p>
	 * The domain object will not be attached to any session.
	 *
	 * <p>
	 * On client, invoked by PojoAspect; on server, invoked by ...??
	 * 
	 * <p>
	 * TODO: public only so that PojoAspect can instantiate; need to address.
	 */
	public DomainObject(final T pojo) {
		this._pojo = pojo;
	}

	//////////////////////////////////////////////////////////////////////////
	// SessionBinding, isAttached
	//////////////////////////////////////////////////////////////////////////
	
	/**
	 * Required to be serialized, so not <tt>transient</tt>.
	 */
	private SessionBinding _sessionBinding;
	/*
	 * @see org.essentialplatform.runtime.shared.domain.IDomainObject#getSessionBinding()
	 */
	public SessionBinding getSessionBinding() {
		return _sessionBinding;
	}

	/*
	 * @see org.essentialplatform.runtime.shared.domain.IDomainObject#setSessionBinding(org.essentialplatform.runtime.shared.session.SessionBinding)
	 */
	public void setSessionBinding(SessionBinding sessionBinding) {
		_sessionBinding = sessionBinding;
	}


	/*
	 * @see org.essentialplatform.runtime.shared.domain.IDomainObject#clearSessionBinding()
	 */
	public void clearSessionBinding() {
		_runtimeBinding.assertCanClearSessionBinding();
		_sessionBinding = null;
	}


	public boolean isAttached() {
		return _runtimeBinding.isAttached();
	}


	//////////////////////////////////////////////////////////////////////////
	// DomainClass
	//////////////////////////////////////////////////////////////////////////

	/**
	 * Marked as <tt>transient</tt> so that it is not distributed.
	 */
	private transient IDomainClass _domainClass;
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

	private static <V> IDomainClass lookupDomainClass(final V pojo) {
		Class<?> javaClass = pojo.getClass();
		IDomainClass domainClass = Domain.domainFor(javaClass).lookup(javaClass);
		return domainClass;
	}
	
	
	//////////////////////////////////////////////////////////////////////////
	// Pojo
	//////////////////////////////////////////////////////////////////////////
	
	private final T _pojo;
	/*
	 * @see org.essentialplatform.session.IDomainObject#getPojo()
	 */
	public T getPojo() {
		return _pojo;
	}

	

	//////////////////////////////////////////////////////////////////////////
	// Bindings (class- and instance-level)
	//////////////////////////////////////////////////////////////////////////

	/**
	 * initialized on {@link #init(SessionBinding, PersistState, ResolveState)}
	 * 
	 * <p>
	 * Marked as <tt>transient</tt> because bindings are by definition specific
	 * to a particular runtime environment and so should not be serialized
	 * across the network.
	 */
	private transient IDomainClassRuntimeBinding<T> _runtimeClassBinding;
	/**
	 * initialized on {@link #init(SessionBinding, PersistState, ResolveState)}
	 * 
	 * <p>
	 * Marked as <tt>transient</tt> because bindings are by definition specific
	 * to a particular runtime environment and so should not be serialized
	 * across the network.
	 */
	private transient IDomainObjectRuntimeBinding<T> _runtimeBinding;
	public IDomainObjectRuntimeBinding<T> getBinding() {
		return _runtimeBinding;
	}



	//////////////////////////////////////////////////////////////////////////
	// Handle 
	//////////////////////////////////////////////////////////////////////////
	
	private Handle _handle = null;

	/*
	 * @see org.essentialplatform.runtime.shared.domain.IDomainObject#getHandle()
	 */
	public Handle getHandle() {
		return _handle;
	}

	/*
	 * @see org.essentialplatform.runtime.shared.domain.IDomainObject#assignHandle(org.essentialplatform.runtime.shared.domain.Handle)
	 */
	public void assignHandle(Handle persistenceId) {
		_handle = persistenceId;
	}


	//////////////////////////////////////////////////////////////////////////
	// PersistState, isPersistent (inherit IPersistable) 
	//////////////////////////////////////////////////////////////////////////

	private PersistState _persistState = PersistState.UNKNOWN;

	/*
	 * @see org.essentialplatform.runtime.shared.domain.IDomainObject#isPersistent()
	 */
	public boolean isPersistent() {
		return _persistState.isPersistent();
	}

	/*
	 * @see org.essentialplatform.runtime.shared.persistence.IPersistable#getPersistState()
	 */
	public PersistState getPersistState() {
		return _persistState;
	}

	/*
	 * @see org.essentialplatform.runtime.shared.persistence.IPersistable#nowTransient()
	 */
	public void nowTransient() {
		checkInState(PersistState.PERSISTED);
		_persistState = PersistState.TRANSIENT;
	}

	/*
	 * @see org.essentialplatform.runtime.shared.persistence.IPersistable#nowPersisted()
	 */
	public void nowPersisted() {
		checkInState(PersistState.TRANSIENT);
		_persistState = PersistState.PERSISTED;
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

	//////////////////////////////////////////////////////////////////////////
	// ResolveState (inherit IResolvable) 
	//////////////////////////////////////////////////////////////////////////

	private ResolveState _resolveState = ResolveState.UNKNOWN;

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



	//////////////////////////////////////////////////////////////////////////
	// IObjectXxx getXxx (member hashes) 
	// obtain corresponding object member from class member
	//
	// TODO: need hook method in binding.
	// TODO: key on name (String) rather than EXxxx, and/or make non-transient as an arraylist
	//////////////////////////////////////////////////////////////////////////

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


	/*
	 * @see org.essentialplatform.runtime.shared.domain.IDomainObject#getAttribute(org.essentialplatform.core.domain.IDomainClass.IAttribute)
	 */
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
		IObjectAttributeRuntimeBinding atBinding = attribute.getBinding();
		atBinding.gotAttribute();
		return attribute;
	}

	/**
	 * Returns an {@link IObjectReference} for any EReference, whether it
	 * represents a 1:1 reference or a collection.
	 * 
	 * @param eReference
	 * @return
	 */
	public synchronized IObjectReference getReference(final IDomainClass.IReference iReference) {
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
		if (eReference.isMany()) {
			IObjectCollectionReferenceRuntimeBinding refBinding = 
				(IObjectCollectionReferenceRuntimeBinding)((ObjectCollectionReference)reference).getBinding();
			refBinding.gotReference();
		} else {
			IObjectOneToOneReferenceRuntimeBinding refBinding = 
				(IObjectOneToOneReferenceRuntimeBinding)((ObjectOneToOneReference)reference).getBinding();
			refBinding.gotReference();
		}
		return reference;
	}

	/*
	 * @see org.essentialplatform.runtime.shared.domain.IDomainObject#getOneToOneReference(org.essentialplatform.core.domain.IDomainClass.IReference)
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
		IObjectOneToOneReferenceRuntimeBinding refBinding = 
			(IObjectOneToOneReferenceRuntimeBinding)reference.getBinding();
		refBinding.gotReference();
		return reference;
	}

	/*
	 * @see org.essentialplatform.runtime.shared.domain.IDomainObject#getCollectionReference(org.essentialplatform.core.domain.IDomainClass.IReference)
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
		IObjectCollectionReferenceRuntimeBinding refBinding = 
			(IObjectCollectionReferenceRuntimeBinding)reference.getBinding();
		refBinding.gotReference();
		return reference;
	}

	/*
	 * @see org.essentialplatform.runtime.shared.domain.IDomainObject#getOperation(org.essentialplatform.core.domain.IDomainClass.IOperation)
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
		IObjectOperationRuntimeBinding opBinding = (IObjectOperationRuntimeBinding)operation.getBinding();
		opBinding.gotOperation();
		return operation;
	}


	//////////////////////////////////////////////////////////////////////////
	// getIXxxNamed 
	//////////////////////////////////////////////////////////////////////////

	/*
	 * @see org.essentialplatform.runtime.shared.domain.IDomainObject#getIAttributeNamed(java.lang.String)
	 */
	public IAttribute getIAttributeNamed(String attributeName) {
		return getDomainClass().getIAttributeNamed(attributeName);
	}

	/*
	 * @see org.essentialplatform.runtime.shared.domain.IDomainObject#getIReferenceNamed(java.lang.String)
	 */
	public IReference getIReferenceNamed(final String referenceName) {
		return getDomainClass().getIReferenceNamed(referenceName);
	}

	/*
	 * @see org.essentialplatform.runtime.shared.domain.IDomainObject#getIOperationNamed(java.lang.String)
	 */
	public IOperation getIOperationNamed(final String operationName) {
		return getDomainClass().getIOperationNamed(operationName);
	}


	//////////////////////////////////////////////////////////////////////////
	// Adapters 
	//////////////////////////////////////////////////////////////////////////

	/**
	 * Marked as <tt>transient</tt> so that it is not distributed.
	 */
	private transient Map<Class, Object> _adaptersByClass = new HashMap<Class, Object>();

	/*
	 * @see org.essentialplatform.runtime.shared.domain.IDomainObject#getAdapter(java.lang.Class)
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


	//////////////////////////////////////////////////////////////////////////
	// IObjectXxx member class definitions 
	//////////////////////////////////////////////////////////////////////////


	private class ObjectAttribute implements IDomainObject.IObjectAttribute {
		
		//////////////////////////////////////////////////////////////////////////
		// Constructor 
		//////////////////////////////////////////////////////////////////////////

		/**
		 * Do not instantiate directly, instead use
		 * {@link DomainObject#getAttribute(EAttribute)}
		 * 
		 * @param eAttribute
		 */
		private ObjectAttribute(final EAttribute eAttribute) {
			_eAttribute = eAttribute;
			_attribute = getDomainClass().getIAttribute(eAttribute);
			_runtimeClassBinding = (IAttributeRuntimeBinding) _attribute.getBinding(); // JAVA5_FIXME
			_runtimeBinding = (IObjectAttributeRuntimeBinding)_runtimeClassBinding.getObjectBinding(this);
		}

		//////////////////////////////////////////////////////////////////////////
		// getDomainObject 
		//////////////////////////////////////////////////////////////////////////

		public IDomainObject<?> getDomainObject() {
			return (IDomainObject) DomainObject.this; // JAVA_5_FIXME
		}

		//////////////////////////////////////////////////////////////////////////
		// EAttribute, IAttribute 
		//////////////////////////////////////////////////////////////////////////

		/**
		 * transient since don't serialize backing EMF meta-model
		 */
		private transient EAttribute _eAttribute;
		private final IDomainClass.IAttribute _attribute;
		
		/*
		 * @see org.essentialplatform.runtime.shared.domain.IDomainObject.IObjectAttribute#getAttribute()
		 */
		public IAttribute getAttribute() {
			return _attribute;
		}

		//////////////////////////////////////////////////////////////////////////
		// Bindings 
		//////////////////////////////////////////////////////////////////////////

		/**
		 * Marked as <tt>transient</tt> because bindings are by definition specific
		 * to a particular runtime environment and so should not be serialized
		 * across the network.
		 */
		private transient IAttributeRuntimeBinding _runtimeClassBinding;
		/**
		 * Marked as <tt>transient</tt> because bindings are by definition specific
		 * to a particular runtime environment and so should not be serialized
		 * across the network.
		 */
		private transient IObjectAttributeRuntimeBinding _runtimeBinding;
		public IObjectAttributeRuntimeBinding getBinding() {
			return _runtimeBinding;
		}


		//////////////////////////////////////////////////////////////////////////
		// get, set
		//////////////////////////////////////////////////////////////////////////

		/*
		 * @see org.essentialplatform.runtime.shared.domain.IDomainObject.IObjectAttribute#get()
		 */
		public Object get() {
			return _runtimeClassBinding.invokeAccessor(getDomainObject().getPojo());
		}

		/*
		 * @see org.essentialplatform.runtime.shared.domain.IDomainObject.IObjectAttribute#set(java.lang.Object)
		 */
		public void set(Object newValue) {
			_runtimeClassBinding.invokeMutator(getDomainObject().getPojo(), newValue);
		}



		//////////////////////////////////////////////////////////////////////////
		// toString
		//////////////////////////////////////////////////////////////////////////
		
		@Override
		public String toString() {
			return getAttribute().toString();
		}

	}

	private class ObjectReference implements IDomainObject.IObjectReference {

		//////////////////////////////////////////////////////////////////////////
		// Constructor 
		//////////////////////////////////////////////////////////////////////////

		/**
		 * Do not instantiate directly, instead use
		 * {@link DomainObject#getReferenceNamed(String)}
		 * 
		 * @param eReference
		 */
		private ObjectReference(final EReference eReference) {
			this._eReference = eReference;
			this._reference = getDomainClass().getIReference(eReference);
			this._runtimeClassBinding = (IReferenceRuntimeBinding) _reference.getBinding(); // JAVA5_FIXME
		}


		//////////////////////////////////////////////////////////////////////////
		// getDomainObject 
		//////////////////////////////////////////////////////////////////////////

		/*
		 * @see org.essentialplatform.session.IDomainObject.IObjectReference#getDomainObject()
		 */
		public <Q> IDomainObject<Q> getDomainObject() {
			return (IDomainObject) DomainObject.this; // JAVA_5_FIXME
		}

		//////////////////////////////////////////////////////////////////////////
		// EReference, IReference 
		//////////////////////////////////////////////////////////////////////////

		final transient EReference _eReference;
		final IDomainClass.IReference _reference;
		
		/*
		 * @see org.essentialplatform.session.IDomainObject.IObjectReference#getReference()
		 */
		public IReference getReference() {
			return _reference;
		}


		//////////////////////////////////////////////////////////////////////////
		// Bindings
		// (class-level only; instance-level in the subclasses)
		//////////////////////////////////////////////////////////////////////////
		/**
		 * Marked as <tt>transient</tt> because bindings are by definition specific
		 * to a particular runtime environment and so should not be serialized
		 * across the network.
		 */
		transient IReferenceRuntimeBinding _runtimeClassBinding;

		//////////////////////////////////////////////////////////////////////////
		// ResolveState, IResolvable
		//////////////////////////////////////////////////////////////////////////

		ResolveState _resolveState = ResolveState.UNRESOLVED;

		/*
		 * @see org.essentialplatform.runtime.shared.persistence.IResolvable#getResolveState()
		 */
		public ResolveState getResolveState() {
			return _resolveState;
		}

		/*
		 * @see org.essentialplatform.runtime.shared.persistence.IResolvable#nowResolved()
		 */
		public void nowResolved() {
			checkInState(ResolveState.UNRESOLVED);
			_resolveState = ResolveState.RESOLVED;
		}


		//////////////////////////////////////////////////////////////////////////
		// toString
		//////////////////////////////////////////////////////////////////////////

		@Override
		public String toString() {
			return _eReference.toString();
		}

	}

	private class ObjectOneToOneReference extends ObjectReference implements
			IDomainObject.IObjectOneToOneReference {

		//////////////////////////////////////////////////////////////////////////
		// Constructor
		//////////////////////////////////////////////////////////////////////////

		private ObjectOneToOneReference(final EReference eReference) {
			super(eReference);
			assert !_reference.isMultiple();
			_oneToOneReference = (IDomainClass.IOneToOneReference) _reference;
			_runtimeClassBinding = (IOneToOneReferenceRuntimeBinding) _oneToOneReference.getBinding();
			_runtimeBinding = (IObjectOneToOneReferenceRuntimeBinding)_runtimeClassBinding.getObjectBinding(this);
		}

		//////////////////////////////////////////////////////////////////////////
		// IOneToOneReference, Bindings
		//////////////////////////////////////////////////////////////////////////
		
		private final IDomainClass.IOneToOneReference _oneToOneReference;
		/**
		 * Marked as <tt>transient</tt> because bindings are by definition specific
		 * to a particular runtime environment and so should not be serialized
		 * across the network.
		 */
		private transient IOneToOneReferenceRuntimeBinding _runtimeClassBinding;
		/**
		 * Marked as <tt>transient</tt> because bindings are by definition specific
		 * to a particular runtime environment and so should not be serialized
		 * across the network.
		 */
		private transient IObjectOneToOneReferenceRuntimeBinding _runtimeBinding;
		/*
		 * @see org.essentialplatform.runtime.shared.domain.IDomainObject.IObjectOneToOneReference#getBinding()
		 */
		public IObjectOneToOneReferenceRuntimeBinding getBinding() {
			return _runtimeBinding;
		}


		//////////////////////////////////////////////////////////////////////////
		// get, set
		//////////////////////////////////////////////////////////////////////////

		public <Q> IDomainObject<Q> get() {
			Object referencedObject = _runtimeClassBinding.invokeAccessor(getDomainObject().getPojo());
			if (referencedObject == null) return null;
			if (!(referencedObject instanceof IPojo)) return null;
			IPojo referencedPojo = (IPojo)referencedObject;
			return referencedPojo.domainObject();
		}

		/*
		 * @see org.essentialplatform.runtime.shared.domain.IDomainObject.IObjectOneToOneReference#set(org.essentialplatform.runtime.shared.domain.IDomainObject)
		 */
		public <Q> void set(IDomainObject<Q> domainObject) {
			if (domainObject != null) {
				_runtimeClassBinding.invokeAssociator(getDomainObject().getPojo(), domainObject.getPojo());
				_runtimeBinding.set(domainObject.getPojo());
			} else {
				_runtimeClassBinding.invokeDissociator(getDomainObject().getPojo(), null);
				_runtimeBinding.set(null);
			}
		}

	}

	private class ObjectCollectionReference extends ObjectReference implements
			IDomainObject.IObjectCollectionReference {

		//////////////////////////////////////////////////////////////////////////
		// Constructor
		//////////////////////////////////////////////////////////////////////////

		private ObjectCollectionReference(final EReference eReference) {
			super(eReference);
			assert _reference.isMultiple();
			_collectionReference = (IDomainClass.ICollectionReference) _reference;
			_runtimeClassBinding = (ICollectionReferenceRuntimeBinding) _collectionReference.getBinding();
			_runtimeBinding = (IObjectCollectionReferenceRuntimeBinding)_runtimeClassBinding.getObjectBinding(this);
		}

		//////////////////////////////////////////////////////////////////////////
		// ICollectionReference, Bindings
		//////////////////////////////////////////////////////////////////////////

		private final IDomainClass.ICollectionReference _collectionReference;
		/**
		 * Marked as <tt>transient</tt> because bindings are by definition specific
		 * to a particular runtime environment and so should not be serialized
		 * across the network.
		 */
		private transient ICollectionReferenceRuntimeBinding _runtimeClassBinding;
		/**
		 * Marked as <tt>transient</tt> because bindings are by definition specific
		 * to a particular runtime environment and so should not be serialized
		 * across the network.
		 */
		private transient IObjectCollectionReferenceRuntimeBinding _runtimeBinding;
		/*
		 * @see org.essentialplatform.runtime.shared.domain.IDomainObject.IObjectCollectionReference#getBinding()
		 */
		public IObjectCollectionReferenceRuntimeBinding getBinding() {
			return _runtimeBinding;
		}
		


		//////////////////////////////////////////////////////////////////////////
		// getCollection, addToCollection, removeFromCollection
		//////////////////////////////////////////////////////////////////////////

		public <V> Collection<IDomainObject<V>> getCollection() {
			Collection<IPojo> pojoCollection = 
				(Collection<IPojo>) _runtimeClassBinding.invokeAccessor(getPojo());
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
			_runtimeClassBinding.invokeAssociator(getPojo(), domainObject.getPojo());
			getBinding().addedToCollection();
		}

		/*
		 * @see org.essentialplatform.session.IDomainObject.IObjectCollectionReference#removeFromCollection(org.essentialplatform.session.IDomainObject)
		 */
		public <Q> void removeFromCollection(IDomainObject<Q> domainObject) {
			assert _collectionReference.getReferencedDomainClass() == domainObject
					.getDomainClass();
			assert _eReference.isChangeable();
			_runtimeClassBinding.invokeDissociator(getPojo(), domainObject.getPojo());
			getBinding().removedFromCollection();
		}

		
	}

	private class ObjectOperation implements IDomainObject.IObjectOperation {

		//////////////////////////////////////////////////////////////////////////
		// Constructor
		//////////////////////////////////////////////////////////////////////////

		/**
		 * Will reset the arguments, according to {@link #reset()}.
		 * 
		 * @param eOperation
		 */
		ObjectOperation(final EOperation eOperation) {
			this._eOperation = eOperation;
			this._operation = getDomainClass().getIOperation(eOperation);
			_runtimeClassBinding = (IOperationRuntimeBinding) _operation.getBinding();
			_runtimeBinding = (IObjectOperationRuntimeBinding)_runtimeClassBinding.getObjectBinding(this);
			_runtimeBinding.reset();
		}

		
		//////////////////////////////////////////////////////////////////////////
		// getDomainObject
		//////////////////////////////////////////////////////////////////////////

		public IDomainObject getDomainObject() {
			return (IDomainObject) DomainObject.this; // JAVA_5_FIXME
		}

		//////////////////////////////////////////////////////////////////////////
		// EOperation, IOperation
		//////////////////////////////////////////////////////////////////////////

		private final EOperation _eOperation;
		private final IDomainClass.IOperation _operation;
		public IOperation getOperation() {
			return _operation;
		}
		
		//////////////////////////////////////////////////////////////////////////
		// Bindings (class level, instance level)
		//////////////////////////////////////////////////////////////////////////

		/**
		 * Marked as <tt>transient</tt> because bindings are by definition specific
		 * to a particular runtime environment and so should not be serialized
		 * across the network.
		 */
		private transient IOperationRuntimeBinding _runtimeClassBinding;
		/**
		 * Marked as <tt>transient</tt> because bindings are by definition specific
		 * to a particular runtime environment and so should not be serialized
		 * across the network.
		 */
		private transient IObjectOperationRuntimeBinding _runtimeBinding;
		
		public IObjectOperationRuntimeBinding getBinding() {
			return _runtimeBinding;
		}


		//////////////////////////////////////////////////////////////////////////
		// toString
		//////////////////////////////////////////////////////////////////////////

		@Override
		public String toString() {
			return _eOperation.toString();
		}


	}


	
	//////////////////////////////////////////////////////////////////////////
	// Initialization methods 
	// TODO: need to move to client-side bindings
	//////////////////////////////////////////////////////////////////////////
	
	/**
	 * Initializes the domain object to represent a pojo that cannot be 
	 * persisted, and attaches to the specified session.
	 * 
	 * @param domainClass
	 * @param pojo -
	 *            the pojo that this domain object wraps and represents the
	 *            state of
	 * @param session -
	 *            to attach to
	 */
	public static <V> DomainObject<V> initAsCreatingTransient(final V pojo, final SessionBinding sessionBinding) {
		DomainObject<V> domainObject = (DomainObject<V>) ((IPojo) pojo).domainObject();
		domainObject.init(
			sessionBinding, PersistState.TRANSIENT, ResolveState.RESOLVED);
		return domainObject;
	}

	/**
	 * Initializes the domain object to represent a pojo that will be 
	 * persisted when the transaction commits, and attaches to the specified 
	 * session.
	 * 
	 * @param domainClass
	 * @param pojo -
	 *            the pojo that this domain object wraps and represents the
	 *            state of
	 * @param session -
	 *            to attach to
	 */
	public static <V> DomainObject<V> initAsCreatingPersistent(final V pojo, final SessionBinding sessionBinding) {
		IPojo iPojo = (IPojo)pojo;
	
		DomainObject<V> domainObject = (DomainObject<V>)iPojo.domainObject();
		domainObject.init(
			sessionBinding, PersistState.PERSISTED, ResolveState.RESOLVED);
		return domainObject;
	}

	/**
	 * Initializes the domain object to represent a pojo that has been 
	 * persisted, but not yet resolved, and attaches to the specified session.
	 * 
	 * @param domainClass
	 * @param pojo -
	 *            the pojo that this domain object wraps and represents the
	 *            state of
	 * @param session -
	 *            to attach to
	 */
	public static <V> DomainObject initAsRecreatingPersistent(final V pojo, final SessionBinding sessionBinding) {
		DomainObject<V> domainObject = (DomainObject<V>) ((IPojo) pojo).domainObject();
		domainObject.init(
			sessionBinding, PersistState.PERSISTED, ResolveState.UNRESOLVED);
		return domainObject;
	}

	/**
	 * Initializes the domain object.
	 * @param _domainClass
	 * @param _pojo
	 */
	private void init(final SessionBinding sessionBinding, PersistState persistState, ResolveState resolveState) {
		_sessionBinding = sessionBinding;
		_persistState = persistState;
		_resolveState = resolveState;
		_runtimeClassBinding = (IDomainClassRuntimeBinding<T>)getDomainClass().getBinding();
		_runtimeBinding = _runtimeClassBinding.getObjectBinding(this);
	}

	
}
