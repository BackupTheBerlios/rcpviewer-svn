package de.berlios.rcpviewer.progmodel.standard;

import org.eclipse.emf.ecore.EAttribute;

import de.berlios.rcpviewer.AbstractTestCase;
import de.berlios.rcpviewer.domain.Domain;
import de.berlios.rcpviewer.domain.IDomainClass;

public class TestDomainClassAttributesUnsettability extends AbstractTestCase {

	@InDomain
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
	@InDomain
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
	@InDomain
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

	private Domain domain;
	private IDomainClass<?> domainClass;
	protected void setUp() throws Exception {
		super.setUp();
		domain = Domain.instance();
	}

	protected void tearDown() throws Exception {
		domain.reset();
		domain = null;
		super.tearDown();
	}
	
	public void testWhetherEAttributeIsUnsettableWhenIs() {
		domainClass = domain.lookup(CustomerWithUnsettableAttribute.class);
		domain.done();
		EAttribute eAttribute = domainClass.getEAttributeNamed("age");
		assertTrue(eAttribute.isUnsettable());
		assertTrue(domainClass.isUnsettable(eAttribute));
	}

	public void testWhetherEAttributeIsUnsettableWhenNotDueToMissingUnsetMethod() {
		domainClass = domain.lookup(CustomerWithOnlyIsUnsetForAttribute.class);
		domain.done();
		EAttribute eAttribute = domainClass.getEAttributeNamed("age");
		assertFalse(eAttribute.isUnsettable());
		assertFalse(domainClass.isUnsettable(eAttribute));
	}
	public void testWhetherEAttributeIsUnsettableWhenNotDueToMissingIsUnsetMethod() {
		domainClass = domain.lookup(CustomerWithOnlyUnsetForAttribute.class);
		domain.done();
		EAttribute eAttribute = domainClass.getEAttributeNamed("age");
		assertFalse(eAttribute.isUnsettable());
		assertFalse(domainClass.isUnsettable(eAttribute));
	}

	
	

}
