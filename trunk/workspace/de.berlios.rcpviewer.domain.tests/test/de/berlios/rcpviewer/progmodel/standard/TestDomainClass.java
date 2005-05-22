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
		domain = null;
		domain2 = null;
		Domain.reset();
		super.tearDown();
	}
	
	public void testGetJavaClass() {
		domainClass = Domain.lookup(TestDomainClassCustomerWithNoAttributes.class);
		Domain.instance().done();
		
		assertSame(TestDomainClassCustomerWithNoAttributes.class, domainClass.getJavaClass());
	}

	public void testGetEClass() {
		domainClass = Domain.lookup(TestDomainClassCustomerWithNoAttributes.class);
		Domain.instance().done();
		
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
			Domain.lookup(TestDomainClassDepartment.class);
		Domain.instance().done();
		
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
			Domain.lookup(CustomerImplicitlyInDefaultDomain.class);
		
		assertEquals("default", domainClass.getDomain().getName());
	}


	public void testGetDomainFromDomainClassForExplicitDefaultDomain() {
		domainClass = Domain.lookup(CustomerExplicitlyInDefaultDomain.class);
		
		assertEquals("default", domainClass.getDomain().getName());
	}

	/**
	 * Prospect class is in marketing domain, so shouldn't be found in the
	 * default domain.
	 *
	 */
	public void testGetDomainClassFromWrongDomainWillFindNothing() {
		domainClass = Domain.instance().localLookup(Prospect.class);
		
		assertNull(domainClass);
	}

	public void testGetDomainFromDomainClassForExplicitCustomDomain() {
		domain = Domain.instance("marketing");
		IDomainClass<Prospect> domainClass = 
			Domain.lookup(Prospect.class);
		domain.done();
		
		assertEquals("marketing", domainClass.getDomain().getName());
	}


	public void testDomainsAreIndependent() {
		domain = Domain.instance();
		domain2 = Domain.instance("marketing");
		IDomainClass<Customer> customerDomainClass = 
			Domain.lookup(Customer.class);
		IDomainClass<Prospect> prospectDomainClass = 
			Domain.lookup(Prospect.class);
		
		assertEquals("marketing", prospectDomainClass.getDomain().getName());
		assertEquals("default", customerDomainClass.getDomain().getName());
	
		assertEquals(1, prospectDomainClass.getDomain().classes().size());
		assertEquals(1, customerDomainClass.getDomain().classes().size());
	}

	
	
}
