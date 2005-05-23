package de.berlios.rcpviewer.progmodel.standard.attribute;

import org.eclipse.emf.ecore.EAttribute;

import de.berlios.rcpviewer.AbstractTestCase;
import de.berlios.rcpviewer.domain.Domain;
import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.progmodel.standard.attribute.CustomerWithOnlyIsUnsetForAttribute;
import de.berlios.rcpviewer.progmodel.standard.attribute.CustomerWithOnlyUnsetForAttribute;
import de.berlios.rcpviewer.progmodel.standard.attribute.CustomerWithUnsettableAttribute;

public class TestDomainClassAttributesUnsettability extends AbstractTestCase {

	private IDomainClass<?> domainClass;
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testWhetherEAttributeIsUnsettableWhenIs() {
		domainClass = Domain.lookupAny(CustomerWithUnsettableAttribute.class);
		EAttribute eAttribute = domainClass.getEAttributeNamed("age");
		assertTrue(eAttribute.isUnsettable());
		assertTrue(domainClass.isUnsettable(eAttribute));
	}

	public void testWhetherEAttributeIsUnsettableWhenNotDueToMissingUnsetMethod() {
		domainClass = Domain.lookupAny(CustomerWithOnlyIsUnsetForAttribute.class);
		EAttribute eAttribute = domainClass.getEAttributeNamed("age");
		assertFalse(eAttribute.isUnsettable());
		assertFalse(domainClass.isUnsettable(eAttribute));
	}
	public void testWhetherEAttributeIsUnsettableWhenNotDueToMissingIsUnsetMethod() {
		domainClass = Domain.lookupAny(CustomerWithOnlyUnsetForAttribute.class);
		EAttribute eAttribute = domainClass.getEAttributeNamed("age");
		assertFalse(eAttribute.isUnsettable());
		assertFalse(domainClass.isUnsettable(eAttribute));
	}

	
	

}
