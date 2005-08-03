package de.berlios.rcpviewer.progmodel.extended;

import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EOperation;

import de.berlios.rcpviewer.AbstractTestCase;
import de.berlios.rcpviewer.IDeploymentSpecifics;
import de.berlios.rcpviewer.domain.IDomainBuilder;
import de.berlios.rcpviewer.domain.IDomainClass;

/**
 * Tests for the use of the {@link de.berlios.rcpviewer.progmodel.extended.Optional}.
 * 
 * @author Dan Haywood
 */
public abstract class TestMaxLengthOf extends AbstractTestCase {

	public TestMaxLengthOf(IDeploymentSpecifics domainSpecifics, IDomainBuilder domainBuilder) {
		super(domainSpecifics, domainBuilder);
	}

	private IDomainClass<?> domainClass;
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testDomainClassWithMaxLengthOfSpecified() {
		domainClass = 
			lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		ExtendedDomainClass<?> extendedDomainClass =
			domainClass.getAdapter(ExtendedDomainClass.class);
		assertNotNull(extendedDomainClass);
		
		EAttribute lastName = domainClass.getEAttributeNamed("lastName");
		assertEquals(64, extendedDomainClass.getMaxLengthOf(lastName));
	}

	
	/**
	 * Should use {@link FieldLengthOf} if no {@link MaxLengthOf}.
	 *
	 */
	public void testDomainClassWithMaxLengthOfNotSpecifiedButFieldLengthOfSpecified() {
		domainClass = 
			lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		ExtendedDomainClass<?> extendedDomainClass =
			domainClass.getAdapter(ExtendedDomainClass.class);
		assertNotNull(extendedDomainClass);
		
		EAttribute middleName = domainClass.getEAttributeNamed("middleName");
		assertEquals(32, extendedDomainClass.getMaxLengthOf(middleName));
	}

	
	/**
	 * Should use {@link FieldLengthOf} if {@link MaxLengthOf} is invalid (<= 0)
	 *
	 */
	public void testDomainClassWithMaxLengthOfInvalidButFieldLengthOfSpecified() {
		domainClass = 
			lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		ExtendedDomainClass<?> extendedDomainClass =
			domainClass.getAdapter(ExtendedDomainClass.class);
		assertNotNull(extendedDomainClass);
		
		EAttribute attrib = domainClass.getEAttributeNamed("attributeWithNegativeMaxLengthButValidFieldLength");
		assertEquals(10, extendedDomainClass.getMaxLengthOf(attrib));
	}


	
	/**
	 * Should use {@link MinLengthOf} if {@link MaxLengthOf} is invalid (<= 0) 
	 * and {@link FieldLengthOf} is not specified
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
		
		EAttribute attrib = domainClass.getEAttributeNamed("attributeWithNegativeMaxLengthButValidMinLength");
		assertEquals(10, extendedDomainClass.getMaxLengthOf(attrib));
	}


	
	/**
	 * If {@link MaxLengthOf} is invalid (<= 0) and others are too then 
	 * return default (64).
	 *
	 */
	public void testDomainClassWithMaxLengthOfInvalidButMinFieldLengthAlsoInvalid() {
		domainClass = 
			lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		ExtendedDomainClass<?> extendedDomainClass =
			domainClass.getAdapter(ExtendedDomainClass.class);
		assertNotNull(extendedDomainClass);
		
		EAttribute attrib = domainClass.getEAttributeNamed("attributeWithNegativeLengths");
		assertEquals(64, extendedDomainClass.getMaxLengthOf(attrib));
	}


	/**
	 * If {@link MaxLengthOf} not specified and neither are others then  
	 * return default (64).
	 *
	 */
	public void testDomainClassWithAttributeWithNoMaxLengthAnnotationsOrAnyOther() {
		domainClass = 
			lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		ExtendedDomainClass<?> extendedDomainClass =
			domainClass.getAdapter(ExtendedDomainClass.class);
		assertNotNull(extendedDomainClass);
		
		EAttribute attrib = domainClass.getEAttributeNamed("attributeWithNoAnnotations");
		assertEquals(64, extendedDomainClass.getMaxLengthOf(attrib));
	}



	/**
	 * If {@link MaxLengthOf} is specified on a non-string attribute then  
	 * it should be ignored and return 0.
	 *
	 */
	public void testDomainClassWithNonStringAttributeThatHasMaxLengthAnnotation() {
		domainClass = 
			lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		ExtendedDomainClass<?> extendedDomainClass =
			domainClass.getAdapter(ExtendedDomainClass.class);
		assertNotNull(extendedDomainClass);
		
		EAttribute attrib = domainClass.getEAttributeNamed("nonStringAttributeWithLengthAnnotations");
		assertEquals(0, extendedDomainClass.getMaxLengthOf(attrib));
	}


}
