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
public abstract class TestMinLengthOf extends AbstractTestCase {

	private IDomainClass domainClass;
	
	public void testDomainClassWithMinLengthOfSpecifiedOnAttribute() {
		domainClass = lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		
		IDomainClass.IAttribute attrib =  
			domainClass.getIAttributeNamed("suffix");
		assertEquals(12, attrib.getMinLengthOf());
	}

	public void testDomainClassWithMinLengthOfSpecifiedOnOperationParameter() {
		domainClass = lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		
		IDomainClass.IOperation op = 
			domainClass.getIOperationNamed("updateSuffix");
		assertEquals(12, op.getMinLengthOf(0));
	}

	
	/**
	 * Should return default of -1 if no {@link MinLengthOf}, even if there
	 * is a {@link FieldLengthOf}
	 *
	 */
	public void testDomainClassWithMinLengthOfNotSpecifiedButFieldLengthOfSpecifiedOnAttribute() {
		domainClass = lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		
		IDomainClass.IAttribute attrib = 
			domainClass.getIAttributeNamed("middleName");
		assertEquals(-1, attrib.getMinLengthOf());
	}
	
	/**
	 * Should return default of -1 if no {@link MinLengthOf}, even if there
	 * is a {@link FieldLengthOf}
	 *
	 */
	public void testDomainClassWithMinLengthOfNotSpecifiedButFieldLengthOfSpecifiedOnOperationParameter() {
		domainClass = lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		
		IDomainClass.IOperation op = 
			domainClass.getIOperationNamed("updateMiddleName");
		assertEquals(-1, op.getMinLengthOf(0));
	}

	
	/**
	 * Should return default of -1 if no {@link MinLengthOf}, even if there
	 * is a {@link MaxLengthOf}
	 *
	 */
	public void testDomainClassWithMinLengthOfNotSpecifiedButMaxLengthOfSpecifiedOnAttribute() {
		domainClass = lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		
		IDomainClass.IAttribute attrib = 
			domainClass.getIAttributeNamed("lastName");
		assertEquals(-1, attrib.getMinLengthOf());
	}

	/**
	 * Should return default of -1 if no {@link MinLengthOf}, even if there
	 * is a {@link MaxLengthOf}
	 *
	 */
	public void testDomainClassWithMinLengthOfNotSpecifiedButMaxLengthOfSpecifiedOnOperationParameter() {
		domainClass = lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		
		IDomainClass.IOperation op = 
			domainClass.getIOperationNamed("updateLastName");
		assertEquals(-1, op.getMinLengthOf(0));
	}

	
	/**
	 * If {@link MinLengthOf} is invalid (< 0) and others are too then 
	 * return default (-1).
	 *
	 */
	public void testDomainClassWithMinLengthOfInvalidButFieldMaxLengthAlsoInvalidOnAttribute() {
		domainClass = lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		
		IDomainClass.IAttribute attrib = 
			domainClass.getIAttributeNamed("attributeWithNegativeLengths");
		assertEquals(-1, attrib.getMinLengthOf());
	}

	
	/**
	 * If {@link MinLengthOf} is invalid (< 0) and others are too then 
	 * return default (-1).
	 *
	 */
	public void testDomainClassWithMinLengthOfInvalidButFieldMaxLengthAlsoInvalidOnOperationParameter() {
		domainClass = lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		
		IDomainClass.IOperation op = 
			domainClass.getIOperationNamed("operationToUpdateAttributeWithNegativeLengths");
		assertEquals(-1, op.getMinLengthOf(0));
	}

	/**
	 * If {@link MinLengthOf} not specified and neither are others then  
	 * return default (-1).
	 *
	 */
	public void testDomainClassWithAttributeWithNoMinLengthAnnotationsOrAnyOtherOnAttribute() {
		domainClass = lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		
		IDomainClass.IAttribute attrib = 
			domainClass.getIAttributeNamed("attributeWithNoAnnotations");
		assertEquals(-1, attrib.getMinLengthOf());
	}
	

	/**
	 * If {@link MinLengthOf} not specified and neither are others then  
	 * return default (-1).
	 *
	 */
	public void testDomainClassWithAttributeWithNoMinLengthAnnotationsOrAnyOtherOnOperationParameter() {
		domainClass = lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		
		IDomainClass.IOperation op = 
			domainClass.getIOperationNamed("operationToUpdateAttributeWithNoAnnotations");
		assertEquals(-1, op.getMinLengthOf(0));
	}


	/**
	 * If {@link MaxLengthOf} is specified on a non-string attribute then  
	 * it should be ignored and return -1.
	 *
	 */
	public void testDomainClassWithNonStringAttributeThatHasMinLengthAnnotationOnAttribute() {
		domainClass = lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		
		IDomainClass.IAttribute attrib = 
			domainClass.getIAttributeNamed("nonStringAttributeWithLengthAnnotations");
		assertEquals(-1, attrib.getMinLengthOf());
	}


	/**
	 * If {@link MaxLengthOf} is specified on a non-string attribute then  
	 * it should be ignored and return -1.
	 *
	 */
	public void testDomainClassWithNonStringAttributeThatHasMinLengthAnnotationOnOperationParameter() {
		domainClass = lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		
		IDomainClass.IOperation op = 
			domainClass.getIOperationNamed("operationToUpdateNonStringAttributeWithLengthAnnotations");
		assertEquals(-1, op.getMinLengthOf(0));
	}

}
