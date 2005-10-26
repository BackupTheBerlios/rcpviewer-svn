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

	private IDomainClass domainClass;
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
		
		IDomainClass.IAttribute attrib = 
			domainClass.getAttribute(domainClass.getEAttributeNamed("firstName"));
		assertTrue(attrib.isMandatory());
		assertFalse(attrib.isOptional());
	}

	public void testDomainClassWithMandatoryOperationParameter() {
		domainClass = 
			lookupAny(CustomerToTestOptionalAnnotation.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		IDomainClass.IOperation op = 
			domainClass.getOperation(domainClass.getEOperationNamed("placeOrder"));
		assertTrue(op.isMandatory(0));
		assertFalse(op.isOptional(0));
	}

	
	
	public void testDomainClassWithOptionalAttribute() {
		domainClass = 
			lookupAny(CustomerToTestOptionalAnnotation.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		IDomainClass.IAttribute attrib = 
			domainClass.getAttribute(domainClass.getEAttributeNamed("lastName"));
		assertFalse(attrib.isMandatory());
		assertTrue(attrib.isOptional());
	}
	
	public void testDomainClassWithOptionalOperationParameter() {
		domainClass = 
			lookupAny(CustomerToTestOptionalAnnotation.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		IDomainClass.IOperation op = 
			domainClass.getOperation(domainClass.getEOperationNamed("placeOrder"));
		assertFalse(op.isMandatory(1));
		assertTrue(op.isOptional(1));
	}

}
