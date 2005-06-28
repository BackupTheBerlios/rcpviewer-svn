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
import de.berlios.rcpviewer.progmodel.standard.InDomain;

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

	private <V> void build(IRuntimeDomainClass<V> domainClass) {
		Class<V> javaClass = domainClass.getJavaClass();
		
		// Instantiable (File>New)
		addIfInstantiable(javaClass.getAnnotation(Lifecycle.class), domainClass.getEClass());

		// Searchable (Search>???)
		addIfSearchable(javaClass.getAnnotation(Lifecycle.class), domainClass.getEClass());

		// Saveable (File>Save)
		addIfSaveable(javaClass.getAnnotation(Lifecycle.class), domainClass.getEClass());

		// PositionedAt: installs an adapter
		domainClass.setAdapterFactory(ExtendedDomainClass.class, 
			new ExtendedDomainClassAdapterFactory<ExtendedDomainClass>());
		
		for(EAttribute eAttribute: domainClass.attributes()) {
			Method accessorOrMutator = domainClass.getAccessorOrMutatorFor(eAttribute);
			Order positionedAt = 
				accessorOrMutator.getAnnotation(Order.class);
			if (positionedAt != null) {
				EAnnotation attributeAnnotation = 
					emfFacade.annotationOf(eAttribute, de.berlios.rcpviewer.progmodel.extended.ExtendedProgModelConstants.ANNOTATION_ATTRIBUTE);
				Map<String,String> details = new HashMap<String,String>();
				details.put(de.berlios.rcpviewer.progmodel.extended.ExtendedProgModelConstants.ANNOTATION_ATTRIBUTE_POSITIONED_AT_KEY, "" + positionedAt.value());
				emfFacade.putAnnotationDetails(attributeAnnotation, details);	
			}
		}

	}
	
	private void addIfInstantiable(Lifecycle lifecycle, EModelElement modelElement) {
		putAnnotationDetails(modelElement, 
				ExtendedProgModelConstants.ANNOTATION_ELEMENT_INSTANTIABLE_KEY, 
				lifecycle != null && lifecycle.instantiable());	
	}

	private void addIfSearchable(Lifecycle lifecycle, EModelElement modelElement) {
		putAnnotationDetails(modelElement, 
				ExtendedProgModelConstants.ANNOTATION_ELEMENT_SEARCHABLE_KEY, 
				lifecycle != null && lifecycle.searchable());	
	}
	
	private void addIfSaveable(Lifecycle lifecycle, EModelElement modelElement) {
		putAnnotationDetails(modelElement, 
				ExtendedProgModelConstants.ANNOTATION_ELEMENT_SAVEABLE_KEY, 
				lifecycle != null && lifecycle.saveable());	
	}

	private void putAnnotationDetails(EModelElement modelElement, String key, boolean value) {
		EAnnotation ea = modelElement.getEAnnotation(StandardProgModelConstants.ANNOTATION_ELEMENT);
		if (ea == null) {
			ea = emfFacade.annotationOf(modelElement, StandardProgModelConstants.ANNOTATION_ELEMENT);
		}
		putAnnotationDetails(ea, key, value?"true":"false");
	}
	private final EmfFacade emfFacade = new EmfFacade();
	
	// TODO: used in DomainClass itself; move to some common library.
	EAnnotation putAnnotationDetails(EAnnotation eAnnotation, String key, String value) {
		Map<String, String> details = new HashMap<String, String>();
		details.put(key, value);
		return emfFacade.putAnnotationDetails(eAnnotation, details);
	}
	
	
}
