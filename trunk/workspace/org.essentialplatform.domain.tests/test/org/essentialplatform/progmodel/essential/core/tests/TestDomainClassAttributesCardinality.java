package org.essentialplatform.progmodel.essential.core.tests;

import org.eclipse.emf.ecore.EAttribute;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.fixture.progmodel.essential.standard.CustomerWithLowerBoundReadOnlyAttribute;
import org.essentialplatform.core.fixture.progmodel.essential.standard.CustomerWithNoLowerBoundReadOnlyAttribute;
import org.essentialplatform.core.fixture.progmodel.essential.standard.CustomerWithNoUpperBoundReadOnlyAttribute;
import org.essentialplatform.core.fixture.progmodel.essential.standard.CustomerWithUpperBoundReadOnlyAttribute;
import org.essentialplatform.core.tests.AbstractTestCase;

public abstract class TestDomainClassAttributesCardinality extends AbstractTestCase {

	private IDomainClass domainClass;
	
	// lower & upper bounds //

	public void testLowerBoundOfEAttributeWhenNoneSpecified() {
		domainClass = lookupAny(CustomerWithNoLowerBoundReadOnlyAttribute.class);
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertEquals(1, eAttribute.getLowerBound());
		assertEquals(1, domainClass.getIAttribute(eAttribute).getLowerBound());
	}

	public void testLowerBoundOfEAttributeWhenSpecified() {
		domainClass = lookupAny(CustomerWithLowerBoundReadOnlyAttribute.class);
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertEquals(0, eAttribute.getLowerBound());
		assertEquals(0, domainClass.getIAttribute(eAttribute).getLowerBound());
	}

	public void incompletetestLowerBoundOfEAttributeWithDatatypeNotSupporting() {
		// TODO
		
	}

	public void testUpperBoundOfEAttributeWhenNoneSpecified() {
		domainClass = lookupAny(CustomerWithNoUpperBoundReadOnlyAttribute.class);
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertEquals(1, eAttribute.getUpperBound());
		assertEquals(1, domainClass.getIAttribute(eAttribute).getLowerBound());
	}

	public void testUpperBoundOfEAttributeWhenSpecified() {
		domainClass = lookupAny(CustomerWithUpperBoundReadOnlyAttribute.class);
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertEquals(3, eAttribute.getUpperBound());
		assertEquals(3, domainClass.getIAttribute(eAttribute).getUpperBound());
	}

	public void incompletetestUpperBoundOfEAttributeWithDatatypeNotSupporting() {
		// TODO
	}


}
