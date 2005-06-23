package de.berlios.rcpviewer.progmodel.standard;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.impl.EReferenceImpl;

import de.berlios.rcpviewer.domain.EmfFacade;
import de.berlios.rcpviewer.domain.EmfFacadeAware;
import de.berlios.rcpviewer.domain.IAdapterFactory;
import de.berlios.rcpviewer.domain.IDomain;
import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.domain.II18nData;
import de.berlios.rcpviewer.domain.IRuntimeDomainClass;
import de.berlios.rcpviewer.domain.LinkSemanticsType;
import de.berlios.rcpviewer.domain.MethodNameHelper;
import de.berlios.rcpviewer.domain.OperationKind;
import de.berlios.rcpviewer.progmodel.ProgrammingModelException;
import de.berlios.rcpviewer.session.IDomainObject;


/**
 * Represents a class in the meta model, akin to {@link java.lang.Class} and
 * wrapping an underlying EMF EClass.
 * 
 * <p>
 * TODO: should factor much up into a Generic implementation; the standard
 * programming model should be a user of getAdapter() for Eclipse-specific 
 * things.
 *  
 * <p>
 * TODO: should implement the choreography of interacting with the underlying 
 * POJOs (or this could be done by DomainObject).
 * 
 * <p>
 * TODO: need to sort out generic parameterization. 
 * 
 * @author Dan Haywood
 */
public class DomainClass<T> 
		implements IRuntimeDomainClass<T>,
				   EmfFacadeAware {
	
	public DomainClass(final IDomain domain, final Class<T> javaClass) {
		
		this.domain = domain;
		this.javaClass = javaClass;
		this.namingConventions = new RuntimeNamingConventions();

		identifyClass();
	}

	public DomainClass(final Class<T> javaClass) {
		this(null, javaClass);
	}

	public void init() {
		identifyAccessors();
		identifyMutators();
		identifyUnSettableAttributes();
		identifyOperations();
		identifyReferences();
		identifyAssociatorsAndDissociators();
		identifyBidirectionalReferences();
	}

	private final IDomain domain;
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

	private final RuntimeNamingConventions namingConventions;
	public RuntimeNamingConventions getNamingConventions() {
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

	public boolean isChangeable() {
		EAnnotation annotation = 
			eClass.getEAnnotation(Constants.ANNOTATION_ELEMENT);
		if (annotation == null) {
			return false;
		}
		return annotation.getDetails().get(Constants.ANNOTATION_ELEMENT_IMMUTABLE_KEY) == null;
	}

	public boolean isSearchable() {
		EAnnotation annotation = 
			eClass.getEAnnotation(Constants.ANNOTATION_ELEMENT);
		if (annotation == null) {
			return false;
		}
		return annotation.getDetails().get(Constants.ANNOTATION_ELEMENT_SEARCHABLE_KEY) != null;
	}


	public boolean isInstantiable() {
		EAnnotation annotation = 
			eClass.getEAnnotation(Constants.ANNOTATION_ELEMENT);
		if (annotation == null) {
			return false;
		}
		return annotation.getDetails().get(Constants.ANNOTATION_ELEMENT_INSTANTIABLE_KEY) != null;
	}

	public boolean isPersistable() {
		EAnnotation annotation = 
			eClass.getEAnnotation(Constants.ANNOTATION_ELEMENT);
		if (annotation == null) {
			return false;
		}
		return annotation.getDetails().get(Constants.ANNOTATION_ELEMENT_SAVEABLE_KEY) != null;
	}

	public II18nData getI18nData() {
		throw new RuntimeException("Not yet implemented");
	}

	
	private Map<Class<?>, Object> adaptersByClass = new HashMap<Class<?>, Object>();
	/**
	 * Extension object pattern - getting an extension.
	 * 
	 * @param adapterClass
	 * @return adapter (extension) that will implement the said class.
	 */
	public <V> V getAdapter(Class<V> adapterClass) {
		Map<String, String> detailsPlusFactoryName = 
			emfFacade.getAnnotationDetails(eClass, Constants.ANNOTATION_EXTENSIONS_PREFIX + adapterClass.getName());
		String adapterFactoryName = 
			(String)detailsPlusFactoryName.get(Constants.ANNOTATION_EXTENSIONS_ADAPTER_FACTORY_NAME_KEY);
		IAdapterFactory<V> adapterFactory;
		try {
			adapterFactory = (IAdapterFactory<V>)Class.forName(adapterFactoryName).newInstance();
			return adapterFactory.createAdapter(detailsPlusFactoryName);
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
	/**
	 * Extension object pattern - adding an extension.
	 * 
	 * @param adapterClass
	 * @param adapter
	 */
	public <V> void setAdapterFactory(Class<V> adapterClass, IAdapterFactory<V> adapterFactory) {
		EAnnotation eAnnotation = 
			emfFacade.annotationOf(eClass, Constants.ANNOTATION_EXTENSIONS_PREFIX + adapterClass.getName());
		Map<String,String> detailsPlusFactoryName = new HashMap<String,String>();
		detailsPlusFactoryName.putAll(adapterFactory.getDetails());
		detailsPlusFactoryName.put(Constants.ANNOTATION_EXTENSIONS_ADAPTER_FACTORY_NAME_KEY, adapterFactory.getClass().getName());
		emfFacade.putAnnotationDetails(eAnnotation, detailsPlusFactoryName);
	}

	/**
	 * Process any semantics on the type itself.
	 * 
	 * <p>
	 * TODO: should also identify class hierarchy
	 */
	private void identifyClass() {
		Package javaPackage = javaClass.getPackage();
		this.eClass = EcoreFactory.eINSTANCE.createEClass();
		
		// EPackage
		EPackage ePackage = EPackage.Registry.INSTANCE.getEPackage(javaPackage.getName());
		if (ePackage == null) {
			ePackage = EcoreFactory.eINSTANCE.createEPackage();
			((List<? super EClass>)ePackage.getEClassifiers()).add(this.eClass);
			ePackage.setName(javaPackage.getName());
		}

		// InstanceClass
		eClass.setInstanceClass(javaClass);

		// Name
		String name = null;
		Named named = javaClass.getAnnotation(Named.class);
		name = named != null? named.value(): javaClass.getSimpleName();
		eClass.setName(name);

		// Description
		addDescription(javaClass.getAnnotation(DescribedAs.class), eClass);

		// Immutable (to support isChangeable)
		addIfImmutable(javaClass.getAnnotation(Immutable.class), eClass);

		// Instantiable (File>New)
		addIfInstantiable(javaClass.getAnnotation(InDomain.class), eClass);

		// Searchable (Search>???)
		addIfSearchable(javaClass.getAnnotation(InDomain.class), eClass);

		// Saveable (File>Save)
		addIfSaveable(javaClass.getAnnotation(InDomain.class), eClass);

	}

	private void addDescription(DescribedAs describedAs, EModelElement modelElement) {
		if (describedAs == null) {
			return;
		}
		EAnnotation ea = modelElement.getEAnnotation(Constants.ANNOTATION_ELEMENT);
		if (ea == null) {
			ea = emfFacade.annotationOf(modelElement, Constants.ANNOTATION_ELEMENT);
		}
		putAnnotationDetails(ea, 
			Constants.ANNOTATION_ELEMENT_DESCRIPTION_KEY, describedAs.value());
	}

	private void addIfImmutable(Immutable immutable, EModelElement modelElement) {
		
		if (immutable == null) {
			return;
		}
		EAnnotation ea = modelElement.getEAnnotation(Constants.ANNOTATION_ELEMENT);
		if (ea == null) {
			ea = emfFacade.annotationOf(modelElement, Constants.ANNOTATION_ELEMENT);
		}
		putAnnotationDetails(ea, 
			Constants.ANNOTATION_ELEMENT_IMMUTABLE_KEY, "dummy");
	}

	private void addIfInstantiable(InDomain inDomain, EModelElement modelElement) {
		
		if (inDomain == null) {
			return;
		}
		EAnnotation ea = modelElement.getEAnnotation(Constants.ANNOTATION_ELEMENT);
		if (ea == null) {
			ea = emfFacade.annotationOf(modelElement, Constants.ANNOTATION_ELEMENT);
		}
		if (inDomain.instantiable()) {
			putAnnotationDetails(ea, 
				Constants.ANNOTATION_ELEMENT_INSTANTIABLE_KEY, "dummy");
		}
	}
		

	private void addIfSearchable(InDomain inDomain, EModelElement modelElement) {
		
		if (inDomain == null) {
			return;
		}
		EAnnotation ea = modelElement.getEAnnotation(Constants.ANNOTATION_ELEMENT);
		if (ea == null) {
			ea = emfFacade.annotationOf(modelElement, Constants.ANNOTATION_ELEMENT);
		}
		if (inDomain.searchable()) {
			putAnnotationDetails(ea, 
				Constants.ANNOTATION_ELEMENT_SEARCHABLE_KEY, null);
		}
	}

	
	private void addIfSaveable(InDomain inDomain, EModelElement modelElement) {
		
		if (inDomain == null) {
			return;
		}
		EAnnotation ea = modelElement.getEAnnotation(Constants.ANNOTATION_ELEMENT);
		if (ea == null) {
			ea = emfFacade.annotationOf(modelElement, Constants.ANNOTATION_ELEMENT);
		}
		if (inDomain.saveable()) {
			putAnnotationDetails(ea, 
				Constants.ANNOTATION_ELEMENT_SAVEABLE_KEY, "dummy");
		}
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
			((List<? super EAttribute>)eClass.getEStructuralFeatures()).add(eAttribute);

			putAnnotationDetails(
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

				((List<? super EAttribute>)eClass.getEStructuralFeatures()).add(eAttribute);

				emfFacade.annotationOf(eAttribute, Constants.ANNOTATION_ATTRIBUTE_WRITE_ONLY);
			}
			
			putAnnotationDetails(
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
			
			putAnnotationDetails(
					methodAnnotationFor(eAttribute), 
					Constants.ANNOTATION_ATTRIBUTE_IS_UNSET_METHOD_NAME_KEY, 
					isUnsetMethod.getName());
			
			putAnnotationDetails(
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
	

	EAnnotation putMethodNameIn(EAnnotation eAnnotation, String methodKey, String methodName) {
		return putAnnotationDetails(eAnnotation, methodKey, methodName);
	}
	
	String getMethodNameFrom(EAnnotation eAnnotation, String methodKey) {
		return getAnnotationDetail(eAnnotation, methodKey);
	}
	
	public Method getAccessorFor(EAttribute eAttribute) {
		String accessorMethodName = 
			getMethodNameFrom(
					methodAnnotationFor(eAttribute), 
					Constants.ANNOTATION_ATTRIBUTE_ACCESSOR_METHOD_NAME_KEY);
		try {
			Method accessorMethod = 
				getJavaClass().getMethod(
						accessorMethodName, new Class[]{});
			return accessorMethod;
		} catch (SecurityException ex) {
			// TODO: log?
			return null;
		} catch (NoSuchMethodException ex) {
			// TODO: log?
			return null;
		}
	}

	public Method getMutatorFor(EAttribute eAttribute) {
		String mutatorMethodName = 
			getMethodNameFrom(
					methodAnnotationFor(eAttribute), 
					Constants.ANNOTATION_ATTRIBUTE_MUTATOR_METHOD_NAME_KEY);
		EDataType dataType = (EDataType)eAttribute.getEType();
		try {
			Method mutatorMethod = 
				getJavaClass().getMethod(
						mutatorMethodName, new Class[]{dataType.getInstanceClass()});
			return mutatorMethod;
		} catch (SecurityException ex) {
			// TODO: log?
			return null;
		} catch (NoSuchMethodException ex) {
			// TODO: log?
			return null;
		}
	}

	public Method getAccessorOrMutatorFor(EAttribute eAttribute) {
		Method accessorMethod = getAccessorFor(eAttribute);
		if (accessorMethod != null) {
			return accessorMethod;
		}
		return getMutatorFor(eAttribute);
	}

	public boolean isWriteOnly(EAttribute eAttribute) {
		return eAttribute.getEAnnotation(Constants.ANNOTATION_ATTRIBUTE_WRITE_ONLY) != null;
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
			if (getNamingConventions().isReserved(method)) {
				continue;
			}
			if (getNamingConventions().isAccessor(method) ||
					getNamingConventions().isMutator(method)) {
				continue;
			}
			if (getNamingConventions().isReference(method)) {
				continue;
			}
			if (method.getAnnotation(Programmatic.class) != null) {
				continue;
			}
			
			Class<?> returnType = method.getReturnType();
			boolean returnsValue = getNamingConventions().isValueType(returnType);
			boolean returnsReference = getNamingConventions().isReferenceType(returnType);
			boolean returnsVoid = getNamingConventions().isVoid(returnType);
			
			if (!returnsValue && !returnsReference && !returnsVoid) {
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

			if (returnsValue) {
				EDataType returnDataType = getEmfFacade().getEDataTypeFor(returnType);
				eOperation.setEType(returnDataType);
			} else if (returnsReference) {
				IDomainClass<?> returnDomainClass = domain.lookup(returnType);
				eOperation.setEType(returnDomainClass.getEClass());
			} else {
				// do nothing; EMF does not apparently have a built-in classifier for Void 
			}

			Map<Class<?>, int[]> unnamedParameterIndexByType = new HashMap<Class<?>, int[]>();
			for(int j=0; j<parameterTypes.length; j++) {
				Class<?> parameterType = parameterTypes[j];
				Annotation[] parameterAnnotationSet = parameterAnnotationSets[j];
				
				// try to match as a data type or a reference (domain class)
				EDataType parameterDataType = getEmfFacade().getEDataTypeFor(parameterType);
				IDomainClass parameterDomainClass = null;
				boolean isValue = (parameterDataType != null);
				boolean isReference = false;
				if (!isValue) {
					// register rather than lookup since we may not have seen
					// the referenced DomainClass yet.
					parameterDomainClass = domain.lookup(parameterType);
					isReference = (parameterDomainClass != null);
				}
				if (!isValue && !isReference) {
					continue;
				}
				
				EParameter eParameter = EcoreFactory.eINSTANCE.createEParameter();
				((List<? super EParameter>)eOperation.getEParameters()).add(eParameter);
				if (isValue) {
					eParameter.setEType(parameterDataType);
				} else {
					eParameter.setEType(parameterDomainClass.getEClass());
				}
				
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
					parameterName = new MethodNameHelper().camelCase(parameterType.getSimpleName());
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

			if ((methods[i].getModifiers() & Modifier.STATIC) == Modifier.STATIC) {
				emfFacade.annotationOf(eOperation, Constants.ANNOTATION_OPERATION_STATIC);
			}
			
			//((List<? super EOperation>)eClass.getEOperations()).add(eOperation);
			eClass.getEOperations().add(eOperation);
			
			// these are supported by EMF, but not (yet) by our metamodel.
//			eOperation.setLowerBound(..);
//			eOperation.setUpperBound(..);
//			eOperation.setOrdered(..);
//			eOperation.setUnique(..);
//			eOperation.setOrdered(..);
			
			putAnnotationDetails(
					methodAnnotationFor(eOperation), 
					Constants.ANNOTATION_OPERATION_METHOD_NAME_KEY, 
					methods[i].getName());
			
		}
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

	public boolean isParameterADomainObject(EOperation operation, int parameterPosition) {
		EParameter parameter = (EParameter)operation.getEParameters().get(parameterPosition);
		return parameter.getEType() instanceof EClass;
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

	public II18nData getI18nDataFor(EOperation operation) {
		throw new RuntimeException("Not yet implemented");
	}

	public II18nData getI18nDataFor(EOperation operation, int parameterPosition) {
		throw new RuntimeException("Not yet implemented");
	}


	public Method getInvokerFor(EOperation eOperation) {
		String invokerMethodName = 
			getMethodNameFrom(
					methodAnnotationFor(eOperation), 
					Constants.ANNOTATION_OPERATION_METHOD_NAME_KEY);
		EList eParameterList = eOperation.getEParameters();
		Class[] parameterTypes = new Class[eParameterList.size()];
		int i=0;
		for(EParameter eParameter: (List<EParameter>)eParameterList ) {
			parameterTypes[i++] = eParameter.getEType().getInstanceClass();
		}
		try {
			Method invokerMethod = 
				getJavaClass().getMethod(
						invokerMethodName, parameterTypes);
			return invokerMethod;
		} catch (SecurityException ex) {
			// TODO: log?
			return null;
		} catch (NoSuchMethodException ex) {
			// TODO: log?
			return null;
		}
	}


	// OPERATION SUPPORT: END


	// REFERENCES SUPPORT: START

	private void identifyReferences() {

		Method[] methods = javaClass.getMethods();
		for(int i=0; i<methods.length; i++) {
			final Method method = methods[i];

			if (!getNamingConventions().isReference(method)) {
				continue;
			}
			
			LinkSemanticsType linkSemanticsType = null;

			Class<?> referencedJavaClass = methods[i].getReturnType();
			IDomainClass<?> referencedDomainClass = null;
			boolean couldBeCollection = true;
			Associates associates = null;

			// let's see if its a 1:m (collection class)
			if (couldBeCollection) {
				linkSemanticsType = LinkSemanticsType.lookupBy(referencedJavaClass);
				if (linkSemanticsType == null) {
					// no, it's not a List, Set, Map etc.
					couldBeCollection = false;
				}
			}
			if (couldBeCollection) {
				associates = method.getAnnotation(Associates.class);
				if (associates == null) {
					// they've forgotten the @Associates annotation.
					couldBeCollection = false;
				}
			}
			if (couldBeCollection) {
				referencedJavaClass = associates.value();
				referencedDomainClass = domain.lookup(referencedJavaClass);
				if (referencedDomainClass == null) {
					// what they're referencing isn't a domain class
					couldBeCollection = false;
				}
			}
			if (!couldBeCollection) {
				// treat as a 1:1 reference
				referencedDomainClass = domain.lookup(referencedJavaClass);
				if (referencedDomainClass != null) {
					// 1:1
					linkSemanticsType = LinkSemanticsType.SIMPLE_REF;	
				} 				
			}
			EReference eReference = EcoreFactory.eINSTANCE.createEReference();
			EReferenceImpl eReferenceImpl = (EReferenceImpl)eReference;
			eReference.setEType(referencedDomainClass.getEClass()); // sets EReferenceType
			String referenceName = getNamingConventions().deriveReferenceName(method);
			eReference.setName(referenceName);
			linkSemanticsType.setOrderingUniquenessAndMultiplicity(eReference);
			
			// TODO: use EAnnotations to specify if qualified and if sorted
			
			Container container = method.getAnnotation(Container.class);
			if (container != null) {
				eReference.setContainment(true);
			}
			Derived derived = method.getAnnotation(Derived.class);
			if (derived != null) {
				eReference.setDerived(true);
				eReference.setTransient(true);
				eReference.setVolatile(true);
			}
			
			
			
			// we determine immutability based on presence of associator/dissociator.
			eReference.setChangeable(false);
			
			
			// eReference.setUnsettable()
			// eReference.setDefaultValueLiteral(someDefaultLiteral); // TODO
			// eReference.setEOpposite(eReferenceOpposite); // TODO
			
			// eReference.setResolveProxies(...); // TODO: this is a little like Hibernate lazy loading.
			
			// eReference.setLowerBound( ... set by linkSemanticsType ...);


			((List<? super EReference>)eClass.getEStructuralFeatures()).add(eReference);

			putAnnotationDetails(
					methodAnnotationFor(eReference), 
					Constants.ANNOTATION_REFERENCE_ACCESSOR_NAME_KEY, 
					method.getName());


//			if (!getProgrammingModel().isLink(methods[i])) // simple or composite.
//				continue;
			
			// TODO: resolveProxies

		}
	}

	
	
	private void identifyAssociatorsAndDissociators() {
		Method[] methods = javaClass.getMethods();
		Method associatorMethod = null;
		Method dissociatorMethod = null;
		for(EReference eReference: references()) {
			for(int i=0; i<methods.length; i++) {
				if (getNamingConventions().isAssociatorFor(methods[i], eReference)) {
					associatorMethod = methods[i];
					break;
				}
			}
			if (associatorMethod == null) {
				continue;
			}
			for(int i=0; i<methods.length; i++) {
				if (getNamingConventions().isDissociatorFor(methods[i], eReference)) {
					dissociatorMethod = methods[i];
					break;
				}
			}
			if (dissociatorMethod == null) {
				continue;
			}
			// has both an associator and a dissociator method for this reference
			eReference.setChangeable(true);
			
			putAnnotationDetails(
					methodAnnotationFor(eReference), 
					Constants.ANNOTATION_REFERENCE_ASSOCIATOR_NAME_KEY,  
					associatorMethod.getName());
			
			putAnnotationDetails(
					methodAnnotationFor(eReference), 
					Constants.ANNOTATION_REFERENCE_DISSOCIATOR_NAME_KEY, 
					dissociatorMethod.getName());
		}
	}


	/**
	 * TODO
	 */
	private void identifyBidirectionalReferences() {
		
	}


	/**
	 * indicates whether supplied associator and dissociator are compatible,
	 * that is, that they have the same type and the same name. 
	 * @param associator
	 * @param dissociator
	 * @return
	 */
	private final boolean isReferencePairCompatible(final Method associator, final Method dissociator) {
		getNamingConventions().assertAssociator(associator); // TODO: precondition aspect
		getNamingConventions().assertDissociator(dissociator);  // TODO: precondition aspect
		if (associator.getParameterTypes()[0] != dissociator.getParameterTypes()[0]) {
			return false;
		}
		String deriveReferenceNameForAssociator = getNamingConventions().deriveReferenceName(associator);
		String deriveReferenceNameForDissociator = getNamingConventions().deriveReferenceName(dissociator);
		if (!deriveReferenceNameForAssociator.equals(deriveReferenceNameForDissociator)) {
			return false;
		}
		return true;
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

	public Method getAccessorFor(EReference eReference) {
		String accessorMethodName = 
			getMethodNameFrom(
					methodAnnotationFor(eReference), 
					Constants.ANNOTATION_REFERENCE_ACCESSOR_NAME_KEY);
		try {
			Method accessorMethod = 
				getJavaClass().getMethod(
						accessorMethodName, new Class[]{});
			return accessorMethod;
		} catch (SecurityException ex) {
			// TODO: log?
			return null;
		} catch (NoSuchMethodException ex) {
			// TODO: log?
			return null;
		}
	}

	public Method getAssociatorFor(EReference eReference) {
		String associatorMethodName = 
			getMethodNameFrom(
					methodAnnotationFor(eReference), 
					Constants.ANNOTATION_REFERENCE_ASSOCIATOR_NAME_KEY);
		EClass eClass = (EClass)eReference.getEReferenceType();
		try {
			Method associatorMethod = 
				getJavaClass().getMethod(
						associatorMethodName, new Class[]{eClass.getInstanceClass()});
			return associatorMethod;
		} catch (SecurityException ex) {
			// TODO: log?
			return null;
		} catch (NoSuchMethodException ex) {
			// TODO: log?
			return null;
		}
	}

	public Method getDissociatorFor(EReference eReference) {
		String dissociatorMethodName = 
			getMethodNameFrom(
					methodAnnotationFor(eReference), 
					Constants.ANNOTATION_REFERENCE_DISSOCIATOR_NAME_KEY);
		EClass eClass = (EClass)eReference.getEReferenceType();
		try {
			Method dissociatorMethod = 
				getJavaClass().getMethod(
						dissociatorMethodName, new Class[]{eClass.getInstanceClass()});
			return dissociatorMethod;
		} catch (SecurityException ex) {
			// TODO: log?
			return null;
		} catch (NoSuchMethodException ex) {
			// TODO: log?
			return null;
		}
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

	// REFERENCES SUPPORT: END

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
		return emfFacade.annotationOf(eModelElement, Constants.ANNOTATION_SOURCE_METHOD_NAMES);
	}
	
	EAnnotation putAnnotationDetails(EAnnotation eAnnotation, String key, String value) {
		Map<String, String> details = new HashMap<String, String>();
		details.put(key, value);
		return emfFacade.putAnnotationDetails(eAnnotation, details);
	}
	
	String getAnnotationDetail(EAnnotation eAnnotation, String key) {
		return (String)emfFacade.getAnnotationDetails(eAnnotation).get(key);
	}
	
	// ANNOTATION SUPPORT: END
	
	// DOMAIN OBJECT SUPPORT: START

	public <T> IDomainObject<T> createTransient() {
		try {
			Object pojo = getJavaClass().newInstance();
			Class<T> pojoClass = pojoClass(pojo.getClass());
			IDomainObject<T> domainObject = new DomainObject(this, pojo);
			return domainObject;
		} catch(IllegalAccessException ex) {
			throw new ProgrammingModelException("Cannot instantiate", ex);
		} catch(InstantiationException ex) {
			throw new ProgrammingModelException("Cannot instantiate", ex);
		}
	}
	private <T> Class<T> pojoClass(Class<?> pojoClass) {
		return (Class<T>)pojoClass;
	}

	// DOMAIN OBJECT SUPPORT: END

	
	/**
	 * Setting up for AspectJ introduction of logging infrastructure.
	 * @param message
	 */
	private void logWarning(String message) {}

	public String toString() { return "DomainClass.javaClass = " + javaClass ; }

	// DEPENDENCY INJECTION
	
	private EmfFacade emfFacade = new EmfFacade();
	public EmfFacade getEmfFacade() {
		return emfFacade;
	}
	public void setEmfFacade(EmfFacade emfFacade) {
		this.emfFacade = emfFacade;
	}

	// DEPENDENCY INJECTION END



}
