package de.berlios.rcpviewer.progmodel.standard;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EOperation;

import de.berlios.rcpviewer.metamodel.DomainObjectAttributeEvent;
import de.berlios.rcpviewer.metamodel.IDomainClass;
import de.berlios.rcpviewer.metamodel.IDomainObject;
import de.berlios.rcpviewer.metamodel.IDomainObjectListener;
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

	public EOperation getEOperationNamed(String operationName) {
		return getDomainClass().getEOperationNamed(operationName);
	}
	
	public void invokeOperation(EOperation operation, final Object[] args) {
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
	 * Session through which this was created.
	 * 
	 * @return
	 */
	public ISession getSession() {
		return session;
	}

}
