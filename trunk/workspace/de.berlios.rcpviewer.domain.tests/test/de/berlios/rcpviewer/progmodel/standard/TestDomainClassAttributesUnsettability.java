package de.berlios.rcpviewer.progmodel.standard;

import org.eclipse.emf.ecore.EAttribute;

import de.berlios.rcpviewer.AbstractTestCase;
import de.berlios.rcpviewer.domain.Domain;
import de.berlios.rcpviewer.domain.IDomainClass;

public class TestDomainClassAttributesUnsettability extends AbstractTestCase {

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
	
	public void testWhetherEAttributeIsUnsettableWhenIs() {
		domainClass = Domain.lookup(TestDomainClassAttributesUnsettabilityCustomerWithUnsettableAttribute.class);
		domainClass.getDomain().done();
		EAttribute eAttribute = domainClass.getEAttributeNamed("age");
		assertTrue(eAttribute.isUnsettable());
		assertTrue(domainClass.isUnsettable(eAttribute));
	}

	public void testWhetherEAttributeIsUnsettableWhenNotDueToMissingUnsetMethod() {
		domainClass = Domain.lookup(TestDomainClassAttributesUnsettabilityCustomerWithOnlyIsUnsetForAttribute.class);
		domainClass.getDomain().done();
		EAttribute eAttribute = domainClass.getEAttributeNamed("age");
		assertFalse(eAttribute.isUnsettable());
		assertFalse(domainClass.isUnsettable(eAttribute));
	}
	public void testWhetherEAttributeIsUnsettableWhenNotDueToMissingIsUnsetMethod() {
		domainClass = Domain.lookup(TestDomainClassAttributesUnsettabilityCustomerWithOnlyUnsetForAttribute.class);
		domainClass.getDomain().done();
		EAttribute eAttribute = domainClass.getEAttributeNamed("age");
		assertFalse(eAttribute.isUnsettable());
		assertFalse(domainClass.isUnsettable(eAttribute));
	}

	
	

}
