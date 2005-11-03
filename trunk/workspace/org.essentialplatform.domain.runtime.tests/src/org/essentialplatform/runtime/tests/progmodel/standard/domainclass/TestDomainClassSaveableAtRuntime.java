package org.essentialplatform.runtime.tests.progmodel.standard.domainclass;

import org.essentialplatform.core.deployment.Deployment;
import org.essentialplatform.progmodel.standard.domainclass.TestDomainClassSaveable;
import org.essentialplatform.runtime.domain.runtime.RuntimeDeployment;



/**
 * Binds the tests defined in {@link TestDomainClassImmutable} to the runtime 
 * environment.
 * 
 * @author Dan Haywood
 *
 */
public class TestDomainClassSaveableAtRuntime extends TestDomainClassSaveable {

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
