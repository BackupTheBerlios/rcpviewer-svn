package org.essentialplatform.progmodel.standard.domainclass;

import org.essentialplatform.AbstractTestCase;
import org.essentialplatform.core.domain.IDomainClass;

/**
 * Tests for the use of the <tt>Immutable</tt>.
 * 
 * @author Dan Haywood
 */
public abstract class TestDomainClassSaveable extends AbstractTestCase {

	private IDomainClass domainClass;
	
	public void testPersistableDomainClass() {
		domainClass = lookupAny(SaveableOrderSummary.class);

		assertTrue(domainClass.isSaveable());
	}
	
	public void testNonPersistableDomainClass() {
		domainClass = lookupAny(NonSaveableOrderSummary.class);

		assertFalse(domainClass.isSaveable());
	}
	
}
