package de.berlios.rcpviewer.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.EReference;

import de.berlios.rcpviewer.domain.IDomainClass.IReference;
import de.berlios.rcpviewer.progmodel.java.JavaProgModelRules;
import de.berlios.rcpviewer.progmodel.standard.FeatureId;
import de.berlios.rcpviewer.progmodel.standard.IFeatureId;
import de.berlios.rcpviewer.progmodel.standard.StandardProgModelRules;
import de.berlios.rcpviewer.progmodel.standard.StandardProgModelConstants;

public abstract class AbstractDomainClass<T> 
	implements IDomainClass<T> {

	private abstract class Member implements IDomainClass.IMember {
		public IDomainClass getDomainClass() {
			return AbstractDomainClass.this; 
		}
	}
	
	private WeakHashMap<EAttribute, IAttribute> _attributesByEAttribute = new WeakHashMap<EAttribute, IAttribute>();
	private WeakHashMap<EReference, IReference> _referencesByEReference = new WeakHashMap<EReference, IReference>();
	private WeakHashMap<EOperation, IOperation> _operationsByEOperation = new WeakHashMap<EOperation, IOperation>();

	protected final IDomain _domain;
	protected final EClass _eClass;
	
	/**
	 * Built up lazily whenever {@link #getAdapter(Class)} is called.
	 */
	private Map<String, Object> adaptersByAnnotationSource = new HashMap<String, Object>();

	/**
	 * Required since annotations are used to store adapter class names.
	 * 
	 * @see #setAdapterFactory(Class, IAdapterFactory)
	 * @see #getAdapter(Class)
	 * @see #getAdapters()
	 */
	private EmfAnnotations _emfAnnotations = new EmfAnnotations();
	
	public AbstractDomainClass(final IDomain domain, final EClass eClass) {
		this._domain = domain;
		this._eClass = eClass;
	}


	/*
	 * @see de.berlios.rcpviewer.domain.IDomainClass#getDomain()
	 */
	public IDomain getDomain() {
		return _domain;
	}

	/*
	 * @see de.berlios.rcpviewer.domain.IDomainClass#getName()
	 */
	public abstract String getName();

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
	 * @see de.berlios.rcpviewer.domain.IDomainClass#getI18nData()
	 */
	public II18nData getI18nData() {
		throw new RuntimeException("Not yet implemented");
	}



	///////////////////////////////////////////////////////////////
	// attributes
	
	public int getNumberOfAttributes() {
		return getEClass().getEAllAttributes().size();
	}

	/**
	 * Returns all the attributes of the class, including inherited attributes.
	 * 
	 * <p>
	 * The returned list is a copy and so may safely be modified by the caller
	 * with no side-effects.
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

	public EAttribute getEAttributeNamed(String attributeName) {
		for(EAttribute eAttribute: attributes() ) {
			if (eAttribute.getName().equals(attributeName)) {
				return eAttribute;
			}
		}
		return null;
	}

	public synchronized IAttribute getAttribute(final EAttribute eAttribute) {
		IAttribute attribute = _attributesByEAttribute.get(eAttribute);
		if (attribute == null) {
			attribute = new Attribute(eAttribute);
			_attributesByEAttribute.put(eAttribute, attribute);
		}
		return attribute;
	}


	private final class Attribute extends Member implements IDomainClass.IAttribute {
		private final EAttribute _attribute;
		public Attribute(EAttribute eAttribute) {
			_attribute = eAttribute;
		}
		public EAttribute getEAttribute() {
			return _attribute;
		}
		public boolean isWriteOnly() {
			return _attribute.getEAnnotation(StandardProgModelConstants.ANNOTATION_ATTRIBUTE_WRITE_ONLY) != null;
		}
		public boolean isChangeable() {
			return _attribute.isChangeable();
		}
		public boolean isDerived() {
			return _attribute.isDerived();
		}
		public int getLowerBound() {
			return _attribute.getLowerBound();
		}
		public int getUpperBound() {
			return _attribute.getUpperBound();
		}
		public boolean isRequired() {
			return _attribute.isRequired();
		}
		public boolean isMany() {
			return _attribute.isMany();
		}
		public boolean isUnique() {
			return _attribute.isUnique();
		}
		public boolean isOrdered() {
			return _attribute.isOrdered();
		}
		public boolean isUnsettable() {
			return _attribute.isUnsettable();
		}
		public II18nData getI18nDataFor() {
			throw new RuntimeException("Not yet implemented");
		}

		/*
		 * @see de.berlios.rcpviewer.domain.IDomainClass#attributeIdFor(org.eclipse.emf.ecore.EAttribute)
		 */
		public IFeatureId attributeIdFor() {
			return FeatureId.create(_attribute.getName(), getDomainClass(), IFeatureId.Type.ATTRIBUTE); 
		}

	}

	///////////////////////////////////////////////////////////////
	// references 
	

	/**
	 * Returns references from this class to other classes, including those 
	 * inherited.
	 */
	public List<EReference> references() {
		return references(true);
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
				reference = new CollectionReference(eReference);
			} else {
				reference = new OneToOneReference(eReference);
			}
			_referencesByEReference.put(eReference, reference);
		}
		return reference;
	}

	private abstract class Reference extends Member implements IDomainClass.IReference {
		private final EReference _reference;
		Reference(EReference reference) {
			_reference = reference;
		}
		public EReference getEReference() {
			return _reference;
		}

		public <V> IDomainClass<V> getReferencedClass() {
			EClass eClass = (EClass)_reference.getEReferenceType();
			return _domain.lookupNoRegister(((Class<V>)eClass.getInstanceClass()));
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

	/**
	 * Returns all the operations (both static and instance) of the class, 
	 * including inherited operations.
	 * 
	 * <p>
	 * The returned list is a copy and so may safely be modified by the caller
	 * with no side-effects.
	 */
	public List<EOperation> operations() {
		return operations(OperationKind.ALL, true);
	}

	/**
	 * Returns all the attributes of the class, of the specified
	 * {@link OperationKind}, and including inherited operations only if 
	 * requested.
	 * 
	 * <p>
	 * The returned list is a copy and so may safely be modified by the caller
	 * with no side-effects.
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
			operation = new Operation(eOperation);
			_operationsByEOperation.put(eOperation, operation);
		}
		return operation;
	}

	public boolean containsOperation(EOperation eOperation) {
		return this._eClass.getEAllOperations().contains(eOperation);
	}

	private final class Operation extends Member implements IDomainClass.IOperation {
		private final EOperation _operation;
		public Operation(EOperation operation) {
			_operation = operation;
		}
		public EOperation getEOperation() {
			return _operation;
		}
		
		public boolean isStatic() {
			return _operation.getEAnnotation(StandardProgModelConstants.ANNOTATION_OPERATION_STATIC) != null;
		}
		public EDataType getEDataTypeFor(int parameterPosition) {
			if (!isParameterAValue(parameterPosition)) {
				throw new IllegalArgumentException("Parameter does not represent a value.");
			}
			EParameter parameter = (EParameter)_operation.getEParameters().get(parameterPosition);
			return (EDataType)parameter.getEType();
		}
	
		public II18nData getI18nDataFor() {
			throw new RuntimeException("Not yet implemented");
		}

		public IDomainClass getDomainClassFor(int parameterPosition) {
			if (!isParameterADomainObject(parameterPosition)) {
				throw new IllegalArgumentException("Parameter does not represent a reference.");
			}
			EParameter parameter = (EParameter)_operation.getEParameters().get(parameterPosition);
			EClass eClass = (EClass)parameter.getEType();
			return _domain.domainClassFor(eClass);
		}
	
		public String getNameFor(int parameterPosition) {
			EParameter parameter = (EParameter)_operation.getEParameters().get(parameterPosition);
			return parameter.getName();
		}
		public String getDescriptionFor(int parameterPosition) {
			EParameter parameter = (EParameter)_operation.getEParameters().get(parameterPosition);
			return descriptionOf(parameter);
		}
		/*
		 * @see de.berlios.rcpviewer.domain.IDomainClass#isParameterAValue(org.eclipse.emf.ecore.EOperation, int)
		 */
		public boolean isParameterAValue(int parameterPosition) {
			EParameter parameter = (EParameter)_operation.getEParameters().get(parameterPosition);
			return parameter.getEType() instanceof EDataType;
		}

		/*
		 * @see de.berlios.rcpviewer.domain.IDomainClass#isParameterADomainObject(org.eclipse.emf.ecore.EOperation, int)
		 */
		public boolean isParameterADomainObject(int parameterPosition) {
			EParameter parameter = (EParameter)_operation.getEParameters().get(parameterPosition);
			return parameter.getEType() instanceof EClass;
		}

		public II18nData getI18nDataFor(int parameterPosition) {
			throw new RuntimeException("Not yet implemented");
		}

		/*
		 * @see de.berlios.rcpviewer.domain.IDomainClass#operationIdFor(org.eclipse.emf.ecore.EOperation)
		 */
		public IFeatureId operationIdFor() {
			return FeatureId.create(_operation.getName(), getDomainClass(), IFeatureId.Type.OPERATION);
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
	
	protected abstract Class<IAdapterFactory> loadClass(String adapterFactoryName) throws ClassNotFoundException ;

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


}
