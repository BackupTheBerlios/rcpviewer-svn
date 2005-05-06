package de.berlios.rcpviewer.metamodel;

import org.eclipse.emf.ecore.EClass;

import de.berlios.rcpviewer.progmodel.standard.impl.Department;
import junit.framework.TestCase;

public class TestMetaModel extends TestCase {

	public void tearDown() throws Exception {
		MetaModel.instance().clear();
		super.tearDown();
	}
	
	public void testMetaModelCreated() {
		assertNotNull(MetaModel.instance());
	}
	
	public void testOneMetaModelPerThread() throws InterruptedException {
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
		assertTrue(metaModels[0] != metaModels[1]);
	}


	public void testGetDomainClassFromEClass() {
		IDomainClass<Department> domainClass = 
			MetaModel.instance().register(Department.class);

		EClass eClass = domainClass.getEClass();
		IDomainClass reverseDomainClass = MetaModel.instance().domainClassFor(eClass);
		assertNotNull(reverseDomainClass);
		assertSame(reverseDomainClass, domainClass);
	}

}
