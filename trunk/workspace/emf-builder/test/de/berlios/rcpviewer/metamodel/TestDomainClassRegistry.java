package de.berlios.rcpviewer.metamodel;

import junit.framework.TestCase;

public class TestDomainClassRegistry extends TestCase {

	public void tearDown() throws Exception {
		DomainClassRegistry.instance().clear();
		super.tearDown();
	}
	
	public void testRegistryCreated() {
		assertNotNull(DomainClassRegistry.instance());
	}
	
	public void testOneRegistryPerThread() throws InterruptedException {
		final DomainClassRegistry[] registries = new DomainClassRegistry[2];
		for (int i = 0; i<registries.length; i++) {
			final int j = i;
			Thread t = new Thread() {
				public void run() {
					registries[j] = DomainClassRegistry.instance();
				}
			};
			t.start();
			t.join();
		}
		assertTrue(registries[0] != registries[1]);
	}




}
