package org.essentialplatform.progmodel.essential.core.tests;

import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.fixture.progmodel.essential.standard.domainclass.NonSearchableCustomer;
import org.essentialplatform.core.fixture.progmodel.essential.standard.domainclass.SearchableCustomer;
import org.essentialplatform.core.tests.AbstractTestCase;

/**
 * Tests for the use of the <tt>Immutable</tt>.
 * 
 * @author Dan Haywood
 */
public abstract class TestDomainClassSearchable extends AbstractTestCase {

	private IDomainClass domainClass;
	
	public void testSearchableDomainClass() {
		domainClass = lookupAny(SearchableCustomer.class);

		assertTrue(domainClass.isSearchable());
	}
	
	public void testNonPersistableDomainClass() {
		domainClass = lookupAny(NonSearchableCustomer.class);

		assertFalse(domainClass.isSearchable());
	}
	
}
