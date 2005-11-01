package org.essentialplatform.domain;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

import org.essentialplatform.domain.EmfAnnotations;

/**
 * Independent (so also applicable) to both runtime and compiletime, 
 * and therefore tested in both.
 * 
 * @author Dan Haywood
 */
public class TestEmf extends TestCase {

	private Class[] primitiveClasses;
	private Class[] primitiveArrayClasses;
	private Class[] wrapperClasses;
	private Class[] builtInValueObjectClasses;
	
	private Emf emf;
	
	protected void setUp() throws Exception {
		super.setUp();
		emf = new Emf();
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
		EPackage ePackage = emf.getEcorePackage();
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
			emf.getEPackageFor(javaPackage, null);
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
				emf.getEPackageFor(javaPackage, null);
			assertNotNull(ePackage);
			assertEquals("http://www.eclipse.org/emf/2002/Ecore", ePackage.getNsURI());
		}
	}

	public void testGetEPackageForRegularPackage() {
		Package javaPackage = 
			org.essentialplatform.domain.ClassInRegularPackage.class.getPackage();
		EPackage ePackage = 
			emf.getEPackageFor(javaPackage, new ResourceSetImpl());
		assertNotNull(ePackage);
		assertEquals("http://org.essentialplatform.domain/2005/org.essentialplatform.domain", ePackage.getNsURI());
		assertEquals("org.essentialplatform.domain", ePackage.getName());
	}
	
	public void testFindPackageWithNameUsingRegularPackageWhenNotYetCreated() {
		Package javaPackage = 
			org.essentialplatform.domain.ClassInRegularPackage.class.getPackage();
		EPackage ePackage = 
			emf.findPackageWithName(javaPackage.getName(), new ResourceSetImpl());
		assertNull(ePackage);
	}

	
	public void testFindPackageWithNameUsingRegularPackageWhenCreated() {
		Package javaPackage = 
			org.essentialplatform.domain.ClassInRegularPackage.class.getPackage();
		
		ResourceSet resourceSet = new ResourceSetImpl();
		
		// cause to get created
		emf.getEPackageFor(javaPackage, resourceSet);

		// should now be there
		EPackage ePackage = 
			emf.findPackageWithName(javaPackage.getName(), resourceSet);
		
		assertNotNull(ePackage);
		assertEquals("http://org.essentialplatform.domain/2005/org.essentialplatform.domain", ePackage.getNsURI());
		assertEquals("org.essentialplatform.domain", ePackage.getName());
	}
	
	public void testFindPackageWithNameUsingNonExistentPackage() {
		EPackage ePackage = 
			emf.findPackageWithName("no.such.named.package", new ResourceSetImpl());
		assertNull(ePackage);
	}

	public void testGetEDataTypeForPrimitives() {
		for(Class c: primitiveClasses) {
			EDataType eDataType = emf.getEDataTypeFor(c, null);
			assertNotNull(eDataType);
			assertEquals(c.getCanonicalName(), eDataType.getInstanceClassName());
		}
	}
	
	public void testGetEDataTypeForPrimitiveArrays() {
		for(Class c: primitiveArrayClasses) {
			EDataType eDataType = emf.getEDataTypeFor(c, null);
			assertNotNull(eDataType);
			assertEquals(c.getCanonicalName(), eDataType.getInstanceClassName());
		}
	}
	

	public void testGetEDataTypeForWrappers() {
		for(Class c: wrapperClasses) {
			EDataType eDataType = emf.getEDataTypeFor(c, null);
			assertNotNull(eDataType);
			assertEquals(c.getCanonicalName(), eDataType.getInstanceClassName());
		}
	}


	public void testGetEDataTypeForBuiltInValueObjects() {
		for(Class c: builtInValueObjectClasses) {
			EDataType eDataType = emf.getEDataTypeFor(c, null);
			assertNotNull(eDataType);
			assertEquals(c.getCanonicalName(), eDataType.getInstanceClassName());
		}
	}
	
	public void testGetEDataTypeForValueObject() {
		ResourceSet resourceSet = new ResourceSetImpl();
		EDataType eDataType = 
			emf.getEDataTypeFor(EmfFacadeDatePeriod.class, resourceSet);
		assertNotNull(eDataType);
		assertEquals(EmfFacadeDatePeriod.class.getName(), 
					 eDataType.getInstanceClassName());
	}

	// removed since the semantics of this method have now changed;
	// emf.getEDataTypeFor should only be called for classes that represent
	// values.
//	public void incompletetestGetEDataTypeForClassThatIsntAValueObject() {
//		ResourceSet resourceSet = new ResourceSetImpl();
//		EDataType eDataType = 
//			emf.getEDataTypeFor(EmfFacadeCustomer.class, resourceSet);
//		assertNull(eDataType);
//	}



}
