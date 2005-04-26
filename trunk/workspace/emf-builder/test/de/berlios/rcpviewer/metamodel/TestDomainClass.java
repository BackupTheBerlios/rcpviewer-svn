package de.berlios.rcpviewer.metamodel;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;

import de.berlios.rcpviewer.metamodel.annotations.Derived;
import de.berlios.rcpviewer.metamodel.annotations.LowerBoundOf;
import de.berlios.rcpviewer.metamodel.annotations.Ordered;
import de.berlios.rcpviewer.metamodel.annotations.Unique;
import de.berlios.rcpviewer.metamodel.annotations.UpperBoundOf;

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
	public static class CustomerWithReadWriteAttribute {
		private String surname;
		public String getSurname() {
			return surname;
		}
		public void setSurname(String surname) {
			this.surname = surname;
		}
	}
	public static class CustomerWithReadOnlyAttribute {
		private String surname;
		public String getSurname() {
			return surname;
		}
	}
	public static class CustomerWithWriteOnlyAttribute {
		private String surname;
		public void setSurname(String surname) {
			this.surname = surname;
		}
	}
	public static class CustomerWithPrimitiveAttribute {
		private int age;
		public int getAge() {
			return age;
		}
		public void setAge(int age) {
			this.age = age;
		}
	}
	public static class CustomerWithClassBasedAttribute {
		private String surname;
		public String getSurname() {
			return surname;
		}
		public void setSurname(String surname) {
			this.surname = surname;
		}
	}
	public static class CustomerWithNonDerivedReadOnlyAttribute {
		private String surname;
		public String getSurname() {
			return surname;
		}
	}
	public static class CustomerWithDerivedReadOnlyAttribute {
		@Derived
		public String getSurname() {
			return "foobar";
		}
	}
	public static class CustomerWithNoLowerBoundReadOnlyAttribute {
		private String surname;
		public String getSurname() {
			return surname;
		}
	}
	public static class CustomerWithLowerBoundReadOnlyAttribute {
		private String surname;
		@LowerBoundOf(0)
		public String getSurname() {
			return surname;
		}
	}
	public static class CustomerWithNoUpperBoundReadOnlyAttribute {
		private String surname;
		public String getSurname() {
			return surname;
		}
	}
	public static class CustomerWithUpperBoundReadOnlyAttribute {
		private String surname;
		@UpperBoundOf(3)
		public String getSurname() {
			return surname;
		}
	}
	public static class CustomerWithNoUniquenessReadOnlyAttribute {
		private String surname;
		public String getSurname() {
			return surname;
		}
	}
	public static class CustomerWithUniqueReadOnlyAttribute {
		private String surname;
		@UpperBoundOf(3)
		@Unique
		public String getSurname() {
			return surname;
		}
	}
	public static class CustomerWithNonUniqueReadOnlyAttribute {
		private String surname;
		@UpperBoundOf(3)
		@Unique(false)
		public String getSurname() {
			return surname;
		}
	}
	public static class CustomerWithNoOrderingReadOnlyAttribute {
		private String surname;
		public String getSurname() {
			return surname;
		}
	}
	public static class CustomerWithOrderingReadOnlyAttribute {
		private String surname;
		@UpperBoundOf(3)
		@Ordered
		public String getSurname() {
			return surname;
		}
	}
	public static class CustomerWithoutOrderingReadOnlyAttribute {
		private String surname;
		@UpperBoundOf(3)
		@Ordered(false)
		public String getSurname() {
			return surname;
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

	public void testWhetherEAttributeIsChangeableForReadWriteAttribute() {
		domainClass = new DomainClass(CustomerWithReadWriteAttribute.class);
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertTrue(eAttribute.isChangeable());
		assertTrue(domainClass.isChangeable(eAttribute));
	}

	public void testWhetherEAttributeIsChangeableForReadOnlyAttribute() {
		domainClass = new DomainClass(CustomerWithReadOnlyAttribute.class);
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertFalse(eAttribute.isChangeable());
		assertFalse(domainClass.isChangeable(eAttribute));
	}

	public void testWhetherEAttributeIsChangeableForWriteOnlyAttribute() {
		domainClass = new DomainClass(CustomerWithWriteOnlyAttribute.class);
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertTrue(eAttribute.isChangeable());
		assertTrue(domainClass.isChangeable(eAttribute));
	}
	
	public void testWhetherEAttributeIsAnnotatedAsWriteOnlyForReadWriteAttribute() {
		domainClass = new DomainClass(CustomerWithReadWriteAttribute.class);
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertFalse(domainClass.isWriteOnly(eAttribute));
	}
	
	public void testWhetherEAttributeIsAnnotatedAsReadOnlyForReadOnlyAttribute() {
		domainClass = new DomainClass(CustomerWithReadOnlyAttribute.class);
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertFalse(domainClass.isWriteOnly(eAttribute));
	}
	
	public void testWhetherEAttributeIsAnnotatedAsWriteOnlyForWriteOnlyAttribute() {
		domainClass = new DomainClass(CustomerWithWriteOnlyAttribute.class);
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertTrue(domainClass.isWriteOnly(eAttribute));
	}

	public void testWhetherEAttributeIsDerivedForNonDerivedReadOnlyAttribute() {
		domainClass = new DomainClass(CustomerWithNonDerivedReadOnlyAttribute.class);
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertFalse(eAttribute.isDerived());
		assertFalse(eAttribute.isTransient());
		assertFalse(eAttribute.isVolatile());
		assertFalse(domainClass.isDerived(eAttribute));
	}
	
	public void testWhetherEAttributeIsDerivedForDerivedReadOnlyAttribute() {
		domainClass = new DomainClass(CustomerWithDerivedReadOnlyAttribute.class);
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertTrue(eAttribute.isDerived());
		assertTrue(eAttribute.isTransient());
		assertTrue(eAttribute.isVolatile());
		assertTrue(domainClass.isDerived(eAttribute));
	}

	public void incompletetestWhetherEAttributeIsDerivedForDerivedWriteOnlyAttribute() {
		// TODO: not tested, but this is a placeholder: we should have a compile time rule that prevents this 
	}

	public void testLowerBoundOfEAttributeWhenNoneSpecified() {
		domainClass = new DomainClass(CustomerWithNoLowerBoundReadOnlyAttribute.class);
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertEquals(1, eAttribute.getLowerBound());
		assertEquals(1, domainClass.getLowerBound(eAttribute));
	}

	public void testLowerBoundOfEAttributeWhenSpecified() {
		domainClass = new DomainClass(CustomerWithLowerBoundReadOnlyAttribute.class);
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertEquals(0, eAttribute.getLowerBound());
		assertEquals(0, domainClass.getLowerBound(eAttribute));
	}

	public void incompletetestLowerBoundOfEAttributeWithDatatypeNotSupporting() {
		// TODO
		
	}

	public void testUpperBoundOfEAttributeWhenNoneSpecified() {
		domainClass = new DomainClass(CustomerWithNoUpperBoundReadOnlyAttribute.class);
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertEquals(1, eAttribute.getUpperBound());
		assertEquals(1, domainClass.getLowerBound(eAttribute));
	}

	public void testUpperBoundOfEAttributeWhenSpecified() {
		domainClass = new DomainClass(CustomerWithUpperBoundReadOnlyAttribute.class);
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertEquals(3, eAttribute.getUpperBound());
		assertEquals(3, domainClass.getUpperBound(eAttribute));
	}

	public void incompletetestUpperBoundOfEAttributeWithDatatypeNotSupporting() {
		// TODO
	}

	public void testUniquenessOfEAttributeWhenNoneSpecified() {
		domainClass = new DomainClass(CustomerWithNoUniquenessReadOnlyAttribute.class);
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertTrue(eAttribute.isUnique());
		assertTrue(domainClass.isUnique(eAttribute));
	}

	public void testUniquenessOfEAttributeWhenSpecifiedAsTrue() {
		domainClass = new DomainClass(CustomerWithUniqueReadOnlyAttribute.class);
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertTrue(eAttribute.isUnique());
		assertTrue(domainClass.isUnique(eAttribute));
	}

	public void testUniquenessOfEAttributeWhenSpecifiedAsFalse() {
		domainClass = new DomainClass(CustomerWithNonUniqueReadOnlyAttribute.class);
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertFalse(eAttribute.isUnique());
		assertFalse(domainClass.isUnique(eAttribute));
	}

	public void incompletetestUniquenessOfEAttributeWhenSpecifiedWithoutUpperBound() {
		// TODO
	}

	public void testOrderingOfEAttributeWhenNoneSpecified() {
		domainClass = new DomainClass(CustomerWithNoOrderingReadOnlyAttribute.class);
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertTrue(eAttribute.isOrdered());
		assertTrue(domainClass.isOrdered(eAttribute));
	}

	public void testOrderingOfEAttributeWhenSpecifiedAsTrue() {
		domainClass = new DomainClass(CustomerWithOrderingReadOnlyAttribute.class);
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertTrue(eAttribute.isOrdered());
		assertTrue(domainClass.isOrdered(eAttribute));
	}

	public void testOrderingOfEAttributeWhenSpecifiedAsFalse() {
		domainClass = new DomainClass(CustomerWithoutOrderingReadOnlyAttribute.class);
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertFalse(eAttribute.isOrdered());
		assertFalse(domainClass.isOrdered(eAttribute));
	}

	public void incompletetestOrderingOfEAttributeWhenSpecifiedWithoutUpperBound() {
		// TODO
	}

	public void incompletetestWhetherEAttributeIsUnsettableForPrimitiveAttribute() {
		domainClass = new DomainClass(CustomerWithPrimitiveAttribute.class);
		EAttribute eAttribute = domainClass.getEAttributeNamed("age");
		assertFalse(eAttribute.isUnsettable());
	}

	public void incompletetestWhetherEAttributeIsUnsettableForClassBasedAttribute() {
		domainClass = new DomainClass(CustomerWithClassBasedAttribute.class);
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertTrue(eAttribute.isUnsettable());
	}

	
}
