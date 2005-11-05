package org.essentialplatform.runtime.tests.progmodel.standard.namesanddesc;

import org.essentialplatform.core.deployment.Deployment;
import org.essentialplatform.progmodel.standard.namesanddesc.TestExplicitNamesAndDescriptions;
import org.essentialplatform.runtime.RuntimeDeployment;



/**
 * Binds tests in {@link TestExplicitNamesAndDescriptions} in a runtime 
 * environment.
 * 
 * @author Dan Haywood
 */
public class TestExplicitNamesAndDescriptionsAtRuntime extends TestExplicitNamesAndDescriptions {

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
