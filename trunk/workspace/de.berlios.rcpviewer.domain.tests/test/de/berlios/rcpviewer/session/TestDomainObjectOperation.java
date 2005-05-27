package de.berlios.rcpviewer.session;

import org.eclipse.emf.ecore.EOperation;

import de.berlios.rcpviewer.AbstractTestCase;
import de.berlios.rcpviewer.domain.Domain;
import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.domain.IRuntimeDomainClass;

public class TestDomainObjectOperation extends AbstractTestCase  {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testCanInvokeOperation() {
		IRuntimeDomainClass<Department> domainClass = 
			Domain.lookupAny(Department.class);
		
		IDomainObject<Department> domainObject = 
			(IDomainObject<Department>)session.createTransient(domainClass);
		Department pojo = domainObject.getPojo();
		EOperation moveOfficeOperation = domainObject.getEOperationNamed("moveOffice");
		assertFalse(pojo.movedOffice);
		domainObject.invokeOperation(moveOfficeOperation, new Object[]{});
		assertTrue(pojo.movedOffice);
	}


}
