package org.essentialplatform.core.emf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.resource.ResourceSet;

/**
 * EMF facade.
 * 
 * <p>
 * Wraps the core registry (from which we pick up EMF equivalents of primitive
 * types).
 *  
 * @author Dan Haywood
 */
public final class Emf {

	static {
		// seems to cause the core package registry to get populated
		EcoreFactory.eINSTANCE.createEPackage();
	}
	
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

	private final Map<Class,EDataTypeData> coreDataTypes;
	/**
	 * TODO: should probably be combined with coreDataTypes as a bidirectional
	 * map (perhaps using one of the apache commons collections)
	 */
	private final List<EClassifier> integralDataTypes; 

	public Emf() {
		

		coreDataTypes = new HashMap<Class,EDataTypeData>() {
			private static final long serialVersionUID = 1L;
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
		
		integralDataTypes = new ArrayList<EClassifier>() {
			{
				add(getEcorePackage().getEClassifier("EBigInteger"));
				add(getEcorePackage().getEClassifier("EByte"));
				add(getEcorePackage().getEClassifier("EByteObject"));
				add(getEcorePackage().getEClassifier("EInt"));
				add(getEcorePackage().getEClassifier("EIntegerObject"));
				add(getEcorePackage().getEClassifier("ELong"));
				add(getEcorePackage().getEClassifier("ELongObject"));
				add(getEcorePackage().getEClassifier("EShort"));
				add(getEcorePackage().getEClassifier("EShortObject"));
			}
		};
	}

	/**
	 * The javaDataType is presumed to represent a type (primitive or value 
	 * type) that the programming model deems to be a datatype.
	 * 
	 * @param javaDataType
	 * @param resourceSet
	 * @return
	 */
	public EDataType getEDataTypeFor(Class<?> javaDataType, ResourceSet resourceSet) {

		EDataTypeData coreDataTypeData = coreDataTypes.get(javaDataType); 
		if (coreDataTypeData != null) {
			return coreDataTypeData.getEDataType();
		}
		
		Package javaPackage = javaDataType.getPackage();
		EPackage ePackage = getEPackageFor(javaPackage, resourceSet);
		for(Object eClassifierAsObject: ePackage.getEClassifiers()) {
			EClassifier eClassifier = (EClassifier)eClassifierAsObject;
			if (eClassifier.getInstanceClass() == javaDataType) {
				return (EDataType)eClassifier;
			}
		}

		EDataType eDataType = EcoreFactory.eINSTANCE.createEDataType();
		eDataType.setInstanceClass(javaDataType);
		eDataType.setName(javaDataType.getName());
		eDataType.setSerializable(true);
		((List<? super EDataType>)ePackage.getEClassifiers()).add(eDataType);
		
		return eDataType;
	}


	public final EPackage getEcorePackage() {
		return EPackage.Registry.INSTANCE.getEPackage("http://www.eclipse.org/emf/2002/Ecore");
	}

	public Map<Class, EDataTypeData> getCoreDataTypes() {
		return coreDataTypes;
	}
	public List<EClassifier> getIntegralDataTypes() {
		return integralDataTypes;
	}
	
	public EPackage getEPackageFor(Package javaPackage, ResourceSet resourceSet) {
		if (javaPackage == null) {
			return getEcorePackage();
		}
		if (resourceSet == null) {
			throw new IllegalArgumentException("Must supply resourceSet if java package is not null");
		}
		
		EPackage ePackage = findPackageWithName(javaPackage.getName(), resourceSet); 
		if (ePackage == null) {
			ePackage = EcoreFactory.eINSTANCE.createEPackage();
			ePackage.setName(javaPackage.getName());
			ePackage.setNsURI("http://org.essentialplatform.domain/2005/" + javaPackage.getName());
		}
		((Map<? super String,? super EPackage>)resourceSet.getPackageRegistry()).put(ePackage.getNsURI(), ePackage);
		return ePackage;
	}
	/**
	 * searches in global registry and our resource set's registry. 
	 * @param packageName
	 * @return the package, or null if cannot be found
	 */
	public EPackage findPackageWithName(String packageName, ResourceSet resourceSet) {
		EPackage ePackage = null;
		ePackage = findPackageInRegistryWithName(
				EPackage.Registry.INSTANCE, packageName); 
		if (ePackage == null) {
			ePackage = findPackageInRegistryWithName(
					resourceSet.getPackageRegistry(), packageName);
		}
		return ePackage;
	}
	
	private EPackage findPackageInRegistryWithName(EPackage.Registry ePackageRegistry, String packageName) {
		Set<String> packageRegistryKeySet = (Set<String>)ePackageRegistry.keySet();
		// need to take a copy because otherwise getting a concurrent modification exception when
		// running plugin tests (not sure why, didn't bother to investigate.
		Set<String> copyOfPackageRegistryKeySet = new HashSet<String>(packageRegistryKeySet);
		for(String ePackageNsUri: copyOfPackageRegistryKeySet) {
			EPackage ePackage = ePackageRegistry.getEPackage(ePackageNsUri);
			if (ePackage.getName().equals(packageName)) {
				return ePackage;
			}
		}
		return null;
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
	public boolean isIsUnsettableFor(Class<?> javaDataType, ResourceSet resourceSet) {
		EDataTypeData builtInDataTypeData = coreDataTypes.get(javaDataType); 
		if (builtInDataTypeData != null) {
			return builtInDataTypeData.getIsUnsettable();
		}
		
		Package javaPackage = javaDataType.getPackage();
		EPackage ePackage = getEPackageFor(javaPackage, resourceSet);
		for(Object eClassifierAsObject: ePackage.getEClassifiers()) {
			EClassifier eClassifier = (EClassifier)eClassifierAsObject;
			if (eClassifier.getInstanceClass() == javaDataType) {
				return true;
			}
		}
		
		throw new IllegalArgumentException("Not a built-in, and is not a Value");
	}

	public boolean isIntegralNumber(EDataType attributeType) {
		return integralDataTypes.contains(attributeType);
	}


}
