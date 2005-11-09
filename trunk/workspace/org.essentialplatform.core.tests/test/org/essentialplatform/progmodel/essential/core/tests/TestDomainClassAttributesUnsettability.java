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
		IDomainClass.IAttribute iAttribute = domainClass.getIAttributeNamed("age");
		assertTrue(iAttribute.isUnsettable());
		assertTrue(iAttribute.getEAttribute().isUnsettable());
	}

	public void testWhetherEAttributeIsUnsettableWhenNotDueToMissingUnsetMethod() {
		domainClass = lookupAny(CustomerWithOnlyIsUnsetForAttribute.class);
		IDomainClass.IAttribute iAttribute = domainClass.getIAttributeNamed("age");
		assertFalse(iAttribute.isUnsettable());
		assertFalse(iAttribute.getEAttribute().isUnsettable());
	}
	public void testWhetherEAttributeIsUnsettableWhenNotDueToMissingIsUnsetMethod() {
		domainClass = lookupAny(CustomerWithOnlyUnsetForAttribute.class);
		IDomainClass.IAttribute iAttribute = domainClass.getIAttributeNamed("age");
		assertFalse(iAttribute.isUnsettable());
		assertFalse(iAttribute.getEAttribute().isUnsettable());
	}

	
	

}
