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
	
	public void testDomainClassWithFieldLengthOfSpecified() {
		domainClass = 
			lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		ExtendedDomainClass<?> extendedDomainClass =
			domainClass.getAdapter(ExtendedDomainClass.class);
		assertNotNull(extendedDomainClass);
		
		EAttribute middleName = domainClass.getEAttributeNamed("middleName");
		assertEquals(32, extendedDomainClass.getFieldLengthOf(middleName));
	}

	
	/**
	 * Should use {@link MaxLengthOf} if no {@link FieldLengthOf}.
	 *
	 */
	public void testDomainClassWithFieldLengthOfNotSpecifiedButMaxLengthOfSpecified() {
		domainClass = 
			lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		ExtendedDomainClass<?> extendedDomainClass =
			domainClass.getAdapter(ExtendedDomainClass.class);
		assertNotNull(extendedDomainClass);
		
		EAttribute lastName = domainClass.getEAttributeNamed("lastName");
		assertEquals(64, extendedDomainClass.getFieldLengthOf(lastName));
	}

	
	/**
	 * Should use {@link MaxLengthOf} if {@link FieldLengthOf} is invalid (<= 0)
	 *
	 */
	public void testDomainClassWithFieldLengthOfInvalidButMaxLengthOfSpecified() {
		domainClass = 
			lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		ExtendedDomainClass<?> extendedDomainClass =
			domainClass.getAdapter(ExtendedDomainClass.class);
		assertNotNull(extendedDomainClass);
		
		EAttribute attrib = domainClass.getEAttributeNamed("attributeWithNegativeFieldLengthButValidMaxLength");
		assertEquals(20, extendedDomainClass.getFieldLengthOf(attrib));
	}


	
	/**
	 * Should use {@link MinLengthOf} if {@link FieldLengthOf} is invalid (<= 0) 
	 * and {@link MaxLengthOf} is not specified
	 *
	 */
	public void testDomainClassWithFieldLengthOfInvalidButMinLengthOfSpecified() {
		domainClass = 
			lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		ExtendedDomainClass<?> extendedDomainClass =
			domainClass.getAdapter(ExtendedDomainClass.class);
		assertNotNull(extendedDomainClass);
		
		EAttribute attrib = domainClass.getEAttributeNamed("attributeWithNegativeFieldLengthButValidMinLength");
		assertEquals(20, extendedDomainClass.getFieldLengthOf(attrib));
	}


	/**
	 * If {@link FieldLengthOf} is invalid (<= 0) and others are too then 
	 * return default (64).
	 *
	 */
	public void testDomainClassWithFieldLengthOfInvalidButMinMaxLengthAlsoInvalid() {
		domainClass = 
			lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		ExtendedDomainClass<?> extendedDomainClass =
			domainClass.getAdapter(ExtendedDomainClass.class);
		assertNotNull(extendedDomainClass);
		
		EAttribute attrib = domainClass.getEAttributeNamed("attributeWithNegativeLengths");
		assertEquals(64, extendedDomainClass.getFieldLengthOf(attrib));
	}

	/**
	 * If {@link FieldLengthOf} not specified and neither are others then  
	 * return default (64).
	 *
	 */
	public void testDomainClassWithAttributeWithNoFieldLengthAnnotationsOrAnyOther() {
		domainClass = 
			lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		ExtendedDomainClass<?> extendedDomainClass =
			domainClass.getAdapter(ExtendedDomainClass.class);
		assertNotNull(extendedDomainClass);
		
		EAttribute attrib = domainClass.getEAttributeNamed("attributeWithNoAnnotations");
		assertEquals(64, extendedDomainClass.getFieldLengthOf(attrib));
	}

	/**
	 * If {@link FieldLengthOf} is specified on a non-string attribute then  
	 * it should be ignored and return 0.
	 *
	 */
	public void testDomainClassWithNonStringAttributeThatHasFieldLengthAnnotation() {
		domainClass = 
			lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		ExtendedDomainClass<?> extendedDomainClass =
			domainClass.getAdapter(ExtendedDomainClass.class);
		assertNotNull(extendedDomainClass);
		
		EAttribute attrib = domainClass.getEAttributeNamed("nonStringAttributeWithLengthAnnotations");
		assertEquals(0, extendedDomainClass.getFieldLengthOf(attrib));
	}

	

}
