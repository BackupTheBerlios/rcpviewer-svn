package de.berlios.rcpviewer.progmodel.standard;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Collections;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EReference;

import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.session.DomainObjectAttributeEvent;
import de.berlios.rcpviewer.session.DomainObjectReferenceEvent;
import de.berlios.rcpviewer.session.IDomainObject;
import de.berlios.rcpviewer.session.IDomainObjectListener;
import de.berlios.rcpviewer.session.ISession;

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

	/**
	 * Creates a domain object unattached to any session.
	 * 
	 * @param domainClass
	 * @param pojo
	 */
	public DomainObject(final IDomainClass<T> domainClass, final T pojo) {
		this(domainClass, pojo, null);
	}
	
	/**
	 * Creates a domain object attached to the session.
	 * 
	 * @param domainClass
	 * @param pojo
	 */
	public DomainObject(final IDomainClass<T> domainClass, final T pojo, final ISession session) {
		this.domainClass = domainClass;
		this.pojo = pojo;
		this.session = session;
	}
	
	private IDomainClass<T> domainClass;
	public IDomainClass<T> getDomainClass() {
		return domainClass;
	}
	public DomainClass<T> getDomainClassImpl() {
		return (DomainClass<T>)domainClass;
	}
	
	private final T pojo;
	public T getPojo() {
		return pojo;
	}

	private boolean persistent;
	public boolean isPersistent() {
		return persistent;
	}

	public void persist() {
		if (isPersistent()) {
			throw new IllegalStateException("Already persisted.");
		}
		if (getSession() == null) {
			throw new IllegalStateException("Not attached to a session");
		}
		getSession().persist(this);
		persistent = true;
	}
	
	/**
	 * For the title we just return the POJO's <tt>toString()</tt>.
	 */
	public String title() {
		return pojo.toString();
	}

	
	public EAttribute getEAttributeNamed(String attributeName) {
		return getDomainClass().getEAttributeNamed(attributeName);
	}


	public Object get(EAttribute attribute) {
		Method accessorMethod = getDomainClass().getAccessorFor(attribute);
		if (accessorMethod == null) {
			throw new UnsupportedOperationException("Accesor method '" + accessorMethod + "' not accessible / could not be found");
		}
		String accessorMethodName = accessorMethod.getName();
		try {
			return accessorMethod.invoke(this.getPojo());
		} catch (SecurityException e) {
			throw new UnsupportedOperationException("Accessor method '" + accessorMethodName + "' not accessible", e);
		} catch (IllegalAccessException e) {
			throw new UnsupportedOperationException("Could not invoke accessor method '" + accessorMethodName + "'", e);
		} catch (InvocationTargetException e) {
			throw new UnsupportedOperationException("Could not invoke accessor method '" + accessorMethodName + "'", e);
		}
	}

	public void set(EAttribute attribute, Object newValue) throws IllegalArgumentException {
		Method mutatorMethod = getDomainClass().getMutatorFor(attribute);
		if (mutatorMethod == null) {
			throw new UnsupportedOperationException("Mutator method '" + mutatorMethod + "' not accessible / could not be found");
		}
		String mutatorMethodName = mutatorMethod.getName();
		try {
			mutatorMethod.invoke(this.getPojo(), newValue);
			// notify listeners
			DomainObjectAttributeEvent event = 
				new DomainObjectAttributeEvent(this, attribute, newValue);
			for(IDomainObjectListener listener: listeners) {
				listener.attributeChanged(event);
			}
		} catch (SecurityException e) {
			throw new UnsupportedOperationException("Mutator method '" + mutatorMethodName + "' not accessible");
		} catch (IllegalAccessException e) {
			throw new UnsupportedOperationException("Could not invoke mutator method '" + mutatorMethodName + "'", e);
		} catch (InvocationTargetException e) {
			throw new UnsupportedOperationException("Could not invoke mutator method '" + mutatorMethodName + "'", e);
		}
	}

	public EOperation getEOperationNamed(final String operationName) {
		return getDomainClass().getEOperationNamed(operationName);
	}
	
	public void invokeOperation(final EOperation operation, final Object[] args) {
		Method operationMethod = getDomainClass().getInvokerFor(operation);
		if (operationMethod == null) {
			throw new UnsupportedOperationException("Operation method '" + operationMethod + "' not accessible / could not be found");
		}
		String operationMethodName = operationMethod.getName();
		try {
			operationMethod.invoke(this.getPojo(), args);
		} catch (SecurityException e) {
			throw new UnsupportedOperationException("Mutator method '" + operationMethodName + "' not accessible");
		} catch (IllegalAccessException e) {
			throw new UnsupportedOperationException("Could not invoke mutator method '" + operationMethodName + "'", e);
		} catch (InvocationTargetException e) {
			throw new UnsupportedOperationException("Could not invoke mutator method '" + operationMethodName + "'", e);
		}
		
	}

	public EReference getEReferenceNamed(final String referenceName) {
		return getDomainClass().getEReferenceNamed(referenceName);
	}

	public <V> Collection<IDomainObject<V>> getCollection(EReference collectionReference) {
		assert getDomainClass().isMultiple(collectionReference);
		Method collectionAccessorMethod = getDomainClass().getAccessorFor(collectionReference);
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
	
	public <Q> void addToCollection(EReference collectionReference, IDomainObject<Q> domainObject) {
		assert getDomainClass().isMultiple(collectionReference);
		assert getDomainClass().getReferencedClass(collectionReference) == 
			domainObject.getDomainClass();
		Method collectionAssociatorMethod = getDomainClass().getAssociatorFor(collectionReference);
		try {
			collectionAssociatorMethod.invoke(getPojo(), new Object[]{domainObject.getPojo()});
			// notify listeners
			DomainObjectReferenceEvent event = 
				new DomainObjectReferenceEvent(this, collectionReference, getPojo());
			for(IDomainObjectListener listener: listeners) {
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
	public <Q> void removeFromCollection(EReference collectionReference, IDomainObject<Q> domainObject) {
		assert getDomainClass().isMultiple(collectionReference);
		assert getDomainClass().getReferencedClass(collectionReference) == 
			domainObject.getDomainClass();
		Method collectionDissociatorMethod = getDomainClass().getDissociatorFor(collectionReference);
		try {
			collectionDissociatorMethod.invoke(getPojo(), new Object[]{domainObject.getPojo()});
			// notify listeners
			DomainObjectReferenceEvent event = 
				new DomainObjectReferenceEvent(this, collectionReference, getPojo());
			for(IDomainObjectListener listener: listeners) {
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

	private List<IDomainObjectListener> listeners = new ArrayList<IDomainObjectListener>();
	/**
	 * Returns listener only because it simplifies test implementation to do so.
	 */
	public <T extends IDomainObjectListener> T addDomainObjectListener(T listener) {
		synchronized(listeners) {
			if (!listeners.contains(listener)) {
				listeners.add(listener);
			}
		}
		return listener;
	}
	public void removeDomainObjectListener(IDomainObjectListener listener) {
		synchronized(listeners) {
			listeners.remove(listener);
		}
	}
	

	private ISession session;
	
	/**
	 * The {@link ISession} to which this domain object is currently attached.
	 * 
	 * @return
	 */
	public ISession getSession() {
		return session;
	}

	/**
	 * Ensures that the session id is compatible.
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
					"(session.id = '" + session.getId() + "', " +
					"this.sessionId = '" + this.getSessionId() + "')");
			}
		}
		this.session = session;
	}
	public void detached() {
		this.session = null;
	}
	public boolean isAttached() {
		return session != null;
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
			throw new IllegalStateException("Cannot clear session id when attached to session");
		}
		sessionId = null;
	}

}
