package org.essentialplatform.progmodel.essential.core.tests;

import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.fixture.progmodel.essential.standard.domainclass.NonSaveableOrderSummary;
import org.essentialplatform.core.fixture.progmodel.essential.standard.domainclass.SaveableOrderSummary;
import org.essentialplatform.core.tests.AbstractTestCase;

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
