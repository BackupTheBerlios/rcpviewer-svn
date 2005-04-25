package de.berlios.rcpviewer.metamodel;


import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import de.berlios.rcpviewer.metamodel.annotations.*;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

/**
 * Collection of methods to access various EMF-related functionality.
 * 
 * <p>
 * Stateless class; an alternative design would have been a collection of 
 * static methods.
 */
public class EmfFacade {

	private final ResourceSet resourceSet;
	public final Map<Class,EClassifier> coreDataTypes; 

	public EmfFacade() {
		// seems to cause the core package registry to get populated
		EcoreFactory.eINSTANCE.createEPackage();
		resourceSet = new ResourceSetImpl();
		coreDataTypes = new HashMap<Class,EClassifier>() {
			{
				put(java.math.BigDecimal.class, getEcorePackage().getEClassifier("EBigDecimal"));
				put(java.math.BigInteger.class, getEcorePackage().getEClassifier("EBigInteger"));
				put(boolean.class, getEcorePackage().getEClassifier("EBoolean"));
				put(java.lang.Boolean.class, getEcorePackage().getEClassifier("EBooleanObject"));
				put(byte.class, getEcorePackage().getEClassifier("EByte"));
				put(byte[].class, getEcorePackage().getEClassifier("EByteArray"));
				put(java.lang.Byte.class, getEcorePackage().getEClassifier("EByteObject"));
				put(char.class, getEcorePackage().getEClassifier("EChar"));
				put(java.lang.Character.class, getEcorePackage().getEClassifier("ECharacterObject"));
				put(java.util.Date.class, getEcorePackage().getEClassifier("EDate"));
				put(double.class, getEcorePackage().getEClassifier("EDouble"));
				put(java.lang.Double.class, getEcorePackage().getEClassifier("EDoubleObject"));
				put(float.class, getEcorePackage().getEClassifier("EFloat"));
				put(java.lang.Float.class, getEcorePackage().getEClassifier("EFloatObject"));
				put(int.class, getEcorePackage().getEClassifier("EInt"));
				put(java.lang.Integer.class, getEcorePackage().getEClassifier("EIntegerObject"));
				put(long.class, getEcorePackage().getEClassifier("ELong"));
				put(java.lang.Long.class, getEcorePackage().getEClassifier("ELongObject"));
				put(short.class, getEcorePackage().getEClassifier("EShort"));
				put(java.lang.Short.class, getEcorePackage().getEClassifier("EShortObject"));
				put(java.lang.String.class, getEcorePackage().getEClassifier("EString"));
			}
		};
	}
	
	public final EPackage getEcorePackage() {
		return EPackage.Registry.INSTANCE.getEPackage("http://www.eclipse.org/emf/2002/Ecore");
	}

	public EPackage getEPackageFor(Package javaPackage) {
		if (javaPackage == null) {
			return EPackage.Registry.INSTANCE.getEPackage("http://www.eclipse.org/emf/2002/Ecore");
		}
		EPackage ePackage = findPackageWithName(javaPackage.getName()); 
		if (ePackage == null) {
			ePackage = EcoreFactory.eINSTANCE.createEPackage();
			ePackage.setName(javaPackage.getName());
			ePackage.setNsURI("http://de.berlios.rcpviewer.metamodel/2005/" + javaPackage.getName());
		}
		resourceSet.getPackageRegistry().put(ePackage.getNsURI(), ePackage);
		return ePackage;
	}
	/**
	 * searches in global registry and our resource set's registry. 
	 * @param packageName
	 * @return the package, or null if cannot be found
	 */
	public EPackage findPackageWithName(String packageName) {
		EPackage ePackage = findPackageInRegistryWithName(
								EPackage.Registry.INSTANCE, packageName); 
		if (ePackage == null) {
			ePackage = findPackageInRegistryWithName(
						resourceSet.getPackageRegistry(), packageName);
		}
		return ePackage;
	}
	
	private EPackage findPackageInRegistryWithName(EPackage.Registry ePackageRegistry, String packageName) {
		Set<Map.Entry> ePackageEntrySet = ePackageRegistry.entrySet();  
		for(Map.Entry ePackageEntry: ePackageEntrySet) {
			String ePackageNsUri = (String)ePackageEntry.getKey();
			EPackage ePackage = (EPackage)ePackageEntry.getValue();
			if (ePackage.getName().equals(packageName)) {
				return ePackage;
			}
		}
		return null;
	}

	public EDataType getEDataTypeFor(Class<?> javaDataType) {
		EClassifier builtInDataType = coreDataTypes.get(javaDataType);
		if (builtInDataType != null && builtInDataType instanceof EDataType) {
			return (EDataType)builtInDataType;
		}
		
		Package javaPackage = javaDataType.getPackage();
		EPackage ePackage = getEPackageFor(javaPackage);
		for(Object eClassifierAsObject: ePackage.getEClassifiers()) {
			EClassifier eClassifier = (EClassifier)eClassifierAsObject;
			if (eClassifier.getInstanceClass() == javaDataType) {
				return (EDataType)eClassifier;
			}
		}
		
		if (!ValueMarker.class.isAssignableFrom(javaDataType)) {
			throw new IllegalArgumentException("Not a built-in, and is not a Value");
		}
		EDataType eDataType = EcoreFactory.eINSTANCE.createEDataType();
		eDataType.setInstanceClass(javaDataType);
		eDataType.setName(javaDataType.getName());
		eDataType.setSerializable(true);
		ePackage.getEClassifiers().add(eDataType);
		
		return eDataType;
	}

}
