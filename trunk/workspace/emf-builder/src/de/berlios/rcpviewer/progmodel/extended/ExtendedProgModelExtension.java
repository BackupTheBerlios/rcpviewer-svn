package de.berlios.rcpviewer.progmodel.extended;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EAttribute;

import de.berlios.rcpviewer.metamodel.EmfFacade;
import de.berlios.rcpviewer.metamodel.IDomainClass;
import de.berlios.rcpviewer.metamodel.IMetaModelExtension;

/**
 * Adds annotations specific to the RCPViewer.
 * 
 * <p>
 * Specifically, these are:
 * <ul>
 * <li>ImageDescriptor.
 * </ul>
 * 
 * @author Dan Haywood
 *
 */
public class ExtendedProgModelExtension implements IMetaModelExtension {

	public void analyze(IDomainClass<?> domainClass) {
		Class<?> javaClass = domainClass.getJavaClass();
		domainClass.setAdapterFactory(AttributeComparator.class, 
			new AttributeComparatorAdapterFactory<AttributeComparator>());
		
		for(EAttribute attribute: domainClass.attributes()) {
			Method accessorOrMutator = domainClass.getAccessorOrMutatorFor(attribute);
			PositionedAt positionedAt = 
				accessorOrMutator.getAnnotation(PositionedAt.class);
			if (positionedAt != null) {
				EAnnotation attributeAnnotation = 
					emfFacade.annotationOf(attribute, Constants.ANNOTATION_ATTRIBUTE);
				Map<String,String> details = new HashMap<String,String>();
				details.put(Constants.ANNOTATION_ATTRIBUTE_POSITIONED_AT_KEY, "" + positionedAt.value());
				emfFacade.putAnnotationDetails(attributeAnnotation, details);	
			}
		}

	}
	
	private final EmfFacade emfFacade = new EmfFacade();
}
