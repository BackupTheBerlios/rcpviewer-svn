package org.essentialplatform.progmodel.extended;

import org.essentialplatform.AbstractTestCase;
import org.essentialplatform.domain.IDomainClass;

/**
 * Tests for the use of the {@link org.essentialplatform.progmodel.extended.Invisible}.
 * 
 * @author Dan Haywood
 */
public abstract class TestInvisible extends AbstractTestCase {

	private IDomainClass domainClass;
	
	public void testDomainClassWithInvisibleAttributes() {
		domainClass = 
			lookupAny(CustomerToTestInvisibleAnnotation.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		IDomainClass.IAttribute attrib = 
			domainClass.getAttribute(domainClass.getEAttributeNamed("id"));
		assertTrue(attrib.isInvisible());
	}

	
	public void testDomainClassWithVisibleAttributes() {
		domainClass = 
			lookupAny(CustomerToTestInvisibleAnnotation.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		IDomainClass.IAttribute attrib = 
			domainClass.getAttribute(domainClass.getEAttributeNamed("firstName"));
		assertFalse(attrib.isInvisible());
	}
	
}
