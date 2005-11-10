package org.essentialplatform.progmodel.essential.core.emf;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.EReference;

import org.essentialplatform.core.emf.AbstractProgModelSemanticsEmfSerializer;
import org.essentialplatform.core.domain.comparators.AbstractEAttributeComparator;
import org.essentialplatform.progmodel.essential.app.AssignmentType;
import org.essentialplatform.progmodel.essential.app.BusinessKey;
import org.essentialplatform.progmodel.essential.app.FieldLengthOf;
import org.essentialplatform.progmodel.essential.app.IPrerequisites;
import org.essentialplatform.progmodel.essential.app.Id;
import org.essentialplatform.progmodel.essential.app.ImmutableOncePersisted;
import org.essentialplatform.progmodel.essential.app.Invisible;
import org.essentialplatform.progmodel.essential.app.Lifecycle;
import org.essentialplatform.progmodel.essential.app.Mask;
import org.essentialplatform.progmodel.essential.app.MaxLengthOf;
import org.essentialplatform.progmodel.essential.app.MinLengthOf;
import org.essentialplatform.progmodel.essential.app.Optional;
import org.essentialplatform.progmodel.essential.app.Regex;
import org.essentialplatform.progmodel.essential.app.RelativeOrder;
import org.essentialplatform.progmodel.essential.core.EssentialProgModelExtendedSemanticsConstants;
import org.essentialplatform.progmodel.essential.core.EssentialProgModelStandardSemanticsConstants;


/**
 * Serializes and deserializes semantics for the extended programming model
 * to and from the EMF model.
 *  
 * @author Dan Haywood
 */
public final class EssentialProgModelExtendedSemanticsEmfSerializer extends AbstractProgModelSemanticsEmfSerializer {

	private final EssentialProgModelStandardSemanticsEmfSerializer standardSerializer = new EssentialProgModelStandardSemanticsEmfSerializer();

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
	 *  is more extensible.  It is used by {@link AbstractEAttributeComparator}. 
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
	
	public <V> void setAttributeAccessorPreMethodIfPossible(EAttribute eAttribute, Class<V> javaClass) {
		Method accessor = standardSerializer.getAttributeAccessorMethod(eAttribute);
		if (accessor == null) {
			return;
		}
		String accessorPreMethodName = accessor.getName() + 
			EssentialProgModelExtendedSemanticsConstants.SUFFIX_ELEMENT_PRECONDITIONS;
		Method accessorPreCandidate;
		try {
			accessorPreCandidate = javaClass.getMethod(accessorPreMethodName, new Class[]{});
		} catch (SecurityException ex) {
			return;
		} catch (NoSuchMethodException ex) {
			return;
		}
		if (accessorPreCandidate == null) {
			return;
		}
		if (!methodReturns(accessorPreCandidate, IPrerequisites.class)) {
			return;
		}
		if (!isPublic(accessorPreCandidate)) {
			return;
		}
		_emfAnnotations.putAnnotationDetails(
			eAttribute,
			EssentialProgModelStandardSemanticsConstants.ANNOTATION_SOURCE_METHOD_NAMES,  
			EssentialProgModelExtendedSemanticsConstants.ANNOTATION_ATTRIBUTE_ACCESSOR_PRECONDITION_METHOD_NAME_KEY, 
			accessorPreCandidate.getName());
	}

	public Method getAttributeMutatorPreMethod(EAttribute attribute) {
		String methodName = _emfAnnotations.getAnnotationDetail(
				_emfAnnotations.methodNamesAnnotationFor(attribute), 
				EssentialProgModelExtendedSemanticsConstants.ANNOTATION_ATTRIBUTE_MUTATOR_PRECONDITION_METHOD_NAME_KEY);
		Class<?> attributeType = attribute.getEAttributeType().getInstanceClass();
		return findMethod(javaClassFor(attribute), methodName, new Class[]{attributeType});
	}
	
	public <V> void setAttributeMutatorPreMethodIfPossible(EAttribute eAttribute, Class<V> javaClass) {
		Method mutator = standardSerializer.getAttributeMutatorMethod(eAttribute);
		if (mutator == null) {
			return;
		}
		Class<?> attributeType = eAttribute.getEAttributeType().getInstanceClass();
		String mutatorPreMethodName = mutator.getName() + 
			EssentialProgModelExtendedSemanticsConstants.SUFFIX_ELEMENT_PRECONDITIONS;
		Method mutatorPreCandidate;
		try {
			mutatorPreCandidate = javaClass.getMethod(mutatorPreMethodName, new Class[]{attributeType});
		} catch (SecurityException ex) {
			return;
		} catch (NoSuchMethodException ex) {
			return;
		}
		if (mutatorPreCandidate == null) {
			return;
		}
		if (!methodReturns(mutatorPreCandidate, IPrerequisites.class)) {
			return;
		}
		if (!isPublic(mutatorPreCandidate)) {
			return;
		}
		_emfAnnotations.putAnnotationDetails(
			eAttribute,
			EssentialProgModelStandardSemanticsConstants.ANNOTATION_SOURCE_METHOD_NAMES,  
			EssentialProgModelExtendedSemanticsConstants.ANNOTATION_ATTRIBUTE_MUTATOR_PRECONDITION_METHOD_NAME_KEY, 
			mutatorPreCandidate.getName());
	}

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
	
	public <V> void setReferenceAccessorPreMethodIfPossible(EReference eReference, Class<V> javaClass) {
		Method accessor = standardSerializer.getReferenceAccessor(eReference);
		if (accessor == null) {
			return;
		}
		String accessorPreMethodName = accessor.getName() + 
			EssentialProgModelExtendedSemanticsConstants.SUFFIX_ELEMENT_PRECONDITIONS;
		Method accessorPreCandidate;
		try {
			accessorPreCandidate = javaClass.getMethod(accessorPreMethodName, new Class[]{});
		} catch (SecurityException ex) {
			return;
		} catch (NoSuchMethodException ex) {
			return;
		}
		if (accessorPreCandidate == null) {
			return;
		}
		if (!isPublic(accessorPreCandidate)) {
			return;
		}
		if (!methodReturns(accessorPreCandidate, IPrerequisites.class)) {
			return;
		}
		_emfAnnotations.putAnnotationDetails(
			eReference,
			EssentialProgModelStandardSemanticsConstants.ANNOTATION_SOURCE_METHOD_NAMES,  
			EssentialProgModelExtendedSemanticsConstants.ANNOTATION_REFERENCE_ACCESSOR_PRECONDITION_METHOD_NAME_KEY, 
			accessorPreCandidate.getName());
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
	public <V> void setReferenceMutatorPreMethodIfPossible(EReference eReference, Class<V> javaClass) {
		if (eReference.isMany()) {
			return;
		}
		Method mutator = standardSerializer.getReferenceMutator(eReference);
		if (mutator == null) {
			return;
		}
		String accessorPreMethodName = mutator.getName() + 
			EssentialProgModelExtendedSemanticsConstants.SUFFIX_ELEMENT_PRECONDITIONS;
		Method mutatorPreCandidate;
		try {
			mutatorPreCandidate = javaClass.getMethod(accessorPreMethodName, new Class[]{});
		} catch (SecurityException ex) {
			return;
		} catch (NoSuchMethodException ex) {
			return;
		}
		if (mutatorPreCandidate == null) {
			return;
		}
		if (!isPublic(mutatorPreCandidate)) {
			return;
		}
		if (!methodReturns(mutatorPreCandidate, IPrerequisites.class)) {
			return;
		}
		_emfAnnotations.putAnnotationDetails(
			eReference,
			EssentialProgModelStandardSemanticsConstants.ANNOTATION_SOURCE_METHOD_NAMES,  
			EssentialProgModelExtendedSemanticsConstants.ANNOTATION_REFERENCE_MUTATOR_PRECONDITION_METHOD_NAME_KEY, 
			mutatorPreCandidate.getName());
	}

	public Method getReferenceAddToPreMethod(EReference reference) {
		String methodName = _emfAnnotations.getAnnotationDetail(
				_emfAnnotations.methodNamesAnnotationFor(reference), 
				EssentialProgModelExtendedSemanticsConstants.ANNOTATION_REFERENCE_ADD_TO_PRECONDITION_METHOD_NAME_KEY);
		Class<?> referenceType = reference.getEType().getInstanceClass();
		return findMethod(javaClassFor(reference), methodName, new Class[]{referenceType});
	}

	public <V> void setReferenceAddToPreMethodIfPossible(EReference reference, Class<V> javaClass) {
		if (!reference.isMany()) {
			return;
		}
		Method addTo = standardSerializer.getReferenceCollectionAssociator(reference);
		if (addTo == null) {
			return;
		}
		String addToPreMethodName = addTo.getName() + 
			EssentialProgModelExtendedSemanticsConstants.SUFFIX_ELEMENT_PRECONDITIONS;
		Method addToPreCandidate;
		try {
			addToPreCandidate = javaClass.getMethod(addToPreMethodName, new Class[]{});
		} catch (SecurityException ex) {
			return;
		} catch (NoSuchMethodException ex) {
			return;
		}
		if (addToPreCandidate == null) {
			return;
		}
		if (!isPublic(addToPreCandidate)) {
			return;
		}
		if (!methodReturns(addToPreCandidate, IPrerequisites.class)) {
			return;
		}
		_emfAnnotations.putAnnotationDetails(
			reference,
			EssentialProgModelStandardSemanticsConstants.ANNOTATION_SOURCE_METHOD_NAMES,  
			EssentialProgModelExtendedSemanticsConstants.ANNOTATION_REFERENCE_ADD_TO_PRECONDITION_METHOD_NAME_KEY, 
			addToPreCandidate.getName());
	}

	public Method getReferenceRemoveFromPreMethod(EReference reference) {
		String methodName = _emfAnnotations.getAnnotationDetail(
				_emfAnnotations.methodNamesAnnotationFor(reference), 
				EssentialProgModelExtendedSemanticsConstants.ANNOTATION_REFERENCE_REMOVE_FROM_PRECONDITION_METHOD_NAME_KEY);
		Class<?> referenceType = reference.getEType().getInstanceClass();
		return findMethod(javaClassFor(reference), methodName, new Class[]{referenceType});
	}
	
	public <V> void setReferenceRemoveFromPreMethodIfPossible(EReference reference, Class<V> javaClass) {
		if (!reference.isMany()) {
			return;
		}
		Method removeFrom = standardSerializer.getReferenceCollectionDissociator(reference);
		if (removeFrom == null) {
			return;
		}
		String removeFromPreMethodName = removeFrom.getName() + 
			EssentialProgModelExtendedSemanticsConstants.SUFFIX_ELEMENT_PRECONDITIONS;
		Method removeFromPreCandidate;
		try {
			removeFromPreCandidate = javaClass.getMethod(removeFromPreMethodName, new Class[]{});
		} catch (SecurityException ex) {
			return;
		} catch (NoSuchMethodException ex) {
			return;
		}
		if (removeFromPreCandidate == null) {
			return;
		}
		if (!isPublic(removeFromPreCandidate)) {
			return;
		}
		if (!methodReturns(removeFromPreCandidate, IPrerequisites.class)) {
			return;
		}
		_emfAnnotations.putAnnotationDetails(
			reference,
			EssentialProgModelStandardSemanticsConstants.ANNOTATION_SOURCE_METHOD_NAMES,  
			EssentialProgModelExtendedSemanticsConstants.ANNOTATION_REFERENCE_REMOVE_FROM_PRECONDITION_METHOD_NAME_KEY, 
			removeFromPreCandidate.getName());
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

	public <V> void setOperationPreMethodIfPossible(EOperation eOperation, Class<V> javaClass, Method invoker) {
		String invokerName = invoker.getName();
		String invokerPreMethodName = invokerName + 
			EssentialProgModelExtendedSemanticsConstants.SUFFIX_ELEMENT_PRECONDITIONS;
		Method invokerPreCandidate;
		try {
			EList eParameters = eOperation.getEParameters();
			Class<?>[] parameterTypes = new Class<?>[eParameters.size()];
			for(int i=0; i<parameterTypes.length; i++) {
				EParameter eParameter = (EParameter)eParameters.get(i);
				parameterTypes[i] = eParameter.getEType().getInstanceClass();
			}
			invokerPreCandidate = javaClass.getMethod(invokerPreMethodName, parameterTypes);
		} catch (SecurityException ex) {
			return;
		} catch (NoSuchMethodException ex) {
			return;
		}
		if (invokerPreCandidate == null) {
			return;
		}
		if (!isPublic(invokerPreCandidate)) {
			return;
		}
		if (!methodReturns(invokerPreCandidate, IPrerequisites.class)) {
			return;
		}
		_emfAnnotations.putAnnotationDetails(
			eOperation,
			EssentialProgModelStandardSemanticsConstants.ANNOTATION_SOURCE_METHOD_NAMES,  
			EssentialProgModelExtendedSemanticsConstants.ANNOTATION_OPERATION_PRECONDITION_METHOD_NAME_KEY, 
			invokerPreCandidate.getName());
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
	
	public <V> void setOperationDefaultsIfPossible(EOperation eOperation, Class<V> javaClass, Method invoker) {
		String invokerName = invoker.getName();
		String invokerDefaultsMethodName = invokerName + 
			EssentialProgModelExtendedSemanticsConstants.SUFFIX_OPERATION_DEFAULTS;
		Method invokerDefaultsCandidate;
		try {
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
			invokerDefaultsCandidate = javaClass.getMethod(invokerDefaultsMethodName, (Class[])parameterTypes);
		} catch (SecurityException ex) {
			return;
		} catch (NoSuchMethodException ex) {
			return;
		}
		if (invokerDefaultsCandidate == null) {
			return;
		}
		if (!isPublic(invokerDefaultsCandidate)) {
			return;
		}
		if (!methodReturns(invokerDefaultsCandidate, Void.class)) {
			return;
		}
		setOperationDefaults(eOperation, invokerDefaultsCandidate);
	}
	private boolean isPublic(Method method) {
		return Modifier.isPublic(method.getModifiers());
	}
	/**
	 * Although methods that are <code>void</code> do not actually reflectively
	 * return <code>Void.class</code>, this method will - as a convenience - 
	 * make that interpretation.
	 * 
	 * @param method
	 * @param javaClass
	 * @return
	 */
	private boolean methodReturns(Method method, Class<?> javaClass) {
		// hack, good enough for us.
		if (javaClass == Void.class) {
			return method.getReturnType() == null ||
			       "void".equals(method.getReturnType().getName());
		}
		return javaClass.isAssignableFrom(method.getReturnType());
	}

	private void setOperationDefaults(EOperation operation, Method method) {
		_emfAnnotations.putAnnotationDetails(
				operation,
				EssentialProgModelStandardSemanticsConstants.ANNOTATION_SOURCE_METHOD_NAMES,  
				EssentialProgModelExtendedSemanticsConstants.ANNOTATION_OPERATION_DEFAULTS_METHOD_NAME_KEY, 
				method.getName());
	}

}
