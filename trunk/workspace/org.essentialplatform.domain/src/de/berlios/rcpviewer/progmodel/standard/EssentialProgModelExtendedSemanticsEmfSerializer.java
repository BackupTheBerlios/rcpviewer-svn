package de.berlios.rcpviewer.progmodel.standard;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.EReference;

import de.berlios.rcpviewer.domain.AbstractProgModelSemanticsEmfSerializer;
import de.berlios.rcpviewer.progmodel.extended.*;

/**
 * Serializes and deserializes semantics for the extended programming model
 * to and from the EMF model.
 *  
 * @author Dan Haywood
 */
public final class EssentialProgModelExtendedSemanticsEmfSerializer extends AbstractProgModelSemanticsEmfSerializer {

	// any model element
	public Optional getOptional(EModelElement modelElement) {
		Map<String,String> attributeDetails = 
			_emfAnnotations.getAnnotationDetails(modelElement, EssentialProgModelExtendedSemanticsConstants.ANNOTATION_ELEMENT);
		if (attributeDetails.get(EssentialProgModelExtendedSemanticsConstants.ANNOTATION_ELEMENT_OPTIONAL_KEY) == null) {
			return null;
		}
		return Optional.Factory.create();
	}
	public void setOptional(EModelElement modelElement, Optional optional) {
		if (optional == null) return;
		_emfAnnotations.putAnnotationDetails(modelElement,
				EssentialProgModelExtendedSemanticsConstants.ANNOTATION_ELEMENT, 
				EssentialProgModelExtendedSemanticsConstants.ANNOTATION_ELEMENT_OPTIONAL_KEY, 
				"true");
	}
	
	public MinLengthOf getMinLengthOf(EModelElement modelElement) {
		Map<String,String> attributeDetails = 
			_emfAnnotations.getAnnotationDetails(modelElement, EssentialProgModelExtendedSemanticsConstants.ANNOTATION_ELEMENT);
		String valueStr = attributeDetails.get(EssentialProgModelExtendedSemanticsConstants.ANNOTATION_ELEMENT_MIN_LENGTH_OF_KEY);
		if (valueStr == null) {
			return null;
		}
		final int value = Integer.parseInt(valueStr);
		return MinLengthOf.Factory.create(value);
	}
	public void setMinLengthOf(EModelElement modelElement, MinLengthOf minLengthOf) {
		if (minLengthOf == null) return;
		_emfAnnotations.putAnnotationDetails(modelElement,
				EssentialProgModelExtendedSemanticsConstants.ANNOTATION_ELEMENT, 
				EssentialProgModelExtendedSemanticsConstants.ANNOTATION_ELEMENT_MIN_LENGTH_OF_KEY, 
				"" + minLengthOf.value());
	}
	
	public MaxLengthOf getMaxLengthOf(EModelElement modelElement) {
		Map<String,String> attributeDetails = 
			_emfAnnotations.getAnnotationDetails(modelElement, EssentialProgModelExtendedSemanticsConstants.ANNOTATION_ELEMENT);
		String valueStr = attributeDetails.get(EssentialProgModelExtendedSemanticsConstants.ANNOTATION_ELEMENT_MAX_LENGTH_OF_KEY);
		if (valueStr == null) {
			return null;
		}
		final int value = Integer.parseInt(valueStr);
		return MaxLengthOf.Factory.create(value);
	}
	public void setMaxLengthOf(EModelElement modelElement, MaxLengthOf maxLengthOf) {
		if (maxLengthOf == null) return;
		_emfAnnotations.putAnnotationDetails(modelElement,
				EssentialProgModelExtendedSemanticsConstants.ANNOTATION_ELEMENT, 
				EssentialProgModelExtendedSemanticsConstants.ANNOTATION_ELEMENT_MAX_LENGTH_OF_KEY, 
				"" + maxLengthOf.value());
	}
	
	public FieldLengthOf getFieldLengthOf(EModelElement modelElement) {
		Map<String,String> attributeDetails = 
			_emfAnnotations.getAnnotationDetails(modelElement, EssentialProgModelExtendedSemanticsConstants.ANNOTATION_ELEMENT);
		String valueStr = attributeDetails.get(EssentialProgModelExtendedSemanticsConstants.ANNOTATION_ELEMENT_FIELD_LENGTH_OF_KEY);
		if (valueStr == null) {
			return null;
		}
		final int value = Integer.parseInt(valueStr);
		return FieldLengthOf.Factory.create(value);
	}
	public void setFieldLengthOf(EModelElement modelElement, FieldLengthOf fieldLengthOf) {
		if (fieldLengthOf == null) return;
		_emfAnnotations.putAnnotationDetails(modelElement,
				EssentialProgModelExtendedSemanticsConstants.ANNOTATION_ELEMENT, 
				EssentialProgModelExtendedSemanticsConstants.ANNOTATION_ELEMENT_FIELD_LENGTH_OF_KEY, 
				"" + fieldLengthOf.value());
	}

	// class

	public Lifecycle getClassLifecycle(EClass eClass) {
		Map<String,String> attributeDetails = 
			_emfAnnotations.getAnnotationDetails(eClass, EssentialProgModelExtendedSemanticsConstants.ANNOTATION_CLASS);
		String instantiableStr = attributeDetails.get(EssentialProgModelExtendedSemanticsConstants.ANNOTATION_CLASS_INSTANTIABLE_KEY);
		if (instantiableStr == null) {
			return null;
		}
		final boolean instantiable = Boolean.valueOf(instantiableStr);
		final boolean searchable = Boolean.valueOf(attributeDetails.get(EssentialProgModelExtendedSemanticsConstants.ANNOTATION_CLASS_SEARCHABLE_KEY));
		final boolean saveable = Boolean.valueOf(attributeDetails.get(EssentialProgModelExtendedSemanticsConstants.ANNOTATION_CLASS_SAVEABLE_KEY));
		return Lifecycle.Factory.create(searchable, instantiable, saveable);
	}
	public void setClassLifecycle(EClass eClass, Lifecycle lifecycle) {
		if (lifecycle == null) return;
		_emfAnnotations.putAnnotationDetails(eClass, 
				EssentialProgModelExtendedSemanticsConstants.ANNOTATION_CLASS, 
				EssentialProgModelExtendedSemanticsConstants.ANNOTATION_CLASS_SEARCHABLE_KEY, 
				""+lifecycle.searchable());	
		_emfAnnotations.putAnnotationDetails(eClass, 
				EssentialProgModelExtendedSemanticsConstants.ANNOTATION_CLASS, 
				EssentialProgModelExtendedSemanticsConstants.ANNOTATION_CLASS_INSTANTIABLE_KEY, 
				""+lifecycle.instantiable());	
		_emfAnnotations.putAnnotationDetails(eClass, 
				EssentialProgModelExtendedSemanticsConstants.ANNOTATION_CLASS, 
				EssentialProgModelExtendedSemanticsConstants.ANNOTATION_CLASS_SAVEABLE_KEY, 
				""+lifecycle.saveable());	
	}
	
	public ImmutableOncePersisted getClassImmutableOncePersisted(EClass eClass) {
		Map<String,String> attributeDetails = 
			_emfAnnotations.getAnnotationDetails(eClass, EssentialProgModelExtendedSemanticsConstants.ANNOTATION_ATTRIBUTE);
		if (attributeDetails.get(EssentialProgModelExtendedSemanticsConstants.ANNOTATION_CLASS_IMMUTABLE_ONCE_PERSISTED_KEY) == null) {
			return null;
		}
		return ImmutableOncePersisted.Factory.create(false);
	}
	public void setClassImmutableOncePersisted(EClass eClass, ImmutableOncePersisted immutableOncePersisted) {
		if (immutableOncePersisted== null) return;
		_emfAnnotations.putAnnotationDetails(eClass,
				EssentialProgModelExtendedSemanticsConstants.ANNOTATION_CLASS, 
				EssentialProgModelExtendedSemanticsConstants.ANNOTATION_CLASS_IMMUTABLE_ONCE_PERSISTED_KEY, 
				"true");
	}

	
	// attributes
	/**
	 * There is no direct getter because it is accessed instead using
	 * {@link #getAttributeAnnotationInt(EAttribute, String)}.
	 * 
	 * @param attribute
	 * @param relativeOrder
	 */
	public void setAttributeRelativeOrder(EAttribute attribute, RelativeOrder relativeOrder) {
		if (relativeOrder == null) return;
		_emfAnnotations.putAnnotationDetails(
				attribute,
				EssentialProgModelExtendedSemanticsConstants.ANNOTATION_ATTRIBUTE, 
				EssentialProgModelExtendedSemanticsConstants.ANNOTATION_ATTRIBUTE_RELATIVE_ORDER_KEY, 
				"" + relativeOrder.value());
	}
	
	public Id getAttributeId(EAttribute attribute) {
		Map<String,String> attributeDetails = 
			_emfAnnotations.getAnnotationDetails(attribute, EssentialProgModelExtendedSemanticsConstants.ANNOTATION_ATTRIBUTE);
		String valueStr = attributeDetails.get(EssentialProgModelExtendedSemanticsConstants.ANNOTATION_ATTRIBUTE_ID_VALUE);
		if (valueStr == null) {
			return null;
		}
		String assignedByStr = attributeDetails.get(EssentialProgModelExtendedSemanticsConstants.ANNOTATION_ATTRIBUTE_ID_ASSIGNED_BY);
		final int value = Integer.parseInt(valueStr);
		final AssignmentType assignedBy = AssignmentType.valueOf(assignedByStr);
		return Id.Factory.create(value, assignedBy);
	}
	public void setAttributeId(EAttribute attribute, Id id) {
		if (id == null) return;
		_emfAnnotations.putAnnotationDetails(attribute,
				EssentialProgModelExtendedSemanticsConstants.ANNOTATION_ATTRIBUTE, 
				EssentialProgModelExtendedSemanticsConstants.ANNOTATION_ATTRIBUTE_ID_VALUE, 
				"" + id.value());
		_emfAnnotations.putAnnotationDetails(attribute,
				EssentialProgModelExtendedSemanticsConstants.ANNOTATION_ATTRIBUTE, 
				EssentialProgModelExtendedSemanticsConstants.ANNOTATION_ATTRIBUTE_ID_ASSIGNED_BY, 
				"" + id.assignedBy().name());
	}
	
	public Invisible getAttributeInvisible(EAttribute attribute) {
		Map<String,String> attributeDetails = 
			_emfAnnotations.getAnnotationDetails(attribute, EssentialProgModelExtendedSemanticsConstants.ANNOTATION_ATTRIBUTE);
		if (attributeDetails.get(EssentialProgModelExtendedSemanticsConstants.ANNOTATION_ATTRIBUTE_INVISIBLE_KEY) == null) {
			return null;
		}
		return Invisible.Factory.create();
	}
	public void setAttributeInvisible(EAttribute attribute, Invisible invisible) {
		if (invisible == null) return; 
		_emfAnnotations.putAnnotationDetails(attribute,
				EssentialProgModelExtendedSemanticsConstants.ANNOTATION_ATTRIBUTE, 
				EssentialProgModelExtendedSemanticsConstants.ANNOTATION_ATTRIBUTE_INVISIBLE_KEY, 
				"true");
	}
	
	public BusinessKey getAttributeBusinessKey(EAttribute attribute) {
		Map<String,String> attributeDetails = 
			_emfAnnotations.getAnnotationDetails(attribute, EssentialProgModelExtendedSemanticsConstants.ANNOTATION_ATTRIBUTE);
		final String name = attributeDetails.get(EssentialProgModelExtendedSemanticsConstants.ANNOTATION_ATTRIBUTE_BUSINESS_KEY_NAME_KEY);
		if (name == null) {
			return null;
		}
		String posStr = attributeDetails.get(EssentialProgModelExtendedSemanticsConstants.ANNOTATION_ATTRIBUTE_BUSINESS_KEY_POS_KEY);
		final int pos = Integer.parseInt(posStr);
		return BusinessKey.Factory.create(name, pos);
	}
	public void setAttributeBusinessKey(EAttribute attribute, BusinessKey businessKey) {
		if (businessKey == null) return;
		_emfAnnotations.putAnnotationDetails(attribute,
				EssentialProgModelExtendedSemanticsConstants.ANNOTATION_ATTRIBUTE, 
				EssentialProgModelExtendedSemanticsConstants.ANNOTATION_ATTRIBUTE_BUSINESS_KEY_NAME_KEY, 
				businessKey.name());
		_emfAnnotations.putAnnotationDetails(attribute,
				EssentialProgModelExtendedSemanticsConstants.ANNOTATION_ATTRIBUTE, 
				EssentialProgModelExtendedSemanticsConstants.ANNOTATION_ATTRIBUTE_BUSINESS_KEY_POS_KEY, 
				""+businessKey.pos());
	}
	
	public Mask getAttributeMask(EAttribute attribute) {
		Map<String,String> attributeDetails = 
			_emfAnnotations.getAnnotationDetails(attribute, EssentialProgModelExtendedSemanticsConstants.ANNOTATION_ATTRIBUTE);
		final String value = attributeDetails.get(EssentialProgModelExtendedSemanticsConstants.ANNOTATION_ATTRIBUTE_MASK_KEY);
		if (value == null) {
			return null;
		}
		return Mask.Factory.create(value);
	}
	public void setAttributeMask(EAttribute attribute, Mask mask) {
		if (mask == null) return;
		_emfAnnotations.putAnnotationDetails(attribute,
				EssentialProgModelExtendedSemanticsConstants.ANNOTATION_ATTRIBUTE, 
				EssentialProgModelExtendedSemanticsConstants.ANNOTATION_ATTRIBUTE_MASK_KEY, 
				mask.value());
	}
	
	public Regex getAttributeRegex(EAttribute attribute) {
		Map<String,String> attributeDetails = 
			_emfAnnotations.getAnnotationDetails(attribute, EssentialProgModelExtendedSemanticsConstants.ANNOTATION_ATTRIBUTE);
		final String value = attributeDetails.get(EssentialProgModelExtendedSemanticsConstants.ANNOTATION_ATTRIBUTE_REGEX_KEY);
		if (value == null) {
			return null;
		}
		return Regex.Factory.create(value);
	}
	public void setAttributeRegex(EAttribute attribute, Regex regex) {
		if (regex== null) return;
		_emfAnnotations.putAnnotationDetails(attribute,
				EssentialProgModelExtendedSemanticsConstants.ANNOTATION_ATTRIBUTE, 
				EssentialProgModelExtendedSemanticsConstants.ANNOTATION_ATTRIBUTE_REGEX_KEY, 
				regex.value());
	}
	
	public ImmutableOncePersisted getAttributeImmutableOncePersisted(EAttribute attribute) {
		Map<String,String> attributeDetails = 
			_emfAnnotations.getAnnotationDetails(attribute, EssentialProgModelExtendedSemanticsConstants.ANNOTATION_ATTRIBUTE);
		if (attributeDetails.get(EssentialProgModelExtendedSemanticsConstants.ANNOTATION_ATTRIBUTE_IMMUTABLE_ONCE_PERSISTED_KEY) == null) {
			return null;
		}
		String optoutStr = attributeDetails.get(EssentialProgModelExtendedSemanticsConstants.ANNOTATION_ATTRIBUTE_IMMUTABLE_ONCE_PERSISTED_OPTOUT_KEY);
		final boolean optout = Boolean.valueOf(optoutStr);
		return ImmutableOncePersisted.Factory.create(optout);
	}
	public void setAttributeImmutableOncePersisted(EAttribute attribute, ImmutableOncePersisted immutableOncePersisted) {
		if (immutableOncePersisted== null) return;
		_emfAnnotations.putAnnotationDetails(attribute,
				EssentialProgModelExtendedSemanticsConstants.ANNOTATION_ATTRIBUTE, 
				EssentialProgModelExtendedSemanticsConstants.ANNOTATION_ATTRIBUTE_IMMUTABLE_ONCE_PERSISTED_KEY, 
				"true");
		_emfAnnotations.putAnnotationDetails(attribute,
				EssentialProgModelExtendedSemanticsConstants.ANNOTATION_ATTRIBUTE, 
				EssentialProgModelExtendedSemanticsConstants.ANNOTATION_ATTRIBUTE_IMMUTABLE_ONCE_PERSISTED_OPTOUT_KEY, 
				""+immutableOncePersisted.optout());
	}
	
	/**
	 * Returns the attribute annotation of specified key, parsing to an int.
	 * 
	 *  <p>
	 *  Admittedly, this doesn't follow the model of the other methods, but
	 *  is more extensible.  It is used by {@link AbstractAttributeComparator}. 
	 */
	public Integer getAttributeAnnotationInt(EAttribute attribute, String annotationKey) {
		Map<String,String> attributeDetails = 
			_emfAnnotations.getAnnotationDetails(attribute, EssentialProgModelExtendedSemanticsConstants.ANNOTATION_ATTRIBUTE);
		String valStr = 
			attributeDetails.get(annotationKey);
		if (valStr == null) return null;
		try {
			return Integer.parseInt(valStr);
		} catch(RuntimeException ex) {
			return null;
		}
	}


	// references
	// (no additional semantics)
	
	// operations
	// (no additional semantics)

	
	//////////////////////////////////////////////////////////////////////
	// Specific to Deployment Binding
	// (need to move out)
	//////////////////////////////////////////////////////////////////////

	// attributes
	public Method getAttributeAccessorPreMethod(EAttribute attribute) {
		String methodName = _emfAnnotations.getAnnotationDetail(
				_emfAnnotations.methodNamesAnnotationFor(attribute), 
				EssentialProgModelExtendedSemanticsConstants.ANNOTATION_ATTRIBUTE_ACCESSOR_PRECONDITION_METHOD_NAME_KEY);
		return findMethod(javaClassFor(attribute), methodName);
	}
	public void setAttributeAccessorPreMethod(EAttribute attribute, Method method) {
		_emfAnnotations.putAnnotationDetails(
				attribute,
				EssentialProgModelStandardSemanticsConstants.ANNOTATION_SOURCE_METHOD_NAMES,  
				EssentialProgModelExtendedSemanticsConstants.ANNOTATION_ATTRIBUTE_ACCESSOR_PRECONDITION_METHOD_NAME_KEY, 
				method.getName());
	}

	public Method getAttributeMutatorPreMethod(EAttribute attribute) {
		String methodName = _emfAnnotations.getAnnotationDetail(
				_emfAnnotations.methodNamesAnnotationFor(attribute), 
				EssentialProgModelExtendedSemanticsConstants.ANNOTATION_ATTRIBUTE_MUTATOR_PRECONDITION_METHOD_NAME_KEY);
		Class<?> attributeType = attribute.getEAttributeType().getInstanceClass();
		return findMethod(javaClassFor(attribute), methodName, new Class[]{attributeType});
	}
	public void setAttributeMutatorPreMethod(EAttribute attribute, Method method) {
		_emfAnnotations.putAnnotationDetails(
				attribute,
				EssentialProgModelStandardSemanticsConstants.ANNOTATION_SOURCE_METHOD_NAMES,  
				EssentialProgModelExtendedSemanticsConstants.ANNOTATION_ATTRIBUTE_MUTATOR_PRECONDITION_METHOD_NAME_KEY, 
				method.getName());
	}

	// references
	
	/**
	 * TODO: I think this actually corresponds to the preconditions for a 1:1 reference only (since there
	 * is also getReferenceAddToPre / getReferenceRemoveFromPre methods.
	 */
	public Method getReferenceAccessorPreMethod(EReference reference) {
		String methodName = _emfAnnotations.getAnnotationDetail(
				_emfAnnotations.methodNamesAnnotationFor(reference), 
				EssentialProgModelExtendedSemanticsConstants.ANNOTATION_REFERENCE_ACCESSOR_PRECONDITION_METHOD_NAME_KEY);
		return findMethod(javaClassFor(reference), methodName);
	}
	/**
	 * TODO: I think this actually corresponds to the preconditions for a 1:1 reference only (since there
	 * is also getReferenceAddToPre / getReferenceRemoveFromPre methods.
	 */
	public void setReferenceAccessorPreMethod(EReference reference, Method method) {
		_emfAnnotations.putAnnotationDetails(
				reference,
				EssentialProgModelStandardSemanticsConstants.ANNOTATION_SOURCE_METHOD_NAMES,  
				EssentialProgModelExtendedSemanticsConstants.ANNOTATION_REFERENCE_ACCESSOR_PRECONDITION_METHOD_NAME_KEY, 
				method.getName());
	}

	/**
	 * TODO: I think this actually corresponds to the preconditions for a 1:1 reference only (since there
	 * is also getReferenceAddToPre / getReferenceRemoveFromPre methods.
	 */
	public Method getReferenceMutatorPreMethod(EReference reference) {
		String methodName = _emfAnnotations.getAnnotationDetail(
				_emfAnnotations.methodNamesAnnotationFor(reference), 
				EssentialProgModelExtendedSemanticsConstants.ANNOTATION_REFERENCE_MUTATOR_PRECONDITION_METHOD_NAME_KEY);
		
		Class<?> referenceType = reference.getEType().getInstanceClass();
		return findMethod(javaClassFor(reference), methodName, new Class[]{referenceType});
	}
	/**
	 * TODO: I think this actually corresponds to the preconditions for a 1:1 reference only (since there
	 * is also getReferenceAddToPre / getReferenceRemoveFromPre methods.
	 */
	public void setReferenceMutatorPreMethod(EReference reference, Method method) {
		_emfAnnotations.putAnnotationDetails(
				reference,
				EssentialProgModelStandardSemanticsConstants.ANNOTATION_SOURCE_METHOD_NAMES,  
				EssentialProgModelExtendedSemanticsConstants.ANNOTATION_REFERENCE_MUTATOR_PRECONDITION_METHOD_NAME_KEY, 
				method.getName());
	}
	
	public Method getReferenceAddToPreMethod(EReference reference) {
		String methodName = _emfAnnotations.getAnnotationDetail(
				_emfAnnotations.methodNamesAnnotationFor(reference), 
				EssentialProgModelExtendedSemanticsConstants.ANNOTATION_REFERENCE_ADD_TO_PRECONDITION_METHOD_NAME_KEY);
		Class<?> referenceType = reference.getEType().getInstanceClass();
		return findMethod(javaClassFor(reference), methodName, new Class[]{referenceType});
	}
	public void setReferenceAddToPreMethod(EReference reference, Method method) {
		_emfAnnotations.putAnnotationDetails(
				reference,
				EssentialProgModelStandardSemanticsConstants.ANNOTATION_SOURCE_METHOD_NAMES,  
				EssentialProgModelExtendedSemanticsConstants.ANNOTATION_REFERENCE_ADD_TO_PRECONDITION_METHOD_NAME_KEY, 
				method.getName());
	}

	public Method getReferenceRemoveFromPreMethod(EReference reference) {
		String methodName = _emfAnnotations.getAnnotationDetail(
				_emfAnnotations.methodNamesAnnotationFor(reference), 
				EssentialProgModelExtendedSemanticsConstants.ANNOTATION_REFERENCE_REMOVE_FROM_PRECONDITION_METHOD_NAME_KEY);
		Class<?> referenceType = reference.getEType().getInstanceClass();
		return findMethod(javaClassFor(reference), methodName, new Class[]{referenceType});
	}
	public void setReferenceRemoveFromPre(EReference reference, Method method) {
		_emfAnnotations.putAnnotationDetails(
				reference,
				EssentialProgModelStandardSemanticsConstants.ANNOTATION_SOURCE_METHOD_NAMES,  
				EssentialProgModelExtendedSemanticsConstants.ANNOTATION_REFERENCE_REMOVE_FROM_PRECONDITION_METHOD_NAME_KEY, 
				method.getName());
	}
	
	// operations
	public Method getOperationPreMethod(EOperation operation) {
		String methodName = _emfAnnotations.getAnnotationDetail(
				_emfAnnotations.methodNamesAnnotationFor(operation), 
				EssentialProgModelExtendedSemanticsConstants.ANNOTATION_OPERATION_PRECONDITION_METHOD_NAME_KEY);
		EList eParameters = operation.getEParameters();
		Class<?>[] parameterTypes = new Class<?>[eParameters.size()];
		for(int i=0; i<parameterTypes.length; i++) {
			EParameter eParameter = (EParameter)eParameters.get(i);
			parameterTypes[i] = eParameter.getEType().getInstanceClass();
		}
		return findMethod(javaClassFor(operation), methodName, parameterTypes);
	}
	public void setOperationPreMethod(EOperation operation, Method method) {
		_emfAnnotations.putAnnotationDetails(
				operation,
				EssentialProgModelStandardSemanticsConstants.ANNOTATION_SOURCE_METHOD_NAMES,  
				EssentialProgModelExtendedSemanticsConstants.ANNOTATION_OPERATION_PRECONDITION_METHOD_NAME_KEY, 
				method.getName());
	}
	
	public Method getOperationDefaultsMethod(EOperation operation) {
		String methodName = _emfAnnotations.getAnnotationDetail(
				_emfAnnotations.methodNamesAnnotationFor(operation), 
				EssentialProgModelExtendedSemanticsConstants.ANNOTATION_OPERATION_DEFAULTS_METHOD_NAME_KEY);
		EList eParameters = operation.getEParameters();
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
		return findMethod(javaClassFor(operation), methodName, parameterTypes);
	}
	public void setOperationDefaults(EOperation operation, Method method) {
		_emfAnnotations.putAnnotationDetails(
				operation,
				EssentialProgModelStandardSemanticsConstants.ANNOTATION_SOURCE_METHOD_NAMES,  
				EssentialProgModelExtendedSemanticsConstants.ANNOTATION_OPERATION_DEFAULTS_METHOD_NAME_KEY, 
				method.getName());
	}

}
