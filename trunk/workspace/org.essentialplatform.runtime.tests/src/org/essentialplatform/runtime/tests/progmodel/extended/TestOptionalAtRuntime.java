package org.essentialplatform.runtime.tests.progmodel.extended;

import org.essentialplatform.core.deployment.Deployment;
import org.essentialplatform.progmodel.essential.core.tests.TestOptional;
import org.essentialplatform.progmodel.essential.runtime.EssentialProgModelRuntimeBuilder;
import org.essentialplatform.runtime.RuntimeDeployment;



/**
 * Binds the tests defined in {@link TestOptional} to the runtime 
 * environment.
 * 
 * @author Dan Haywood
 *
 */
public class TestOptionalAtRuntime extends TestOptional {

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
