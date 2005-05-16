package de.berlios.rcpviewer.progmodel.standard;

import org.eclipse.emf.ecore.EAttribute;

import de.berlios.rcpviewer.AbstractTestCase;
import de.berlios.rcpviewer.metamodel.IDomainClass;
import de.berlios.rcpviewer.metamodel.Domain;

public class TestDomainClassAttributesOrdering extends AbstractTestCase {

	@InDomain
	public static class CustomerWithNoOrderingReadOnlyAttribute {
		private String surname;
		public String getSurname() {
			return surname;
		}
	}
	@InDomain
	public static class CustomerWithOrderingReadOnlyAttribute {
		private String surname;
		@UpperBoundOf(3)
		@Ordered
		public String getSurname() {
			return surname;
		}
	}
	@InDomain
	public static class CustomerWithoutOrderingReadOnlyAttribute {
		private String surname;
		@UpperBoundOf(3)
		@Ordered(false)
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
	
	// ordering //

	public void testOrderingOfEAttributeWhenNoneSpecified() {
		domainClass = domain.lookup(CustomerWithNoOrderingReadOnlyAttribute.class);
		domain.done();
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertTrue(eAttribute.isOrdered());
		assertTrue(domainClass.isOrdered(eAttribute));
	}

	public void testOrderingOfEAttributeWhenSpecifiedAsTrue() {
		domainClass = domain.lookup(CustomerWithOrderingReadOnlyAttribute.class);
		domain.done();
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertTrue(eAttribute.isOrdered());
		assertTrue(domainClass.isOrdered(eAttribute));
	}

	public void testOrderingOfEAttributeWhenSpecifiedAsFalse() {
		domainClass = domain.lookup(CustomerWithoutOrderingReadOnlyAttribute.class);
		domain.done();
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertFalse(eAttribute.isOrdered());
		assertFalse(domainClass.isOrdered(eAttribute));
	}

	public void incompletetestOrderingOfEAttributeWhenSpecifiedWithoutUpperBound() {
		// TODO
	}


}
