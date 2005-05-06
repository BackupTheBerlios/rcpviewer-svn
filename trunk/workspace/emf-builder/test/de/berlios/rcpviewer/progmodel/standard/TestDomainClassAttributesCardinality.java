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

public class TestDomainClassAttributesCardinality extends AbstractTestCase {

	public static class CustomerWithNoLowerBoundReadOnlyAttribute {
		private String surname;
		public String getSurname() {
			return surname;
		}
	}
	public static class CustomerWithLowerBoundReadOnlyAttribute {
		private String surname;
		@LowerBoundOf(0)
		public String getSurname() {
			return surname;
		}
	}
	public static class CustomerWithNoUpperBoundReadOnlyAttribute {
		private String surname;
		public String getSurname() {
			return surname;
		}
	}
	public static class CustomerWithUpperBoundReadOnlyAttribute {
		private String surname;
		@UpperBoundOf(3)
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
	
	// lower & upper bounds //

	public void testLowerBoundOfEAttributeWhenNoneSpecified() {
		domainClass = new DomainClass(CustomerWithNoLowerBoundReadOnlyAttribute.class);
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertEquals(1, eAttribute.getLowerBound());
		assertEquals(1, domainClass.getLowerBound(eAttribute));
	}

	public void testLowerBoundOfEAttributeWhenSpecified() {
		domainClass = new DomainClass(CustomerWithLowerBoundReadOnlyAttribute.class);
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertEquals(0, eAttribute.getLowerBound());
		assertEquals(0, domainClass.getLowerBound(eAttribute));
	}

	public void incompletetestLowerBoundOfEAttributeWithDatatypeNotSupporting() {
		// TODO
		
	}

	public void testUpperBoundOfEAttributeWhenNoneSpecified() {
		domainClass = new DomainClass(CustomerWithNoUpperBoundReadOnlyAttribute.class);
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertEquals(1, eAttribute.getUpperBound());
		assertEquals(1, domainClass.getLowerBound(eAttribute));
	}

	public void testUpperBoundOfEAttributeWhenSpecified() {
		domainClass = new DomainClass(CustomerWithUpperBoundReadOnlyAttribute.class);
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertEquals(3, eAttribute.getUpperBound());
		assertEquals(3, domainClass.getUpperBound(eAttribute));
	}

	public void incompletetestUpperBoundOfEAttributeWithDatatypeNotSupporting() {
		// TODO
	}


}
