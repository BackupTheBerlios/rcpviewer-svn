package org.essentialplatform.progmodel.essential.runtime;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcoreFactory;
import org.essentialplatform.core.domain.Domain;
import org.essentialplatform.core.domain.DomainClass;
import org.essentialplatform.core.domain.IDomain;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.domain.LinkSemanticsType;
import org.essentialplatform.core.domain.DomainClass.OppRefState;
import org.essentialplatform.core.domain.builders.IDomainBuilder;
import org.essentialplatform.core.emf.Emf;
import org.essentialplatform.core.util.MethodNameHelper;
import org.essentialplatform.progmodel.essential.app.ContainerOf;
import org.essentialplatform.progmodel.essential.app.Derived;
import org.essentialplatform.progmodel.essential.app.DescribedAs;
import org.essentialplatform.progmodel.essential.app.Immutable;
import org.essentialplatform.progmodel.essential.app.InDomain;
import org.essentialplatform.progmodel.essential.app.LowerBoundOf;
import org.essentialplatform.progmodel.essential.app.Named;
import org.essentialplatform.progmodel.essential.app.OppositeOf;
import org.essentialplatform.progmodel.essential.app.Ordered;
import org.essentialplatform.progmodel.essential.app.Programmatic;
import org.essentialplatform.progmodel.essential.app.TypeOf;
import org.essentialplatform.progmodel.essential.app.Unique;
import org.essentialplatform.progmodel.essential.app.UpperBoundOf;
import org.essentialplatform.progmodel.essential.core.domain.OppositeReferencesIdentifier;
import org.essentialplatform.progmodel.essential.core.emf.EssentialProgModelStandardSemanticsEmfSerializer;
import org.essentialplatform.progmodel.essential.core.util.EssentialProgModelStandardSemanticsRules;
import org.essentialplatform.progmodel.essential.core.util.JavaRules;
import org.essentialplatform.runtime.client.domain.bindings.RuntimeClientBinding.RuntimeClientClassBinding;

/**
 * Builds standard domain model.
 * 
 * <p>
 * Specifically, these means:
 * <ul>
 * <li>class attributes
 * <li>class operatrions
 * <li>class references
 * </ul>
 * <p>
 * A limited set of semantics (broadly, those intrinsically supported by EMF) 
 * are identified.
 * 
 * 
 * @author Dan Haywood
 *
 */
final class EssentialProgModelStandardRuntimeBuilder implements IDomainBuilder{

	private Emf emf = new Emf();
	private EssentialProgModelStandardSemanticsEmfSerializer serializer = new EssentialProgModelStandardSemanticsEmfSerializer();

	private final EssentialProgModelStandardSemanticsRules _standardRuntimeRules = new EssentialProgModelRuntimeRules();
	private JavaRules _javaRules = new JavaRules();

	/**
	 * Because this implementation is the primary domain builder, this method
	 * is called as each {@link IDomainClass} is registered, rather than at the
	 * end of registration via {@link org.essentialplatform.core.domain.IDomain#done()}.
	 */
	public void build(IDomainClass domainClass) {
		init((DomainClass)domainClass);
	}

	/**
	 * Identify attributes, operations, and references.
	 * 
	 * <p>
	 * For bidirectional references we note any @OppositeOf annotations but
	 * the actual wiring up of bidirectional references is done in the
	 * {@link EssentialProgModelRuntimeBuilder#done}, delegating back to 
	 * {@link #wireUpOppositeReferences()}.
	 */
	public void init(DomainClass domainClass) {
		identifyClassSemantics(domainClass);
		identifyAccessors(domainClass);
		identifyMutators(domainClass);
		identifyUnSettableAttributes(domainClass);
		identifyOperations(domainClass);
		identifyReferences(domainClass);
		identifyOppositeReferences(domainClass);
		identifyAssociatorsAndDissociators(domainClass);
		domainClass.oppRefState = OppRefState.onceMore;
	}

	
	/**
	 * Process any semantics on the type itself.
	 * 
	 * <p>
	 * TODO: should also identify class hierarchy
	 * 
	 * JAVA5_FIXME: downcasts for getAnnotation should not be needed...
	 */
	private void identifyClassSemantics(DomainClass runtimeDomainClass) {
		RuntimeClientClassBinding binding = (RuntimeClientClassBinding)runtimeDomainClass.getBinding();

		EClass eClass = runtimeDomainClass.getEClass();
		InDomain domainAnnotation = (InDomain)binding.getAnnotation(InDomain.class);

		serializer.setNamed(eClass, (Named)binding.getAnnotation(Named.class));
		serializer.setDescribedAs(eClass, (DescribedAs)binding.getAnnotation(DescribedAs.class));
		serializer.setImmutable(eClass, (Immutable)binding.getAnnotation(Immutable.class));
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
	public void identifyAccessors(DomainClass domainClass) {
		
		Class<?> javaClass = ((RuntimeClientClassBinding)domainClass.getBinding()).getJavaClass();
		EClass eClass = domainClass.getEClass();
		
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
						
			EDataType eDataType = getEDataTypeFor(dataType);
			eAttribute.setEType(eDataType);
			String attributeName = getRuntimeStandardProgModelRules().deriveAttributeName(method);
			eAttribute.setName(attributeName);
			((List<? super EAttribute>)eClass.getEStructuralFeatures()).add(eAttribute);

			serializer.setAttributeAccessorMethod(eAttribute, method);
			
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
			// TODO: should be using serializer, surely???
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
	public void identifyMutators(DomainClass domainClass) {

		Class<?> javaClass = ((RuntimeClientClassBinding)domainClass.getBinding()).getJavaClass();
		Method[] methods = javaClass.getMethods();

		for(int i=0; i<methods.length; i++) {
			final Method method = methods[i];
			if (!getRuntimeStandardProgModelRules().isMutator(method)) {
				continue;
			}

			String attributeName = getRuntimeStandardProgModelRules().deriveAttributeName(method);
			IDomainClass.IAttribute iAttribute = domainClass.getIAttributeNamed(attributeName);
			
			EAttribute eAttribute = null;
			if (iAttribute != null) {
				eAttribute = iAttribute.getEAttribute(); 
				eAttribute.setChangeable(true);
			} else {
				eAttribute = EcoreFactory.eINSTANCE.createEAttribute();
				Class<?> dataType = methods[i].getParameterTypes()[0];
				EDataType eDataType = getEDataTypeFor(dataType);
				eAttribute.setEType(eDataType);
				eAttribute.setName(attributeName);

				((List<? super EAttribute>)domainClass.getEClass().getEStructuralFeatures()).add(eAttribute);

				serializer.setAttributeWriteOnly(eAttribute);
			}
			
			serializer.setAttributeMutatorMethod(eAttribute, methods[i]);
			
//			eAttribute.setDefaultValueLiteral(defaultValueAsString); // TODO: read from annotation
		}
	}

	/**
	 *  
	 */
	public void identifyUnSettableAttributes(DomainClass domainClass) {
		Class<?> javaClass = ((RuntimeClientClassBinding)domainClass.getBinding()).getJavaClass();
		Method[] methods = javaClass.getMethods();
		Method isUnsetMethod = null;
		Method unsetMethod = null;
		for(IDomainClass.IAttribute iAttribute: domainClass.iAttributes()) {
			EAttribute eAttribute = iAttribute.getEAttribute();
			for(int i=0; i<methods.length; i++) {
				if (getRuntimeStandardProgModelRules().isIsUnsetMethodFor(methods[i], eAttribute)) {
					isUnsetMethod = methods[i];
					break;
				}
			}
			if (isUnsetMethod == null) {
				continue;
			}
			for(int i=0; i<methods.length; i++) {
				if (getRuntimeStandardProgModelRules().isUnsetMethodFor(methods[i], eAttribute)) {
					unsetMethod = methods[i];
					break;
				}
			}
			if (unsetMethod == null) {
				continue;
			}
			// has both an IsUnset and an unset method for this attribute
			eAttribute.setUnsettable(true);
			serializer.setAttributeIsUnsetMethod(eAttribute, isUnsetMethod);
			serializer.setAttributeIsUnsetMethod(eAttribute, unsetMethod);
		}
	}
	
	public void identifyReferences(DomainClass domainClass) {
		Class<?> javaClass = ((RuntimeClientClassBinding)domainClass.getBinding()).getJavaClass();
		Method[] methods = javaClass.getMethods();
		for(int i=0; i<methods.length; i++) {
			final Method method = methods[i];

			if (!getRuntimeStandardProgModelRules().isReference(method)) {
				continue;
			}
			
			LinkSemanticsType linkSemanticsType = null;

			Class<?> referencedJavaClass = methods[i].getReturnType();
			IDomainClass referencedDomainClass = null;
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
				referencedDomainClass = domainClass.getDomain().lookup(referencedJavaClass);
				if (referencedDomainClass == null) {
					// what they're referencing isn't a domain class
					couldBeCollection = false;
				}
			}
			if (!couldBeCollection) {
				// treat as a 1:1 reference
				referencedDomainClass = domainClass.getDomain().lookup(referencedJavaClass);
				if (referencedDomainClass != null) {
					// 1:1
					linkSemanticsType = LinkSemanticsType.SIMPLE_REF;	
				} 				
			}
			String referenceName = getRuntimeStandardProgModelRules().deriveReferenceName(method);
			if (domainClass.getIReferenceNamed(referenceName) != null) {
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
			serializer.setReferenceOppositeOf(eReference, method.getAnnotation(OppositeOf.class));
			
			// we determine immutability based on presence of associator/dissociator.
			eReference.setChangeable(false);
			
			
			// eReference.setUnsettable()
			// eReference.setDefaultValueLiteral(someDefaultLiteral); // TODO
			
			// eReference.setResolveProxies(...); // TODO: this is a little like Hibernate lazy loading.
			
			// eReference.setLowerBound( ... set by linkSemanticsType ...);


			((List<? super EReference>)domainClass.getEClass().getEStructuralFeatures()).add(eReference);

			serializer.setReferenceAccessor(eReference, method);

//			if (!getProgrammingModel().isLink(methods[i])) // simple or composite.
//				continue;
			
			// TODO: resolveProxies

		}
	}

	
	
	public void identifyAssociatorsAndDissociators(DomainClass runtimeDomainClass) {
		Class<?> javaClass = ((RuntimeClientClassBinding)runtimeDomainClass.getBinding()).getJavaClass();
		Method[] methods = javaClass.getMethods();
		for(IDomainClass.IReference iReference: runtimeDomainClass.iReferences()) {
			EReference eReference = iReference.getEReference();
			Method associatorMethod = null;
			Method dissociatorMethod = null;
			for(int i=0; i<methods.length; i++) {
				if (getRuntimeStandardProgModelRules().isAssociatorFor(methods[i], eReference)) {
					associatorMethod = methods[i];
					break;
				}
			}
			if (associatorMethod == null) {
				continue;
			}
			for(int i=0; i<methods.length; i++) {
				if (getRuntimeStandardProgModelRules().isDissociatorFor(methods[i], eReference)) {
					dissociatorMethod = methods[i];
					break;
				}
			}
			if (dissociatorMethod == null) {
				continue;
			}
			// has both an associator and a dissociator method for this reference
			eReference.setChangeable(true);
			
			if (eReference.isMany()) {
				serializer.setReferenceCollectionAssociator(eReference, associatorMethod);
				serializer.setReferenceCollectionDissociator(eReference, dissociatorMethod);
			} else {
				serializer.setReferenceOneToOneAssociator(eReference, associatorMethod);
				serializer.setReferenceOneToOneDissociator(eReference, dissociatorMethod);
			}
		}
	}


	public void identifyOppositeReferences(IDomainClass domainClass) {
		new OppositeReferencesIdentifier(((DomainClass)domainClass)).identify();
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
	public void identifyOperations(DomainClass runtimeDomainClass) {
		Class<?> javaClass = ((RuntimeClientClassBinding)runtimeDomainClass.getBinding()).getJavaClass();
		Method[] methods = javaClass.getMethods();

		eachMethod: 
		for(int i=0; i<methods.length; i++) {
			final Method method = methods[i];
			if ((method.getModifiers() & Method.PUBLIC) != Method.PUBLIC) {
				continue;
			}
			if (getRuntimeStandardProgModelRules().isReserved(method)) {
				continue;
			}
			if (getRuntimeStandardProgModelRules().isAccessor(method) ||
				getRuntimeStandardProgModelRules().isMutator(method)) {
				continue;
			}
			if (getRuntimeStandardProgModelRules().isReference(method)) {
				continue;
			}
			if (method.getAnnotation(Programmatic.class) != null) {
				continue;
			}
			
			Class<?> returnType = method.getReturnType();
			boolean returnsValue = getStandardProgModelRules().isValueType(returnType);
			boolean returnsReference = getStandardProgModelRules().isReferenceType(returnType);
			boolean returnsVoid = getJavaProgModelRules().isVoid(returnType);
			
			if (!returnsValue && !returnsReference && !returnsVoid) {
				continue;
			}
			
			Class<?>[] parameterTypes = method.getParameterTypes();
			Annotation[][] parameterAnnotationSets = method.getParameterAnnotations();
			for(Class<?> parameterType: parameterTypes) {
				if (!getStandardProgModelRules().isValueType(parameterType) &&
					!getStandardProgModelRules().isReferenceType(parameterType)) {
					continue eachMethod;
				}
			}
			
			EOperation eOperation = EcoreFactory.eINSTANCE.createEOperation();
			eOperation.setName(method.getName());

			if (returnsValue) {
				EDataType returnDataType = getEDataTypeFor(returnType);
				eOperation.setEType(returnDataType);
			} else if (returnsReference) {
				IDomainClass returnDomainClass = runtimeDomainClass.getDomain().lookup(returnType);
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
					parameterDomainClass = runtimeDomainClass.getDomain().lookup(parameterType);
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
					serializer.setDescribedAs(eParameter, describedAs);
				}
				
			}

			if ((methods[i].getModifiers() & Modifier.STATIC) == Modifier.STATIC) {
				serializer.setOperationStatic(eOperation);
			}
			
			((List<? super EOperation>)runtimeDomainClass.getEClass().getEOperations()).add(eOperation);
			
			// these are supported by EMF, but not (yet) by our metamodel.
//			eOperation.setLowerBound(..);
//			eOperation.setUpperBound(..);
//			eOperation.setOrdered(..);
//			eOperation.setUnique(..);
//			eOperation.setOrdered(..);
			serializer.setOperationMethod(eOperation, methods[i]);
		}
	}


	
	///////////////////////////////////////////////////////////////////////
	
	/**
	 * indicates whether supplied accessor and mutator methods are compatible, 
	 * that is, that they have the same type and the same name.
	 *
	 * <p>
	 * TODO: not actually used yet, but should be overloaded to also check for
	 * unset and isUnset methods.  
	 *
	 * <p>
	 * TODO: not currently used, so may not work as advertised...
	 *  
	 * @param accessor
	 * @param mutator
	 * @return
	 */
	private final boolean areAttributeMethodsCompatible(final Method accessor, final Method mutator) {
		getRuntimeStandardProgModelRules().assertAccessor(accessor);  // TODO: precondition aspect
		getRuntimeStandardProgModelRules().assertMutator(mutator);  // TODO: precondition aspect
		// check return type of accessor to the type 
		// of the first parameter of the mutator
		if (!accessor.getReturnType().equals(mutator.getParameterTypes()[0])) {
			return false;
		}
		String accessorName = getRuntimeStandardProgModelRules().deriveAttributeName(accessor);
		String mutatorName = getRuntimeStandardProgModelRules().deriveAttributeName(mutator);
		if (!accessorName.equals(mutatorName)) {
			return false;
		}
		return true;
	}


	/**
	 * indicates whether supplied associator and dissociator are compatible,
	 * that is, that they have the same type and the same name.
	 * 
	 * <p>
	 * TODO: not currently used, so may not work as advertised...
	 *  
	 * @param associator
	 * @param dissociator
	 * @return
	 */
	private final boolean isReferencePairCompatible(final Method associator, final Method dissociator) {
		getRuntimeStandardProgModelRules().assertAssociator(associator); // TODO: precondition aspect
		getRuntimeStandardProgModelRules().assertDissociator(dissociator);  // TODO: precondition aspect
		if (associator.getParameterTypes()[0] != dissociator.getParameterTypes()[0]) {
			return false;
		}
		String deriveReferenceNameForAssociator = getRuntimeStandardProgModelRules().deriveReferenceName(associator);
		String deriveReferenceNameForDissociator = getRuntimeStandardProgModelRules().deriveReferenceName(dissociator);
		if (!deriveReferenceNameForAssociator.equals(deriveReferenceNameForDissociator)) {
			return false;
		}
		return true;
	}



	private EDataType getEDataTypeFor(Class<?> dataType) {
		if (!getStandardProgModelRules().isValueType(dataType)) {
			return null;
		}
		if (getJavaProgModelRules().isCoreValueType(dataType)) {
			return emf.getEDataTypeFor(dataType, null);
		}
		String domainName = 
			getStandardProgModelRules().getValueTypeDomainName(dataType);
		IDomain domain = Domain.instance(domainName);
					
		return emf.getEDataTypeFor(dataType, domain.getResourceSet());
	}
	

	private boolean isAttribute(Method method) {
		return (getRuntimeStandardProgModelRules().isAccessor(method) || 
				getRuntimeStandardProgModelRules().isMutator(method)) &&
				getRuntimeStandardProgModelRules().isValueType(method.getReturnType());
	}


	public JavaRules getJavaProgModelRules() {
		return _javaRules;
	}

	public EssentialProgModelStandardSemanticsRules getStandardProgModelRules() {
		return _standardRuntimeRules;
	}
	
	// TODO: should be using covariance
	public EssentialProgModelRuntimeRules getRuntimeStandardProgModelRules() {
		return (EssentialProgModelRuntimeRules)getStandardProgModelRules();
	}

	public void identifyOppositeReferencesFor(IDomainClass domainClass) {
		new OppositeReferencesIdentifier((DomainClass)domainClass).identify();		
	}

}
