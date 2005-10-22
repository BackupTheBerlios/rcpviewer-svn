package de.berlios.rcpviewer.progmodel.standard.attribute;

import org.eclipse.emf.ecore.EAttribute;

import de.berlios.rcpviewer.AbstractTestCase;
import de.berlios.rcpviewer.IDeploymentSpecifics;
import de.berlios.rcpviewer.domain.IDomainBuilder;
import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.progmodel.standard.CustomerWithLowerBoundReadOnlyAttribute;
import de.berlios.rcpviewer.progmodel.standard.CustomerWithNoLowerBoundReadOnlyAttribute;
import de.berlios.rcpviewer.progmodel.standard.CustomerWithNoUpperBoundReadOnlyAttribute;
import de.berlios.rcpviewer.progmodel.standard.CustomerWithUpperBoundReadOnlyAttribute;

public abstract class TestDomainClassAttributesCardinality extends AbstractTestCase {

	public TestDomainClassAttributesCardinality(IDeploymentSpecifics domainSpecifics, IDomainBuilder domainAnalyzer) {
		super(domainSpecifics, domainAnalyzer);
	}

	private IDomainClass<?> domainClass;
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	// lower & upper bounds //

	public void testLowerBoundOfEAttributeWhenNoneSpecified() {
		domainClass = lookupAny(CustomerWithNoLowerBoundReadOnlyAttribute.class);
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertEquals(1, eAttribute.getLowerBound());
		assertEquals(1, domainClass.getAttribute(eAttribute).getLowerBound());
	}

	public void testLowerBoundOfEAttributeWhenSpecified() {
		domainClass = lookupAny(CustomerWithLowerBoundReadOnlyAttribute.class);
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertEquals(0, eAttribute.getLowerBound());
		assertEquals(0, domainClass.getAttribute(eAttribute).getLowerBound());
	}

	public void incompletetestLowerBoundOfEAttributeWithDatatypeNotSupporting() {
		// TODO
		
	}

	public void testUpperBoundOfEAttributeWhenNoneSpecified() {
		domainClass = lookupAny(CustomerWithNoUpperBoundReadOnlyAttribute.class);
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertEquals(1, eAttribute.getUpperBound());
		assertEquals(1, domainClass.getAttribute(eAttribute).getLowerBound());
	}

	public void testUpperBoundOfEAttributeWhenSpecified() {
		domainClass = lookupAny(CustomerWithUpperBoundReadOnlyAttribute.class);
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertEquals(3, eAttribute.getUpperBound());
		assertEquals(3, domainClass.getAttribute(eAttribute).getUpperBound());
	}

	public void incompletetestUpperBoundOfEAttributeWithDatatypeNotSupporting() {
		// TODO
	}


}
