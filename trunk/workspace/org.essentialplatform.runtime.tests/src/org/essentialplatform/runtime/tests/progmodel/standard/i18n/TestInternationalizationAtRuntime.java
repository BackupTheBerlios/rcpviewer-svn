package org.essentialplatform.runtime.tests.progmodel.standard.i18n;

import org.essentialplatform.core.deployment.Binding;
import org.essentialplatform.runtime.RuntimeBinding;
import org.essentialplatform.progmodel.essential.core.tests.TestInternationalization;
import org.essentialplatform.progmodel.essential.runtime.EssentialProgModelRuntimeBuilder;


/**
 * Binds tests in {@link TestInternationalization} in a runtime environment.
 * 
 * @author Dan Haywood
 */
public class TestInternationalizationAtRuntime extends TestInternationalization {

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
