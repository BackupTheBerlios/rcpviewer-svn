package org.essentialplatform.progmodel.standard.attribute;

import org.eclipse.emf.ecore.EAttribute;
import org.essentialplatform.AbstractTestCase;
import org.essentialplatform.domain.IDomainClass;

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
