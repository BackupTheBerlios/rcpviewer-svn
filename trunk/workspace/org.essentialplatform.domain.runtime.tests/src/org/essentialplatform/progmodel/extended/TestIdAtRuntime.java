package org.essentialplatform.progmodel.extended;

import org.essentialplatform.domain.Deployment;
import org.essentialplatform.domain.runtime.RuntimeDeployment;


/**
 * Binds the tests defined in {@link TestId} to the runtime 
 * environment.
 * 
 * @author Dan Haywood
 *
 */
public class TestIdAtRuntime extends TestId {

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
