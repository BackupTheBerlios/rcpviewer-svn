package de.berlios.rcpviewer.progmodel.extended;

import java.util.List;

import org.eclipse.emf.ecore.EAttribute;

import de.berlios.rcpviewer.AbstractTestCase;
import de.berlios.rcpviewer.domain.Domain;
import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.progmodel.standard.InDomain;

/**
 * Tests for the use of the <tt>PositionedAt</tt>.
 * 
 * @author Dan Haywood
 */
public class TestPositionedAt extends AbstractTestCase {

	@InDomain
	public static class CustomerWithAllAttributesPositioned {

		private int numberOfOrders;
		@PositionedAt(3)
		public int getNumberOfOrders() {
			return numberOfOrders;
		}

		private String firstName;
		@PositionedAt(2)
		public String getFirstName() {
			return firstName;
		}
		
		private String lastName;
		@PositionedAt(1)
		public String getLastName() {
			return lastName;
		}
	}
	
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
