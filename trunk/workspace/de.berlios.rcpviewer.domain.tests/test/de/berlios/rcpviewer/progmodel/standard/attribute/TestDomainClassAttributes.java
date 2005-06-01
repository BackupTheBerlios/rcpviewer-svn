package de.berlios.rcpviewer.progmodel.standard.attribute;

import org.eclipse.emf.ecore.EAttribute;

import de.berlios.rcpviewer.AbstractTestCase;
import de.berlios.rcpviewer.IDeploymentSpecifics;
import de.berlios.rcpviewer.domain.Domain;
import de.berlios.rcpviewer.domain.IDomainBuilder;
import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.progmodel.standard.attribute.CustomerWithDerivedReadOnlyAttribute;
import de.berlios.rcpviewer.progmodel.standard.attribute.CustomerWithEveryPrimitiveTypeAccessor;
import de.berlios.rcpviewer.progmodel.standard.attribute.CustomerWithNoAttributes;
import de.berlios.rcpviewer.progmodel.standard.attribute.CustomerWithNonDerivedReadOnlyAttribute;
import de.berlios.rcpviewer.progmodel.standard.attribute.CustomerWithReadOnlyAttribute;
import de.berlios.rcpviewer.progmodel.standard.attribute.CustomerWithReadWriteAttribute;
import de.berlios.rcpviewer.progmodel.standard.attribute.CustomerWithWriteOnlyAttribute;

public abstract class TestDomainClassAttributes extends AbstractTestCase {

	public TestDomainClassAttributes(IDeploymentSpecifics domainSpecifics, IDomainBuilder domainAnalyzer) {
		super(domainSpecifics, domainAnalyzer);
	}

	private IDomainClass<?> domainClass;
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	

	public void testGetAttributesWhenNone() {
		domainClass = lookupAny(CustomerWithNoAttributes.class);
		assertEquals(0, domainClass.attributes().size());
	}

	public void testGetAttributesForPrimitives() {
		domainClass = lookupAny(CustomerWithEveryPrimitiveTypeAccessor.class);
		assertEquals(8, domainClass.attributes().size());
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
		assertTrue(domainClass.isChangeable(eAttribute));
	}

	public void testWhetherEAttributeIsChangeableForReadOnlyAttribute() {
		domainClass = lookupAny(CustomerWithReadOnlyAttribute.class);
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertFalse(eAttribute.isChangeable());
		assertFalse(domainClass.isChangeable(eAttribute));
	}

	public void testWhetherEAttributeIsChangeableForWriteOnlyAttribute() {
		domainClass = lookupAny(CustomerWithWriteOnlyAttribute.class);
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertTrue(eAttribute.isChangeable());
		assertTrue(domainClass.isChangeable(eAttribute));
	}
	
	public void testWhetherEAttributeIsAnnotatedAsWriteOnlyForReadWriteAttribute() {
		domainClass = lookupAny(CustomerWithReadWriteAttribute.class);
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertFalse(domainClass.isWriteOnly(eAttribute));
	}
	
	public void testWhetherEAttributeIsAnnotatedAsReadOnlyForReadOnlyAttribute() {
		domainClass = lookupAny(CustomerWithReadOnlyAttribute.class);
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertFalse(domainClass.isWriteOnly(eAttribute));
	}
	
	public void testWhetherEAttributeIsAnnotatedAsWriteOnlyForWriteOnlyAttribute() {
		domainClass = lookupAny(CustomerWithWriteOnlyAttribute.class);
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertTrue(domainClass.isWriteOnly(eAttribute));
	}

	public void testWhetherEAttributeIsDerivedForNonDerivedReadOnlyAttribute() {
		domainClass = lookupAny(CustomerWithNonDerivedReadOnlyAttribute.class);
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertFalse(eAttribute.isDerived());
		assertFalse(eAttribute.isTransient());
		assertFalse(eAttribute.isVolatile());
		assertFalse(domainClass.isDerived(eAttribute));
	}
	
	public void testWhetherEAttributeIsDerivedForDerivedReadOnlyAttribute() {
		domainClass = lookupAny(CustomerWithDerivedReadOnlyAttribute.class);
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertTrue(eAttribute.isDerived());
		assertTrue(eAttribute.isTransient());
		assertTrue(eAttribute.isVolatile());
		assertTrue(domainClass.isDerived(eAttribute));
	}

	public void incompletetestWhetherEAttributeIsDerivedForDerivedWriteOnlyAttribute() {
		// TODO: not tested, but this is a placeholder: we should have a compile time rule that prevents this 
	}


	
	public void incompletetestNeedTestsUsingValueAnnotation() {
		// TODO
	}


}
