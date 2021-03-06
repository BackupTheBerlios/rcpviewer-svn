package org.essentialplatform.core.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

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
import org.essentialplatform.core.deployment.Binding;
import org.essentialplatform.core.deployment.IAttributeBinding;
import org.essentialplatform.core.deployment.IBinding;
import org.essentialplatform.core.deployment.IDomainClassBinding;
import org.essentialplatform.core.deployment.IOperationBinding;
import org.essentialplatform.core.deployment.IReferenceBinding;
import org.essentialplatform.core.domain.adapters.IAdapterFactory;
import org.essentialplatform.core.domain.adapters.IDomainClassAdapter;
import org.essentialplatform.core.domain.filters.IFilter;
import org.essentialplatform.core.domain.filters.IdAttributeFilter;
import org.essentialplatform.core.domain.validators.IValidator;
import org.essentialplatform.core.domain.validators.RegexValidator;
import org.essentialplatform.core.emf.Emf;
import org.essentialplatform.core.emf.EmfAnnotations;
import org.essentialplatform.core.features.FeatureId;
import org.essentialplatform.core.features.IFeatureId;
import org.essentialplatform.core.i18n.II18nData;
import org.essentialplatform.progmodel.essential.app.Abbreviated;
import org.essentialplatform.progmodel.essential.app.AssignmentType;
import org.essentialplatform.progmodel.essential.app.BusinessKey;
import org.essentialplatform.progmodel.essential.app.FieldLengthOf;
import org.essentialplatform.progmodel.essential.app.Id;
import org.essentialplatform.progmodel.essential.app.ImmutableOncePersisted;
import org.essentialplatform.progmodel.essential.app.Lifecycle;
import org.essentialplatform.progmodel.essential.app.Mask;
import org.essentialplatform.progmodel.essential.app.MaxLengthOf;
import org.essentialplatform.progmodel.essential.app.MinLengthOf;
import org.essentialplatform.progmodel.essential.app.MultiLine;
import org.essentialplatform.progmodel.essential.app.Named;
import org.essentialplatform.progmodel.essential.app.Regex;
import org.essentialplatform.progmodel.essential.core.EssentialProgModelStandardSemanticsConstants;
import org.essentialplatform.progmodel.essential.core.emf.EssentialProgModelExtendedSemanticsEmfSerializer;
import org.essentialplatform.progmodel.essential.core.emf.EssentialProgModelStandardSemanticsEmfSerializer;
import org.osgi.framework.Bundle;

public final class DomainClass implements IDomainClass {

	
	/**
	 * To deserialize semantics from EMF metamodel.
	 */
	private EssentialProgModelStandardSemanticsEmfSerializer _standardSerializer = new EssentialProgModelStandardSemanticsEmfSerializer();
	private EssentialProgModelExtendedSemanticsEmfSerializer _extendedSerializer = new EssentialProgModelExtendedSemanticsEmfSerializer();

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
	
	public DomainClass(final IDomain domain, final EClass eClass, final Object classRepresentation) {
		this._domain = domain;
		this._eClass = eClass;
		this._classRepresentation = classRepresentation;
	}


	/////////////////////////////////////////////////////////////////////
	// Domain
	/////////////////////////////////////////////////////////////////////

	protected final IDomain _domain;
	/*
	 * @see org.essentialplatform.domain.IDomainClass#getDomain()
	 */
	public IDomain getDomain() {
		return _domain;
	}


	
	/////////////////////////////////////////////////////////////////////
	// Binding
	/////////////////////////////////////////////////////////////////////
	
	private IDomainClassBinding _binding;
	// JAVA5_FIXME
	public <V extends IDomainClassBinding> V getBinding() {
		return (V)_binding;
	}
	public <V extends IDomainClassBinding> void setBinding(V binding) {
		_binding = binding;
	}

	/*
	 * @see org.essentialplatform.core.domain.IDomainClass#replaceBindings(org.essentialplatform.core.deployment.IBinding)
	 */
	public void replaceBindings(IBinding binding) {
		setBinding(binding.bind(this, _classRepresentation));
		for(IAttribute attrib: _attributesByEAttribute.values()) {
			attrib.replaceBindings(binding);
		}
		for(IReference reference: _referencesByEReference.values()) {
			reference.replaceBindings(binding);
		}
		for(IOperation operation: _operationsByEOperation.values()) {
			operation.replaceBindings(binding);
		}
	}


	/////////////////////////////////////////////////////////////////////
	// ClassRepresentation
	/////////////////////////////////////////////////////////////////////

	private final Object _classRepresentation;
	/*
	 * @see org.essentialplatform.core.domain.IDomainClass#getClassRepresentation()
	 */
	public Object getClassRepresentation() {
		return _classRepresentation;
	}


	/////////////////////////////////////////////////////////////////////
	// Name, Description, Abbreviation
	/////////////////////////////////////////////////////////////////////
	

	/*
	 * @see org.essentialplatform.domain.IDomainClass#getName()
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
	 * @see org.essentialplatform.domain.IDomainClass#getDescription()
	 */
	public String getDescription() {
		return descriptionOf(_eClass);
	}

	
	/*
	 * @see org.essentialplatform.core.domain.IDomainClass#getAbbreviation()
	 */
	public String getAbbreviation(){
		Abbreviated abbreviated = _extendedSerializer.getAbbreviation(getEClass());
		if (abbreviated == null) {
			return getEClassName().toUpperCase();
		}
		return abbreviated.value();
	}



	/////////////////////////////////////////////////////////////////////
	// EClass, EClassName
	/////////////////////////////////////////////////////////////////////

	protected final EClass _eClass;
	/*
	 * @see org.essentialplatform.domain.IDomainClass#getEClass()
	 */
	public EClass getEClass() {
		return _eClass;
	}

	/*
	 * @see org.essentialplatform.domain.IDomainClass#getEClassName()
	 */
	public String getEClassName() {
		return _eClass.getName();
	}

	
	/////////////////////////////////////////////////////////////////////
	// other stuff...
	/////////////////////////////////////////////////////////////////////

	/*
	 * @see org.essentialplatform.domain.IDomainClass#isChangeable()
	 */
	public boolean isChangeable() {
		EAnnotation annotation = 
			_eClass.getEAnnotation(EssentialProgModelStandardSemanticsConstants.ANNOTATION_ELEMENT);
		if (annotation == null) {
			return false;
		}
		String immutable = 
			(String)annotation.getDetails().get(EssentialProgModelStandardSemanticsConstants.ANNOTATION_ELEMENT_IMMUTABLE_KEY);
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
	 * @see org.essentialplatform.domain.IDomainClass#getI18nData()
	 */
	public II18nData getI18nData() {
		throw new RuntimeException("Not yet implemented");
	}

	/*
	 * @see org.essentialplatform.progmodel.extended.IExtendedDomainClass#isSearchable()
	 */
	public boolean isSearchable() {
		Lifecycle lifecycle = _extendedSerializer.getClassLifecycle(_eClass);
		return lifecycle != null && lifecycle.searchable();
	}


	/*
	 * @see org.essentialplatform.progmodel.extended.IExtendedDomainClass#isInstantiable()
	 */
	public boolean isInstantiable() {
		Lifecycle lifecycle = _extendedSerializer.getClassLifecycle(_eClass);
		return lifecycle != null && lifecycle.instantiable();
	}

	/*
	 * @see org.essentialplatform.progmodel.extended.IExtendedDomainClass#isSaveable()
	 */
	public boolean isSaveable() {
		Lifecycle lifecycle = _extendedSerializer.getClassLifecycle(_eClass);
		return lifecycle != null && lifecycle.saveable();
	}

	/*
	 * @see org.essentialplatform.progmodel.extended.IExtendedDomainClass#isImmutableOncePersisted()
	 */
	public boolean isImmutableOncePersisted() {
		return _extendedSerializer.getClassImmutableOncePersisted(_eClass) != null;
	}

	/*
	 * @see org.essentialplatform.progmodel.extended.IDomainClass#isSimpleId()
	 */
	public boolean isSimpleId() {
		return iAttributes(new IdAttributeFilter()).size() == 1;
	}

	/*
	 * @see org.essentialplatform.progmodel.extended.IExtendedDomainClass#isCompositeId()
	 */
	public boolean isCompositeId() {
		return iAttributes(new IdAttributeFilter()).size() > 1;
	}
	
	/*
	 * @see org.essentialplatform.progmodel.extended.IExtendedDomainClass#getAssignmentType()
	 */
	public AssignmentType getIdAssignmentType() {
		if (isCompositeId()) {
			return AssignmentType.APPLICATION;
		}
		List<IAttribute> attributes = iAttributes();
		for(Iterator<IAttribute> iter = attributes.iterator(); iter.hasNext(); ) {
			IAttribute attr = iter.next();
			EAttribute eAttr = attr.getEAttribute();
			Id id = _extendedSerializer.getAttributeId(eAttr);
			if (id == null) {
				continue;
			}
			EDataType attributeType = eAttr.getEAttributeType();
			if (!_emf.isIntegralNumber(attributeType)) {
				return AssignmentType.APPLICATION;
			}
			AssignmentType assignedBy = id.assignedBy();
			// is an integral type
			if (assignedBy == AssignmentType.CONTEXT) {
				assignedBy = AssignmentType.OBJECT_STORE;
			}
			return assignedBy;
		}
		return AssignmentType.OBJECT_STORE;
	}

	
	/*
	 * @see org.essentialplatform.progmodel.extended.IDomainClass#businessKeys()
	 */
	public Map<String, List<IDomainClass.IAttribute>> businessKeys() {
		Map<String, Map<Integer,IDomainClass.IAttribute>> businessKeyAttributesByPosByName = 
			new HashMap<String, Map<Integer,IDomainClass.IAttribute>>();
		for(IDomainClass.IAttribute attribute: iAttributes()) {
			BusinessKey businessKey = _extendedSerializer.getAttributeBusinessKey(attribute.getEAttribute());
			if (businessKey == null) {
				continue;
			}
			Map<Integer, IDomainClass.IAttribute> businessKeyAttributesByPos = 
				businessKeyAttributesByPosByName.get(businessKey.name());
			if (businessKeyAttributesByPos == null) {
				businessKeyAttributesByPos = new HashMap<Integer, IDomainClass.IAttribute>();
				businessKeyAttributesByPosByName.put(businessKey.name(), businessKeyAttributesByPos);
			}
			IDomainClass.IAttribute businessKeyAttribute =  
				businessKeyAttributesByPos.get(businessKey.pos());
			if (businessKeyAttribute != null) {
				// we already have an attribute in this position, so give up
				businessKeyAttributesByPos.put(-1, null); // magic value indicating an error
				continue;
			}
			businessKeyAttributesByPos.put(businessKey.pos(), attribute);
		}
		
		// instantiate the Map that we will return.
		Map<String, List<IDomainClass.IAttribute>> businessKeyAttributeListByName = 
			new HashMap<String, List<IDomainClass.IAttribute>>();
		
		// process the Map of Maps and convert all good maps into lists
		nextBusinessKey:
		for(String businessKeyName: businessKeyAttributesByPosByName.keySet()) {
			Map<Integer, IDomainClass.IAttribute> businessKeyAttributesByPos =
				businessKeyAttributesByPosByName.get(businessKeyName);
			// check for our magic value meaning this is a bad map
			if (businessKeyAttributesByPos.get(-1)!=null) {
				continue;
			}
			// ensure that all values are contiguous
			int size = businessKeyAttributesByPos.size();
			List<IDomainClass.IAttribute> businessKeyAttributes = 
				new ArrayList<IDomainClass.IAttribute>();
			nextBusinessKeyAttributes:
			for(int i=0; i<size; i++) {
				IDomainClass.IAttribute businessKeyAttribute = businessKeyAttributesByPos.get(i+1);
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

	public boolean containsIAttribute(IAttribute iAttribute) {
		return this._eClass.getEAllAttributes().contains(iAttribute.getEAttribute());
	}

	/*
	 * @see org.essentialplatform.domain.IDomainClass#getEAttributeNamed(java.lang.String)
	 */
	public IAttribute getIAttributeNamed(String attributeName) {
		for(IAttribute iAttribute: iAttributes() ) {
			if (iAttribute.getName().equals(attributeName)) {
				return iAttribute;
			}
		}
		return null;
	}

	/*
	 * @see org.essentialplatform.domain.IDomainClass#getAttribute(org.eclipse.emf.ecore.EAttribute)
	 */
	public synchronized IAttribute getIAttribute(final EAttribute eAttribute) {
		IAttribute attribute = _attributesByEAttribute.get(eAttribute);
		if (attribute == null) {
			Attribute concreteAttribute = new Attribute(eAttribute);
			concreteAttribute.setBinding(Binding.getBinding().bindingFor(concreteAttribute));
			attribute = concreteAttribute;
			_attributesByEAttribute.put(eAttribute, attribute);
		}
		return attribute;
	}


	/*
	 * @see org.essentialplatform.domain.IDomainClass#iAttributes()
	 */
	public List<IAttribute> iAttributes() {
		final boolean includeInherited = true;
		EList eAttributes = includeInherited?
								getEClass().getEAllAttributes():
								getEClass().getEAttributes();

		List<IAttribute> attributes = new ArrayList<IAttribute>();
		for(EAttribute eAttribute: (List<EAttribute>)eAttributes) {
			attributes.add(getIAttribute(eAttribute));
		}
		return attributes;
	}

	/*
	 * @see org.essentialplatform.core.domain.IDomainClass#iAttributes(org.essentialplatform.core.domain.IAttributeComparator)
	 */
	public List<IAttribute> iAttributes(final Comparator<IAttribute> comparator) {
		List<IAttribute> attributes = iAttributes();
		Collections.sort(attributes, comparator);
		return attributes;
	}

	/*
	 * @see org.essentialplatform.core.domain.IDomainClass#iAttributes(org.essentialplatform.core.domain.IAttributeFilter)
	 */
	public List<IAttribute> iAttributes(IFilter<IAttribute> filter) {
		List<IAttribute> attributes = iAttributes();
		List<IAttribute> filteredAttributes = new ArrayList<IAttribute>();
		for(IAttribute attribute: attributes) {
			if (filter.accept(attribute)) {
				filteredAttributes.add(attribute);
			}
		}
		return filteredAttributes;
	}
	
	/*
	 * @see org.essentialplatform.core.domain.IDomainClass#iAttributes(org.essentialplatform.core.domain.IAttributeFilter, org.essentialplatform.core.domain.IAttributeComparator)
	 */
	public List<IAttribute> iAttributes(IFilter<IAttribute> filter, Comparator<IAttribute> comparator) {
		List<IAttribute> attributes = iAttributes(filter);
		Collections.sort(attributes, comparator);
		return attributes;
	}


	public int getNumberOfAttributes() {
		return getEClass().getEAllAttributes().size();
	}


	private final class Attribute extends Member implements IDomainClass.IAttribute {

		private final EAttribute _eAttribute;
		private IAttributeBinding _binding;
		/**
		 * built up first time ask for validators.
		 */
		private List<IValidator> _validators = null;
		
		public Attribute(EAttribute eAttribute) {
			_eAttribute = eAttribute;
		}
		
		/*
		 * @see org.essentialplatform.core.domain.IDomainClass.IMember#getName()
		 */
		public String getName() {
			return _eAttribute.getName();
		}

		/*
		 * @see org.essentialplatform.domain.IDomainClass.IAttribute#getEAttribute()
		 */
		public EAttribute getEAttribute() {
			return _eAttribute;
		}
		
		<V extends IAttributeBinding> void setBinding(V binding) {
			_binding = binding;
		}

		public void replaceBindings(IBinding binding) {
			setBinding(binding.bindingFor(this));
		}


		
		/*
		 * @see org.essentialplatform.domain.IDomainClass.IAttribute#isWriteOnly()
		 */
		public boolean isWriteOnly() {
			return _eAttribute.getEAnnotation(EssentialProgModelStandardSemanticsConstants.ANNOTATION_ATTRIBUTE_WRITE_ONLY) != null;
		}
		/*
		 * @see org.essentialplatform.domain.IDomainClass.IAttribute#isChangeable()
		 */
		public boolean isChangeable() {
			return _eAttribute.isChangeable();
		}
		/*
		 * @see org.essentialplatform.domain.IDomainClass.IAttribute#isDerived()
		 */
		public boolean isDerived() {
			return _eAttribute.isDerived();
		}
		/*
		 * @see org.essentialplatform.domain.IDomainClass.IAttribute#getLowerBound()
		 */
		public int getLowerBound() {
			return _eAttribute.getLowerBound();
		}
		/*
		 * @see org.essentialplatform.domain.IDomainClass.IAttribute#getUpperBound()
		 */
		public int getUpperBound() {
			return _eAttribute.getUpperBound();
		}
		/*
		 * @see org.essentialplatform.domain.IDomainClass.IAttribute#isRequired()
		 */
		public boolean isRequired() {
			return _eAttribute.isRequired();
		}
		/*
		 * @see org.essentialplatform.domain.IDomainClass.IAttribute#isMany()
		 */
		public boolean isMany() {
			return _eAttribute.isMany();
		}
		/*
		 * @see org.essentialplatform.domain.IDomainClass.IAttribute#isUnique()
		 */
		public boolean isUnique() {
			return _eAttribute.isUnique();
		}
		/*
		 * @see org.essentialplatform.domain.IDomainClass.IAttribute#isOrdered()
		 */
		public boolean isOrdered() {
			return _eAttribute.isOrdered();
		}
		/*
		 * @see org.essentialplatform.domain.IDomainClass.IAttribute#isUnsettable()
		 */
		public boolean isUnsettable() {
			return _eAttribute.isUnsettable();
		}
		/*
		 * @see org.essentialplatform.domain.IDomainClass.IAttribute#getI18nDataFor()
		 */
		public II18nData getI18nData() {
			throw new RuntimeException("Not yet implemented");
		}

		/*
		 * @see org.essentialplatform.domain.IDomainClass.IAttribute#isId()
		 */
		public boolean isId() {
			return _extendedSerializer.getAttributeId(_eAttribute) != null;
		}
		/*
		 * @see org.essentialplatform.domain.IDomainClass.IAttribute#isOptional()
		 */
		public boolean isOptional() {
			return _extendedSerializer.getOptional(_eAttribute) != null;
		}
		/*
		 * @see org.essentialplatform.domain.IDomainClass.IAttribute#isMandatory()
		 */
		public boolean isMandatory() {
			return !isOptional();
		}
		/*
		 * @see org.essentialplatform.domain.IDomainClass.IAttribute#isInvisible()
		 */
		public boolean isInvisible() {
			return _extendedSerializer.getAttributeInvisible(_eAttribute) != null;
		}
		/*
		 * @see org.essentialplatform.domain.IDomainClass.IAttribute#getMultiLine()
		 */
		public int getMultiLine() {
			if (!returnsString()) {
				return -1;
			}
			MultiLine multiLine = _extendedSerializer.getMultiLine(_eAttribute);
			if (multiLine == null) {
				return -1;
			}
			return multiLine.value() > 0?multiLine.value():-1;
		}
		/*
		 * @see org.essentialplatform.domain.IDomainClass.IAttribute#getFieldLengthOf()
		 */
		public int getFieldLengthOf() {
			if (!returnsString()) {
				return -1;
			}
			return computeFieldLengthOf(_eAttribute);
		}
		/*
		 * @see org.essentialplatform.domain.IDomainClass.IAttribute#getMaxLengthOf()
		 */
		public int getMaxLengthOf() {
			if (!returnsString()) {
				return -1;
			}
			return computeMaxLengthOf(_eAttribute);
		}
		/*
		 * @see org.essentialplatform.domain.IDomainClass.IAttribute#getMinLengthOf()
		 */
		public int getMinLengthOf() {
			if (!returnsString()) {
				return -1;
			}
			return computeMinLengthOf(_eAttribute);
		}
		/*
		 * @see org.essentialplatform.domain.IDomainClass.IAttribute#isImmutableOncePersisted()
		 */
		public boolean isImmutableOncePersisted() {
			ImmutableOncePersisted immutableOncePersisted = _extendedSerializer.getAttributeImmutableOncePersisted(_eAttribute);
			return immutableOncePersisted != null && !immutableOncePersisted.optout();
		}
		/*
		 * @see org.essentialplatform.domain.IDomainClass.IAttribute#getMask()
		 */
		public String getMask() {
			Mask mask = _extendedSerializer.getAttributeMask(_eAttribute);
			if (mask == null) return null;
			return mask.value();
		}

		/*
		 * @see org.essentialplatform.domain.IDomainClass.IAttribute#getBinding()
		 */
		public <V extends IAttributeBinding> V getBinding() {
			return (V)_binding; // JAVA5_FIXME
		}

		/*
		 * @see org.essentialplatform.domain.IDomainClass#attributeIdFor(org.eclipse.emf.ecore.EAttribute)
		 */
		public IFeatureId getFeatureId() {
			return FeatureId.create(_eAttribute.getName(), getDomainClass(), IFeatureId.Type.ATTRIBUTE); 
		}

		
		@Override
		public String toString() {
			return "IAttribute: " + _eAttribute.getName();
		}
		
		//////////////
		// helpers
		
		private boolean returnsString() {
			EDataType dataType = _eAttribute.getEAttributeType();
			String instanceClassName = dataType.getInstanceClassName();
			return instanceClassName != null && instanceClassName.equals("java.lang.String");
		}

		/*
		 * @see org.essentialplatform.core.domain.IDomainClass.IAttribute#isValid(java.lang.String)
		 */
		public boolean isValid(String candidate) {
			for(IValidator validator: validators()) {
				if (!validator.isValid(candidate)) {
					return false;
				}
			}
			return true;
		}

		/*
		 * TODO: it would be nice to have the IValidators somehow injected into
		 * the IAttribute, rather than it have to know how to recreate them.
		 * 
		 * @see org.essentialplatform.core.domain.IDomainClass.IAttribute#validators()
		 */
		public synchronized List<IValidator> validators() {
			if (_validators == null) {
				_validators = new ArrayList<IValidator>();
				// regex
				Regex regex = _extendedSerializer.getAttributeRegex(_eAttribute);
				if (regex != null) {
					_validators.add(new RegexValidator(regex));
				}
			}
			return _validators;
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
	 * @see org.essentialplatform.domain.IDomainClass#iReferences()
	 */
	public List<IReference> iReferences() {
		final boolean includeInherited = true;
		EList eReferences = includeInherited?
								getEClass().getEAllReferences():
								getEClass().getEReferences();

		List<IReference> references = new ArrayList<IReference>();
		for(EReference eReference: (List<EReference>)eReferences) {
			references.add(getIReference(eReference));
		}
		return references;
	}

	/*
	 * @see org.essentialplatform.core.domain.IDomainClass#iReferences(org.essentialplatform.core.domain.IComparator<IReference>)
	 */
	public List<IReference> iReferences(final Comparator<IReference> comparator) {
		List<IReference> references = iReferences();
		Collections.sort(references, comparator);
		return references;
	}

	/*
	 * @see org.essentialplatform.core.domain.IDomainClass#iReferences(org.essentialplatform.core.domain.IFilter<IReference>)
	 */
	public List<IReference> iReferences(IFilter<IReference> filter) {
		List<IReference> references = iReferences();
		List<IReference> filteredReferences = new ArrayList<IReference>();
		for(IReference reference: references) {
			if (filter.accept(reference)) {
				filteredReferences.add(reference);
			}
		}
		return filteredReferences;
	}
	
	/*
	 * @see org.essentialplatform.core.domain.IDomainClass#iReferences(org.essentialplatform.core.domain.IFilter<IReference>, org.essentialplatform.core.domain.IComparator<IReference>)
	 */
	public List<IReference> iReferences(IFilter<IReference> filter, Comparator<IReference> comparator) {
		List<IReference> references = iReferences(filter);
		Collections.sort(references, comparator);
		return references;
	}

	
	public IReference getIReferenceNamed(String referenceName) {
		for(IReference iReference: iReferences() ) {
			if (iReference.getName().equals(referenceName)) {
				return iReference;
			}
		}
		return null;
	}

	public boolean containsReference(IReference iReference) {
		return this._eClass.getEAllReferences().contains(iReference.getEReference());
	}

	/**
	 * Returns an {@link IReference} for any EReference, whether it represents
	 * a 1:1 reference or a collection.
	 *  
	 * @param eReference
	 * @return
	 */
	public synchronized IReference getIReference(final EReference eReference) {
		if (eReference == null) {
			return null;
		}
		IReference reference = _referencesByEReference.get(eReference);
		if (reference == null) {
			if (eReference.isMany()) {
				CollectionReference concreteReference = new CollectionReference(eReference);
				concreteReference.setBinding(Binding.getBinding().bindingFor(concreteReference));
				reference = concreteReference;
			} else {
				OneToOneReference concreteReference = new OneToOneReference(eReference);
				concreteReference.setBinding(Binding.getBinding().bindingFor(concreteReference));
				reference = concreteReference;
			}
			_referencesByEReference.put(eReference, reference);
		}
		return reference;
	}

	private abstract class Reference extends Member implements IDomainClass.IReference {
		
		final EReference _eReference;
		private IReferenceBinding _binding;
		
		Reference(EReference eReference) {
			_eReference = eReference;
		}
		
		/*
		 * @see org.essentialplatform.core.domain.IDomainClass.IMember#getName()
		 */
		public String getName() {
			return _eReference.getName();
		}


		/*
		 * @see org.essentialplatform.domain.IDomainClass.IReference#getEReference()
		 */
		public EReference getEReference() {
			return _eReference;
		}

		/*
		 * @see org.essentialplatform.domain.IDomainClass.IReference#getBinding()
		 */
		public <V extends IReferenceBinding> V getBinding() {
			return (V)_binding; // JAVA5_FIXME
		}
		<V extends IReferenceBinding> void setBinding(V binding) {
			_binding = binding;
		}
		

		/*
		 * @see org.essentialplatform.domain.IDomainClass.IReference#getReferencedClass()
		 */
		public IDomainClass getReferencedDomainClass() {
			EClass eClass = (EClass)_eReference.getEReferenceType();
			return _domain.lookupNoRegister(((Class<?>)eClass.getInstanceClass()));
		}
		/*
		 * @see org.essentialplatform.domain.IDomainClass.IReference#isMultiple()
		 */
		public boolean isMultiple() {
			return _eReference.isMany();
		}
		/*
		 * @see org.essentialplatform.domain.IDomainClass.IReference#isOrdered()
		 */
		public boolean isOrdered() {
			return _eReference.isOrdered();
		}
		/*
		 * @see org.essentialplatform.domain.IDomainClass.IReference#isContainer()
		 */
		public boolean isContainer() {
			return _eReference.isContainer();
		}
		/*
		 * @see org.essentialplatform.domain.IDomainClass.IReference#isUnique()
		 */
		public boolean isUnique() {
			return _eReference.isUnique();
		}
		/*
		 * @see org.essentialplatform.domain.IDomainClass.IReference#isChangeable()
		 */
		public boolean isChangeable() {
			return _eReference.isChangeable();
		}
		/*
		 * @see org.essentialplatform.domain.IDomainClass.IReference#isDerived()
		 */
		public boolean isDerived() {
			return _eReference.isDerived();
		}

		/*
		 * @see org.essentialplatform.domain.IDomainClass#referenceIdFor(org.eclipse.emf.ecore.EReference)
		 */
		public IFeatureId getFeatureId() {
			return FeatureId.create(_eReference.getName(), getDomainClass(), IFeatureId.Type.REFERENCE);
		}

		@Override
		public String toString() {
			return "IReference: " + _eReference.getName();
		}


	}

	///////////////////////////////////////////////////////////////
	// 1:1 references 

	public synchronized IOneToOneReference getIOneToOneReference(final EReference eReference) {
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
		
		public void replaceBindings(IBinding binding) {
			setBinding(binding.bindingFor(this));
		}


		@Override
		public String toString() {
			return "1:1 IReference: " + _eReference.getName();
		}

	}

	
	///////////////////////////////////////////////////////////////
	// collections 
	
	public synchronized ICollectionReference getICollectionReference(final EReference eReference) {
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

		public void replaceBindings(IBinding binding) {
			setBinding(binding.bindingFor(this));
		}


		@Override
		public String toString() {
			return "1:M IReference: " + _eReference.getName();
		}
	}

	///////////////////////////////////////////////////////////////
	// operations 



	/*
	 * @see org.essentialplatform.domain.IDomainClass#iOperations()
	 */
	public List<IOperation> iOperations() {
		final boolean includeInherited = true;
		EList eOperations = includeInherited?
								getEClass().getEAllOperations():
								getEClass().getEOperations();

		List<IOperation> operations = new ArrayList<IOperation>();
		for(EOperation eOperation: (List<EOperation>)eOperations) {
			operations.add(getIOperation(eOperation));
		}
		return operations;
	}

	/*
	 * @see org.essentialplatform.core.domain.IDomainClass#iOperations(org.essentialplatform.core.domain.IComparator<IOperation>)
	 */
	public List<IOperation> iOperations(final Comparator<IOperation> comparator) {
		List<IOperation> operations = iOperations();
		Collections.sort(operations, comparator);
		return operations;
	}

	/*
	 * @see org.essentialplatform.core.domain.IDomainClass#iOperations(org.essentialplatform.core.domain.IFilter<IOperation>)
	 */
	public List<IOperation> iOperations(IFilter<IOperation> filter) {
		List<IOperation> operations = iOperations();
		List<IOperation> filteredOperations = new ArrayList<IOperation>();
		for(IOperation operation: operations) {
			if (filter.accept(operation)) {
				filteredOperations.add(operation);
			}
		}
		return filteredOperations;
	}
	
	/*
	 * @see org.essentialplatform.core.domain.IDomainClass#iOperations(org.essentialplatform.core.domain.IFilter<IOperation>, org.essentialplatform.core.domain.IComparator<IOperation>)
	 */
	public List<IOperation> iOperations(IFilter<IOperation> filter, Comparator<IOperation> comparator) {
		List<IOperation> operations = iOperations(filter);
		Collections.sort(operations, comparator);
		return operations;
	}

	
	public IOperation getIOperationNamed(String operationName) {
		for(IOperation iOperation: iOperations() ) {
			if (iOperation.getName().equals(operationName)) {
				return iOperation;
			}
		}
		return null;
	}

	public synchronized IOperation getIOperation(EOperation eOperation) {
		if (eOperation == null) {
			return null;
		}
		IOperation operation = _operationsByEOperation.get(eOperation);
		if (operation == null) {
			Operation concreteOperation = new Operation(eOperation);
			concreteOperation.setBinding(Binding.getBinding().bindingFor(concreteOperation));
			operation = concreteOperation;
			_operationsByEOperation.put(eOperation, operation);
		}
		return operation;
	}

	public boolean containsOperation(IOperation iOperation) {
		return this._eClass.getEAllOperations().contains(iOperation.getEOperation());
	}

	private final class Operation extends Member implements IDomainClass.IOperation {
		
		public Operation(EOperation eOperation) {
			_eOperation = eOperation;
		}
		
		/*
		 * @see org.essentialplatform.core.domain.IDomainClass.IMember#getName()
		 */
		public String getName() {
			return _eOperation.getName();
		}


		private final EOperation _eOperation;
		/*
		 * @see org.essentialplatform.domain.IDomainClass.IOperation#getEOperation()
		 */
		public EOperation getEOperation() {
			return _eOperation;
		}
		
		private IOperationBinding _binding;
		
		/*
		 * @see org.essentialplatform.domain.IDomainClass.IOperation#getBinding()
		 */
		public <V extends IOperationBinding> V getBinding() {
			return (V)_binding; // JAVA5_FIXME
		}
		<V extends IOperationBinding> void setBinding(V binding) {
			_binding = binding;
		}
		public void replaceBindings(IBinding binding) {
			setBinding(binding.bindingFor(this));
		}


		/*
		 * @see org.essentialplatform.domain.IDomainClass.IOperation#isStatic()
		 */
		public boolean isStatic() {
			return _eOperation.getEAnnotation(EssentialProgModelStandardSemanticsConstants.ANNOTATION_OPERATION_STATIC) != null;
		}
		
		/*
		 * @see org.essentialplatform.domain.IDomainClass.IOperation#getEDataTypeFor(int)
		 */
		public EDataType getEDataTypeFor(final int parameterPosition) {
			if (!isParameterAValue(parameterPosition)) {
				throw new IllegalArgumentException("Parameter does not represent a value.");
			}
			EParameter parameter = getParameter(parameterPosition);
			return (EDataType)parameter.getEType();
		}
		
		/*
		 * @see org.essentialplatform.domain.IDomainClass.IOperation#getI18nDataFor()
		 */
		public II18nData getI18nData() {
			throw new RuntimeException("Not yet implemented");
		}

		/*
		 * @see org.essentialplatform.domain.IDomainClass.IOperation#getI18nDataFor()
		 */
		public II18nData getI18nDataFor(final int parameterPosition) {
			throw new RuntimeException("Not yet implemented");
		}

		/*
		 * @see org.essentialplatform.domain.IDomainClass.IOperation#getDomainClassFor(int)
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
		 * @see org.essentialplatform.domain.IDomainClass.IOperation#getNameFor(int)
		 */
		public String getNameFor(int parameterPosition) {
			EParameter parameter = getParameter(parameterPosition);
			return parameter.getName();
		}
		
		/*
		 * @see org.essentialplatform.domain.IDomainClass.IOperation#getDescriptionFor(int)
		 */
		public String getDescriptionFor(int parameterPosition) {
			EParameter parameter = getParameter(parameterPosition);
			return descriptionOf(parameter);
		}
		
		/*
		 * @see org.essentialplatform.domain.IDomainClass#isParameterAValue(org.eclipse.emf.ecore.EOperation, int)
		 */
		public boolean isParameterAValue(int parameterPosition) {
			EParameter parameter = getParameter(parameterPosition);
			return parameter.getEType() instanceof EDataType;
		}

		/*
		 * @see org.essentialplatform.domain.IDomainClass#isParameterADomainObject(org.eclipse.emf.ecore.EOperation, int)
		 */
		public boolean isParameterADomainObject(int parameterPosition) {
			EParameter parameter = getParameter(parameterPosition);
			return parameter.getEType() instanceof EClass;
		}

		/*
		 * @see org.essentialplatform.domain.IDomainClass.IOperation#isOptional(int)
		 */
		public boolean isOptional(final int parameterPosition) {
			return _extendedSerializer.getOptional(getParameter(parameterPosition)) != null;
		}

		/*
		 * @see org.essentialplatform.domain.IDomainClass.IOperation#isMandatory(int)
		 */
		public boolean isMandatory(final int parameterPosition) {
			return !isOptional(parameterPosition);
		}

		/*
		 * @see org.essentialplatform.domain.IDomainClass.IOperation#getFieldLengthOf(int)
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
		 * @see org.essentialplatform.core.domain.IDomainClass.IOperation#getMultiLine()
		 */
		public int getMultiLine(final int parameterPosition) {
			EParameter parameter = getParameter(parameterPosition);
			if (!isString(parameter)) {
				return -1;
			}
			MultiLine multiLine = _extendedSerializer.getMultiLine(parameter);
			if (multiLine == null) {
				return -1;
			}
			return multiLine.value() > 0?multiLine.value():-1;
		}

		/*
		 * @see org.essentialplatform.domain.IDomainClass#operationIdFor(org.eclipse.emf.ecore.EOperation)
		 */
		public IFeatureId getFeatureId() {
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

		@Override
		public String toString() {
			return "IOperation: " + _eOperation.getName();
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
			EssentialProgModelStandardSemanticsConstants.ANNOTATION_EXTENSIONS_PREFIX + adapterClass.getName();
		return getAdapter(annotationSource);
	}

	public List<IDomainClassAdapter> getAdapters() {
		List<EAnnotation> annotations = 
			_emfAnnotations.annotationsPrefixed(_eClass, EssentialProgModelStandardSemanticsConstants.ANNOTATION_EXTENSIONS_PREFIX);
		
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
			details.get(EssentialProgModelStandardSemanticsConstants.ANNOTATION_EXTENSIONS_ADAPTER_FACTORY_NAME_KEY);
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

		Bundle bundle = Binding.getBinding().getBundle();
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
	 * <i>http://org.essentialplatform/progmodel/extensions/com.mycompany.SuperDuperDomainClass</i>
	 * whose details would have a key of <i>adapterFactoryClassName</i> with
	 * a value of <i>com.mycompany.SuperDuperDomainClassFactory</i>.
	 * 
	 * 
	 * @param adapterClass
	 * @param adapter
	 */
	public <V> void setAdapterFactory(Class<V> adapterClass, IAdapterFactory<? extends V> adapterFactory) {
		EAnnotation eAnnotation = 
			_emfAnnotations.annotationOf(_eClass, EssentialProgModelStandardSemanticsConstants.ANNOTATION_EXTENSIONS_PREFIX + adapterClass.getName());
		Map<String,String> detailsPlusFactoryName = new HashMap<String,String>();
		detailsPlusFactoryName.putAll(adapterFactory.getDetails());
		detailsPlusFactoryName.put(EssentialProgModelStandardSemanticsConstants.ANNOTATION_EXTENSIONS_ADAPTER_FACTORY_NAME_KEY, adapterFactory.getClass().getName());
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
			modelElement.getEAnnotation(EssentialProgModelStandardSemanticsConstants.ANNOTATION_ELEMENT);
		if (annotation == null) {
			return null;
		}
		return (String)annotation.getDetails().get(EssentialProgModelStandardSemanticsConstants.ANNOTATION_ELEMENT_DESCRIPTION_KEY);
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
			return -1;
		}
		return -1;
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
			return -1;
		}
		return -1;
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
			return -1;
		}
		return -1;
	}
	

}
