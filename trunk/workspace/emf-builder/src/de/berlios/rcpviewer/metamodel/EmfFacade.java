package de.berlios.rcpviewer.metamodel;


import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import de.berlios.rcpviewer.progmodel.standard.impl.ValueMarker;

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
 * 
 * @author Dan Haywood
 */
public class EmfFacade {

	private final ResourceSet resourceSet;
	public final Map<Class,EDataTypeData> coreDataTypes; 

	/**
	 * Wraps an EDataType to allow additional semantics to be provided.
	 * @author Dan Haywood
	 *
	 */
	final static class EDataTypeData {
		private final EDataType eDataType;
		public EDataType getEDataType() {
			return eDataType;
		}
		private final boolean isUnsettable;
		public boolean getIsUnsettable() {
			return isUnsettable;
		}
		EDataTypeData(EClassifier eClassifier, boolean isUnsettable) {
			this.eDataType = (EDataType)eClassifier;
			this.isUnsettable = isUnsettable; 
		}
	}
	public EmfFacade() {
		// seems to cause the core package registry to get populated
		EcoreFactory.eINSTANCE.createEPackage();
		resourceSet = new ResourceSetImpl();
		
//		for(Object eClassifier: getEcorePackage().getEClassifiers()) {
//			System.out.println(((EClassifier)eClassifier).toString());
//		}
		
		coreDataTypes = new HashMap<Class,EDataTypeData>() {
			{
				put(java.math.BigDecimal.class, 
						new EDataTypeData(getEcorePackage().getEClassifier("EBigDecimal"), true));
				put(java.math.BigInteger.class, 
						new EDataTypeData(getEcorePackage().getEClassifier("EBigInteger"), true));
				put(boolean.class, 
						new EDataTypeData(getEcorePackage().getEClassifier("EBoolean"), false));
				put(java.lang.Boolean.class, 
						new EDataTypeData(getEcorePackage().getEClassifier("EBooleanObject"), true));
				put(byte.class, 
						new EDataTypeData(getEcorePackage().getEClassifier("EByte"), true));
				put(byte[].class, 
						new EDataTypeData(getEcorePackage().getEClassifier("EByteArray"), true));
				put(java.lang.Byte.class, 
						new EDataTypeData(getEcorePackage().getEClassifier("EByteObject"), true));
				put(char.class, 
						new EDataTypeData(getEcorePackage().getEClassifier("EChar"), false));
				put(java.lang.Character.class, 
						new EDataTypeData(getEcorePackage().getEClassifier("ECharacterObject"), false));
				put(java.util.Date.class, 
						new EDataTypeData(getEcorePackage().getEClassifier("EDate"), true));
				put(double.class, 
						new EDataTypeData(getEcorePackage().getEClassifier("EDouble"), false));
				put(java.lang.Double.class, 
						new EDataTypeData(getEcorePackage().getEClassifier("EDoubleObject"), true));
				put(float.class, 
						new EDataTypeData(getEcorePackage().getEClassifier("EFloat"), false));
				put(java.lang.Float.class, 
						new EDataTypeData(getEcorePackage().getEClassifier("EFloatObject"), true));
				put(int.class, 
						new EDataTypeData(getEcorePackage().getEClassifier("EInt"), false));
				put(java.lang.Integer.class, 
						new EDataTypeData(getEcorePackage().getEClassifier("EIntegerObject"), true));
				put(long.class, 
						new EDataTypeData(getEcorePackage().getEClassifier("ELong"), false));
				put(java.lang.Long.class, 
						new EDataTypeData(getEcorePackage().getEClassifier("ELongObject"), true));
				put(short.class, 
						new EDataTypeData(getEcorePackage().getEClassifier("EShort"), false));
				put(java.lang.Short.class, 
						new EDataTypeData(getEcorePackage().getEClassifier("EShortObject"), true));
				put(java.lang.String.class, 
						new EDataTypeData(getEcorePackage().getEClassifier("EString"), true));
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
		EDataTypeData coreDataTypeData = coreDataTypes.get(javaDataType); 
		if (coreDataTypeData != null) {
			return coreDataTypeData.getEDataType();
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
			throw new IllegalArgumentException("Java datatype '" + javaDataType + "' is not a built-in, and is not a Value");
		}
		EDataType eDataType = EcoreFactory.eINSTANCE.createEDataType();
		eDataType.setInstanceClass(javaDataType);
		eDataType.setName(javaDataType.getName());
		eDataType.setSerializable(true);
		ePackage.getEClassifiers().add(eDataType);
		
		return eDataType;
	}

	/**
	 * Whether the EDataType corresponding to the java dataType is unsettable.
	 * 
	 * <p>
	 * Some built-ins are, some are not.  If the datatype is a user-defined datatype,
	 * then assumes is unsettable.
	 * 
	 * 
	 * @param javaDataType
	 * @return
	 */
	public boolean isIsUnsettableFor(Class<?> javaDataType) {
		EDataTypeData builtInDataTypeData = coreDataTypes.get(javaDataType); 
		if (builtInDataTypeData != null) {
			return builtInDataTypeData.getIsUnsettable();
		}
		
		Package javaPackage = javaDataType.getPackage();
		EPackage ePackage = getEPackageFor(javaPackage);
		for(Object eClassifierAsObject: ePackage.getEClassifiers()) {
			EClassifier eClassifier = (EClassifier)eClassifierAsObject;
			if (eClassifier.getInstanceClass() == javaDataType) {
				return true;
			}
		}
		
		throw new IllegalArgumentException("Not a built-in, and is not a Value");
	}

}
