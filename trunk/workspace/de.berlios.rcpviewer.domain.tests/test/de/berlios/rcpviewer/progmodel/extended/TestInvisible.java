package de.berlios.rcpviewer.progmodel.extended;

import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EOperation;

import de.berlios.rcpviewer.AbstractTestCase;
import de.berlios.rcpviewer.IDeploymentSpecifics;
import de.berlios.rcpviewer.domain.IDomainBuilder;
import de.berlios.rcpviewer.domain.IDomainClass;

/**
 * Tests for the use of the {@link de.berlios.rcpviewer.progmodel.extended.Invisible}.
 * 
 * @author Dan Haywood
 */
public abstract class TestInvisible extends AbstractTestCase {

	public TestInvisible(IDeploymentSpecifics domainSpecifics, IDomainBuilder domainBuilder) {
		super(domainSpecifics, domainBuilder);
	}

	private IDomainClass<?> domainClass;
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testDomainClassWithInvisibleAttributes() {
		domainClass = 
			lookupAny(CustomerToTestInvisibleAnnotation.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		ExtendedDomainClass<?> extendedDomainClass =
			domainClass.getAdapter(ExtendedDomainClass.class);
		assertNotNull(extendedDomainClass);
		
		EAttribute id = domainClass.getEAttributeNamed("id");
		assertTrue(extendedDomainClass.isInvisible(id));
	}

	
	public void testDomainClassWithVisibleAttributes() {
		domainClass = 
			lookupAny(CustomerToTestInvisibleAnnotation.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		ExtendedDomainClass<?> extendedDomainClass =
			domainClass.getAdapter(ExtendedDomainClass.class);
		assertNotNull(extendedDomainClass);
		
		EAttribute firstName = domainClass.getEAttributeNamed("firstName");
		assertFalse(extendedDomainClass.isInvisible(firstName));
	}
	
}
