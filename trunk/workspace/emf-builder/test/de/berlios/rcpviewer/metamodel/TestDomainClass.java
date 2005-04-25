package de.berlios.rcpviewer.metamodel;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;

import junit.framework.TestCase;

public class TestDomainClass extends TestCase {

	public static class CustomerWithNoAttributes {
	}

	public static class CustomerWithEveryPrimitiveTypeAccessor {
		private byte aByte;
		public byte getAByte() {
			return aByte;
		}
		private short aShort;
		public short getAShort() {
			return aShort;
		}
		private int anInt;
		public int getAnInt() {
			return anInt;
		}
		private long aLong;
		public long getALong() {
			return aLong;
		}
		private char aChar;
		public char getAChar() {
			return aChar;
		}
		private float aFloat;
		public float getAFloat() {
			return aFloat;
		}
		private double aDouble;
		public double getADouble() {
			return aDouble;
		}
		private boolean aBoolean;
		public boolean isABoolean() {
			return aBoolean;
		}
	}

	private IDomainClass domainClass;
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testGetJavaClass() {
		domainClass = new DomainClass(CustomerWithNoAttributes.class);
		assertSame(CustomerWithNoAttributes.class, domainClass.getJavaClass());
	}

	public void testGetEClass() {
		domainClass = new DomainClass(CustomerWithNoAttributes.class);
		EClass eClass = domainClass.getEClass();
		assertNotNull(eClass);
		assertSame(eClass.getInstanceClass(), CustomerWithNoAttributes.class);
		assertEquals("CustomerWithNoAttributes", eClass.getName());
		EPackage ePackage = eClass.getEPackage();
		assertNotNull(ePackage);
		assertEquals(
				CustomerWithNoAttributes.class.getPackage().getName(), 
				ePackage.getName());
	}

	public void testNumberOfAttributesWhenNone() {
		domainClass = new DomainClass(CustomerWithNoAttributes.class);
		assertEquals(0, domainClass.getNumberOfAttributes());
	}

	public void testNumberOfAttributesForPrimitves() {
		domainClass = new DomainClass(CustomerWithEveryPrimitiveTypeAccessor.class);
		assertEquals(8, domainClass.getNumberOfAttributes());
	}
	
	public void testGetEAttributeNamedForAllExisting() {
		domainClass = new DomainClass(CustomerWithEveryPrimitiveTypeAccessor.class);
		for(String attributeName: new String[] { 
								"aByte", "aShort", "anInt", "aLong", 
								"aChar", "aFloat", "aDouble", "aBoolean", }) {
			EAttribute eAttribute = domainClass.getEAttributeNamed(attributeName);
			assertEquals(attributeName, eAttribute.getName());
		}
	}

	public void testGetEAttributeNamedForNoneExisting() {
		domainClass = new DomainClass(CustomerWithEveryPrimitiveTypeAccessor.class);
		assertNull(domainClass.getEAttributeNamed("nonExistingAttribute"));
	}

}
