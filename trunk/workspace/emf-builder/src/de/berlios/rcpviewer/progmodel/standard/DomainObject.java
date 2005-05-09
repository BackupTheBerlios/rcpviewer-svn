package de.berlios.rcpviewer.progmodel.standard;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EModelElement;

import de.berlios.rcpviewer.metamodel.IDomainClass;
import de.berlios.rcpviewer.metamodel.IDomainObject;
import de.berlios.rcpviewer.session.*;

/**
 * Wrapper for a POJO that also knows its {@link IDomainClass}.
 * 
 * <p>
 * Implementation note: created by {@link DomainAspect} (perthis aspect for 
 * pojos).
 * 
 * @author Dan Haywood
 */
public final class DomainObject<T> implements IDomainObject /*, ISessionAware */ {

	public DomainObject(final IDomainClass<T> domainClass, final T pojo) {
		this.domainClass = domainClass;
		this.pojo = pojo;
	}
	
	private final IDomainClass<T> domainClass;
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
		} catch (SecurityException e) {
			throw new UnsupportedOperationException("Mutator method '" + mutatorMethodName + "' not accessible");
		} catch (IllegalAccessException e) {
			throw new UnsupportedOperationException("Could not invoke mutator method '" + mutatorMethodName + "'", e);
		} catch (InvocationTargetException e) {
			throw new UnsupportedOperationException("Could not invoke mutator method '" + mutatorMethodName + "'", e);
		}
	}


	// DEPENDENCY INJECTION START
	
	private ISession session;
	public ISession getSession() {
		return de.berlios.rcpviewer.session.local.Session.instance();
	}
	public void setSession(ISession session) {
		this.session = session;
	}

	// DEPENDENCY INJECTION END

}
