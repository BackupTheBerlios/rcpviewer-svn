package org.essentialplatform.progmodel.extended;

import org.essentialplatform.AbstractTestCase;
import org.essentialplatform.core.domain.IDomainClass;

/**
 * Tests for the use of the {@link org.essentialplatform.progmodel.extended.Optional}.
 * 
 * @author Dan Haywood
 */
public abstract class TestMask extends AbstractTestCase {

	private IDomainClass domainClass;
	
	public void testDomainClassWithAttributeWithMask() {
		domainClass = 
			lookupAny(CustomerToTestMask.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		IDomainClass.IAttribute attrib = 
			domainClass.getIAttribute(domainClass.getEAttributeNamed("lastName"));
		assertEquals("AAAA", attrib.getMask());
	}

	
	public void testDomainClassWithAttributeWithoutMask() {
		domainClass = 
			lookupAny(CustomerToTestMask.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		IDomainClass.IAttribute attrib = 
			domainClass.getIAttribute(domainClass.getEAttributeNamed("firstName"));
		assertNull(attrib.getMask());
	}
}
