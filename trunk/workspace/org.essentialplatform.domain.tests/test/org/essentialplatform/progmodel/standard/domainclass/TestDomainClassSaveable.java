package org.essentialplatform.progmodel.standard.domainclass;

import org.essentialplatform.AbstractTestCase;
import org.essentialplatform.IDeploymentSpecifics;
import org.essentialplatform.domain.IDomainBuilder;
import org.essentialplatform.domain.IDomainClass;

/**
 * Tests for the use of the <tt>Immutable</tt>.
 * 
 * @author Dan Haywood
 */
public abstract class TestDomainClassSaveable extends AbstractTestCase {

	public TestDomainClassSaveable(IDeploymentSpecifics domainSpecifics, IDomainBuilder domainBuilder) {
		super(domainSpecifics, domainBuilder);
	}

	private IDomainClass domainClass;
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testPersistableDomainClass() {
		domainClass = 
			lookupAny(SaveableOrderSummary.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();

		assertTrue(domainClass.isSaveable());
		
	}
	
	public void testNonPersistableDomainClass() {
		domainClass = 
			lookupAny(NonSaveableOrderSummary.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();

		assertFalse(domainClass.isSaveable());
	}
	
}
