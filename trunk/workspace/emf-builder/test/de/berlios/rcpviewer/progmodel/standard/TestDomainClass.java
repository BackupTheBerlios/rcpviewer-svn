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

	@Domain
	public static class Department {
	}

	@Domain
	public static class CustomerWithNoAttributes {
	}

	private MetaModel metaModel;
	private MetaModel metaModel2;
	private IDomainClass<?> domainClass;
	protected void setUp() throws Exception {
		super.setUp();
		metaModel = MetaModel.instance();
	}

	protected void tearDown() throws Exception {
		metaModel.reset();
		metaModel = null;
		if (metaModel2 != null) {
			metaModel2.reset();
			metaModel2 = null;
		}
		super.tearDown();
	}
	
	public void testGetJavaClass() {
		domainClass = new DomainClass<CustomerWithNoAttributes>(CustomerWithNoAttributes.class);
		assertSame(CustomerWithNoAttributes.class, domainClass.getJavaClass());
	}

	public void testGetEClass() {
		domainClass = new DomainClass<CustomerWithNoAttributes>(CustomerWithNoAttributes.class);
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

	public void testGetDomainClassFromEClass() {
		IDomainClass<Department> domainClass = 
			metaModel.lookup(Department.class);

		EClass eClass = domainClass.getEClass();
		IDomainClass reverseDomainClass = metaModel.domainClassFor(eClass);
		assertNotNull(reverseDomainClass);
		assertSame(reverseDomainClass, domainClass);
	}
	

	@Domain
	private static class CustomerImplicitlyInDefaultDomain { }
	
	@Domain("default")
	private static class CustomerExplicitlyInDefaultDomain { }

	@Domain("marketing")
	private static class Prospect { }
	@Domain
	private static class Customer { }


	public void testGetDomainFromDomainClassForImplicitDefaultDomain() {
		IDomainClass<CustomerImplicitlyInDefaultDomain> domainClass = 
			metaModel.lookup(CustomerImplicitlyInDefaultDomain.class);
		
		assertEquals("default", domainClass.getDomain().getName());
	}


	public void testGetDomainFromDomainClassForExplicitDefaultDomain() {
		IDomainClass<CustomerExplicitlyInDefaultDomain> domainClass = 
			metaModel.lookup(CustomerExplicitlyInDefaultDomain.class);
		
		assertEquals("default", domainClass.getDomain().getName());
	}

	public void testGetDomainClassFromWrongDomainWillFindNothing() {
		IDomainClass<Prospect> domainClass = 
			metaModel.lookup(Prospect.class);
		
		assertNull(domainClass);
	}

	public void testGetDomainFromDomainClassForExplicitCustomDomain() {
		metaModel = MetaModel.instance("marketing");
		IDomainClass<Prospect> domainClass = 
			metaModel.lookup(Prospect.class);
		
		assertEquals("marketing", domainClass.getDomain().getName());
	}


	public void testDomainsAreIndependent() {
		metaModel = MetaModel.instance();
		metaModel2 = MetaModel.instance("marketing");
		IDomainClass<Customer> customerDomainClass = 
			metaModel.lookup(Customer.class);
		IDomainClass<Prospect> prospectDomainClass = 
			metaModel2.lookup(Prospect.class);
		
		assertEquals("marketing", prospectDomainClass.getDomain().getName());
		assertEquals("default", customerDomainClass.getDomain().getName());
	
		assertEquals(1, prospectDomainClass.getDomain().classes().size());
		assertEquals(1, customerDomainClass.getDomain().classes().size());
	}

	
	
}
