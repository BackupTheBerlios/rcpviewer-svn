package org.essentialplatform.progmodel.essential.core.tests;

import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.fixture.progmodel.essential.extended.CustomerToTestMinMaxFieldLengthOf;
import org.essentialplatform.core.tests.AbstractTestCase;
import org.essentialplatform.progmodel.essential.app.FieldLengthOf;
import org.essentialplatform.progmodel.essential.app.MaxLengthOf;
import org.essentialplatform.progmodel.essential.app.MinLengthOf;

/**
 * Tests for the use of the {@link org.essentialplatform.progmodel.essential.app.Optional}.
 * 
 * @author Dan Haywood
 */
public abstract class TestMaxLengthOf extends AbstractTestCase {

	private IDomainClass domainClass;
	
	public void testDomainClassWithMaxLengthOfSpecifiedOnAttribute() {
		domainClass = lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		
		IDomainClass.IAttribute attrib = 
			domainClass.getIAttributeNamed("lastName");
		assertEquals(64, attrib.getMaxLengthOf());
	}

	public void testDomainClassWithMaxLengthOfSpecifiedOnOperationParameter() {
		domainClass = lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		
		IDomainClass.IOperation op = 
			domainClass.getIOperationNamed("updateLastName");
		assertEquals(64, op.getMaxLengthOf(0));
	}

	
	/**
	 * Should use {@link FieldLengthOf} if no {@link MaxLengthOf}.
	 *
	 */
	public void testDomainClassWithMaxLengthOfNotSpecifiedButFieldLengthOfSpecifiedOnAttribute() {
		domainClass = lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		
		IDomainClass.IAttribute attrib = 
			domainClass.getIAttributeNamed("middleName");
		assertEquals(32, attrib.getMaxLengthOf());
	}

	
	/**
	 * Should use {@link FieldLengthOf} if no {@link MaxLengthOf}.
	 *
	 */
	public void testDomainClassWithMaxLengthOfNotSpecifiedButFieldLengthOfSpecifiedOnOperationParameter() {
		domainClass = lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		
		IDomainClass.IOperation op = 
			domainClass.getIOperationNamed("updateMiddleName");
		assertEquals(32, op.getMaxLengthOf(0));
	}
	
	/**
	 * Should use {@link FieldLengthOf} if {@link MaxLengthOf} is invalid (<= 0)
	 *
	 */
	public void testDomainClassWithMaxLengthOfInvalidButFieldLengthOfSpecifiedOnAttribute() {
		domainClass = lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		
		IDomainClass.IAttribute attrib = 
			domainClass.getIAttributeNamed("attributeWithNegativeMaxLengthButValidFieldLength");
		assertEquals(10, attrib.getMaxLengthOf());
	}

	
	/**
	 * Should use {@link FieldLengthOf} if {@link MaxLengthOf} is invalid (<= 0)
	 *
	 */
	public void testDomainClassWithMaxLengthOfInvalidButFieldLengthOfSpecifiedOnOperationParameter() {
		domainClass = lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		
		IDomainClass.IOperation op = 
			domainClass.getIOperationNamed("operationToUpdateAttributeWithNegativeMaxLengthButValidFieldLength");
		assertEquals(10, op.getMaxLengthOf(0));
	}

	
	/**
	 * Should use {@link MinLengthOf} if {@link MaxLengthOf} is invalid (<= 0) 
	 * and {@link FieldLengthOf} is not specified
	 *
	 */
	public void testDomainClassWithFieldLengthOfInvalidButMinLengthOfSpecifiedOnAttribute() {
		domainClass = lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		
		IDomainClass.IAttribute attrib = 
			domainClass.getIAttributeNamed("attributeWithNegativeMaxLengthButValidMinLength");
		assertEquals(10, attrib.getMaxLengthOf());
	}

	/**
	 * Should use {@link MinLengthOf} if {@link MaxLengthOf} is invalid (<= 0) 
	 * and {@link FieldLengthOf} is not specified
	 *
	 */
	public void testDomainClassWithFieldLengthOfInvalidButMinLengthOfSpecifiedOnOperationParameter() {
		domainClass = lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		
		IDomainClass.IOperation op = 
			domainClass.getIOperationNamed("operationToUpdateAttributeWithNegativeMaxLengthButValidMinLength");
		assertEquals(10, op.getMaxLengthOf(0));
	}


	
	/**
	 * If {@link MaxLengthOf} is invalid (<= 0) and others are too then 
	 * return default (-1).
	 *
	 */
	public void testDomainClassWithMaxLengthOfInvalidButMinFieldLengthAlsoInvalidOnAttribute() {
		domainClass = lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		
		IDomainClass.IAttribute attrib = 
			domainClass.getIAttributeNamed("attributeWithNegativeLengths");
		assertEquals(-1, attrib.getMaxLengthOf());
	}

	
	/**
	 * If {@link MaxLengthOf} is invalid (<= 0) and others are too then 
	 * return default (-1).
	 *
	 */
	public void testDomainClassWithMaxLengthOfInvalidButMinFieldLengthAlsoInvalidOnOperationParameter() {
		domainClass = lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		
		IDomainClass.IOperation op = 
			domainClass.getIOperationNamed("operationToUpdateAttributeWithNegativeLengths");
		assertEquals(-1, op.getMaxLengthOf(0));
	}

	/**
	 * If {@link MaxLengthOf} not specified and neither are others then  
	 * return default (-1).
	 *
	 */
	public void testDomainClassWithAttributeWithNoMaxLengthAnnotationsOrAnyOtherOnAttribute() {
		domainClass = lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		
		IDomainClass.IAttribute attrib = 
			domainClass.getIAttributeNamed("attributeWithNoAnnotations");
		assertEquals(-1, attrib.getMaxLengthOf());
	}

	/**
	 * If {@link MaxLengthOf} not specified and neither are others then  
	 * return default (-1).
	 *
	 */
	public void testDomainClassWithAttributeWithNoMaxLengthAnnotationsOrAnyOtherOnOperationParameter() {
		domainClass = lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		
		IDomainClass.IOperation op = 
			domainClass.getIOperationNamed("operationToUpdateAttributeWithNoAnnotations");
		assertEquals(-1, op.getMaxLengthOf(0));
	}


	/**
	 * If {@link MaxLengthOf} is specified on a non-string attribute then  
	 * it should be ignored and return -1.
	 *
	 */
	public void testDomainClassWithNonStringAttributeThatHasMaxLengthAnnotationOnAttribute() {
		domainClass = lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		
		IDomainClass.IAttribute attrib = 
			domainClass.getIAttributeNamed("nonStringAttributeWithLengthAnnotations");
		assertEquals(-1, attrib.getMaxLengthOf());
	}


	/**
	 * If {@link MaxLengthOf} is specified on a non-string attribute then  
	 * it should be ignored and return -1.
	 *
	 */
	public void testDomainClassWithNonStringAttributeThatHasMaxLengthAnnotationOnOperationParameter() {
		domainClass = lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		
		IDomainClass.IOperation op = 
			domainClass.getIOperationNamed("operationToUpdateNonStringAttributeWithLengthAnnotations");
		assertEquals(-1, op.getMaxLengthOf(0));
	}

}
