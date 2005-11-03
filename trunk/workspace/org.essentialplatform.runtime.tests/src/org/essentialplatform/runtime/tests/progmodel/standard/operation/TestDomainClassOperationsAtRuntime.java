package org.essentialplatform.runtime.tests.progmodel.standard.operation;

import org.essentialplatform.core.deployment.Deployment;
import org.essentialplatform.progmodel.standard.operation.TestDomainClassOperations;
import org.essentialplatform.runtime.domain.runtime.RuntimeDeployment;


/**
 * Binds tests in {@link TestDomainClassOperations} in a runtime 
 * environment, as well as testing runtime-specific features.
 * 
 * @author Dan Haywood
 */
public class TestDomainClassOperationsAtRuntime extends TestDomainClassOperations {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		new RuntimeDeployment();
	}
	
	@Override
	protected void tearDown() throws Exception {
		Deployment.reset();
		super.tearDown();
	}

}
