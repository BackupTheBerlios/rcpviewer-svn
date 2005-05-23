package de.berlios.rcpviewer.progmodel.standard;

import org.eclipse.emf.ecore.EAttribute;

import de.berlios.rcpviewer.AbstractTestCase;
import de.berlios.rcpviewer.domain.Domain;
import de.berlios.rcpviewer.domain.IDomainClass;

public class TestDomainClassAttributesOrdering extends AbstractTestCase {

	private IDomainClass<?> domainClass;
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testOrderingOfEAttributeWhenNoneSpecified() {
		domainClass = Domain.lookupAny(TestDomainClassAttributesOrderingCustomerWithNoOrderingReadOnlyAttribute.class);
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertTrue(eAttribute.isOrdered());
		assertTrue(domainClass.isOrdered(eAttribute));
	}

	public void testOrderingOfEAttributeWhenSpecifiedAsTrue() {
		domainClass = Domain.lookupAny(TestDomainClassAttributesOrderingCustomerWithOrderingReadOnlyAttribute.class);
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertTrue(eAttribute.isOrdered());
		assertTrue(domainClass.isOrdered(eAttribute));
	}

	public void testOrderingOfEAttributeWhenSpecifiedAsFalse() {
		domainClass = Domain.lookupAny(TestDomainClassAttributesOrderingCustomerWithoutOrderingReadOnlyAttribute.class);
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertFalse(eAttribute.isOrdered());
		assertFalse(domainClass.isOrdered(eAttribute));
	}

	public void incompletetestOrderingOfEAttributeWhenSpecifiedWithoutUpperBound() {
		// TODO
	}


}
