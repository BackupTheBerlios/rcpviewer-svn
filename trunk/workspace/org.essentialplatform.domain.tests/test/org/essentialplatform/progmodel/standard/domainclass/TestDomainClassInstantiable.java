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
public abstract class TestDomainClassInstantiable extends AbstractTestCase {

	public TestDomainClassInstantiable(IDeploymentSpecifics domainSpecifics, IDomainBuilder domainBuilder) {
		super(domainSpecifics, domainBuilder);
	}

	private IDomainClass domainClass;
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testInstantiableDomainClass() {
		domainClass = 
			lookupAny(InstantiableProduct.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();

		assertTrue(domainClass.isInstantiable());
		
	}
	
	public void testNonInstantiableDomainClass() {
		domainClass = 
			lookupAny(NonInstantiableProduct.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();

		assertFalse(domainClass.isInstantiable());
	}
	
}
