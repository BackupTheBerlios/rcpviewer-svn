package de.berlios.rcpviewer.session;

import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.EOperation;

import de.berlios.rcpviewer.AbstractRuntimeTestCase;
import de.berlios.rcpviewer.domain.IRuntimeDomainClass;
import de.berlios.rcpviewer.progmodel.extended.ExtendedProgModelDomainBuilder;
import de.berlios.rcpviewer.progmodel.extended.IExtendedDomainObject;
import de.berlios.rcpviewer.progmodel.extended.IPrerequisites;
import de.berlios.rcpviewer.progmodel.standard.operation.CustomerOperationReturningDomainObject;
import de.berlios.rcpviewer.progmodel.standard.operation.CustomerOperationReturningVoid;
import de.berlios.rcpviewer.progmodel.standard.operation.CustomerOperationWithDefaults;
import de.berlios.rcpviewer.progmodel.standard.operation.CustomerOperationWithPre;
import de.berlios.rcpviewer.progmodel.standard.operation.CustomerOperationWithPreAndArgs;
import de.berlios.rcpviewer.progmodel.standard.operation.Order;
import de.berlios.rcpviewer.progmodel.standard.operation.Product;

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
	
	public void testOperationNoArgWithPre() {
		IRuntimeDomainClass<CustomerOperationWithPre> domainClass = 
			(IRuntimeDomainClass<CustomerOperationWithPre>) lookupAny(CustomerOperationWithPre.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();

		IDomainObject<CustomerOperationWithPre> domainObject = 
			(IDomainObject<CustomerOperationWithPre>) session.create(domainClass);
		CustomerOperationWithPre pojo = domainObject.getPojo();
		
		EOperation eOperation = domainObject.getEOperationNamed("placeOrder");
		IDomainObject.IOperation op = domainObject.getOperation(eOperation);
		IExtendedDomainObject<CustomerOperationWithPre> edc = domainObject.getAdapter(IExtendedDomainObject.class);
		IExtendedDomainObject.IExtendedOperation extendedOp = edc.getOperation(eOperation);
			
		
		IPrerequisites prereqs; 
		pojo.placeOrderVeto = false;
		prereqs = extendedOp.prerequisitesFor();
		assertTrue(prereqs.getUsableRequirement().isMet());
		
		pojo.placeOrderVeto = true;
		prereqs = extendedOp.prerequisitesFor();
		assertFalse(prereqs.getUsableRequirement().isMet());
	}

	public void testOperationArgsAndPre() {
		IRuntimeDomainClass<CustomerOperationWithPreAndArgs> domainClass = 
			(IRuntimeDomainClass<CustomerOperationWithPreAndArgs>) lookupAny(CustomerOperationWithPreAndArgs.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();

		IDomainObject<CustomerOperationWithPreAndArgs> domainObject = 
			(IDomainObject<CustomerOperationWithPreAndArgs>) session.create(domainClass);
		CustomerOperationWithPreAndArgs pojo = domainObject.getPojo();
		
		EOperation eOperation = domainObject.getEOperationNamed("computeDifference");
		IDomainObject.IOperation op = domainObject.getOperation(eOperation);
		IExtendedDomainObject<CustomerOperationWithPre> edc = domainObject.getAdapter(IExtendedDomainObject.class);
		IExtendedDomainObject.IExtendedOperation extendedOp = edc.getOperation(eOperation);
			
		
		IPrerequisites prereqs;
		
		// both args are null (converted as int primitives = 0), so should not meet prereqs 
		prereqs = extendedOp.prerequisitesFor();
		assertFalse(prereqs.getUsableRequirement().isMet());

		// first arg is zero, no joy 
		extendedOp.setArg(0, -1);
		extendedOp.setArg(1, 1);
		prereqs = extendedOp.prerequisitesFor();
		assertFalse(prereqs.getUsableRequirement().isMet());
		
		// second arg is zero, no joy
		extendedOp.reset();
		extendedOp.setArg(0, 1);
		extendedOp.setArg(1, -1);
		prereqs = extendedOp.prerequisitesFor();
		assertFalse(prereqs.getUsableRequirement().isMet());
		
		// second arg is > first, no joy
		extendedOp.reset();
		extendedOp.setArg(0, 5);
		extendedOp.setArg(1, 10);
		prereqs = extendedOp.prerequisitesFor();
		assertFalse(prereqs.getUsableRequirement().isMet());
		
		// all correct
		extendedOp.reset();
		extendedOp.setArg(0, 10);
		extendedOp.setArg(1, 5);
		prereqs = extendedOp.prerequisitesFor();
		assertTrue(prereqs.getUsableRequirement().isMet());
		
	}

	public void testOperationDefaults() {
		IRuntimeDomainClass<CustomerOperationWithDefaults> domainClass = 
			(IRuntimeDomainClass<CustomerOperationWithDefaults>) lookupAny(CustomerOperationWithDefaults.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();

		IDomainObject<CustomerOperationWithDefaults> domainObject = 
			(IDomainObject<CustomerOperationWithDefaults>) session.create(domainClass);
		CustomerOperationWithDefaults pojo = domainObject.getPojo();
		
		EOperation eOperation = domainObject.getEOperationNamed("placeOrder");
		IDomainObject.IOperation op = domainObject.getOperation(eOperation);
		IExtendedDomainObject<CustomerOperationWithPre> edc = domainObject.getAdapter(IExtendedDomainObject.class);
		IExtendedDomainObject.IExtendedOperation extendedOp = edc.getOperation(eOperation);
		
		// instantiating should have reset implicitly, picking up the defaults
		Object[] args = extendedOp.getArgs();
		assertSame(pojo._productDefaulted, args[0]);
		assertEquals(new Integer(pojo._quantityDefaulted), args[1]);
	}


	public void testOperationResetDefaults() {
		IRuntimeDomainClass<CustomerOperationWithDefaults> domainClass = 
			(IRuntimeDomainClass<CustomerOperationWithDefaults>) lookupAny(CustomerOperationWithDefaults.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();

		IDomainObject<CustomerOperationWithDefaults> domainObject = 
			(IDomainObject<CustomerOperationWithDefaults>) session.create(domainClass);
		CustomerOperationWithDefaults pojo = domainObject.getPojo();
		
		EOperation eOperation = domainObject.getEOperationNamed("placeOrder");
		IDomainObject.IOperation op = domainObject.getOperation(eOperation);
		IExtendedDomainObject<CustomerOperationWithPre> edc = domainObject.getAdapter(IExtendedDomainObject.class);
		IExtendedDomainObject.IExtendedOperation extendedOp = edc.getOperation(eOperation);
		
		// trash the defaults
		extendedOp.setArg(0, new Product());
		extendedOp.setArg(1, 21);
		Object[] args = extendedOp.getArgs();
		assertNotSame(pojo._productDefaulted, args[0]);
		assertFalse(new Integer(pojo._quantityDefaulted).equals(args[1]));

		// now reset back to defaults
		args = extendedOp.reset();
		assertSame(pojo._productDefaulted, args[0]);
		assertEquals(new Integer(pojo._quantityDefaulted), args[1]);
	}

	/**
	 * not yet finished...
	 *
	 */
	public void incompletetestOperationListener() {
		IRuntimeDomainClass<CustomerOperationWithPreAndArgs> domainClass = 
			(IRuntimeDomainClass<CustomerOperationWithPreAndArgs>) lookupAny(CustomerOperationWithPreAndArgs.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();

		IDomainObject<CustomerOperationWithPreAndArgs> domainObject = 
			(IDomainObject<CustomerOperationWithPreAndArgs>) session.create(domainClass);
		CustomerOperationWithPreAndArgs pojo = domainObject.getPojo();

		Set<IObservedFeature> features = session.getObservedFeatures();
		
		EOperation eOperation = domainObject.getEOperationNamed("computeDifference");
		IDomainObject.IOperation op = domainObject.getOperation(eOperation);
		IExtendedDomainObject<CustomerOperationWithPre> edc = domainObject.getAdapter(IExtendedDomainObject.class);
		IExtendedDomainObject.IExtendedOperation extendedOp = edc.getOperation(eOperation);
	}


}
