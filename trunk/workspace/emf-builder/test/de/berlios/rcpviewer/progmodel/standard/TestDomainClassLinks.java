package de.berlios.rcpviewer.progmodel.standard;

import de.berlios.rcpviewer.AbstractTestCase;
import de.berlios.rcpviewer.metamodel.IDomainClass;
import de.berlios.rcpviewer.metamodel.MetaModel;

public class TestDomainClassLinks extends AbstractTestCase {

	private IDomainClass domainClass;
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		MetaModel.instance().clear();
		super.tearDown();
	}
	

	public void testDummy(){} 
}
