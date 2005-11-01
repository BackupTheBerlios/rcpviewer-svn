package org.essentialplatform.progmodel.extended;

import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EOperation;

import org.essentialplatform.AbstractTestCase;
import org.essentialplatform.IDeploymentSpecifics;
import org.essentialplatform.domain.IDomainBuilder;
import org.essentialplatform.domain.IDomainClass;

/**
 * Tests for the use of the {@link org.essentialplatform.progmodel.extended.Optional}.
 * 
 * @author Dan Haywood
 */
public abstract class TestImmutableOncePersisted extends AbstractTestCase {

	public TestImmutableOncePersisted(IDeploymentSpecifics domainSpecifics, IDomainBuilder domainBuilder) {
		super(domainSpecifics, domainBuilder);
	}

	private IDomainClass domainClass;
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testDomainClassWithImmutableOncePersistedAttributes() {
		domainClass = 
			lookupAny(CustomerToTestImmutableOncePersistedAnnotation.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		IDomainClass.IAttribute attrib = 
			domainClass.getAttribute(domainClass.getEAttributeNamed("email"));
		assertTrue(attrib.isImmutableOncePersisted());
	}

	
	public void testDomainClassWithoutImmutableOncePersistedAttributes() {
		domainClass = 
			lookupAny(CustomerToTestImmutableOncePersistedAnnotation.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		IDomainClass.IAttribute attrib = 
			domainClass.getAttribute(domainClass.getEAttributeNamed("firstName"));
		assertFalse(attrib.isImmutableOncePersisted());
	}

}