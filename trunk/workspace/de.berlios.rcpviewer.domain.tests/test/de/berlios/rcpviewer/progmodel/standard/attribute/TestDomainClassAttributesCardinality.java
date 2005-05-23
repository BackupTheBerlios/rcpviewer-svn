package de.berlios.rcpviewer.progmodel.standard.attribute;

import org.eclipse.emf.ecore.EAttribute;

import de.berlios.rcpviewer.AbstractTestCase;
import de.berlios.rcpviewer.domain.Domain;
import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.progmodel.standard.CustomerWithLowerBoundReadOnlyAttribute;
import de.berlios.rcpviewer.progmodel.standard.CustomerWithNoLowerBoundReadOnlyAttribute;
import de.berlios.rcpviewer.progmodel.standard.CustomerWithNoUpperBoundReadOnlyAttribute;
import de.berlios.rcpviewer.progmodel.standard.CustomerWithUpperBoundReadOnlyAttribute;

public class TestDomainClassAttributesCardinality extends AbstractTestCase {

	private IDomainClass<?> domainClass;
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		Domain.resetAll();
		domain = null;
		super.tearDown();
	}
	
	// lower & upper bounds //

	public void testLowerBoundOfEAttributeWhenNoneSpecified() {
		domainClass = Domain.lookupAny(CustomerWithNoLowerBoundReadOnlyAttribute.class);
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertEquals(1, eAttribute.getLowerBound());
		assertEquals(1, domainClass.getLowerBound(eAttribute));
	}

	public void testLowerBoundOfEAttributeWhenSpecified() {
		domainClass = Domain.lookupAny(CustomerWithLowerBoundReadOnlyAttribute.class);
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertEquals(0, eAttribute.getLowerBound());
		assertEquals(0, domainClass.getLowerBound(eAttribute));
	}

	public void incompletetestLowerBoundOfEAttributeWithDatatypeNotSupporting() {
		// TODO
		
	}

	public void testUpperBoundOfEAttributeWhenNoneSpecified() {
		domainClass = Domain.lookupAny(CustomerWithNoUpperBoundReadOnlyAttribute.class);
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertEquals(1, eAttribute.getUpperBound());
		assertEquals(1, domainClass.getLowerBound(eAttribute));
	}

	public void testUpperBoundOfEAttributeWhenSpecified() {
		domainClass = Domain.lookupAny(CustomerWithUpperBoundReadOnlyAttribute.class);
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertEquals(3, eAttribute.getUpperBound());
		assertEquals(3, domainClass.getUpperBound(eAttribute));
	}

	public void incompletetestUpperBoundOfEAttributeWithDatatypeNotSupporting() {
		// TODO
	}


}
