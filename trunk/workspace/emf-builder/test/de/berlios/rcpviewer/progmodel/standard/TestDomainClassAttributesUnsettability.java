package de.berlios.rcpviewer.progmodel.standard;

import org.eclipse.emf.ecore.EAttribute;

import de.berlios.rcpviewer.AbstractTestCase;
import de.berlios.rcpviewer.metamodel.IDomainClass;
import de.berlios.rcpviewer.metamodel.MetaModel;

public class TestDomainClassAttributesUnsettability extends AbstractTestCase {

	@Domain
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
	@Domain
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
	@Domain
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

	private MetaModel metaModel;
	private IDomainClass<?> domainClass;
	protected void setUp() throws Exception {
		super.setUp();
		metaModel = MetaModel.instance();
	}

	protected void tearDown() throws Exception {
		metaModel = null;
		super.tearDown();
	}
	
	public void testWhetherEAttributeIsUnsettableWhenIs() {
		domainClass = metaModel.lookup(CustomerWithUnsettableAttribute.class);
		metaModel.done();
		EAttribute eAttribute = domainClass.getEAttributeNamed("age");
		assertTrue(eAttribute.isUnsettable());
		assertTrue(domainClass.isUnsettable(eAttribute));
	}

	public void testWhetherEAttributeIsUnsettableWhenNotDueToMissingUnsetMethod() {
		domainClass = metaModel.lookup(CustomerWithOnlyIsUnsetForAttribute.class);
		metaModel.done();
		EAttribute eAttribute = domainClass.getEAttributeNamed("age");
		assertFalse(eAttribute.isUnsettable());
		assertFalse(domainClass.isUnsettable(eAttribute));
	}
	public void testWhetherEAttributeIsUnsettableWhenNotDueToMissingIsUnsetMethod() {
		domainClass = metaModel.lookup(CustomerWithOnlyUnsetForAttribute.class);
		metaModel.done();
		EAttribute eAttribute = domainClass.getEAttributeNamed("age");
		assertFalse(eAttribute.isUnsettable());
		assertFalse(domainClass.isUnsettable(eAttribute));
	}

	
	

}
