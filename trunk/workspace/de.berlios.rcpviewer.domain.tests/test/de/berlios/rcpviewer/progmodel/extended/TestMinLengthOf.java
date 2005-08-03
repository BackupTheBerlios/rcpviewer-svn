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
public abstract class TestMinLengthOf extends AbstractTestCase {

	public TestMinLengthOf(IDeploymentSpecifics domainSpecifics, IDomainBuilder domainBuilder) {
		super(domainSpecifics, domainBuilder);
	}

	private IDomainClass<?> domainClass;
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testDomainClassWithMinLengthOfSpecified() {
		domainClass = 
			lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		ExtendedDomainClass<?> extendedDomainClass =
			domainClass.getAdapter(ExtendedDomainClass.class);
		assertNotNull(extendedDomainClass);
		
		EAttribute suffix = domainClass.getEAttributeNamed("suffix");
		assertEquals(12, extendedDomainClass.getMinLengthOf(suffix));
	}

	
	/**
	 * Should return default of 0 if no {@link MinLengthOf}, even if there
	 * is a {@link FieldLengthOf}
	 *
	 */
	public void testDomainClassWithMinLengthOfNotSpecifiedButFieldLengthOfSpecified() {
		domainClass = 
			lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		ExtendedDomainClass<?> extendedDomainClass =
			domainClass.getAdapter(ExtendedDomainClass.class);
		assertNotNull(extendedDomainClass);
		
		EAttribute middleName = domainClass.getEAttributeNamed("middleName");
		assertEquals(0, extendedDomainClass.getMinLengthOf(middleName));
	}

	
	/**
	 * Should return default of 0 if no {@link MinLengthOf}, even if there
	 * is a {@link MaxLengthOf}
	 *
	 */
	public void testDomainClassWithMinLengthOfNotSpecifiedButMaxLengthOfSpecified() {
		domainClass = 
			lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		ExtendedDomainClass<?> extendedDomainClass =
			domainClass.getAdapter(ExtendedDomainClass.class);
		assertNotNull(extendedDomainClass);
		
		EAttribute lastName = domainClass.getEAttributeNamed("lastName");
		assertEquals(0, extendedDomainClass.getMinLengthOf(lastName));
	}

	
	/**
	 * If {@link MinLengthOf} is invalid (< 0) and others are too then 
	 * return default (0).
	 *
	 */
	public void testDomainClassWithMinLengthOfInvalidButFieldMaxLengthAlsoInvalid() {
		domainClass = 
			lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		ExtendedDomainClass<?> extendedDomainClass =
			domainClass.getAdapter(ExtendedDomainClass.class);
		assertNotNull(extendedDomainClass);
		
		EAttribute attrib = domainClass.getEAttributeNamed("attributeWithNegativeLengths");
		assertEquals(0, extendedDomainClass.getMinLengthOf(attrib));
	}


	/**
	 * If {@link MinLengthOf} not specified and neither are others then  
	 * return default (0).
	 *
	 */
	public void testDomainClassWithAttributeWithNoMinLengthAnnotationsOrAnyOther() {
		domainClass = 
			lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		ExtendedDomainClass<?> extendedDomainClass =
			domainClass.getAdapter(ExtendedDomainClass.class);
		assertNotNull(extendedDomainClass);
		
		EAttribute attrib = domainClass.getEAttributeNamed("attributeWithNoAnnotations");
		assertEquals(0, extendedDomainClass.getMinLengthOf(attrib));
	}
	


	/**
	 * If {@link MaxLengthOf} is specified on a non-string attribute then  
	 * it should be ignored and return 0.
	 *
	 */
	public void testDomainClassWithNonStringAttributeThatHasMinLengthAnnotation() {
		domainClass = 
			lookupAny(CustomerToTestMinMaxFieldLengthOf.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		ExtendedDomainClass<?> extendedDomainClass =
			domainClass.getAdapter(ExtendedDomainClass.class);
		assertNotNull(extendedDomainClass);
		
		EAttribute attrib = domainClass.getEAttributeNamed("nonStringAttributeWithLengthAnnotations");
		assertEquals(0, extendedDomainClass.getMinLengthOf(attrib));
	}

}
