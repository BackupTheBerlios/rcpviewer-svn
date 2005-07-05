package de.berlios.rcpviewer.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.EReference;

import de.berlios.rcpviewer.CorePlugin;
import de.berlios.rcpviewer.progmodel.standard.NamingConventions;
import de.berlios.rcpviewer.progmodel.standard.StandardProgModelConstants;

public abstract class AbstractDomainClass<T> 
	implements IDomainClass<T>, EmfFacadeAware {

	public AbstractDomainClass(final IDomain domain, final NamingConventions namingConventions) {
		this.domain = domain;
		this.namingConventions = namingConventions;
	}

	protected final IDomain domain;

	protected final NamingConventions namingConventions;
	public NamingConventions getNamingConventions() {
		return namingConventions;
	}

	/**
	 * The domain to which this DomainClass belongs.
	 * 
	 * <p>
	 * Under the standard programming model, this is default using the @InDomain
	 * annotation.
	 * 
	 * @return
	 */
	public IDomain getDomain() {
		return domain;
	}

	protected EClass eClass;
	public EClass getEClass() {
		return eClass;
	}

	public abstract String getName();
	
	public String getEClassName() {
		return eClass.getName();
	}

	public String getDescription() {
		return descriptionOf(eClass);
	}
	
	public boolean isChangeable() {
		EAnnotation annotation = 
			eClass.getEAnnotation(StandardProgModelConstants.ANNOTATION_ELEMENT);
		if (annotation == null) {
			return false;
		}
		String immutable = 
			(String)annotation.getDetails().get(StandardProgModelConstants.ANNOTATION_ELEMENT_IMMUTABLE_KEY);
		return "false".equals(immutable);
	}

	public II18nData getI18nData() {
		throw new RuntimeException("Not yet implemented");
	}

	public EmfFacade emfFacade = new EmfFacade();

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
			emfFacade.annotationsPrefixed(eClass, StandardProgModelConstants.ANNOTATION_EXTENSIONS_PREFIX);
		
		List<IDomainClassAdapter> adapters = new ArrayList<IDomainClassAdapter>();
		for(EAnnotation annotation: annotations) {
			adapters.add( (IDomainClassAdapter)getAdapter(annotation.getSource()) );
		}
		
		return adapters;
	}



	/**
	 * Built up lazily whenever {@link #getAdapter(Class)} is called.
	 */
	private Map<String, Object> adaptersByAnnotationSource = new HashMap<String, Object>();
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
		
		Map<String, String> detailsPlusFactoryName = 
			emfFacade.getAnnotationDetails(eClass, annotationSource);
		String adapterFactoryName = 
			detailsPlusFactoryName.get(StandardProgModelConstants.ANNOTATION_EXTENSIONS_ADAPTER_FACTORY_NAME_KEY);
		if (adapterFactoryName == null) {
			throw new IllegalArgumentException("No such adapter '" + adapterFactoryName + "'");
		}
		IAdapterFactory<V> adapterFactory;
		try {
			adapterFactory = (IAdapterFactory<V>)loadClass(adapterFactoryName).newInstance(); 
			V adapter = adapterFactory.createAdapter(this, detailsPlusFactoryName);
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
	
	protected Class<IAdapterFactory> loadClass(String adapterFactoryName) throws ClassNotFoundException  {
		return (Class<IAdapterFactory>)Class.forName(adapterFactoryName);
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
			emfFacade.annotationOf(eClass, StandardProgModelConstants.ANNOTATION_EXTENSIONS_PREFIX + adapterClass.getName());
		Map<String,String> detailsPlusFactoryName = new HashMap<String,String>();
		detailsPlusFactoryName.putAll(adapterFactory.getDetails());
		detailsPlusFactoryName.put(StandardProgModelConstants.ANNOTATION_EXTENSIONS_ADAPTER_FACTORY_NAME_KEY, adapterFactory.getClass().getName());
		emfFacade.putAnnotationDetails(eAnnotation, detailsPlusFactoryName);
	}

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

	public boolean isWriteOnly(EAttribute eAttribute) {
		return eAttribute.getEAnnotation(StandardProgModelConstants.ANNOTATION_ATTRIBUTE_WRITE_ONLY) != null;
	}

	public boolean isChangeable(EAttribute eAttribute) {
		return eAttribute.isChangeable();
	}

	public boolean isDerived(EAttribute eAttribute) {
		return eAttribute.isDerived();
	}

	public int getLowerBound(EAttribute eAttribute) {
		return eAttribute.getLowerBound();
	}

	public int getUpperBound(EAttribute eAttribute) {
		return eAttribute.getUpperBound();
	}

	public boolean isRequired(EAttribute eAttribute) {
		return eAttribute.isRequired();
	}

	public boolean isMany(EAttribute eAttribute) {
		return eAttribute.isMany();
	}

	public boolean isUnique(EAttribute eAttribute) {
		return eAttribute.isUnique();
	}

	public boolean isOrdered(EAttribute eAttribute) {
		return eAttribute.isOrdered();
	}

	public boolean isUnsettable(EAttribute eAttribute) {
		return eAttribute.isUnsettable();
	}

	public II18nData getI18nDataFor(EAttribute attribute) {
		throw new RuntimeException("Not yet implemented");
	}

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
			switch(operationKind) {
				case INSTANCE:
					if (!isStatic(eOperation)) {
						eOperations.add(eOperation);
					}
					break;
				case STATIC:
					if (isStatic(eOperation)) {
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

	public boolean containsOperation(EOperation eOperation) {
		return this.eClass.getEAllOperations().contains(eOperation);
	}

	public boolean isStatic(EOperation eOperation) {
		return eOperation.getEAnnotation(StandardProgModelConstants.ANNOTATION_OPERATION_STATIC) != null;
	}

	public EDataType getEDataTypeFor(EOperation operation, int parameterPosition) {
		if (!isParameterAValue(operation, parameterPosition)) {
			throw new IllegalArgumentException("Parameter does not represent a value.");
		}
		EParameter parameter = (EParameter)operation.getEParameters().get(parameterPosition);
		return (EDataType)parameter.getEType();
	}

	public IDomainClass getDomainClassFor(EOperation operation, int parameterPosition) {
		if (!isParameterADomainObject(operation, parameterPosition)) {
			throw new IllegalArgumentException("Parameter does not represent a reference.");
		}
		EParameter parameter = (EParameter)operation.getEParameters().get(parameterPosition);
		EClass eClass = (EClass)parameter.getEType();
		return domain.domainClassFor(eClass);
	}

	public String getNameFor(EOperation operation, int parameterPosition) {
		EParameter parameter = (EParameter)operation.getEParameters().get(parameterPosition);
		return parameter.getName();
	}

	public String getDescriptionFor(EOperation operation, int parameterPosition) {
		EParameter parameter = (EParameter)operation.getEParameters().get(parameterPosition);
		return descriptionOf(parameter);
	}

	/**
	 * Since descriptions are stored as annotations and apply to many model
	 * elements, this is a convenient way of getting to the description.
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


	public II18nData getI18nDataFor(EOperation operation) {
		throw new RuntimeException("Not yet implemented");
	}

	public II18nData getI18nDataFor(EOperation operation, int parameterPosition) {
		throw new RuntimeException("Not yet implemented");
	}

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
		return this.eClass.getEAllReferences().contains(eReference);
	}

	public <V> IDomainClass<V> getReferencedClass(EReference eReference) {
		EClass eClass = (EClass)eReference.getEReferenceType();
		return domain.lookupNoRegister(((Class<V>)eClass.getInstanceClass()));
		
	}

	public boolean isMultiple(EReference eReference) {
		return eReference.isMany();
	}

	public boolean isOrdered(EReference eReference) {
		return eReference.isOrdered();
	}

	public boolean isContainer(EReference eReference) {
		return eReference.isContainer();
	}

	public boolean isUnique(EReference eReference) {
		return eReference.isUnique();
	}

	public boolean isChangeable(EReference eReference) {
		return eReference.isChangeable();
	}

	public boolean isDerived(EReference eReference) {
		return eReference.isDerived();
	}

	protected EAnnotation referenceAnnotationFor(EModelElement eModelElement) {
		return emfFacade.annotationFor(eModelElement, StandardProgModelConstants.ANNOTATION_REFERENCE_OPPOSITE);
	}

	public EmfFacade getEmfFacade() {
		return emfFacade;
	}

	public void setEmfFacade(EmfFacade emfFacade) {
		this.emfFacade = emfFacade;
	}

}
