package de.berlios.rcpviewer.session;

import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.EOperation;

import de.berlios.rcpviewer.AbstractRuntimeTestCase;
import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.progmodel.extended.IPrerequisites;
import de.berlios.rcpviewer.progmodel.standard.EssentialProgModelExtendedSemanticsDomainBuilder;
import de.berlios.rcpviewer.progmodel.standard.operation.CustomerOperationWithDefaults;
import de.berlios.rcpviewer.progmodel.standard.operation.CustomerOperationWithPre;
import de.berlios.rcpviewer.progmodel.standard.operation.CustomerOperationWithPreAndArgs;
import de.berlios.rcpviewer.progmodel.standard.operation.Product;

public class TestExtendedDomainObjectOperation extends AbstractRuntimeTestCase {

	public TestExtendedDomainObjectOperation() {
		super(new EssentialProgModelExtendedSemanticsDomainBuilder());
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testOperationNoArgWithPre() {
		IDomainClass domainClass = lookupAny(CustomerOperationWithPre.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();

		IDomainObject<?> domainObject = session.create(domainClass);
		CustomerOperationWithPre pojo = (CustomerOperationWithPre)domainObject.getPojo();
		
		EOperation eOperation = domainObject.getEOperationNamed("placeOrder");
		IDomainObject.IObjectOperation op = domainObject.getOperation(eOperation);
		
		IPrerequisites prereqs; 
		pojo.placeOrderVeto = false;
		prereqs = op.prerequisitesFor();
		assertTrue(prereqs.getUsableRequirement().isMet());
		
		pojo.placeOrderVeto = true;
		prereqs = op.prerequisitesFor();
		assertFalse(prereqs.getUsableRequirement().isMet());
	}

	public void testOperationArgsAndPre() {
		IDomainClass domainClass = 
			(IDomainClass) lookupAny(CustomerOperationWithPreAndArgs.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();

		IDomainObject<?> domainObject = session.create(domainClass);
		CustomerOperationWithPreAndArgs pojo = (CustomerOperationWithPreAndArgs)domainObject.getPojo();
		
		EOperation eOperation = domainObject.getEOperationNamed("computeDifference");
		IDomainObject.IObjectOperation op = domainObject.getOperation(eOperation);
		
		IPrerequisites prereqs;
		
		// both args are null (converted as int primitives = 0), so should not meet prereqs 
		prereqs = op.prerequisitesFor();
		assertFalse(prereqs.getUsableRequirement().isMet());

		// first arg is zero, no joy 
		op.setArg(0, -1);
		op.setArg(1, 1);
		prereqs = op.prerequisitesFor();
		assertFalse(prereqs.getUsableRequirement().isMet());
		
		// second arg is zero, no joy
		op.reset();
		op.setArg(0, 1);
		op.setArg(1, -1);
		prereqs = op.prerequisitesFor();
		assertFalse(prereqs.getUsableRequirement().isMet());
		
		// second arg is > first, no joy
		op.reset();
		op.setArg(0, 5);
		op.setArg(1, 10);
		prereqs = op.prerequisitesFor();
		assertFalse(prereqs.getUsableRequirement().isMet());
		
		// all correct
		op.reset();
		op.setArg(0, 10);
		op.setArg(1, 5);
		prereqs = op.prerequisitesFor();
		assertTrue(prereqs.getUsableRequirement().isMet());
		
	}

	public void testOperationDefaults() {
		IDomainClass domainClass = 
			(IDomainClass) lookupAny(CustomerOperationWithDefaults.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();

		IDomainObject<CustomerOperationWithDefaults> domainObject = 
			(IDomainObject<CustomerOperationWithDefaults>) session.create(domainClass);
		CustomerOperationWithDefaults pojo = domainObject.getPojo();
		
		EOperation eOperation = domainObject.getEOperationNamed("placeOrder");
		IDomainObject.IObjectOperation op = domainObject.getOperation(eOperation);
		
		// instantiating should have reset implicitly, picking up the defaults
		Object[] args = op.getArgs();
		assertSame(pojo._productDefaulted, args[0]);
		assertEquals(new Integer(pojo._quantityDefaulted), args[1]);
	}


	public void testOperationResetDefaults() {
		IDomainClass domainClass = 
			(IDomainClass) lookupAny(CustomerOperationWithDefaults.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();

		IDomainObject<CustomerOperationWithDefaults> domainObject = 
			(IDomainObject<CustomerOperationWithDefaults>) session.create(domainClass);
		CustomerOperationWithDefaults pojo = domainObject.getPojo();
		
		EOperation eOperation = domainObject.getEOperationNamed("placeOrder");
		IDomainObject.IObjectOperation op = domainObject.getOperation(eOperation);
		
		// trash the defaults
		op.setArg(0, new Product());
		op.setArg(1, 21);
		Object[] args = op.getArgs();
		assertNotSame(pojo._productDefaulted, args[0]);
		assertFalse(new Integer(pojo._quantityDefaulted).equals(args[1]));

		// now reset back to defaults
		args = op.reset();
		assertSame(pojo._productDefaulted, args[0]);
		assertEquals(new Integer(pojo._quantityDefaulted), args[1]);
	}

	/**
	 * not yet finished...
	 *
	 */
	public void incompletetestOperationListener() {
		IDomainClass domainClass = 
			(IDomainClass) lookupAny(CustomerOperationWithPreAndArgs.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();

		IDomainObject<CustomerOperationWithPreAndArgs> domainObject = 
			(IDomainObject<CustomerOperationWithPreAndArgs>) session.create(domainClass);
		CustomerOperationWithPreAndArgs pojo = domainObject.getPojo();

		Set<IObservedFeature> features = session.getObservedFeatures();
		
		EOperation eOperation = domainObject.getEOperationNamed("computeDifference");
		IDomainObject.IObjectOperation op = domainObject.getOperation(eOperation);
	}


}
