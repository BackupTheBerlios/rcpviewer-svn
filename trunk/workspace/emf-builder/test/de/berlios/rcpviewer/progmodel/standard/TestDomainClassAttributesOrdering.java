package de.berlios.rcpviewer.progmodel.standard;

import org.eclipse.emf.ecore.EAttribute;

import de.berlios.rcpviewer.AbstractTestCase;
import de.berlios.rcpviewer.metamodel.IDomainClass;
import de.berlios.rcpviewer.metamodel.MetaModel;

public class TestDomainClassAttributesOrdering extends AbstractTestCase {

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

	private MetaModel metaModel;
	private IDomainClass<?> domainClass;
	protected void setUp() throws Exception {
		super.setUp();
		metaModel = new MetaModel();
	}

	protected void tearDown() throws Exception {
		metaModel = null;
		super.tearDown();
	}
	
	// ordering //

	public void testOrderingOfEAttributeWhenNoneSpecified() {
		domainClass = metaModel.register(CustomerWithNoOrderingReadOnlyAttribute.class);
		metaModel.done();
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertTrue(eAttribute.isOrdered());
		assertTrue(domainClass.isOrdered(eAttribute));
	}

	public void testOrderingOfEAttributeWhenSpecifiedAsTrue() {
		domainClass = metaModel.register(CustomerWithOrderingReadOnlyAttribute.class);
		metaModel.done();
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertTrue(eAttribute.isOrdered());
		assertTrue(domainClass.isOrdered(eAttribute));
	}

	public void testOrderingOfEAttributeWhenSpecifiedAsFalse() {
		domainClass = metaModel.register(CustomerWithoutOrderingReadOnlyAttribute.class);
		metaModel.done();
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertFalse(eAttribute.isOrdered());
		assertFalse(domainClass.isOrdered(eAttribute));
	}

	public void incompletetestOrderingOfEAttributeWhenSpecifiedWithoutUpperBound() {
		// TODO
	}


}
