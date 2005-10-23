package de.berlios.rcpviewer.progmodel.extended;

import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EOperation;

import de.berlios.rcpviewer.AbstractTestCase;
import de.berlios.rcpviewer.IDeploymentSpecifics;
import de.berlios.rcpviewer.domain.IDomainBuilder;
import de.berlios.rcpviewer.domain.IDomainClass;

/**
 * Tests for the use of the {@link de.berlios.rcpviewer.progmodel.extended.Optional}.
 * 
 * @author Dan Haywood
 */
public abstract class TestImmutableOncePersisted extends AbstractTestCase {

	public TestImmutableOncePersisted(IDeploymentSpecifics domainSpecifics, IDomainBuilder domainBuilder) {
		super(domainSpecifics, domainBuilder);
	}

	private IDomainClass<?> domainClass;
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
		
		IExtendedDomainClass<?> extendedDomainClass =
			domainClass.getAdapter(IExtendedDomainClass.class);
		assertNotNull(extendedDomainClass);
		
		EAttribute email = domainClass.getEAttributeNamed("email");
		assertTrue(extendedDomainClass.isImmutableOncePersisted(email));
	}

	
	public void testDomainClassWithoutImmutableOncePersistedAttributes() {
		domainClass = 
			lookupAny(CustomerToTestImmutableOncePersistedAnnotation.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		IExtendedDomainClass<?> extendedDomainClass =
			domainClass.getAdapter(IExtendedDomainClass.class);
		assertNotNull(extendedDomainClass);
		
		EAttribute firstName = domainClass.getEAttributeNamed("firstName");
		assertFalse(extendedDomainClass.isImmutableOncePersisted(firstName));
	}

}
