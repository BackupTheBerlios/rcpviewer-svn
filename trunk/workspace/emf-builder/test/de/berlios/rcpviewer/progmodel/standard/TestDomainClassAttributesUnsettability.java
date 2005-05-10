package de.berlios.rcpviewer.progmodel.standard;

import org.eclipse.emf.ecore.EAttribute;

import de.berlios.rcpviewer.AbstractTestCase;
import de.berlios.rcpviewer.metamodel.IDomainClass;

public class TestDomainClassAttributesUnsettability extends AbstractTestCase {

	public static class CustomerWithUnsettableAttribute {
		private int age;
		public boolean isUnsetAge() {
			return age == -1;
		}
		public void unsetAge() {
			age = -1;
		}
		public int getAge() {
			return age;
		}
		public void setAge(int age) {
			this.age = age;
		}
	}
	public static class CustomerWithOnlyIsUnsetForAttribute {
		private int age;
		/**
		 * Not having unsetXxx means this attribute isn't unsettable.
		 * @return
		 */
		public boolean isUnsetAge() {
			return age == -1;
		}
		public int getAge() {
			return age;
		}
		public void setAge(int age) {
			this.age = age;
		}
	}
	public static class CustomerWithOnlyUnsetForAttribute {
		private int age;
		/**
		 * Not having isUnsetXxx means this attribute isn't unsettable.
		 * @return
		 */
		public void unsetAge() {
			age = -1;
		}
		public int getAge() {
			return age;
		}
		public void setAge(int age) {
			this.age = age;
		}
	}

	private IDomainClass<?> domainClass;
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testWhetherEAttributeIsUnsettableWhenIs() {
		domainClass = new DomainClass<CustomerWithUnsettableAttribute>(CustomerWithUnsettableAttribute.class);
		EAttribute eAttribute = domainClass.getEAttributeNamed("age");
		assertTrue(eAttribute.isUnsettable());
		assertTrue(domainClass.isUnsettable(eAttribute));
	}

	public void testWhetherEAttributeIsUnsettableWhenNotDueToMissingUnsetMethod() {
		domainClass = new DomainClass<CustomerWithOnlyIsUnsetForAttribute>(CustomerWithOnlyIsUnsetForAttribute.class);
		EAttribute eAttribute = domainClass.getEAttributeNamed("age");
		assertFalse(eAttribute.isUnsettable());
		assertFalse(domainClass.isUnsettable(eAttribute));
	}
	public void testWhetherEAttributeIsUnsettableWhenNotDueToMissingIsUnsetMethod() {
		domainClass = new DomainClass<CustomerWithOnlyUnsetForAttribute>(CustomerWithOnlyUnsetForAttribute.class);
		EAttribute eAttribute = domainClass.getEAttributeNamed("age");
		assertFalse(eAttribute.isUnsettable());
		assertFalse(domainClass.isUnsettable(eAttribute));
	}

	
	

}
