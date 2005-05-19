package de.berlios.rcpviewer.domain;

import de.berlios.rcpviewer.domain.Domain;
import junit.framework.TestCase;

public class TestMetaModel extends TestCase {

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
	
	public void testDomainCreated() {
		assertNotNull(Domain.instance());
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
