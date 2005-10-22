package de.berlios.rcpviewer.progmodel.extended;

import java.lang.annotation.Annotation;
import java.util.Map;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EReference;

import sun.security.krb5.internal.crypto.b;

import de.berlios.rcpviewer.domain.EmfAnnotations;
import de.berlios.rcpviewer.progmodel.standard.StandardProgModelConstants;

/**
 * Serializes and deserializes semantics for the extended programming model
 * to and from the EMF model.
 *  
 * @author Dan Haywood
 */
public final class ExtendedProgModelSemanticsEmfSerializer {

	private final EmfAnnotations _emfAnnotations = new EmfAnnotations();
	
	// any model element
	public Optional getOptional(EModelElement modelElement) {
		Map<String,String> attributeDetails = 
			_emfAnnotations.getAnnotationDetails(modelElement, ExtendedProgModelConstants.ANNOTATION_ELEMENT);
		if (attributeDetails.get(ExtendedProgModelConstants.ANNOTATION_ELEMENT_OPTIONAL_KEY) == null) {
			return null;
		}
		return Optional.Factory.create();
	}
	public void setOptional(EModelElement modelElement, Optional optional) {
		if (optional == null) return;
		_emfAnnotations.putAnnotationDetails(modelElement,
				ExtendedProgModelConstants.ANNOTATION_ELEMENT, 
				ExtendedProgModelConstants.ANNOTATION_ELEMENT_OPTIONAL_KEY, 
				"true");
	}
	
	public MinLengthOf getMinLengthOf(EModelElement modelElement) {
		Map<String,String> attributeDetails = 
			_emfAnnotations.getAnnotationDetails(modelElement, ExtendedProgModelConstants.ANNOTATION_ELEMENT);
		String valueStr = attributeDetails.get(ExtendedProgModelConstants.ANNOTATION_ELEMENT_MIN_LENGTH_OF_KEY);
		if (valueStr == null) {
			return null;
		}
		final int value = Integer.parseInt(valueStr);
		return MinLengthOf.Factory.create(value);
	}
	public void setMinLengthOf(EModelElement modelElement, MinLengthOf minLengthOf) {
		if (minLengthOf == null) return;
		_emfAnnotations.putAnnotationDetails(modelElement,
				ExtendedProgModelConstants.ANNOTATION_ELEMENT, 
				ExtendedProgModelConstants.ANNOTATION_ELEMENT_MIN_LENGTH_OF_KEY, 
				"" + minLengthOf.value());
	}
	
	public MaxLengthOf getMaxLengthOf(EModelElement modelElement) {
		Map<String,String> attributeDetails = 
			_emfAnnotations.getAnnotationDetails(modelElement, ExtendedProgModelConstants.ANNOTATION_ELEMENT);
		String valueStr = attributeDetails.get(ExtendedProgModelConstants.ANNOTATION_ELEMENT_MAX_LENGTH_OF_KEY);
		if (valueStr == null) {
			return null;
		}
		final int value = Integer.parseInt(valueStr);
		return MaxLengthOf.Factory.create(value);
	}
	public void setMaxLengthOf(EModelElement modelElement, MaxLengthOf maxLengthOf) {
		if (maxLengthOf == null) return;
		_emfAnnotations.putAnnotationDetails(modelElement,
				ExtendedProgModelConstants.ANNOTATION_ELEMENT, 
				ExtendedProgModelConstants.ANNOTATION_ELEMENT_MAX_LENGTH_OF_KEY, 
				"" + maxLengthOf.value());
	}
	
	public FieldLengthOf getFieldLengthOf(EModelElement modelElement) {
		Map<String,String> attributeDetails = 
			_emfAnnotations.getAnnotationDetails(modelElement, ExtendedProgModelConstants.ANNOTATION_ELEMENT);
		String valueStr = attributeDetails.get(ExtendedProgModelConstants.ANNOTATION_ELEMENT_FIELD_LENGTH_OF_KEY);
		if (valueStr == null) {
			return null;
		}
		final int value = Integer.parseInt(valueStr);
		return FieldLengthOf.Factory.create(value);
	}
	public void setFieldLengthOf(EModelElement modelElement, FieldLengthOf fieldLengthOf) {
		if (fieldLengthOf == null) return;
		_emfAnnotations.putAnnotationDetails(modelElement,
				ExtendedProgModelConstants.ANNOTATION_ELEMENT, 
				ExtendedProgModelConstants.ANNOTATION_ELEMENT_FIELD_LENGTH_OF_KEY, 
				"" + fieldLengthOf.value());
	}

	// class

	public Lifecycle getClassLifecycle(EClass eClass) {
		Map<String,String> attributeDetails = 
			_emfAnnotations.getAnnotationDetails(eClass, ExtendedProgModelConstants.ANNOTATION_CLASS);
		String instantiableStr = attributeDetails.get(ExtendedProgModelConstants.ANNOTATION_CLASS_INSTANTIABLE_KEY);
		if (instantiableStr == null) {
			return null;
		}
		final boolean instantiable = Boolean.valueOf(instantiableStr);
		final boolean searchable = Boolean.valueOf(attributeDetails.get(ExtendedProgModelConstants.ANNOTATION_CLASS_SEARCHABLE_KEY));
		final boolean saveable = Boolean.valueOf(attributeDetails.get(ExtendedProgModelConstants.ANNOTATION_CLASS_SAVEABLE_KEY));
		return Lifecycle.Factory.create(searchable, instantiable, saveable);
	}
	public void setClassLifecycle(EClass eClass, Lifecycle lifecycle) {
		if (lifecycle == null) return;
		_emfAnnotations.putAnnotationDetails(eClass, 
				ExtendedProgModelConstants.ANNOTATION_CLASS, 
				ExtendedProgModelConstants.ANNOTATION_CLASS_SEARCHABLE_KEY, 
				""+lifecycle.searchable());	
		_emfAnnotations.putAnnotationDetails(eClass, 
				ExtendedProgModelConstants.ANNOTATION_CLASS, 
				ExtendedProgModelConstants.ANNOTATION_CLASS_INSTANTIABLE_KEY, 
				""+lifecycle.instantiable());	
		_emfAnnotations.putAnnotationDetails(eClass, 
				ExtendedProgModelConstants.ANNOTATION_CLASS, 
				ExtendedProgModelConstants.ANNOTATION_CLASS_SAVEABLE_KEY, 
				""+lifecycle.saveable());	
	}
	
	public ImmutableOncePersisted getClassImmutableOncePersisted(EClass eClass) {
		Map<String,String> attributeDetails = 
			_emfAnnotations.getAnnotationDetails(eClass, ExtendedProgModelConstants.ANNOTATION_ATTRIBUTE);
		if (attributeDetails.get(ExtendedProgModelConstants.ANNOTATION_CLASS_IMMUTABLE_ONCE_PERSISTED_KEY) == null) {
			return null;
		}
		return ImmutableOncePersisted.Factory.create(false);
	}
	public void setClassImmutableOncePersisted(EClass eClass, ImmutableOncePersisted immutableOncePersisted) {
		if (immutableOncePersisted== null) return;
		_emfAnnotations.putAnnotationDetails(eClass,
				ExtendedProgModelConstants.ANNOTATION_CLASS, 
				ExtendedProgModelConstants.ANNOTATION_CLASS_IMMUTABLE_ONCE_PERSISTED_KEY, 
				"true");
	}

	// attributes
	public String getAttributeAccessorPre(EAttribute attribute) {
		return _emfAnnotations.getAnnotationDetail(
				_emfAnnotations.methodNamesAnnotationFor(attribute), 
				ExtendedProgModelConstants.ANNOTATION_ATTRIBUTE_ACCESSOR_PRECONDITION_METHOD_NAME_KEY);
	}
	public void setAttributeAccessorPre(EAttribute attribute, String methodName) {
		_emfAnnotations.putAnnotationDetails(
				attribute,
				StandardProgModelConstants.ANNOTATION_SOURCE_METHOD_NAMES,  
				ExtendedProgModelConstants.ANNOTATION_ATTRIBUTE_ACCESSOR_PRECONDITION_METHOD_NAME_KEY, 
				methodName);
	}

	public String getAttributeMutatorPre(EAttribute attribute) {
		return _emfAnnotations.getAnnotationDetail(
				_emfAnnotations.methodNamesAnnotationFor(attribute), 
				ExtendedProgModelConstants.ANNOTATION_ATTRIBUTE_MUTATOR_PRECONDITION_METHOD_NAME_KEY);
	}
	public void setAttributeMutatorPre(EAttribute attribute, String methodName) {
		_emfAnnotations.putAnnotationDetails(
				attribute,
				StandardProgModelConstants.ANNOTATION_SOURCE_METHOD_NAMES,  
				ExtendedProgModelConstants.ANNOTATION_ATTRIBUTE_MUTATOR_PRECONDITION_METHOD_NAME_KEY, 
				methodName);
	}
	
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
				ExtendedProgModelConstants.ANNOTATION_ATTRIBUTE, 
				ExtendedProgModelConstants.ANNOTATION_ATTRIBUTE_RELATIVE_ORDER_KEY, 
				"" + relativeOrder.value());
	}
	
	public Id getAttributeId(EAttribute attribute) {
		Map<String,String> attributeDetails = 
			_emfAnnotations.getAnnotationDetails(attribute, ExtendedProgModelConstants.ANNOTATION_ATTRIBUTE);
		String valueStr = attributeDetails.get(ExtendedProgModelConstants.ANNOTATION_ATTRIBUTE_ID_VALUE);
		if (valueStr == null) {
			return null;
		}
		String assignedByStr = attributeDetails.get(ExtendedProgModelConstants.ANNOTATION_ATTRIBUTE_ID_ASSIGNED_BY);
		final int value = Integer.parseInt(valueStr);
		final AssignmentType assignedBy = AssignmentType.valueOf(assignedByStr);
		return Id.Factory.create(value, assignedBy);
	}
	public void setAttributeId(EAttribute attribute, Id id) {
		if (id == null) return;
		_emfAnnotations.putAnnotationDetails(attribute,
				ExtendedProgModelConstants.ANNOTATION_ATTRIBUTE, 
				ExtendedProgModelConstants.ANNOTATION_ATTRIBUTE_ID_VALUE, 
				"" + id.value());
		_emfAnnotations.putAnnotationDetails(attribute,
				ExtendedProgModelConstants.ANNOTATION_ATTRIBUTE, 
				ExtendedProgModelConstants.ANNOTATION_ATTRIBUTE_ID_ASSIGNED_BY, 
				"" + id.assignedBy().name());
	}
	
	public Invisible getAttributeInvisible(EAttribute attribute) {
		Map<String,String> attributeDetails = 
			_emfAnnotations.getAnnotationDetails(attribute, ExtendedProgModelConstants.ANNOTATION_ATTRIBUTE);
		if (attributeDetails.get(ExtendedProgModelConstants.ANNOTATION_ATTRIBUTE_INVISIBLE_KEY) == null) {
			return null;
		}
		return Invisible.Factory.create();
	}
	public void setAttributeInvisible(EAttribute attribute, Invisible invisible) {
		if (invisible == null) return; 
		_emfAnnotations.putAnnotationDetails(attribute,
				ExtendedProgModelConstants.ANNOTATION_ATTRIBUTE, 
				ExtendedProgModelConstants.ANNOTATION_ATTRIBUTE_INVISIBLE_KEY, 
				"true");
	}
	
	public BusinessKey getAttributeBusinessKey(EAttribute attribute) {
		Map<String,String> attributeDetails = 
			_emfAnnotations.getAnnotationDetails(attribute, ExtendedProgModelConstants.ANNOTATION_ATTRIBUTE);
		final String name = attributeDetails.get(ExtendedProgModelConstants.ANNOTATION_ATTRIBUTE_BUSINESS_KEY_NAME_KEY);
		if (name == null) {
			return null;
		}
		String posStr = attributeDetails.get(ExtendedProgModelConstants.ANNOTATION_ATTRIBUTE_BUSINESS_KEY_POS_KEY);
		final int pos = Integer.parseInt(posStr);
		return BusinessKey.Factory.create(name, pos);
	}
	public void setAttributeBusinessKey(EAttribute attribute, BusinessKey businessKey) {
		if (businessKey == null) return;
		_emfAnnotations.putAnnotationDetails(attribute,
				ExtendedProgModelConstants.ANNOTATION_ATTRIBUTE, 
				ExtendedProgModelConstants.ANNOTATION_ATTRIBUTE_BUSINESS_KEY_NAME_KEY, 
				businessKey.name());
		_emfAnnotations.putAnnotationDetails(attribute,
				ExtendedProgModelConstants.ANNOTATION_ATTRIBUTE, 
				ExtendedProgModelConstants.ANNOTATION_ATTRIBUTE_BUSINESS_KEY_POS_KEY, 
				""+businessKey.pos());
	}
	
	public Mask getAttributeMask(EAttribute attribute) {
		Map<String,String> attributeDetails = 
			_emfAnnotations.getAnnotationDetails(attribute, ExtendedProgModelConstants.ANNOTATION_ATTRIBUTE);
		final String value = attributeDetails.get(ExtendedProgModelConstants.ANNOTATION_ATTRIBUTE_MASK_KEY);
		if (value == null) {
			return null;
		}
		return Mask.Factory.create(value);
	}
	public void setAttributeMask(EAttribute attribute, Mask mask) {
		if (mask == null) return;
		_emfAnnotations.putAnnotationDetails(attribute,
				ExtendedProgModelConstants.ANNOTATION_ATTRIBUTE, 
				ExtendedProgModelConstants.ANNOTATION_ATTRIBUTE_MASK_KEY, 
				mask.value());
	}
	
	public Regex getAttributeRegex(EAttribute attribute) {
		Map<String,String> attributeDetails = 
			_emfAnnotations.getAnnotationDetails(attribute, ExtendedProgModelConstants.ANNOTATION_ATTRIBUTE);
		final String value = attributeDetails.get(ExtendedProgModelConstants.ANNOTATION_ATTRIBUTE_REGEX_KEY);
		if (value == null) {
			return null;
		}
		return Regex.Factory.create(value);
	}
	public void setAttributeRegex(EAttribute attribute, Regex regex) {
		if (regex== null) return;
		_emfAnnotations.putAnnotationDetails(attribute,
				ExtendedProgModelConstants.ANNOTATION_ATTRIBUTE, 
				ExtendedProgModelConstants.ANNOTATION_ATTRIBUTE_REGEX_KEY, 
				regex.value());
	}
	
	public ImmutableOncePersisted getAttributeImmutableOncePersisted(EAttribute attribute) {
		Map<String,String> attributeDetails = 
			_emfAnnotations.getAnnotationDetails(attribute, ExtendedProgModelConstants.ANNOTATION_ATTRIBUTE);
		if (attributeDetails.get(ExtendedProgModelConstants.ANNOTATION_ATTRIBUTE_IMMUTABLE_ONCE_PERSISTED_KEY) == null) {
			return null;
		}
		String optoutStr = attributeDetails.get(ExtendedProgModelConstants.ANNOTATION_ATTRIBUTE_IMMUTABLE_ONCE_PERSISTED_OPTOUT_KEY);
		final boolean optout = Boolean.valueOf(optoutStr);
		return ImmutableOncePersisted.Factory.create(optout);
	}
	public void setAttributeImmutableOncePersisted(EAttribute attribute, ImmutableOncePersisted immutableOncePersisted) {
		if (immutableOncePersisted== null) return;
		_emfAnnotations.putAnnotationDetails(attribute,
				ExtendedProgModelConstants.ANNOTATION_ATTRIBUTE, 
				ExtendedProgModelConstants.ANNOTATION_ATTRIBUTE_IMMUTABLE_ONCE_PERSISTED_KEY, 
				"true");
		_emfAnnotations.putAnnotationDetails(attribute,
				ExtendedProgModelConstants.ANNOTATION_ATTRIBUTE, 
				ExtendedProgModelConstants.ANNOTATION_ATTRIBUTE_IMMUTABLE_ONCE_PERSISTED_OPTOUT_KEY, 
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
			_emfAnnotations.getAnnotationDetails(attribute, ExtendedProgModelConstants.ANNOTATION_ATTRIBUTE);
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
	public String getReferenceAccessorPre(EReference reference) {
		return _emfAnnotations.getAnnotationDetail(
				_emfAnnotations.methodNamesAnnotationFor(reference), 
				ExtendedProgModelConstants.ANNOTATION_REFERENCE_ACCESSOR_PRECONDITION_METHOD_NAME_KEY);
	}
	public void setReferenceAccessorPre(EReference reference, String methodName) {
		_emfAnnotations.putAnnotationDetails(
				reference,
				StandardProgModelConstants.ANNOTATION_SOURCE_METHOD_NAMES,  
				ExtendedProgModelConstants.ANNOTATION_REFERENCE_ACCESSOR_PRECONDITION_METHOD_NAME_KEY, 
				methodName);
	}

	public String getReferenceMutatorPre(EReference reference) {
		return _emfAnnotations.getAnnotationDetail(
				_emfAnnotations.methodNamesAnnotationFor(reference), 
				ExtendedProgModelConstants.ANNOTATION_REFERENCE_MUTATOR_PRECONDITION_METHOD_NAME_KEY);

	}
	public void setReferenceMutatorPre(EReference reference, String methodName) {
		_emfAnnotations.putAnnotationDetails(
				reference,
				StandardProgModelConstants.ANNOTATION_SOURCE_METHOD_NAMES,  
				ExtendedProgModelConstants.ANNOTATION_REFERENCE_MUTATOR_PRECONDITION_METHOD_NAME_KEY, 
				methodName);
	}
	
	public String getReferenceAddToPre(EReference reference) {
		return _emfAnnotations.getAnnotationDetail(
				_emfAnnotations.methodNamesAnnotationFor(reference), 
				ExtendedProgModelConstants.ANNOTATION_REFERENCE_ADD_TO_PRECONDITION_METHOD_NAME_KEY);
	}
	public void setReferenceAddToPre(EReference reference, String methodName) {
		_emfAnnotations.putAnnotationDetails(
				reference,
				StandardProgModelConstants.ANNOTATION_SOURCE_METHOD_NAMES,  
				ExtendedProgModelConstants.ANNOTATION_REFERENCE_ADD_TO_PRECONDITION_METHOD_NAME_KEY, 
				methodName);
	}

	public String getReferenceRemoveFromPre(EReference reference) {
		return _emfAnnotations.getAnnotationDetail(
				_emfAnnotations.methodNamesAnnotationFor(reference), 
				ExtendedProgModelConstants.ANNOTATION_REFERENCE_REMOVE_FROM_PRECONDITION_METHOD_NAME_KEY);

	}
	public void setReferenceRemoveFromPre(EReference reference, String methodName) {
		_emfAnnotations.putAnnotationDetails(
				reference,
				StandardProgModelConstants.ANNOTATION_SOURCE_METHOD_NAMES,  
				ExtendedProgModelConstants.ANNOTATION_REFERENCE_REMOVE_FROM_PRECONDITION_METHOD_NAME_KEY, 
				methodName);
	}
	
	// operations
	public String getOperationPre(EOperation operation) {
		return _emfAnnotations.getAnnotationDetail(
				_emfAnnotations.methodNamesAnnotationFor(operation), 
				ExtendedProgModelConstants.ANNOTATION_OPERATION_PRECONDITION_METHOD_NAME_KEY);
	}
	public void setOperationPre(EOperation operation, String methodName) {
		_emfAnnotations.putAnnotationDetails(
				operation,
				StandardProgModelConstants.ANNOTATION_SOURCE_METHOD_NAMES,  
				ExtendedProgModelConstants.ANNOTATION_OPERATION_PRECONDITION_METHOD_NAME_KEY, 
				methodName);
	}
	
	public String getOperationDefaults(EOperation operation) {
		return _emfAnnotations.getAnnotationDetail(
				_emfAnnotations.methodNamesAnnotationFor(operation), 
				ExtendedProgModelConstants.ANNOTATION_OPERATION_DEFAULTS_METHOD_NAME_KEY);
	}
	public void setOperationDefaults(EOperation operation, String methodName) {
		_emfAnnotations.putAnnotationDetails(
				operation,
				StandardProgModelConstants.ANNOTATION_SOURCE_METHOD_NAMES,  
				ExtendedProgModelConstants.ANNOTATION_OPERATION_DEFAULTS_METHOD_NAME_KEY, 
				methodName);
	}

}