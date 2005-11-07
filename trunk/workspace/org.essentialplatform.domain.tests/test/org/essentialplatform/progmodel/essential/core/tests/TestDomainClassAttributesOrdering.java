package org.essentialplatform.progmodel.essential.core.tests;

import org.eclipse.emf.ecore.EAttribute;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.fixture.progmodel.essential.standard.attribute.CustomerWithNoOrderingReadOnlyAttribute;
import org.essentialplatform.core.fixture.progmodel.essential.standard.attribute.CustomerWithOrderingReadOnlyAttribute;
import org.essentialplatform.core.fixture.progmodel.essential.standard.attribute.CustomerWithoutOrderingReadOnlyAttribute;
import org.essentialplatform.core.tests.AbstractTestCase;

public abstract class TestDomainClassAttributesOrdering extends AbstractTestCase {

	private IDomainClass domainClass;
	
	public void testOrderingOfEAttributeWhenNoneSpecified() {
		domainClass = lookupAny(CustomerWithNoOrderingReadOnlyAttribute.class);
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertTrue(eAttribute.isOrdered());
		assertTrue(domainClass.getIAttribute(eAttribute).isOrdered());
	}

	public void testOrderingOfEAttributeWhenSpecifiedAsTrue() {
		domainClass = lookupAny(CustomerWithOrderingReadOnlyAttribute.class);
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertTrue(eAttribute.isOrdered());
		assertTrue(domainClass.getIAttribute(eAttribute).isOrdered());
	}

	public void testOrderingOfEAttributeWhenSpecifiedAsFalse() {
		domainClass = lookupAny(CustomerWithoutOrderingReadOnlyAttribute.class);
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertFalse(eAttribute.isOrdered());
		assertFalse(domainClass.getIAttribute(eAttribute).isOrdered());
	}

	public void incompletetestOrderingOfEAttributeWhenSpecifiedWithoutUpperBound() {
		// TODO
	}


}
