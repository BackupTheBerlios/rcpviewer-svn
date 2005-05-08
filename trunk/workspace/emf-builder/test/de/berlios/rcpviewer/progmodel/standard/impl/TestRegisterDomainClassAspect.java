package de.berlios.rcpviewer.progmodel.standard.impl;

import junit.framework.TestCase;
import de.berlios.rcpviewer.metamodel.*;
import de.berlios.rcpviewer.progmodel.standard.DomainClass;

public class TestRegisterDomainClassAspect extends TestCase {

	
	protected void tearDown() throws Exception {
		MetaModel.instance().clear();
		super.tearDown();
	}

	/**
	 * Classes can be registered explicitly.
	 */
	public void testExplicitRegistration() {
		assertNull(MetaModel.instance().lookup(Department.class));
		MetaModel.instance().register(Department.class);
		assertNotNull(MetaModel.instance().lookup(Department.class));
	}


	/**
	 * Instantiating a domain class is enough to register it (though
	 * links between classes cannot be setup implicitly).
	 * 
	 * <p>
	 * TODO: not working yet
	 */
	public void incompletetestImplicitRegistrationByDomainClassInstantiation() {
		assertNull(MetaModel.instance().lookup(Department.class));
		new DomainClass(Department.class);
		assertNotNull(MetaModel.instance().lookup(Department.class));
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
		assertNull(MetaModel.instance().lookup(Department.class));
		Class.forName(Department.class.getName());
		assertNotNull(MetaModel.instance().lookup(Department.class));
	}
	

	
	/**
	 * Instantiating an object is enough to register it (though
	 * links between classes cannot be setup implicitly).
	 */
	public void testImplicitRegistration() {
		assertNull(MetaModel.instance().lookup(Department.class));
		new Department();
		assertNotNull(MetaModel.instance().lookup(Department.class));
	}


	/**
	 * The registry remembers what's in it :-)
	 */
	public void testCanLookupOnceRegistered() {
		assertNull(MetaModel.instance().lookup(Department.class));
		MetaModel.instance().register(Department.class);
		MetaModel.instance().done();
		DomainClass d = MetaModel.instance().lookup(Department.class);
		assertNotNull(d);
	}



}
