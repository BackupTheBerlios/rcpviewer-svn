package org.essentialplatform.progmodel.essential.core.tests;

import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.fixture.progmodel.essential.extended.CustomerToTestImmutableOncePersistedAnnotation;
import org.essentialplatform.core.tests.AbstractTestCase;

/**
 * Tests for the use of the {@link org.essentialplatform.progmodel.essential.app.Optional}.
 * 
 * @author Dan Haywood
 */
public abstract class TestImmutableOncePersisted extends AbstractTestCase {

	private IDomainClass domainClass;
	
	public void testDomainClassWithImmutableOncePersistedAttributes() {
		domainClass = lookupAny(CustomerToTestImmutableOncePersistedAnnotation.class);
		
		IDomainClass.IAttribute attrib = 
			domainClass.getIAttribute(domainClass.getEAttributeNamed("email"));
		assertTrue(attrib.isImmutableOncePersisted());
	}

	
	public void testDomainClassWithoutImmutableOncePersistedAttributes() {
		domainClass = lookupAny(CustomerToTestImmutableOncePersistedAnnotation.class);
		
		IDomainClass.IAttribute attrib = 
			domainClass.getIAttribute(domainClass.getEAttributeNamed("firstName"));
		assertFalse(attrib.isImmutableOncePersisted());
	}

}
