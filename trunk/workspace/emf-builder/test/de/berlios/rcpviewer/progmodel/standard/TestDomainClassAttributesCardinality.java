package de.berlios.rcpviewer.progmodel.standard;

import org.eclipse.emf.ecore.EAttribute;

import de.berlios.rcpviewer.AbstractTestCase;
import de.berlios.rcpviewer.metamodel.IDomainClass;
import de.berlios.rcpviewer.metamodel.MetaModel;

public class TestDomainClassAttributesCardinality extends AbstractTestCase {

	public static class CustomerWithNoLowerBoundReadOnlyAttribute {
		private String surname;
		public String getSurname() {
			return surname;
		}
	}
	public static class CustomerWithLowerBoundReadOnlyAttribute {
		private String surname;
		@LowerBoundOf(0)
		public String getSurname() {
			return surname;
		}
	}
	public static class CustomerWithNoUpperBoundReadOnlyAttribute {
		private String surname;
		public String getSurname() {
			return surname;
		}
	}
	public static class CustomerWithUpperBoundReadOnlyAttribute {
		private String surname;
		@UpperBoundOf(3)
		public String getSurname() {
			return surname;
		}
	}

	private MetaModel metaModel;
	private IDomainClass<?> domainClass;
	protected void setUp() throws Exception {
		super.setUp();
		metaModel = MetaModel.threadInstance();  // since aspects pick up thread singleton
	}

	protected void tearDown() throws Exception {
		metaModel.reset();
		super.tearDown();
	}
	
	// lower & upper bounds //

	public void testLowerBoundOfEAttributeWhenNoneSpecified() {
		domainClass = metaModel.register(CustomerWithNoLowerBoundReadOnlyAttribute.class);
		metaModel.done();
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertEquals(1, eAttribute.getLowerBound());
		assertEquals(1, domainClass.getLowerBound(eAttribute));
	}

	public void testLowerBoundOfEAttributeWhenSpecified() {
		domainClass = metaModel.register(CustomerWithLowerBoundReadOnlyAttribute.class);
		metaModel.done();
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertEquals(0, eAttribute.getLowerBound());
		assertEquals(0, domainClass.getLowerBound(eAttribute));
	}

	public void incompletetestLowerBoundOfEAttributeWithDatatypeNotSupporting() {
		// TODO
		
	}

	public void testUpperBoundOfEAttributeWhenNoneSpecified() {
		domainClass = metaModel.register(CustomerWithNoUpperBoundReadOnlyAttribute.class);
		metaModel.done();
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertEquals(1, eAttribute.getUpperBound());
		assertEquals(1, domainClass.getLowerBound(eAttribute));
	}

	public void testUpperBoundOfEAttributeWhenSpecified() {
		domainClass = metaModel.register(CustomerWithUpperBoundReadOnlyAttribute.class);
		metaModel.done();
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertEquals(3, eAttribute.getUpperBound());
		assertEquals(3, domainClass.getUpperBound(eAttribute));
	}

	public void incompletetestUpperBoundOfEAttributeWithDatatypeNotSupporting() {
		// TODO
	}


}
