package de.berlios.rcpviewer.progmodel.standard.impl;
import de.berlios.rcpviewer.AbstractTestCase;
import de.berlios.rcpviewer.metamodel.IDomainClass;
import de.berlios.rcpviewer.metamodel.IDomainObject;


public class TestDomainAspect extends AbstractTestCase {

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Each pojo is automatically wrapped.
	 */
	public void testPojoWrappedInDomainObject() {
		Department d = new Department();
		IDomainObject obj = getSession().getWrapper().wrapped(d);
		assertNotNull(obj);
		
		Object pojo = obj.getPojo();
		assertSame(d, pojo);
	}
	
	/**
	 * DomainObject has DomainClass references the correct class
	 *
	 */
	public void testDomainObjectHasCorrectClass() {
		Department d = new Department();
		IDomainObject obj = getSession().getWrapper().wrapped(d);
		IDomainClass dc = obj.getDomainClass();
		assertSame(Department.class, dc.getJavaClass());
	}

	/**
	 * <p>
	 * TODO: marked incomplete for now; must create domain objects through
	 * domain class (was getting two copies of IDomainClass). 
	 */
	public void testCanGetDomainObjectFromMetaModel() {
		Department d = new Department();
		IDomainObject obj = getSession().getWrapper().wrapped(d);
		IDomainClass dc = obj.getDomainClass();
		assertSame(Department.class, dc.getJavaClass());
	}

	
}
