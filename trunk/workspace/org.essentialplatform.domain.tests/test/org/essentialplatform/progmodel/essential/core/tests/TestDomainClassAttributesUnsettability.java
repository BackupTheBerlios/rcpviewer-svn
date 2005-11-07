package org.essentialplatform.progmodel.essential.core.tests;

import org.eclipse.emf.ecore.EAttribute;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.fixture.progmodel.essential.standard.attribute.CustomerWithOnlyIsUnsetForAttribute;
import org.essentialplatform.core.fixture.progmodel.essential.standard.attribute.CustomerWithOnlyUnsetForAttribute;
import org.essentialplatform.core.fixture.progmodel.essential.standard.attribute.CustomerWithUnsettableAttribute;
import org.essentialplatform.core.tests.AbstractTestCase;

public abstract class TestDomainClassAttributesUnsettability extends AbstractTestCase {

	private IDomainClass domainClass;
	
	public void testWhetherEAttributeIsUnsettableWhenIs() {
		domainClass = lookupAny(CustomerWithUnsettableAttribute.class);
		EAttribute eAttribute = domainClass.getEAttributeNamed("age");
		assertTrue(eAttribute.isUnsettable());
		assertTrue(domainClass.getIAttribute(eAttribute).isUnsettable());
	}

	public void testWhetherEAttributeIsUnsettableWhenNotDueToMissingUnsetMethod() {
		domainClass = lookupAny(CustomerWithOnlyIsUnsetForAttribute.class);
		EAttribute eAttribute = domainClass.getEAttributeNamed("age");
		assertFalse(eAttribute.isUnsettable());
		assertFalse(domainClass.getIAttribute(eAttribute).isUnsettable());
	}
	public void testWhetherEAttributeIsUnsettableWhenNotDueToMissingIsUnsetMethod() {
		domainClass = lookupAny(CustomerWithOnlyUnsetForAttribute.class);
		EAttribute eAttribute = domainClass.getEAttributeNamed("age");
		assertFalse(eAttribute.isUnsettable());
		assertFalse(domainClass.getIAttribute(eAttribute).isUnsettable());
	}

	
	

}
