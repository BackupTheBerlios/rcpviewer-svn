package de.berlios.rcpviewer.progmodel.standard;

import org.eclipse.emf.ecore.*;

import de.berlios.rcpviewer.metamodel.DomainClassRegistry;
import de.berlios.rcpviewer.metamodel.EmfFacade;
import de.berlios.rcpviewer.metamodel.EmfFacadeAware;
import de.berlios.rcpviewer.metamodel.IDomainClass;
import de.berlios.rcpviewer.metamodel.IDomainObject;
import de.berlios.rcpviewer.progmodel.IProgrammingModel;
import de.berlios.rcpviewer.progmodel.IProgrammingModelAware;
import de.berlios.rcpviewer.progmodel.ProgrammingModelException;
import de.berlios.rcpviewer.session.IWrapper;
import de.berlios.rcpviewer.session.IWrapperAware;
import de.berlios.rcpviewer.session.local.Session;

import java.lang.reflect.Method;
import java.util.ArrayList;

import java.util.List;


/**
 * Represents a class in the meta model, akin to {@link java.lang.Class} and
 * wrapping an underlying EMF EClass.
 * 
 * TODO: should delegate much more to ProgrammingModel
 * TODO: should be part of the implementation of a specific programming model 
 * TODO: should implement the choreography of interacting with the underlying POJOs (or this could be done by DomainObject).
 * 
 * @author Dan Haywood
 */
public final class DomainClass<T> 
		implements IDomainClass,
				   IProgrammingModelAware,
				   EmfFacadeAware,
				   IWrapperAware {
	
	public DomainClass(final Class<T> javaClass) {
		
		this.javaClass = javaClass;

		identifyClass();

		Method[] methods = javaClass.getMethods();

		identifyAccessors();
		identifyMutators();
		identifyUnSettableAttributes();
		identifyActions();

	}

	private final Class<T> javaClass;
	public Class<T> getJavaClass() {
		return javaClass;
	}
	
	private EClass eClass;
	public EClass getEClass() {
		return eClass;
	}

	public String toString() { return "DomainClass.javaClass = " + javaClass ; }
	

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
		eClass.setName(javaClass.getSimpleName());
	}


	
	/**
	 * 
	 * @param methods
	 * @param attributesByName
	 */
	private void identifyAccessors() {
		
		Method[] methods = javaClass.getMethods();
		// search for accessors of value types
		// initially all attributes start off read-only
		for(int i=0; i<methods.length; i++) {
			if (!getProgrammingModel().representsAttribute(methods[i])) {
				continue;
			}
			
			EAttribute eAttribute = EcoreFactory.eINSTANCE.createEAttribute();
			Class<?> dataType = getProgrammingModel().accessorType(methods[i]);
			EDataType eDataType = getEmfFacade().getEDataTypeFor(dataType);
			eAttribute.setEType(eDataType);
			String attributeName = getProgrammingModel().deriveAttributeName(methods[i]);
			eAttribute.setName(attributeName);
			eClass.getEStructuralFeatures().add(eAttribute);

			putMethodNameIn(
					methodAnnotationFor(eAttribute), 
					Constants.ANNOTATION_ATTRIBUTE_ACCESSOR_METHOD_NAME_KEY, 
					methods[i].getName());
			
			eAttribute.setChangeable(false); // if find a mutator, make changeable
			Derived derivedAnnotation = methods[i].getAnnotation(Derived.class);
			boolean whetherDerived = derivedAnnotation != null;
			
			eAttribute.setDerived(whetherDerived);
			eAttribute.setTransient(whetherDerived);
			eAttribute.setVolatile(whetherDerived);

			// TODO: should check that the datatype supports lower bounds
			LowerBoundOf lowerBoundOfAnnotation = 
				methods[i].getAnnotation(LowerBoundOf.class);
			if (lowerBoundOfAnnotation != null) {
				eAttribute.setLowerBound(lowerBoundOfAnnotation.value());
			} else {
				eAttribute.setLowerBound(1);
			}

			// TODO: should check that the datatype supports upper bounds
			UpperBoundOf upperBoundOfAnnotation = 
				methods[i].getAnnotation(UpperBoundOf.class);
			if (upperBoundOfAnnotation != null) {
				int upperBound = upperBoundOfAnnotation.value();
				eAttribute.setUpperBound(upperBound);
				if (upperBound > 1) {
					Unique uniqueAnnotation = 
						methods[i].getAnnotation(Unique.class);
					if (uniqueAnnotation != null) {
						eAttribute.setUnique(uniqueAnnotation.value());
					}
					Ordered orderedAnnotation = 
						methods[i].getAnnotation(Ordered.class);
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
     * TODO
	 */
	private void identifyMutators() {

		Method[] methods = javaClass.getMethods();

		for(int i=0; i<methods.length; i++) {
			if (!getProgrammingModel().isMutator(methods[i])) {
				continue;
			}

			String attributeName = getProgrammingModel().deriveAttributeName(methods[i]);
			EAttribute eAttribute = getEAttributeNamed(attributeName);

			if (eAttribute != null) {
				eAttribute.setChangeable(true);
			} else {
				eAttribute = EcoreFactory.eINSTANCE.createEAttribute();
				Class<?> dataType = getProgrammingModel().mutatorType(methods[i]);
				EDataType eDataType = getEmfFacade().getEDataTypeFor(dataType);
				eAttribute.setEType(eDataType);
				eAttribute.setName(attributeName);

				eClass.getEStructuralFeatures().add(eAttribute);

				EAnnotation eAnnotation = EcoreFactory.eINSTANCE.createEAnnotation();
				eAnnotation.setSource(de.berlios.rcpviewer.metamodel.Constants.ANNOTATION_ATTRIBUTE_WRITE_ONLY);
				eAnnotation.setEModelElement(eAttribute);
			}
			
			putMethodNameIn(
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
		for(EAttribute eAttribute: allAttributes()) {
			for(int i=0; i<methods.length; i++) {
				if (getProgrammingModel().isIsUnsetMethodFor(methods[i], eAttribute)) {
					isUnsetMethod = methods[i];
					break;
				}
			}
			if (isUnsetMethod == null) {
				continue;
			}
			for(int i=0; i<methods.length; i++) {
				if (getProgrammingModel().isUnsetMethodFor(methods[i], eAttribute)) {
					unsetMethod = methods[i];
					break;
				}
			}
			if (unsetMethod == null) {
				continue;
			}
			// has both an IsUnset and an unset method for this attribute
			eAttribute.setUnsettable(true);
			
			putMethodNameIn(
					methodAnnotationFor(eAttribute), 
					Constants.ANNOTATION_ATTRIBUTE_IS_UNSET_METHOD_NAME_KEY, 
					isUnsetMethod.getName());
			
			putMethodNameIn(
					methodAnnotationFor(eAttribute), 
					Constants.ANNOTATION_ATTRIBUTE_UNSET_METHOD_NAME_KEY, 
					isUnsetMethod.getName());
			

		}
	}
	



	/**
	 * TODO
	 */
	private void identifyActions() {
		Method[] methods = javaClass.getMethods();

//		for(int i=0; i<methods.length; i++) {
//			if (!getProgrammingModel().isAction(methods[i]))
//				continue;
//		}
	}

	


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

	public int getNumberOfAttributes() {
		return getEClass().getEAttributes().size();
	}

	public EAttribute getEAttributeNamed(String attributeName) {
		for(EAttribute eAttribute: allAttributes() ) {
			if (eAttribute.getName().equals(attributeName)) {
				return eAttribute;
			}
		}
		return null;
	}
	
	public boolean isWriteOnly(EAttribute eAttribute) {
		return eAttribute.getEAnnotation(de.berlios.rcpviewer.metamodel.Constants.ANNOTATION_ATTRIBUTE_WRITE_ONLY) != null;
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

	public List<EAttribute> allAttributes() {
		List<EAttribute> eAttributes = new ArrayList<EAttribute>();
		eAttributes.addAll(getEClass().getEAllAttributes());
		return eAttributes;
	}
	
	public List<EAttribute> attributes() {
		List<EAttribute> eAttributes = new ArrayList<EAttribute>();
		eAttributes.addAll(getEClass().getEAttributes());
		return eAttributes;
	}

	public boolean containsAttribute(EAttribute eAttribute) {
		return this.eClass.getEAllAttributes().contains(eAttribute);
	}

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

	EAnnotation methodAnnotationFor(EModelElement eModelElement) {
		EAnnotation eAnnotation = 
			eModelElement.getEAnnotation(de.berlios.rcpviewer.metamodel.Constants.ANNOTATION_SOURCE_METHOD_NAMES);
		if (eAnnotation != null) {
			return eAnnotation;
		}
		eAnnotation = EcoreFactory.eINSTANCE.createEAnnotation();
		eAnnotation.setSource(de.berlios.rcpviewer.metamodel.Constants.ANNOTATION_SOURCE_METHOD_NAMES);
		eAnnotation.setEModelElement(eModelElement);
		return eAnnotation;
	}
	
	EAnnotation putMethodNameIn(EAnnotation eAnnotation, String methodKey, String methodName) {
		if (eAnnotation == null) {
			return null;
		}
		eAnnotation.getDetails().put(methodKey, methodName);
		return eAnnotation;
	}
	
	String getMethodNameFrom(EAnnotation eAnnotation, String methodKey) {
		if (eAnnotation == null) {
			return null;
		}
		return (String)eAnnotation.getDetails().get(methodKey);
	}
	

	/**
	 * Setting up for AspectJ introduction of logging infrastructure.
	 * @param message
	 */
	private void logWarning(String message) {}
	

	// DEPENDENCY INJECTION
	
	private IProgrammingModel programmingModel;
	public IProgrammingModel getProgrammingModel() {
		return programmingModel;
	}
	public void setProgrammingModel(IProgrammingModel programmingModel) {
		this.programmingModel = programmingModel;
	}

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
	 * Cross-cutting validation of any eAttributes passed to us in the
	 * various convenience methods.
	 * 
	 * don't worry about the red squigglies: AJDT has not yet taught the
	 * Java editor about inner aspects.
	 */
	// 
	private static aspect EnsureAttributeBelongsToDomainClassAspect {

		pointcut convenienceMethod(DomainClass domainClass, EAttribute eAttribute):
			execution(* *..DomainClass+.*(EAttribute, ..)) &&
			!execution(* *..DomainClass+.containsAttribute(EAttribute)) &&
			this(domainClass) && args(eAttribute, ..);
		
		before(DomainClass domainClass, EAttribute eAttribute): 
			convenienceMethod(domainClass, eAttribute) {
			if (domainClass.containsAttribute(eAttribute)) {
				return;
			}
			throw new IllegalArgumentException(
				"EAttribute '" + eAttribute + "' not part of this DomainClass");
		}
	}



}
