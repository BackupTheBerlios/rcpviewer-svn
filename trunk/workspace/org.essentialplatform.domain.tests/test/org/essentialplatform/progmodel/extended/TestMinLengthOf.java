package org.essentialplatform.progmodel.extended;

import org.essentialplatform.AbstractTestCase;
import org.essentialplatform.core.domain.IDomainClass;
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
		domainClass = 
			lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		IDomainClass.IAttribute attrib = 
			domainClass.getIAttribute(domainClass.getEAttributeNamed("suffix"));
		assertEquals(12, attrib.getMinLengthOf());
	}

	public void testDomainClassWithMinLengthOfSpecifiedOnOperationParameter() {
		domainClass = 
			lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		IDomainClass.IOperation op = 
			domainClass.getIOperation(domainClass.getEOperationNamed("updateSuffix"));
		assertEquals(12, op.getMinLengthOf(0));
	}

	
	/**
	 * Should return default of 0 if no {@link MinLengthOf}, even if there
	 * is a {@link FieldLengthOf}
	 *
	 */
	public void testDomainClassWithMinLengthOfNotSpecifiedButFieldLengthOfSpecifiedOnAttribute() {
		domainClass = 
			lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		IDomainClass.IAttribute attrib = 
			domainClass.getIAttribute(domainClass.getEAttributeNamed("middleName"));
		assertEquals(0, attrib.getMinLengthOf());
	}
	
	/**
	 * Should return default of 0 if no {@link MinLengthOf}, even if there
	 * is a {@link FieldLengthOf}
	 *
	 */
	public void testDomainClassWithMinLengthOfNotSpecifiedButFieldLengthOfSpecifiedOnOperationParameter() {
		domainClass = 
			lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		IDomainClass.IOperation op = 
			domainClass.getIOperation(domainClass.getEOperationNamed("updateMiddleName"));
		assertEquals(0, op.getMinLengthOf(0));
	}

	
	/**
	 * Should return default of 0 if no {@link MinLengthOf}, even if there
	 * is a {@link MaxLengthOf}
	 *
	 */
	public void testDomainClassWithMinLengthOfNotSpecifiedButMaxLengthOfSpecifiedOnAttribute() {
		domainClass = 
			lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		IDomainClass.IAttribute attrib = 
			domainClass.getIAttribute(domainClass.getEAttributeNamed("lastName"));
		assertEquals(0, attrib.getMinLengthOf());
	}

	/**
	 * Should return default of 0 if no {@link MinLengthOf}, even if there
	 * is a {@link MaxLengthOf}
	 *
	 */
	public void testDomainClassWithMinLengthOfNotSpecifiedButMaxLengthOfSpecifiedOnOperationParameter() {
		domainClass = 
			lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		IDomainClass.IOperation op = 
			domainClass.getIOperation(domainClass.getEOperationNamed("updateLastName"));
		assertEquals(0, op.getMinLengthOf(0));
	}

	
	/**
	 * If {@link MinLengthOf} is invalid (< 0) and others are too then 
	 * return default (0).
	 *
	 */
	public void testDomainClassWithMinLengthOfInvalidButFieldMaxLengthAlsoInvalidOnAttribute() {
		domainClass = 
			lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		IDomainClass.IAttribute attrib = 
			domainClass.getIAttribute(domainClass.getEAttributeNamed("attributeWithNegativeLengths"));
		assertEquals(0, attrib.getMinLengthOf());
	}

	
	/**
	 * If {@link MinLengthOf} is invalid (< 0) and others are too then 
	 * return default (0).
	 *
	 */
	public void testDomainClassWithMinLengthOfInvalidButFieldMaxLengthAlsoInvalidOnOperationParameter() {
		domainClass = 
			lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		IDomainClass.IOperation op = 
			domainClass.getIOperation(domainClass.getEOperationNamed("operationToUpdateAttributeWithNegativeLengths"));
		assertEquals(0, op.getMinLengthOf(0));
	}

	/**
	 * If {@link MinLengthOf} not specified and neither are others then  
	 * return default (0).
	 *
	 */
	public void testDomainClassWithAttributeWithNoMinLengthAnnotationsOrAnyOtherOnAttribute() {
		domainClass = 
			lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		IDomainClass.IAttribute attrib = 
			domainClass.getIAttribute(domainClass.getEAttributeNamed("attributeWithNoAnnotations"));
		assertEquals(0, attrib.getMinLengthOf());
	}
	

	/**
	 * If {@link MinLengthOf} not specified and neither are others then  
	 * return default (0).
	 *
	 */
	public void testDomainClassWithAttributeWithNoMinLengthAnnotationsOrAnyOtherOnOperationParameter() {
		domainClass = 
			lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		IDomainClass.IOperation op = 
			domainClass.getIOperation(domainClass.getEOperationNamed("operationToUpdateAttributeWithNoAnnotations"));
		assertEquals(0, op.getMinLengthOf(0));
	}


	/**
	 * If {@link MaxLengthOf} is specified on a non-string attribute then  
	 * it should be ignored and return 0.
	 *
	 */
	public void testDomainClassWithNonStringAttributeThatHasMinLengthAnnotationOnAttribute() {
		domainClass = 
			lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		IDomainClass.IAttribute attrib = 
			domainClass.getIAttribute(domainClass.getEAttributeNamed("nonStringAttributeWithLengthAnnotations"));
		assertEquals(-1, attrib.getMinLengthOf());
	}


	/**
	 * If {@link MaxLengthOf} is specified on a non-string attribute then  
	 * it should be ignored and return -1.
	 *
	 */
	public void testDomainClassWithNonStringAttributeThatHasMinLengthAnnotationOnOperationParameter() {
		domainClass = 
			lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		IDomainClass.IOperation op = 
			domainClass.getIOperation(domainClass.getEOperationNamed("operationToUpdateNonStringAttributeWithLengthAnnotations"));
		assertEquals(-1, op.getMinLengthOf(0));
	}

}
