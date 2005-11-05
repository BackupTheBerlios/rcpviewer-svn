package org.essentialplatform.runtime.tests.progmodel.extended;

import org.essentialplatform.core.deployment.Deployment;
import org.essentialplatform.progmodel.extended.TestMaxLengthOf;
import org.essentialplatform.runtime.RuntimeDeployment;



/**
 * Binds the tests defined in {@link TestMaxLengthOf} to the runtime 
 * environment.
 * 
 * @author Dan Haywood
 *
 */
public class TestMaxLengthOfAtRuntime extends TestMaxLengthOf {

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
