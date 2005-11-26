package org.essentialplatform.runtime.tests.progmodel.standard.namesanddesc;

import org.essentialplatform.core.deployment.Binding;
import org.essentialplatform.runtime.RuntimeBinding;
import org.essentialplatform.progmodel.essential.core.tests.TestExplicitNamesAndDescriptions;
import org.essentialplatform.progmodel.essential.runtime.EssentialProgModelRuntimeBuilder;



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
		Binding.setBinding(
				new RuntimeBinding(new EssentialProgModelRuntimeBuilder()));
	}
	
	@Override
	protected void tearDown() throws Exception {
		Binding.reset();
		super.tearDown();
	}
	
}
