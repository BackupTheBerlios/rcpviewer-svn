package de.berlios.rcpviewer.domain;

import de.berlios.rcpviewer.AbstractTestCase;
import de.berlios.rcpviewer.domain.Domain;
import junit.framework.TestCase;

public class TestDomain extends AbstractTestCase {

	public void setUp() throws Exception {
		super.setUp();
	}
	
	public void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testDefaultDomainCreatedIfRequested() {
		Domain domain = Domain.instance(); 
		assertNotNull(domain);
		assertEquals(0, domain.classes().size());
	}
	
	public void testDomainCreatedWhenSomethingRegistered() {
		IDomainClass<TestDomainClassInOtherDomain> domainClass = 
			Domain.lookupAny(TestDomainClassInOtherDomain.class);
		
		assertEquals(1, Domain.instance("other").classes().size());
	}
	
	public void testDomainOfDomainClassCorrespondsToThatOfInDefaultDomainImplicitly() {
		IDomainClass<TestDomainClassInDefaultDomainImplicitly> domainClass = 
			Domain.lookupAny(TestDomainClassInDefaultDomainImplicitly.class);
		assertEquals("default", domainClass.getDomain().getName());
	}
	
	public void testDomainOfDomainClassCorrespondsToThatOfInDefaultDomainExplicitly() {
		IDomainClass<TestDomainClassInDefaultDomainExplicitly> domainClass = 
			Domain.lookupAny(TestDomainClassInDefaultDomainExplicitly.class);
		assertEquals("default", domainClass.getDomain().getName());
	}
	
	public void testDomainOfDomainClassCorrespondsToThatOfInDomainExplicit() {
		IDomainClass<TestDomainClassInOtherDomain> domainClass = 
			Domain.lookupAny(TestDomainClassInOtherDomain.class);
		assertEquals("other", domainClass.getDomain().getName());
	}
	
	public void testDomainsSharedAcrossThreads() throws InterruptedException {
		final Domain[] domains = new Domain[2];
		for (int i = 0; i<domains.length; i++) {
			final int j = i;
			Thread t = new Thread() {
				public void run() {
					domains[j] = Domain.instance();
				}
			};
			t.start();
			t.join();
		}
		assertSame(domains[0], domains[1]);
	}

}
