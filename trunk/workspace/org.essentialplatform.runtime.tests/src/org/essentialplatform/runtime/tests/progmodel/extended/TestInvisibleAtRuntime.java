package org.essentialplatform.runtime.tests.progmodel.extended;

import org.essentialplatform.core.deployment.Deployment;
import org.essentialplatform.progmodel.extended.TestInvisible;
import org.essentialplatform.runtime.domain.runtime.RuntimeDeployment;


/**
 * Binds the tests defined in {@link TestInvisible} to the runtime 
 * environment.
 * 
 * @author Dan Haywood
 *
 */
public class TestInvisibleAtRuntime extends TestInvisible {

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
