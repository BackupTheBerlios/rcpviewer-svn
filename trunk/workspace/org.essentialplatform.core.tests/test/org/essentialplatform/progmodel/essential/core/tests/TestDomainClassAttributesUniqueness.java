package org.essentialplatform.progmodel.essential.core.tests;

import org.eclipse.emf.ecore.EAttribute;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.fixture.progmodel.essential.standard.attribute.CustomerWithNoUniquenessReadOnlyAttribute;
import org.essentialplatform.core.fixture.progmodel.essential.standard.attribute.CustomerWithNonUniqueReadOnlyAttribute;
import org.essentialplatform.core.fixture.progmodel.essential.standard.attribute.CustomerWithUniqueReadOnlyAttribute;
import org.essentialplatform.core.tests.AbstractTestCase;

public abstract class TestDomainClassAttributesUniqueness extends AbstractTestCase {

	private IDomainClass domainClass;
	public void testUniquenessOfEAttributeWhenNoneSpecified() {
		domainClass = lookupAny(CustomerWithNoUniquenessReadOnlyAttribute.class);
		IDomainClass.IAttribute iAttribute = domainClass.getIAttributeNamed("surname");
		assertTrue(iAttribute.isUnique());
		assertTrue(iAttribute.getEAttribute().isUnique());
	}

	public void testUniquenessOfEAttributeWhenSpecifiedAsTrue() {
		domainClass = lookupAny(CustomerWithUniqueReadOnlyAttribute.class);
		IDomainClass.IAttribute iAttribute = domainClass.getIAttributeNamed("surname");
		assertTrue(iAttribute.isUnique());
		assertTrue(iAttribute.getEAttribute().isUnique());
	}

	public void testUniquenessOfEAttributeWhenSpecifiedAsFalse() {
		domainClass = lookupAny(CustomerWithNonUniqueReadOnlyAttribute.class);
		IDomainClass.IAttribute iAttribute = domainClass.getIAttributeNamed("surname");
		assertFalse(iAttribute.isUnique());
		assertFalse(iAttribute.getEAttribute().isUnique());
	}

	public void incompletetestUniquenessOfEAttributeWhenSpecifiedWithoutUpperBound() {
		// TODO
	}




}
