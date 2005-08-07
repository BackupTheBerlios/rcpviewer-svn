package de.berlios.rcpviewer.progmodel.extended;

import org.eclipse.emf.ecore.EAttribute;

import de.berlios.rcpviewer.AbstractTestCase;
import de.berlios.rcpviewer.IDeploymentSpecifics;
import de.berlios.rcpviewer.domain.IDomainBuilder;
import de.berlios.rcpviewer.domain.IDomainClass;

/**
 * Tests for the use of the {@link de.berlios.rcpviewer.progmodel.extended.Optional}.
 * 
 * @author Dan Haywood
 */
public abstract class TestRegex extends AbstractTestCase {

	public TestRegex(IDeploymentSpecifics domainSpecifics, IDomainBuilder domainBuilder) {
		super(domainSpecifics, domainBuilder);
	}

	private IDomainClass<?> domainClass;
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testDomainClassWithAttributeWithRegex() {
		domainClass = 
			lookupAny(CustomerToTestRegex.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		ExtendedDomainClass<?> extendedDomainClass =
			domainClass.getAdapter(ExtendedDomainClass.class);
		assertNotNull(extendedDomainClass);
		
		EAttribute lastName = domainClass.getEAttributeNamed("lastName");
		assertEquals("[A-Z].+", extendedDomainClass.getRegex(lastName));
		assertTrue(extendedDomainClass.regexMatches(lastName, "Abc"));
		assertFalse(extendedDomainClass.regexMatches(lastName, "abc"));
		assertFalse(extendedDomainClass.regexMatches(lastName, "A"));
	}

	
	public void testDomainClassWithAttributeWithoutRegex() {
		domainClass = 
			lookupAny(CustomerToTestRegex.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		ExtendedDomainClass<?> extendedDomainClass =
			domainClass.getAdapter(ExtendedDomainClass.class);
		assertNotNull(extendedDomainClass);
		
		EAttribute firstName = domainClass.getEAttributeNamed("firstName");
		assertNull(extendedDomainClass.getRegex(firstName));
		assertTrue(extendedDomainClass.regexMatches(firstName, "nonsense"));
	}
}
