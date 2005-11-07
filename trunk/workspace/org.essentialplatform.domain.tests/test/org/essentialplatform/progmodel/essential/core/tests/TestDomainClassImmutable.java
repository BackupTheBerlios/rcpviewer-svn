package org.essentialplatform.progmodel.essential.core.tests;

import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.fixture.progmodel.essential.standard.domainclass.ImmutableCustomerCategory;
import org.essentialplatform.core.fixture.progmodel.essential.standard.domainclass.MutableCustomer;
import org.essentialplatform.core.tests.AbstractTestCase;

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
