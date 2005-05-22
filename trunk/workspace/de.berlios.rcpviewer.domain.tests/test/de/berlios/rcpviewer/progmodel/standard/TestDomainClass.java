package de.berlios.rcpviewer.progmodel.standard;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;

import de.berlios.rcpviewer.AbstractTestCase;
import de.berlios.rcpviewer.domain.Domain;
import de.berlios.rcpviewer.domain.IDomainClass;


/**
 * Class names and internalization are tested elsewhere.
 * 
 * @author Dan Haywood
 */
public class TestDomainClass extends AbstractTestCase {

	private Domain domain;
	private Domain domain2;
	private IDomainClass<?> domainClass;
	protected void setUp() throws Exception {
		super.setUp();
		domain = Domain.instance();
	}

	protected void tearDown() throws Exception {
		domain.reset();
		domain = null;
		if (domain2 != null) {
			domain2.reset();
			domain2 = null;
		}
		super.tearDown();
	}
	
	public void testGetJavaClass() {
		domainClass = new DomainClass<TestDomainClassCustomerWithNoAttributes>(TestDomainClassCustomerWithNoAttributes.class);
		assertSame(TestDomainClassCustomerWithNoAttributes.class, domainClass.getJavaClass());
	}

	public void testGetEClass() {
		domainClass = new DomainClass<TestDomainClassCustomerWithNoAttributes>(TestDomainClassCustomerWithNoAttributes.class);
		EClass eClass = domainClass.getEClass();
		assertNotNull(eClass);
		assertSame(eClass.getInstanceClass(), TestDomainClassCustomerWithNoAttributes.class);
		assertEquals("TestDomainClassCustomerWithNoAttributes", eClass.getName());
		EPackage ePackage = eClass.getEPackage();
		assertNotNull(ePackage);
		assertEquals(
				TestDomainClassCustomerWithNoAttributes.class.getPackage().getName(), 
				ePackage.getName());
	}

	public void testGetDomainClassFromEClass() {
		IDomainClass<TestDomainClassDepartment> domainClass = 
			domain.localLookup(TestDomainClassDepartment.class);

		EClass eClass = domainClass.getEClass();
		IDomainClass reverseDomainClass = domain.domainClassFor(eClass);
		assertNotNull(reverseDomainClass);
		assertSame(reverseDomainClass, domainClass);
	}
	

	@InDomain
	private static class CustomerImplicitlyInDefaultDomain { }
	
	@InDomain("default")
	private static class CustomerExplicitlyInDefaultDomain { }

	@InDomain("marketing")
	private static class Prospect { }
	@InDomain
	private static class Customer { }


	public void testGetDomainFromDomainClassForImplicitDefaultDomain() {
		IDomainClass<CustomerImplicitlyInDefaultDomain> domainClass = 
			domain.localLookup(CustomerImplicitlyInDefaultDomain.class);
		
		assertEquals("default", domainClass.getDomain().getName());
	}


	public void testGetDomainFromDomainClassForExplicitDefaultDomain() {
		IDomainClass<CustomerExplicitlyInDefaultDomain> domainClass = 
			domain.localLookup(CustomerExplicitlyInDefaultDomain.class);
		
		assertEquals("default", domainClass.getDomain().getName());
	}

	public void testGetDomainClassFromWrongDomainWillFindNothing() {
		IDomainClass<Prospect> domainClass = 
			domain.localLookup(Prospect.class);
		
		assertNull(domainClass);
	}

	public void testGetDomainFromDomainClassForExplicitCustomDomain() {
		domain = Domain.instance("marketing");
		IDomainClass<Prospect> domainClass = 
			domain.localLookup(Prospect.class);
		
		assertEquals("marketing", domainClass.getDomain().getName());
	}


	public void testDomainsAreIndependent() {
		domain = Domain.instance();
		domain2 = Domain.instance("marketing");
		IDomainClass<Customer> customerDomainClass = 
			domain.localLookup(Customer.class);
		IDomainClass<Prospect> prospectDomainClass = 
			domain2.localLookup(Prospect.class);
		
		assertEquals("marketing", prospectDomainClass.getDomain().getName());
		assertEquals("default", customerDomainClass.getDomain().getName());
	
		assertEquals(1, prospectDomainClass.getDomain().classes().size());
		assertEquals(1, customerDomainClass.getDomain().classes().size());
	}

	
	
}
