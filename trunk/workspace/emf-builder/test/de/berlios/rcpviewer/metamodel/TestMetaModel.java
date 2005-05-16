package de.berlios.rcpviewer.metamodel;

import de.berlios.rcpviewer.progmodel.standard.Domain;
import junit.framework.TestCase;

public class TestMetaModel extends TestCase {

	@Domain
	private static class Department { }
	

	private MetaModel metaModel;

	public void setUp() throws Exception {
		super.setUp();
		metaModel = MetaModel.instance();
	}
	
	public void tearDown() throws Exception {
		super.tearDown();
		metaModel = null;
	}
	
	public void testMetaModelCreated() {
		assertNotNull(MetaModel.instance());
	}
	
	public void testMetaModelSharedAcrossThreads() throws InterruptedException {
		final MetaModel[] metaModels = new MetaModel[2];
		for (int i = 0; i<metaModels.length; i++) {
			final int j = i;
			Thread t = new Thread() {
				public void run() {
					metaModels[j] = MetaModel.instance();
				}
			};
			t.start();
			t.join();
		}
		assertSame(metaModels[0], metaModels[1]);
	}


}
