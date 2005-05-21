package de.berlios.rcpviewer.progmodel.standard.impl;

import junit.framework.TestCase;
import de.berlios.rcpviewer.domain.*;
import de.berlios.rcpviewer.progmodel.standard.DomainClass;

public class TestRegisterDomainClassAspect extends TestCase {

	
	private Domain metaModel;
	protected void setUp() throws Exception {
		super.setUp();
		metaModel = Domain.instance();
	}

	protected void tearDown() throws Exception {
		metaModel = null;
		Domain.instance().reset();
		super.tearDown();
	}

	/**
	 * Classes can be registered explicitly using lookup.
	 */
	public void incompletetestExplicitRegistration() {
		// incomplete since already registered ...
		// (which is good, right?)
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
	public void incompletetestImplicitRegistrationByClassLoading() throws ClassNotFoundException {
		// incomplete since this is already doing the lookup...
		// (which is good, right?)
		assertNull(Domain.instance().lookupNoRegister(Department.class));
		Class.forName(Department.class.getName());
		assertNotNull(Domain.instance().lookupNoRegister(Department.class));
	}
	

	
	/**
	 * Instantiating an object is enough to register it (though
	 * links between classes cannot be setup implicitly).
	 */
	public void incompletetestImplicitRegistration() {
		// incomplete since this is already doing the lookup...
		// (which is good, right?)
		assertNull(Domain.instance().lookupNoRegister(Department.class));
		new Department();
		assertNotNull(Domain.instance().lookupNoRegister(Department.class));
	}


	/**
	 * The registry remembers what's in it :-)
	 */
	public void testCanLookupOnceRegistered() {
		metaModel.lookup(Department.class); // that is, register.
		IDomainClass<Department> d = metaModel.lookupNoRegister(Department.class);
		assertNotNull(d);
	}



}
