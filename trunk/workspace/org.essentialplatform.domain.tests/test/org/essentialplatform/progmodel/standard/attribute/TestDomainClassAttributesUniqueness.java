package org.essentialplatform.progmodel.standard.attribute;

import org.eclipse.emf.ecore.EAttribute;
import org.essentialplatform.AbstractTestCase;
import org.essentialplatform.core.domain.IDomainClass;

public abstract class TestDomainClassAttributesUniqueness extends AbstractTestCase {

	private IDomainClass domainClass;
	public void testUniquenessOfEAttributeWhenNoneSpecified() {
		domainClass = lookupAny(CustomerWithNoUniquenessReadOnlyAttribute.class);
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertTrue(eAttribute.isUnique());
		assertTrue(domainClass.getIAttribute(eAttribute).isUnique());
	}

	public void testUniquenessOfEAttributeWhenSpecifiedAsTrue() {
		domainClass = lookupAny(CustomerWithUniqueReadOnlyAttribute.class);
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertTrue(eAttribute.isUnique());
		assertTrue(domainClass.getIAttribute(eAttribute).isUnique());
	}

	public void testUniquenessOfEAttributeWhenSpecifiedAsFalse() {
		domainClass = lookupAny(CustomerWithNonUniqueReadOnlyAttribute.class);
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertFalse(eAttribute.isUnique());
		assertFalse(domainClass.getIAttribute(eAttribute).isUnique());
	}

	public void incompletetestUniquenessOfEAttributeWhenSpecifiedWithoutUpperBound() {
		// TODO
	}




}
