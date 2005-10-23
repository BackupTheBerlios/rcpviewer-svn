package de.berlios.rcpviewer.progmodel.extended;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.EReference;

import de.berlios.rcpviewer.domain.EmfAnnotations;
import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.domain.IRuntimeDomainClass;
import de.berlios.rcpviewer.domain.IRuntimeDomainClassAdapter;
import de.berlios.rcpviewer.session.IDomainObject;

public final class ExtendedRuntimeDomainClass<T> extends ExtendedDomainClass<T> 
	implements IRuntimeDomainClassAdapter<T>, IExtendedRuntimeDomainClass<T> {

	private static final Class<IExtendedDomainObject> INTERFACE_CLASS = IExtendedDomainObject.class;
	private static final Class<ExtendedDomainObject> IMPLEMENTATION_CLASS = ExtendedDomainObject.class;
	
	private final ExtendedProgModelSemanticsEmfSerializer serializer = new ExtendedProgModelSemanticsEmfSerializer();

	public ExtendedRuntimeDomainClass(IDomainClass<T> adaptedDomainClass) {
		super(adaptedDomainClass);
	}

	// JAVA_5_FIXME: use covariance
	public IRuntimeDomainClass<T> runtimeAdapts() {
		return (IRuntimeDomainClass<T>)adapts();
	}

	public <V> V getObjectAdapterFor(IDomainObject<T> domainObject, Class<V> objectAdapterClass) {
		if (!isCompatible(objectAdapterClass)) {
			return null;
		}
		try {
			Constructor<V> con = (Constructor<V>)IMPLEMENTATION_CLASS.getConstructor(IDomainObject.class);
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

	/*
	 * @see de.berlios.rcpviewer.domain.IRuntimeDomainClassAdapter#isCompatible(java.lang.Class)
	 */
	public <V> boolean isCompatible(Class<V> objectAdapterClass) {
		return objectAdapterClass == INTERFACE_CLASS;
	}
	
	/*
	 * @see de.berlios.rcpviewer.progmodel.extended.IExtendedRuntimeDomainClass#getAccessorPre(org.eclipse.emf.ecore.EAttribute)
	 */
	public Method getAccessorPre(EAttribute attribute) {
		String accessorPre = serializer.getAttributeAccessorPre(attribute);
		if (accessorPre == null) {
			return null;
		}
		try {
			Method accessorPreMethod = 
				runtimeAdapts().getJavaClass().getMethod(
						accessorPre, new Class[]{});
			return accessorPreMethod;
		} catch (SecurityException ex) {
			// TODO: log?
			return null;
		} catch (NoSuchMethodException ex) {
			// TODO: log?
			return null;
		}
	}

	/*
	 * @see de.berlios.rcpviewer.progmodel.extended.IExtendedRuntimeDomainClass#getMutatorPre(org.eclipse.emf.ecore.EAttribute)
	 */
	public Method getMutatorPre(EAttribute attribute) {
		String mutatorPre = serializer.getAttributeMutatorPre(attribute);
		if (mutatorPre == null) {
			return null;
		}
		Class<?> attributeType = attribute.getEAttributeType().getInstanceClass();
		try {
			Method mutatorPreMethod = 
				runtimeAdapts().getJavaClass().getMethod(
						mutatorPre, new Class[]{attributeType});
			return mutatorPreMethod;
		} catch (SecurityException ex) {
			// TODO: log?
			return null;
		} catch (NoSuchMethodException ex) {
			// TODO: log?
			return null;
		}
	}

	public Method getAccessorPre(EReference reference) {
		String accessorPre = serializer.getReferenceAccessorPre(reference);
		if (accessorPre == null) {
			return null;
		}
		try {
			Method accessorPreMethod = 
				runtimeAdapts().getJavaClass().getMethod(
						accessorPre, new Class[]{});
			return accessorPreMethod;
		} catch (SecurityException ex) {
			// TODO: log?
			return null;
		} catch (NoSuchMethodException ex) {
			// TODO: log?
			return null;
		}
	}

	public Method getMutatorPre(EReference reference) {
		String mutatorPre = serializer.getReferenceMutatorPre(reference);
		if (mutatorPre == null) {
			return null;
		}
		Class<?> referenceType = reference.getEType().getInstanceClass();
		try {
			Method addToPreMethod = 
				runtimeAdapts().getJavaClass().getMethod(
						mutatorPre, new Class[]{referenceType});
			return addToPreMethod;
		} catch (SecurityException ex) {
			// TODO: log?
			return null;
		} catch (NoSuchMethodException ex) {
			// TODO: log?
			return null;
		}
	}

	public Method getAddToPre(EReference reference) {
		String addToPre = serializer.getReferenceAddToPre(reference);
		if (addToPre == null) {
			return null;
		}
		Class<?> referenceType = reference.getEType().getInstanceClass();
		try {
			Method mutatorPreMethod = 
				runtimeAdapts().getJavaClass().getMethod(
						addToPre, new Class[]{referenceType});
			return mutatorPreMethod;
		} catch (SecurityException ex) {
			// TODO: log?
			return null;
		} catch (NoSuchMethodException ex) {
			// TODO: log?
			return null;
		}
	}

	public Method getRemoveFromPre(EReference reference) {
		String removeFromPre =
			serializer.getReferenceRemoveFromPre(reference);
		if (removeFromPre == null) {
			return null;
		}
		Class<?> referenceType = reference.getEType().getInstanceClass();
		try {
			Method removeFromPreMethod = 
				runtimeAdapts().getJavaClass().getMethod(
						removeFromPre, new Class[]{referenceType});
			return removeFromPreMethod;
		} catch (SecurityException ex) {
			// TODO: log?
			return null;
		} catch (NoSuchMethodException ex) {
			// TODO: log?
			return null;
		}
	}

	public Method getInvokePre(EOperation operation) {
		String invokePre = serializer.getOperationPre(operation);
		if (invokePre == null) {
			return null;
		}
		EList eParameters = operation.getEParameters();
		Class<?>[] parameterTypes = new Class<?>[eParameters.size()];
		for(int i=0; i<parameterTypes.length; i++) {
			EParameter eParameter = (EParameter)eParameters.get(i);
			parameterTypes[i] = eParameter.getEType().getInstanceClass();
		}
		try {
			Method invokePreMethod = 
				runtimeAdapts().getJavaClass().getMethod(invokePre, parameterTypes);
			return invokePreMethod;
		} catch (SecurityException ex) {
			// TODO: log?
			return null;
		} catch (NoSuchMethodException ex) {
			// TODO: log?
			return null;
		}
	}

	public Method getInvokeDefaults(EOperation eOperation) {
		String invokeDefaults = serializer.getOperationDefaults(eOperation);
		if (invokeDefaults == null) {
			return null;
		}
		EList eParameters = eOperation.getEParameters();
		Class<?>[] parameterTypes = new Class<?>[eParameters.size()];
		for(int i=0; i<parameterTypes.length; i++) {
			// find the type of this parameter
			EParameter eParameter = (EParameter)eParameters.get(i);
			Class<?> parameterType = eParameter.getEType().getInstanceClass();
			// create a prototype array of this type
			Object argDefaultArray = Array.newInstance(parameterType, 1);
			// now obtain the class of this array of the required type
			Class<?> arrayOfParameterType = argDefaultArray.getClass();
			parameterTypes[i] = arrayOfParameterType;
		}
		try {
			Method invokePreMethod = 
				runtimeAdapts().getJavaClass().getMethod(invokeDefaults, parameterTypes);
			return invokePreMethod;
		} catch (SecurityException ex) {
			// TODO: log?
			return null;
		} catch (NoSuchMethodException ex) {
			// TODO: log?
			return null;
		}
	}
}
