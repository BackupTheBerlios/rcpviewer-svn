package de.berlios.rcpviewer.progmodel.standard;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
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
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.impl.EReferenceImpl;

import de.berlios.rcpviewer.domain.AbstractDomainClass;
import de.berlios.rcpviewer.domain.IAdapterFactory;
import de.berlios.rcpviewer.domain.IDomain;
import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.domain.IDomainClassAdapter;
import de.berlios.rcpviewer.domain.IRuntimeDomainClass;
import de.berlios.rcpviewer.domain.IRuntimeDomainClassAdapter;
import de.berlios.rcpviewer.domain.LinkSemanticsType;
import de.berlios.rcpviewer.domain.MethodNameHelper;
import de.berlios.rcpviewer.domain.RuntimeDomain;
import de.berlios.rcpviewer.domain.runtime.IRuntimeDomain;
import de.berlios.rcpviewer.progmodel.ProgrammingModelException;
import de.berlios.rcpviewer.progmodel.extended.IPrerequisites;
import de.berlios.rcpviewer.progmodel.extended.Named;
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
public class RuntimeDomainClass<T> 
		extends AbstractDomainClass<T> 
		implements IRuntimeDomainClass<T> {
	
	private final Class<T> _javaClass;

	public RuntimeDomainClass(final IDomain domain, final EClass eClass, final Class<T> javaClass) {
		super(domain, new RuntimeNamingConventions());
		this._eClass = eClass;
		this._javaClass = javaClass;
		identifyClass();
	}

	@Override
	protected Class<IAdapterFactory> loadClass(String adapterFactoryName) throws ClassNotFoundException {
		
		try {
			return (Class<IAdapterFactory>)Class.forName(adapterFactoryName);
		} catch (ClassNotFoundException ex) {
			// do nothing
		}
		
		return (Class<IAdapterFactory>)Platform.getBundle("de.berlios.rcpviewer.domain.runtime").loadClass(adapterFactoryName);
		//return (Class<IAdapterFactory>)DomainPlugin.getInstance().getBundle().loadClass(adapterFactoryName);
		
	}


	/**
	 * Identify attributes, operations, and references.
	 * 
	 * <p>
	 * For bidirectional references we note any @OppositeOf annotations but
	 * the actual wiring up of bidirectional references is done in the
	 * {@link StandardProgModelDomainBuilder#done}, delegating back to 
	 * {@link #wireUpOppositeReferences()}.
	 */
	public void init() {
		identifyAccessors();
		identifyMutators();
		identifyUnSettableAttributes();
		identifyOperations();
		identifyReferences();
		identifyOppositeReferences();
		identifyAssociatorsAndDissociators();
		oppRefState = OppRefState.onceMore;
	}

	static enum OppRefState {
		stillBuilding, onceMore, neverAgain;
	}

	/**
	 * HACK: This is a horrible hack that allows our 
	 * {@link #identifyOppositeReferences()} to be called once more, in 
	 * {@link RuntimeDomain#lookup(Class)}.
	 */
	protected OppRefState oppRefState = OppRefState.stillBuilding;

	public void identifyOppositeReferences() {
		
		if (oppRefState == OppRefState.neverAgain) {
			return;
		} else if (oppRefState == OppRefState.stillBuilding) {
			// do nothing
		} else if (oppRefState == OppRefState.onceMore) {
			oppRefState = OppRefState.neverAgain;
		}
		
		for(EReference reference: this.references()) {
			// since later on we call this same method on the DomainClass 
			// representing the referenced class, we have the possibility of
			// an infinite loop if both sides have an @OppositeOf annotation.
			// this guard prevents this from happening.
			if (reference.getEOpposite() != null) {
				continue;
			}
			EClass referencedEClass = reference.getEReferenceType();
			IDomainClass<?> referencedClass = 
				getDomain().domainClassFor(referencedEClass);

			String oppositeReferenceName = 
				emfFacade.getAnnotationDetail(referenceAnnotationFor(reference), StandardProgModelConstants.ANNOTATION_REFERENCE_OPPOSITE_KEY);
			if (oppositeReferenceName != null) {
				// annotation on this end, so set up the opposite
				EReference oppositeReference = 
					referencedClass.getEReferenceNamed(oppositeReferenceName);
				if(oppositeReference != null) {
					reference.setEOpposite(oppositeReference);
					oppositeReference.setEOpposite(reference);
				}
			} else {
				// no annotation this end, but its possible that the referenced
				// object does have an annotation its end.
				// however, we pick this up by doing postprocessubg when we
				// do a lookup of any object.
			}
		}
	}

	public Class<T> getJavaClass() {
		return _javaClass;
	}
	
	/**
	 * Process any semantics on the type itself.
	 * 
	 * <p>
	 * TODO: should also identify class hierarchy
	 */
	private void identifyClass() {
		InDomain domainAnnotation = _javaClass.getAnnotation(InDomain.class);
		String domainName = domainAnnotation.value();
		RuntimeDomain domain = RuntimeDomain.instance(domainName);

		addNamed(_javaClass.getAnnotation(Named.class), _eClass);
		
		addDescription(_javaClass.getAnnotation(DescribedAs.class), _eClass);
		
		// Immutable (to support isChangeable)
		addIfImmutable(_javaClass.getAnnotation(Immutable.class), _eClass);
	}

	private void addNamed(Named named, EModelElement modelElement) {
		String name = named != null ? named.value() : getEClassName();
		emfFacade.putAnnotationDetails(this, modelElement, 
				StandardProgModelConstants.ANNOTATION_ELEMENT_NAMED_KEY, 
				name);	
	}

	private void addDescription(DescribedAs describedAs, EModelElement modelElement) {
		if (describedAs == null) {
			return;
		}
		emfFacade.putAnnotationDetails(this, modelElement, 
			StandardProgModelConstants.ANNOTATION_ELEMENT_DESCRIPTION_KEY, 
			describedAs.value());
	}


	private void addIfImmutable(Immutable immutable, EModelElement modelElement) {
		emfFacade.putAnnotationDetails( 
			this, modelElement, StandardProgModelConstants.ANNOTATION_ELEMENT_IMMUTABLE_KEY, immutable != null);
	}
	

	/*
	 * @see de.berlios.rcpviewer.domain.IDomainClass#getName()
	 */
	@Override
	public String getName() {
		EAnnotation annotation = 
			getEClass().getEAnnotation(StandardProgModelConstants.ANNOTATION_ELEMENT);
		if (annotation == null) {
			return getEClassName();
		}
		String explicitName = 
			(String)annotation.getDetails().get(StandardProgModelConstants.ANNOTATION_ELEMENT_NAMED_KEY);
		return explicitName;
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
		
		Method[] methods = _javaClass.getMethods();
		// search for accessors of value types
		// initially all attributes start off read-only
		for(int i=0; i<methods.length; i++) {
			final Method method = methods[i];
			if (!isAttribute(methods[i])) {
				continue;
			}
			
			EAttribute eAttribute = EcoreFactory.eINSTANCE.createEAttribute();
			Class<?> dataType = methods[i].getReturnType();
						
			EDataType eDataType = getEDataTypeFor(dataType);
			eAttribute.setEType(eDataType);
			String attributeName = getRuntimeNamingConventions().deriveAttributeName(method);
			eAttribute.setName(attributeName);
			((List<? super EAttribute>)_eClass.getEStructuralFeatures()).add(eAttribute);

			emfFacade.putAnnotationDetails(
					this, emfFacade.methodNamesAnnotationFor(eAttribute), 
					StandardProgModelConstants.ANNOTATION_ATTRIBUTE_ACCESSOR_METHOD_NAME_KEY, 
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
	
	private EDataType getEDataTypeFor(Class<?> dataType) {
		if (!getNamingConventions().isValueType(dataType)) {
			return null;
		}
		if (getNamingConventions().isCoreValueType(dataType)) {
			return getEmfFacade().getEDataTypeFor(dataType, null);
		}
		String domainName = 
			getNamingConventions().getValueTypeDomainName(dataType);
		IDomain domain = RuntimeDomain.instance(domainName);
					
		return getEmfFacade().getEDataTypeFor(dataType, domain.getResourceSet());
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

		Method[] methods = _javaClass.getMethods();

		for(int i=0; i<methods.length; i++) {
			final Method method = methods[i];
			if (!getRuntimeNamingConventions().isMutator(method)) {
				continue;
			}

			String attributeName = getRuntimeNamingConventions().deriveAttributeName(method);
			EAttribute eAttribute = getEAttributeNamed(attributeName);

			if (eAttribute != null) {
				eAttribute.setChangeable(true);
			} else {
				eAttribute = EcoreFactory.eINSTANCE.createEAttribute();
				Class<?> dataType = methods[i].getParameterTypes()[0];
				EDataType eDataType = getEDataTypeFor(dataType);
				eAttribute.setEType(eDataType);
				eAttribute.setName(attributeName);

				((List<? super EAttribute>)_eClass.getEStructuralFeatures()).add(eAttribute);

				emfFacade.annotationOf(eAttribute, StandardProgModelConstants.ANNOTATION_ATTRIBUTE_WRITE_ONLY);
			}
			
			emfFacade.putAnnotationDetails(
					this, emfFacade.methodNamesAnnotationFor(eAttribute), 
					StandardProgModelConstants.ANNOTATION_ATTRIBUTE_MUTATOR_METHOD_NAME_KEY, 
					methods[i].getName());
			
//			eAttribute.setDefaultValueLiteral(defaultValueAsString); // TODO: read from annotation
		}
	}

	/**
	 *  
	 */
	private void identifyUnSettableAttributes() {
	
		Method[] methods = _javaClass.getMethods();
		Method isUnsetMethod = null;
		Method unsetMethod = null;
		for(EAttribute eAttribute: attributes()) {
			for(int i=0; i<methods.length; i++) {
				if (getRuntimeNamingConventions().isIsUnsetMethodFor(methods[i], eAttribute)) {
					isUnsetMethod = methods[i];
					break;
				}
			}
			if (isUnsetMethod == null) {
				continue;
			}
			for(int i=0; i<methods.length; i++) {
				if (getRuntimeNamingConventions().isUnsetMethodFor(methods[i], eAttribute)) {
					unsetMethod = methods[i];
					break;
				}
			}
			if (unsetMethod == null) {
				continue;
			}
			// has both an IsUnset and an unset method for this attribute
			eAttribute.setUnsettable(true);
			
			emfFacade.putAnnotationDetails(
					this, emfFacade.methodNamesAnnotationFor(eAttribute), 
					StandardProgModelConstants.ANNOTATION_ATTRIBUTE_IS_UNSET_METHOD_NAME_KEY, 
					isUnsetMethod.getName());
			
			emfFacade.putAnnotationDetails(
					this, emfFacade.methodNamesAnnotationFor(eAttribute), 
					StandardProgModelConstants.ANNOTATION_ATTRIBUTE_UNSET_METHOD_NAME_KEY, 
					isUnsetMethod.getName());
		}
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
		getRuntimeNamingConventions().assertAccessor(accessor);  // TODO: precondition aspect
		getRuntimeNamingConventions().assertMutator(mutator);  // TODO: precondition aspect
		// check return type of accessor to the type 
		// of the first parameter of the mutator
		if (!accessor.getReturnType().equals(mutator.getParameterTypes()[0])) {
			return false;
		}
		String accessorName = getRuntimeNamingConventions().deriveAttributeName(accessor);
		String mutatorName = getRuntimeNamingConventions().deriveAttributeName(mutator);
		if (!accessorName.equals(mutatorName)) {
			return false;
		}
		return true;
	}

	/*
	 * @see de.berlios.rcpviewer.domain.IRuntimeDomainClass#getAccessorFor(org.eclipse.emf.ecore.EAttribute)
	 */
	public Method getAccessorFor(EAttribute eAttribute) {
		String accessorMethodName = 
			emfFacade.getAnnotationDetail(emfFacade.methodNamesAnnotationFor(eAttribute), StandardProgModelConstants.ANNOTATION_ATTRIBUTE_ACCESSOR_METHOD_NAME_KEY);
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

	/*
	 * @see de.berlios.rcpviewer.domain.IRuntimeDomainClass#getMutatorFor(org.eclipse.emf.ecore.EAttribute)
	 */
	public Method getMutatorFor(EAttribute eAttribute) {
		String mutatorMethodName = 
			emfFacade.getAnnotationDetail(emfFacade.methodNamesAnnotationFor(eAttribute), StandardProgModelConstants.ANNOTATION_ATTRIBUTE_MUTATOR_METHOD_NAME_KEY);
		if (mutatorMethodName == null) {
			return null;
		}
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

	/*
	 * @see de.berlios.rcpviewer.domain.IRuntimeDomainClass#getAccessorOrMutatorFor(org.eclipse.emf.ecore.EAttribute)
	 */
	public Method getAccessorOrMutatorFor(EAttribute eAttribute) {
		Method accessorMethod = getAccessorFor(eAttribute);
		if (accessorMethod != null) {
			return accessorMethod;
		}
		return getMutatorFor(eAttribute);
	}

	public boolean containsAttribute(EAttribute eAttribute) {
		return this._eClass.getEAllAttributes().contains(eAttribute);
	}

	/**
	 * Identifies operations of the domain class.
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
		Method[] methods = _javaClass.getMethods();

		eachMethod: 
		for(int i=0; i<methods.length; i++) {
			final Method method = methods[i];
			if ((method.getModifiers() & Method.PUBLIC) != Method.PUBLIC) {
				continue;
			}
			if (getRuntimeNamingConventions().isReserved(method)) {
				continue;
			}
			if (getRuntimeNamingConventions().isAccessor(method) ||
					getRuntimeNamingConventions().isMutator(method)) {
				continue;
			}
			if (getRuntimeNamingConventions().isReference(method)) {
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
				EDataType returnDataType = getEDataTypeFor(returnType);
				eOperation.setEType(returnDataType);
			} else if (returnsReference) {
				IDomainClass<?> returnDomainClass = _domain.lookup(returnType);
				eOperation.setEType(returnDomainClass.getEClass());
			} else {
				// do nothing; EMF does not apparently have a built-in classifier for Void 
			}

			Map<Class<?>, int[]> unnamedParameterIndexByType = new HashMap<Class<?>, int[]>();
			for(int j=0; j<parameterTypes.length; j++) {
				Class<?> parameterType = parameterTypes[j];
				Annotation[] parameterAnnotationSet = parameterAnnotationSets[j];
				
				// try to match as a data type or a reference (domain class)
				EDataType parameterDataType = getEDataTypeFor(parameterType);
				IDomainClass parameterDomainClass = null;
				boolean isValue = (parameterDataType != null);
				boolean isReference = false;
				if (!isValue) {
					// register rather than lookup since we may not have seen
					// the referenced DomainClass yet.
					parameterDomainClass = _domain.lookup(parameterType);
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
				emfFacade.annotationOf(eOperation, StandardProgModelConstants.ANNOTATION_OPERATION_STATIC);
			}
			
			((List<? super EOperation>)_eClass.getEOperations()).add(eOperation);
			
			// these are supported by EMF, but not (yet) by our metamodel.
//			eOperation.setLowerBound(..);
//			eOperation.setUpperBound(..);
//			eOperation.setOrdered(..);
//			eOperation.setUnique(..);
//			eOperation.setOrdered(..);
			
			emfFacade.putAnnotationDetails(
					this, emfFacade.methodNamesAnnotationFor(eOperation), 
					StandardProgModelConstants.ANNOTATION_OPERATION_METHOD_NAME_KEY, 
					methods[i].getName());
			
		}
	}

	/*
	 * @see de.berlios.rcpviewer.domain.IDomainClass#isParameterAValue(org.eclipse.emf.ecore.EOperation, int)
	 */
	public boolean isParameterAValue(EOperation operation, int parameterPosition) {
		EParameter parameter = (EParameter)operation.getEParameters().get(parameterPosition);
		return parameter.getEType() instanceof EDataType;
	}

	/*
	 * @see de.berlios.rcpviewer.domain.IDomainClass#isParameterADomainObject(org.eclipse.emf.ecore.EOperation, int)
	 */
	public boolean isParameterADomainObject(EOperation operation, int parameterPosition) {
		EParameter parameter = (EParameter)operation.getEParameters().get(parameterPosition);
		return parameter.getEType() instanceof EClass;
	}

	/*
	 * @see de.berlios.rcpviewer.domain.IRuntimeDomainClass#getInvokerFor(org.eclipse.emf.ecore.EOperation)
	 */
	public Method getInvokerFor(EOperation eOperation) {
		String invokerMethodName = 
			emfFacade.getAnnotationDetail(emfFacade.methodNamesAnnotationFor(eOperation), StandardProgModelConstants.ANNOTATION_OPERATION_METHOD_NAME_KEY);
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

		Method[] methods = _javaClass.getMethods();
		for(int i=0; i<methods.length; i++) {
			final Method method = methods[i];

			if (!getRuntimeNamingConventions().isReference(method)) {
				continue;
			}
			
			LinkSemanticsType linkSemanticsType = null;

			Class<?> referencedJavaClass = methods[i].getReturnType();
			IDomainClass<?> referencedDomainClass = null;
			boolean couldBeCollection = true;
			TypeOf associates = null;

			// let's see if its a 1:m (collection class)
			if (couldBeCollection) {
				linkSemanticsType = LinkSemanticsType.lookupBy(referencedJavaClass);
				if (linkSemanticsType == null) {
					// no, it's not a List, Set, Map etc.
					couldBeCollection = false;
				}
			}
			if (couldBeCollection) {
				associates = method.getAnnotation(TypeOf.class);
				if (associates == null) {
					// they've forgotten the @TypeOf annotation.
					couldBeCollection = false;
				}
			}
			if (couldBeCollection) {
				referencedJavaClass = associates.value();
				referencedDomainClass = _domain.lookup(referencedJavaClass);
				if (referencedDomainClass == null) {
					// what they're referencing isn't a domain class
					couldBeCollection = false;
				}
			}
			if (!couldBeCollection) {
				// treat as a 1:1 reference
				referencedDomainClass = _domain.lookup(referencedJavaClass);
				if (referencedDomainClass != null) {
					// 1:1
					linkSemanticsType = LinkSemanticsType.SIMPLE_REF;	
				} 				
			}
			String referenceName = getRuntimeNamingConventions().deriveReferenceName(method);
			if (this.getEReferenceNamed(referenceName) != null) {
				continue;
			}

			EReference eReference = EcoreFactory.eINSTANCE.createEReference();
			eReference.setEType(referencedDomainClass.getEClass()); // sets EReferenceType
			eReference.setName(referenceName);
			linkSemanticsType.setOrderingUniquenessAndMultiplicity(eReference);
			
			// TODO: use EAnnotations to specify if qualified and if sorted
			
			ContainerOf container = method.getAnnotation(ContainerOf.class);
			if (container != null) {
				eReference.setContainment(true);
			}
			Derived derived = method.getAnnotation(Derived.class);
			if (derived != null) {
				eReference.setDerived(true);
				eReference.setTransient(true);
				eReference.setVolatile(true);
			}

			// initially we just catalog the opposite; later we shall look 
			// for them.
			OppositeOf oppositeOf = method.getAnnotation(OppositeOf.class);
			if (oppositeOf != null) {
				emfFacade.putAnnotationDetails(
						this, referenceAnnotationFor(eReference), 
						StandardProgModelConstants.ANNOTATION_REFERENCE_OPPOSITE_KEY, 
						oppositeOf.value());
			}
			
			
			// we determine immutability based on presence of associator/dissociator.
			eReference.setChangeable(false);
			
			
			// eReference.setUnsettable()
			// eReference.setDefaultValueLiteral(someDefaultLiteral); // TODO
			
			// eReference.setResolveProxies(...); // TODO: this is a little like Hibernate lazy loading.
			
			// eReference.setLowerBound( ... set by linkSemanticsType ...);


			((List<? super EReference>)_eClass.getEStructuralFeatures()).add(eReference);

			emfFacade.putAnnotationDetails(
					this, emfFacade.methodNamesAnnotationFor(eReference), 
					StandardProgModelConstants.ANNOTATION_REFERENCE_ACCESSOR_NAME_KEY, 
					method.getName());


//			if (!getProgrammingModel().isLink(methods[i])) // simple or composite.
//				continue;
			
			// TODO: resolveProxies

		}
	}

	
	
	private void identifyAssociatorsAndDissociators() {
		Method[] methods = _javaClass.getMethods();
		for(EReference eReference: references()) {
			Method associatorMethod = null;
			Method dissociatorMethod = null;
			for(int i=0; i<methods.length; i++) {
				if (getRuntimeNamingConventions().isAssociatorFor(methods[i], eReference)) {
					associatorMethod = methods[i];
					break;
				}
			}
			if (associatorMethod == null) {
				continue;
			}
			for(int i=0; i<methods.length; i++) {
				if (getRuntimeNamingConventions().isDissociatorFor(methods[i], eReference)) {
					dissociatorMethod = methods[i];
					break;
				}
			}
			if (dissociatorMethod == null) {
				continue;
			}
			// has both an associator and a dissociator method for this reference
			eReference.setChangeable(true);
			
			emfFacade.putAnnotationDetails(
					this, emfFacade.methodNamesAnnotationFor(eReference),
					eReference.isMany()?
							StandardProgModelConstants.ANNOTATION_REFERENCE_ADD_TO_NAME_KEY:
							StandardProgModelConstants.ANNOTATION_REFERENCE_ASSOCIATE_NAME_KEY,  
					associatorMethod.getName());
			
			emfFacade.putAnnotationDetails(
					this, emfFacade.methodNamesAnnotationFor(eReference), 
					eReference.isMany()?
							StandardProgModelConstants.ANNOTATION_REFERENCE_REMOVE_FROM_NAME_KEY:
							StandardProgModelConstants.ANNOTATION_REFERENCE_DISSOCIATE_NAME_KEY, 
					dissociatorMethod.getName());
		}
	}


	/*
	 * @see de.berlios.rcpviewer.domain.IRuntimeDomainClass#getAccessorFor(org.eclipse.emf.ecore.EReference)
	 */
	public Method getAccessorFor(EReference eReference) {
		String accessorMethodName = 
			emfFacade.getAnnotationDetail(emfFacade.methodNamesAnnotationFor(eReference), StandardProgModelConstants.ANNOTATION_REFERENCE_ACCESSOR_NAME_KEY);
		if (accessorMethodName == null) {
			return null;
		}
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

	/*
	 * 
	 * @see de.berlios.rcpviewer.domain.IRuntimeDomainClass#getMutatorFor(org.eclipse.emf.ecore.EReference)
	 */
	public Method getMutatorFor(EReference reference) {
		String mutatorMethodName = 
			emfFacade.getAnnotationDetail(emfFacade.methodNamesAnnotationFor(reference), StandardProgModelConstants.ANNOTATION_REFERENCE_MUTATOR_NAME_KEY);
		if (mutatorMethodName == null) {
			return null;
		}
		try {
			Method accessorMethod = 
				getJavaClass().getMethod(
						mutatorMethodName, new Class[]{});
			return accessorMethod;
		} catch (SecurityException ex) {
			// TODO: log?
			return null;
		} catch (NoSuchMethodException ex) {
			// TODO: log?
			return null;
		}
	}

	/*
	 * @see de.berlios.rcpviewer.domain.IRuntimeDomainClass#getAssociatorFor(org.eclipse.emf.ecore.EReference)
	 */
	public Method getAssociatorFor(EReference eReference) {
		String associatorMethodName = 
			emfFacade.getAnnotationDetail(
				emfFacade.methodNamesAnnotationFor(eReference),
				eReference.isMany()?
					StandardProgModelConstants.ANNOTATION_REFERENCE_ADD_TO_NAME_KEY:
					StandardProgModelConstants.ANNOTATION_REFERENCE_ASSOCIATE_NAME_KEY);
		if (associatorMethodName == null) {
			return null;
		}
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

	/*
	 * @see de.berlios.rcpviewer.domain.IRuntimeDomainClass#getDissociatorFor(org.eclipse.emf.ecore.EReference)
	 */
	public Method getDissociatorFor(EReference eReference) {
		String dissociatorMethodName = 
			emfFacade.getAnnotationDetail(
				emfFacade.methodNamesAnnotationFor(eReference), 
					eReference.isMany()?
						StandardProgModelConstants.ANNOTATION_REFERENCE_REMOVE_FROM_NAME_KEY:
							StandardProgModelConstants.ANNOTATION_REFERENCE_DISSOCIATE_NAME_KEY);
		if (dissociatorMethodName == null) {
			return null;
		}
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

	/*
	 * @see de.berlios.rcpviewer.domain.IRuntimeDomainClass#createTransient()
	 */
	public <T> IDomainObject<T> createTransient() {
		try {
			Object pojo = getJavaClass().newInstance();
			IDomainObject<T> domainObject = new DomainObject(this, pojo);
			domainObject.nowTransient();
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

	protected boolean isAttribute(Method method) {
		return (getRuntimeNamingConventions().isAccessor(method) || 
				getRuntimeNamingConventions().isMutator(method)) &&
				getRuntimeNamingConventions().isValueType(method.getReturnType());
	}

	/**
	 * indicates whether supplied associator and dissociator are compatible,
	 * that is, that they have the same type and the same name. 
	 * @param associator
	 * @param dissociator
	 * @return
	 */
	private final boolean isReferencePairCompatible(final Method associator, final Method dissociator) {
		getRuntimeNamingConventions().assertAssociator(associator); // TODO: precondition aspect
		getRuntimeNamingConventions().assertDissociator(dissociator);  // TODO: precondition aspect
		if (associator.getParameterTypes()[0] != dissociator.getParameterTypes()[0]) {
			return false;
		}
		String deriveReferenceNameForAssociator = getRuntimeNamingConventions().deriveReferenceName(associator);
		String deriveReferenceNameForDissociator = getRuntimeNamingConventions().deriveReferenceName(dissociator);
		if (!deriveReferenceNameForAssociator.equals(deriveReferenceNameForDissociator)) {
			return false;
		}
		return true;
	}

	/*
	 * @see de.berlios.rcpviewer.domain.IRuntimeDomainClass#getObjectAdapterFor(de.berlios.rcpviewer.session.IDomainObject, java.lang.Class)
	 */
	public <V> V getObjectAdapterFor(IDomainObject<T> domainObject, Class<V> objectAdapterClass) {
		List<IDomainClassAdapter> classAdapters = getAdapters();
		for(IDomainClassAdapter classAdapter: classAdapters) {
			// TODO: should be using covariance of getAdapters()
			IRuntimeDomainClassAdapter runtimeClassAdapter = (IRuntimeDomainClassAdapter)classAdapter;
			V objectAdapter = (V)runtimeClassAdapter.getObjectAdapterFor(domainObject, objectAdapterClass); 
			if (objectAdapter != null) {
				return objectAdapter;
			}
		}
		return null;
	}

	// TODO: should be using covariance
	public RuntimeNamingConventions getRuntimeNamingConventions() {
		return (RuntimeNamingConventions)getNamingConventions();
	}

	/*
	 * @see de.berlios.rcpviewer.domain.IDomainClass#getEClassName()
	 */
	public String getEClassName() {
		return getEClass().getName();
	}

	/*
	 * @see de.berlios.rcpviewer.domain.IRuntimeDomainClass#attributeIdFor(org.eclipse.emf.ecore.EAttribute)
	 */
	public IFeatureId attributeIdFor(EAttribute attribute) {
		return FeatureId.create(attribute.getName(), this, IFeatureId.Type.ATTRIBUTE); 
	}

	/*
	 * 
	 * @see de.berlios.rcpviewer.domain.IDomainClass#referenceIdFor(org.eclipse.emf.ecore.EReference)
	 */
	public IFeatureId referenceIdFor(EReference reference) {
		return FeatureId.create(reference.getName(), this, IFeatureId.Type.REFERENCE);
	}

	/*
	 * 
	 * @see de.berlios.rcpviewer.domain.IDomainClass#operationIdFor(org.eclipse.emf.ecore.EOperation)
	 */
	public IFeatureId operationIdFor(EOperation operation) {
		return FeatureId.create(operation.getName(), this, IFeatureId.Type.OPERATION);
	}

	/*
	 * @see de.berlios.rcpviewer.domain.IRuntimeDomainClass#authorizationConstraintFor(org.eclipse.emf.ecore.EAttribute)
	 */
	public IPrerequisites authorizationConstraintFor(EAttribute attribute) {
		IRuntimeDomain rd = (IRuntimeDomain)this.getDomain();
		return rd.getAuthorizationManager().preconditionsFor(attributeIdFor(attribute));
	}


	public IPrerequisites authorizationConstraintFor(EReference reference) {
		IRuntimeDomain rd = (IRuntimeDomain)this.getDomain();
		return rd.getAuthorizationManager().preconditionsFor(referenceIdFor(reference));
	}

	public IPrerequisites authorizationConstraintFor(EOperation operation) {
		IRuntimeDomain rd = (IRuntimeDomain)this.getDomain();
		return rd.getAuthorizationManager().preconditionsFor(operationIdFor(operation));
	}

	/*
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() { return "DomainClass.javaClass = " + _javaClass ; }


}
