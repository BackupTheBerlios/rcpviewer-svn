package de.berlios.rcpviewer.progmodel.standard.attribute;

import org.eclipse.emf.ecore.EAttribute;

import de.berlios.rcpviewer.AbstractTestCase;
import de.berlios.rcpviewer.domain.Domain;
import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.progmodel.standard.attribute.TestDomainClassAttributesUniquenessCustomerWithNoUniquenessReadOnlyAttribute;
import de.berlios.rcpviewer.progmodel.standard.attribute.TestDomainClassAttributesUniquenessCustomerWithNonUniqueReadOnlyAttribute;
import de.berlios.rcpviewer.progmodel.standard.attribute.TestDomainClassAttributesUniquenessCustomerWithUniqueReadOnlyAttribute;

public class TestDomainClassAttributesUniqueness extends AbstractTestCase {

	private IDomainClass<?> domainClass;
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testUniquenessOfEAttributeWhenNoneSpecified() {
		domainClass = Domain.lookupAny(TestDomainClassAttributesUniquenessCustomerWithNoUniquenessReadOnlyAttribute.class);
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertTrue(eAttribute.isUnique());
		assertTrue(domainClass.isUnique(eAttribute));
	}

	public void testUniquenessOfEAttributeWhenSpecifiedAsTrue() {
		domainClass = Domain.lookupAny(TestDomainClassAttributesUniquenessCustomerWithUniqueReadOnlyAttribute.class);
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertTrue(eAttribute.isUnique());
		assertTrue(domainClass.isUnique(eAttribute));
	}

	public void testUniquenessOfEAttributeWhenSpecifiedAsFalse() {
		domainClass = Domain.lookupAny(TestDomainClassAttributesUniquenessCustomerWithNonUniqueReadOnlyAttribute.class);
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertFalse(eAttribute.isUnique());
		assertFalse(domainClass.isUnique(eAttribute));
	}

	public void incompletetestUniquenessOfEAttributeWhenSpecifiedWithoutUpperBound() {
		// TODO
	}




}
