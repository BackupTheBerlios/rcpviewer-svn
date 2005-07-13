package de.berlios.rcpviewer.progmodel.extended;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.eclipse.emf.ecore.EAttribute;

import de.berlios.rcpviewer.domain.EmfFacade;
import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.domain.IRuntimeDomainClass;
import de.berlios.rcpviewer.domain.IRuntimeDomainClassAdapter;
import de.berlios.rcpviewer.session.IDomainObject;

public final class ExtendedRuntimeDomainClass<T> extends ExtendedDomainClass<T> 
	implements IRuntimeDomainClassAdapter<T> {

	public ExtendedRuntimeDomainClass(IDomainClass<T> adaptedDomainClass) {
		super(adaptedDomainClass);
	}

	private EmfFacade emf = new EmfFacade();

// think this code is not needed - it clearly was never finished -- dan
//	public IDomainObjectAdapter<T> adapterFor(IDomainObject<T> domainObject) {
//		IRuntimeDomainClass<T> rdc = domainObject.getDomainClass();
//		List<IDomainClassAdapter> adapters = rdc.getAdapters();
//		// TODO Auto-generated method stub
//		return null;
//	}
	
	// TODO: use covariance on adapts()
	public IRuntimeDomainClass<T> runtimeAdapts() {
		return (IRuntimeDomainClass<T>)adapts();
	}

	public <V> V getObjectAdapterFor(IDomainObject<T> domainObject, Class<V> objectAdapterClass) {
		if (!isCompatible(objectAdapterClass)) {
			return null;
		}
		try {
			Constructor<V> con = objectAdapterClass.getConstructor(IDomainObject.class);
			return con.newInstance(domainObject);
		} catch (SecurityException ex) {
			// TODO log?
			ex.printStackTrace();
			return null;
		} catch (NoSuchMethodException ex) {
			// TODO log? - represents an error in coding the object adapter.
			ex.printStackTrace();
			return null;
		} catch (IllegalArgumentException ex) {
			// TODO log? - represents an error in coding the object adapter.
			ex.printStackTrace();
			return null;
		} catch (InstantiationException ex) {
			// TODO log? - represents an error in coding the object adapter.
			ex.printStackTrace();
			return null;
		} catch (IllegalAccessException ex) {
			// TODO log?
			ex.printStackTrace();
			return null;
		} catch (InvocationTargetException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
		return null;
	}

	public <V> boolean isCompatible(Class<V> objectAdapterClass) {
		return objectAdapterClass == ExtendedDomainObject.class;
	}
	
	public Method getAttributePre(EAttribute eAttribute) {
		String attributePre = 
			emf.getAnnotationDetail(
					emf.methodNamesAnnotationFor(eAttribute), 
					ExtendedProgModelConstants.ANNOTATION_ATTRIBUTE_PRECONDITION_METHOD_NAME_KEY);
		if (attributePre == null) {
			return null;
		}
		try {
			Method attributePreMethod = 
				runtimeAdapts().getJavaClass().getMethod(
						attributePre, new Class[]{});
			return attributePreMethod;
		} catch (SecurityException ex) {
			// TODO: log?
			return null;
		} catch (NoSuchMethodException ex) {
			// TODO: log?
			return null;
		}
	}
	

}
