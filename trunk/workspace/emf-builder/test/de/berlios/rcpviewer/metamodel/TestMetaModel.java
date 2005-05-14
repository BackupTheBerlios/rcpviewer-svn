package de.berlios.rcpviewer.metamodel;

import org.eclipse.emf.ecore.EClass;

import de.berlios.rcpviewer.progmodel.standard.impl.Department;
import junit.framework.TestCase;

public class TestMetaModel extends TestCase {

	private MetaModel metaModel;

	public void setUp() throws Exception {
		super.setUp();
		metaModel = new MetaModel();
	}
	
	public void tearDown() throws Exception {
		super.tearDown();
		metaModel = null;
	}
	
	public void testMetaModelCreated() {
		assertNotNull(MetaModel.threadInstance());
	}
	
	public void testOneMetaModelPerThread() throws InterruptedException {
		final MetaModel[] metaModels = new MetaModel[2];
		for (int i = 0; i<metaModels.length; i++) {
			final int j = i;
			Thread t = new Thread() {
				public void run() {
					metaModels[j] = MetaModel.threadInstance();
				}
			};
			t.start();
			t.join();
		}
		assertTrue(metaModels[0] != metaModels[1]);
	}


	public void testGetDomainClassFromEClass() {
		IDomainClass<Department> domainClass = 
			metaModel.lookup(Department.class);

		EClass eClass = domainClass.getEClass();
		IDomainClass reverseDomainClass = metaModel.domainClassFor(eClass);
		assertNotNull(reverseDomainClass);
		assertSame(reverseDomainClass, domainClass);
	}

}
