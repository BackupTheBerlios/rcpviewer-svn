package de.berlios.rcpviewer.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.EReference;
import org.osgi.framework.Bundle;

import de.berlios.rcpviewer.domain.IDomainClass.IReference;
import de.berlios.rcpviewer.progmodel.extended.AssignmentType;
import de.berlios.rcpviewer.progmodel.extended.BusinessKey;
import de.berlios.rcpviewer.progmodel.extended.FieldLengthOf;
import de.berlios.rcpviewer.progmodel.extended.Id;
import de.berlios.rcpviewer.progmodel.extended.ImmutableOncePersisted;
import de.berlios.rcpviewer.progmodel.extended.Lifecycle;
import de.berlios.rcpviewer.progmodel.extended.Mask;
import de.berlios.rcpviewer.progmodel.extended.MaxLengthOf;
import de.berlios.rcpviewer.progmodel.extended.MinLengthOf;
import de.berlios.rcpviewer.progmodel.extended.Named;
import de.berlios.rcpviewer.progmodel.extended.Regex;
import de.berlios.rcpviewer.progmodel.java.JavaProgModelRules;
import de.berlios.rcpviewer.progmodel.standard.AttributeComparator;
import de.berlios.rcpviewer.progmodel.standard.ExtendedProgModelConstants;
import de.berlios.rcpviewer.progmodel.standard.ExtendedProgModelSemanticsEmfSerializer;
import de.berlios.rcpviewer.progmodel.standard.FeatureId;
import de.berlios.rcpviewer.progmodel.standard.IFeatureId;
import de.berlios.rcpviewer.progmodel.standard.IdComparator;
import de.berlios.rcpviewer.progmodel.standard.OppositeReferencesIdentifier;
import de.berlios.rcpviewer.progmodel.standard.StandardProgModelRules;
import de.berlios.rcpviewer.progmodel.standard.StandardProgModelConstants;
import de.berlios.rcpviewer.progmodel.standard.StandardProgModelSemanticsEmfSerializer;

public final class DomainClass implements IDomainClass {

	
	protected final IDomain _domain;
	protected final EClass _eClass;
	
	/**
	 * To deserialize semantics from EMF metamodel.
	 */
	private StandardProgModelSemanticsEmfSerializer _standardSerializer = new StandardProgModelSemanticsEmfSerializer();
	private ExtendedProgModelSemanticsEmfSerializer _extendedSerializer = new ExtendedProgModelSemanticsEmfSerializer();

	/**
	 * for {@link Emf#isIntegralNumber(EDataType)}. 
	 */
	private Emf _emf = new Emf();

	/**
	 * Required since annotations are used to store adapter class names.
	 * 
	 * @see #setAdapterFactory(Class, IAdapterFactory)
	 * @see #getAdapter(Class)
	 * @see #getAdapters()
	 */
	private EmfAnnotations _emfAnnotations = new EmfAnnotations();

	private WeakHashMap<EAttribute, IAttribute> _attributesByEAttribute = new WeakHashMap<EAttribute, IAttribute>();
	private WeakHashMap<EReference, IReference> _referencesByEReference = new WeakHashMap<EReference, IReference>();
	private WeakHashMap<EOperation, IOperation> _operationsByEOperation = new WeakHashMap<EOperation, IOperation>();

	/**
	 * Built up lazily whenever {@link #getAdapter(Class)} is called.
	 */
	private Map<String, Object> adaptersByAnnotationSource = new HashMap<String, Object>();
	
	public DomainClass(final IDomain domain, final EClass eClass) {
		this._domain = domain;
		this._eClass = eClass;
	}

	private Object _binding;
	public Object getBinding() {
		return _binding;
	}
	public void setBinding(Object binding) {
		_binding = binding;
	}

	/*
	 * @see de.berlios.rcpviewer.domain.IDomainClass#getDomain()
	 */
	public IDomain getDomain() {
		return _domain;
	}

	/*
	 * @see de.berlios.rcpviewer.domain.IDomainClass#getName()
	 * 
	 * <p>
	 * Uses the serializer to obtain the name from EMF; if none can be found, 
	 * then the EClass' name is used instead.
	 */
	public String getName() {
		Named named = _standardSerializer.getNamed(getEClass());
		if (named == null) {
			return getEClassName();
		}
		return named.value();
	}


	/*
	 * @see de.berlios.rcpviewer.domain.IDomainClass#getDescription()
	 */
	public String getDescription() {
		return descriptionOf(_eClass);
	}

	/*
	 * @see de.berlios.rcpviewer.domain.IDomainClass#getEClass()
	 */
	public EClass getEClass() {
		return _eClass;
	}

	/*
	 * @see de.berlios.rcpviewer.domain.IDomainClass#getEClassName()
	 */
	public String getEClassName() {
		return _eClass.getName();
	}

	/*
	 * @see de.berlios.rcpviewer.domain.IDomainClass#isChangeable()
	 */
	public boolean isChangeable() {
		EAnnotation annotation = 
			_eClass.getEAnnotation(StandardProgModelConstants.ANNOTATION_ELEMENT);
		if (annotation == null) {
			return false;
		}
		String immutable = 
			(String)annotation.getDetails().get(StandardProgModelConstants.ANNOTATION_ELEMENT_IMMUTABLE_KEY);
		return "false".equals(immutable);
	}

	
	/*
	 * TODO: to implement, picking up @TransientOnly or equiv.
	 * 
	 */
	public boolean isTransientOnly() {
		return false;
	}

	/*
	 * @see de.berlios.rcpviewer.domain.IDomainClass#getI18nData()
	 */
	public II18nData getI18nData() {
		throw new RuntimeException("Not yet implemented");
	}

	/*
	 * @see de.berlios.rcpviewer.progmodel.extended.IExtendedDomainClass#isSearchable()
	 */
	public boolean isSearchable() {
		Lifecycle lifecycle = _extendedSerializer.getClassLifecycle(_eClass);
		return lifecycle != null && lifecycle.searchable();
	}


	/*
	 * @see de.berlios.rcpviewer.progmodel.extended.IExtendedDomainClass#isInstantiable()
	 */
	public boolean isInstantiable() {
		Lifecycle lifecycle = _extendedSerializer.getClassLifecycle(_eClass);
		return lifecycle != null && lifecycle.instantiable();
	}

	/*
	 * @see de.berlios.rcpviewer.progmodel.extended.IExtendedDomainClass#isSaveable()
	 */
	public boolean isSaveable() {
		Lifecycle lifecycle = _extendedSerializer.getClassLifecycle(_eClass);
		return lifecycle != null && lifecycle.saveable();
	}

	/*
	 * @see de.berlios.rcpviewer.progmodel.extended.IExtendedDomainClass#isImmutableOncePersisted()
	 */
	public boolean isImmutableOncePersisted() {
		return _extendedSerializer.getClassImmutableOncePersisted(_eClass) != null;
	}

	/*
	 * @see de.berlios.rcpviewer.progmodel.extended.IDomainClass#isSimpleId()
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
		List<EAttribute> attributes = attributes();
		for(Iterator<EAttribute> iter = attributes.iterator(); iter.hasNext(); ) {
			EAttribute attr = iter.next();
			Id id = _extendedSerializer.getAttributeId(attr);
			if (id == null) {
				continue;
			}
			EDataType attributeType = attr.getEAttributeType();
			if (!_emf.isIntegralNumber(attributeType)) {
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
	 * @see de.berlios.rcpviewer.progmodel.extended.IDomainClass#businessKeys()
	 */
	public Map<String, List<EAttribute>> businessKeys() {
		Map<String, Map<Integer,EAttribute>> businessKeyAttributesByPosByName = 
			new HashMap<String, Map<Integer,EAttribute>>();
		for(EAttribute attribute: attributes()) {
			BusinessKey businessKey = _extendedSerializer.getAttributeBusinessKey(attribute);
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



	///////////////////////////////////////////////////////////////
	// members

	private abstract class Member implements IDomainClass.IMember {
		public IDomainClass getDomainClass() {
			return DomainClass.this; 
		}
	}


	///////////////////////////////////////////////////////////////
	// attributes
	
	/*
	 * @see de.berlios.rcpviewer.domain.IDomainClass#containsAttribute(org.eclipse.emf.ecore.EAttribute)
	 */
	public boolean containsAttribute(EAttribute eAttribute) {
		return this._eClass.getEAllAttributes().contains(eAttribute);
	}

	/*
	 * @see de.berlios.rcpviewer.domain.IDomainClass#getEAttributeNamed(java.lang.String)
	 */
	public EAttribute getEAttributeNamed(String attributeName) {
		for(EAttribute eAttribute: attributes() ) {
			if (eAttribute.getName().equals(attributeName)) {
				return eAttribute;
			}
		}
		return null;
	}

	/*
	 * @see de.berlios.rcpviewer.domain.IDomainClass#getAttribute(org.eclipse.emf.ecore.EAttribute)
	 */
	public synchronized IAttribute getAttribute(final EAttribute eAttribute) {
		IAttribute attribute = _attributesByEAttribute.get(eAttribute);
		if (attribute == null) {
			Attribute concreteAttribute = new Attribute(eAttribute);
			concreteAttribute.setBinding(Deployment.getDeployment().bindingFor(concreteAttribute));
			attribute = concreteAttribute;
			_attributesByEAttribute.put(eAttribute, attribute);
		}
		return attribute;
	}


	/*
	 * @see de.berlios.rcpviewer.domain.IDomainClass#attributes()
	 */
	public List<EAttribute> attributes() {
		return attributes(true);
	}

	/**
	 * Returns all the attributes of the class, including inherited attributes
	 * only if requested.
	 * 
	 * <p>
	 * The returned list is a copy and so may safely be modified by the caller
	 * with no side-effects.
	 */
	public List<EAttribute> attributes(boolean includeInherited) {
		List<EAttribute> eAttributes = new ArrayList<EAttribute>();
		EList attributes = includeInherited?
								getEClass().getEAllAttributes():
								getEClass().getEAttributes();
		eAttributes.addAll(attributes);
		return eAttributes;
	}

	/*
	 * @see de.berlios.rcpviewer.domain.IDomainClass#iAttributes()
	 */
	public List<IAttribute> iAttributes() {
		List<IAttribute> attributes = new ArrayList<IAttribute>();
		for(EAttribute eAttribute: attributes()) {
			attributes.add(getAttribute(eAttribute));
		}
		return attributes;
	}

	/*
	 * @see de.berlios.rcpviewer.progmodel.extended.IDomainClass#orderedAttributes()
	 */
	public List<EAttribute> orderedAttributes() {
		List<EAttribute> attributes = attributes();
		Collections.sort(attributes, new AttributeComparator());
		return attributes;
	}
	
	/*
	 * @see de.berlios.rcpviewer.progmodel.extended.IDomainClass#idAttributes()
	 */
	public List<EAttribute> idAttributes() {
		List<EAttribute> attributes = attributes();
		for(Iterator<EAttribute> iter = attributes.iterator(); iter.hasNext(); ) {
			EAttribute attr = iter.next();
			if (!getAttribute(attr).isId()) {
				iter.remove();
			}
		}
		Collections.sort(attributes, new IdComparator());
		return attributes;
	}
	
	public int getNumberOfAttributes() {
		return getEClass().getEAllAttributes().size();
	}


	private final class Attribute extends Member implements IDomainClass.IAttribute {

		private final EAttribute _eAttribute;
		private Object _binding;
		
		public Attribute(EAttribute eAttribute) {
			_eAttribute = eAttribute;
		}
		
		/*
		 * @see de.berlios.rcpviewer.domain.IDomainClass.IAttribute#getEAttribute()
		 */
		public EAttribute getEAttribute() {
			return _eAttribute;
		}
		void setBinding(Object binding) {
			_binding = binding;
		}
		
		/*
		 * @see de.berlios.rcpviewer.domain.IDomainClass.IAttribute#isWriteOnly()
		 */
		public boolean isWriteOnly() {
			return _eAttribute.getEAnnotation(StandardProgModelConstants.ANNOTATION_ATTRIBUTE_WRITE_ONLY) != null;
		}
		/*
		 * @see de.berlios.rcpviewer.domain.IDomainClass.IAttribute#isChangeable()
		 */
		public boolean isChangeable() {
			return _eAttribute.isChangeable();
		}
		/*
		 * @see de.berlios.rcpviewer.domain.IDomainClass.IAttribute#isDerived()
		 */
		public boolean isDerived() {
			return _eAttribute.isDerived();
		}
		/*
		 * @see de.berlios.rcpviewer.domain.IDomainClass.IAttribute#getLowerBound()
		 */
		public int getLowerBound() {
			return _eAttribute.getLowerBound();
		}
		/*
		 * @see de.berlios.rcpviewer.domain.IDomainClass.IAttribute#getUpperBound()
		 */
		public int getUpperBound() {
			return _eAttribute.getUpperBound();
		}
		/*
		 * @see de.berlios.rcpviewer.domain.IDomainClass.IAttribute#isRequired()
		 */
		public boolean isRequired() {
			return _eAttribute.isRequired();
		}
		/*
		 * @see de.berlios.rcpviewer.domain.IDomainClass.IAttribute#isMany()
		 */
		public boolean isMany() {
			return _eAttribute.isMany();
		}
		/*
		 * @see de.berlios.rcpviewer.domain.IDomainClass.IAttribute#isUnique()
		 */
		public boolean isUnique() {
			return _eAttribute.isUnique();
		}
		/*
		 * @see de.berlios.rcpviewer.domain.IDomainClass.IAttribute#isOrdered()
		 */
		public boolean isOrdered() {
			return _eAttribute.isOrdered();
		}
		/*
		 * @see de.berlios.rcpviewer.domain.IDomainClass.IAttribute#isUnsettable()
		 */
		public boolean isUnsettable() {
			return _eAttribute.isUnsettable();
		}
		/*
		 * @see de.berlios.rcpviewer.domain.IDomainClass.IAttribute#getI18nDataFor()
		 */
		public II18nData getI18nDataFor() {
			throw new RuntimeException("Not yet implemented");
		}

		/*
		 * @see de.berlios.rcpviewer.domain.IDomainClass.IAttribute#isId()
		 */
		public boolean isId() {
			return _extendedSerializer.getAttributeId(_eAttribute) != null;
		}
		/*
		 * @see de.berlios.rcpviewer.domain.IDomainClass.IAttribute#isOptional()
		 */
		public boolean isOptional() {
			return _extendedSerializer.getOptional(_eAttribute) != null;
		}
		/*
		 * @see de.berlios.rcpviewer.domain.IDomainClass.IAttribute#isMandatory()
		 */
		public boolean isMandatory() {
			return !isOptional();
		}
		/*
		 * @see de.berlios.rcpviewer.domain.IDomainClass.IAttribute#isInvisible()
		 */
		public boolean isInvisible() {
			return _extendedSerializer.getAttributeInvisible(_eAttribute) != null;
		}
		/*
		 * @see de.berlios.rcpviewer.domain.IDomainClass.IAttribute#getFieldLengthOf()
		 */
		public int getFieldLengthOf() {
			if (!returnsString()) {
				return -1;
			}
			return computeFieldLengthOf(_eAttribute);
		}
		/*
		 * @see de.berlios.rcpviewer.domain.IDomainClass.IAttribute#getMaxLengthOf()
		 */
		public int getMaxLengthOf() {
			if (!returnsString()) {
				return -1;
			}
			return computeMaxLengthOf(_eAttribute);
		}
		/*
		 * @see de.berlios.rcpviewer.domain.IDomainClass.IAttribute#getMinLengthOf()
		 */
		public int getMinLengthOf() {
			if (!returnsString()) {
				return -1;
			}
			return computeMinLengthOf(_eAttribute);
		}
		/*
		 * @see de.berlios.rcpviewer.domain.IDomainClass.IAttribute#isImmutableOncePersisted()
		 */
		public boolean isImmutableOncePersisted() {
			ImmutableOncePersisted immutableOncePersisted = _extendedSerializer.getAttributeImmutableOncePersisted(_eAttribute);
			return immutableOncePersisted != null && !immutableOncePersisted.optout();
		}
		/*
		 * @see de.berlios.rcpviewer.domain.IDomainClass.IAttribute#getMask()
		 */
		public String getMask() {
			Mask mask = _extendedSerializer.getAttributeMask(_eAttribute);
			if (mask == null) return null;
			return mask.value();
		}
		/*
		 * @see de.berlios.rcpviewer.domain.IDomainClass.IAttribute#getRegex()
		 */
		public String getRegex() {
			Regex regex = _extendedSerializer.getAttributeRegex(_eAttribute);
			if (regex == null) return null;
			return regex.value();
		}
		/*
		 * @see de.berlios.rcpviewer.domain.IDomainClass.IAttribute#regexMatches(java.lang.String)
		 */
		public boolean regexMatches(final String candidateValue) {
			String regex = getRegex();
			if (regex == null) {
				return true;
			}
			return candidateValue.matches(regex);
		}

		/*
		 * @see de.berlios.rcpviewer.domain.IDomainClass.IAttribute#getBinding()
		 */
		public Object getBinding() {
			return _binding; // JAVA5_FIXME
		}

		/*
		 * @see de.berlios.rcpviewer.domain.IDomainClass#attributeIdFor(org.eclipse.emf.ecore.EAttribute)
		 */
		public IFeatureId attributeIdFor() {
			return FeatureId.create(_eAttribute.getName(), getDomainClass(), IFeatureId.Type.ATTRIBUTE); 
		}

		//////////////
		// helpers
		
		private boolean returnsString() {
			EDataType dataType = _eAttribute.getEAttributeType();
			String instanceClassName = dataType.getInstanceClassName();
			return instanceClassName != null && instanceClassName.equals("java.lang.String");
		}

	}

	///////////////////////////////////////////////////////////////
	// references 
	
	/**
	 * Horrible hack, see {@link #oppRefState}.
	 * 
	 */
	public static enum OppRefState {
		stillBuilding,
		onceMore,
		neverAgain;
	}

	/**
	 * HACK: This is a horrible hack to allow 
	 * {@link OppositeReferencesIdentifier#identify()} to be called once more,
	 * in {@link Domain#lookup(Class)}.
	 */
	public OppRefState oppRefState = OppRefState.stillBuilding;




	/*
	 * @see de.berlios.rcpviewer.domain.IDomainClass#references()
	 */
	public List<EReference> references() {
		return references(true);
	}

	/*
	 * @see de.berlios.rcpviewer.domain.IDomainClass#iReferences()
	 */
	public List<IReference> iReferences() {
		List<IReference> references = new ArrayList<IReference>();
		for(EReference eReference: references()) {
			references.add(getReference(eReference));
		}
		return references;
	}


	/**
	 * Returns references from this class to other classes, specifying whether
	 * inherited references should be included.
	 * 
	 * @param includeInherited
	 * @return
	 */
	public List<EReference> references(final boolean includeInherited) {
		List<EReference> references = new ArrayList<EReference>();
		EClass eClass = getEClass();
		EList eReferenceList = includeInherited? eClass.getEAllReferences(): eClass.getEReferences();
		for(Iterator<?> iter = eReferenceList.iterator(); iter.hasNext(); ) {
			EReference ref = (EReference)iter.next();
			references.add(ref);
		}
		return references;
	}

	public EReference getEReferenceNamed(String referenceName) {
		for(EReference eReference: references() ) {
			if (eReference.getName().equals(referenceName)) {
				return eReference;
			}
		}
		return null;
	}

	public boolean containsReference(EReference eReference) {
		return this._eClass.getEAllReferences().contains(eReference);
	}

	/**
	 * Returns an {@link IReference} for any EReference, whether it represents
	 * a 1:1 reference or a collection.
	 *  
	 * @param eReference
	 * @return
	 */
	public synchronized IReference getReference(final EReference eReference) {
		if (eReference == null) {
			return null;
		}
		IReference reference = _referencesByEReference.get(eReference);
		if (reference == null) {
			if (eReference.isMany()) {
				CollectionReference concreteReference = new CollectionReference(eReference);
				concreteReference.setBinding(Deployment.getDeployment().bindingFor(concreteReference));
				reference = concreteReference;
			} else {
				OneToOneReference concreteReference = new OneToOneReference(eReference);
				concreteReference.setBinding(Deployment.getDeployment().bindingFor(concreteReference));
				reference = concreteReference;
			}
			_referencesByEReference.put(eReference, reference);
		}
		return reference;
	}

	private abstract class Reference extends Member implements IDomainClass.IReference {
		private final EReference _reference;
		private Object _binding;
		Reference(EReference reference) {
			_reference = reference;
		}
		public EReference getEReference() {
			return _reference;
		}

		public Object getBinding() {
			return _binding; // JAVA5_FIXME
		}
		void setBinding(Object binding) {
			_binding = binding;
		}
		

		public IDomainClass getReferencedClass() {
			EClass eClass = (EClass)_reference.getEReferenceType();
			return _domain.lookupNoRegister(((Class<?>)eClass.getInstanceClass()));
		}
		public boolean isMultiple() {
			return _reference.isMany();
		}
		public boolean isOrdered() {
			return _reference.isOrdered();
		}
		public boolean isContainer() {
			return _reference.isContainer();
		}
		public boolean isUnique() {
			return _reference.isUnique();
		}
		public boolean isChangeable() {
			return _reference.isChangeable();
		}
		public boolean isDerived() {
			return _reference.isDerived();
		}

		/*
		 * @see de.berlios.rcpviewer.domain.IDomainClass#referenceIdFor(org.eclipse.emf.ecore.EReference)
		 */
		public IFeatureId referenceIdFor() {
			return FeatureId.create(_reference.getName(), getDomainClass(), IFeatureId.Type.REFERENCE);
		}

	}

	///////////////////////////////////////////////////////////////
	// 1:1 references 

	public synchronized IOneToOneReference getOneToOneReference(final EReference eReference) {
		if (eReference == null) {
			return null;
		}
		if (eReference.isMany()) {
			throw new IllegalArgumentException("EMF reference represents a collection (ref='" + eReference + "'");
		}
		IOneToOneReference reference = (IOneToOneReference)_referencesByEReference.get(eReference);
		if (reference == null) {
				reference = new OneToOneReference(eReference);
			_referencesByEReference.put(eReference, reference);
		}
		return reference;
	}

	private final class OneToOneReference extends Reference implements IDomainClass.IOneToOneReference {
		public OneToOneReference(EReference reference) {
			super(reference);
		}
	}

	
	///////////////////////////////////////////////////////////////
	// collections 
	
	public synchronized ICollectionReference getCollectionReference(final EReference eReference) {
		if (eReference == null) {
			return null;
		}
		if (!eReference.isMany()) {
			throw new IllegalArgumentException("EMF reference represents a 1:1 reference (ref='" + eReference + "'");
		}
		ICollectionReference reference = (ICollectionReference)_referencesByEReference.get(eReference);
		if (reference == null) {
				reference = new CollectionReference(eReference);
			_referencesByEReference.put(eReference, reference);
		}
		return reference;
	}

	private final class CollectionReference extends Reference implements IDomainClass.ICollectionReference {
		public CollectionReference(EReference reference) {
			super(reference);
		}
	}

	///////////////////////////////////////////////////////////////
	// operations 

	/*
	 * @see de.berlios.rcpviewer.domain.IDomainClass#operations()
	 */
	public List<EOperation> operations() {
		return operations(OperationKind.ALL, true);
	}


	/*
	 * @see de.berlios.rcpviewer.domain.IDomainClass#operations(de.berlios.rcpviewer.domain.OperationKind, boolean)
	 */
	public List<EOperation> operations(OperationKind operationKind, boolean includeInherited) {
		List<EOperation> eOperations = new ArrayList<EOperation>();
		EList operations = includeInherited?
								getEClass().getEAllOperations():
								getEClass().getEOperations();
		for(Iterator iter = operations.iterator(); iter.hasNext(); ) {
			EOperation eOperation = (EOperation)iter.next();
			IOperation operation = getOperation(eOperation);
			switch(operationKind) {
				case INSTANCE:
					if (!operation.isStatic()) {
						eOperations.add(eOperation);
					}
					break;
				case STATIC:
					if (operation.isStatic()) {
						eOperations.add(eOperation);
					}
					break;
				case ALL:
					eOperations.add(eOperation);
			}
		}
		return eOperations;
	}

	/*
	 * @see de.berlios.rcpviewer.domain.IDomainClass#iOperations()
	 */
	public List<IOperation> iOperations() {
		List<IOperation> operations = new ArrayList<IOperation>();
		for(EOperation eOperation: operations()) {
			operations.add(getOperation(eOperation));
		}
		return operations;
	}

	public EOperation getEOperationNamed(String operationName) {
		for(EOperation eOperation: operations() ) {
			if (eOperation.getName().equals(operationName)) {
				return eOperation;
			}
		}
		return null;
	}

	public synchronized IOperation getOperation(EOperation eOperation) {
		if (eOperation == null) {
			return null;
		}
		IOperation operation = _operationsByEOperation.get(eOperation);
		if (operation == null) {
			Operation concreteOperation = new Operation(eOperation);
			concreteOperation.setBinding(Deployment.getDeployment().bindingFor(concreteOperation));
			operation = concreteOperation;
			_operationsByEOperation.put(eOperation, operation);
		}
		return operation;
	}

	public boolean containsOperation(EOperation eOperation) {
		return this._eClass.getEAllOperations().contains(eOperation);
	}

	private final class Operation extends Member implements IDomainClass.IOperation {
		
		private final EOperation _eOperation;
		private Object _binding;
		
		public Operation(EOperation eOperation) {
			_eOperation = eOperation;
		}
		
		/*
		 * @see de.berlios.rcpviewer.domain.IDomainClass.IOperation#getEOperation()
		 */
		public EOperation getEOperation() {
			return _eOperation;
		}
		
		/*
		 * @see de.berlios.rcpviewer.domain.IDomainClass.IOperation#getBinding()
		 */
		public Object getBinding() {
			return _binding;
		}
		void setBinding(Object binding) {
			_binding = binding;
		}

		/*
		 * @see de.berlios.rcpviewer.domain.IDomainClass.IOperation#isStatic()
		 */
		public boolean isStatic() {
			return _eOperation.getEAnnotation(StandardProgModelConstants.ANNOTATION_OPERATION_STATIC) != null;
		}
		
		/*
		 * @see de.berlios.rcpviewer.domain.IDomainClass.IOperation#getEDataTypeFor(int)
		 */
		public EDataType getEDataTypeFor(final int parameterPosition) {
			if (!isParameterAValue(parameterPosition)) {
				throw new IllegalArgumentException("Parameter does not represent a value.");
			}
			EParameter parameter = getParameter(parameterPosition);
			return (EDataType)parameter.getEType();
		}
		
		/*
		 * @see de.berlios.rcpviewer.domain.IDomainClass.IOperation#getI18nDataFor()
		 */
		public II18nData getI18nDataFor() {
			throw new RuntimeException("Not yet implemented");
		}

		/*
		 * @see de.berlios.rcpviewer.domain.IDomainClass.IOperation#getI18nDataFor()
		 */
		public II18nData getI18nDataFor(final int parameterPosition) {
			throw new RuntimeException("Not yet implemented");
		}

		/*
		 * @see de.berlios.rcpviewer.domain.IDomainClass.IOperation#getDomainClassFor(int)
		 */
		public IDomainClass getDomainClassFor(final int parameterPosition) {
			if (!isParameterADomainObject(parameterPosition)) {
				throw new IllegalArgumentException("Parameter does not represent a reference.");
			}
			EParameter parameter = getParameter(parameterPosition);
			EClass eClass = (EClass)parameter.getEType();
			return _domain.domainClassFor(eClass);
		}

		/*
		 * @see de.berlios.rcpviewer.domain.IDomainClass.IOperation#getNameFor(int)
		 */
		public String getNameFor(int parameterPosition) {
			EParameter parameter = getParameter(parameterPosition);
			return parameter.getName();
		}
		
		/*
		 * @see de.berlios.rcpviewer.domain.IDomainClass.IOperation#getDescriptionFor(int)
		 */
		public String getDescriptionFor(int parameterPosition) {
			EParameter parameter = getParameter(parameterPosition);
			return descriptionOf(parameter);
		}
		
		/*
		 * @see de.berlios.rcpviewer.domain.IDomainClass#isParameterAValue(org.eclipse.emf.ecore.EOperation, int)
		 */
		public boolean isParameterAValue(int parameterPosition) {
			EParameter parameter = getParameter(parameterPosition);
			return parameter.getEType() instanceof EDataType;
		}

		/*
		 * @see de.berlios.rcpviewer.domain.IDomainClass#isParameterADomainObject(org.eclipse.emf.ecore.EOperation, int)
		 */
		public boolean isParameterADomainObject(int parameterPosition) {
			EParameter parameter = getParameter(parameterPosition);
			return parameter.getEType() instanceof EClass;
		}

		/*
		 * @see de.berlios.rcpviewer.domain.IDomainClass.IOperation#isOptional(int)
		 */
		public boolean isOptional(final int parameterPosition) {
			return _extendedSerializer.getOptional(getParameter(parameterPosition)) != null;
		}

		/*
		 * @see de.berlios.rcpviewer.domain.IDomainClass.IOperation#isMandatory(int)
		 */
		public boolean isMandatory(final int parameterPosition) {
			return !isOptional(parameterPosition);
		}

		/*
		 * @see de.berlios.rcpviewer.domain.IDomainClass.IOperation#getFieldLengthOf(int)
		 */
		public int getFieldLengthOf(final int parameterPosition) {
			EParameter parameter = getParameter(parameterPosition);
			if (!isString(parameter)) {
				return -1;
			}
			return computeFieldLengthOf(parameter);
		}
		
		public int getMaxLengthOf(final int parameterPosition) {
			EParameter parameter = getParameter(parameterPosition);
			if (!isString(parameter)) {
				return -1;
			}
			return computeMaxLengthOf(parameter);
		}

		public int getMinLengthOf(final int parameterPosition) {
			EParameter parameter = getParameter(parameterPosition);
			if (!isString(parameter)) {
				return -1;
			}
			return computeMinLengthOf(parameter);
		}
		

		/*
		 * @see de.berlios.rcpviewer.domain.IDomainClass#operationIdFor(org.eclipse.emf.ecore.EOperation)
		 */
		public IFeatureId operationIdFor() {
			return FeatureId.create(_eOperation.getName(), getDomainClass(), IFeatureId.Type.OPERATION);
		}

		/////////////////
		// helpers
		
		/**
		 * 
		 * @param _eOperation
		 * @param parameterPosition
		 * @return parameter, or null if there are not that many parameters.
		 */
		private EParameter getParameter(final int parameterPosition) {
			return (EParameter)_eOperation.getEParameters().get(parameterPosition);
		}

		private boolean isString(final EParameter parameter) {
			EClassifier dataType = parameter.getEType();
			String instanceClassName = dataType.getInstanceClassName();
			return instanceClassName != null && instanceClassName.equals("java.lang.String");
		}


	}
	
	///////////////////////////////////////////////////////////////
	// adapters 

	/**
	 * Extension object pattern - getting an extension.
	 * 
	 * @param adapterClass
	 * @return adapter (extension) that will implement the said class.
	 */
	public <V> V getAdapter(Class<V> adapterClass) {
		String annotationSource = 
			StandardProgModelConstants.ANNOTATION_EXTENSIONS_PREFIX + adapterClass.getName();
		return getAdapter(annotationSource);
	}

	public List<IDomainClassAdapter> getAdapters() {
		List<EAnnotation> annotations = 
			_emfAnnotations.annotationsPrefixed(_eClass, StandardProgModelConstants.ANNOTATION_EXTENSIONS_PREFIX);
		
		List<IDomainClassAdapter> adapters = new ArrayList<IDomainClassAdapter>();
		for(EAnnotation annotation: annotations) {
			adapters.add( (IDomainClassAdapter)getAdapter(annotation.getSource()) );
		}
		
		return adapters;
	}

	/**
	 * Extension object pattern - getting an extension.
	 * 
	 * @param annotationSource holding the adapter factory used to create the adapter
	 * @return adapter (extension) that will implement the said class.
	 */
	private <V> V getAdapter(String annotationSource) {
		if (adaptersByAnnotationSource.containsKey(annotationSource)) {
			return (V)adaptersByAnnotationSource.get(annotationSource);
		}
		
		Map<String, String> details = 
			_emfAnnotations.getAnnotationDetails(_eClass, annotationSource);
		String adapterFactoryName = 
			details.get(StandardProgModelConstants.ANNOTATION_EXTENSIONS_ADAPTER_FACTORY_NAME_KEY);
		if (adapterFactoryName == null) {
			throw new IllegalArgumentException("No such adapter '" + adapterFactoryName + "'");
		}
		IAdapterFactory<V> adapterFactory;
		try {
			adapterFactory = (IAdapterFactory<V>)loadClass(adapterFactoryName).newInstance(); 
			V adapter = adapterFactory.createAdapter(this);
			adaptersByAnnotationSource.put(annotationSource, adapter); // cache
			return adapter;
		} catch (InstantiationException e) {
			// TODO - log?
			return null;
		} catch (IllegalAccessException e) {
			// TODO - log?
			return null;
		} catch (ClassNotFoundException e) {
			// TODO - log?
			return null;
		}
	}
	protected Class<IAdapterFactory> loadClass(String adapterFactoryName) throws ClassNotFoundException {

		Bundle bundle = Deployment.getDeployment().getBundle();
		try {
			return (Class<IAdapterFactory>)Class.forName(adapterFactoryName);
		} catch (ClassNotFoundException ex) {
			// do nothing
		}
		
		return (Class<IAdapterFactory>)bundle.loadClass(adapterFactoryName);
	}

	
	/**
	 * Installs an adapter on this domain class, (that is, the extension object 
	 * pattern).
	 * 
	 * <p>
	 * Recall that DomainClass does not hold any persistent state other than
	 * a reference to an EMF EClass.  The actual information about the domain
	 * class (attributes, operations and so forth) is held within an EMF 
	 * instance.
	 * 
	 * <p>
	 * This design decision makes life a little tricky for holding adapters.
	 * Normally one might have a Map&lt;Class&lt;V>, V> where the 
	 * <i>Class&lt;V></i> is a key to return a value of <i>V</i>.  Since we
	 * are banning ourselves from using such maps, we instead use an 
	 * EAnnotation on the EClass.  However, EMF will not allow us to store
	 * arbitrary classes and objects as annotations; instead we must serialize
	 * these somehow as strings.
	 * 
	 * <p>
	 * The solution then is to store the fully qualified name of a class that
	 * can act as a factory for the actual adapter (an implementation of 
	 * {@link IAdapterFactory}.  This is stored as (the value of a 
	 * well-defined key to) an EAnnotation whose source is derived from the 
	 * fully qualified name of the adapter class itself.  The factory must be 
	 * provided by the caller.
	 * 
	 * <p>
	 * For example, if setting an adapter of SuperDuperDomainClass.class, one 
	 * would need to write a SuperDuperDomainClassFactory that could 
	 * instantiate the SuperDuperDomainClass.  In terms of EMF, we would have
	 * an EAnnotation with source of 
	 * <i>http://de.berlios.rcpviewer/progmodel/extensions/com.mycompany.SuperDuperDomainClass</i>
	 * whose details would have a key of <i>adapterFactoryClassName</i> with
	 * a value of <i>com.mycompany.SuperDuperDomainClassFactory</i>.
	 * 
	 * 
	 * @param adapterClass
	 * @param adapter
	 */
	public <V> void setAdapterFactory(Class<V> adapterClass, IAdapterFactory<? extends V> adapterFactory) {
		EAnnotation eAnnotation = 
			_emfAnnotations.annotationOf(_eClass, StandardProgModelConstants.ANNOTATION_EXTENSIONS_PREFIX + adapterClass.getName());
		Map<String,String> detailsPlusFactoryName = new HashMap<String,String>();
		detailsPlusFactoryName.putAll(adapterFactory.getDetails());
		detailsPlusFactoryName.put(StandardProgModelConstants.ANNOTATION_EXTENSIONS_ADAPTER_FACTORY_NAME_KEY, adapterFactory.getClass().getName());
		_emfAnnotations.putAnnotationDetails(eAnnotation, detailsPlusFactoryName);
	}



	/*
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() { return _eClass.getInstanceClassName() ; }

	
	///////////////////////////////////////////////////////////////
	// helpers 

	/**
	 * Since descriptions are stored as annotations and apply to many model
	 * elements, this is a convenient way of getting to the description.
	 * 
	 * <p>
	 * TODO: should use serializer???
	 * 
	 * @param modelElement
	 * @return
	 */
	private String descriptionOf(EModelElement modelElement) {
		EAnnotation annotation = 
			modelElement.getEAnnotation(StandardProgModelConstants.ANNOTATION_ELEMENT);
		if (annotation == null) {
			return null;
		}
		return (String)annotation.getDetails().get(StandardProgModelConstants.ANNOTATION_ELEMENT_DESCRIPTION_KEY);
	}

	private int computeFieldLengthOf(EModelElement modelElement) {

		FieldLengthOf fieldLengthOfAnnotation = _extendedSerializer.getFieldLengthOf(modelElement);
		MinLengthOf minLengthOfAnnotation = _extendedSerializer.getMinLengthOf(modelElement);
		MaxLengthOf maxLengthOfAnnotation = _extendedSerializer.getMaxLengthOf(modelElement);

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

	private int computeMaxLengthOf(final EModelElement modelElement) {

		FieldLengthOf fieldLengthOfAnnotation = _extendedSerializer.getFieldLengthOf(modelElement);
		MinLengthOf minLengthOfAnnotation = _extendedSerializer.getMinLengthOf(modelElement);
		MaxLengthOf maxLengthOfAnnotation = _extendedSerializer.getMaxLengthOf(modelElement);

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

	private int computeMinLengthOf(final EModelElement modelElement) {

		MinLengthOf minLengthOfAnnotation = _extendedSerializer.getMinLengthOf(modelElement);
		MaxLengthOf maxLengthOfAnnotation = _extendedSerializer.getMaxLengthOf(modelElement);

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


}
