package de.berlios.rcpviewer.metamodel;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.impl.EPackageRegistryImpl;
import org.eclipse.emf.ecore.util.EcoreEList;
import org.eclipse.emf.ecore.util.EcoreSwitch;
import org.eclipse.emf.ecore.util.EcoreUtil;

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
 * Akin to {@link java.lang.Class}.
 * TODO: need some logic to ensure that the namespace of members is unique.
 */
public final class DomainClass 
		implements IDomainClass, 
				   IProgrammingModelAware,
				   EmfFacadeAware {

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
				
//			eAttribute.setDefaultValueLiteral(defaultValueAsString); // TODO: read from annotation
//			eAttribute.setDerived(whetherDerived);  // TODO: read from annotation

			Class<?> dataType = getProgrammingModel().accessorType(methods[i]);
			
			EDataType eDataType = getEmfFacade().getEDataTypeFor(dataType);
			eDataType = getEmfFacade().getEDataTypeFor(dataType);
			eAttribute.setEType(eDataType);
			
//			eAttribute.setID(trueOrFalse); // not sure
			String attributeName = getProgrammingModel().deriveAttributeName(methods[i]);
			eAttribute.setName(attributeName);
			
			eAttribute.setChangeable(false); // if find a mutator, make changeable
//			eAttribute.setTransient(whetherTransient); // TODO: read as 'derived' from annotation
//			eAttribute.setVolatile(whetherVolatile);   // TODO: read as 'derived' from annotation; must also be non-changeable
			
			eAttribute.setLowerBound(1); // TODO: read instead from annotation
//			eAttribute.setUpperBound(upperBound); // TODO: read from annotation (need to support arrays)

//			eAttribute.setUnique(whetherUnique); // TODO:
//			eAttribute.setOrdered(whetherOrdered); // TODO:
			
			eClass.getEStructuralFeatures().add(eAttribute);
		}
		int numberOfAttributes = eClass.getEAttributes().size();
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



	/**
	 * searches for mutators of value types; either update existing attribute
	 * (ie read/write) or write-only 
     *
     * TODO
	 */
	private void identifyMutators() {

		Method[] methods = javaClass.getMethods();

		for(int i=0; i<methods.length; i++) {
			if (!getProgrammingModel().isMutator(methods[i])) {
				continue;
			}

			// needs converting to use EMF
//			Class type = methods[i].getParameterTypes()[0];
//			if (!Util.isValueType(type)) {
//				continue;
//			}
//			String attributeName = AttributeImpl.deriveAttributeName(methods[i]);
//			AttributeImpl existingAttribute =
//				(AttributeImpl)attributesByName.get(attributeName);
//			if (existingAttribute != null) {
//				if (!attributeName.equals(existingAttribute.getName())) {
//					continue;
//				}
//				if (!type.equals(existingAttribute.getDataType())) {
//					continue;
//				}
//				existingAttribute.initMutator(methods[i]);
//			} else {
//				Attribute attribute = 
//					AttributeFactory.instance().attributeWithMutator(methods[i]);
//				if (attribute != null) {
//					attributesByName.put(attributeName, attribute);
//				}
//			}
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

		}
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
	// DEPENDENCY INJECTION END
	
}
