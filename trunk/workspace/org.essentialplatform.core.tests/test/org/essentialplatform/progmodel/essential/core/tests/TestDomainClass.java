package org.essentialplatform.progmodel.essential.core.tests;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.essentialplatform.core.domain.Domain;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.fixture.progmodel.essential.standard.CustomerExplicitlyInDefaultDomain;
import org.essentialplatform.core.fixture.progmodel.essential.standard.CustomerImplicitlyInDefaultDomain;
import org.essentialplatform.core.fixture.progmodel.essential.standard.domainclass.Customer;
import org.essentialplatform.core.fixture.progmodel.essential.standard.domainclass.CustomerWithAbbreviation;
import org.essentialplatform.core.fixture.progmodel.essential.standard.domainclass.CustomerWithNoAbbreviation;
import org.essentialplatform.core.fixture.progmodel.essential.standard.domainclass.CustomerWithNoAttributes;
import org.essentialplatform.core.fixture.progmodel.essential.standard.domainclass.Department;
import org.essentialplatform.core.fixture.progmodel.essential.standard.domainclass.Prospect;
import org.essentialplatform.core.tests.AbstractTestCase;


/**
 * Class names and internalization are tested elsewhere.
 * 
 * @author Dan Haywood
 */
public abstract class TestDomainClass extends AbstractTestCase {

	private IDomainClass domainClass;
	
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

	public void testAbbreviationWhenExplicitlyProvided() {
		domainClass = lookupAny(CustomerWithNoAbbreviation.class);
		
		assertEquals("CustomerWithNoAbbreviation".toUpperCase(), domainClass.getAbbreviation());
	}

	public void testAbbreviationWhenHasNone() {
		domainClass = lookupAny(CustomerWithNoAttributes.class);
		
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
