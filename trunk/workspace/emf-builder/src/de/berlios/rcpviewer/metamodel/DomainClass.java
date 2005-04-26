package de.berlios.rcpviewer.metamodel;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.impl.EPackageRegistryImpl;
import org.eclipse.emf.ecore.util.EcoreEList;
import org.eclipse.emf.ecore.util.EcoreSwitch;
import org.eclipse.emf.ecore.util.EcoreUtil;

import de.berlios.rcpviewer.metamodel.annotations.Derived;
import de.berlios.rcpviewer.metamodel.annotations.LowerBoundOf;
import de.berlios.rcpviewer.metamodel.annotations.Ordered;
import de.berlios.rcpviewer.metamodel.annotations.Unique;
import de.berlios.rcpviewer.metamodel.annotations.UpperBoundOf;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

//import de.berlios.rcpviewer.metamodel.impl.ActionImpl;
//import de.berlios.rcpviewer.metamodel.impl.AttributeImpl;
//import de.berlios.rcpviewer.metamodel.impl.LinkImpl;


/**
 * Represents a class in the meta model, akin to {@link java.lang.Class} and
 * wrapping an underlying EMF EClass.
 * 
 * TODO: should delegate much more to ProgrammingModel
 * TODO: should be part of the implementation of a specific programming model 
 * TODO: should implement the choreography of interacting with the underlying POJOs (or this could be done by DomainObject).
 */
public final class DomainClass 
		implements IDomainClass,
				   IProgrammingModelAware,
				   EmfFacadeAware {

	/**
	 * Presence of an EAnnotation with this source on an EAttribute indicates 
	 * that the EAttribute is write-only (has a mutator, no accessor).
	 * 
	 * TODO: apparently this should be a URI.
	 */
	public final static String ANNOTATION_SOURCE_WRITE_ONLY_ATTRIBUTE = 
							"de.berlios.rcpviewer.metamodel.writeOnly";
	
	DomainClass(final Class javaClass) {
		this.javaClass = javaClass;
		Package javaPackage = javaClass.getPackage();
		this.eClass = EcoreFactory.eINSTANCE.createEClass();
		eClass.setInstanceClass(javaClass);
		eClass.setName(javaClass.getSimpleName());

		EPackage ePackage = EPackage.Registry.INSTANCE.getEPackage(javaPackage.getName());
		if (ePackage == null) {
			ePackage = EcoreFactory.eINSTANCE.createEPackage();
			ePackage.getEClassifiers().add(this.eClass);
			ePackage.setName(javaPackage.getName());
		}

		Method[] methods = javaClass.getMethods();

		identifyAccessors();
		identifyMutators();
		identifyUnSettableAttributes();
		identifyActions();

	}

	private final Class javaClass;
	public Class getJavaClass() {
		return javaClass;
	}
	
	private final EClass eClass;
	public EClass getEClass() {
		return eClass;
	}

	public String toString() { return "DomainClass.javaClass = " + javaClass ; }
	
	
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
				eAnnotation.setSource(ANNOTATION_SOURCE_WRITE_ONLY_ATTRIBUTE);
				eAnnotation.setEModelElement(eAttribute);
//				EAnnotation justChecking = eAttribute.getEAnnotation(ANNOTATION_SOURCE_WRITE_ONLY_ATTRIBUTE);
//				assert justChecking != null;
//				assert eAnnotation == justChecking;
			} 
			
//			eAttribute.setDefaultValueLiteral(defaultValueAsString); // TODO: read from annotation
		}
	}
	

	/**
	 * TODO: not yet doing anything. 
	 */
	private void identifyUnSettableAttributes() {
		
		Method[] methods = javaClass.getMethods();
		for(int i=0; i<methods.length; i++) {
			if (!getProgrammingModel().isUnSettableAttribute(methods[i])) {
				continue;
			}
						
			String attributeName = getProgrammingModel().deriveAttributeName(methods[i]);
			EAttribute eAttribute = getEAttributeNamed(attributeName);
			if (eAttribute == null) {
				logWarning("isUnsetXxx found without getXxx or isXxx for '" + attributeName + "'");
				return;
			}

			if (!eAttribute.isChangeable()) {
				logWarning("isUnsetXxx found on unchangeable attribute '" + attributeName + "'");
				return;
			}
			if (eAttribute == null) {
				logWarning("unsetXxx found without setXxx for '" + attributeName + "'");
				return;
			}
			
			eAttribute.setUnsettable(true);
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
		for(Iterator iter = getEClass().getEAllAttributes().iterator();
		    iter.hasNext(); ) {
			EAttribute eAttribute = (EAttribute)iter.next();
			if (eAttribute.getName().equals(attributeName)) {
				return eAttribute;
			}
		}
		return null;
	}

	public boolean isWriteOnly(EAttribute eAttribute) {
		return eAttribute.getEAnnotation(ANNOTATION_SOURCE_WRITE_ONLY_ATTRIBUTE) != null;
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

	public boolean containsAttribute(EAttribute eAttribute) {
		return this.eClass.getEAllAttributes().contains(eAttribute);
	}

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

	/**
	 * Setting up for AspectJ introduction of logging infrastructure.
	 * @param message
	 */
	private void logWarning(String message) {}
	
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
