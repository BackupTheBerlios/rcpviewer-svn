package de.berlios.rcpviewer.progmodel.extended;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EParameter;

import de.berlios.rcpviewer.domain.Emf;
import de.berlios.rcpviewer.domain.AbstractDomainClassAdapter;
import de.berlios.rcpviewer.domain.IDomainClass;


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

	private ExtendedProgModelSemanticsEmfSerializer serializer = new ExtendedProgModelSemanticsEmfSerializer();
	/**
	 * for {@link Emf#isIntegralNumber(EDataType)}. 
	 */
	private Emf emf = new Emf();
	
	public ExtendedDomainClass(IDomainClass<T> adaptedDomainClass) {
		super(adaptedDomainClass);
	}


	/*
	 * TODO: to implement, picking up @TransientOnly or equiv.
	 * 
	 * @see de.berlios.rcpviewer.progmodel.extended.IExtendedDomainClass#isTransientOnly()
	 */
	public boolean isTransientOnly() {
		return false;
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
	 * @see de.berlios.rcpviewer.progmodel.extended.IExtendedDomainClass#idAttributes()
	 */
	public List<EAttribute> idAttributes() {
		List<EAttribute> attributes = adapts().attributes();
		for(Iterator<EAttribute> iter = attributes.iterator(); iter.hasNext(); ) {
			EAttribute attr = iter.next();
			if (!isId(attr)) {
				iter.remove();
			}
		}
		Collections.sort(attributes, new IdComparator());
		return attributes;
	}
	
	/*
	 * @see de.berlios.rcpviewer.progmodel.extended.IExtendedDomainClass#isId(org.eclipse.emf.ecore.EAttribute)
	 */
	public boolean isId(final EAttribute attribute) {
		return serializer.getAttributeId(attribute) != null;
	}
	
	/*
	 * @see de.berlios.rcpviewer.progmodel.extended.IExtendedDomainClass#isSimpleId()
	 */
	public boolean isSimpleId() {
		return idAttributes().size() == 1;
	}

	/*
	 * @see de.berlios.rcpviewer.progmodel.extended.IExtendedDomainClass#isCompositeId()
	 */
	public boolean isCompositeId() {
		return idAttributes().size() > 1;
	}
	
	/*
	 * @see de.berlios.rcpviewer.progmodel.extended.IExtendedDomainClass#getAssignmentType()
	 */
	public AssignmentType getIdAssignmentType() {
		AssignmentType defaultAssignmentType = AssignmentType.APPLICATION;
		if (isCompositeId()) {
			return defaultAssignmentType;
		}
		List<EAttribute> attributes = adapts().attributes();
		for(Iterator<EAttribute> iter = attributes.iterator(); iter.hasNext(); ) {
			EAttribute attr = iter.next();
			Id id = serializer.getAttributeId(attr);
			if (id == null) {
				continue;
			}
			EDataType attributeType = attr.getEAttributeType();
			if (!emf.isIntegralNumber(attributeType)) {
				return defaultAssignmentType;
			}
			AssignmentType assignedBy = id.assignedBy();
			// is an integral type
			if (assignedBy == AssignmentType.CONTEXT) {
				assignedBy = AssignmentType.OBJECT_STORE;
			}
			return assignedBy;
		}
		return defaultAssignmentType;
	}

	
	/*
	 * @see de.berlios.rcpviewer.progmodel.extended.IExtendedDomainClass#isSearchable()
	 */
	public boolean isSearchable() {
		Lifecycle lifecycle = serializer.getClassLifecycle(adapts().getEClass());
		return lifecycle != null && lifecycle.searchable();
	}


	/*
	 * @see de.berlios.rcpviewer.progmodel.extended.IExtendedDomainClass#isInstantiable()
	 */
	public boolean isInstantiable() {
		Lifecycle lifecycle = serializer.getClassLifecycle(adapts().getEClass());
		return lifecycle != null && lifecycle.instantiable();
	}

	/*
	 * @see de.berlios.rcpviewer.progmodel.extended.IExtendedDomainClass#isSaveable()
	 */
	public boolean isSaveable() {
		Lifecycle lifecycle = serializer.getClassLifecycle(adapts().getEClass());
		return lifecycle != null && lifecycle.saveable();
	}

	/*
	 * @see de.berlios.rcpviewer.progmodel.extended.IExtendedDomainClass#isImmutableOncePersisted()
	 */
	public boolean isImmutableOncePersisted() {
		return serializer.getClassImmutableOncePersisted(adapts().getEClass()) != null;
	}


	/*
	 * @see de.berlios.rcpviewer.progmodel.extended.IExtendedDomainClass#isOptional(org.eclipse.emf.ecore.EAttribute)
	 */
	public boolean isOptional(final EAttribute attribute) {
		return serializer.getOptional(attribute) != null;
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
		return serializer.getOptional(getParameter(operation, parameterPosition)) != null;
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
		return serializer.getAttributeInvisible(attribute) != null;
	}


	/*
	 * @see de.berlios.rcpviewer.progmodel.extended.IExtendedDomainClass#businessKeys()
	 */
	public Map<String, List<EAttribute>> businessKeys() {
		Map<String, Map<Integer,EAttribute>> businessKeyAttributesByPosByName = 
			new HashMap<String, Map<Integer,EAttribute>>();
		for(EAttribute attribute: adapts().attributes()) {
			BusinessKey businessKey = serializer.getAttributeBusinessKey(attribute);
			if (businessKey == null) {
				continue;
			}
			Map<Integer, EAttribute> businessKeyAttributesByPos = 
				businessKeyAttributesByPosByName.get(businessKey.name());
			if (businessKeyAttributesByPos == null) {
				businessKeyAttributesByPos = new HashMap<Integer, EAttribute>();
				businessKeyAttributesByPosByName.put(businessKey.name(), businessKeyAttributesByPos);
			}
			EAttribute businessKeyAttribute =  
				businessKeyAttributesByPos.get(businessKey.pos());
			if (businessKeyAttribute != null) {
				// we already have an attribute in this position, so give up
				businessKeyAttributesByPos.put(-1, null); // magic value indicating an error
				continue;
			}
			businessKeyAttributesByPos.put(businessKey.pos(), attribute);
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
		return computeFieldLengthOf(attribute);
	}
	/*
	 * @see de.berlios.rcpviewer.progmodel.extended.IExtendedDomainClass#getFieldLengthOf(org.eclipse.emf.ecore.EOperation, int)
	 */
	public int getFieldLengthOf(EOperation operation, final int parameterPosition) {
		
		EParameter parameter = getParameter(operation, parameterPosition);
		if (!isString(parameter)) {
			return -1;
		}
		return computeFieldLengthOf(parameter);
	}
	
	private int computeFieldLengthOf(EModelElement modelElement) {

		FieldLengthOf fieldLengthOfAnnotation = serializer.getFieldLengthOf(modelElement);
		MinLengthOf minLengthOfAnnotation = serializer.getMinLengthOf(modelElement);
		MaxLengthOf maxLengthOfAnnotation = serializer.getMaxLengthOf(modelElement);

		int fieldLengthOf = 
			fieldLengthOfAnnotation != null? fieldLengthOfAnnotation.value(): -1;
		int minLengthOf = 
			minLengthOfAnnotation != null? minLengthOfAnnotation.value(): -1;
		int maxLengthOf = 
			maxLengthOfAnnotation != null? maxLengthOfAnnotation.value(): -1;

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
		return computeMaxLengthOf(attribute);
	}
	/*
	 * @see de.berlios.rcpviewer.progmodel.extended.IExtendedDomainClass#getMaxLengthOf(org.eclipse.emf.ecore.EOperation, int)
	 */
	public int getMaxLengthOf(EOperation operation, final int parameterPosition) {
		EParameter parameter = (EParameter)operation.getEParameters().get(parameterPosition);
		if (!isString(parameter)) {
			return -1;
		}
		return computeMaxLengthOf(parameter);
	}
	private int computeMaxLengthOf(final EModelElement modelElement) {

		FieldLengthOf fieldLengthOfAnnotation = serializer.getFieldLengthOf(modelElement);
		MinLengthOf minLengthOfAnnotation = serializer.getMinLengthOf(modelElement);
		MaxLengthOf maxLengthOfAnnotation = serializer.getMaxLengthOf(modelElement);

		int fieldLengthOf = 
			fieldLengthOfAnnotation != null? fieldLengthOfAnnotation.value(): -1;
		int minLengthOf = 
			minLengthOfAnnotation != null? minLengthOfAnnotation.value(): -1;
		int maxLengthOf = 
			maxLengthOfAnnotation != null? maxLengthOfAnnotation.value(): -1;

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
		return computeMinLengthOf(attribute);
	}
	/*
	 * @see de.berlios.rcpviewer.progmodel.extended.IExtendedDomainClass#getMinLengthOf(org.eclipse.emf.ecore.EOperation, int)
	 */
	public int getMinLengthOf(EOperation operation, final int parameterPosition) {
		EParameter parameter = (EParameter)operation.getEParameters().get(parameterPosition);
		if (!isString(parameter)) {
			return -1;
		}
		return computeMinLengthOf(parameter);
	}
	private int computeMinLengthOf(final EModelElement modelElement) {

		MinLengthOf minLengthOfAnnotation = serializer.getMinLengthOf(modelElement);
		MaxLengthOf maxLengthOfAnnotation = serializer.getMaxLengthOf(modelElement);

		int minLengthOf = 
			minLengthOfAnnotation != null? minLengthOfAnnotation.value(): -1;
		int maxLengthOf = 
			maxLengthOfAnnotation != null? maxLengthOfAnnotation.value(): -1;

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
		ImmutableOncePersisted immutableOncePersisted = serializer.getAttributeImmutableOncePersisted(attribute);
		return immutableOncePersisted != null && !immutableOncePersisted.optout();
	}


	/*
	 * @see de.berlios.rcpviewer.progmodel.extended.IExtendedDomainClass#getMask(org.eclipse.emf.ecore.EAttribute)
	 */
	public String getMask(EAttribute attribute) {
		Mask mask = serializer.getAttributeMask(attribute);
		if (mask == null) return null;
		return mask.value();
	}


	/*
	 * @see de.berlios.rcpviewer.progmodel.extended.IExtendedDomainClass#getRegex(org.eclipse.emf.ecore.EAttribute)
	 */
	public String getRegex(EAttribute attribute) {
		Regex regex = serializer.getAttributeRegex(attribute);
		if (regex == null) return null;
		return regex.value();
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

	/**
	 * 
	 * @param operation
	 * @param parameterPosition
	 * @return parameter, or null if there are not that many parameters.
	 */
	private EParameter getParameter(final EOperation operation, final int parameterPosition) {
		return (EParameter)operation.getEParameters().get(parameterPosition);
	}


}
