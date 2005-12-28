package org.essentialplatform.runtime.shared.tests.progmodel.extended;

import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.tests.AbstractRuntimeTestCase;
import org.essentialplatform.runtime.shared.tests.session.fixture.Department;

/**
 * These tests are obsolete since ExtendedDomainClass et al have been incorporated
 * into DomainClass.
 * 
 * @author Dan Haywood
 *
 */
public class TestExtendedClassAdapter extends AbstractRuntimeTestCase  {

	public void testDummy() {}
	
	public void obsoletetestGetAdapter() {
		IDomainClass domainClass = lookupAny(Department.class);

//		assertNotNull(domainClass.getAdapter(IExtendedRuntimeDomainClass.class));
	}

	public void obsoletetestIsCompatible() {
		IDomainClass domainClass = lookupAny(Department.class);
		
//		IExtendedRuntimeDomainClass classAdapter = 
//			domainClass.getAdapter(IExtendedRuntimeDomainClass.class);
//
//		assertTrue(classAdapter.isCompatible(IExtendedDomainObject.class));
	}
	
	public void obsoletetestGetObjectAdapterFor() {
		IDomainClass domainClass = lookupAny(Department.class);
		
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
