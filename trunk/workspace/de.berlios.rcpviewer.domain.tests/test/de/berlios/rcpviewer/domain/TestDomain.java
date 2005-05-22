package de.berlios.rcpviewer.domain;

import de.berlios.rcpviewer.domain.Domain;
import junit.framework.TestCase;

public class TestDomain extends TestCase {

	private Domain domain;

	public void setUp() throws Exception {
		super.setUp();
		domain = Domain.instance();
	}
	
	public void tearDown() throws Exception {
		domain.reset();
		domain = null;
		super.tearDown();
	}
	
	public void testDefaultDomainCreatedIfRequested() {
		Domain domain = Domain.instance(); 
		assertNotNull(domain);
		assertEquals(0, domain.classes().size());
	}
	
	public void testDomainCreatedWhenSomethingRegistered() {
		IDomainClass<TestDomainClassInOtherDomain> domainClass = 
			Domain.lookup(TestDomainClassInOtherDomain.class);
		Domain domain = domainClass.getDomain();
		assertEquals(1, domain.classes().size());
	}
	
	public void testDomainOfDomainClassCorrespondsToThatOfInDefaultDomainImplicitly() {
		IDomainClass<TestDomainClassInDefaultDomainImplicitly> domainClass = 
			Domain.lookup(TestDomainClassInDefaultDomainImplicitly.class);
		assertEquals("default", domainClass.getDomain().getName());
	}
	
	public void testDomainOfDomainClassCorrespondsToThatOfInDefaultDomainExplicitly() {
		IDomainClass<TestDomainClassInDefaultDomainExplicitly> domainClass = 
			Domain.lookup(TestDomainClassInDefaultDomainExplicitly.class);
		assertEquals("default", domainClass.getDomain().getName());
	}
	
	public void testDomainOfDomainClassCorrespondsToThatOfInDomainExplicit() {
		IDomainClass<TestDomainClassInOtherDomain> domainClass = 
			Domain.lookup(TestDomainClassInOtherDomain.class);
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
