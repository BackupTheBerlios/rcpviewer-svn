package org.essentialplatform.progmodel.standard.domainclass;

import org.essentialplatform.AbstractTestCase;
import org.essentialplatform.core.domain.IDomainClass;

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
