package de.berlios.rcpviewer.progmodel.extended;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EModelElement;

import de.berlios.rcpviewer.domain.EmfFacade;
import de.berlios.rcpviewer.domain.IDomainBuilder;
import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.domain.IRuntimeDomainClass;
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
	
	// TODO: used in DomainClass itself; move to some common library.
	EAnnotation putAnnotationDetails(EAnnotation eAnnotation, String key, String value) {
		Map<String, String> details = new HashMap<String, String>();
		details.put(key, value);
		return emfFacade.putAnnotationDetails(eAnnotation, details);
	}

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
				
		}
	}
	
	private void processClassInstantiable(Lifecycle lifecycle, EModelElement modelElement) {
		putAnnotationDetails(modelElement, 
				ExtendedProgModelConstants.ANNOTATION_CLASS_INSTANTIABLE_KEY, 
				lifecycle != null && lifecycle.instantiable());	
	}

	private void processClassSearchable(Lifecycle lifecycle, EModelElement modelElement) {
		putAnnotationDetails(modelElement, 
				ExtendedProgModelConstants.ANNOTATION_CLASS_SEARCHABLE_KEY, 
				lifecycle != null && lifecycle.searchable());	
	}
	
	private void processClassSaveable(Lifecycle lifecycle, EModelElement modelElement) {
		putAnnotationDetails(modelElement, 
				ExtendedProgModelConstants.ANNOTATION_CLASS_SAVEABLE_KEY, 
				lifecycle != null && lifecycle.saveable());	
	}

	private void putAnnotationDetails(EModelElement modelElement, String key, boolean value) {
		putAnnotationDetails(modelElement, key, value?"true":"false");
	}
	
	private void putAnnotationDetails(EModelElement modelElement, String key, String value) {
		EAnnotation ea = modelElement.getEAnnotation(StandardProgModelConstants.ANNOTATION_ELEMENT);
		if (ea == null) {
			ea = emfFacade.annotationOf(modelElement, StandardProgModelConstants.ANNOTATION_ELEMENT);
		}
		putAnnotationDetails(ea, key, value);
	}
	
	private void processAttributeOrder(EAttribute eAttribute, Method accessorOrMutator) {
		Order positionedAt = 
			accessorOrMutator.getAnnotation(Order.class);
		if (positionedAt != null) {
			EAnnotation attributeAnnotation = 
				emfFacade.annotationOf(eAttribute, de.berlios.rcpviewer.progmodel.extended.ExtendedProgModelConstants.ANNOTATION_ATTRIBUTE);
			Map<String,String> details = new HashMap<String,String>();
			details.put(de.berlios.rcpviewer.progmodel.extended.ExtendedProgModelConstants.ANNOTATION_ATTRIBUTE_ORDER_KEY, "" + positionedAt.value());
			emfFacade.putAnnotationDetails(attributeAnnotation, details);	
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

