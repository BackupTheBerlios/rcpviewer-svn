package de.berlios.rcpviewer.progmodel.standard.impl;
import org.aspectj.lang.*;
import org.aspectj.runtime.*;
import junit.framework.TestCase;
import de.berlios.rcpviewer.metamodel.*;
import de.berlios.rcpviewer.session.local.Session;


public class TestDomainAspect extends TestCase {

	/**
	 * Each pojo is automatically wrapped.
	 */
	public void testPojoWrappedInDomainObject() {
		Department d = new Department();
		IDomainObject obj = Session.instance().getWrapper().wrapped(d);
		assertNotNull(obj);
		
		Object pojo = obj.getPojo();
		assertSame(d, pojo);
	}
	
	/**
	 * DomainObject has DomainClass references the correct class
	 */
	public void testDomainObjectHasCorrectClass() {
		Department d = new Department();
		IDomainObject obj = Session.instance().getWrapper().wrapped(d);
		IDomainClass dc = obj.getDomainClass();
		assertSame(Department.class, dc.getJavaClass());
	}

	/**
	 */
	public void testCanGetDomainObjectFromDomainClassRegistry() {
		Department d = new Department();
		IDomainObject obj = Session.instance().getWrapper().wrapped(d);
		IDomainClass dc = obj.getDomainClass();
		assertSame(Department.class, dc.getJavaClass());
	}

	
}
