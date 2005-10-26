package de.berlios.rcpviewer.progmodel.extended;

import de.berlios.rcpviewer.AbstractRuntimeTestCase;
import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.session.Department;
import de.berlios.rcpviewer.session.IDomainObject;

public class TestExtendedClassAdapter extends AbstractRuntimeTestCase  {

	public TestExtendedClassAdapter() {
		super(new ExtendedProgModelDomainBuilder());
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testGetAdapter() {
		IDomainClass domainClass = 
			(IDomainClass)lookupAny(Department.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();

		assertNotNull(domainClass.getAdapter(IExtendedRuntimeDomainClass.class));
	}

	public void testIsCompatible() {
		IDomainClass domainClass = 
			(IDomainClass)lookupAny(Department.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		IExtendedRuntimeDomainClass classAdapter = 
			domainClass.getAdapter(IExtendedRuntimeDomainClass.class);

		assertTrue(classAdapter.isCompatible(IExtendedDomainObject.class));
	}
	
	public void testGetObjectAdapterFor() {
		IDomainClass domainClass = 
			(IDomainClass)lookupAny(Department.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		IExtendedRuntimeDomainClass classAdapter = 
			domainClass.getAdapter(IExtendedRuntimeDomainClass.class);

		IDomainObject<Department> domainObject = 
			(IDomainObject<Department>)session.create(domainClass);
		
		IExtendedDomainObject<Department> objectAdapter = 
			domainObject.getAdapter(IExtendedDomainObject.class);
		assertNotNull(objectAdapter);
		
		assertTrue(IExtendedRuntimeDomainClass.class.isAssignableFrom(objectAdapter.getExtendedRuntimeDomainClass().getClass()));
		assertSame(domainObject, objectAdapter.adapts());
	}
	

}
