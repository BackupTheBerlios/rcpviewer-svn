package de.berlios.rcpviewer.progmodel.standard.impl;

import junit.framework.TestCase;
import de.berlios.rcpviewer.metamodel.*;
import de.berlios.rcpviewer.progmodel.standard.DomainClass;

public class TestRegisterDomainClassAspect extends TestCase {

	
	private MetaModel metaModel;
	protected void setUp() throws Exception {
		super.setUp();
		metaModel = new MetaModel();
	}

	protected void tearDown() throws Exception {
		metaModel = null;
		MetaModel.threadInstance().reset();
		super.tearDown();
	}

	/**
	 * Classes can be registered explicitly using lookup.
	 */
	public void testExplicitRegistration() {
		assertNull(metaModel.lookupNoRegister(Department.class));
		metaModel.lookup(Department.class);
		assertNotNull(metaModel.lookupNoRegister(Department.class));
	}


	/**
	 * Instantiating a domain class is enough to register it (though
	 * links between classes cannot be setup implicitly).
	 * 
	 * <p>
	 * TODO: not working yet
	 */
	public void incompletetestImplicitRegistrationByDomainClassInstantiation() {
		assertNull(metaModel.lookupNoRegister(Department.class));
		new DomainClass<Department>(Department.class);
		assertNotNull(metaModel.lookupNoRegister(Department.class));
	}


	/**
	 * Using Class#forName() should be enough to register a class (though
	 * links between classes cannot be setup implicitly).
	 *  
	 * <p>
	 * Would have preferred to be simply able to use Xxx.class, perhaps
	 * using staticinitialization pointcut?
	 * 
	 * @throws ClassNotFoundException
	 */
	public void testImplicitRegistrationByClassLoading() throws ClassNotFoundException {
		assertNull(MetaModel.threadInstance().lookupNoRegister(Department.class));
		Class.forName(Department.class.getName());
		assertNotNull(MetaModel.threadInstance().lookupNoRegister(Department.class));
	}
	

	
	/**
	 * Instantiating an object is enough to register it (though
	 * links between classes cannot be setup implicitly).
	 */
	public void testImplicitRegistration() {
		assertNull(MetaModel.threadInstance().lookupNoRegister(Department.class));
		new Department();
		assertNotNull(MetaModel.threadInstance().lookupNoRegister(Department.class));
	}


	/**
	 * The registry remembers what's in it :-)
	 */
	public void testCanLookupOnceRegistered() {
		assertNull(metaModel.lookupNoRegister(Department.class));
		metaModel.lookup(Department.class);
		metaModel.done();
		IDomainClass<Department> d = metaModel.lookupNoRegister(Department.class);
		assertNotNull(d);
	}



}
