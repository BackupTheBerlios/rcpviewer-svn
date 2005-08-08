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
	
	public void testDomainClassWithMandatoryAttribute() {
		domainClass = 
			lookupAny(CustomerToTestOptionalAnnotation.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		IExtendedDomainClass<?> extendedDomainClass =
			domainClass.getAdapter(IExtendedDomainClass.class);
		assertNotNull(extendedDomainClass);
		
		EAttribute attrib = domainClass.getEAttributeNamed("firstName");
		assertTrue(extendedDomainClass.isMandatory(attrib));
		assertFalse(extendedDomainClass.isOptional(attrib));
	}

	public void testDomainClassWithMandatoryOperationParameter() {
		domainClass = 
			lookupAny(CustomerToTestOptionalAnnotation.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		IExtendedDomainClass<?> extendedDomainClass =
			domainClass.getAdapter(IExtendedDomainClass.class);
		assertNotNull(extendedDomainClass);
		
		EOperation op = domainClass.getEOperationNamed("placeOrder");
		assertTrue(extendedDomainClass.isMandatory(op, 0));
		assertFalse(extendedDomainClass.isOptional(op, 0));
	}

	
	
	public void testDomainClassWithOptionalAttribute() {
		domainClass = 
			lookupAny(CustomerToTestOptionalAnnotation.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		IExtendedDomainClass<?> extendedDomainClass =
			domainClass.getAdapter(IExtendedDomainClass.class);
		assertNotNull(extendedDomainClass);
		
		EAttribute attrib = domainClass.getEAttributeNamed("lastName");
		assertFalse(extendedDomainClass.isMandatory(attrib));
		assertTrue(extendedDomainClass.isOptional(attrib));
	}
	
	public void testDomainClassWithOptionalOperationParameter() {
		domainClass = 
			lookupAny(CustomerToTestOptionalAnnotation.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		IExtendedDomainClass<?> extendedDomainClass =
			domainClass.getAdapter(IExtendedDomainClass.class);
		assertNotNull(extendedDomainClass);
		
		EOperation op = domainClass.getEOperationNamed("placeOrder");
		assertFalse(extendedDomainClass.isMandatory(op, 1));
		assertTrue(extendedDomainClass.isOptional(op, 1));
	}

}
