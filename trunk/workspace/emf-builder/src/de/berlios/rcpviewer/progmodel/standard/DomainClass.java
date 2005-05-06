package de.berlios.rcpviewer.progmodel.standard;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.impl.EOperationImpl;
import org.eclipse.jface.resource.ImageDescriptor;

import de.berlios.rcpviewer.progmodel.standard.Constants;
import de.berlios.rcpviewer.metamodel.DomainClassRegistry;
import de.berlios.rcpviewer.metamodel.EmfFacade;
import de.berlios.rcpviewer.metamodel.EmfFacadeAware;
import de.berlios.rcpviewer.metamodel.IDomainClass;
import de.berlios.rcpviewer.metamodel.IDomainObject;
import de.berlios.rcpviewer.metamodel.II18nData;
import de.berlios.rcpviewer.metamodel.OperationKind;
import de.berlios.rcpviewer.metamodel.Util;
import de.berlios.rcpviewer.progmodel.ProgrammingModelException;
import de.berlios.rcpviewer.session.IWrapper;
import de.berlios.rcpviewer.session.IWrapperAware;
import de.berlios.rcpviewer.progmodel.standard.Constants;
import de.berlios.rcpviewer.progmodel.standard.impl.ValueMarker;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;

import java.util.List;


/**
 * Represents a class in the meta model, akin to {@link java.lang.Class} and
 * wrapping an underlying EMF EClass.
 * 
 * TODO: should factor much up into a Generic implementation; the standard
 * programming model should be a user of getAdapter() for Eclipse-specific things.
 *  
 * TODO: should implement the choreography of interacting with the underlying POJOs (or this could be done by DomainObject).
 * 
 * @author Dan Haywood
 */
public class DomainClass<T> 
		implements IDomainClass,
				   EmfFacadeAware,
				   IWrapperAware {
	
	public DomainClass(final Class<T> javaClass) {
		
		this.javaClass = javaClass;
		this.namingConventions = new NamingConventions();

		identifyClass();

		identifyAccessors();
		identifyMutators();
		identifyUnSettableAttributes();
		identifyOperations();
	}

	private final NamingConventions namingConventions;
	public NamingConventions getNamingConventions() {
		return namingConventions;
	}

	private final Class<T> javaClass;
	public Class<T> getJavaClass() {
		return javaClass;
	}
	
	private EClass eClass;
	public EClass getEClass() {
		return eClass;
	}

	public String getName() {
		return eClass.getName();
	}


	public String getDescription() {
		return descriptionOf(eClass);
	}

	public II18nData getI18nData() {
		throw new RuntimeException("Not yet implemented");
	}

	
	private Map<Class<?>, Object> adaptersByClass = new HashMap<Class<?>, Object>();
	/**
	 * Extension object pattern.
	 * 
	 * @param adapterClass
	 * @return
	 */
	public Object getAdapter(Class adapterClass) {
		return adaptersByClass.get(adapterClass);
	}
	/**
	 * Protected visibility so that subclass implementations can populate.
	 * 
	 * @param adapterClass
	 * @param adapter
	 */
	protected void setAdapter(Class<?> adapterClass, Object adapter) {
		if (!adapterClass.isAssignableFrom(adapter.getClass())) {
			throw new IllegalArgumentException(
				"Adapter '" + adapter + "' does not implement " +
				"adapterClass '" + adapterClass + "'");
		}
		adaptersByClass.put(adapterClass, adapter);
	}

	/**
	 * TODO: should also identify class hierarchy
	 */
	private void identifyClass() {
		Package javaPackage = javaClass.getPackage();
		this.eClass = EcoreFactory.eINSTANCE.createEClass();
		
		EPackage ePackage = EPackage.Registry.INSTANCE.getEPackage(javaPackage.getName());
		if (ePackage == null) {
			ePackage = EcoreFactory.eINSTANCE.createEPackage();
			ePackage.getEClassifiers().add(this.eClass);
			ePackage.setName(javaPackage.getName());
		}

		eClass.setInstanceClass(javaClass);

		String name = null;
		Named named = javaClass.getAnnotation(Named.class);
		name = named != null? named.value(): javaClass.getSimpleName();
		eClass.setName(name);

		addDescription(javaClass.getAnnotation(DescribedAs.class), eClass);
		
		ImageUrlAt imageUrlAt = javaClass.getAnnotation(ImageUrlAt.class);
		if (imageUrlAt != null) {
			URL imageUrl;
			try {
				imageUrl = new URL(imageUrlAt.value());
				this.setAdapter(ImageDescriptor.class, 
				        ImageDescriptor.createFromURL(imageUrl));
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block - should log.
				e.printStackTrace();
			}
		}
	}

	private void addDescription(DescribedAs describedAs, EModelElement modelElement) {
		if (describedAs == null) {
			return;
		}
		EAnnotation ea = modelElement.getEAnnotation(Constants.ANNOTATION_ELEMENT);
		if (ea == null) {
			ea = setAnnotation(modelElement, Constants.ANNOTATION_ELEMENT);
		}
		putAnnotationDetail(ea, 
			Constants.ANNOTATION_ELEMENT_DESCRIPTION_KEY, describedAs.value());
	}

	
	/**
	 * 
	 * <p>
	 * <h3>Implementation Note</h3>
	 * <p>
	 * Seems possible to add using either <tt>eClass.getEAttributes()</tt> or
	 * <tt>eClass.getEStructuralFeatures()</tt>, but latter is correct.  Note 
	 * this is not symmetric with {@link #identifyOperations()}.
	 * 
	 * @param methods
	 * @param attributesByName
	 */
	private void identifyAccessors() {
		
		Method[] methods = javaClass.getMethods();
		// search for accessors of value types
		// initially all attributes start off read-only
		for(int i=0; i<methods.length; i++) {
			final Method method = methods[i];
			if (!isAttribute(methods[i])) {
				continue;
			}
			
			EAttribute eAttribute = EcoreFactory.eINSTANCE.createEAttribute();
			Class<?> dataType = methods[i].getReturnType();
			EDataType eDataType = getEmfFacade().getEDataTypeFor(dataType);
			eAttribute.setEType(eDataType);
			String attributeName = getNamingConventions().deriveAttributeName(method);
			eAttribute.setName(attributeName);
			eClass.getEStructuralFeatures().add(eAttribute);

			putAnnotationDetail(
					methodAnnotationFor(eAttribute), 
					Constants.ANNOTATION_ATTRIBUTE_ACCESSOR_METHOD_NAME_KEY, 
					method.getName());
			
			eAttribute.setChangeable(false); // if find a mutator, make changeable
			Derived derivedAnnotation = method.getAnnotation(Derived.class);
			boolean whetherDerived = derivedAnnotation != null;
			
			eAttribute.setDerived(whetherDerived);
			eAttribute.setTransient(whetherDerived);
			eAttribute.setVolatile(whetherDerived);

			// TODO: should check that the datatype supports lower bounds
			LowerBoundOf lowerBoundOfAnnotation = 
				method.getAnnotation(LowerBoundOf.class);
			if (lowerBoundOfAnnotation != null) {
				eAttribute.setLowerBound(lowerBoundOfAnnotation.value());
			} else {
				eAttribute.setLowerBound(1);
			}

			// TODO: should check that the datatype supports upper bounds
			UpperBoundOf upperBoundOfAnnotation = 
				method.getAnnotation(UpperBoundOf.class);
			if (upperBoundOfAnnotation != null) {
				int upperBound = upperBoundOfAnnotation.value();
				eAttribute.setUpperBound(upperBound);
				if (upperBound > 1) {
					Unique uniqueAnnotation = 
						method.getAnnotation(Unique.class);
					if (uniqueAnnotation != null) {
						eAttribute.setUnique(uniqueAnnotation.value());
					}
					Ordered orderedAnnotation = 
						method.getAnnotation(Ordered.class);
					if (orderedAnnotation != null) {
						eAttribute.setOrdered(orderedAnnotation.value());
					}
				}
			}
		}
	}
	


	/**
	 * searches for mutators of value types; either update existing attribute
	 * (ie read/write) or create new (write-only) 
     *
	 * <p>
	 * <h3>Implementation Note</h3>
	 * <p>
	 * Seems possible to add using either <tt>eClass.getEAttributes()</tt> or
	 * <tt>eClass.getEStructuralFeatures()</tt>, but latter is correct.  Note 
	 * this is not symmetric with {@link #identifyOperations()}.
	 * 
	 */
	private void identifyMutators() {

		Method[] methods = javaClass.getMethods();

		for(int i=0; i<methods.length; i++) {
			final Method method = methods[i];
			if (!getNamingConventions().isMutator(method)) {
				continue;
			}

			String attributeName = getNamingConventions().deriveAttributeName(method);
			EAttribute eAttribute = getEAttributeNamed(attributeName);

			if (eAttribute != null) {
				eAttribute.setChangeable(true);
			} else {
				eAttribute = EcoreFactory.eINSTANCE.createEAttribute();
				Class<?> dataType = methods[i].getParameterTypes()[0];
				EDataType eDataType = getEmfFacade().getEDataTypeFor(dataType);
				eAttribute.setEType(eDataType);
				eAttribute.setName(attributeName);

				eClass.getEStructuralFeatures().add(eAttribute);

				setAnnotation(eAttribute, de.berlios.rcpviewer.progmodel.standard.Constants.ANNOTATION_ATTRIBUTE_WRITE_ONLY);
			}
			
			putAnnotationDetail(
					methodAnnotationFor(eAttribute), 
					Constants.ANNOTATION_ATTRIBUTE_MUTATOR_METHOD_NAME_KEY, 
					methods[i].getName());
			
//			eAttribute.setDefaultValueLiteral(defaultValueAsString); // TODO: read from annotation
		}
	}
	

	/**
	 *  
	 */
	private void identifyUnSettableAttributes() {
	
		Method[] methods = javaClass.getMethods();
		Method isUnsetMethod = null;
		Method unsetMethod = null;
		for(EAttribute eAttribute: attributes()) {
			for(int i=0; i<methods.length; i++) {
				if (getNamingConventions().isIsUnsetMethodFor(methods[i], eAttribute)) {
					isUnsetMethod = methods[i];
					break;
				}
			}
			if (isUnsetMethod == null) {
				continue;
			}
			for(int i=0; i<methods.length; i++) {
				if (getNamingConventions().isUnsetMethodFor(methods[i], eAttribute)) {
					unsetMethod = methods[i];
					break;
				}
			}
			if (unsetMethod == null) {
				continue;
			}
			// has both an IsUnset and an unset method for this attribute
			eAttribute.setUnsettable(true);
			
			putAnnotationDetail(
					methodAnnotationFor(eAttribute), 
					Constants.ANNOTATION_ATTRIBUTE_IS_UNSET_METHOD_NAME_KEY, 
					isUnsetMethod.getName());
			
			putAnnotationDetail(
					methodAnnotationFor(eAttribute), 
					Constants.ANNOTATION_ATTRIBUTE_UNSET_METHOD_NAME_KEY, 
					isUnsetMethod.getName());
		}
	}
	
	private boolean isAttribute(Method method) {
		return (getNamingConventions().isAccessor(method) || 
				getNamingConventions().isMutator(method)) &&
				getNamingConventions().isValueType(method.getReturnType());
	}

	/**
	 * indicates whether supplied accessor and mutator methods are compatible, 
	 * that is, that they have the same type and the same name.
	 *
	 * <p>
	 * TODO: not actually used, but should be overloaded to also check for
	 * unset and isUnset methods.  
	 *
	 * @param accessor
	 * @param mutator
	 * @return
	 */
	private final boolean areAttributeMethodsCompatible(final Method accessor, final Method mutator) {
		getNamingConventions().assertAccessor(accessor);  // TODO: precondition aspect
		getNamingConventions().assertMutator(mutator);  // TODO: precondition aspect
		// check return type of accessor to the type 
		// of the first parameter of the mutator
		if (!accessor.getReturnType().equals(mutator.getParameterTypes()[0])) {
			return false;
		}
		String accessorName = getNamingConventions().deriveAttributeName(accessor);
		String mutatorName = getNamingConventions().deriveAttributeName(mutator);
		if (!accessorName.equals(mutatorName)) {
			return false;
		}
		return true;
	}

	public int getNumberOfAttributes() {
		return getEClass().getEAllAttributes().size();
	}

	public List<EAttribute> attributes() {
		return attributes(true);
	}

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
		return eAttribute.getEAnnotation(de.berlios.rcpviewer.progmodel.standard.Constants.ANNOTATION_ATTRIBUTE_WRITE_ONLY) != null;
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

	public boolean containsAttribute(EAttribute eAttribute) {
		return this.eClass.getEAllAttributes().contains(eAttribute);
	}

	public II18nData getI18nDataFor(EAttribute attribute) {
		throw new RuntimeException("Not yet implemented");
	}


	// ATTRIBUTE SUPPORT: END

	// OPERATION SUPPORT: START

	/**
	 * TODO
	 * 
	 * <p>
	 * <h3>Implementation Note</h3>
	 * <p>
	 * Unlike attributes (see {@link #identifyAccessors()}, should add 
	 * operations using <tt>eClass.getEOperations()</tt>.
	 * 
	 * <p>
	 * TODO: EMF support lower and upper bounds, also ordered and unique?  
	 * not yet exposing them (what would they mean?)
	 */
	private void identifyOperations() {
		Method[] methods = javaClass.getMethods();

		eachMethod: 
		for(int i=0; i<methods.length; i++) {
			final Method method = methods[i];
			if ((method.getModifiers() & Method.PUBLIC) != Method.PUBLIC) {
				continue;
			}
			if (method.getAnnotation(Programmatic.class) != null) {
				continue;
			}
			if (getNamingConventions().isReserved(method)) {
				continue;
			}
			Class<?> returnType = method.getReturnType();
			if (!getNamingConventions().isValueType(returnType) &&
				!getNamingConventions().isReferenceType(returnType) &&
				!getNamingConventions().isVoid(returnType)) {
				continue;
			}
			
			Class<?>[] parameterTypes = method.getParameterTypes();
			Annotation[][] parameterAnnotationSets = method.getParameterAnnotations();
			for(Class<?> parameterType: parameterTypes) {
				if (!getNamingConventions().isValueType(parameterType) &&
					!getNamingConventions().isReferenceType(parameterType)) {
					continue eachMethod;
				}
			}
			
			EOperation eOperation = EcoreFactory.eINSTANCE.createEOperation();
			eOperation.setName(method.getName());
			Map<Class<?>, int[]> unnamedParameterIndexByType = new HashMap<Class<?>, int[]>();
			for(int j=0; j<parameterTypes.length; j++) {
				Class<?> parameterType = parameterTypes[j];
				Annotation[] parameterAnnotationSet = parameterAnnotationSets[j];
				EDataType dataType = getEmfFacade().getEDataTypeFor(parameterType);
				EParameter eParameter = EcoreFactory.eINSTANCE.createEParameter();
				eOperation.getEParameters().add(eParameter);
				eParameter.setEType(dataType);
				
				// parameter name
				Named named = null;				
				for(Annotation parameterAnnotation: parameterAnnotationSet) {
					if (parameterAnnotation instanceof Named) {
						named = (Named)parameterAnnotation;
						break;
					}
				}
				String parameterName = null;
				if (named != null) {
					parameterName = named.value();
				} else {
					int[] parameterIndex = unnamedParameterIndexByType.get(parameterType);
					if (parameterIndex == null) {
						parameterIndex = new int[] {-1};
						unnamedParameterIndexByType.put(parameterType, parameterIndex);
					}
					parameterIndex[0]++;
					parameterName = Util.camelCase(parameterType.getSimpleName());
					if (parameterIndex[0] > 0) {
						parameterName += parameterIndex[0];
					}
				}
				eParameter.setName(parameterName);
				
				// description
				DescribedAs describedAs = null;
				for(Annotation parameterAnnotation: parameterAnnotationSet) {
					if (parameterAnnotation instanceof DescribedAs) {
						describedAs = (DescribedAs)parameterAnnotation;
						break;
					}
				}
				if (describedAs != null) {
					addDescription(describedAs, eParameter);
				}
				
			}
			//addDescription(javaClass.getAnnotation(DescribedAs.class), eClass);

			Class<?> dataType = methods[i].getReturnType();
			// EMF does not have a built-in classifier for Void
			if (!getNamingConventions().isVoid(dataType)) { // HACK
				EDataType eDataType = getEmfFacade().getEDataTypeFor(dataType);
				eOperation.setEType(eDataType);
			}

			if ((methods[i].getModifiers() & Modifier.STATIC) == Modifier.STATIC) {
				setAnnotation(eOperation, Constants.ANNOTATION_OPERATION_STATIC);
			}
			
			
			eClass.getEOperations().add(eOperation);

			// these are supported by EMF, but not (yet) by our metamodel.
//			eOperation.setLowerBound(..);
//			eOperation.setUpperBound(..);
//			eOperation.setOrdered(..);
//			eOperation.setUnique(..);
//			eOperation.setOrdered(..);

		
			
			putAnnotationDetail(
					methodAnnotationFor(eOperation), 
					Constants.ANNOTATION_OPERATION_METHOD_NAME_KEY, 
					methods[i].getName());
			
		}
	}

	public List<EOperation> operations() {
		return operations(OperationKind.ALL, true);
	}

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
		return eOperation.getEAnnotation(Constants.ANNOTATION_OPERATION_STATIC) != null;
	}

	public boolean isParameterAValue(EOperation operation, int parameterPosition) {
		EParameter parameter = (EParameter)operation.getEParameters().get(parameterPosition);
		return parameter.getEType() instanceof EDataType;
	}

	public EDataType getEDataTypeFor(EOperation operation, int parameterPosition) {
		if (!isParameterAValue(operation, parameterPosition)) {
			throw new IllegalArgumentException("Parameter does not represent a value.");
		}
		EParameter parameter = (EParameter)operation.getEParameters().get(parameterPosition);
		return (EDataType)parameter.getEType();
	}

	public boolean isParameterAReference(EOperation operation, int parameterPosition) {
		EParameter parameter = (EParameter)operation.getEParameters().get(parameterPosition);
		return parameter.getEType() instanceof EClass;
	}

	public IDomainClass getDomainClassFor(EOperation operation, int parameterPosition) {
		if (!isParameterAReference(operation, parameterPosition)) {
			throw new IllegalArgumentException("Parameter does not represent a reference.");
		}
		EParameter parameter = (EParameter)operation.getEParameters().get(parameterPosition);
		EClass eClass = (EClass)parameter.getEType();
		return DomainClassRegistry.instance().domainClassFor(eClass);
	}

	public String getNameFor(EOperation operation, int parameterPosition) {
		EParameter parameter = (EParameter)operation.getEParameters().get(parameterPosition);
		return parameter.getName();
	}
	
	public String getDescriptionFor(EOperation operation, int parameterPosition) {
		EParameter parameter = (EParameter)operation.getEParameters().get(parameterPosition);
		return descriptionOf(parameter);
	}

	public II18nData getI18nDataFor(EOperation operation) {
		throw new RuntimeException("Not yet implemented");
	}

	public II18nData getI18nDataFor(EOperation operation, int parameterPosition) {
		throw new RuntimeException("Not yet implemented");
	}



	// OPERATION SUPPORT: END


	// LINKS SUPPORT: START

	/**
	 * Invoked by {@link DomainClassRegistry} when it is informed that all 
	 * classes have been registered (@link DomainClassRegistry#done()}.
	 * 
	 * TODO.
	 */
	void identifyLinks() {

		Method[] methods = javaClass.getMethods();
		for(int i=0; i<methods.length; i++) {
			
//			if (!getProgrammingModel().isLink(methods[i])) // simple or composite.
//				continue;
			
			// TODO: resolveProxies

		}
	}
	


	/**
	 * indicates whether supplied associator and dissociator are compatible,
	 * that is, that they have the same type and the same name. 
	 * @param associator
	 * @param dissociator
	 * @return
	 */
	private final boolean isLinkPairCompatible(final Method associator, final Method dissociator) {
		getNamingConventions().assertAssociator(associator); // TODO: precondition aspect
		getNamingConventions().assertDissociator(dissociator);  // TODO: precondition aspect
		if (!linkType(associator).equals(linkType(dissociator))) {
			return false;
		}
		String deriveLinkNameForAssociator = getNamingConventions().deriveLinkName(associator);
		String deriveLinkNameForDissociator = getNamingConventions().deriveLinkName(dissociator);
		if (!deriveLinkNameForAssociator.equals(deriveLinkNameForDissociator)) {
			return false;
		}
		return true;
	}



	/**
	 * TODO: this isn't used an needs to change - the type is instead picked
	 * up from an annotation.
	 * 
	 * @param associator or dissociator method
	 * 
	 * @return The type of the associator or dissociator (the type of its first argument).
	 */
	private final Class linkType(final Method associatorOrDissociator) {
		if (associatorOrDissociator == null) {
			throw new AssertionError("null associator/dissociator method");
		}
		if (!getNamingConventions().isAssociator(associatorOrDissociator) &&
			!getNamingConventions().isDissociator(associatorOrDissociator)  ) {
			throw new AssertionError("not an associator/dissociator method");
		}
		return associatorOrDissociator.getParameterTypes()[0];
	}

	

	// LINKS SUPPORT: END

	// ANNOTATION SUPPORT: START


	/**
	 * Since descriptions are stored as annotations and apply to many model
	 * elements, this is a convenient way of getting to the description.
	 * 
	 * @param modelElement
	 * @return
	 */
	private String descriptionOf(EModelElement modelElement) {
		EAnnotation annotation = 
			modelElement.getEAnnotation(Constants.ANNOTATION_ELEMENT);
		if (annotation == null) {
			return null;
		}
		return (String)annotation.getDetails().get(Constants.ANNOTATION_ELEMENT_DESCRIPTION_KEY);
	}

	EAnnotation methodAnnotationFor(EModelElement eModelElement) {
		EAnnotation eAnnotation = 
			eModelElement.getEAnnotation(Constants.ANNOTATION_SOURCE_METHOD_NAMES);
		if (eAnnotation != null) {
			return eAnnotation;
		}
		return setAnnotation(eModelElement, Constants.ANNOTATION_SOURCE_METHOD_NAMES);
	}
	
	EAnnotation putAnnotationDetail(EAnnotation eAnnotation, String key, String value) {
		if (eAnnotation == null) {
			return null;
		}
		eAnnotation.getDetails().put(key, value);
		return eAnnotation;
	}
	
	String getAnnotationDetail(EAnnotation eAnnotation, String key) {
		if (eAnnotation == null) {
			return null;
		}
		return (String)eAnnotation.getDetails().get(key);
	}
	
	private EAnnotation setAnnotation(EModelElement eModelElement, String annotationSource) {
		EAnnotation eAnnotation = EcoreFactory.eINSTANCE.createEAnnotation();
		eAnnotation.setSource(annotationSource);
		eAnnotation.setEModelElement(eModelElement);
		return eAnnotation;
	}

	// ANNOTATION SUPPORT: END
	
	// DOMAIN OBJECT SUPPORT: START

	public IDomainObject createTransient() {
		try {
			Object pojo = getJavaClass().newInstance();
			IDomainObject domainObject = getWrapper().wrapped(pojo);
			return domainObject;
		} catch(IllegalAccessException ex) {
			throw new ProgrammingModelException("Cannot instantiate", ex);
		} catch(InstantiationException ex) {
			throw new ProgrammingModelException("Cannot instantiate", ex);
		}
	}

	// DOMAIN OBJECT SUPPORT: END

	
	/**
	 * Setting up for AspectJ introduction of logging infrastructure.
	 * @param message
	 */
	private void logWarning(String message) {}

	public String toString() { return "DomainClass.javaClass = " + javaClass ; }

	// DEPENDENCY INJECTION
	
	private EmfFacade emfFacade;
	public EmfFacade getEmfFacade() {
		return emfFacade;
	}
	public void setEmfFacade(EmfFacade emfFacade) {
		this.emfFacade = emfFacade;
	}

	private IWrapper wrapper;
	public IWrapper getWrapper() {
		return wrapper;
	}
	public void setWrapper(IWrapper wrapper) {
		this.wrapper = wrapper;
	}

	// DEPENDENCY INJECTION END

	
	/**
	 * Cross-cutting validation of any eAttributes or eOperations 
	 * passed to us in the various convenience methods.
	 * 
	 * <p>
	 * Don't worry about the red squigglies: AJDT has not yet taught the
	 * Java editor about inner aspects.
	 */
	private static aspect EnsureMemberBelongsToDomainClassAspect {

		pointcut convenienceMethodForAttribute(DomainClass domainClass, EAttribute eAttribute):
			execution(* *..DomainClass+.*(EAttribute, ..)) &&
			!execution(* *..DomainClass+.containsAttribute(EAttribute)) &&
			this(domainClass) && args(eAttribute, ..);
		
		before(DomainClass domainClass, EAttribute eAttribute): 
			convenienceMethodForAttribute(domainClass, eAttribute) {
			if (domainClass.containsAttribute(eAttribute)) {
				return;
			}
			throw new IllegalArgumentException(
				"EAttribute '" + eAttribute + "' not part of this DomainClass");
		}

		pointcut convenienceMethodForOperation(DomainClass domainClass, EOperation eOperation):
			execution(* *..DomainClass+.*(EOperation, ..)) &&
			!execution(* *..DomainClass+.containsOperation(EOperation)) &&
			this(domainClass) && args(eOperation, ..);
		
		before(DomainClass domainClass, EOperation eOperation): 
			convenienceMethodForOperation(domainClass, eOperation) {
			if (domainClass.containsOperation(eOperation)) {
				return;
			}
			throw new IllegalArgumentException(
				"EOperation '" + eOperation + "' not part of this DomainClass");
		}

	}

	/**
	 * Cross-cutting validation of any convenience methods on operations that
	 * take a second int (being the parameter position). 
	 * 
	 * <p>
	 * Don't worry about the red squigglies: AJDT has not yet taught the
	 * Java editor about inner aspects.
	 */
	private static aspect EnsureParameterPositionIsInRangeAspect {

		declare precedence: EnsureMemberBelongsToDomainClassAspect, 
							EnsureParameterPositionIsInRangeAspect;
	
		pointcut convenienceMethodForParameter(EOperation eOperation, int parameterPosition):
			execution(* *..DomainClass+.*(EOperation, int)) &&
			args(eOperation, parameterPosition);
		
		before(EOperation eOperation, int parameterPosition): 
			convenienceMethodForParameter(eOperation, parameterPosition) {
			int numberOfParametersForOperation = eOperation.getEParameters().size();  
			if (numberOfParametersForOperation == 0) {
				throw new IllegalArgumentException(
					"EOperation '" + eOperation + "' takes no parameters");
			}
			if (parameterPosition < 0 ||
			    parameterPosition >= numberOfParametersForOperation) {
				throw new IllegalArgumentException(
					"EOperation '" + eOperation + "': 0 <= parameterPosition < " + numberOfParametersForOperation);
			}
		}
	}


}
