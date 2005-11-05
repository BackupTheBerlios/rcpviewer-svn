package org.essentialplatform.runtime.tests.progmodel.extended;

import org.essentialplatform.core.deployment.Deployment;
import org.essentialplatform.progmodel.extended.TestFieldLengthOf;
import org.essentialplatform.runtime.RuntimeDeployment;

/**
 * Binds the tests defined in {@link TestFieldLengthOf} to the runtime 
 * environment.
 * 
 * @author Dan Haywood
 *
 */
public class TestFieldLengthOfAtRuntime extends TestFieldLengthOf {

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

