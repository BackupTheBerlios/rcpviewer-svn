package org.essentialplatform.progmodel.standard.domainclass;

import org.essentialplatform.AbstractTestCase;
import org.essentialplatform.core.domain.IDomainClass;

/**
 * Tests for the use of the <tt>Immutable</tt>.
 * 
 * @author Dan Haywood
 */
public abstract class TestDomainClassImmutable extends AbstractTestCase {

	private IDomainClass domainClass;
	
	public void testImmutableDomainClass() {
		domainClass = lookupAny(ImmutableCustomerCategory.class);

		assertFalse(domainClass.isChangeable());
	}
	
	public void testNonImmutableDomainClass() {
		domainClass = lookupAny(MutableCustomer.class);

		assertTrue(domainClass.isChangeable());
	}
	
}
