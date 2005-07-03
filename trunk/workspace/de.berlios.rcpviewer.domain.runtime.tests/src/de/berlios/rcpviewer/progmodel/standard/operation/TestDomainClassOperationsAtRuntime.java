package de.berlios.rcpviewer.progmodel.standard.operation;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;

import de.berlios.rcpviewer.AbstractTestCase;
import de.berlios.rcpviewer.IDeploymentSpecifics;
import de.berlios.rcpviewer.RuntimeDomainSpecifics;
import de.berlios.rcpviewer.domain.RuntimeDomain;
import de.berlios.rcpviewer.domain.IDomainBuilder;
import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.domain.IRuntimeDomainClass;
import de.berlios.rcpviewer.progmodel.standard.CustomerExplicitlyInDefaultDomain;
import de.berlios.rcpviewer.progmodel.standard.CustomerImplicitlyInDefaultDomain;


/**
 * Binds tests in {@link TestDomainClassOperations} in a runtime 
 * environment, as well as testing runtime-specific features.
 * 
 * @author Dan Haywood
 */
public class TestDomainClassOperationsAtRuntime 
					extends TestDomainClassOperations {

	public TestDomainClassOperationsAtRuntime() {
		super(new RuntimeDomainSpecifics(), null);
	}
	
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void incompletetestCanInvokeOperationReturningVoidWithNoArgs() {
		// TODO
	}
	
	public void incompletetestCanInvokeOperationReturningDomainObjectWithNoArgs() {
		// TODO
	}
	
	public void incompletetestCanInvokeOperationReturningVoidWithPrimitiveArgs() {
		// TODO
	}
	
	public void incompletetestCanInvokeOperationReturningDomainObjectWithPrimitiveArgs() {
		// TODO
	}
	
	public void incompletetestCanInvokeOperationReturningVoidWithDomainObjectArgs() {
		// TODO
	}
	
	public void incompletetestCanInvokeOperationReturningDomainObjectWithDomainObjectArgs() {
		// TODO
	}
	

}
