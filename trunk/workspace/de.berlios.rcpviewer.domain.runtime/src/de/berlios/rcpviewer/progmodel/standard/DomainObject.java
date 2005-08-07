package de.berlios.rcpviewer.progmodel.standard;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
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
import de.berlios.rcpviewer.session.ISession;
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
	private boolean _persistent;
	private List<IDomainObjectListener> _domainObjectListeners = new ArrayList<IDomainObjectListener>();
	private ISession _session;
	private WeakHashMap<EAttribute, IAttribute> _attributesByEAttribute = new WeakHashMap<EAttribute, IAttribute>();
	private WeakHashMap<EReference, IReference> _referencesByEReference = new WeakHashMap<EReference, IReference>();
	private WeakHashMap<EOperation, IOperation> _operationsByEOperation = new WeakHashMap<EOperation, IOperation>();
	
	/**
	 * Creates a domain object unattached to any _session.
	 * 
	 * @param _domainClass
	 * @param _pojo
	 */
	public DomainObject(final IRuntimeDomainClass<T> domainClass, final T pojo) {
		this(domainClass, pojo, null);
	}
	
	/**
	 * Creates a domain object attached to the _session.
	 * 
	 * @param _domainClass
	 * @param _pojo
	 */
	public DomainObject(final IRuntimeDomainClass<T> domainClass, final T pojo, final ISession session) {
		this._domainClass = domainClass;
		this._pojo = pojo;
		this._session = session;
	}
	
	public IRuntimeDomainClass<T> getDomainClass() {
		return _domainClass;
	}
	public RuntimeDomainClass<T> getDomainClassImpl() {
		return (RuntimeDomainClass<T>)_domainClass;
	}
	
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

	
	public boolean isPersistent() {
		return _persistent;
	}

	
	public void persist() {
		if (isPersistent()) {
			throw new IllegalStateException("Already persisted.");
		}
		if (getSession() == null) {
			throw new IllegalStateException("Not attached to a _session");
		}
		getSession().persist(this);
		_persistent = true;
	}
	

	public void save() {
		if (!isPersistent()) {
			throw new IllegalStateException("Not yet persisted.");
		}
		if (getSession() == null) {
			throw new IllegalStateException("Not attached to a _session");
		}
		getSession().save(this);
	}

	/**
	 * For the title we just return the POJO's <tt>toString()</tt>.
	 */
	public String title() {
		return _pojo.toString();
	}

	
	public EAttribute getEAttributeNamed(String attributeName) {
		return getDomainClass().getEAttributeNamed(attributeName);
	}

	public EReference getEReferenceNamed(final String referenceName) {
		return getDomainClass().getEReferenceNamed(referenceName);
	}

	public EOperation getEOperationNamed(final String operationName) {
		return getDomainClass().getEOperationNamed(operationName);
	}
	
	/**
	 * Returns listener only because it simplifies test implementation to do so.
	 */
	public <T extends IDomainObjectListener> T addDomainObjectListener(T listener) {
		synchronized(_domainObjectListeners) {
			if (!_domainObjectListeners.contains(listener)) {
				_domainObjectListeners.add(listener);
			}
		}
		return listener;
	}
	public void removeDomainObjectListener(IDomainObjectListener listener) {
		synchronized(_domainObjectListeners) {
			_domainObjectListeners.remove(listener);
		}
	}
	

	
	/**
	 * The {@link ISession} to which this domain object is currently attached.
	 * 
	 * @return
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
	public boolean isAttached() {
		return _session != null;
	}

	private String sessionId;
	/**
	 * The identifier of the {@link ISession} that originally managed this
	 * domain object.
	 * 
	 * <p>
	 * If set to <code>null</code>, then indicates  
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

	public synchronized IReference getReference(final EReference eReference) {
		IReference reference = _referencesByEReference.get(eReference);
		if (reference == null) {
			reference = new Reference(eReference);
			_referencesByEReference.put(eReference, reference);
		}
		return reference;
	}

	public synchronized IOperation getOperation(EOperation eOperation) {
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
		 * Do not instantiate directly, instead use {@link DomainObject#getAttributeNamed(String)}
		 * 
		 * @param eAttribute
		 */
		private Attribute(final EAttribute eAttribute) {
			this._eAttribute = eAttribute;
		}

		public <T> IDomainObject<T> getDomainObject() {
			return (IDomainObject)DomainObject.this; // JAVA_5_FIXME
		}

		public EAttribute getEAttribute() {
			return _eAttribute;
		}

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

		public void set(Object newValue) {
			Method mutatorMethod = getDomainClass().getMutatorFor(_eAttribute);
			if (mutatorMethod == null) {
				throw new UnsupportedOperationException("Mutator method '" + mutatorMethod + "' not accessible / could not be found");
			}
			String mutatorMethodName = mutatorMethod.getName();
			try {
				mutatorMethod.invoke(getDomainObject().getPojo(), newValue);
				
				notifyAttributeListeners(newValue);
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
		public <T extends IDomainObjectAttributeListener> T addDomainObjectAttributeListener(T listener) {
			synchronized(_domainObjectAttributeListeners) {
				if (!_domainObjectAttributeListeners.contains(listener)) {
					_domainObjectAttributeListeners.add(listener);
				}
			}
			return listener;
		}
		public void removeDomainObjectAttributeListener(IDomainObjectAttributeListener listener) {
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
		public void notifyAttributeListeners(Object newValue) {
			DomainObjectAttributeEvent event = 
				new DomainObjectAttributeEvent(getDomainObject(), _eAttribute, newValue);
			for(IDomainObjectAttributeListener listener: _domainObjectAttributeListeners) {
				listener.attributeChanged(event);
			}
		}

	}

	private class Reference implements IDomainObject.IReference {

		private EReference _eReference;
		private List<IDomainObjectReferenceListener> _domainObjectReferenceListeners = 
			new ArrayList<IDomainObjectReferenceListener>();

		/**
		 * Do not instantiate directly, instead use {@link DomainObject#getReferenceNamed(String)}
		 * 
		 * @param eReference
		 */
		private Reference(final EReference eReference) {
			this._eReference = eReference;
		}

		public <T> IDomainObject<T> getDomainObject() {
			return (IDomainObject)DomainObject.this; // JAVA_5_FIXME
		}

		public EReference getEReference() {
			return _eReference;
		}

		public <V> Collection<IDomainObject<V>> getCollection() {
			assert getDomainClass().isMultiple(_eReference);
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
				// notify _domainObjectListeners
				DomainObjectReferenceEvent event = 
					new DomainObjectReferenceEvent(getDomainObject(), _eReference, getPojo());
				for(IDomainObjectReferenceListener listener: _domainObjectReferenceListeners) {
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
				// notify _domainObjectListeners
				DomainObjectReferenceEvent event = 
					new DomainObjectReferenceEvent(getDomainObject(), _eReference, getPojo());
				for(IDomainObjectReferenceListener listener: _domainObjectReferenceListeners) {
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

		
		/**
		 * Returns listener only because it simplifies test implementation to do so.
		 */
		public <T extends IDomainObjectReferenceListener> T addDomainObjectReferenceListener(T listener) {
			synchronized(_domainObjectReferenceListeners) {
				if (!_domainObjectReferenceListeners.contains(listener)) {
					_domainObjectReferenceListeners.add(listener);
				}
			}
			return listener;
		}
		public void removeDomainObjectReferenceListener(IDomainObjectReferenceListener listener) {
			synchronized(_domainObjectReferenceListeners) {
				_domainObjectReferenceListeners.remove(listener);
			}
		}

		/**
		 * public so that it can be invoked by NotifyListenersAspect.
		 * 
		 * @param attribute
		 * @param newValue
		 */
		public void notifyReferenceListeners(Object newValue) {
			DomainObjectReferenceEvent event = 
				new DomainObjectReferenceEvent(getDomainObject(), _eReference, newValue);
			for(IDomainObjectReferenceListener listener: _domainObjectReferenceListeners) {
				listener.referenceChanged(event);
			}
		}

	}

	
	private class Operation implements IDomainObject.IOperation {
		
		private EOperation _eOperation;
		private List<IDomainObjectOperationListener> _domainObjectOperationListeners = 
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
				throw new UnsupportedOperationException("Mutator method '" + operationMethodName + "' not accessible");
			} catch (IllegalAccessException e) {
				throw new UnsupportedOperationException("Could not invoke mutator method '" + operationMethodName + "'", e);
			} catch (InvocationTargetException e) {
				throw new UnsupportedOperationException("Could not invoke mutator method '" + operationMethodName + "'", e);
			}
			
		}

		/**
		 * Returns listener only because it simplifies test implementation to do so.
		 */
		public <T extends IDomainObjectOperationListener> T addDomainObjectOperationListener(T listener) {
			synchronized(_domainObjectOperationListeners) {
				if (!_domainObjectOperationListeners.contains(listener)) {
					_domainObjectOperationListeners.add(listener);
				}
			}
			return listener;
		}
		public void removeDomainObjectOperationListener(IDomainObjectOperationListener listener) {
			synchronized(_domainObjectOperationListeners) {
				_domainObjectOperationListeners.remove(listener);
			}
		}

	}

}
