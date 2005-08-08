package de.berlios.rcpviewer.session;

import org.eclipse.emf.ecore.EOperation;

import de.berlios.rcpviewer.AbstractRuntimeTestCase;
import de.berlios.rcpviewer.domain.IRuntimeDomainClass;
import de.berlios.rcpviewer.progmodel.extended.ExtendedProgModelDomainBuilder;
import de.berlios.rcpviewer.progmodel.extended.IExtendedDomainObject;
import de.berlios.rcpviewer.progmodel.extended.IPrerequisites;
import de.berlios.rcpviewer.progmodel.standard.operation.CustomerOperationReturningDomainObject;
import de.berlios.rcpviewer.progmodel.standard.operation.CustomerOperationReturningVoid;
import de.berlios.rcpviewer.progmodel.standard.operation.CustomerOperationWithPre;
import de.berlios.rcpviewer.progmodel.standard.operation.Order;

public class TestExtendedDomainObjectOperation extends AbstractRuntimeTestCase {

	public TestExtendedDomainObjectOperation() {
		super(new ExtendedProgModelDomainBuilder());
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testDummy() {
		
	}
	
	/**
	 * not working just yet, but hopefully nearly there...
	 *
	 */
	public void incompletetestCanLocateOperationInvokerPre() {
		IRuntimeDomainClass<CustomerOperationWithPre> domainClass = 
			(IRuntimeDomainClass<CustomerOperationWithPre>) lookupAny(CustomerOperationWithPre.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();

		IDomainObject<CustomerOperationWithPre> domainObject = 
			(IDomainObject<CustomerOperationWithPre>) session.createTransient(domainClass);
		CustomerOperationWithPre pojo = domainObject.getPojo();
		
		EOperation placeOrderEOperation = domainObject.getEOperationNamed("placeOrder");
		IDomainObject.IOperation placeOrderOperation = domainObject.getOperation(placeOrderEOperation);
		IExtendedDomainObject<CustomerOperationWithPre> edc = domainObject.getAdapter(IExtendedDomainObject.class);
		IExtendedDomainObject.IExtendedOperation placeOrderExtendedOperation = edc.getOperation(placeOrderEOperation);
			
		
		IPrerequisites prereqs; 
		pojo.placeOrderVeto = false;
		prereqs = placeOrderExtendedOperation.prerequisitesFor();
		assertTrue(prereqs.getUsableRequirement().isMet());
		
		pojo.placeOrderVeto = true;
		prereqs = placeOrderExtendedOperation.prerequisitesFor();
		assertFalse(prereqs.getUsableRequirement().isMet());
		
		
	}

}
