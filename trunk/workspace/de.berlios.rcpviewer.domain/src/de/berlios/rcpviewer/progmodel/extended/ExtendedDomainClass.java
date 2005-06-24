package de.berlios.rcpviewer.progmodel.extended;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EModelElement;

import de.berlios.rcpviewer.domain.AbstractDomainClassAdapter;
import de.berlios.rcpviewer.domain.EmfFacade;
import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.progmodel.standard.StandardProgModelConstants;
import de.berlios.rcpviewer.progmodel.extended.ExtendedProgModelConstants;
import de.berlios.rcpviewer.progmodel.standard.InDomain;


/**
 * Extension of {@link IDomainClass} that supports semantics of the
 * extended programming model.
 * 
 * <p>
 * Typical usage:
 * <pre>
 * IDomainClass<T> someDC = ...
 * ExtendedDomainClass someExtendedDC = someDC.getAdapter(ExtendedDomainClass.class);
 * </pre>
 * 
 * @author dkhaywood
 *
 */
public final class ExtendedDomainClass<T> extends AbstractDomainClassAdapter<T> {

	
	public ExtendedDomainClass(IDomainClass<T> adaptedDomainClass) {
		super(adaptedDomainClass);
	}

	/**
	 * To support the PositionedAt annotation on attributes, use the
	 * {@link AttributeComparator} to compare a collection of attributes as
	 * returned from {@link IDomainClass#attributes()}.
	 * 
	 * <p>
	 * Typical usage:
	 * <pre>
	 * List<EAttribute> sortedAttributes =
	 *     someExtendedDC.attributeComparator(someDC.attributes());
	 * </pre> 
	 * 
	 * @return
	 */
	public AttributeComparator attributeComparator() {
		return new AttributeComparator();
	}
	
	/**
	 * Whether this class may be searched for by the UI in some generic
	 * search mechanism, eg Search.
	 * 
	 * @return
	 */
	public boolean isSearchable() {
		EAnnotation annotation = 
			adapts().getEClass().getEAnnotation(StandardProgModelConstants.ANNOTATION_ELEMENT);
		if (annotation == null) {
			return false;
		}
		String searchable = 
			(String)annotation.getDetails().get(ExtendedProgModelConstants.ANNOTATION_ELEMENT_SEARCHABLE_KEY);
		return "true".equals(searchable);
	}


	/**
	 * Whether this class can be instantiated generically, eg File>New.
	 * 
	 * <p>
	 * The majority of domain classes are expected to be persistable.
	 * 
	 * <p>
	 * In programming model: @InDomain(instantiable=false).
	 * 
	 * @return
	 */
	public boolean isInstantiable() {
		EAnnotation annotation = 
			adapts().getEClass().getEAnnotation(StandardProgModelConstants.ANNOTATION_ELEMENT);
		if (annotation == null) {
			return false;
		}
		String instantiable = 
			(String)annotation.getDetails().get(ExtendedProgModelConstants.ANNOTATION_ELEMENT_INSTANTIABLE_KEY);
		return "true".equals(instantiable);
	}

	/**
	 * Whether instances of this class can be persisted, eg File>Save.
	 * 
	 * <p>
	 * The majority of domain classes are expected to not be directly 
	 * persistable.
	 * 
	 * <p>
	 * In programming model: @InDomain(nonPersistable=false).
	 * 
	 * @return
	 */
	public boolean isSaveable() {
		EAnnotation annotation = 
			adapts().getEClass().getEAnnotation(StandardProgModelConstants.ANNOTATION_ELEMENT);
		if (annotation == null) {
			return false;
		}
		String saveable = 
				(String)annotation.getDetails().get(ExtendedProgModelConstants.ANNOTATION_ELEMENT_SAVEABLE_KEY);
		return "true".equals(saveable);
	}

}
