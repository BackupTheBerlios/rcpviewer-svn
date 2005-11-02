package org.essentialplatform.progmodel.extended;

import org.essentialplatform.AbstractTestCase;
import org.essentialplatform.domain.IDomainBuilder;
import org.essentialplatform.domain.IDomainClass;

/**
 * Tests for the use of the {@link org.essentialplatform.progmodel.extended.Optional}.
 * 
 * @author Dan Haywood
 */
public abstract class TestRegex extends AbstractTestCase {

	private IDomainClass domainClass;
	
	public void testDomainClassWithAttributeWithRegex() {
		domainClass = 
			lookupAny(CustomerToTestRegex.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		IDomainClass.IAttribute attrib = 
			domainClass.getAttribute(domainClass.getEAttributeNamed("lastName"));
		assertEquals("[A-Z].+", attrib.getRegex());
		assertTrue(attrib.regexMatches("Abc"));
		assertFalse(attrib.regexMatches("abc"));
		assertFalse(attrib.regexMatches("A"));
	}

	
	public void testDomainClassWithAttributeWithoutRegex() {
		domainClass = 
			lookupAny(CustomerToTestRegex.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		IDomainClass.IAttribute attrib = 
			domainClass.getAttribute(domainClass.getEAttributeNamed("firstName"));
		assertNull(attrib.getRegex());
		assertTrue(attrib.regexMatches("nonsense"));
	}
}
