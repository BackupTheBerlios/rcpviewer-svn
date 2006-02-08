package org.essentialplatform.runtime.shared.tests.progmodel.extended;

import org.essentialplatform.core.deployment.Binding;
import org.essentialplatform.progmodel.essential.core.tests.TestRegex;
import org.essentialplatform.progmodel.essential.runtime.EssentialProgModelRuntimeBuilder;
import org.essentialplatform.runtime.client.domain.bindings.RuntimeClientBinding;


/**
 * Binds the tests defined in {@link TestRegex} to the runtime 
 * environment.
 * 
 * @author Dan Haywood
 *
 */
public class TestRegexAtRuntime extends TestRegex {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		Binding.setBinding(
				new RuntimeClientBinding().init(new EssentialProgModelRuntimeBuilder()));
	}
	
	@Override
	protected void tearDown() throws Exception {
		Binding.reset();
		super.tearDown();
	}

}
