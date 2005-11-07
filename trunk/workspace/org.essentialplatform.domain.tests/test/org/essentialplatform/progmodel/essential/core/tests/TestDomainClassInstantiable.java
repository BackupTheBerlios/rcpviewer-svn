package org.essentialplatform.progmodel.essential.core.tests;

import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.fixture.progmodel.essential.standard.domainclass.InstantiableProduct;
import org.essentialplatform.core.fixture.progmodel.essential.standard.domainclass.NonInstantiableProduct;
import org.essentialplatform.core.tests.AbstractTestCase;

/**
 * Tests for the use of the <tt>Immutable</tt>.
 * 
 * @author Dan Haywood
 */
public abstract class TestDomainClassInstantiable extends AbstractTestCase {

	private IDomainClass domainClass;
	
	public void testInstantiableDomainClass() {
		domainClass = lookupAny(InstantiableProduct.class);

		assertTrue(domainClass.isInstantiable());
		
	}
	
	public void testNonInstantiableDomainClass() {
		domainClass = lookupAny(NonInstantiableProduct.class);

		assertFalse(domainClass.isInstantiable());
	}
	
}
