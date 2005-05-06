package de.berlios.rcpviewer.progmodel.standard;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;

import de.berlios.rcpviewer.AbstractTestCase;
import de.berlios.rcpviewer.metamodel.*;
import de.berlios.rcpviewer.progmodel.standard.Derived;
import de.berlios.rcpviewer.progmodel.standard.DomainClass;
import de.berlios.rcpviewer.progmodel.standard.LowerBoundOf;
import de.berlios.rcpviewer.progmodel.standard.Ordered;
import de.berlios.rcpviewer.progmodel.standard.Unique;
import de.berlios.rcpviewer.progmodel.standard.UpperBoundOf;

import junit.framework.TestCase;

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

	private IDomainClass domainClass;
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	// ordering //

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


}
