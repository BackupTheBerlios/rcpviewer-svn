package de.berlios.rcpviewer.progmodel.extended;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EAttribute;

import de.berlios.rcpviewer.domain.EmfFacade;
import de.berlios.rcpviewer.domain.IDomainBuilder;
import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.domain.IRuntimeDomainClass;

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
		analyze((IRuntimeDomainClass<V>)domainClass);
	}

	public <V> void analyze(IRuntimeDomainClass<V> domainClass) {
		Class<V> javaClass = domainClass.getJavaClass();
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
