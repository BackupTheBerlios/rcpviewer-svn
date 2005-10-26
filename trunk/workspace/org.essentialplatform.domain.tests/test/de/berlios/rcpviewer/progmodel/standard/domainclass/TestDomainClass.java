package de.berlios.rcpviewer.progmodel.standard.domainclass;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;

import de.berlios.rcpviewer.AbstractTestCase;
import de.berlios.rcpviewer.IDeploymentSpecifics;
import de.berlios.rcpviewer.domain.Domain;
import de.berlios.rcpviewer.domain.IDomainBuilder;
import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.progmodel.standard.CustomerExplicitlyInDefaultDomain;
import de.berlios.rcpviewer.progmodel.standard.CustomerImplicitlyInDefaultDomain;


/**
 * Class names and internalization are tested elsewhere.
 * 
 * @author Dan Haywood
 */
public abstract class TestDomainClass extends AbstractTestCase {

	public TestDomainClass(IDeploymentSpecifics domainSpecifics, IDomainBuilder domainAnalyzer) {
		super(domainSpecifics, domainAnalyzer);
	}

	private IDomainClass domainClass;
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testGetEClass() {
		domainClass = lookupAny(CustomerWithNoAttributes.class);
		
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
		IDomainClass domainClass = lookupAny(Department.class);
		EClass eClass = domainClass.getEClass();
		IDomainClass reverseDomainClass = getDomainInstance().domainClassFor(eClass);
		assertNotNull(reverseDomainClass);
		assertSame(reverseDomainClass, domainClass);
	}
	

	public void testGetDomainFromDomainClassForImplicitDefaultDomain() {
		IDomainClass domainClass = 
			lookupAny(CustomerImplicitlyInDefaultDomain.class);
		
		assertEquals("default", domainClass.getDomain().getName());
	}


	public void testGetDomainFromDomainClassForExplicitDefaultDomain() {
		domainClass = lookupAny(CustomerExplicitlyInDefaultDomain.class);
		
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
		IDomainClass domainClass = 
			lookupAny(Prospect.class);
		
		assertEquals("marketing", domainClass.getDomain().getName());
	}


	public void testDomainsAreIndependent() {
		IDomainClass customerDomainClass = lookupAny(Customer.class);
		IDomainClass prospectDomainClass = lookupAny(Prospect.class);
		
		assertEquals("marketing", prospectDomainClass.getDomain().getName());
		assertEquals("default", customerDomainClass.getDomain().getName());
	
		assertEquals(1, prospectDomainClass.getDomain().classes().size());
		assertEquals(1, customerDomainClass.getDomain().classes().size());
	}
	
	
}
