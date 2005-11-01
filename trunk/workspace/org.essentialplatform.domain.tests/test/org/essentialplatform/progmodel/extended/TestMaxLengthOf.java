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
public abstract class TestMaxLengthOf extends AbstractTestCase {

	public TestMaxLengthOf(IDeploymentSpecifics domainSpecifics, IDomainBuilder domainBuilder) {
		super(domainSpecifics, domainBuilder);
	}

	private IDomainClass domainClass;
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testDomainClassWithMaxLengthOfSpecifiedOnAttribute() {
		domainClass = 
			lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		IDomainClass.IAttribute attrib = 
			domainClass.getAttribute(domainClass.getEAttributeNamed("lastName"));
		assertEquals(64, attrib.getMaxLengthOf());
	}

	public void testDomainClassWithMaxLengthOfSpecifiedOnOperationParameter() {
		domainClass = 
			lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		IDomainClass.IOperation op = 
			domainClass.getOperation(domainClass.getEOperationNamed("updateLastName"));
		assertEquals(64, op.getMaxLengthOf(0));
	}

	
	/**
	 * Should use {@link FieldLengthOf} if no {@link MaxLengthOf}.
	 *
	 */
	public void testDomainClassWithMaxLengthOfNotSpecifiedButFieldLengthOfSpecifiedOnAttribute() {
		domainClass = 
			lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		IDomainClass.IAttribute attrib = 
			domainClass.getAttribute(domainClass.getEAttributeNamed("middleName"));
		assertEquals(32, attrib.getMaxLengthOf());
	}

	
	/**
	 * Should use {@link FieldLengthOf} if no {@link MaxLengthOf}.
	 *
	 */
	public void testDomainClassWithMaxLengthOfNotSpecifiedButFieldLengthOfSpecifiedOnOperationParameter() {
		domainClass = 
			lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		IDomainClass.IOperation op = 
			domainClass.getOperation(domainClass.getEOperationNamed("updateMiddleName"));
		assertEquals(32, op.getMaxLengthOf(0));
	}
	
	/**
	 * Should use {@link FieldLengthOf} if {@link MaxLengthOf} is invalid (<= 0)
	 *
	 */
	public void testDomainClassWithMaxLengthOfInvalidButFieldLengthOfSpecifiedOnAttribute() {
		domainClass = 
			lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		IDomainClass.IAttribute attrib = 
			domainClass.getAttribute(domainClass.getEAttributeNamed("attributeWithNegativeMaxLengthButValidFieldLength"));
		assertEquals(10, attrib.getMaxLengthOf());
	}

	
	/**
	 * Should use {@link FieldLengthOf} if {@link MaxLengthOf} is invalid (<= 0)
	 *
	 */
	public void testDomainClassWithMaxLengthOfInvalidButFieldLengthOfSpecifiedOnOperationParameter() {
		domainClass = 
			lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		IDomainClass.IOperation op = 
			domainClass.getOperation(domainClass.getEOperationNamed("operationToUpdateAttributeWithNegativeMaxLengthButValidFieldLength"));
		assertEquals(10, op.getMaxLengthOf(0));
	}

	
	/**
	 * Should use {@link MinLengthOf} if {@link MaxLengthOf} is invalid (<= 0) 
	 * and {@link FieldLengthOf} is not specified
	 *
	 */
	public void testDomainClassWithFieldLengthOfInvalidButMinLengthOfSpecifiedOnAttribute() {
		domainClass = 
			lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		IDomainClass.IAttribute attrib = 
			domainClass.getAttribute(domainClass.getEAttributeNamed("attributeWithNegativeMaxLengthButValidMinLength"));
		assertEquals(10, attrib.getMaxLengthOf());
	}

	/**
	 * Should use {@link MinLengthOf} if {@link MaxLengthOf} is invalid (<= 0) 
	 * and {@link FieldLengthOf} is not specified
	 *
	 */
	public void testDomainClassWithFieldLengthOfInvalidButMinLengthOfSpecifiedOnOperationParameter() {
		domainClass = 
			lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		IDomainClass.IOperation op = 
			domainClass.getOperation(domainClass.getEOperationNamed("operationToUpdateAttributeWithNegativeMaxLengthButValidMinLength"));
		assertEquals(10, op.getMaxLengthOf(0));
	}


	
	/**
	 * If {@link MaxLengthOf} is invalid (<= 0) and others are too then 
	 * return default (64).
	 *
	 */
	public void testDomainClassWithMaxLengthOfInvalidButMinFieldLengthAlsoInvalidOnAttribute() {
		domainClass = 
			lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		IDomainClass.IAttribute attrib = 
			domainClass.getAttribute(domainClass.getEAttributeNamed("attributeWithNegativeLengths"));
		assertEquals(64, attrib.getMaxLengthOf());
	}

	
	/**
	 * If {@link MaxLengthOf} is invalid (<= 0) and others are too then 
	 * return default (64).
	 *
	 */
	public void testDomainClassWithMaxLengthOfInvalidButMinFieldLengthAlsoInvalidOnOperationParameter() {
		domainClass = 
			lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		IDomainClass.IOperation op = 
			domainClass.getOperation(domainClass.getEOperationNamed("operationToUpdateAttributeWithNegativeLengths"));
		assertEquals(64, op.getMaxLengthOf(0));
	}

	/**
	 * If {@link MaxLengthOf} not specified and neither are others then  
	 * return default (64).
	 *
	 */
	public void testDomainClassWithAttributeWithNoMaxLengthAnnotationsOrAnyOtherOnAttribute() {
		domainClass = 
			lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		IDomainClass.IAttribute attrib = 
			domainClass.getAttribute(domainClass.getEAttributeNamed("attributeWithNoAnnotations"));
		assertEquals(64, attrib.getMaxLengthOf());
	}

	/**
	 * If {@link MaxLengthOf} not specified and neither are others then  
	 * return default (64).
	 *
	 */
	public void testDomainClassWithAttributeWithNoMaxLengthAnnotationsOrAnyOtherOnOperationParameter() {
		domainClass = 
			lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		IDomainClass.IOperation op = 
			domainClass.getOperation(domainClass.getEOperationNamed("operationToUpdateAttributeWithNoAnnotations"));
		assertEquals(64, op.getMaxLengthOf(0));
	}



	/**
	 * If {@link MaxLengthOf} is specified on a non-string attribute then  
	 * it should be ignored and return 0.
	 *
	 */
	public void testDomainClassWithNonStringAttributeThatHasMaxLengthAnnotationOnAttribute() {
		domainClass = 
			lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		IDomainClass.IAttribute attrib = 
			domainClass.getAttribute(domainClass.getEAttributeNamed("nonStringAttributeWithLengthAnnotations"));
		assertEquals(-1, attrib.getMaxLengthOf());
	}


	/**
	 * If {@link MaxLengthOf} is specified on a non-string attribute then  
	 * it should be ignored and return 0.
	 *
	 */
	public void testDomainClassWithNonStringAttributeThatHasMaxLengthAnnotationOnOperationParameter() {
		domainClass = 
			lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		IDomainClass.IOperation op = 
			domainClass.getOperation(domainClass.getEOperationNamed("operationToUpdateNonStringAttributeWithLengthAnnotations"));
		assertEquals(-1, op.getMaxLengthOf(0));
	}

}
