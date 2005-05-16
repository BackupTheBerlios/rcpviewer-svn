package de.berlios.rcpviewer.progmodel.standard;

import org.eclipse.emf.ecore.EAttribute;

import de.berlios.rcpviewer.AbstractTestCase;
import de.berlios.rcpviewer.metamodel.IDomainClass;
import de.berlios.rcpviewer.metamodel.Domain;

public class TestDomainClassAttributesUniqueness extends AbstractTestCase {

	@InDomain
	public static class CustomerWithNoUniquenessReadOnlyAttribute {
		private String surname;
		public String getSurname() {
			return surname;
		}
	}
	@InDomain
	public static class CustomerWithUniqueReadOnlyAttribute {
		private String surname;
		@UpperBoundOf(3)
		@Unique
		public String getSurname() {
			return surname;
		}
	}
	@InDomain
	public static class CustomerWithNonUniqueReadOnlyAttribute {
		private String surname;
		@UpperBoundOf(3)
		@Unique(false)
		public String getSurname() {
			return surname;
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
	
	// uniqueness //

	public void testUniquenessOfEAttributeWhenNoneSpecified() {
		domainClass = domain.lookup(CustomerWithNoUniquenessReadOnlyAttribute.class);
		domain.done();
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertTrue(eAttribute.isUnique());
		assertTrue(domainClass.isUnique(eAttribute));
	}

	public void testUniquenessOfEAttributeWhenSpecifiedAsTrue() {
		domainClass = domain.lookup(CustomerWithUniqueReadOnlyAttribute.class);
		domain.done();
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertTrue(eAttribute.isUnique());
		assertTrue(domainClass.isUnique(eAttribute));
	}

	public void testUniquenessOfEAttributeWhenSpecifiedAsFalse() {
		domainClass = domain.lookup(CustomerWithNonUniqueReadOnlyAttribute.class);
		domain.done();
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertFalse(eAttribute.isUnique());
		assertFalse(domainClass.isUnique(eAttribute));
	}

	public void incompletetestUniquenessOfEAttributeWhenSpecifiedWithoutUpperBound() {
		// TODO
	}




}
