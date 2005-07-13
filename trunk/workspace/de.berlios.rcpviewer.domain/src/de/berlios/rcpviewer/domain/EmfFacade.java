package de.berlios.rcpviewer.domain;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.resource.ResourceSet;

import de.berlios.rcpviewer.progmodel.standard.NamingConventions;
import de.berlios.rcpviewer.progmodel.standard.StandardProgModelConstants;

/**
 * Collection of methods to access various EMF-related functionality.
 * 
 * <p>
 * Wraps the core registry (from which we pick up EMF equivalents of primitive
 * types). 
 * 
 * <p>
 * TODO: shouldn't be using NamingConventions really.
 * 
 * @author Dan Haywood
 */
public class EmfFacade {

	private final Map<Class,EDataTypeData> coreDataTypes; 

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
	}
	
	public final EPackage getEcorePackage() {
		return EPackage.Registry.INSTANCE.getEPackage("http://www.eclipse.org/emf/2002/Ecore");
	}

	public EPackage getEPackageFor(Package javaPackage, ResourceSet resourceSet) {
		if (javaPackage == null) {
			return EPackage.Registry.INSTANCE.getEPackage("http://www.eclipse.org/emf/2002/Ecore");
		}
		if (resourceSet == null) {
			throw new IllegalArgumentException("Must supply resourceSet if java package is not null");
		}
		
		EPackage ePackage = findPackageWithName(javaPackage.getName(), resourceSet); 
		if (ePackage == null) {
			ePackage = EcoreFactory.eINSTANCE.createEPackage();
			ePackage.setName(javaPackage.getName());
			ePackage.setNsURI("http://de.berlios.rcpviewer.domain/2005/" + javaPackage.getName());
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

	public EDataType getEDataTypeFor(Class<?> javaDataType, ResourceSet resourceSet) {

		EDataTypeData coreDataTypeData = coreDataTypes.get(javaDataType); 
		if (coreDataTypeData != null) {
			return coreDataTypeData.getEDataType();
		}
		
		if (!getNamingConventions().isValueType(javaDataType)) {
			return null;
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

	private NamingConventions namingConventions = new NamingConventions();
	private NamingConventions getNamingConventions() {
		return namingConventions;
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

	/**
	 * Returns the {@link EAnnotation} of the supplied source on the model element,
	 * creating it if necessary.
	 * 
	 * @param modelElement
	 * @param annotationSource
	 * @return
	 */
	public EAnnotation annotationOf(EModelElement eModelElement, String annotationSource) {
		EAnnotation eAnnotation = eModelElement.getEAnnotation(annotationSource);
		if (eAnnotation == null) {
			eAnnotation = EcoreFactory.eINSTANCE.createEAnnotation();
			eAnnotation.setSource(annotationSource);
			eAnnotation.setEModelElement(eModelElement);
		}
		return eAnnotation;
	}
	

	public EAnnotation putAnnotationDetails(EAnnotation eAnnotation, Map<String, String> details) {
		eAnnotation.getDetails().putAll(details);
		return eAnnotation;
	}

	public void putAnnotationDetails(EModelElement eModelElement, String annotationSource, Map<String, String> details) {
		annotationOf(eModelElement, annotationSource).getDetails().putAll(details);
	}

	public Map<String, String> getAnnotationDetails(EAnnotation eAnnotation) {
		Map<String, String> retrievedDetails = new HashMap<String,String>();
		EMap details = eAnnotation.getDetails();
		for(Object key: details.keySet()) {
			retrievedDetails.put((String)key, (String)details.get(key));
		}
		return retrievedDetails;
	}
	
	public Map<String, String> getAnnotationDetails(EModelElement eModelElement, String annotationSource) {
		Map<String, String> retrievedDetails = new HashMap<String,String>();
		EMap details = annotationOf(eModelElement, annotationSource).getDetails();
		for(Object key: details.keySet()) {
			retrievedDetails.put((String)key, (String)details.get(key));
		}
		return retrievedDetails;
	}

	/**
	 * Returns all annotations of the element.
	 * 
	 * <p>
	 * The returned list is a copy and may safely be modified by the caller.
	 *  
	 * @param eModelElement
	 * @return
	 */
	public List<EAnnotation> annotationsOf(EModelElement eModelElement) {
		return annotationsPrefixed(eModelElement, null);
	}
	
	/**
	 * Returns all annotations of the element whose source has a prefix that
	 * matches the supplied annotation source prefix. 
	 * 
	 * <p>
	 * The returned list is a copy and may safely be modified by the caller.
	 *  
	 * @param eModelElement
	 * @return
	 */
	public List<EAnnotation> annotationsPrefixed(EModelElement eModelElement, String annotationPrefix) {
		List<EAnnotation> annotations = new ArrayList<EAnnotation>();
		annotations.addAll(eModelElement.getEAnnotations());
		Iterator<EAnnotation> iter = annotations.iterator();
		while(iter.hasNext()) {
			EAnnotation annotation = iter.next();
			if (!annotation.getSource().startsWith(annotationPrefix)) {
				iter.remove();
			}
		}
		return annotations;
	}

	EAnnotation putMethodNameIn(IDomainClass domainClass, EAnnotation eAnnotation, String methodKey, String methodName) {
		return putAnnotationDetails(domainClass, eAnnotation, methodKey, methodName);
	}

	public String getAnnotationDetail(EAnnotation eAnnotation, String key) {
		return (String)getAnnotationDetails(eAnnotation).get(key);
	}
	
	public EAnnotation putAnnotationDetails(IDomainClass domainClass, EAnnotation eAnnotation, String key, String value) {
		Map<String, String> details = new HashMap<String, String>();
		details.put(key, value);
		return putAnnotationDetails(eAnnotation, details);
	}

	public void putAnnotationDetails(IDomainClass domainClass, EModelElement modelElement, String key, boolean value) {
		putAnnotationDetails(domainClass, modelElement, key, value?"true":"false");
	}

	public void putAnnotationDetails(IDomainClass domainClass, EModelElement modelElement, String key, String value) {
		EAnnotation ea = modelElement.getEAnnotation(StandardProgModelConstants.ANNOTATION_ELEMENT);
		if (ea == null) {
			ea = annotationOf(modelElement, StandardProgModelConstants.ANNOTATION_ELEMENT);
		}
		putAnnotationDetails(domainClass, ea, key, value);
	}

	public EAnnotation methodNamesAnnotationFor(EModelElement eModelElement) {
		return annotationFor(eModelElement, StandardProgModelConstants.ANNOTATION_SOURCE_METHOD_NAMES);
	}

	public EAnnotation annotationFor(EModelElement eModelElement, final String annotationSource) {
		EAnnotation eAnnotation = 
			eModelElement.getEAnnotation(annotationSource);
		if (eAnnotation != null) {
			return eAnnotation;
		}
		return annotationOf(eModelElement, annotationSource);
	}

}
