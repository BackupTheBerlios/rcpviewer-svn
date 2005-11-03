package org.essentialplatform.progmodel.extended;

import org.essentialplatform.AbstractTestCase;
import org.essentialplatform.core.domain.IDomainClass;

/**
 * Tests for the use of the {@link org.essentialplatform.progmodel.extended.Optional}.
 * 
 * @author Dan Haywood
 */
public abstract class TestImmutableOncePersisted extends AbstractTestCase {

	private IDomainClass domainClass;
	
	public void testDomainClassWithImmutableOncePersistedAttributes() {
		domainClass = 
			lookupAny(CustomerToTestImmutableOncePersistedAnnotation.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		IDomainClass.IAttribute attrib = 
			domainClass.getIAttribute(domainClass.getEAttributeNamed("email"));
		assertTrue(attrib.isImmutableOncePersisted());
	}

	
	public void testDomainClassWithoutImmutableOncePersistedAttributes() {
		domainClass = 
			lookupAny(CustomerToTestImmutableOncePersistedAnnotation.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		IDomainClass.IAttribute attrib = 
			domainClass.getIAttribute(domainClass.getEAttributeNamed("firstName"));
		assertFalse(attrib.isImmutableOncePersisted());
	}

}
