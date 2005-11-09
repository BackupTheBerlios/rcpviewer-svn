package org.essentialplatform.progmodel.essential.core.tests;

import org.eclipse.emf.ecore.EAttribute;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.features.IFeatureId;
import org.essentialplatform.core.fixture.progmodel.essential.standard.attribute.CustomerToTestFeatureIds;
import org.essentialplatform.core.fixture.progmodel.essential.standard.attribute.CustomerWithDerivedReadOnlyAttribute;
import org.essentialplatform.core.fixture.progmodel.essential.standard.attribute.CustomerWithEveryPrimitiveTypeAccessor;
import org.essentialplatform.core.fixture.progmodel.essential.standard.attribute.CustomerWithNoAttributes;
import org.essentialplatform.core.fixture.progmodel.essential.standard.attribute.CustomerWithNonDerivedReadOnlyAttribute;
import org.essentialplatform.core.fixture.progmodel.essential.standard.attribute.CustomerWithReadOnlyAttribute;
import org.essentialplatform.core.fixture.progmodel.essential.standard.attribute.CustomerWithReadWriteAttribute;
import org.essentialplatform.core.fixture.progmodel.essential.standard.attribute.CustomerWithWriteOnlyAttribute;
import org.essentialplatform.core.tests.AbstractTestCase;

public abstract class TestDomainClassAttributes extends AbstractTestCase {

	private IDomainClass domainClass;

	public void testGetAttributesWhenNone() {
		domainClass = lookupAny(CustomerWithNoAttributes.class);
		assertEquals(0, domainClass.iAttributes().size());
	}

	public void testGetAttributesForPrimitives() {
		domainClass = lookupAny(CustomerWithEveryPrimitiveTypeAccessor.class);
		assertEquals(8, domainClass.iAttributes().size());
	}
	
	public void incompletetestGetAttributesIgnoringInherited() {
		// TODO: invoking domainClass.attributes(false);
	}
	

	public void testGetEAttributeNamedForEveryBuiltInPrimitiveType() {
		domainClass = lookupAny(CustomerWithEveryPrimitiveTypeAccessor.class);
		for(String attributeName: new String[] { 
								"aByte", "aShort", "anInt", "aLong", 
								"aChar", "aFloat", "aDouble", "aBoolean", }) {
			IDomainClass.IAttribute iAttribute = domainClass.getIAttributeNamed(attributeName);
			assertEquals(attributeName, iAttribute.getName());
		}
	}

	public void testGetEAttributeNamedForNoneExisting() {
		domainClass = lookupAny(CustomerWithEveryPrimitiveTypeAccessor.class);
		assertNull(domainClass.getIAttributeNamed("nonExistingAttribute"));
	}

	public void testWhetherEAttributeIsChangeableForReadWriteAttribute() {
		domainClass = lookupAny(CustomerWithReadWriteAttribute.class);
		IDomainClass.IAttribute iAttribute = domainClass.getIAttributeNamed("surname");
		assertTrue(iAttribute.isChangeable());
	}

	public void testWhetherEAttributeIsChangeableForReadOnlyAttribute() {
		domainClass = lookupAny(CustomerWithReadOnlyAttribute.class);
		IDomainClass.IAttribute iAttribute = domainClass.getIAttributeNamed("surname");
		assertFalse(iAttribute.isChangeable());
	}

	public void testWhetherEAttributeIsChangeableForWriteOnlyAttribute() {
		domainClass = lookupAny(CustomerWithWriteOnlyAttribute.class);
		IDomainClass.IAttribute iAttribute = domainClass.getIAttributeNamed("surname");
		assertTrue(iAttribute.isChangeable());
	}
	
	public void testWhetherEAttributeIsAnnotatedAsWriteOnlyForReadWriteAttribute() {
		domainClass = lookupAny(CustomerWithReadWriteAttribute.class);
		IDomainClass.IAttribute iAttribute = domainClass.getIAttributeNamed("surname");
		assertFalse(iAttribute.isWriteOnly());
	}
	
	public void testWhetherEAttributeIsAnnotatedAsReadOnlyForReadOnlyAttribute() {
		domainClass = lookupAny(CustomerWithReadOnlyAttribute.class);
		IDomainClass.IAttribute iAttribute = domainClass.getIAttributeNamed("surname");
		assertFalse(iAttribute.isWriteOnly());
	}
	
	public void testWhetherEAttributeIsAnnotatedAsWriteOnlyForWriteOnlyAttribute() {
		domainClass = lookupAny(CustomerWithWriteOnlyAttribute.class);
		IDomainClass.IAttribute iAttribute = domainClass.getIAttributeNamed("surname");
		assertTrue(iAttribute.isWriteOnly());
	}

	public void testWhetherEAttributeIsDerivedForNonDerivedReadOnlyAttribute() {
		domainClass = lookupAny(CustomerWithNonDerivedReadOnlyAttribute.class);
		IDomainClass.IAttribute iAttribute = domainClass.getIAttributeNamed("surname");
		assertFalse(iAttribute.isDerived());
		assertFalse(iAttribute.getEAttribute().isDerived());
		assertFalse(iAttribute.getEAttribute().isTransient());
		assertFalse(iAttribute.getEAttribute().isVolatile());
	}
	
	public void testWhetherEAttributeIsDerivedForDerivedReadOnlyAttribute() {
		domainClass = lookupAny(CustomerWithDerivedReadOnlyAttribute.class);
		IDomainClass.IAttribute iAttribute = domainClass.getIAttributeNamed("surname");
		assertTrue(iAttribute.isDerived());
		assertTrue(iAttribute.getEAttribute().isDerived());
		assertTrue(iAttribute.getEAttribute().isTransient());
		assertTrue(iAttribute.getEAttribute().isVolatile());
	}

	public void incompletetestWhetherEAttributeIsDerivedForDerivedWriteOnlyAttribute() {
		// TODO: not tested, but this is a placeholder: we should have a compile time rule that prevents this 
	}


	
	public void incompletetestNeedTestsUsingValueAnnotation() {
		// TODO
	}

	public void testFeatureIdForAttribute() {
		domainClass = lookupAny(CustomerToTestFeatureIds.class);
		IFeatureId firstNameFeatureId = 
			domainClass.getIAttributeNamed("firstName").getFeatureId();
		assertSame(domainClass, firstNameFeatureId.getDomainClass());
		assertSame(IFeatureId.Type.ATTRIBUTE, firstNameFeatureId.getFeatureType());
		assertEquals("firstName", firstNameFeatureId.getName());
		assertEquals(
				CustomerToTestFeatureIds.class.getCanonicalName() + "#firstName", // in case we move fixture classes around later 
				firstNameFeatureId.toString());
		
		
	}

}
