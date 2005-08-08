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

	private IDomainClass<?> domainClass;
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
		
		IExtendedDomainClass<?> extendedDomainClass =
			domainClass.getAdapter(IExtendedDomainClass.class);
		assertNotNull(extendedDomainClass);
		
		Map<String, List<EAttribute>> businessKeys = extendedDomainClass.businessKeys();
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
		
		IExtendedDomainClass<?> extendedDomainClass =
			domainClass.getAdapter(IExtendedDomainClass.class);
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
		
		IExtendedDomainClass<?> extendedDomainClass =
			domainClass.getAdapter(IExtendedDomainClass.class);
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
		
		IExtendedDomainClass<?> extendedDomainClass =
			domainClass.getAdapter(IExtendedDomainClass.class);
		assertNotNull(extendedDomainClass);
		
		EOperation placeOrder = domainClass.getEOperationNamed("placeOrder");
		assertFalse(extendedDomainClass.isMandatory(placeOrder, 1));
		assertTrue(extendedDomainClass.isOptional(placeOrder, 1));
	}

}
