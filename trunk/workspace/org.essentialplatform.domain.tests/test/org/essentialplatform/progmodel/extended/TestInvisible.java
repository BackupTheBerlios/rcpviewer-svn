package org.essentialplatform.progmodel.extended;

import org.essentialplatform.AbstractTestCase;
import org.essentialplatform.core.domain.IDomainClass;

/**
 * Tests for the use of the {@link org.essentialplatform.progmodel.essential.app.Invisible}.
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
			domainClass.getIAttribute(domainClass.getEAttributeNamed("id"));
		assertTrue(attrib.isInvisible());
	}

	
	public void testDomainClassWithVisibleAttributes() {
		domainClass = 
			lookupAny(CustomerToTestInvisibleAnnotation.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		IDomainClass.IAttribute attrib = 
			domainClass.getIAttribute(domainClass.getEAttributeNamed("firstName"));
		assertFalse(attrib.isInvisible());
	}
	
}
