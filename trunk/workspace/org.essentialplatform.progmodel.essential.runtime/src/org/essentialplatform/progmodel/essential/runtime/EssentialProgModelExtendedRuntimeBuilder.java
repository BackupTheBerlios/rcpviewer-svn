package org.essentialplatform.progmodel.essential.runtime;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.EReference;
import org.essentialplatform.core.domain.DomainClass;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.domain.builders.IDomainBuilder;
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
import org.essentialplatform.progmodel.essential.app.MultiLine;
import org.essentialplatform.progmodel.essential.app.Optional;
import org.essentialplatform.progmodel.essential.app.Regex;
import org.essentialplatform.progmodel.essential.app.RelativeOrder;
import org.essentialplatform.progmodel.essential.core.EssentialProgModelExtendedSemanticsConstants;
import org.essentialplatform.progmodel.essential.core.domain.OppositeReferencesIdentifier;
import org.essentialplatform.progmodel.essential.core.emf.EssentialProgModelExtendedSemanticsEmfSerializer;
import org.essentialplatform.progmodel.essential.core.emf.EssentialProgModelStandardSemanticsEmfSerializer;
import org.essentialplatform.runtime.client.RuntimeClientBinding.RuntimeClientAttributeBinding;
import org.essentialplatform.runtime.client.RuntimeClientBinding.RuntimeClientClassBinding;

/**
 * Analyzes annotations specific to the extended programming model.
 * 
 * @author Dan Haywood
 *
 */
class EssentialProgModelExtendedRuntimeBuilder implements IDomainBuilder {

	private final EssentialProgModelStandardSemanticsEmfSerializer standardSerializer = new EssentialProgModelStandardSemanticsEmfSerializer();
	private final EssentialProgModelExtendedSemanticsEmfSerializer serializer = new EssentialProgModelExtendedSemanticsEmfSerializer();

	/**
	 * Prior to rev 521, this code also installed an adapter factory that was 
	 * capable of instantiating an "extended domain object".  Subsequent to
	 * rev 521, this functionality has been moved into domain object itself 
	 * (and its bindings).
	 */
	public void build(IDomainClass domainClass) {
		Class<?> javaClass = ((RuntimeClientClassBinding)domainClass.getBinding()).getJavaClass();
		EClass eClass = domainClass.getEClass();
		

		// Instantiable (File>New)
		// Searchable (Search>???)
		// Saveable (File>Save)
		Lifecycle lifecycle = javaClass.getAnnotation(Lifecycle.class);
		serializer.setClassLifecycle(eClass, lifecycle);

		for(IDomainClass.IAttribute iAttribute: domainClass.iAttributes()) {
			EAttribute eAttribute = iAttribute.getEAttribute();
			processAccessorPre(eAttribute, domainClass, javaClass); // getXxxPre() method
			processMutatorPre(eAttribute, domainClass, javaClass); // setXxxPre(..) method

			// serialize extended semantics as EMF annotations
			RuntimeClientAttributeBinding attributeBinding = (RuntimeClientAttributeBinding)iAttribute.getBinding();

			serializer.setAttributeRelativeOrder(eAttribute, attributeBinding.getAnnotation(RelativeOrder.class));
			serializer.setAttributeId(eAttribute, attributeBinding.getAnnotation(Id.class));
			serializer.setOptional(eAttribute, attributeBinding.getAnnotation(Optional.class));
			serializer.setAttributeInvisible(eAttribute, attributeBinding.getAnnotation(Invisible.class));
			serializer.setAttributeBusinessKey(eAttribute, attributeBinding.getAnnotation(BusinessKey.class));
			serializer.setAttributeMask(eAttribute, attributeBinding.getAnnotation(Mask.class));
			serializer.setAttributeRegex(eAttribute, attributeBinding.getAnnotation(Regex.class));
			serializer.setAttributeImmutableOncePersisted(eAttribute, attributeBinding.getAnnotation(ImmutableOncePersisted.class));

			if (returnsString(eAttribute)) {
				serializer.setMinLengthOf(eAttribute, attributeBinding.getAnnotation(MinLengthOf.class));
				serializer.setMaxLengthOf(eAttribute, attributeBinding.getAnnotation(MaxLengthOf.class));
				serializer.setFieldLengthOf(eAttribute, attributeBinding.getAnnotation(FieldLengthOf.class));
				serializer.setMultiLine(eAttribute, attributeBinding.getAnnotation(MultiLine.class));
			}
		}

		for(IDomainClass.IReference iReference: domainClass.iReferences()) {
			EReference eReference = iReference.getEReference();
			processAccessorPre(eReference, domainClass, javaClass); // getXxxPre() method
			processMutatorPre(eReference, domainClass, javaClass); // setXxxPre(..) method (simple reference only)
			processAddToPre(eReference, domainClass, javaClass); // addToXxxPre(..) method (collections only)
			processRemoveFromPre(eReference, domainClass, javaClass); // removeFromXxxPre(..) method (collections only)
		}
		
		for(IDomainClass.IOperation iOperation: domainClass.iOperations()) {
			EOperation operation = iOperation.getEOperation();
			Method invoker = standardSerializer.getOperationMethod(operation);

			// xxxPre(..) method
			processInvokerPre(operation, domainClass, javaClass, invoker); 
			// xxxDefaults(..) method
			processInvokerDefaults(operation, domainClass, javaClass, invoker); 
			
			Class<?>[] parameterTypes = invoker.getParameterTypes();
			Annotation[][] parameterAnnotationSets = invoker.getParameterAnnotations();
			for(int i=0; i<parameterTypes.length; i++) {
				EParameter parameter = (EParameter)operation.getEParameters().get(i);
				Annotation[] parameterAnnotationSet = parameterAnnotationSets[i];
				for(Annotation parameterAnnotation: parameterAnnotationSet) {
					// Optional
					if (parameterAnnotation instanceof Optional) {
						serializer.setOptional(parameter, (Optional)parameterAnnotation);
					}
					if (parameterTypes[i] == java.lang.String.class) {
						// MinLengthOf
						if (parameterAnnotation instanceof MinLengthOf) {
							serializer.setMinLengthOf(parameter, (MinLengthOf)parameterAnnotation);
						}
						// MaxLengthOf
						if (parameterAnnotation instanceof MaxLengthOf) {
							serializer.setMaxLengthOf(parameter, (MaxLengthOf)parameterAnnotation);
						}
						// FieldLengthOf
						if (parameterAnnotation instanceof FieldLengthOf) {
							serializer.setFieldLengthOf(parameter, (FieldLengthOf)parameterAnnotation);
						}
						// MultiLine
						if (parameterAnnotation instanceof MultiLine) {
							serializer.setMultiLine(parameter, (MultiLine)parameterAnnotation);
						}
					}
				}
			}
		}
	}
	
	private <V> void processAccessorPre(EAttribute eAttribute, IDomainClass domainClass, Class<V> javaClass) {
		serializer.setAttributeAccessorPreMethodIfPossible(eAttribute, javaClass);
	}

	private <V> void processMutatorPre(EAttribute eAttribute, IDomainClass domainClass, Class<V> javaClass) {
		serializer.setAttributeMutatorPreMethodIfPossible(eAttribute, javaClass);
	}

	private <V> void processAccessorPre(EReference eReference, IDomainClass domainClass, Class<V> javaClass) {
		serializer.setReferenceAccessorPreMethodIfPossible(eReference, javaClass);
	}

	private <V> void processMutatorPre(EReference eReference, IDomainClass domainClass, Class<V> javaClass) {
		serializer.setReferenceMutatorPreMethodIfPossible(eReference, javaClass);
	}

	private <V> void processAddToPre(EReference reference, IDomainClass domainClass, Class<V> javaClass) {
		serializer.setReferenceAddToPreMethodIfPossible(reference, javaClass);
	}


	private <V> void processRemoveFromPre(EReference reference, IDomainClass domainClass, Class<V> javaClass) {
		serializer.setReferenceRemoveFromPreMethodIfPossible(reference, javaClass);
	}


	private <V> void processInvokerPre(EOperation eOperation, IDomainClass domainClass, Class<V> javaClass, Method invoker) {
		serializer.setOperationPreMethodIfPossible(eOperation, javaClass, invoker);
	}


	/*
	 * TODO: there is similar code in the serializer (getter), so this should probably be moved into there
	 */
	private <V> void processInvokerDefaults(EOperation eOperation, IDomainClass domainClass, Class<V> javaClass, Method invoker) {
		serializer.setOperationDefaultsIfPossible(eOperation, javaClass, invoker);
	}

	private boolean returnsString(final EAttribute attribute) {
		EDataType dataType = attribute.getEAttributeType();
		String instanceClassName = dataType.getInstanceClassName();
		return instanceClassName != null && instanceClassName.equals("java.lang.String");
	}
	
	/**
	 * Does nothing.
	 * 
	 * <p>
	 * The companion {@link ExtendedProgModelStandardRuntimeBuilder} does the
	 * heavy lifting here.
	 */
	public void identifyOppositeReferencesFor(IDomainClass domainClass) {
		// does nothing		
	}

}

