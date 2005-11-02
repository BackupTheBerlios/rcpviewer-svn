package org.essentialplatform.progmodel.standard.attribute;

import org.eclipse.emf.ecore.EAttribute;
import org.essentialplatform.AbstractTestCase;
import org.essentialplatform.domain.IDomainBuilder;
import org.essentialplatform.domain.IDomainClass;

public abstract class TestDomainClassAttributesOrdering extends AbstractTestCase {

	private IDomainClass domainClass;
	
	public void testOrderingOfEAttributeWhenNoneSpecified() {
		domainClass = lookupAny(CustomerWithNoOrderingReadOnlyAttribute.class);
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertTrue(eAttribute.isOrdered());
		assertTrue(domainClass.getAttribute(eAttribute).isOrdered());
	}

	public void testOrderingOfEAttributeWhenSpecifiedAsTrue() {
		domainClass = lookupAny(CustomerWithOrderingReadOnlyAttribute.class);
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertTrue(eAttribute.isOrdered());
		assertTrue(domainClass.getAttribute(eAttribute).isOrdered());
	}

	public void testOrderingOfEAttributeWhenSpecifiedAsFalse() {
		domainClass = lookupAny(CustomerWithoutOrderingReadOnlyAttribute.class);
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertFalse(eAttribute.isOrdered());
		assertFalse(domainClass.getAttribute(eAttribute).isOrdered());
	}

	public void incompletetestOrderingOfEAttributeWhenSpecifiedWithoutUpperBound() {
		// TODO
	}


}
