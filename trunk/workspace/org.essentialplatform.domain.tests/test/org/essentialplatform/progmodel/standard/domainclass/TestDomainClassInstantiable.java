package org.essentialplatform.progmodel.standard.domainclass;

import org.essentialplatform.AbstractTestCase;
import org.essentialplatform.domain.IDomainClass;

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
