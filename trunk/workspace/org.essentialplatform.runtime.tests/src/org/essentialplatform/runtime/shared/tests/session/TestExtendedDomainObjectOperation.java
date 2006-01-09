package org.essentialplatform.runtime.shared.tests.session;

import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.fixture.progmodel.essential.standard.operation.CustomerOperationWithDefaults;
import org.essentialplatform.core.fixture.progmodel.essential.standard.operation.CustomerOperationWithPre;
import org.essentialplatform.core.fixture.progmodel.essential.standard.operation.CustomerOperationWithPreAndArgs;
import org.essentialplatform.core.fixture.progmodel.essential.standard.operation.Product;
import org.essentialplatform.progmodel.essential.app.IPrerequisites;
import org.essentialplatform.runtime.client.domain.bindings.IObjectOperationClientBinding;
import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.tests.AbstractRuntimeClientTestCase;

public class TestExtendedDomainObjectOperation extends AbstractRuntimeClientTestCase {

	public void testOperationNoArgWithPre() {
		IDomainClass domainClass = lookupAny(CustomerOperationWithPre.class);

		IDomainObject<CustomerOperationWithPre> domainObject = session.create(domainClass);
		CustomerOperationWithPre pojo = domainObject.getPojo();
		
		IDomainClass.IOperation iOperation = domainObject.getIOperationNamed("placeOrder");
		IDomainObject.IObjectOperation op = domainObject.getOperation(iOperation);
		
		IPrerequisites prereqs; 
		pojo.placeOrderVeto = false;
		IObjectOperationClientBinding opBinding = (IObjectOperationClientBinding)op.getBinding();
		prereqs = opBinding.prerequisitesFor();
		assertTrue(prereqs.getUsableRequirement().isMet());
		
		pojo.placeOrderVeto = true;
		prereqs = opBinding.prerequisitesFor();
		assertFalse(prereqs.getUsableRequirement().isMet());
	}

	public void testOperationArgsAndPre() {
 		IDomainClass domainClass = lookupAny(CustomerOperationWithPreAndArgs.class);

		IDomainObject<CustomerOperationWithPreAndArgs> domainObject = session.create(domainClass);
		
		IDomainClass.IOperation iOperation = domainObject.getIOperationNamed("computeDifference");
		IDomainObject.IObjectOperation op = domainObject.getOperation(iOperation);
		
		IPrerequisites prereqs;
		
		// both args are null (converted as int primitives = 0), so should not meet prereqs
		IObjectOperationClientBinding opBinding = (IObjectOperationClientBinding)op.getBinding();
		prereqs = opBinding.prerequisitesFor();
		assertFalse(prereqs.getUsableRequirement().isMet());

		// first arg is zero, no joy
		opBinding.setArg(0, -1);
		opBinding.setArg(1, 1);
		prereqs = opBinding.prerequisitesFor();
		assertFalse(prereqs.getUsableRequirement().isMet());
		
		// second arg is zero, no joy
		opBinding.reset();
		opBinding.setArg(0, 1);
		opBinding.setArg(1, -1);
		prereqs = opBinding.prerequisitesFor();
		assertFalse(prereqs.getUsableRequirement().isMet());
		
		// second arg is > first, no joy
		opBinding.reset();
		opBinding.setArg(0, 5);
		opBinding.setArg(1, 10);
		prereqs = opBinding.prerequisitesFor();
		assertFalse(prereqs.getUsableRequirement().isMet());
		
		// all correct
		opBinding.reset();
		opBinding.setArg(0, 10);
		opBinding.setArg(1, 5);
		prereqs = opBinding.prerequisitesFor();
		assertTrue(prereqs.getUsableRequirement().isMet());
		
	}

	public void testOperationDefaults() {
		IDomainClass domainClass = lookupAny(CustomerOperationWithDefaults.class);

		IDomainObject<CustomerOperationWithDefaults> domainObject = session.create(domainClass);
		CustomerOperationWithDefaults pojo = domainObject.getPojo();
		
		IDomainClass.IOperation iOperation = domainObject.getIOperationNamed("placeOrder");
		IDomainObject.IObjectOperation op = domainObject.getOperation(iOperation);
		
		// instantiating should have reset implicitly, picking up the defaults
		IObjectOperationClientBinding opBinding = (IObjectOperationClientBinding)op.getBinding();
		Object[] args = opBinding.getArgs();
		assertSame(pojo._productDefaulted, args[0]);
		assertEquals(new Integer(pojo._quantityDefaulted), args[1]);
	}


	public void testOperationResetDefaults() {
		IDomainClass domainClass = lookupAny(CustomerOperationWithDefaults.class);

		IDomainObject<CustomerOperationWithDefaults> domainObject = session.create(domainClass);
		CustomerOperationWithDefaults pojo = domainObject.getPojo();
		
		IDomainClass.IOperation iOperation = domainObject.getIOperationNamed("placeOrder");
		IDomainObject.IObjectOperation op = domainObject.getOperation(iOperation);
		
		// trash the defaults
		IObjectOperationClientBinding opBinding = (IObjectOperationClientBinding)op.getBinding();
		opBinding.setArg(0, new Product());
		opBinding.setArg(1, 21);
		Object[] args = opBinding.getArgs();
		assertNotSame(pojo._productDefaulted, args[0]);
		assertFalse(new Integer(pojo._quantityDefaulted).equals(args[1]));

		// now reset back to defaults
		args = opBinding.reset();
		assertSame(pojo._productDefaulted, args[0]);
		assertEquals(new Integer(pojo._quantityDefaulted), args[1]);
	}

	/**
	 * not yet finished...
	 *
	 */
	public void incompletetestOperationListener() {
		IDomainClass domainClass = lookupAny(CustomerOperationWithPreAndArgs.class);

		IDomainObject<CustomerOperationWithPreAndArgs> domainObject = session.create(domainClass);
		
		IDomainClass.IOperation iOperation = domainObject.getIOperationNamed("computeDifference");
		IDomainObject.IObjectOperation op = domainObject.getOperation(iOperation);
	}


}
