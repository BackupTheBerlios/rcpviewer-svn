package org.essentialplatform.progmodel.extended;

import org.essentialplatform.AbstractRuntimeTestCase;
import org.essentialplatform.domain.IDomainClass;
import org.essentialplatform.session.Department;
import org.essentialplatform.session.IDomainObject;

/**
 * These tests are obsolete since ExtendedDomainClass et al have been incorporated
 * into DomainClass.
 * 
 * @author Dan Haywood
 *
 */
public class TestExtendedClassAdapter extends AbstractRuntimeTestCase  {

	public TestExtendedClassAdapter() {
		super();
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testDummy() {}
	
	public void obsoletetestGetAdapter() {
		IDomainClass domainClass = 
			(IDomainClass)lookupAny(Department.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();

//		assertNotNull(domainClass.getAdapter(IExtendedRuntimeDomainClass.class));
	}

	public void obsoletetestIsCompatible() {
		IDomainClass domainClass = 
			(IDomainClass)lookupAny(Department.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
//		IExtendedRuntimeDomainClass classAdapter = 
//			domainClass.getAdapter(IExtendedRuntimeDomainClass.class);
//
//		assertTrue(classAdapter.isCompatible(IExtendedDomainObject.class));
	}
	
	public void obsoletetestGetObjectAdapterFor() {
		IDomainClass domainClass = 
			(IDomainClass)lookupAny(Department.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
//		IExtendedRuntimeDomainClass classAdapter = 
//			domainClass.getAdapter(IExtendedRuntimeDomainClass.class);
//
//		IDomainObject<Department> domainObject = 
//			(IDomainObject<Department>)session.create(domainClass);
//		
//		IExtendedDomainObject<Department> objectAdapter = 
//			domainObject.getAdapter(IExtendedDomainObject.class);
//		assertNotNull(objectAdapter);
//		
//		assertTrue(IExtendedRuntimeDomainClass.class.isAssignableFrom(objectAdapter.getExtendedRuntimeDomainClass().getClass()));
//		assertSame(domainObject, objectAdapter.adapts());
	}
	

}
