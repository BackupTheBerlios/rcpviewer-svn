package de.berlios.rcpviewer.session;

import org.eclipse.emf.ecore.EOperation;

import de.berlios.rcpviewer.AbstractRuntimeTestCase;
import de.berlios.rcpviewer.AbstractTestCase;
import de.berlios.rcpviewer.domain.RuntimeDomain;
import de.berlios.rcpviewer.domain.IRuntimeDomainClass;

public class TestDomainObjectOperation extends AbstractRuntimeTestCase  {

	public TestDomainObjectOperation() {
		super(null);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testCanInvokeOperation() {
		IRuntimeDomainClass<Department> domainClass = 
			(IRuntimeDomainClass<Department>)lookupAny(Department.class);
		
		IDomainObject<Department> domainObject = 
			(IDomainObject<Department>)session.createTransient(domainClass);
		Department pojo = domainObject.getPojo();
		EOperation moveOfficeOperation = domainObject.getEOperationNamed("moveOffice");
		assertFalse(pojo.movedOffice);
		domainObject.invokeOperation(moveOfficeOperation, new Object[]{});
		assertTrue(pojo.movedOffice);
	}


}
