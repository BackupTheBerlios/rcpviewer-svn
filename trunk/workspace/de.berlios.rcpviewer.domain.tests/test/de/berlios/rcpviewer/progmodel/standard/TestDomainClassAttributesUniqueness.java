package de.berlios.rcpviewer.progmodel.standard;

import org.eclipse.emf.ecore.EAttribute;

import de.berlios.rcpviewer.AbstractTestCase;
import de.berlios.rcpviewer.domain.Domain;
import de.berlios.rcpviewer.domain.IDomainClass;

public class TestDomainClassAttributesUniqueness extends AbstractTestCase {

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
	
	// uniqueness //

	public void testUniquenessOfEAttributeWhenNoneSpecified() {
		domainClass = Domain.lookup(TestDomainClassAttributesUniquenessCustomerWithNoUniquenessReadOnlyAttribute.class);
		domainClass.getDomain().done();
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertTrue(eAttribute.isUnique());
		assertTrue(domainClass.isUnique(eAttribute));
	}

	public void testUniquenessOfEAttributeWhenSpecifiedAsTrue() {
		domainClass = Domain.lookup(TestDomainClassAttributesUniquenessCustomerWithUniqueReadOnlyAttribute.class);
		domainClass.getDomain().done();
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertTrue(eAttribute.isUnique());
		assertTrue(domainClass.isUnique(eAttribute));
	}

	public void testUniquenessOfEAttributeWhenSpecifiedAsFalse() {
		domainClass = Domain.lookup(TestDomainClassAttributesUniquenessCustomerWithNonUniqueReadOnlyAttribute.class);
		domainClass.getDomain().done();
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertFalse(eAttribute.isUnique());
		assertFalse(domainClass.isUnique(eAttribute));
	}

	public void incompletetestUniquenessOfEAttributeWhenSpecifiedWithoutUpperBound() {
		// TODO
	}




}
