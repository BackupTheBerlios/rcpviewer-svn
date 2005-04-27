package de.berlios.rcpviewer.metamodel;

import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;

import de.berlios.rcpviewer.metamodel.*;


public class TestEmfFacade extends TestCase {

	public static class ClassInRegularPackage {}

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
			de.berlios.rcpviewer.metamodel.TestEmfFacade.ClassInRegularPackage.class.getPackage();
		EPackage ePackage = 
			emfFacade.getEPackageFor(javaPackage);
		assertNotNull(ePackage);
		assertEquals("http://de.berlios.rcpviewer.metamodel/2005/de.berlios.rcpviewer.metamodel", ePackage.getNsURI());
		assertEquals("de.berlios.rcpviewer.metamodel", ePackage.getName());
	}
	
	public void testFindPackageWithNameUsingRegularPackageWhenNotYetCreated() {
		Package javaPackage = 
			de.berlios.rcpviewer.metamodel.TestEmfFacade.ClassInRegularPackage.class.getPackage();
		EPackage ePackage = 
			emfFacade.findPackageWithName(javaPackage.getName());
		assertNull(ePackage);
	}

	
	public void testFindPackageWithNameUsingRegularPackageWhenCreated() {
		Package javaPackage = 
			de.berlios.rcpviewer.metamodel.TestEmfFacade.ClassInRegularPackage.class.getPackage();
		
		// cause to get created
		emfFacade.getEPackageFor(javaPackage);

		// should now be there
		EPackage ePackage = 
			emfFacade.findPackageWithName(javaPackage.getName());
		
		assertNotNull(ePackage);
		assertEquals("http://de.berlios.rcpviewer.metamodel/2005/de.berlios.rcpviewer.metamodel", ePackage.getNsURI());
		assertEquals("de.berlios.rcpviewer.metamodel", ePackage.getName());
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
		try {
			EDataType eDataType = 
				emfFacade.getEDataTypeFor(TestEmfFacadeCustomer.class);
			fail("Expected an IllegalArgumentException to have been thrown.");
		} catch(IllegalArgumentException ex) {
			// expected
		}
	}

}