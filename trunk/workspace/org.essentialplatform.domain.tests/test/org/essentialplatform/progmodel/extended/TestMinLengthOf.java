package org.essentialplatform.progmodel.extended;

import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EOperation;

import org.essentialplatform.AbstractTestCase;
import org.essentialplatform.IDeploymentSpecifics;
import org.essentialplatform.domain.IDomainBuilder;
import org.essentialplatform.domain.IDomainClass;

/**
 * Tests for the use of the {@link org.essentialplatform.progmodel.extended.Optional}.
 * 
 * @author Dan Haywood
 */
public abstract class TestMinLengthOf extends AbstractTestCase {

	public TestMinLengthOf(IDeploymentSpecifics domainSpecifics, IDomainBuilder domainBuilder) {
		super(domainSpecifics, domainBuilder);
	}

	private IDomainClass domainClass;
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testDomainClassWithMinLengthOfSpecifiedOnAttribute() {
		domainClass = 
			lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		IDomainClass.IAttribute attrib = 
			domainClass.getAttribute(domainClass.getEAttributeNamed("suffix"));
		assertEquals(12, attrib.getMinLengthOf());
	}

	public void testDomainClassWithMinLengthOfSpecifiedOnOperationParameter() {
		domainClass = 
			lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		IDomainClass.IOperation op = 
			domainClass.getOperation(domainClass.getEOperationNamed("updateSuffix"));
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
			domainClass.getAttribute(domainClass.getEAttributeNamed("middleName"));
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
			domainClass.getOperation(domainClass.getEOperationNamed("updateMiddleName"));
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
			domainClass.getAttribute(domainClass.getEAttributeNamed("lastName"));
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
			domainClass.getOperation(domainClass.getEOperationNamed("updateLastName"));
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
			domainClass.getAttribute(domainClass.getEAttributeNamed("attributeWithNegativeLengths"));
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
			domainClass.getOperation(domainClass.getEOperationNamed("operationToUpdateAttributeWithNegativeLengths"));
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
			domainClass.getAttribute(domainClass.getEAttributeNamed("attributeWithNoAnnotations"));
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
			domainClass.getOperation(domainClass.getEOperationNamed("operationToUpdateAttributeWithNoAnnotations"));
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
			domainClass.getAttribute(domainClass.getEAttributeNamed("nonStringAttributeWithLengthAnnotations"));
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
			domainClass.getOperation(domainClass.getEOperationNamed("operationToUpdateNonStringAttributeWithLengthAnnotations"));
		assertEquals(-1, op.getMinLengthOf(0));
	}

}
