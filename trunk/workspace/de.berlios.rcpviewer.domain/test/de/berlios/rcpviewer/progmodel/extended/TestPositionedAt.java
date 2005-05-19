package de.berlios.rcpviewer.progmodel.extended;

import java.util.List;

import org.eclipse.emf.ecore.EAttribute;

import de.berlios.rcpviewer.AbstractTestCase;
import de.berlios.rcpviewer.domain.Domain;
import de.berlios.rcpviewer.domain.IDomainClass;

/**
 * Tests for the use of the <tt>PositionedAt</tt>.
 * 
 * @author Dan Haywood
 */
public class TestPositionedAt extends AbstractTestCase {

	private Domain domain;
	private IDomainClass<?> domainClass;
	protected void setUp() throws Exception {
		super.setUp();
		domain = Domain.instance();
	}

	protected void tearDown() throws Exception {
		domain.reset();
		domain = null;
		super.tearDown();
	}
	

	/**
	 * 
	 *
	 */
	public void testDomainClassWithAllAttributesPositioned() {
		domain.addExtension(new ExtendedProgModelExtension());
		domainClass = 
			domain.lookup(CustomerWithAllAttributesPositioned.class);
		domain.done();
		AttributeComparator comparator = (AttributeComparator)domainClass.getAdapter(AttributeComparator.class);
		assertNotNull(comparator);
		List<EAttribute> sortedAttributes = 
			comparator.compare(domainClass.attributes());
		assertEquals("numberOfOrders", sortedAttributes.get(0).getName());
		assertEquals("firstName", sortedAttributes.get(1).getName());
		assertEquals("lastName", sortedAttributes.get(2).getName());
	}

	
}