package de.berlios.rcpviewer.progmodel.extended;

import java.util.List;

import org.eclipse.emf.ecore.EAttribute;

import de.berlios.rcpviewer.AbstractTestCase;
import de.berlios.rcpviewer.IDeploymentSpecifics;
import de.berlios.rcpviewer.domain.Domain;
import de.berlios.rcpviewer.domain.IDomainBuilder;
import de.berlios.rcpviewer.domain.IDomainClass;

/**
 * Tests for the use of the <tt>PositionedAt</tt>.
 * 
 * @author Dan Haywood
 */
public abstract class TestPositionedAt extends AbstractTestCase {

	public TestPositionedAt(IDeploymentSpecifics domainSpecifics, IDomainBuilder domainAnalyzer) {
		super(domainSpecifics, domainAnalyzer);
	}

	private IDomainClass<?> domainClass;
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testDomainClassWithAllAttributesPositioned() {
		domainClass = 
			lookupAny(CustomerWithAllAttributesPositioned.class);
		getDomainInstance().addBuilder(getDomainAnalyzer());
		getDomainInstance().done();
		
		AttributeComparator comparator = (AttributeComparator)domainClass.getAdapter(AttributeComparator.class);
		assertNotNull(comparator);
		List<EAttribute> sortedAttributes = 
			comparator.compare(domainClass.attributes());
		assertEquals("numberOfOrders", sortedAttributes.get(0).getName());
		assertEquals("firstName", sortedAttributes.get(1).getName());
		assertEquals("lastName", sortedAttributes.get(2).getName());
	}
	
}
