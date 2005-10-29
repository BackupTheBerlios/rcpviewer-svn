package de.berlios.rcpviewer.domain;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.EReference;

import de.berlios.rcpviewer.domain.EmfAnnotations;
import de.berlios.rcpviewer.progmodel.extended.AssignmentType;
import de.berlios.rcpviewer.progmodel.extended.Id;
import de.berlios.rcpviewer.progmodel.extended.Named;
import de.berlios.rcpviewer.progmodel.standard.EssentialProgModelExtendedSemanticsConstants;

/**
 * Serializes and deserializes semantics for a programming model
 * to and from the EMF model.
 *  
 * @author Dan Haywood
 */
public abstract class AbstractProgModelSemanticsEmfSerializer {

	protected final EmfAnnotations _emfAnnotations = new EmfAnnotations();

	public AbstractProgModelSemanticsEmfSerializer() {
	}

	// helper methods
	
	protected Class<?>[] parameterTypesFor(EOperation eOperation) {
		EList eParameterList = eOperation.getEParameters();
		Class<?>[] parameterTypes = new Class[eParameterList.size()];
		int i=0;
		for(EParameter eParameter: (List<EParameter>)eParameterList ) {
			parameterTypes[i++] = eParameter.getEType().getInstanceClass();
		}
		return parameterTypes;
	}
	protected Class<?> typeFor(EAttribute attribute) {
		return attribute.getEType().getInstanceClass();
	}
	protected Class<?>[] typeAsArrayFor(EAttribute attribute) {
		return new Class<?>[] { typeFor(attribute) };
	}
	protected Class<?> typeFor(EReference reference) {
		return reference.getEType().getInstanceClass();
	}
	protected Class<?>[] typeAsArrayFor(EReference reference) {
		return new Class<?>[] { typeFor(reference) };
	}
	protected Class<?> javaClassFor(final EAttribute attribute) {
		EClass eClass = attribute.getEContainingClass();
		return eClass.getInstanceClass();
	}
	protected Class<?> javaClassFor(final EOperation operation) {
		EClass eClass = operation.getEContainingClass();
		return eClass.getInstanceClass();
	}
	protected Class<?> javaClassFor(final EReference reference) {
		EClass eClass = reference.getEContainingClass();
		return eClass.getInstanceClass();
	}
	protected Method findMethod(Class<?> javaClass, final String methodName, Class<?>[] parameterTypes) {
		if (javaClass == null || methodName == null) {
			return null;
		}
		try {
			return javaClass.getMethod(methodName, parameterTypes);
		} catch (SecurityException ex) {
			return null;
		} catch (NoSuchMethodException ex) {
			return null;
		}
	}
	protected Method findMethod(Class<?> javaClass, final String methodName) {
		return findMethod(javaClass, methodName, new Class[]{});
	}

}
