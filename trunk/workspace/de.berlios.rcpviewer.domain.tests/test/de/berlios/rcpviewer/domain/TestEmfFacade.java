package de.berlios.rcpviewer.domain;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EPackage;

import de.berlios.rcpviewer.domain.EmfFacade;


public class TestEmfFacade extends TestCase {

	private Class[] primitiveClasses;
	private Class[] primitiveArrayClasses;
	private Class[] wrapperClasses;
	private Class[] builtInValueObjectClasses;
	
	private EmfFacade emfFacade;
	protected void setUp() throws Exception {
		super.setUp();
		emfFacade = new EmfFacade();
		primitiveClasses = new Class[]{
				byte.class, short.class, int.class, long.class, 
				char.class, float.class, double.class, boolean.class, };
		// TODO: seems to be the only one defined in ECore ? should probably
		// supplement others in own set
		primitiveArrayClasses = new Class[] {
				byte[].class,
		};
		wrapperClasses = new Class[]{
				java.lang.Byte.class, 
				java.lang.Short.class, 
				java.lang.Integer.class, 
				java.lang.Long.class, 
				java.lang.Character.class, 
				java.lang.Float.class, 
				java.lang.Double.class, 
				java.lang.Boolean.class, 
		};
		builtInValueObjectClasses = new Class[]{
				java.math.BigDecimal.class,
				java.math.BigInteger.class,
				java.util.Date.class,
				java.lang.String.class,
		};
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		emfFacade = null;
		primitiveClasses = null;
		primitiveArrayClasses = null;
		wrapperClasses = null;
		builtInValueObjectClasses = null;
	}
	
	
//	public void testDumpEcoreClassifiers() {
//		EcoreFactory.eINSTANCE.createEPackage(); // load ECore in.
//		Set<Map.Entry> ePackageEntrySet = EPackage.Registry.INSTANCE.entrySet();  
//		for(Map.Entry ePackageEntry: ePackageEntrySet) {
//			String ePackageNsUri = (String)ePackageEntry.getKey();
//			EPackage ePackage = (EPackage)ePackageEntry.getValue();
//			System.out.println(ePackageNsUri + "->" + ePackage);
//			for(Object o: ePackage.getEClassifiers().toArray()) {
//				EClassifier c = (EClassifier)o;
//				System.out.println("  " + c.getName() + ": " + c.getInstanceClassName());
//			}
//		}
//	}
	
	public void testGetEcorePackage() {
		EPackage ePackage = emfFacade.getEcorePackage();
		assertNotNull(ePackage);
		assertNotNull(ePackage);
		assertEquals("http://www.eclipse.org/emf/2002/Ecore", ePackage.getNsURI());
	}

	/**
	 * The default package (actually null) maps onto the EMF core package
	 *
	 */
	public void testGetEPackageForDefaultPackage() {
		Package javaPackage = Package.getPackage("");
		EPackage ePackage = 
			emfFacade.getEPackageFor(javaPackage);
		assertNotNull(ePackage);
		assertEquals("http://www.eclipse.org/emf/2002/Ecore", ePackage.getNsURI());
	}

	/**
	 * The package that represents the class of primitives (actually null) maps 
	 * onto the EMF core package
	 *
	 */
	public void testGetEPackageForPackageForClassesRepresentingPrimitives() {
		for(Class c: primitiveClasses) {
			Package javaPackage = c.getPackage();
			EPackage ePackage = 
				emfFacade.getEPackageFor(javaPackage);
			assertNotNull(ePackage);
			assertEquals("http://www.eclipse.org/emf/2002/Ecore", ePackage.getNsURI());
		}
	}

	public void testGetEPackageForRegularPackage() {
		Package javaPackage = 
			de.berlios.rcpviewer.domain.ClassInRegularPackage.class.getPackage();
		EPackage ePackage = 
			emfFacade.getEPackageFor(javaPackage);
		assertNotNull(ePackage);
		assertEquals("http://de.berlios.rcpviewer.domain/2005/de.berlios.rcpviewer.domain", ePackage.getNsURI());
		assertEquals("de.berlios.rcpviewer.domain", ePackage.getName());
	}
	
	public void testFindPackageWithNameUsingRegularPackageWhenNotYetCreated() {
		Package javaPackage = 
			de.berlios.rcpviewer.domain.ClassInRegularPackage.class.getPackage();
		EPackage ePackage = 
			emfFacade.findPackageWithName(javaPackage.getName());
		assertNull(ePackage);
	}

	
	public void testFindPackageWithNameUsingRegularPackageWhenCreated() {
		Package javaPackage = 
			de.berlios.rcpviewer.domain.ClassInRegularPackage.class.getPackage();
		
		// cause to get created
		emfFacade.getEPackageFor(javaPackage);

		// should now be there
		EPackage ePackage = 
			emfFacade.findPackageWithName(javaPackage.getName());
		
		assertNotNull(ePackage);
		assertEquals("http://de.berlios.rcpviewer.domain/2005/de.berlios.rcpviewer.domain", ePackage.getNsURI());
		assertEquals("de.berlios.rcpviewer.domain", ePackage.getName());
	}
	
	public void testFindPackageWithNameUsingNonExistentPackage() {
		EPackage ePackage = 
			emfFacade.findPackageWithName("no.such.named.package");
		assertNull(ePackage);
	}

	public void testGetEDataTypeForPrimitives() {
		for(Class c: primitiveClasses) {
			EDataType eDataType = emfFacade.getEDataTypeFor(c);
			assertNotNull(eDataType);
			assertEquals(c.getCanonicalName(), eDataType.getInstanceClassName());
		}
	}
	
	public void testGetEDataTypeForPrimitiveArrays() {
		for(Class c: primitiveArrayClasses) {
			EDataType eDataType = emfFacade.getEDataTypeFor(c);
			assertNotNull(eDataType);
			assertEquals(c.getCanonicalName(), eDataType.getInstanceClassName());
		}
	}
	

	public void testGetEDataTypeForWrappers() {
		for(Class c: wrapperClasses) {
			EDataType eDataType = emfFacade.getEDataTypeFor(c);
			assertNotNull(eDataType);
			assertEquals(c.getCanonicalName(), eDataType.getInstanceClassName());
		}
	}


	public void testGetEDataTypeForBuiltInValueObjects() {
		for(Class c: builtInValueObjectClasses) {
			EDataType eDataType = emfFacade.getEDataTypeFor(c);
			assertNotNull(eDataType);
			assertEquals(c.getCanonicalName(), eDataType.getInstanceClassName());
		}
	}
	
	public void testGetEDataTypeForValueObject() {
		EDataType eDataType = 
			emfFacade.getEDataTypeFor(TestEmfFacadeDatePeriod.class);
		assertNotNull(eDataType);
		assertEquals(TestEmfFacadeDatePeriod.class.getName(), 
					 eDataType.getInstanceClassName());
	}

	public void testGetEDataTypeForClassThatIsntAValueObject() {
		EDataType eDataType = 
			emfFacade.getEDataTypeFor(TestEmfFacadeCustomer.class);
		assertNull(eDataType);
	}

	/**
	 * Obtain annotation of supplied source on a model element.
	 */
	public void testAnnotationOf() {
		// any model element should do
		EModelElement modelElement = emfFacade.getEcorePackage();
		EAnnotation eAnnotation = emfFacade.annotationOf(modelElement, "http://rcpviewer.berlios.de/test/source");
		assertNotNull(eAnnotation);
		assertEquals("http://rcpviewer.berlios.de/test/source", eAnnotation.getSource());
	}

	/**
	 * The EAnnotations#details is a map-like construct that can be put to
	 * and got from. 
	 */
	public void testSetEAnnotationsDetails() {
		// any model element should do
		EModelElement modelElement = emfFacade.getEcorePackage();
		EAnnotation eAnnotation = emfFacade.annotationOf(modelElement, "http://rcpviewer.berlios.de/test/source");
		Map<String,String> details = new HashMap<String,String>();
		details.put("foo", "bar");
		details.put("baz", "boz");
		assertEquals(0, eAnnotation.getDetails().size());
		emfFacade.putAnnotationDetails(eAnnotation, details);
		assertEquals(2, eAnnotation.getDetails().size());
	}

	/**
	 * The EAnnotations#details is a map-like construct that can be put to
	 * and got from. 
	 */
	public void testGetEAnnotationsDetails() {
		// any model element should do
		EModelElement modelElement = emfFacade.getEcorePackage();
		EAnnotation eAnnotation = emfFacade.annotationOf(modelElement, "http://rcpviewer.berlios.de/test/source");
		Map<String,String> details = new HashMap<String,String>();
		details.put("foo", "bar");
		details.put("baz", "boz");
		emfFacade.putAnnotationDetails(eAnnotation, details);
		
		Map<String,String> retrievedDetails = emfFacade.getAnnotationDetails(eAnnotation);
		assertEquals(2, retrievedDetails.size());
		assertEquals("bar", retrievedDetails.get("foo"));
		assertEquals("boz", retrievedDetails.get("baz"));
	}

}
