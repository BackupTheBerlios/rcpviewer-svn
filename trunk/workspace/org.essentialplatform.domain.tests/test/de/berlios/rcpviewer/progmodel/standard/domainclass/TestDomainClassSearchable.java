package de.berlios.rcpviewer.progmodel.standard.domainclass;

import de.berlios.rcpviewer.AbstractTestCase;
import de.berlios.rcpviewer.IDeploymentSpecifics;
import de.berlios.rcpviewer.domain.IDomainBuilder;
import de.berlios.rcpviewer.domain.IDomainClass;

/**
 * Tests for the use of the <tt>Immutable</tt>.
 * 
 * @author Dan Haywood
 */
public abstract class TestDomainClassSearchable extends AbstractTestCase {

	public TestDomainClassSearchable(IDeploymentSpecifics domainSpecifics, IDomainBuilder domainAnalyzer) {
		super(domainSpecifics, domainAnalyzer);
	}

	private IDomainClass domainClass;
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testSearchableDomainClass() {
		domainClass = 
			lookupAny(SearchableCustomer.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();

		assertTrue(domainClass.isSearchable());
	}
	
	public void testNonPersistableDomainClass() {
		domainClass = 
			lookupAny(NonSearchableCustomer.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();

		assertFalse(domainClass.isSearchable());
	}
	
}
