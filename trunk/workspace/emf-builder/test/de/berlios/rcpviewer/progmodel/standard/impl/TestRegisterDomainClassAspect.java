package de.berlios.rcpviewer.progmodel.standard.impl;

import junit.framework.TestCase;
import de.berlios.rcpviewer.metamodel.*;
import de.berlios.rcpviewer.progmodel.standard.DomainClass;

public class TestRegisterDomainClassAspect extends TestCase {

	/**
	 * Classes can be registered explicitly.
	 */
	public void testExplicitRegistration() {
		DomainClassRegistry.instance().register(Department.class);
		assertNotNull(DomainClassRegistry.instance().lookup(Department.class));
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
		Class.forName(Department.class.getName());
		assertNotNull(DomainClassRegistry.instance().lookup(Department.class));
	}
	

	/**
	 * Instantiating an object is enough to register it (though
	 * links between classes cannot be setup implicitly).
	 */
	public void testImplicitRegistration() {
		new Department();
		assertNotNull(DomainClassRegistry.instance().lookup(Department.class));
	}


	/**
	 * The registry remembers what's in it :-)
	 */
	public void testCanLookupOnceRegistered() {
		DomainClassRegistry.instance().register(Department.class);
		DomainClassRegistry.instance().done();
		DomainClass d = DomainClassRegistry.instance().lookup(Department.class);
		assertNotNull(d);
	}



}
