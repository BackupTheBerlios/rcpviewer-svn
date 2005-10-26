package de.berlios.rcpviewer.progmodel.standard;

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

import de.berlios.rcpviewer.domain.AbstractProgModelSemanticsEmfSerializer;
import de.berlios.rcpviewer.domain.EmfAnnotations;
import de.berlios.rcpviewer.progmodel.extended.AssignmentType;
import de.berlios.rcpviewer.progmodel.extended.Id;
import de.berlios.rcpviewer.progmodel.extended.Named;

/**
 * Serializes and deserializes semantics for the extended programming model
 * to and from the EMF model.
 *  
 * @author Dan Haywood
 */
public final class StandardProgModelSemanticsEmfSerializer extends AbstractProgModelSemanticsEmfSerializer {

	// any model element
	
	public Named getNamed(EModelElement modelElement) {
		Map<String,String> attributeDetails = 
			_emfAnnotations.getAnnotationDetails(modelElement, StandardProgModelConstants.ANNOTATION_ELEMENT);
		final String value = attributeDetails.get(StandardProgModelConstants.ANNOTATION_ELEMENT_NAMED_KEY);
		if (value == null) {
			return null;
		}
		return new Named(){
				public String value() { return value; }
				public Class<? extends Annotation> annotationType() { return Named.class; }
			};
	}
	public void setNamed(EModelElement modelElement, Named named) {
		if (named == null) return;
		_emfAnnotations.putAnnotationDetails(
				modelElement,
				StandardProgModelConstants.ANNOTATION_ELEMENT,
				StandardProgModelConstants.ANNOTATION_ELEMENT_NAMED_KEY, 
				named.value());	
	}

	public void setDescribedAs(EModelElement modelElement, DescribedAs describedAs) {
		if (describedAs == null) return;
		_emfAnnotations.putAnnotationDetails(
				modelElement,
				StandardProgModelConstants.ANNOTATION_ELEMENT,
				StandardProgModelConstants.ANNOTATION_ELEMENT_DESCRIPTION_KEY, 
				describedAs.value() );	
	}

	public void setImmutable(EModelElement modelElement, Immutable immutable) {
		_emfAnnotations.putAnnotationDetails(
				modelElement,
				StandardProgModelConstants.ANNOTATION_ELEMENT,
				StandardProgModelConstants.ANNOTATION_ELEMENT_IMMUTABLE_KEY, 
				"" + (immutable != null));	
	}
	
	// attributes
	public Method getAttributeAccessorMethod(final EAttribute attribute) {
		String methodName = 
			_emfAnnotations.getAnnotationDetail(
				_emfAnnotations.methodNamesAnnotationFor(attribute),
				StandardProgModelConstants.ANNOTATION_ATTRIBUTE_ACCESSOR_METHOD_NAME_KEY);
		return findMethod(javaClassFor(attribute), methodName);
	}
	public void setAttributeAccessorMethod(final EAttribute attribute, Method method) {
		_emfAnnotations.putAnnotationDetails(
				attribute,
				StandardProgModelConstants.ANNOTATION_SOURCE_METHOD_NAMES, 
				StandardProgModelConstants.ANNOTATION_ATTRIBUTE_ACCESSOR_METHOD_NAME_KEY, 
				method.getName());
	}

	public Method getAttributeMutatorMethod(final EAttribute attribute) {
		String methodName = 
			_emfAnnotations.getAnnotationDetail(
				_emfAnnotations.methodNamesAnnotationFor(attribute),
				StandardProgModelConstants.ANNOTATION_ATTRIBUTE_MUTATOR_METHOD_NAME_KEY);
		return findMethod(javaClassFor(attribute), methodName, typeAsArrayFor(attribute));
	}
	public void setAttributeMutatorMethod(final EAttribute attribute, Method method) {
		_emfAnnotations.putAnnotationDetails(
				attribute,
				StandardProgModelConstants.ANNOTATION_SOURCE_METHOD_NAMES, 
				StandardProgModelConstants.ANNOTATION_ATTRIBUTE_MUTATOR_METHOD_NAME_KEY, 
				method.getName());
	}
	
	public void setAttributeIsUnsetMethod(final EAttribute attribute, Method method) {
		_emfAnnotations.putAnnotationDetails(
				attribute,
				StandardProgModelConstants.ANNOTATION_SOURCE_METHOD_NAMES, 
				StandardProgModelConstants.ANNOTATION_ATTRIBUTE_IS_UNSET_METHOD_NAME_KEY, 
				method.getName());
	}
	
	public void setAttributeUnsetMethod(final EAttribute attribute, Method method) {
		_emfAnnotations.putAnnotationDetails(
				attribute,
				StandardProgModelConstants.ANNOTATION_SOURCE_METHOD_NAMES, 
				StandardProgModelConstants.ANNOTATION_ATTRIBUTE_UNSET_METHOD_NAME_KEY, 
				method.getName());
	}
	
	public void setAttributeWriteOnly(final EAttribute attribute) {
		_emfAnnotations.annotationOf(attribute, StandardProgModelConstants.ANNOTATION_ATTRIBUTE_WRITE_ONLY);
	}
	
	// references
	
	public OppositeOf getReferenceOppositeOf(EReference reference) {
		Map<String,String> referenceDetails = 
			_emfAnnotations.getAnnotationDetails(
				reference, StandardProgModelConstants.ANNOTATION_REFERENCE_OPPOSITE);
		final String value = referenceDetails.get(StandardProgModelConstants.ANNOTATION_REFERENCE_OPPOSITE_KEY);
		if (value == null) {
			return null;
		}
		return new OppositeOf(){
				public String value() { return value; }
				public Class<? extends Annotation> annotationType() { return OppositeOf.class; }
			};
	}
	public void setReferenceOppositeOf(EReference reference, OppositeOf oppositeOf) {
		if (reference == null || oppositeOf == null) return;
		_emfAnnotations.putAnnotationDetails(
				reference,
				StandardProgModelConstants.ANNOTATION_REFERENCE_OPPOSITE,
				StandardProgModelConstants.ANNOTATION_REFERENCE_OPPOSITE_KEY, 
				oppositeOf.value());
	}

	public Method getReferenceAccessor(final EReference reference) {
		String accessorMethodName = 
			_emfAnnotations.getAnnotationDetail(
				_emfAnnotations.methodNamesAnnotationFor(reference), 
				StandardProgModelConstants.ANNOTATION_REFERENCE_ACCESSOR_NAME_KEY);
		return findMethod(javaClassFor(reference), accessorMethodName);
	}
	public void setReferenceAccessor(final EReference reference, Method method) {
		_emfAnnotations.putAnnotationDetails(	
				reference,
				StandardProgModelConstants.ANNOTATION_SOURCE_METHOD_NAMES, 
				StandardProgModelConstants.ANNOTATION_REFERENCE_ACCESSOR_NAME_KEY, 
				method.getName());
	}

	/**
	 * TODO: why isn't there a setReferenceMutator?
	 */
	public Method getReferenceMutator(final EReference reference) {
		String mutatorMethodName = 
			_emfAnnotations.getAnnotationDetail(
				_emfAnnotations.methodNamesAnnotationFor(reference), 
				StandardProgModelConstants.ANNOTATION_REFERENCE_MUTATOR_NAME_KEY);
		return findMethod(
				javaClassFor(reference), 
				mutatorMethodName,
				typeAsArrayFor(reference));
	}

	public Method getReferenceOneToOneAssociator(final EReference reference) {
		String associatorMethodName = 
			_emfAnnotations.getAnnotationDetail(
					_emfAnnotations.methodNamesAnnotationFor(reference),
					StandardProgModelConstants.ANNOTATION_REFERENCE_ASSOCIATE_NAME_KEY);
		return findMethod(
				javaClassFor(reference), 
				associatorMethodName, 
				typeAsArrayFor(reference));
	}
	public void setReferenceOneToOneAssociator(final EReference reference, Method method) {
		_emfAnnotations.putAnnotationDetails(	
				reference,
				StandardProgModelConstants.ANNOTATION_SOURCE_METHOD_NAMES, 
				StandardProgModelConstants.ANNOTATION_REFERENCE_ASSOCIATE_NAME_KEY, 
				method.getName());
	}

	public Method getReferenceCollectionAssociator(final EReference reference) {
		String associatorMethodName = 
			_emfAnnotations.getAnnotationDetail(
					_emfAnnotations.methodNamesAnnotationFor(reference),
					StandardProgModelConstants.ANNOTATION_REFERENCE_ADD_TO_NAME_KEY);
		return findMethod(
				javaClassFor(reference), 
				associatorMethodName, 
				typeAsArrayFor(reference));
	}
	public void setReferenceCollectionAssociator(final EReference reference, Method method) {
		_emfAnnotations.putAnnotationDetails(	
				reference,
				StandardProgModelConstants.ANNOTATION_SOURCE_METHOD_NAMES, 
				StandardProgModelConstants.ANNOTATION_REFERENCE_ADD_TO_NAME_KEY, 
				method.getName());
	}
	
	public Method getReferenceOneToOneDissociator(final EReference reference) {
		String dissociatorMethodName = 
			_emfAnnotations.getAnnotationDetail(
					_emfAnnotations.methodNamesAnnotationFor(reference),
					StandardProgModelConstants.ANNOTATION_REFERENCE_DISSOCIATE_NAME_KEY);
		return findMethod(
				javaClassFor(reference), 
				dissociatorMethodName, 
				typeAsArrayFor(reference));
	}
	public void setReferenceOneToOneDissociator(final EReference reference, Method method) {
		_emfAnnotations.putAnnotationDetails(	
				reference,
				StandardProgModelConstants.ANNOTATION_SOURCE_METHOD_NAMES, 
				StandardProgModelConstants.ANNOTATION_REFERENCE_DISSOCIATE_NAME_KEY, 
				method.getName());
	}

	public Method getReferenceCollectionDissociator(final EReference reference) {
		String dissociatorMethodName = 
			_emfAnnotations.getAnnotationDetail(
					_emfAnnotations.methodNamesAnnotationFor(reference),
					StandardProgModelConstants.ANNOTATION_REFERENCE_REMOVE_FROM_NAME_KEY);
		return findMethod(
				javaClassFor(reference), 
				dissociatorMethodName, 
				typeAsArrayFor(reference));
	}
	public void setReferenceCollectionDissociator(final EReference reference, Method method) {
		_emfAnnotations.putAnnotationDetails(	
				reference,
				StandardProgModelConstants.ANNOTATION_SOURCE_METHOD_NAMES, 
				StandardProgModelConstants.ANNOTATION_REFERENCE_REMOVE_FROM_NAME_KEY, 
				method.getName());
	}
	
	// operations
	public Method getOperationMethod(final EOperation eOperation) {
		String invokerMethodName = 
			_emfAnnotations.getAnnotationDetail(
				_emfAnnotations.methodNamesAnnotationFor(eOperation),
				StandardProgModelConstants.ANNOTATION_OPERATION_METHOD_NAME_KEY);
		return findMethod(javaClassFor(eOperation), invokerMethodName, parameterTypesFor(eOperation));
	}
	public void setOperationMethod(final EOperation eOperation, Method method) {
		_emfAnnotations.putAnnotationDetails(	
				eOperation,
				StandardProgModelConstants.ANNOTATION_SOURCE_METHOD_NAMES, 
				StandardProgModelConstants.ANNOTATION_OPERATION_METHOD_NAME_KEY, 
				method.getName());
	}
	
	public void setOperationStatic(EOperation eOperation) {
		_emfAnnotations.annotationOf(
			eOperation, 
			StandardProgModelConstants.ANNOTATION_OPERATION_STATIC);	}
	

}
