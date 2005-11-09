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
import org.essentialplatform.progmodel.essential.app.Optional;
import org.essentialplatform.progmodel.essential.app.Regex;
import org.essentialplatform.progmodel.essential.app.RelativeOrder;
import org.essentialplatform.progmodel.essential.core.EssentialProgModelExtendedSemanticsConstants;
import org.essentialplatform.progmodel.essential.core.domain.OppositeReferencesIdentifier;
import org.essentialplatform.progmodel.essential.core.emf.EssentialProgModelExtendedSemanticsEmfSerializer;
import org.essentialplatform.progmodel.essential.core.emf.EssentialProgModelStandardSemanticsEmfSerializer;
import org.essentialplatform.runtime.RuntimeDeployment.RuntimeAttributeBinding;
import org.essentialplatform.runtime.RuntimeDeployment.RuntimeClassBinding;

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
		Class<?> javaClass = ((RuntimeClassBinding)domainClass.getBinding()).getJavaClass();
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
			RuntimeAttributeBinding attributeBinding = (RuntimeAttributeBinding)iAttribute.getBinding();

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
					}
				}
			}
		}
	}
	
	private <V> void processAccessorPre(EAttribute eAttribute, IDomainClass domainClass, Class<V> javaClass) {
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
		serializer.setAttributeAccessorPreMethod(eAttribute, accessorPreCandidate);
	}

	private <V> void processMutatorPre(EAttribute eAttribute, IDomainClass domainClass, Class<V> javaClass) {
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
		serializer.setAttributeMutatorPreMethod(eAttribute, mutatorPreCandidate);
	}

	private <V> void processAccessorPre(EReference eReference, IDomainClass domainClass, Class<V> javaClass) {
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
		serializer.setReferenceAccessorPreMethod(eReference, accessorPreCandidate);
	}

	private <V> void processMutatorPre(EReference eReference, IDomainClass domainClass, Class<V> javaClass) {
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
		serializer.setReferenceMutatorPreMethod(eReference, mutatorPreCandidate);
	}


	private <V> void processAddToPre(EReference reference, IDomainClass domainClass, Class<V> javaClass) {
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
		serializer.setReferenceAddToPreMethod(reference, addToPreCandidate);
	}


	private <V> void processRemoveFromPre(EReference reference, IDomainClass domainClass, Class<V> javaClass) {
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
		serializer.setReferenceRemoveFromPre(reference, removeFromPreCandidate);
	}


	private <V> void processInvokerPre(EOperation eOperation, IDomainClass domainClass, Class<V> javaClass, Method invoker) {
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
		serializer.setOperationPreMethod(eOperation, invokerPreCandidate);
	}


	/*
	 * TODO: there is similar code in the serializer (getter), so this should probably be moved into there
	 */
	private <V> void processInvokerDefaults(EOperation eOperation, IDomainClass domainClass, Class<V> javaClass, Method invoker) {
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
		serializer.setOperationDefaults(eOperation, invokerDefaultsCandidate);
	}

	private boolean returnsString(final EAttribute attribute) {
		EDataType dataType = attribute.getEAttributeType();
		String instanceClassName = dataType.getInstanceClassName();
		return instanceClassName != null && instanceClassName.equals("java.lang.String");
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
	
	private boolean isPublic(Method method) {
		return Modifier.isPublic(method.getModifiers());
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

