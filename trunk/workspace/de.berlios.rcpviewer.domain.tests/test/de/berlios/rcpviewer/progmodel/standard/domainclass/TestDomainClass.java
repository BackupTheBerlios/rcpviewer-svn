package de.berlios.rcpviewer.progmodel.standard.domainclass;
import de.berlios.rcpviewer.progmodel.standard.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;

import de.berlios.rcpviewer.AbstractTestCase;
import de.berlios.rcpviewer.domain.Domain;
import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.progmodel.standard.CustomerExplicitlyInDefaultDomain;
import de.berlios.rcpviewer.progmodel.standard.CustomerImplicitlyInDefaultDomain;
import de.berlios.rcpviewer.progmodel.standard.domainclass.TestDomainClassCustomerWithNoAttributes;
import de.berlios.rcpviewer.progmodel.standard.domainclass.TestDomainClassDepartment;


/**
 * Class names and internalization are tested elsewhere.
 * 
 * @author Dan Haywood
 */
public class TestDomainClass extends AbstractTestCase {

	private Domain domain2;
	private IDomainClass<?> domainClass;
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		domain2 = null;
		super.tearDown();
	}
	
	public void testGetJavaClass() {
		domainClass = Domain.lookupAny(TestDomainClassCustomerWithNoAttributes.class);
		Domain.instance().done();
		
		assertSame(TestDomainClassCustomerWithNoAttributes.class, domainClass.getJavaClass());
	}

	public void testGetEClass() {
		domainClass = Domain.lookupAny(TestDomainClassCustomerWithNoAttributes.class);
		
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
			Domain.lookupAny(TestDomainClassDepartment.class);
		
		EClass eClass = domainClass.getEClass();
		IDomainClass reverseDomainClass = domain.domainClassFor(eClass);
		assertNotNull(reverseDomainClass);
		assertSame(reverseDomainClass, domainClass);
	}
	

	public void testGetDomainFromDomainClassForImplicitDefaultDomain() {
		IDomainClass<CustomerImplicitlyInDefaultDomain> domainClass = 
			Domain.lookupAny(CustomerImplicitlyInDefaultDomain.class);
		
		assertEquals("default", domainClass.getDomain().getName());
	}


	public void testGetDomainFromDomainClassForExplicitDefaultDomain() {
		domainClass = Domain.lookupAny(CustomerExplicitlyInDefaultDomain.class);
		
		assertEquals("default", domainClass.getDomain().getName());
	}

	/**
	 * Prospect class is in marketing domain, so shouldn't be found in the
	 * default domain.
	 *
	 */
	public void testGetDomainClassFromWrongDomainWillFindNothing() {
		domainClass = Domain.instance().lookup(Prospect.class);
		
		assertNull(domainClass);
	}

	public void testGetDomainFromDomainClassForExplicitCustomDomain() {
		domain = Domain.instance("marketing");
		IDomainClass<Prospect> domainClass = 
			Domain.lookupAny(Prospect.class);
		
		assertEquals("marketing", domainClass.getDomain().getName());
	}


	public void testDomainsAreIndependent() {
		domain2 = Domain.instance("marketing");
		IDomainClass<Customer> customerDomainClass = 
			Domain.lookupAny(Customer.class);
		IDomainClass<Prospect> prospectDomainClass = 
			Domain.lookupAny(Prospect.class);
		
		assertEquals("marketing", prospectDomainClass.getDomain().getName());
		assertEquals("default", customerDomainClass.getDomain().getName());
	
		assertEquals(1, prospectDomainClass.getDomain().classes().size());
		assertEquals(1, customerDomainClass.getDomain().classes().size());
	}
	
	
}
