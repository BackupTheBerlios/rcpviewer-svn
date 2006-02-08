package org.essentialplatform.runtime.shared.tests.progmodel.extended;

import org.essentialplatform.core.deployment.Binding;
import org.essentialplatform.progmodel.essential.core.tests.TestOptional;
import org.essentialplatform.progmodel.essential.runtime.EssentialProgModelRuntimeBuilder;
import org.essentialplatform.runtime.client.domain.bindings.RuntimeClientBinding;



/**
 * Binds the tests defined in {@link TestOptional} to the runtime 
 * environment.
 * 
 * @author Dan Haywood
 *
 */
public class TestOptionalAtRuntime extends TestOptional {

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
