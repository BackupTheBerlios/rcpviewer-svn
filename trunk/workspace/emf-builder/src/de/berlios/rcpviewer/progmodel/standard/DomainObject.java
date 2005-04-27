package de.berlios.rcpviewer.progmodel.standard;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EcoreFactory;

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

	public DomainObject(final DomainClass<T> domainClass, final T pojo) {
		this.domainClass = domainClass;
		this.pojo = pojo;
	}
	
	private final DomainClass<T> domainClass;
	public IDomainClass<T> getDomainClass() {
		return (IDomainClass<T>)domainClass;
	}
	public DomainClass<T> getDomainClassImpl() {
		return domainClass;
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

	EAnnotation methodAnnotationFor(EModelElement eModelElement) {
		return getDomainClassImpl().methodAnnotationFor(eModelElement);
	}
	
	EAnnotation putMethodNameIn(EAnnotation eAnnotation, String methodKey, String methodName) {
		return getDomainClassImpl().putMethodNameIn(eAnnotation, methodKey, methodName);
	}
	
	String getMethodNameFrom(EAnnotation eAnnotation, String methodKey) {
		return getDomainClassImpl().getMethodNameFrom(eAnnotation, methodKey);
	}
	

	public Object get(EAttribute nameAttribute) {
		String accessorMethodName = 
			getMethodNameFrom(
					methodAnnotationFor(nameAttribute), 
					Constants.ANNOTATION_ATTRIBUTE_ACCESSOR_METHOD_NAME_KEY);
		Method accessorMethod;
		try {
			accessorMethod = this.getDomainClass().getJavaClass().getMethod(accessorMethodName, new Class[]{});
			return accessorMethod.invoke(this.getPojo());
		} catch (SecurityException e) {
			throw new UnsupportedOperationException("Accessor method '" + accessorMethodName + "' not accessible", e);
		} catch (NoSuchMethodException e) {
			throw new UnsupportedOperationException("Accessor method '" + accessorMethodName + "' could not be found", e);
		} catch (IllegalAccessException e) {
			throw new UnsupportedOperationException("Could not invoke accessor method '" + accessorMethodName + "'", e);
		} catch (InvocationTargetException e) {
			throw new UnsupportedOperationException("Could not invoke accessor method '" + accessorMethodName + "'", e);
		}
	}

	public void set(EAttribute nameAttribute, Object newValue) throws IllegalArgumentException {
		String mutatorMethodName = 
			getMethodNameFrom(
					methodAnnotationFor(nameAttribute), 
					Constants.ANNOTATION_ATTRIBUTE_MUTATOR_METHOD_NAME_KEY);
		EDataType dataType = (EDataType)nameAttribute.getEType();
		
		Method mutatorMethod;
		try {
			mutatorMethod = this.getDomainClass().getJavaClass().getMethod(mutatorMethodName, new Class[]{dataType.getInstanceClass()});
			mutatorMethod.invoke(this.getPojo(), newValue);
		} catch (SecurityException e) {
			throw new UnsupportedOperationException("Accessor method '" + mutatorMethodName + "' not accessible", e);
		} catch (NoSuchMethodException e) {
			throw new UnsupportedOperationException("Accessor method '" + mutatorMethodName + "' could not be found", e);
		} catch (IllegalAccessException e) {
			throw new UnsupportedOperationException("Could not invoke accessor method '" + mutatorMethodName + "'", e);
		} catch (InvocationTargetException e) {
			throw new UnsupportedOperationException("Could not invoke accessor method '" + mutatorMethodName + "'", e);
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
