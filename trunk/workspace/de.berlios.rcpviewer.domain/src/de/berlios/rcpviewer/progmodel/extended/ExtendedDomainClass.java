package de.berlios.rcpviewer.progmodel.extended;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
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
 * IExtendedDomainClass someExtendedDC = someDC.getAdapter(IExtendedDomainClass.class);
 * </pre>
 * 
 * @author dkhaywood
 *
 */
public class ExtendedDomainClass<T> extends AbstractDomainClassAdapter<T> implements IExtendedDomainClass<T>{

	
	private final EmfFacade emfFacade = new EmfFacade();

	
	public ExtendedDomainClass(IDomainClass<T> adaptedDomainClass) {
		super(adaptedDomainClass);
	}


	/*
	 * @see de.berlios.rcpviewer.progmodel.extended.IExtendedDomainClass#orderedAttributes()
	 */
	public List<EAttribute> orderedAttributes() {
		List<EAttribute> attributes = adapts().attributes();
		Collections.sort(attributes, new AttributeComparator());
		return attributes;
	}
	
	/*
	 * @see de.berlios.rcpviewer.progmodel.extended.IExtendedDomainClass#isSearchable()
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


	/*
	 * @see de.berlios.rcpviewer.progmodel.extended.IExtendedDomainClass#isInstantiable()
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

	/*
	 * @see de.berlios.rcpviewer.progmodel.extended.IExtendedDomainClass#isSaveable()
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

	/*
	 * @see de.berlios.rcpviewer.progmodel.extended.IExtendedDomainClass#isOptional(org.eclipse.emf.ecore.EAttribute)
	 */
	public boolean isOptional(final EAttribute attribute) {
		Map<String,String> attributeDetails = 
			emfFacade.getAnnotationDetails(attribute, ExtendedProgModelConstants.ANNOTATION_ELEMENT);
		
		String optional = attributeDetails.get(
					ExtendedProgModelConstants.ANNOTATION_ELEMENT_OPTIONAL_KEY);
		return optional != null;
	}

	/*
	 * @see de.berlios.rcpviewer.progmodel.extended.IExtendedDomainClass#isMandatory(org.eclipse.emf.ecore.EAttribute)
	 */
	public boolean isMandatory(final EAttribute attribute) {
		return !isOptional(attribute);
	}

	/*
	 * @see de.berlios.rcpviewer.progmodel.extended.IExtendedDomainClass#isOptional(org.eclipse.emf.ecore.EOperation, int)
	 */
	public boolean isOptional(EOperation operation, int parameterPosition) {
		EParameter parameter = (EParameter)operation.getEParameters().get(parameterPosition);

		EAnnotation annotation = 
			parameter.getEAnnotation(ExtendedProgModelConstants.ANNOTATION_ELEMENT);
		if (annotation == null) {
			return false;
		}
		String optional = (String)annotation.getDetails().get(
				ExtendedProgModelConstants.ANNOTATION_ELEMENT_OPTIONAL_KEY);
		return optional != null;
	}


	/*
	 * @see de.berlios.rcpviewer.progmodel.extended.IExtendedDomainClass#isMandatory(org.eclipse.emf.ecore.EOperation, int)
	 */
	public boolean isMandatory(EOperation operation, int parameterPosition) {
		return !isOptional(operation, parameterPosition);
	}

	/*
	 * @see de.berlios.rcpviewer.progmodel.extended.IExtendedDomainClass#isInvisible(org.eclipse.emf.ecore.EAttribute)
	 */
	public boolean isInvisible(EAttribute attribute) {
		Map<String,String> attributeDetails = 
			emfFacade.getAnnotationDetails(attribute, ExtendedProgModelConstants.ANNOTATION_ATTRIBUTE);
		
		String invisible = attributeDetails.get(
					ExtendedProgModelConstants.ANNOTATION_ATTRIBUTE_INVISIBLE_KEY);
		return invisible != null;
	}


	/*
	 * @see de.berlios.rcpviewer.progmodel.extended.IExtendedDomainClass#businessKeys()
	 */
	public Map<String, List<EAttribute>> businessKeys() {
		Map<String, Map<Integer,EAttribute>> businessKeyAttributesByPosByName = 
			new HashMap<String, Map<Integer,EAttribute>>();
		for(EAttribute attribute: adapts().attributes()) {
			Map<String,String> attributeDetails = 
				emfFacade.getAnnotationDetails(attribute, ExtendedProgModelConstants.ANNOTATION_ATTRIBUTE);
			String businessKeyName = attributeDetails.get(
					ExtendedProgModelConstants.ANNOTATION_ATTRIBUTE_BUSINESS_KEY_NAME_KEY);
			if (businessKeyName == null) {
				continue;
			}
			Map<Integer, EAttribute> businessKeyAttributesByPos = 
				businessKeyAttributesByPosByName.get(businessKeyName);
			if (businessKeyAttributesByPos == null) {
				businessKeyAttributesByPos = new HashMap<Integer, EAttribute>();
				businessKeyAttributesByPosByName.put(businessKeyName, businessKeyAttributesByPos);
			}
			String businessKeyPosStr = attributeDetails.get(
						ExtendedProgModelConstants.ANNOTATION_ATTRIBUTE_BUSINESS_KEY_POS_KEY);
			if (businessKeyPosStr == null) {
				continue;
			}
			int businessKeyPos = Integer.parseInt(businessKeyPosStr);
			EAttribute businessKeyAttribute = 
				businessKeyAttributesByPos.get(businessKeyPos);
			if (businessKeyAttribute != null) {
				// we already have an attribute in this position, so give up
				businessKeyAttributesByPos.put(-1, null); // magic value indicating an error
				continue;
			}
			businessKeyAttributesByPos.put(businessKeyPos, attribute);
		}
		
		// instantiate the Map that we will return.
		Map<String, List<EAttribute>> businessKeyAttributeListByName = 
			new HashMap<String, List<EAttribute>>();
		
		// process the Map of Maps and convert all good maps into lists
		nextBusinessKey:
		for(String businessKeyName: businessKeyAttributesByPosByName.keySet()) {
			Map<Integer, EAttribute> businessKeyAttributesByPos =
				businessKeyAttributesByPosByName.get(businessKeyName);
			// check for our magic value meaning this is a bad map
			if (businessKeyAttributesByPos.get(-1)!=null) {
				continue;
			}
			// ensure that all values are contiguous
			int size = businessKeyAttributesByPos.size();
			List<EAttribute> businessKeyAttributes = new ArrayList<EAttribute>();
			nextBusinessKeyAttributes:
			for(int i=0; i<size; i++) {
				EAttribute businessKeyAttribute = businessKeyAttributesByPos.get(i+1);
				if (businessKeyAttributes == null) {
					// no attribute in this position, so
					// give up on processing this business key
					// and don't add anything to the return Map.
					continue nextBusinessKey;
				}
				businessKeyAttributes.add(businessKeyAttribute);
			}
			// have managed to find an attribute for each position, so add the
			// array list to our return Map.
			businessKeyAttributeListByName.put(businessKeyName, businessKeyAttributes);
		}
		return businessKeyAttributeListByName;
	}


	/*
	 * @see de.berlios.rcpviewer.progmodel.extended.IExtendedDomainClass#getFieldLengthOf(org.eclipse.emf.ecore.EAttribute)
	 */
	public int getFieldLengthOf(EAttribute attribute) {
		if (!returnsString(attribute)) {
			return -1;
		}
		Map<String,String> details = 
			emfFacade.getAnnotationDetails(attribute, ExtendedProgModelConstants.ANNOTATION_ELEMENT);
		return computeFieldLengthOf(details);
	}
	/*
	 * @see de.berlios.rcpviewer.progmodel.extended.IExtendedDomainClass#getFieldLengthOf(org.eclipse.emf.ecore.EOperation, int)
	 */
	public int getFieldLengthOf(EOperation operation, final int parameterPosition) {
		EParameter parameter = (EParameter)operation.getEParameters().get(parameterPosition);
		if (!isString(parameter)) {
			return -1;
		}

		Map<String,String> details = 
			emfFacade.getAnnotationDetails(parameter, ExtendedProgModelConstants.ANNOTATION_ELEMENT);
		return computeFieldLengthOf(details);
	}

	private int computeFieldLengthOf(final Map<String, String> details) {
		
		String fieldLengthOfStr = details.get(
					ExtendedProgModelConstants.ANNOTATION_ELEMENT_FIELD_LENGTH_OF_KEY);
		int fieldLengthOf = -1;
		if (fieldLengthOfStr != null) {
			fieldLengthOf = Integer.parseInt(fieldLengthOfStr);
		}

		String maxLengthOfStr = details.get(
				ExtendedProgModelConstants.ANNOTATION_ELEMENT_MAX_LENGTH_OF_KEY);
		int maxLengthOf = -1;
		if (maxLengthOfStr != null) {
			maxLengthOf = Integer.parseInt(maxLengthOfStr);
		}

		String minLengthOfStr = details.get(
				ExtendedProgModelConstants.ANNOTATION_ELEMENT_MIN_LENGTH_OF_KEY);
		int minLengthOf = -1;
		if (minLengthOfStr != null) {
			minLengthOf = Integer.parseInt(minLengthOfStr);
		}

		if (fieldLengthOf > 0 && maxLengthOf > 0) {
			return Math.min(fieldLengthOf, maxLengthOf);
		} else if (fieldLengthOf > 0 && maxLengthOf <= 0) {
			return fieldLengthOf;
		} else if (fieldLengthOf <= 0 && maxLengthOf > 0) {
			return maxLengthOf;
		} else if (fieldLengthOf <= 0 && maxLengthOf <= 0 && minLengthOf > 0) {
			return minLengthOf;
		} else if (fieldLengthOf <= 0 && maxLengthOf <= 0 && minLengthOf <= 0) {
			return ExtendedProgModelConstants.FIELD_LENGTH_OF_DEFAULT;
		}
		return ExtendedProgModelConstants.FIELD_LENGTH_OF_DEFAULT;
	}
	


	/*
	 * @see de.berlios.rcpviewer.progmodel.extended.IExtendedDomainClass#getMaxLengthOf(org.eclipse.emf.ecore.EAttribute)
	 */
	public int getMaxLengthOf(EAttribute attribute) {
		if (!returnsString(attribute)) {
			return -1;
		}
		Map<String,String> details = 
			emfFacade.getAnnotationDetails(attribute, ExtendedProgModelConstants.ANNOTATION_ELEMENT);
		return computeMaxLengthOf(details);
	}
	/*
	 * @see de.berlios.rcpviewer.progmodel.extended.IExtendedDomainClass#getMaxLengthOf(org.eclipse.emf.ecore.EOperation, int)
	 */
	public int getMaxLengthOf(EOperation operation, final int parameterPosition) {
		EParameter parameter = (EParameter)operation.getEParameters().get(parameterPosition);
		if (!isString(parameter)) {
			return -1;
		}

		Map<String,String> details = 
			emfFacade.getAnnotationDetails(parameter, ExtendedProgModelConstants.ANNOTATION_ELEMENT);
		return computeMaxLengthOf(details);
	}
	private int computeMaxLengthOf(final Map<String, String> details) {
		String maxLengthOfStr = details.get(
				ExtendedProgModelConstants.ANNOTATION_ELEMENT_MAX_LENGTH_OF_KEY);
		int maxLengthOf = -1;
		if (maxLengthOfStr != null) {
			maxLengthOf = Integer.parseInt(maxLengthOfStr);
		}

		String fieldLengthOfStr = details.get(
				ExtendedProgModelConstants.ANNOTATION_ELEMENT_FIELD_LENGTH_OF_KEY);
		int fieldLengthOf = -1;
		if (fieldLengthOfStr != null) {
			fieldLengthOf = Integer.parseInt(fieldLengthOfStr);
		}

		String minLengthOfStr = details.get(
				ExtendedProgModelConstants.ANNOTATION_ELEMENT_MIN_LENGTH_OF_KEY);
		int minLengthOf = -1;
		if (minLengthOfStr != null) {
			minLengthOf = Integer.parseInt(minLengthOfStr);
		}
		
		if (fieldLengthOf > 0 && maxLengthOf > 0) {
			return maxLengthOf;
		} else if (fieldLengthOf > 0 && maxLengthOf <= 0) {
			return fieldLengthOf;
		} else if (fieldLengthOf <= 0 && maxLengthOf > 0) {
			return maxLengthOf;
		} else if (fieldLengthOf <= 0 && maxLengthOf <= 0 && minLengthOf > 0) {
			return minLengthOf;
		} else if (fieldLengthOf <= 0 && maxLengthOf <= 0 && minLengthOf <= 0) {
			return ExtendedProgModelConstants.MAX_LENGTH_OF_DEFAULT;
		}
		return ExtendedProgModelConstants.MAX_LENGTH_OF_DEFAULT;
	}

	/*
	 * @see de.berlios.rcpviewer.progmodel.extended.IExtendedDomainClass#getMinLengthOf(org.eclipse.emf.ecore.EAttribute)
	 */
	public int getMinLengthOf(EAttribute attribute) {
		if (!returnsString(attribute)) {
			return -1;
		}
		Map<String,String> details = 
			emfFacade.getAnnotationDetails(attribute, ExtendedProgModelConstants.ANNOTATION_ELEMENT);
		return computeMinLengthOf(details);
	}
	/*
	 * @see de.berlios.rcpviewer.progmodel.extended.IExtendedDomainClass#getMinLengthOf(org.eclipse.emf.ecore.EOperation, int)
	 */
	public int getMinLengthOf(EOperation operation, final int parameterPosition) {
		EParameter parameter = (EParameter)operation.getEParameters().get(parameterPosition);
		if (!isString(parameter)) {
			return -1;
		}

		Map<String,String> details = 
			emfFacade.getAnnotationDetails(parameter, ExtendedProgModelConstants.ANNOTATION_ELEMENT);
		return computeMinLengthOf(details);
	}
	private int computeMinLengthOf(final Map<String,String> details) {
		String minLengthOfStr = details.get(
				ExtendedProgModelConstants.ANNOTATION_ELEMENT_MIN_LENGTH_OF_KEY);
		int minLengthOf = -1;
		if (minLengthOfStr != null) {
			minLengthOf = Integer.parseInt(minLengthOfStr);
		}

		String maxLengthOfStr = details.get(
				ExtendedProgModelConstants.ANNOTATION_ELEMENT_MAX_LENGTH_OF_KEY);
		int maxLengthOf = -1;
		if (maxLengthOfStr != null) {
			maxLengthOf = Integer.parseInt(maxLengthOfStr);
		}
	
		if (minLengthOf > 0 && maxLengthOf > 0) {
			return Math.min(minLengthOf, maxLengthOf);
		} else if (minLengthOf > 0 && maxLengthOf <= 0) {
			return minLengthOf;
		} else if (minLengthOf <= 0) {
			return ExtendedProgModelConstants.MIN_LENGTH_OF_DEFAULT;
		}
		return ExtendedProgModelConstants.MIN_LENGTH_OF_DEFAULT;
	}


	/*
	 * @see de.berlios.rcpviewer.progmodel.extended.IExtendedDomainClass#isImmutableOncePersisted(org.eclipse.emf.ecore.EAttribute)
	 */
	public boolean isImmutableOncePersisted(EAttribute attribute) {
		Map<String,String> attributeDetails = 
			emfFacade.getAnnotationDetails(attribute, ExtendedProgModelConstants.ANNOTATION_ATTRIBUTE);
		
		String immutableOncePersisted = attributeDetails.get(
					ExtendedProgModelConstants.ANNOTATION_ATTRIBUTE_IMMUTABLE_ONCE_PERSISTED_KEY);
		return immutableOncePersisted != null;
	}


	/*
	 * @see de.berlios.rcpviewer.progmodel.extended.IExtendedDomainClass#getMask(org.eclipse.emf.ecore.EAttribute)
	 */
	public String getMask(EAttribute attribute) {
		Map<String,String> attributeDetails = 
			emfFacade.getAnnotationDetails(attribute, ExtendedProgModelConstants.ANNOTATION_ELEMENT);
		
		String mask = attributeDetails.get(
					ExtendedProgModelConstants.ANNOTATION_ELEMENT_MASK_KEY);
		return mask;
	}


	/*
	 * @see de.berlios.rcpviewer.progmodel.extended.IExtendedDomainClass#getRegex(org.eclipse.emf.ecore.EAttribute)
	 */
	public String getRegex(EAttribute attribute) {
		Map<String,String> attributeDetails = 
			emfFacade.getAnnotationDetails(attribute, ExtendedProgModelConstants.ANNOTATION_ELEMENT);
		
		String regex = attributeDetails.get(
					ExtendedProgModelConstants.ANNOTATION_ELEMENT_REGEX_KEY);
		return regex;
	}


	/*
	 * @see de.berlios.rcpviewer.progmodel.extended.IExtendedDomainClass#regexMatches(org.eclipse.emf.ecore.EAttribute, java.lang.String)
	 */
	public boolean regexMatches(EAttribute attribute, String candidateValue) {
		String regex = getRegex(attribute);
		if (regex == null) {
			return true;
		}
		return candidateValue.matches(regex);
	}

	private boolean returnsString(final EAttribute attribute) {
		EDataType dataType = attribute.getEAttributeType();
		String instanceClassName = dataType.getInstanceClassName();
		return instanceClassName != null && instanceClassName.equals("java.lang.String");
	}

	private boolean isString(final EParameter parameter) {
		EClassifier dataType = parameter.getEType();
		String instanceClassName = dataType.getInstanceClassName();
		return instanceClassName != null && instanceClassName.equals("java.lang.String");
	}

}
