package org.essentialplatform.progmodel.essential.core.tests;

import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.fixture.progmodel.essential.extended.CustomerToTestMinMaxFieldLengthOf;
import org.essentialplatform.core.fixture.progmodel.essential.extended.CustomerToTestMultiLine;
import org.essentialplatform.core.tests.AbstractTestCase;
import org.essentialplatform.progmodel.essential.app.FieldLengthOf;
import org.essentialplatform.progmodel.essential.app.MaxLengthOf;
import org.essentialplatform.progmodel.essential.app.MinLengthOf;

/**
 * Tests for the use of the {@link org.essentialplatform.progmodel.essential.app.Optional}.
 * 
 * @author Dan Haywood
 */
public abstract class TestMultiLine extends AbstractTestCase {

	private IDomainClass domainClass;
	
	public void testDomainClassWithMultiLineSpecifiedOnAttribute() {
		domainClass = lookupAny(CustomerToTestMultiLine.class);
		
		IDomainClass.IAttribute attrib = domainClass.getIAttributeNamed("comnents");
		assertEquals(5, attrib.getMultiLine());
	}

	public void testDomainClassWithMultiLineSpecifiedOnParameterOfMethod() {
		domainClass = lookupAny(CustomerToTestMultiLine.class);
		
		IDomainClass.IOperation op = domainClass.getIOperationNamed("updateComments");
		assertEquals(5, op.getMultiLine(0));
	}


	
	/**
	 * Should return default if {@link MultiLine} is invalid (<= 0)
	 *
	 */
	public void testDomainClassWithMultiLineInvalid() {
 		domainClass = lookupAny(CustomerToTestMultiLine.class);
		
		IDomainClass.IAttribute attrib = 
			domainClass.getIAttributeNamed("attributeWithNegativeMultiLine");
		assertEquals(-1, attrib.getMultiLine());
	}
	


	/**
	 * If {@link MultiLine} is specified on a non-string attribute then  
	 * it should be ignored and return -1.
	 *
	 */
	public void testDomainClassWithNonStringAttributeThatHasMultiLineAnnotation() {
		domainClass = lookupAny(CustomerToTestMultiLine.class);
		
		IDomainClass.IAttribute attrib = 
			domainClass.getIAttributeNamed("nonStringAttributeWithMultiLineAnnotation");
		assertEquals(-1, attrib.getMultiLine());
	}

	/**
	 * If {@link FieldLengthOf} is specified on a non-string operation parameter then  
	 * it should be ignored and return -1.
	 *
	 */
	public void testDomainClassWithNonMultiLineAnnotationOnOperationParameter() {
		domainClass = lookupAny(CustomerToTestMultiLine.class);
		
		IDomainClass.IOperation op = 
			domainClass.getIOperationNamed("operationToUpdateNonStringAttributeWithMultiLineAnnotation");
		assertEquals(-1, op.getMultiLine(0));
	}

}
