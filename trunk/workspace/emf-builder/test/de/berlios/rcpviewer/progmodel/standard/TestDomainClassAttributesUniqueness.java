package de.berlios.rcpviewer.progmodel.standard;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;

import de.berlios.rcpviewer.AbstractTestCase;
import de.berlios.rcpviewer.metamodel.*;
import de.berlios.rcpviewer.progmodel.standard.Derived;
import de.berlios.rcpviewer.progmodel.standard.LowerBoundOf;
import de.berlios.rcpviewer.progmodel.standard.Ordered;
import de.berlios.rcpviewer.progmodel.standard.Unique;
import de.berlios.rcpviewer.progmodel.standard.UpperBoundOf;

import junit.framework.TestCase;

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

	private IDomainClass domainClass;
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	// uniqueness //

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




}
