package org.essentialplatform.runtime.tests.progmodel.standard.domainclass;

import org.essentialplatform.core.deployment.Deployment;
import org.essentialplatform.runtime.RuntimeDeployment;
import org.essentialplatform.progmodel.essential.core.tests.TestDomainClassSaveable;
import org.essentialplatform.progmodel.essential.runtime.EssentialProgModelRuntimeBuilder;



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
		new RuntimeDeployment(new EssentialProgModelRuntimeBuilder());
	}
	
	@Override
	protected void tearDown() throws Exception {
		Deployment.reset();
		super.tearDown();
	}

}
