package de.berlios.rcpviewer.progmodel.extended;

import de.berlios.rcpviewer.AbstractRuntimeTestCase;
import de.berlios.rcpviewer.domain.IRuntimeDomainClass;
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
		IRuntimeDomainClass<Department> domainClass = 
			(IRuntimeDomainClass<Department>)lookupAny(Department.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();

		assertNotNull(domainClass.getAdapter(IExtendedDomainClass.class));
		assertNotNull(domainClass.getAdapter(IExtendedRuntimeDomainClass.class));
	}

	public void testIsCompatible() {
		IRuntimeDomainClass<Department> domainClass = 
			(IRuntimeDomainClass<Department>)lookupAny(Department.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		IExtendedRuntimeDomainClass<Department> classAdapter = 
			domainClass.getAdapter(IExtendedRuntimeDomainClass.class);

		assertTrue(classAdapter.isCompatible(IExtendedDomainObject.class));
	}
	
	public void testGetObjectAdapterFor() {
		IRuntimeDomainClass<Department> domainClass = 
			(IRuntimeDomainClass<Department>)lookupAny(Department.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		IExtendedRuntimeDomainClass<Department> classAdapter = 
			domainClass.getAdapter(IExtendedRuntimeDomainClass.class);

		IDomainObject<Department> domainObject = 
			(IDomainObject<Department>)session.createTransient(domainClass);
		
		IExtendedDomainObject<Department> objectAdapter = 
			domainObject.getAdapter(IExtendedDomainObject.class);
		assertNotNull(objectAdapter);
		
		assertTrue(IExtendedRuntimeDomainClass.class.isAssignableFrom(objectAdapter.getExtendedRuntimeDomainClass().getClass()));
		assertSame(domainObject, objectAdapter.adapts());
	}
	

}
