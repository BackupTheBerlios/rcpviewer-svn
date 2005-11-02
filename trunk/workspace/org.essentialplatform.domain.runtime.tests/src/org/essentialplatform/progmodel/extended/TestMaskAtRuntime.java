package org.essentialplatform.progmodel.extended;

import org.essentialplatform.domain.Deployment;
import org.essentialplatform.domain.runtime.RuntimeDeployment;


/**
 * Binds the tests defined in {@link TestMask} to the runtime 
 * environment.
 * 
 * @author Dan Haywood
 *
 */
public class TestMaskAtRuntime extends TestMask {

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
