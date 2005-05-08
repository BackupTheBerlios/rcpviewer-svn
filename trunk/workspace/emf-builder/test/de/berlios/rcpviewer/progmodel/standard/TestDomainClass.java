package de.berlios.rcpviewer.progmodel.standard;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;

import de.berlios.rcpviewer.AbstractTestCase;
import de.berlios.rcpviewer.metamodel.IDomainClass;
import de.berlios.rcpviewer.metamodel.MetaModel;

/**
 * Class names and internalization are tested elsewhere.
 * 
 * @author Dan Haywood
 */
public class TestDomainClass extends AbstractTestCase {

	public static class CustomerWithNoAttributes {
	}

	private IDomainClass<?> domainClass;
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		MetaModel.instance().clear();
		super.tearDown();
	}
	
	public void testGetJavaClass() {
		domainClass = new DomainClass<CustomerWithNoAttributes>(CustomerWithNoAttributes.class);
		assertSame(CustomerWithNoAttributes.class, domainClass.getJavaClass());
	}

	public void testGetEClass() {
		domainClass = new DomainClass(CustomerWithNoAttributes.class);
		EClass eClass = domainClass.getEClass();
		assertNotNull(eClass);
		assertSame(eClass.getInstanceClass(), CustomerWithNoAttributes.class);
		assertEquals("CustomerWithNoAttributes", eClass.getName());
		EPackage ePackage = eClass.getEPackage();
		assertNotNull(ePackage);
		assertEquals(
				CustomerWithNoAttributes.class.getPackage().getName(), 
				ePackage.getName());
	}

	
}
