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
		IDomainClass.IAttribute iAttribute = domainClass.getIAttributeNamed("surname");
		assertEquals(1, iAttribute.getLowerBound());
		assertEquals(1, iAttribute.getEAttribute().getLowerBound());
	}

	public void testLowerBoundOfEAttributeWhenSpecified() {
		domainClass = lookupAny(CustomerWithLowerBoundReadOnlyAttribute.class);
		IDomainClass.IAttribute iAttribute = domainClass.getIAttributeNamed("surname");
		assertEquals(0, iAttribute.getLowerBound());
		assertEquals(0, iAttribute.getEAttribute().getLowerBound());
	}

	public void incompletetestLowerBoundOfEAttributeWithDatatypeNotSupporting() {
		// TODO
		
	}

	public void testUpperBoundOfEAttributeWhenNoneSpecified() {
		domainClass = lookupAny(CustomerWithNoUpperBoundReadOnlyAttribute.class);
		IDomainClass.IAttribute iAttribute = domainClass.getIAttributeNamed("surname");
		assertEquals(1, iAttribute.getUpperBound());
		assertEquals(1, iAttribute.getEAttribute().getUpperBound());
	}

	public void testUpperBoundOfEAttributeWhenSpecified() {
		domainClass = lookupAny(CustomerWithUpperBoundReadOnlyAttribute.class);
		IDomainClass.IAttribute iAttribute = domainClass.getIAttributeNamed("surname");
		assertEquals(3, iAttribute.getUpperBound());
		assertEquals(3, iAttribute.getEAttribute().getUpperBound());
	}

	public void incompletetestUpperBoundOfEAttributeWithDatatypeNotSupporting() {
		// TODO
	}


}
