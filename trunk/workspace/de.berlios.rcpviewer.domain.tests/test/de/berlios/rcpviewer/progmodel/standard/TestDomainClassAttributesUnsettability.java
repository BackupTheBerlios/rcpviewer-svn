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
		domain = Domain.instance();
	}

	protected void tearDown() throws Exception {
		domain.reset();
		domain = null;
		super.tearDown();
	}
	
	public void testWhetherEAttributeIsUnsettableWhenIs() {
		domainClass = domain.localLookup(TestDomainClassAttributesUnsettabilityCustomerWithUnsettableAttribute.class);
		domain.done();
		EAttribute eAttribute = domainClass.getEAttributeNamed("age");
		assertTrue(eAttribute.isUnsettable());
		assertTrue(domainClass.isUnsettable(eAttribute));
	}

	public void testWhetherEAttributeIsUnsettableWhenNotDueToMissingUnsetMethod() {
		domainClass = domain.localLookup(TestDomainClassAttributesUnsettabilityCustomerWithOnlyIsUnsetForAttribute.class);
		domain.done();
		EAttribute eAttribute = domainClass.getEAttributeNamed("age");
		assertFalse(eAttribute.isUnsettable());
		assertFalse(domainClass.isUnsettable(eAttribute));
	}
	public void testWhetherEAttributeIsUnsettableWhenNotDueToMissingIsUnsetMethod() {
		domainClass = domain.localLookup(TestDomainClassAttributesUnsettabilityCustomerWithOnlyUnsetForAttribute.class);
		domain.done();
		EAttribute eAttribute = domainClass.getEAttributeNamed("age");
		assertFalse(eAttribute.isUnsettable());
		assertFalse(domainClass.isUnsettable(eAttribute));
	}

	
	

}
