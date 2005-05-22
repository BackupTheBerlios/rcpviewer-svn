package de.berlios.rcpviewer.progmodel.standard;

import org.eclipse.emf.ecore.EAttribute;

import de.berlios.rcpviewer.AbstractTestCase;
import de.berlios.rcpviewer.domain.Domain;
import de.berlios.rcpviewer.domain.IDomainClass;

public class TestDomainClassAttributesOrdering extends AbstractTestCase {

	private Domain domain;
	private IDomainClass<?> domainClass;
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		Domain.reset();
		domain = null;
		super.tearDown();
	}
	
	// ordering //

	public void testOrderingOfEAttributeWhenNoneSpecified() {
		domainClass = Domain.lookup(TestDomainClassAttributesOrderingCustomerWithNoOrderingReadOnlyAttribute.class);
		domainClass.getDomain().done();
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertTrue(eAttribute.isOrdered());
		assertTrue(domainClass.isOrdered(eAttribute));
	}

	public void testOrderingOfEAttributeWhenSpecifiedAsTrue() {
		domainClass = Domain.lookup(TestDomainClassAttributesOrderingCustomerWithOrderingReadOnlyAttribute.class);
		domainClass.getDomain().done();
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertTrue(eAttribute.isOrdered());
		assertTrue(domainClass.isOrdered(eAttribute));
	}

	public void testOrderingOfEAttributeWhenSpecifiedAsFalse() {
		domainClass = Domain.lookup(TestDomainClassAttributesOrderingCustomerWithoutOrderingReadOnlyAttribute.class);
		domainClass.getDomain().done();
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertFalse(eAttribute.isOrdered());
		assertFalse(domainClass.isOrdered(eAttribute));
	}

	public void incompletetestOrderingOfEAttributeWhenSpecifiedWithoutUpperBound() {
		// TODO
	}


}
