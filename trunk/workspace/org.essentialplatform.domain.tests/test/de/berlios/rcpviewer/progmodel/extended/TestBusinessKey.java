package de.berlios.rcpviewer.progmodel.extended;

import java.util.List;
import java.util.Map;

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
public abstract class TestBusinessKey extends AbstractTestCase {

	public TestBusinessKey(IDeploymentSpecifics domainSpecifics, IDomainBuilder domainBuilder) {
		super(domainSpecifics, domainBuilder);
	}

	private IDomainClass domainClass;
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testDomainClassWithBusinessKeyAttributes() {
		domainClass = 
			lookupAny(CustomerToTestBusinessKeyAnnotation.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		Map<String, List<EAttribute>> businessKeys = domainClass.businessKeys();
		assertEquals(2, businessKeys.size());
		
		assertTrue(businessKeys.containsKey("name"));
		List<EAttribute> attributesForNameBusinessKey = businessKeys.get("name");
		assertEquals(2, attributesForNameBusinessKey.size());
		assertSame(domainClass.getEAttributeNamed("lastName"), attributesForNameBusinessKey.get(0));
		assertSame(domainClass.getEAttributeNamed("firstName"), attributesForNameBusinessKey.get(1));

		assertTrue(businessKeys.containsKey("email"));
		List<EAttribute> attributesForEmailBusinessKey = businessKeys.get("email");
		assertEquals(1, attributesForEmailBusinessKey.size());
		assertSame(domainClass.getEAttributeNamed("email"), attributesForEmailBusinessKey.get(0));

	}

	
	public void testDomainClassWithOptionalAttributes() {
		domainClass = 
			lookupAny(CustomerToTestOptionalAnnotation.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		IDomainClass.IAttribute attrib = 
			domainClass.getAttribute(domainClass.getEAttributeNamed("lastName"));
		assertFalse(attrib.isMandatory());
		assertTrue(attrib.isOptional());
	}
	
	public void testDomainClassWithMandatoryParameterToMethod() {
		domainClass = 
			lookupAny(CustomerToTestOptionalAnnotation.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		IDomainClass.IOperation op = 
			domainClass.getOperation(domainClass.getEOperationNamed("placeOrder"));
		assertTrue(op.isMandatory(0));
		assertFalse(op.isOptional(0));
	}

	
	public void testDomainClassWithOptionalParameterToMethod() {
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