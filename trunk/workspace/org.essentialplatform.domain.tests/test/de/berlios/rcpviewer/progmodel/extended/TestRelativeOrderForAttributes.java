package de.berlios.rcpviewer.progmodel.extended;

import java.util.List;

import org.eclipse.emf.ecore.EAttribute;

import de.berlios.rcpviewer.AbstractTestCase;
import de.berlios.rcpviewer.IDeploymentSpecifics;
import de.berlios.rcpviewer.domain.IDomainBuilder;
import de.berlios.rcpviewer.domain.IDomainClass;

/**
 * Tests for the use of the <tt>@RelativeOrder</tt> annotation.
 * 
 * @author Dan Haywood
 */
public abstract class TestRelativeOrderForAttributes extends AbstractTestCase {

	public TestRelativeOrderForAttributes(IDeploymentSpecifics domainSpecifics, IDomainBuilder domainBuilder) {
		super(domainSpecifics, domainBuilder);
	}

	private IDomainClass domainClass;
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testDomainClassWithAllAttributesPositioned() {
		domainClass = 
			lookupAny(CustomerWithAllAttributesPositioned.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		List<EAttribute> orderedAttributes = 
			domainClass.orderedAttributes();
		assertEquals("lastName", orderedAttributes.get(0).getName());
		assertEquals("firstName", orderedAttributes.get(1).getName());
		assertEquals("numberOfOrders", orderedAttributes.get(2).getName());
	}

	
}
