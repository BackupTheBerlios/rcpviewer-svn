package org.essentialplatform.progmodel.essential.core.tests;

import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EAttribute;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.fixture.progmodel.essential.extended.CustomerToTestBusinessKeyAnnotation;
import org.essentialplatform.core.fixture.progmodel.essential.extended.CustomerToTestOptionalAnnotation;
import org.essentialplatform.core.tests.AbstractTestCase;

/**
 * Tests for the use of the {@link org.essentialplatform.progmodel.essential.app.Optional}.
 * 
 * @author Dan Haywood
 */
public abstract class TestBusinessKey extends AbstractTestCase {

	private IDomainClass domainClass;

	public void testDomainClassWithBusinessKeyAttributes() {
		domainClass = 
			lookupAny(CustomerToTestBusinessKeyAnnotation.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		Map<String, List<IDomainClass.IAttribute>> businessKeys = domainClass.businessKeys();
		assertEquals(2, businessKeys.size());
		
		assertTrue(businessKeys.containsKey("name"));
		List<IDomainClass.IAttribute> attributesForNameBusinessKey = businessKeys.get("name");
		assertEquals(2, attributesForNameBusinessKey.size());
		assertSame(domainClass.getIAttributeNamed("lastName"), attributesForNameBusinessKey.get(0));
		assertSame(domainClass.getIAttributeNamed("firstName"), attributesForNameBusinessKey.get(1));

		assertTrue(businessKeys.containsKey("email"));
		List<IDomainClass.IAttribute> attributesForEmailBusinessKey = businessKeys.get("email");
		assertEquals(1, attributesForEmailBusinessKey.size());
		assertSame(domainClass.getIAttributeNamed("email"), attributesForEmailBusinessKey.get(0));

	}

	
	public void testDomainClassWithOptionalAttributes() {
		domainClass = 
			lookupAny(CustomerToTestOptionalAnnotation.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		IDomainClass.IAttribute attrib = domainClass.getIAttributeNamed("lastName");
		assertFalse(attrib.isMandatory());
		assertTrue(attrib.isOptional());
	}
	
	public void testDomainClassWithMandatoryParameterToMethod() {
		domainClass = 
			lookupAny(CustomerToTestOptionalAnnotation.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		IDomainClass.IOperation op = domainClass.getIOperationNamed("placeOrder");
		assertTrue(op.isMandatory(0));
		assertFalse(op.isOptional(0));
	}

	
	public void testDomainClassWithOptionalParameterToMethod() {
		domainClass = 
			lookupAny(CustomerToTestOptionalAnnotation.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		IDomainClass.IOperation op = domainClass.getIOperationNamed("placeOrder");
		assertFalse(op.isMandatory(1));
		assertTrue(op.isOptional(1));
	}

}
