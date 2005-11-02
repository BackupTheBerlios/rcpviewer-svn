package org.essentialplatform.progmodel.standard.i18n;

import org.essentialplatform.domain.Deployment;
import org.essentialplatform.domain.runtime.RuntimeDeployment;


/**
 * Binds tests in {@link TestInternationalization} in a runtime environment.
 * 
 * @author Dan Haywood
 */
public class TestInternationalizationAtRuntime extends TestInternationalization {

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
