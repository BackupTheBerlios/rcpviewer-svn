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
		assertEquals(0, domainClass.eAttributes().size());
	}

	public void testGetAttributesForPrimitives() {
		domainClass = lookupAny(CustomerWithEveryPrimitiveTypeAccessor.class);
		assertEquals(8, domainClass.eAttributes().size());
	}
	
	public void incompletetestGetAttributesIgnoringInherited() {
		// TODO: invoking domainClass.attributes(false);
	}
	

	public void testGetEAttributeNamedForEveryBuiltInPrimitiveType() {
		domainClass = lookupAny(CustomerWithEveryPrimitiveTypeAccessor.class);
		for(String attributeName: new String[] { 
								"aByte", "aShort", "anInt", "aLong", 
								"aChar", "aFloat", "aDouble", "aBoolean", }) {
			EAttribute eAttribute = domainClass.getEAttributeNamed(attributeName);
			assertEquals(attributeName, eAttribute.getName());
		}
	}

	public void testGetEAttributeNamedForNoneExisting() {
		domainClass = lookupAny(CustomerWithEveryPrimitiveTypeAccessor.class);
		assertNull(domainClass.getEAttributeNamed("nonExistingAttribute"));
	}

	public void testWhetherEAttributeIsChangeableForReadWriteAttribute() {
		domainClass = lookupAny(CustomerWithReadWriteAttribute.class);
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertTrue(eAttribute.isChangeable());
		assertTrue(domainClass.getIAttribute(eAttribute).isChangeable());
	}

	public void testWhetherEAttributeIsChangeableForReadOnlyAttribute() {
		domainClass = lookupAny(CustomerWithReadOnlyAttribute.class);
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertFalse(eAttribute.isChangeable());
		assertFalse(domainClass.getIAttribute(eAttribute).isChangeable());
	}

	public void testWhetherEAttributeIsChangeableForWriteOnlyAttribute() {
		domainClass = lookupAny(CustomerWithWriteOnlyAttribute.class);
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertTrue(eAttribute.isChangeable());
		assertTrue(domainClass.getIAttribute(eAttribute).isChangeable());
	}
	
	public void testWhetherEAttributeIsAnnotatedAsWriteOnlyForReadWriteAttribute() {
		domainClass = lookupAny(CustomerWithReadWriteAttribute.class);
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertFalse(domainClass.getIAttribute(eAttribute).isWriteOnly());
	}
	
	public void testWhetherEAttributeIsAnnotatedAsReadOnlyForReadOnlyAttribute() {
		domainClass = lookupAny(CustomerWithReadOnlyAttribute.class);
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertFalse(domainClass.getIAttribute(eAttribute).isWriteOnly());
	}
	
	public void testWhetherEAttributeIsAnnotatedAsWriteOnlyForWriteOnlyAttribute() {
		domainClass = lookupAny(CustomerWithWriteOnlyAttribute.class);
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertTrue(domainClass.getIAttribute(eAttribute).isWriteOnly());
	}

	public void testWhetherEAttributeIsDerivedForNonDerivedReadOnlyAttribute() {
		domainClass = lookupAny(CustomerWithNonDerivedReadOnlyAttribute.class);
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertFalse(eAttribute.isDerived());
		assertFalse(eAttribute.isTransient());
		assertFalse(eAttribute.isVolatile());
		assertFalse(domainClass.getIAttribute(eAttribute).isDerived());
	}
	
	public void testWhetherEAttributeIsDerivedForDerivedReadOnlyAttribute() {
		domainClass = lookupAny(CustomerWithDerivedReadOnlyAttribute.class);
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertTrue(eAttribute.isDerived());
		assertTrue(eAttribute.isTransient());
		assertTrue(eAttribute.isVolatile());
		assertTrue(domainClass.getIAttribute(eAttribute).isDerived());
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
			domainClass.getIAttribute(domainClass.getEAttributeNamed("firstName")).getFeatureId();
		assertSame(domainClass, firstNameFeatureId.getDomainClass());
		assertSame(IFeatureId.Type.ATTRIBUTE, firstNameFeatureId.getFeatureType());
		assertEquals("firstName", firstNameFeatureId.getName());
		assertEquals(
				CustomerToTestFeatureIds.class.getCanonicalName() + "#firstName", // in case we move fixture classes around later 
				firstNameFeatureId.toString());
		
		
	}

}
