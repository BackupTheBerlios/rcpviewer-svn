package de.berlios.rcpviewer.progmodel.standard.domainclass;

import de.berlios.rcpviewer.AbstractTestCase;
import de.berlios.rcpviewer.IDeploymentSpecifics;
import de.berlios.rcpviewer.domain.IDomainBuilder;
import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.progmodel.extended.ExtendedDomainClass;

/**
 * Tests for the use of the <tt>Immutable</tt>.
 * 
 * @author Dan Haywood
 */
public abstract class TestDomainClassSaveable extends AbstractTestCase {

	public TestDomainClassSaveable(IDeploymentSpecifics domainSpecifics, IDomainBuilder domainBuilder) {
		super(domainSpecifics, domainBuilder);
	}

	private IDomainClass<?> domainClass;
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

		ExtendedDomainClass extendedDomainClass = 
			domainClass.getAdapter(ExtendedDomainClass.class);
		assertTrue(extendedDomainClass.isSaveable());
		
	}
	
	public void testNonPersistableDomainClass() {
		domainClass = 
			lookupAny(NonSaveableOrderSummary.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();

		ExtendedDomainClass extendedDomainClass = 
			domainClass.getAdapter(ExtendedDomainClass.class);
		assertFalse(extendedDomainClass.isSaveable());
	}
	
}
