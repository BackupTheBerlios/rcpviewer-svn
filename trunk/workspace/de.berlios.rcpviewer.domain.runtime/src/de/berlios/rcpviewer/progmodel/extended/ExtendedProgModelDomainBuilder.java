package de.berlios.rcpviewer.progmodel.extended;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EAttribute;
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
 * Adds annotations specific to the RCPViewer.
 * 
 * <p>
 * Specifically, these are:
 * <ul>
 * <li>PositionedAt.
 * </ul>
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
		domainClass.setAdapterFactory(ExtendedDomainClass.class, adapterFactory);
		domainClass.setAdapterFactory(ExtendedRuntimeDomainClass.class, adapterFactory);

		// Instantiable (File>New)
		processClassInstantiable(javaClass.getAnnotation(Lifecycle.class), domainClass.getEClass());

		// Searchable (Search>???)
		processClassSearchable(javaClass.getAnnotation(Lifecycle.class), domainClass.getEClass());

		// Saveable (File>Save)
		processClassSaveable(javaClass.getAnnotation(Lifecycle.class), domainClass.getEClass());

		for(EAttribute eAttribute: domainClass.attributes()) {
			Method accessorOrMutator = domainClass.getAccessorOrMutatorFor(eAttribute);

			// Order (attributes)
			processAttributeOrder(eAttribute, accessorOrMutator);
			
			// XxxPre method to support constraintFor()
			processAttributePre(eAttribute, domainClass, javaClass);
			
			// Optional (attributes)
			processAttributeOptional(eAttribute, accessorOrMutator);
				
			// Invisible (attributes)
			processAttributeInvisible(eAttribute, accessorOrMutator);
				
		}

		for(EOperation eOperation: domainClass.operations()) {
			Method invoker = domainClass.getInvokerFor(eOperation);
			processOperationParametersOptional(eOperation, invoker);
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
			EAnnotation attributeAnnotation = 
				emfFacade.annotationOf(eAttribute, de.berlios.rcpviewer.progmodel.extended.ExtendedProgModelConstants.ANNOTATION_ATTRIBUTE);
			Map<String,String> details = new HashMap<String,String>();
			details.put(de.berlios.rcpviewer.progmodel.extended.ExtendedProgModelConstants.ANNOTATION_ATTRIBUTE_ORDER_KEY, "" + order.value());
			emfFacade.putAnnotationDetails(attributeAnnotation, details);	
		}
	}

	private void processAttributeOptional(EAttribute eAttribute, Method accessorOrMutator) {
		Optional optional = 
			accessorOrMutator.getAnnotation(Optional.class);
		if (optional != null) {
			EAnnotation attributeAnnotation = 
				emfFacade.annotationOf(eAttribute, de.berlios.rcpviewer.progmodel.extended.ExtendedProgModelConstants.ANNOTATION_ATTRIBUTE);
			Map<String,String> details = new HashMap<String,String>();
			details.put(de.berlios.rcpviewer.progmodel.extended.ExtendedProgModelConstants.ANNOTATION_ATTRIBUTE_OPTIONAL_KEY, "true");
			emfFacade.putAnnotationDetails(attributeAnnotation, details);	
		}
	}

	private void processAttributeInvisible(EAttribute eAttribute, Method accessorOrMutator) {
		Invisible invisible = 
			accessorOrMutator.getAnnotation(Invisible.class);
		if (invisible != null) {
			EAnnotation attributeAnnotation = 
				emfFacade.annotationOf(eAttribute, de.berlios.rcpviewer.progmodel.extended.ExtendedProgModelConstants.ANNOTATION_ATTRIBUTE);
			Map<String,String> details = new HashMap<String,String>();
			details.put(de.berlios.rcpviewer.progmodel.extended.ExtendedProgModelConstants.ANNOTATION_ATTRIBUTE_INVISIBLE_KEY, "true");
			emfFacade.putAnnotationDetails(attributeAnnotation, details);	
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
					emfFacade.putAnnotationDetails(parameter,
							ExtendedProgModelConstants.ANNOTATION_OPERATION_PARAMETER, 
							ExtendedProgModelConstants.ANNOTATION_OPERATION_PARAMETER_OPTIONAL_KEY, 
							"true");
				}
			}
		}
	}


	private <V> void processAttributePre(EAttribute eAttribute, IRuntimeDomainClass<V> domainClass, Class<V> javaClass) {
		Method accessor = domainClass.getAccessorFor(eAttribute);
		if (accessor == null) {
			return;
		}
		String accessorPreMethodName = accessor.getName() + 
			ExtendedProgModelConstants.PRECONDITIONS_ATTRIBUTE_SUFFIX;
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
		emfFacade.putAnnotationDetails(
				domainClass, 
				emfFacade.methodNamesAnnotationFor(eAttribute),  
				ExtendedProgModelConstants.ANNOTATION_ATTRIBUTE_PRECONDITION_METHOD_NAME_KEY, 
				accessorPreCandidate.getName());
		

	}



	private boolean methodReturns(Method method, Class javaClass) {
		return javaClass.isAssignableFrom(method.getReturnType());
	}
}

