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
public abstract class TestFieldLengthOf extends AbstractTestCase {

	private IDomainClass domainClass;
	
	public void testDomainClassWithFieldLengthOfSpecifiedOnAttribute() {
		domainClass = lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		
		IDomainClass.IAttribute attrib = domainClass.getIAttributeNamed("middleName");
		assertEquals(32, attrib.getFieldLengthOf());
	}

	public void testDomainClassWithFieldLengthOfSpecifiedOnParameterOfMethod() {
		domainClass = lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		
		IDomainClass.IOperation op = domainClass.getIOperationNamed("updateMiddleName");
		assertEquals(32, op.getFieldLengthOf(0));
	}


	
	/**
	 * Should use {@link MaxLengthOf} if no {@link FieldLengthOf}.
	 *
	 */
	public void testDomainClassWithFieldLengthOfNotSpecifiedButMaxLengthOfSpecifiedOnAttribute() {
		domainClass = lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		
		IDomainClass.IAttribute attrib = domainClass.getIAttributeNamed("lastName");
		assertEquals(64, attrib.getFieldLengthOf());
	}


	
	/**
	 * Should use {@link MaxLengthOf} if no {@link FieldLengthOf}.
	 *
	 */
	public void testDomainClassWithFieldLengthOfNotSpecifiedButMaxLengthOfSpecifiedOnOperationParameter() {
		domainClass = lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		
		IDomainClass.IOperation op = domainClass.getIOperationNamed("updateLastName");
		assertEquals(64, op.getFieldLengthOf(0));
	}
	
	/**
	 * Should use {@link MaxLengthOf} if {@link FieldLengthOf} is invalid (<= 0)
	 *
	 */
	public void testDomainClassWithFieldLengthOfInvalidButMaxLengthOfSpecifiedOnAttribute() {
 		domainClass = lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		
		IDomainClass.IAttribute attrib = 
			domainClass.getIAttributeNamed("attributeWithNegativeFieldLengthButValidMaxLength");
		assertEquals(20, attrib.getFieldLengthOf());
	}
	
	/**
	 * Should use {@link MaxLengthOf} if {@link FieldLengthOf} is invalid (<= 0)
	 *
	 */
	public void testDomainClassWithFieldLengthOfInvalidButMaxLengthOfSpecifiedOnOperationParameter() {
		domainClass = lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		
		IDomainClass.IOperation op = 
			domainClass.getIOperationNamed("operationToUpdateAttributeWithNegativeFieldLengthButValidMaxLength");
		assertEquals(20, op.getFieldLengthOf(0));
	}


	
	/**
	 * Should use {@link MinLengthOf} if {@link FieldLengthOf} is invalid (<= 0) 
	 * and {@link MaxLengthOf} is not specified
	 *
	 */
	public void testDomainClassWithFieldLengthOfInvalidButMinLengthOfSpecifiedOnAttribute() {
		domainClass = lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		
		IDomainClass.IAttribute attrib = 
			domainClass.getIAttributeNamed("attributeWithNegativeFieldLengthButValidMinLength");
		assertEquals(20, attrib.getFieldLengthOf());
	}

	/**
	 * Should use {@link MinLengthOf} if {@link FieldLengthOf} is invalid (<= 0) 
	 * and {@link MaxLengthOf} is not specified
	 *
	 */
	public void testDomainClassWithFieldLengthOfInvalidButMinLengthOfSpecifiedOnOperationParameter() {
		domainClass = 
			lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		IDomainClass.IOperation op = 
			domainClass.getIOperationNamed("operationToUpdateAttributeWithNegativeFieldLengthButValidMinLength");
		assertEquals(20, op.getFieldLengthOf(0));
	}


	/**
	 * If {@link FieldLengthOf} is invalid (<= 0) and others are too then 
	 * return default (-1).
	 *
	 */
	public void testDomainClassWithFieldLengthOfInvalidButMinMaxLengthAlsoInvalidOnAttribute() {
		domainClass = lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		
		IDomainClass.IAttribute attrib = 
			domainClass.getIAttributeNamed("attributeWithNegativeLengths");
		assertEquals(-1, attrib.getFieldLengthOf());
	}



	/**
	 * If {@link FieldLengthOf} is invalid (<= 0) and others are too then 
	 * return default (-1).
	 *
	 */
	public void testDomainClassWithFieldLengthOfInvalidButMinMaxLengthAlsoInvalidOnOperationParameter() {
		domainClass = lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		
		IDomainClass.IOperation op = 
			domainClass.getIOperationNamed("operationToUpdateAttributeWithNegativeLengths");
		assertEquals(-1, op.getFieldLengthOf(0));
	}

	/**
	 * If {@link FieldLengthOf} not specified and neither are others then  
	 * return default (-1).
	 *
	 */
	public void testDomainClassWithAttributeWithNoFieldLengthAnnotationsOrAnyOtherOnAttribute() {
		domainClass = lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		
		IDomainClass.IAttribute attrib = 
			domainClass.getIAttributeNamed("attributeWithNoAnnotations");
		assertEquals(-1, attrib.getFieldLengthOf());
	}

	/**
	 * If {@link FieldLengthOf} not specified and neither are others then  
	 * return default (-1).
	 *
	 */
	public void testDomainClassWithAttributeWithNoFieldLengthAnnotationsOrAnyOtherOnOperationParameter() {
		domainClass = lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		
		IDomainClass.IOperation op = 
			domainClass.getIOperationNamed("operationToUpdateAttributeWithNoAnnotations");
		assertEquals(-1, op.getFieldLengthOf(0));
	}

	/**
	 * If {@link FieldLengthOf} is specified on a non-string attribute then  
	 * it should be ignored and return -1.
	 *
	 */
	public void testDomainClassWithNonStringAttributeThatHasFieldLengthAnnotationOnAttribute() {
		domainClass = lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		
		IDomainClass.IAttribute attrib = 
			domainClass.getIAttributeNamed("nonStringAttributeWithLengthAnnotations");
		assertEquals(-1, attrib.getFieldLengthOf());
	}

	/**
	 * If {@link FieldLengthOf} is specified on a non-string operation parameter then  
	 * it should be ignored and return -1.
	 *
	 */
	public void testDomainClassWithNonStringAttributeThatHasFieldLengthAnnotationOnOperationParameter() {
		domainClass = lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		
		IDomainClass.IOperation op = 
			domainClass.getIOperationNamed("operationToUpdateNonStringAttributeWithLengthAnnotations");
		assertEquals(-1, op.getFieldLengthOf(0));
	}

}
