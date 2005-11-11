package org.essentialplatform.progmodel.essential.core.tests;

import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.domain.builders.IDomainBuilder;
import org.essentialplatform.core.domain.validators.RegexValidator;
import org.essentialplatform.core.fixture.progmodel.essential.extended.CustomerToTestRegex;
import org.essentialplatform.core.tests.AbstractTestCase;

/**
 * Tests for the use of the {@link org.essentialplatform.progmodel.essential.app.Optional}.
 * 
 * @author Dan Haywood
 */
public abstract class TestRegex extends AbstractTestCase {

	private IDomainClass domainClass;
	
	public void testDomainClassWithAttributeWithRegex() {
		domainClass = lookupAny(CustomerToTestRegex.class);
		
		IDomainClass.IAttribute attrib = 
			domainClass.getIAttributeNamed("lastName");
		RegexValidator validator = (RegexValidator)attrib.validators().get(0);
		assertEquals("[A-Z].+", validator.getPattern());
		assertTrue(attrib.isValid("Abc"));
		assertFalse(attrib.isValid("abc"));
		assertFalse(attrib.isValid("A"));
	}

	
	public void testDomainClassWithAttributeWithoutRegex() {
		domainClass = lookupAny(CustomerToTestRegex.class);
		
		IDomainClass.IAttribute attrib = 
			domainClass.getIAttributeNamed("firstName");
		assertEquals(0, attrib.validators().size());
		assertTrue(attrib.isValid("nonsense"));
	}
}
