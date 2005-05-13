package de.berlios.rcpviewer.progmodel.standard;

import org.eclipse.emf.ecore.EAttribute;

import de.berlios.rcpviewer.AbstractTestCase;
import de.berlios.rcpviewer.metamodel.IDomainClass;
import de.berlios.rcpviewer.metamodel.MetaModel;

public class TestDomainClassAttributes extends AbstractTestCase {

	private MetaModel metaModel;
	private IDomainClass<?> domainClass;
	protected void setUp() throws Exception {
		super.setUp();
		metaModel = new MetaModel();
	}

	protected void tearDown() throws Exception {
		metaModel.reset();
		metaModel = null;
		super.tearDown();
	}
	

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

	public void testGetAttributesWhenNone() {
		domainClass = metaModel.register(CustomerWithNoAttributes.class);
		metaModel.done();
		assertEquals(0, domainClass.attributes().size());
	}

	public void testGetAttributesForPrimitives() {
		domainClass = metaModel.register(CustomerWithEveryPrimitiveTypeAccessor.class);
		metaModel.done();
		assertEquals(8, domainClass.attributes().size());
	}
	
	public void incompletetestGetAttributesIgnoringInherited() {
		// TODO: invoking domainClass.attributes(false);
	}
	

	public void testGetEAttributeNamedForEveryBuiltInPrimitiveType() {
		domainClass = metaModel.register(CustomerWithEveryPrimitiveTypeAccessor.class);
		metaModel.done();
		for(String attributeName: new String[] { 
								"aByte", "aShort", "anInt", "aLong", 
								"aChar", "aFloat", "aDouble", "aBoolean", }) {
			EAttribute eAttribute = domainClass.getEAttributeNamed(attributeName);
			assertEquals(attributeName, eAttribute.getName());
		}
	}

	public void testGetEAttributeNamedForNoneExisting() {
		domainClass = new DomainClass<CustomerWithEveryPrimitiveTypeAccessor>(CustomerWithEveryPrimitiveTypeAccessor.class);
		assertNull(domainClass.getEAttributeNamed("nonExistingAttribute"));
	}

	public void testWhetherEAttributeIsChangeableForReadWriteAttribute() {
		domainClass = metaModel.register(CustomerWithReadWriteAttribute.class);
		metaModel.done();
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertTrue(eAttribute.isChangeable());
		assertTrue(domainClass.isChangeable(eAttribute));
	}

	public void testWhetherEAttributeIsChangeableForReadOnlyAttribute() {
		domainClass = metaModel.register(CustomerWithReadOnlyAttribute.class);
		metaModel.done();
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertFalse(eAttribute.isChangeable());
		assertFalse(domainClass.isChangeable(eAttribute));
	}

	public void testWhetherEAttributeIsChangeableForWriteOnlyAttribute() {
		domainClass = metaModel.register(CustomerWithWriteOnlyAttribute.class);
		metaModel.done();
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertTrue(eAttribute.isChangeable());
		assertTrue(domainClass.isChangeable(eAttribute));
	}
	
	public void testWhetherEAttributeIsAnnotatedAsWriteOnlyForReadWriteAttribute() {
		domainClass = metaModel.register(CustomerWithReadWriteAttribute.class);
		metaModel.done();
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertFalse(domainClass.isWriteOnly(eAttribute));
	}
	
	public void testWhetherEAttributeIsAnnotatedAsReadOnlyForReadOnlyAttribute() {
		domainClass = metaModel.register(CustomerWithReadOnlyAttribute.class);
		metaModel.done();
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertFalse(domainClass.isWriteOnly(eAttribute));
	}
	
	public void testWhetherEAttributeIsAnnotatedAsWriteOnlyForWriteOnlyAttribute() {
		domainClass = metaModel.register(CustomerWithWriteOnlyAttribute.class);
		metaModel.done();
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertTrue(domainClass.isWriteOnly(eAttribute));
	}

	public void testWhetherEAttributeIsDerivedForNonDerivedReadOnlyAttribute() {
		domainClass = metaModel.register(CustomerWithNonDerivedReadOnlyAttribute.class);
		metaModel.done();
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertFalse(eAttribute.isDerived());
		assertFalse(eAttribute.isTransient());
		assertFalse(eAttribute.isVolatile());
		assertFalse(domainClass.isDerived(eAttribute));
	}
	
	public void testWhetherEAttributeIsDerivedForDerivedReadOnlyAttribute() {
		domainClass = metaModel.register(CustomerWithDerivedReadOnlyAttribute.class);
		metaModel.done();
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertTrue(eAttribute.isDerived());
		assertTrue(eAttribute.isTransient());
		assertTrue(eAttribute.isVolatile());
		assertTrue(domainClass.isDerived(eAttribute));
	}

	public void incompletetestWhetherEAttributeIsDerivedForDerivedWriteOnlyAttribute() {
		// TODO: not tested, but this is a placeholder: we should have a compile time rule that prevents this 
	}


	
	public void incompletetestNeedTestsUsingValueMarkers() {
		// TODO
	}


}
