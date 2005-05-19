package de.berlios.rcpviewer.domain.tests;

import de.berlios.rcpviewer.domain.Domain;
import junit.framework.TestCase;

public class TestDomain2 extends TestCase {

	public void testDomain() {
		assertNotNull(Domain.instance());
	}
}
