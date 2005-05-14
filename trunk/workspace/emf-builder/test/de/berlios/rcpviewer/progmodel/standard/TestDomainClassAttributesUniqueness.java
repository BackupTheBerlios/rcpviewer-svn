package de.berlios.rcpviewer.progmodel.standard;

import org.eclipse.emf.ecore.EAttribute;

import de.berlios.rcpviewer.AbstractTestCase;
import de.berlios.rcpviewer.metamodel.IDomainClass;
import de.berlios.rcpviewer.metamodel.MetaModel;

public class TestDomainClassAttributesUniqueness extends AbstractTestCase {

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
	
	// uniqueness //

	public void testUniquenessOfEAttributeWhenNoneSpecified() {
		domainClass = metaModel.lookup(CustomerWithNoUniquenessReadOnlyAttribute.class);
		metaModel.done();
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertTrue(eAttribute.isUnique());
		assertTrue(domainClass.isUnique(eAttribute));
	}

	public void testUniquenessOfEAttributeWhenSpecifiedAsTrue() {
		domainClass = metaModel.lookup(CustomerWithUniqueReadOnlyAttribute.class);
		metaModel.done();
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertTrue(eAttribute.isUnique());
		assertTrue(domainClass.isUnique(eAttribute));
	}

	public void testUniquenessOfEAttributeWhenSpecifiedAsFalse() {
		domainClass = metaModel.lookup(CustomerWithNonUniqueReadOnlyAttribute.class);
		metaModel.done();
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertFalse(eAttribute.isUnique());
		assertFalse(domainClass.isUnique(eAttribute));
	}

	public void incompletetestUniquenessOfEAttributeWhenSpecifiedWithoutUpperBound() {
		// TODO
	}




}
