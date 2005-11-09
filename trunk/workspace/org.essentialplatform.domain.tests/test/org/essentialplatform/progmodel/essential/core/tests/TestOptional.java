package org.essentialplatform.progmodel.essential.core.tests;

import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.fixture.progmodel.essential.extended.CustomerToTestOptionalAnnotation;
import org.essentialplatform.core.tests.AbstractTestCase;

/**
 * Tests for the use of the {@link org.essentialplatform.progmodel.essential.app.Optional}.
 * 
 * @author Dan Haywood
 */
public abstract class TestOptional extends AbstractTestCase {

	private IDomainClass domainClass;
	
	public void testDomainClassWithMandatoryAttribute() {
		domainClass = lookupAny(CustomerToTestOptionalAnnotation.class);
		
		IDomainClass.IAttribute attrib = 
			domainClass.getIAttributeNamed("firstName");
		assertTrue(attrib.isMandatory());
		assertFalse(attrib.isOptional());
	}

	public void testDomainClassWithMandatoryOperationParameter() {
		domainClass = lookupAny(CustomerToTestOptionalAnnotation.class);
		
		IDomainClass.IOperation op = 
			domainClass.getIOperationNamed("placeOrder");
		assertTrue(op.isMandatory(0));
		assertFalse(op.isOptional(0));
	}

	
	
	public void testDomainClassWithOptionalAttribute() {
		domainClass = lookupAny(CustomerToTestOptionalAnnotation.class);
		
		IDomainClass.IAttribute attrib = 
			domainClass.getIAttributeNamed("lastName");
		assertFalse(attrib.isMandatory());
		assertTrue(attrib.isOptional());
	}
	
	public void testDomainClassWithOptionalOperationParameter() {
		domainClass = lookupAny(CustomerToTestOptionalAnnotation.class);
		
		IDomainClass.IOperation op = 
			domainClass.getIOperationNamed("placeOrder");
		assertFalse(op.isMandatory(1));
		assertTrue(op.isOptional(1));
	}

}
