package de.berlios.rcpviewer.progmodel.standard.domainclass;

import java.util.List;

import org.eclipse.emf.ecore.EAttribute;

import de.berlios.rcpviewer.AbstractTestCase;
import de.berlios.rcpviewer.IDeploymentSpecifics;
import de.berlios.rcpviewer.domain.Domain;
import de.berlios.rcpviewer.domain.IDomainBuilder;
import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.progmodel.standard.domainclass.ImmutableCustomerCategory;

/**
 * Tests for the use of the <tt>Immutable</tt>.
 * 
 * @author Dan Haywood
 */
public abstract class TestDomainClassImmutable extends AbstractTestCase {

	public TestDomainClassImmutable(IDeploymentSpecifics domainSpecifics, IDomainBuilder domainAnalyzer) {
		super(domainSpecifics, domainAnalyzer);
	}

	private IDomainClass<?> domainClass;
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testImmutableDomainClass() {
		domainClass = 
			lookupAny(ImmutableCustomerCategory.class);
		getDomainInstance().addBuilder(getDomainAnalyzer());
		getDomainInstance().done();

		assertFalse(domainClass.isChangeable());
		
	}
	
	public void testNonImmutableDomainClass() {
		domainClass = 
			lookupAny(MutableCustomer.class);
		getDomainInstance().addBuilder(getDomainAnalyzer());
		getDomainInstance().done();

		assertTrue(domainClass.isChangeable());
	}
	
}