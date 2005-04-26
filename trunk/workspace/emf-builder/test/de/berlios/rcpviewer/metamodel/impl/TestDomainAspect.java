package de.berlios.rcpviewer.metamodel.impl;
import org.aspectj.lang.*;
import org.aspectj.runtime.*;
import junit.framework.TestCase;
import de.berlios.rcpviewer.metamodel.*;


public class TestDomainAspect extends TestCase {

	/**
	 * Each pojo is automatically wrapped.
	 */
	public void testPojoWrappedInDomainObject() {
		Department d = new Department();
		IDomainObject obj = DomainClassRegistry.instance().getDomainObjectFor(d);
		assertNotNull(obj);
		
		Object pojo = obj.getPojo();
		assertSame(d, pojo);
	}
	
	/**
	 * DomainObject has DomainClass references the correct class
	 */
	public void testDomainObjectHasCorrectClass() {
		Department d = new Department();
		IDomainObject obj = DomainClassRegistry.instance().getDomainObjectFor(d);
		IDomainClass dc = obj.getDomainClass();
		assertSame(Department.class, dc.getJavaClass());
	}

	/**
	 */
	public void testCanGetDomainObjectFromDomainClassRegistry() {
		Department d = new Department();
		IDomainObject obj = DomainClassRegistry.instance().getDomainObjectFor(d);
		IDomainClass dc = obj.getDomainClass();
		assertSame(Department.class, dc.getJavaClass());
	}

	
}
