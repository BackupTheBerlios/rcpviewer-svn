package de.berlios.rcpviewer.progmodel.extended;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EParameter;

import de.berlios.rcpviewer.domain.AbstractDomainClassAdapter;
import de.berlios.rcpviewer.domain.EmfFacade;
import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.progmodel.standard.StandardProgModelConstants;


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
public class ExtendedDomainClass<T> extends AbstractDomainClassAdapter<T>{

	private final EmfFacade emfFacade = new EmfFacade();

	
	public ExtendedDomainClass(IDomainClass<T> adaptedDomainClass) {
		super(adaptedDomainClass);
	}


	/**
	 * Returns the attributes of the extended domain class 
	 * {@link IDomainClass#attributes()} in the order defined by the
	 * {@link Order} annotation.
	 * 
	 * @return
	 */
	public List<EAttribute> orderedAttributes() {
		List<EAttribute> attributes = adapts().attributes();
		Collections.sort(attributes, new AttributeComparator());
		return attributes;
	}
	
	/**
	 * Whether this class may be searched for by the UI in some generic
	 * search mechanism, eg Search.
	 * 
	 * @return
	 */
	public boolean isSearchable() {
		EAnnotation annotation = 
			adapts().getEClass().getEAnnotation(ExtendedProgModelConstants.ANNOTATION_CLASS);
		if (annotation == null) {
			return false;
		}
		String searchable = 
			(String)annotation.getDetails().get(ExtendedProgModelConstants.ANNOTATION_CLASS_SEARCHABLE_KEY);
		return "true".equals(searchable);
	}


	/**
	 * Whether this class can be instantiated generically, eg File>New.
	 * 
	 * <p>
	 * The majority of domain classes are expected to be instantiable.
	 * 
	 * <p>
	 * In programming model: <code>@InDomain(instantiable=false)</code>.
	 * 
	 * @return
	 */
	public boolean isInstantiable() {
		EAnnotation annotation = 
			adapts().getEClass().getEAnnotation(ExtendedProgModelConstants.ANNOTATION_CLASS);
		if (annotation == null) {
			return false;
		}
		String instantiable = 
			(String)annotation.getDetails().get(ExtendedProgModelConstants.ANNOTATION_CLASS_INSTANTIABLE_KEY);
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
	 * In programming model: <code>@InDomain(nonPersistable=false)</code>.
	 * 
	 * @return
	 */
	public boolean isSaveable() {
		EAnnotation annotation = 
			adapts().getEClass().getEAnnotation(ExtendedProgModelConstants.ANNOTATION_CLASS);
		if (annotation == null) {
			return false;
		}
		String saveable = 
				(String)annotation.getDetails().get(ExtendedProgModelConstants.ANNOTATION_CLASS_SAVEABLE_KEY);
		return "true".equals(saveable);
	}

	/**
	 * Whether the specified attribute is optional for this domain class.
	 * 
	 * <p>
	 * The {@link Optional} annotation is used to indicate whether an attribute
	 * is optional or not.
	 *  
	 * @param attribute - attribute of the domain class that this is an extension of
	 * @return
	 */
	public boolean isOptional(final EAttribute attribute) {
		Map<String,String> attributeDetails = 
			emfFacade.getAnnotationDetails(attribute, ExtendedProgModelConstants.ANNOTATION_ATTRIBUTE);
		
		String optional = attributeDetails.get(
					ExtendedProgModelConstants.ANNOTATION_ATTRIBUTE_OPTIONAL_KEY);
		return optional != null;
	}

	/**
	 * Whether the specified attribute is mandatory for this domain class.
	 * 
	 * <p>
	 * This is a convenience method; its value is defined as the logical NOT of
	 * invoking {@link #isOptional(EAttribute)}.
	 * 
	 * @param attribute - attribute of the domain class that this is an extension of
	 * @return
	 */
	public boolean isMandatory(final EAttribute attribute) {
		return !isOptional(attribute);
	}

	/**
	 * Whether the specified parameter of the operation is optional for this 
	 * domain class.
	 * 
	 * <p>
	 * The {@link Optional} annotation is used to indicate whether an 
	 * operation's parameter is optional or not.
	 *  
	 * @param operation
	 * @param parameterPosition
	 * @return
	 */
	public boolean isOptional(EOperation operation, int parameterPosition) {
		EParameter parameter = (EParameter)operation.getEParameters().get(parameterPosition);

		EAnnotation annotation = 
			parameter.getEAnnotation(ExtendedProgModelConstants.ANNOTATION_OPERATION_PARAMETER);
		if (annotation == null) {
			return false;
		}
		String optional = (String)annotation.getDetails().get(
				ExtendedProgModelConstants.ANNOTATION_OPERATION_PARAMETER_OPTIONAL_KEY);
		return optional != null;
	}


	/**
	 * Whether the specified parameter of the operation is mandatory for this 
	 * domain class.
	 * 
	 * <p>
	 * This is a convenience method; its value is defined as the logical NOT of
	 * invoking {@link #isOptional(EOperation, int)}.
	 * 
	 * @param operation
	 * @param parameterPosition
	 * @return
	 */
	public boolean isMandatory(EOperation operation, int parameterPosition) {
		return !isOptional(operation, parameterPosition);
	}

	/**
	 * Whether the specified attribute is invisible for this domain class.
	 * 
	 * <p>
	 * The {@link Invisible} annotation is used to indicate whether an attribute
	 * is optional or not.
	 * 
	 * @param attribute - attribute of the domain class that this is an extension of
	 * @return
	 * 
	 * @param attribute
	 * @return
	 */
	public boolean isInvisible(EAttribute attribute) {
		Map<String,String> attributeDetails = 
			emfFacade.getAnnotationDetails(attribute, ExtendedProgModelConstants.ANNOTATION_ATTRIBUTE);
		
		String invisible = attributeDetails.get(
					ExtendedProgModelConstants.ANNOTATION_ATTRIBUTE_INVISIBLE_KEY);
		return invisible != null;
	}


	/**
	 * Returns a map keyed by name of each business key, whose value is a list
	 * of the attribute(s) that make up that business key, in the order that
	 * they were specified.
	 * 
	 * <p>
	 * The {@link BusinessKey} annotation is used to indicate which attributes
	 * are in a business key.
	 * 
	 * @return
	 */
	public Map<String, List<EAttribute>> businessKeys() {
		// TODO Auto-generated method stub
		return null;
	}


	/**
	 * Returns the field length (as displayed in the UI) of the specified
	 * (string) attribute.
	 * 
	 * <p>
	 * The {@link FieldLengthOf} annotation is used to indicate the field 
	 * length of attributes.
	 * 
	 * @param attribute
	 * @return
	 */
	public int getFieldLengthOf(EAttribute attribute) {
		return 64;
	}


	/**
	 * Returns the max length (as persisted in the persistent data store) of 
	 * the specified (string) attribute.
	 * 
	 * <p>
	 * The {@link MaxLengthOf} annotation is used to indicate the maximum 
	 * length of attributes.
	 * 
	 * @param attribute
	 * @return
	 */
	public int getMaxLengthOf(EAttribute attribute) {
		return 64;
	}

	/**
	 * Returns the min length (as required to be entered in the UI) of 
	 * the specified (string) attribute.
	 * 
	 * <p>
	 * The {@link MinLengthOf} annotation is used to indicate the minimum 
	 * length of attributes.
	 * 
	 * @param attribute
	 * @return
	 */
	public int getMinLengthOf(EAttribute attribute) {
		return 0;
	}


	/**
	 * Whether this attribute can be edited for as long as the object has not
	 * been persisted, but should be non-editable thereafter.
	 * 
	 * <p>
	 * The {@link ImmutableOncePersisted} annotation is used to indicate  
	 * whether the attribute has this semantic.
	 * 
	 * @param attribute
	 * @return
	 */
	public boolean isImmutableOncePersisted(EAttribute attribute) {
		return false;
	}


	/**
	 * Whether the content of this (string) attribute must be formatted
	 * according to a mask.
	 * 
	 * <p>
	 * The {@link Mask} annotation is used to indicate the mask string.
	 * 
	 * @param attribute
	 * @return
	 */
	public String getMask(EAttribute attribute) {
		return null;
	}

}
