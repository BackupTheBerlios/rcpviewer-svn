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
public abstract class TestOptional extends AbstractTestCase {

	public TestOptional(IDeploymentSpecifics domainSpecifics, IDomainBuilder domainBuilder) {
		super(domainSpecifics, domainBuilder);
	}

	private IDomainClass<?> domainClass;
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testDomainClassWithMandatoryAttributes() {
		domainClass = 
			lookupAny(CustomerToTestOptionalAnnotation.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		ExtendedDomainClass<?> extendedDomainClass =
			domainClass.getAdapter(ExtendedDomainClass.class);
		assertNotNull(extendedDomainClass);
		
		EAttribute firstName = domainClass.getEAttributeNamed("firstName");
		assertTrue(extendedDomainClass.isMandatory(firstName));
		assertFalse(extendedDomainClass.isOptional(firstName));
	}

	
	public void testDomainClassWithOptionalAttributes() {
		domainClass = 
			lookupAny(CustomerToTestOptionalAnnotation.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		ExtendedDomainClass<?> extendedDomainClass =
			domainClass.getAdapter(ExtendedDomainClass.class);
		assertNotNull(extendedDomainClass);
		
		EAttribute lastName = domainClass.getEAttributeNamed("lastName");
		assertFalse(extendedDomainClass.isMandatory(lastName));
		assertTrue(extendedDomainClass.isOptional(lastName));
	}
	
	public void testDomainClassWithMandatoryParameterToMethod() {
		domainClass = 
			lookupAny(CustomerToTestOptionalAnnotation.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		ExtendedDomainClass<?> extendedDomainClass =
			domainClass.getAdapter(ExtendedDomainClass.class);
		assertNotNull(extendedDomainClass);
		
		EOperation placeOrder = domainClass.getEOperationNamed("placeOrder");
		assertTrue(extendedDomainClass.isMandatory(placeOrder, 0));
		assertFalse(extendedDomainClass.isOptional(placeOrder, 0));
	}

	
	public void testDomainClassWithOptionalParameterToMethod() {
		domainClass = 
			lookupAny(CustomerToTestOptionalAnnotation.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		ExtendedDomainClass<?> extendedDomainClass =
			domainClass.getAdapter(ExtendedDomainClass.class);
		assertNotNull(extendedDomainClass);
		
		EOperation placeOrder = domainClass.getEOperationNamed("placeOrder");
		assertFalse(extendedDomainClass.isMandatory(placeOrder, 1));
		assertTrue(extendedDomainClass.isOptional(placeOrder, 1));
	}

}
