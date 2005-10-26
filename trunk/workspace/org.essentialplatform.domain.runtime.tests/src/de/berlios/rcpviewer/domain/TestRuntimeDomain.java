package de.berlios.rcpviewer.domain;

import de.berlios.rcpviewer.AbstractRuntimeTestCase;
import de.berlios.rcpviewer.domain.Domain;

public class TestRuntimeDomain extends AbstractRuntimeTestCase {

	public TestRuntimeDomain() {
		super(null);
	}

	public void setUp() throws Exception {
		super.setUp();
	}
	
	public void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testDefaultDomainCreatedIfRequested() {
		IDomain domain = getDomainInstance(); 
		assertNotNull(domain);
		assertEquals(0, domain.classes().size());
	}
	
	public void testDomainCreatedWhenSomethingRegistered() {
		IDomainClass domainClass = 
			lookupAny(ClassInOtherDomain.class);
		
		assertEquals(1, Domain.instance("other").classes().size());
	}
	
	public void testDomainOfDomainClassCorrespondsToThatOfInDefaultDomainImplicitly() {
		IDomainClass domainClass = 
			lookupAny(ClassInDefaultDomainImplicitly.class);
		assertEquals("default", domainClass.getDomain().getName());
	}
	
	public void testDomainOfDomainClassCorrespondsToThatOfInDefaultDomainExplicitly() {
		IDomainClass domainClass = 
			lookupAny(ClassInDefaultDomainExplicitly.class);
		assertEquals("default", domainClass.getDomain().getName());
	}
	
	public void testDomainOfDomainClassCorrespondsToThatOfInDomainExplicit() {
		IDomainClass domainClass = 
			lookupAny(ClassInOtherDomain.class);
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
