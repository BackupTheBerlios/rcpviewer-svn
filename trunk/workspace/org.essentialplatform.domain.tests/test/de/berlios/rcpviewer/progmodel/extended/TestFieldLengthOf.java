package de.berlios.rcpviewer.progmodel.extended;

import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EOperation;

import de.berlios.rcpviewer.AbstractTestCase;
import de.berlios.rcpviewer.IDeploymentSpecifics;
import de.berlios.rcpviewer.domain.IDomainBuilder;
import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.progmodel.standard.attribute.CustomerToTestFeatureIds;

/**
 * Tests for the use of the {@link de.berlios.rcpviewer.progmodel.extended.Optional}.
 * 
 * @author Dan Haywood
 */
public abstract class TestFieldLengthOf extends AbstractTestCase {

	public TestFieldLengthOf(IDeploymentSpecifics domainSpecifics, IDomainBuilder domainBuilder) {
		super(domainSpecifics, domainBuilder);
	}

	private IDomainClass<?> domainClass;
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testDomainClassWithFieldLengthOfSpecifiedOnAttribute() {
		domainClass = 
			lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		IExtendedDomainClass<?> extendedDomainClass =
			domainClass.getAdapter(IExtendedDomainClass.class);
		assertNotNull(extendedDomainClass);
		
		EAttribute attrib = domainClass.getEAttributeNamed("middleName");
		assertEquals(32, extendedDomainClass.getFieldLengthOf(attrib));
	}

	public void testDomainClassWithFieldLengthOfSpecifiedOnParameterOfMethod() {
		domainClass = 
			lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		IExtendedDomainClass<?> extendedDomainClass =
			domainClass.getAdapter(IExtendedDomainClass.class);
		assertNotNull(extendedDomainClass);
		
		EOperation op = domainClass.getEOperationNamed("updateMiddleName");
		assertEquals(32, extendedDomainClass.getFieldLengthOf(op, 0));
	}


	
	/**
	 * Should use {@link MaxLengthOf} if no {@link FieldLengthOf}.
	 *
	 */
	public void testDomainClassWithFieldLengthOfNotSpecifiedButMaxLengthOfSpecifiedOnAttribute() {
		domainClass = 
			lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		IExtendedDomainClass<?> extendedDomainClass =
			domainClass.getAdapter(IExtendedDomainClass.class);
		assertNotNull(extendedDomainClass);
		
		EAttribute attrib = domainClass.getEAttributeNamed("lastName");
		assertEquals(64, extendedDomainClass.getFieldLengthOf(attrib));
	}


	
	/**
	 * Should use {@link MaxLengthOf} if no {@link FieldLengthOf}.
	 *
	 */
	public void testDomainClassWithFieldLengthOfNotSpecifiedButMaxLengthOfSpecifiedOnOperationParameter() {
		domainClass = 
			lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		IExtendedDomainClass<?> extendedDomainClass =
			domainClass.getAdapter(IExtendedDomainClass.class);
		assertNotNull(extendedDomainClass);
		
		EOperation op = domainClass.getEOperationNamed("updateLastName");
		assertEquals(64, extendedDomainClass.getFieldLengthOf(op, 0));
	}
	
	/**
	 * Should use {@link MaxLengthOf} if {@link FieldLengthOf} is invalid (<= 0)
	 *
	 */
	public void testDomainClassWithFieldLengthOfInvalidButMaxLengthOfSpecifiedOnAttribute() {
		domainClass = 
			lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		IExtendedDomainClass<?> extendedDomainClass =
			domainClass.getAdapter(IExtendedDomainClass.class);
		assertNotNull(extendedDomainClass);
		
		EAttribute attrib = domainClass.getEAttributeNamed("attributeWithNegativeFieldLengthButValidMaxLength");
		assertEquals(20, extendedDomainClass.getFieldLengthOf(attrib));
	}
	
	/**
	 * Should use {@link MaxLengthOf} if {@link FieldLengthOf} is invalid (<= 0)
	 *
	 */
	public void testDomainClassWithFieldLengthOfInvalidButMaxLengthOfSpecifiedOnOperationParameter() {
		domainClass = 
			lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		IExtendedDomainClass<?> extendedDomainClass =
			domainClass.getAdapter(IExtendedDomainClass.class);
		assertNotNull(extendedDomainClass);
		
		EOperation op = domainClass.getEOperationNamed("operationToUpdateAttributeWithNegativeFieldLengthButValidMaxLength");
		assertEquals(20, extendedDomainClass.getFieldLengthOf(op, 0));
	}


	
	/**
	 * Should use {@link MinLengthOf} if {@link FieldLengthOf} is invalid (<= 0) 
	 * and {@link MaxLengthOf} is not specified
	 *
	 */
	public void testDomainClassWithFieldLengthOfInvalidButMinLengthOfSpecifiedOnAttribute() {
		domainClass = 
			lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		IExtendedDomainClass<?> extendedDomainClass =
			domainClass.getAdapter(IExtendedDomainClass.class);
		assertNotNull(extendedDomainClass);
		
		EAttribute attrib = domainClass.getEAttributeNamed("attributeWithNegativeFieldLengthButValidMinLength");
		assertEquals(20, extendedDomainClass.getFieldLengthOf(attrib));
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
		
		IExtendedDomainClass<?> extendedDomainClass =
			domainClass.getAdapter(IExtendedDomainClass.class);
		assertNotNull(extendedDomainClass);
		
		EOperation op = domainClass.getEOperationNamed("operationToUpdateAttributeWithNegativeFieldLengthButValidMinLength");
		assertEquals(20, extendedDomainClass.getFieldLengthOf(op, 0));
	}


	/**
	 * If {@link FieldLengthOf} is invalid (<= 0) and others are too then 
	 * return default (64).
	 *
	 */
	public void testDomainClassWithFieldLengthOfInvalidButMinMaxLengthAlsoInvalidOnAttribute() {
		domainClass = 
			lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		IExtendedDomainClass<?> extendedDomainClass =
			domainClass.getAdapter(IExtendedDomainClass.class);
		assertNotNull(extendedDomainClass);
		
		EAttribute attrib = domainClass.getEAttributeNamed("attributeWithNegativeLengths");
		assertEquals(32, extendedDomainClass.getFieldLengthOf(attrib));
	}



	/**
	 * If {@link FieldLengthOf} is invalid (<= 0) and others are too then 
	 * return default (64).
	 *
	 */
	public void testDomainClassWithFieldLengthOfInvalidButMinMaxLengthAlsoInvalidOnOperationParameter() {
		domainClass = 
			lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		IExtendedDomainClass<?> extendedDomainClass =
			domainClass.getAdapter(IExtendedDomainClass.class);
		assertNotNull(extendedDomainClass);
		
		EOperation op = domainClass.getEOperationNamed("operationToUpdateAttributeWithNegativeLengths");
		assertEquals(32, extendedDomainClass.getFieldLengthOf(op, 0));
	}

	/**
	 * If {@link FieldLengthOf} not specified and neither are others then  
	 * return default (64).
	 *
	 */
	public void testDomainClassWithAttributeWithNoFieldLengthAnnotationsOrAnyOtherOnAttribute() {
		domainClass = 
			lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		IExtendedDomainClass<?> extendedDomainClass =
			domainClass.getAdapter(IExtendedDomainClass.class);
		assertNotNull(extendedDomainClass);
		
		EAttribute attrib = domainClass.getEAttributeNamed("attributeWithNoAnnotations");
		assertEquals(32, extendedDomainClass.getFieldLengthOf(attrib));
	}

	/**
	 * If {@link FieldLengthOf} not specified and neither are others then  
	 * return default (64).
	 *
	 */
	public void testDomainClassWithAttributeWithNoFieldLengthAnnotationsOrAnyOtherOnOperationParameter() {
		domainClass = 
			lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		IExtendedDomainClass<?> extendedDomainClass =
			domainClass.getAdapter(IExtendedDomainClass.class);
		assertNotNull(extendedDomainClass);
		
		EOperation op = domainClass.getEOperationNamed("operationToUpdateAttributeWithNoAnnotations");
		assertEquals(32, extendedDomainClass.getFieldLengthOf(op, 0));
	}

	/**
	 * If {@link FieldLengthOf} is specified on a non-string attribute then  
	 * it should be ignored and return -1.
	 *
	 */
	public void testDomainClassWithNonStringAttributeThatHasFieldLengthAnnotationOnAttribute() {
		domainClass = 
			lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		IExtendedDomainClass<?> extendedDomainClass =
			domainClass.getAdapter(IExtendedDomainClass.class);
		assertNotNull(extendedDomainClass);
		
		EAttribute attrib = domainClass.getEAttributeNamed("nonStringAttributeWithLengthAnnotations");
		assertEquals(-1, extendedDomainClass.getFieldLengthOf(attrib));
	}

	/**
	 * If {@link FieldLengthOf} is specified on a non-string operation parameter then  
	 * it should be ignored and return -1.
	 *
	 */
	public void testDomainClassWithNonStringAttributeThatHasFieldLengthAnnotationOnOperationParameter() {
		domainClass = 
			lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		IExtendedDomainClass<?> extendedDomainClass =
			domainClass.getAdapter(IExtendedDomainClass.class);
		assertNotNull(extendedDomainClass);
		
		EOperation op = domainClass.getEOperationNamed("operationToUpdateNonStringAttributeWithLengthAnnotations");
		assertEquals(-1, extendedDomainClass.getFieldLengthOf(op, 0));
	}

}
