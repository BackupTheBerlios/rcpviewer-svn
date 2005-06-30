package de.berlios.rcpviewer.progmodel.extended;

import org.eclipse.emf.ecore.EAttribute;

import de.berlios.rcpviewer.AbstractRuntimeTestCase;
import de.berlios.rcpviewer.domain.IRuntimeDomainClass;
import de.berlios.rcpviewer.progmodel.ProgrammingModelException;
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

		assertNotNull(domainClass.getAdapter(ExtendedDomainClass.class));
		assertNotNull(domainClass.getAdapter(ExtendedRuntimeDomainClass.class));
	}

	public void testIsCompatible() {
		IRuntimeDomainClass<Department> domainClass = 
			(IRuntimeDomainClass<Department>)lookupAny(Department.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		ExtendedRuntimeDomainClass<Department> classAdapter = 
			domainClass.getAdapter(ExtendedRuntimeDomainClass.class);

		assertTrue(classAdapter.isCompatible(ExtendedDomainObject.class));
	}
	
	public void testGetObjectAdapterFor() {
		IRuntimeDomainClass<Department> domainClass = 
			(IRuntimeDomainClass<Department>)lookupAny(Department.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		ExtendedRuntimeDomainClass<Department> classAdapter = 
			domainClass.getAdapter(ExtendedRuntimeDomainClass.class);

		IDomainObject<Department> domainObject = 
			(IDomainObject<Department>)session.createTransient(domainClass);
		
		ExtendedDomainObject<Department> objectAdapter = 
			domainObject.getAdapter(ExtendedDomainObject.class);
		assertNotNull(objectAdapter);
		
		assertSame(ExtendedRuntimeDomainClass.class, objectAdapter.getExtendedRuntimeDomainClass().getClass());
		assertSame(domainObject, objectAdapter.adapts());
	}
	

}
