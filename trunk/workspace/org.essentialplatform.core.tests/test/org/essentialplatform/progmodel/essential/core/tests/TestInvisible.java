package org.essentialplatform.progmodel.essential.core.tests;

import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.fixture.progmodel.essential.extended.CustomerToTestInvisibleAnnotation;
import org.essentialplatform.core.tests.AbstractTestCase;

/**
 * Tests for the use of the {@link org.essentialplatform.progmodel.essential.app.Invisible}.
 * 
 * @author Dan Haywood
 */
public abstract class TestInvisible extends AbstractTestCase {

	private IDomainClass domainClass;
	
	public void testDomainClassWithInvisibleAttributes() {
		domainClass = lookupAny(CustomerToTestInvisibleAnnotation.class);
		
		IDomainClass.IAttribute attrib = 
			domainClass.getIAttributeNamed("id");
		assertTrue(attrib.isInvisible());
	}

	
	public void testDomainClassWithVisibleAttributes() {
		domainClass = lookupAny(CustomerToTestInvisibleAnnotation.class);
		
		IDomainClass.IAttribute attrib = 
			domainClass.getIAttributeNamed("firstName");
		assertFalse(attrib.isInvisible());
	}
	
}
