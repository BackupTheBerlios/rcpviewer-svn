package de.berlios.rcpviewer.progmodel.extended;

import java.util.List;

import org.eclipse.emf.ecore.EAttribute;

import de.berlios.rcpviewer.AbstractTestCase;
import de.berlios.rcpviewer.metamodel.IDomainClass;
import de.berlios.rcpviewer.metamodel.MetaModel;

/**
 * Tests for the use of the <tt>PositionedAt</tt>.
 * 
 * @author Dan Haywood
 */
public class TestPositionedAt extends AbstractTestCase {

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
	
	private MetaModel metaModel;
	private IDomainClass<?> domainClass;
	protected void setUp() throws Exception {
		super.setUp();
		metaModel = new MetaModel();
	}

	protected void tearDown() throws Exception {
		metaModel = null;
		super.tearDown();
	}
	

	/**
	 * 
	 *
	 */
	public void testDomainClassWithAllAttributesPositioned() {
		metaModel.addExtension(new ExtendedProgModelExtension());
		domainClass = 
			metaModel.register(CustomerWithAllAttributesPositioned.class);
		metaModel.done();
		AttributeComparator comparator = (AttributeComparator)domainClass.getAdapter(AttributeComparator.class);
		assertNotNull(comparator);
		List<EAttribute> sortedAttributes = 
			comparator.compare(domainClass.attributes());
		assertEquals("numberOfOrders", sortedAttributes.get(0).getName());
		assertEquals("firstName", sortedAttributes.get(1).getName());
		assertEquals("lastName", sortedAttributes.get(2).getName());
	}

	
}
