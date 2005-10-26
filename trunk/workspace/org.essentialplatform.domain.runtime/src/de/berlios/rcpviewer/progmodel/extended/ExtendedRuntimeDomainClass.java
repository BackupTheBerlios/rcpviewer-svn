package de.berlios.rcpviewer.progmodel.extended;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EReference;

import de.berlios.rcpviewer.domain.AbstractDomainClassAdapter;
import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.domain.IRuntimeDomainClassAdapter;
import de.berlios.rcpviewer.progmodel.standard.ExtendedProgModelSemanticsEmfSerializer;
import de.berlios.rcpviewer.session.IDomainObject;

public final class ExtendedRuntimeDomainClass 
		extends AbstractDomainClassAdapter 
		implements IRuntimeDomainClassAdapter, IExtendedRuntimeDomainClass {

	private static final Class<IExtendedDomainObject> INTERFACE_CLASS = IExtendedDomainObject.class;
	private static final Class<ExtendedDomainObject> IMPLEMENTATION_CLASS = ExtendedDomainObject.class;
	
	private final ExtendedProgModelSemanticsEmfSerializer _extendedSerializer = new ExtendedProgModelSemanticsEmfSerializer();

	public ExtendedRuntimeDomainClass(IDomainClass adaptedDomainClass) {
		super(adaptedDomainClass);
	}

	public <V, T> V getObjectAdapterFor(IDomainObject<T> domainObject, Class<V> objectAdapterClass) {
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
		return _extendedSerializer.getAttributeAccessorPreMethod(attribute);
	}

	/*
	 * @see de.berlios.rcpviewer.progmodel.extended.IExtendedRuntimeDomainClass#getMutatorPre(org.eclipse.emf.ecore.EAttribute)
	 */
	public Method getMutatorPre(EAttribute attribute) {
		return _extendedSerializer.getAttributeMutatorPreMethod(attribute);
	}

	public Method getAccessorPre(EReference reference) {
		return _extendedSerializer.getReferenceAccessorPreMethod(reference);
	}

	public Method getMutatorPre(EReference reference) {
		return _extendedSerializer.getReferenceMutatorPreMethod(reference);
	}

	public Method getAddToPre(EReference reference) {
		return _extendedSerializer.getReferenceAddToPreMethod(reference);
	}

	public Method getRemoveFromPre(EReference reference) {
		return _extendedSerializer.getReferenceRemoveFromPreMethod(reference);
	}

	public Method getInvokePre(EOperation operation) {
		return _extendedSerializer.getOperationPreMethod(operation);
	}

	public Method getInvokeDefaults(EOperation eOperation) {
		return _extendedSerializer.getOperationDefaultsMethod(eOperation);
	}
}
