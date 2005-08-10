package de.berlios.rcpviewer.progmodel.extended;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.EcoreFactory;

import de.berlios.rcpviewer.domain.EmfFacade;
import de.berlios.rcpviewer.domain.IDomainBuilder;
import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.domain.IRuntimeDomainClass;
import de.berlios.rcpviewer.domain.MethodNameHelper;
import de.berlios.rcpviewer.progmodel.standard.DescribedAs;
import de.berlios.rcpviewer.progmodel.standard.StandardProgModelConstants;

/**
 * Analyzes annotations specific to the extended programming model.
 * 
 * @author Dan Haywood
 *
 */
public class ExtendedProgModelDomainBuilder implements IDomainBuilder {

	public <V> void build(IDomainClass<V> domainClass) {
		build((IRuntimeDomainClass<V>)domainClass);
	}

	private final EmfFacade emfFacade = new EmfFacade();
	
	private <V> void build(IRuntimeDomainClass<V> domainClass) {
		Class<V> javaClass = domainClass.getJavaClass();
		
		// Install one adapter object under two different bindings so can be
		// obtained in either context.
		ExtendedDomainClassAdapterFactory<ExtendedRuntimeDomainClass> adapterFactory = 
					new ExtendedDomainClassAdapterFactory<ExtendedRuntimeDomainClass>();
		domainClass.setAdapterFactory(IExtendedDomainClass.class, adapterFactory);
		domainClass.setAdapterFactory(IExtendedRuntimeDomainClass.class, adapterFactory);

		// Instantiable (File>New)
		processClassInstantiable(javaClass.getAnnotation(Lifecycle.class), domainClass.getEClass());

		// Searchable (Search>???)
		processClassSearchable(javaClass.getAnnotation(Lifecycle.class), domainClass.getEClass());

		// Saveable (File>Save)
		processClassSaveable(javaClass.getAnnotation(Lifecycle.class), domainClass.getEClass());

		for(EAttribute eAttribute: domainClass.attributes()) {
			Method accessorOrMutator = domainClass.getAccessorOrMutatorFor(eAttribute);
			
			processAccessorPre(eAttribute, domainClass, javaClass); // getXxxPre() method
			processMutatorPre(eAttribute, domainClass, javaClass); // setXxxPre(..) method

			// annotations
			processAttributeOrder(eAttribute, accessorOrMutator);
			processAttributeOptional(eAttribute, accessorOrMutator);
			processAttributeInvisible(eAttribute, accessorOrMutator);
			processAttributeBusinessKey(eAttribute, accessorOrMutator);
			processAttributeMinLengthOf(eAttribute, accessorOrMutator);
			processAttributeMaxLengthOf(eAttribute, accessorOrMutator);
			processAttributeFieldLengthOf(eAttribute, accessorOrMutator);
			processAttributeMask(eAttribute, accessorOrMutator);
			processAttributeRegex(eAttribute, accessorOrMutator);
			processAttributeImmutableOncePersisted(eAttribute, accessorOrMutator);
		
		}

		for(EReference eReference: domainClass.references()) {
			
			processAccessorPre(eReference, domainClass, javaClass); // getXxxPre() method
			processMutatorPre(eReference, domainClass, javaClass); // setXxxPre(..) method (simple reference only)
			processAddToPre(eReference, domainClass, javaClass); // addToXxxPre(..) method (collections only)
			processRemoveFromPre(eReference, domainClass, javaClass); // removeFromXxxPre(..) method (collections only)
			
		}
		
		for(EOperation eOperation: domainClass.operations()) {
			Method invoker = domainClass.getInvokerFor(eOperation);
			processOperationParametersOptional(eOperation, invoker);
			processOperationParameterMinLengthOf(eOperation, invoker);
			processOperationParameterMaxLengthOf(eOperation, invoker);
			processOperationParameterFieldLengthOf(eOperation, invoker);

			processInvokerPre(eOperation, domainClass, javaClass, invoker); // xxxPre(..) method
			processInvokerDefaults(eOperation, domainClass, javaClass, invoker); // xxxPre(..) method

		}

	}
	
	private void processClassInstantiable(Lifecycle lifecycle, EModelElement modelElement) {
		emfFacade.putAnnotationDetails(modelElement, 
				ExtendedProgModelConstants.ANNOTATION_CLASS, 
				ExtendedProgModelConstants.ANNOTATION_CLASS_INSTANTIABLE_KEY, 
				lifecycle != null && lifecycle.instantiable());	
	}

	private void processClassSearchable(Lifecycle lifecycle, EModelElement modelElement) {
		emfFacade.putAnnotationDetails(modelElement, 
				ExtendedProgModelConstants.ANNOTATION_CLASS, 
				ExtendedProgModelConstants.ANNOTATION_CLASS_SEARCHABLE_KEY, 
				lifecycle != null && lifecycle.searchable());	
	}
	
	private void processClassSaveable(Lifecycle lifecycle, EModelElement modelElement) {
		emfFacade.putAnnotationDetails(modelElement, 
				ExtendedProgModelConstants.ANNOTATION_CLASS, 
				ExtendedProgModelConstants.ANNOTATION_CLASS_SAVEABLE_KEY, 
				lifecycle != null && lifecycle.saveable());	
	}

	private void processAttributeOrder(EAttribute eAttribute, Method accessorOrMutator) {
		Order order = 
			accessorOrMutator.getAnnotation(Order.class);
		if (order != null) {
			emfFacade.putAnnotationDetails(eAttribute,
					ExtendedProgModelConstants.ANNOTATION_ATTRIBUTE, 
					ExtendedProgModelConstants.ANNOTATION_ATTRIBUTE_ORDER_KEY, 
					"" + order.value());
		}
	}


	private void processAttributeOptional(EAttribute eAttribute, Method accessorOrMutator) {
		Optional optional = 
			accessorOrMutator.getAnnotation(Optional.class);
		if (optional != null) {
			setOptional(eAttribute, optional);
		}
	}
	private void processOperationParametersOptional(EOperation operation, final Method invoker) {
		Class<?>[] parameterTypes = invoker.getParameterTypes();
		Annotation[][] parameterAnnotationSets = invoker.getParameterAnnotations();
		for(int i=0; i<parameterTypes.length; i++) {
			EParameter parameter = (EParameter)operation.getEParameters().get(i);
			Annotation[] parameterAnnotationSet = parameterAnnotationSets[i];
			for(Annotation parameterAnnotation: parameterAnnotationSet) {
				if (parameterAnnotation instanceof Optional) {
					setOptional(parameter, (Optional)parameterAnnotation);
				}
			}
		}
	}
	private void setOptional(final EModelElement modelElement, final Optional optional) {
		emfFacade.putAnnotationDetails(modelElement,
				ExtendedProgModelConstants.ANNOTATION_ELEMENT, 
				ExtendedProgModelConstants.ANNOTATION_ELEMENT_OPTIONAL_KEY, 
				"true");
	}
	
	private void processAttributeInvisible(EAttribute eAttribute, Method accessorOrMutator) {
		Invisible invisible = 
			accessorOrMutator.getAnnotation(Invisible.class);
		if (invisible != null) {
			emfFacade.putAnnotationDetails(eAttribute,
					ExtendedProgModelConstants.ANNOTATION_ATTRIBUTE, 
					ExtendedProgModelConstants.ANNOTATION_ATTRIBUTE_INVISIBLE_KEY, 
					"true");
		}
	}

	private void processAttributeBusinessKey(EAttribute eAttribute, Method accessorOrMutator) {
		BusinessKey businessKey = 
			accessorOrMutator.getAnnotation(BusinessKey.class);
		if (businessKey != null) {
			emfFacade.putAnnotationDetails(eAttribute,
					ExtendedProgModelConstants.ANNOTATION_ATTRIBUTE, 
					ExtendedProgModelConstants.ANNOTATION_ATTRIBUTE_BUSINESS_KEY_NAME_KEY, 
					businessKey.name());
			emfFacade.putAnnotationDetails(eAttribute,
					ExtendedProgModelConstants.ANNOTATION_ATTRIBUTE, 
					ExtendedProgModelConstants.ANNOTATION_ATTRIBUTE_BUSINESS_KEY_POS_KEY, 
					""+businessKey.pos());
		}
	}

	private void processAttributeMinLengthOf(EAttribute eAttribute, Method accessorOrMutator) {
		if (!returnsString(eAttribute)) {
			return;
		}
		MinLengthOf minLengthOf = 
			accessorOrMutator.getAnnotation(MinLengthOf.class);
		if (minLengthOf != null) {
			setMinLengthOf(eAttribute, minLengthOf);
		}
	}
	private void processOperationParameterMinLengthOf(EOperation operation, Method invoker) {
		Class<?>[] parameterTypes = invoker.getParameterTypes();
		Annotation[][] parameterAnnotationSets = invoker.getParameterAnnotations();
		for(int i=0; i<parameterTypes.length; i++) {
			if (parameterTypes[i] != java.lang.String.class) {
				continue;
			}
			EParameter parameter = (EParameter)operation.getEParameters().get(i);
			Annotation[] parameterAnnotationSet = parameterAnnotationSets[i];

			for(Annotation parameterAnnotation: parameterAnnotationSet) {
				if (parameterAnnotation instanceof MinLengthOf) {
					MinLengthOf minLengthOf = (MinLengthOf)parameterAnnotation;
					setMinLengthOf(parameter, minLengthOf);
				}
			}
		}
	}
	private void setMinLengthOf(final EModelElement modelElement, MinLengthOf minLengthOf) {
		emfFacade.putAnnotationDetails(modelElement,
				ExtendedProgModelConstants.ANNOTATION_ELEMENT, 
				ExtendedProgModelConstants.ANNOTATION_ELEMENT_MIN_LENGTH_OF_KEY, 
				"" + minLengthOf.value());
	}
	
	private void processAttributeMaxLengthOf(EAttribute eAttribute, Method accessorOrMutator) {
		if (!returnsString(eAttribute)) {
			return;
		}
		MaxLengthOf maxLengthOf = 
			accessorOrMutator.getAnnotation(MaxLengthOf.class);
		if (maxLengthOf != null) {
			setMaxLengthOf(eAttribute, maxLengthOf);
		}
	}
	private void processOperationParameterMaxLengthOf(EOperation operation, Method invoker) {
		Class<?>[] parameterTypes = invoker.getParameterTypes();
		Annotation[][] parameterAnnotationSets = invoker.getParameterAnnotations();
		for(int i=0; i<parameterTypes.length; i++) {
			if (parameterTypes[i] != java.lang.String.class) {
				continue;
			}
			EParameter parameter = (EParameter)operation.getEParameters().get(i);
			Annotation[] parameterAnnotationSet = parameterAnnotationSets[i];

			for(Annotation parameterAnnotation: parameterAnnotationSet) {
				if (parameterAnnotation instanceof MaxLengthOf) {
					MaxLengthOf maxLengthOf = (MaxLengthOf)parameterAnnotation;
					setMaxLengthOf(parameter, maxLengthOf);
				}
			}
		}
	}
	private void setMaxLengthOf(final EModelElement modelElement, MaxLengthOf maxLengthOf) {
		emfFacade.putAnnotationDetails(modelElement,
				ExtendedProgModelConstants.ANNOTATION_ELEMENT, 
				ExtendedProgModelConstants.ANNOTATION_ELEMENT_MAX_LENGTH_OF_KEY, 
				"" + maxLengthOf.value());
	}


	private void processAttributeFieldLengthOf(EAttribute eAttribute, Method accessorOrMutator) {
		if (!returnsString(eAttribute)) {
			return;
		}
		FieldLengthOf fieldLengthOf = 
			accessorOrMutator.getAnnotation(FieldLengthOf.class);
		if (fieldLengthOf != null) {
			setFieldLengthOf(eAttribute, fieldLengthOf);
		}
	}
	private void processOperationParameterFieldLengthOf(EOperation operation, Method invoker) {
		Class<?>[] parameterTypes = invoker.getParameterTypes();
		Annotation[][] parameterAnnotationSets = invoker.getParameterAnnotations();
		for(int i=0; i<parameterTypes.length; i++) {
			if (parameterTypes[i] != java.lang.String.class) {
				continue;
			}
			EParameter parameter = (EParameter)operation.getEParameters().get(i);
			Annotation[] parameterAnnotationSet = parameterAnnotationSets[i];
			for(Annotation parameterAnnotation: parameterAnnotationSet) {
				if (parameterAnnotation instanceof FieldLengthOf) {
					FieldLengthOf fieldLengthOf = (FieldLengthOf)parameterAnnotation;
					setFieldLengthOf(parameter, fieldLengthOf);
				}
			}
		}
	}
	private void setFieldLengthOf(final EModelElement modelElement, FieldLengthOf fieldLengthOf) {
		emfFacade.putAnnotationDetails(modelElement,
				ExtendedProgModelConstants.ANNOTATION_ELEMENT, 
				ExtendedProgModelConstants.ANNOTATION_ELEMENT_FIELD_LENGTH_OF_KEY, 
				"" + fieldLengthOf.value());
	}


	private void processAttributeMask(EAttribute eAttribute, Method accessorOrMutator) {
		Mask mask = 
			accessorOrMutator.getAnnotation(Mask.class);
		if (mask != null) {
			emfFacade.putAnnotationDetails(eAttribute,
					ExtendedProgModelConstants.ANNOTATION_ELEMENT, 
					ExtendedProgModelConstants.ANNOTATION_ELEMENT_MASK_KEY, 
					mask.value());
		}
	}

	private void processAttributeRegex(EAttribute eAttribute, Method accessorOrMutator) {
		Regex regex = 
			accessorOrMutator.getAnnotation(Regex.class);
		if (regex != null) {
			emfFacade.putAnnotationDetails(eAttribute,
					ExtendedProgModelConstants.ANNOTATION_ELEMENT, 
					ExtendedProgModelConstants.ANNOTATION_ELEMENT_REGEX_KEY, 
					regex.value());
		}
	}

	private void processAttributeImmutableOncePersisted(EAttribute eAttribute, Method accessorOrMutator) {
		ImmutableOncePersisted immutableOncePersisted = 
			accessorOrMutator.getAnnotation(ImmutableOncePersisted.class);
		if (immutableOncePersisted != null) {
			emfFacade.putAnnotationDetails(eAttribute,
					ExtendedProgModelConstants.ANNOTATION_ATTRIBUTE, 
					ExtendedProgModelConstants.ANNOTATION_ATTRIBUTE_IMMUTABLE_ONCE_PERSISTED_KEY, 
					"true");
		}
	}


	private <V> void processAccessorPre(EAttribute eAttribute, IRuntimeDomainClass<V> domainClass, Class<V> javaClass) {
		Method accessor = domainClass.getAccessorFor(eAttribute);
		if (accessor == null) {
			return;
		}
		String accessorPreMethodName = accessor.getName() + 
			ExtendedProgModelConstants.SUFFIX_ELEMENT_PRECONDITIONS;
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
		emfFacade.putAnnotationDetails(
				domainClass, 
				emfFacade.methodNamesAnnotationFor(eAttribute),  
				ExtendedProgModelConstants.ANNOTATION_ATTRIBUTE_ACCESSOR_PRECONDITION_METHOD_NAME_KEY, 
				accessorPreCandidate.getName());
	}

	private <V> void processMutatorPre(EAttribute eAttribute, IRuntimeDomainClass<V> domainClass, Class<V> javaClass) {
		Method mutator = domainClass.getMutatorFor(eAttribute);
		if (mutator == null) {
			return;
		}
		Class<?> attributeType = eAttribute.getEAttributeType().getInstanceClass();
		String mutatorPreMethodName = mutator.getName() + 
			ExtendedProgModelConstants.SUFFIX_ELEMENT_PRECONDITIONS;
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
		emfFacade.putAnnotationDetails(
				domainClass, 
				emfFacade.methodNamesAnnotationFor(eAttribute),  
				ExtendedProgModelConstants.ANNOTATION_ATTRIBUTE_MUTATOR_PRECONDITION_METHOD_NAME_KEY, 
				mutatorPreCandidate.getName());
	}

	private <V> void processAccessorPre(EReference eReference, IRuntimeDomainClass<V> domainClass, Class<V> javaClass) {
		Method accessor = domainClass.getAccessorFor(eReference);
		if (accessor == null) {
			return;
		}
		String accessorPreMethodName = accessor.getName() + 
			ExtendedProgModelConstants.SUFFIX_ELEMENT_PRECONDITIONS;
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
		emfFacade.putAnnotationDetails(
				domainClass, 
				emfFacade.methodNamesAnnotationFor(eReference),  
				ExtendedProgModelConstants.ANNOTATION_REFERENCE_ACCESSOR_PRECONDITION_METHOD_NAME_KEY, 
				accessorPreCandidate.getName());
	}

	private <V> void processMutatorPre(EReference eReference, IRuntimeDomainClass<V> domainClass, Class<V> javaClass) {
		if (eReference.isMany()) {
			return;
		}
		Method accessor = domainClass.getMutatorFor(eReference);
		if (accessor == null) {
			return;
		}
		String accessorPreMethodName = accessor.getName() + 
			ExtendedProgModelConstants.SUFFIX_ELEMENT_PRECONDITIONS;
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
		emfFacade.putAnnotationDetails(
				domainClass, 
				emfFacade.methodNamesAnnotationFor(eReference),  
				ExtendedProgModelConstants.ANNOTATION_REFERENCE_MUTATOR_PRECONDITION_METHOD_NAME_KEY, 
				mutatorPreCandidate.getName());
	}


	private <V> void processAddToPre(EReference eReference, IRuntimeDomainClass<V> domainClass, Class<V> javaClass) {
		if (!eReference.isMany()) {
			return;
		}
		Method addTo = domainClass.getAssociatorFor(eReference);
		if (addTo == null) {
			return;
		}
		String addToPreMethodName = addTo.getName() + 
			ExtendedProgModelConstants.SUFFIX_ELEMENT_PRECONDITIONS;
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
		emfFacade.putAnnotationDetails(
				domainClass, 
				emfFacade.methodNamesAnnotationFor(eReference),  
				ExtendedProgModelConstants.ANNOTATION_REFERENCE_ADD_TO_PRECONDITION_METHOD_NAME_KEY, 
				addToPreCandidate.getName());
	}


	private <V> void processRemoveFromPre(EReference eReference, IRuntimeDomainClass<V> domainClass, Class<V> javaClass) {
		if (!eReference.isMany()) {
			return;
		}
		Method removeFrom = domainClass.getDissociatorFor(eReference);
		if (removeFrom == null) {
			return;
		}
		String removeFromPreMethodName = removeFrom.getName() + 
			ExtendedProgModelConstants.SUFFIX_ELEMENT_PRECONDITIONS;
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
		emfFacade.putAnnotationDetails(
				domainClass, 
				emfFacade.methodNamesAnnotationFor(eReference),  
				ExtendedProgModelConstants.ANNOTATION_REFERENCE_REMOVE_FROM_PRECONDITION_METHOD_NAME_KEY, 
				removeFromPreCandidate.getName());
	}


	private <V> void processInvokerPre(EOperation eOperation, IRuntimeDomainClass<V> domainClass, Class<V> javaClass, Method invoker) {
		String invokerName = invoker.getName();
		String invokerPreMethodName = invokerName + 
			ExtendedProgModelConstants.SUFFIX_ELEMENT_PRECONDITIONS;
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
		emfFacade.putAnnotationDetails(
				domainClass, 
				emfFacade.methodNamesAnnotationFor(eOperation),  
				ExtendedProgModelConstants.ANNOTATION_OPERATION_PRECONDITION_METHOD_NAME_KEY, 
				invokerPreCandidate.getName());
	}

	


	private <V> void processInvokerDefaults(EOperation eOperation, IRuntimeDomainClass<V> domainClass, Class<V> javaClass, Method invoker) {
		String invokerName = invoker.getName();
		String invokerDefaultsMethodName = invokerName + 
			ExtendedProgModelConstants.SUFFIX_OPERATION_DEFAULTS;
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
			invokerDefaultsCandidate = javaClass.getMethod(invokerDefaultsMethodName, parameterTypes);
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
		emfFacade.putAnnotationDetails(
				domainClass, 
				emfFacade.methodNamesAnnotationFor(eOperation),  
				ExtendedProgModelConstants.ANNOTATION_OPERATION_DEFAULTS_METHOD_NAME_KEY, 
				invokerDefaultsCandidate.getName());
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
	private boolean methodReturns(Method method, Class javaClass) {
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
	

}

