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
public abstract class TestMask extends AbstractTestCase {

	public TestMask(IDeploymentSpecifics domainSpecifics, IDomainBuilder domainBuilder) {
		super(domainSpecifics, domainBuilder);
	}

	private IDomainClass<?> domainClass;
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testDomainClassWithAttributeWithMask() {
		domainClass = 
			lookupAny(CustomerToTestMask.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		ExtendedDomainClass<?> extendedDomainClass =
			domainClass.getAdapter(ExtendedDomainClass.class);
		assertNotNull(extendedDomainClass);
		
		EAttribute lastName = domainClass.getEAttributeNamed("lastName");
		assertEquals("AAAA", extendedDomainClass.getMask(lastName));
	}

	
	public void testDomainClassWithAttributeWithoutMask() {
		domainClass = 
			lookupAny(CustomerToTestMask.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		ExtendedDomainClass<?> extendedDomainClass =
			domainClass.getAdapter(ExtendedDomainClass.class);
		assertNotNull(extendedDomainClass);
		
		EAttribute firstName = domainClass.getEAttributeNamed("firstName");
		assertNull(extendedDomainClass.getMask(firstName));
	}
}
