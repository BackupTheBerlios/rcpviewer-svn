package org.essentialplatform.runtime.tests.progmodel.extended;

import org.essentialplatform.core.deployment.Binding;
import org.essentialplatform.progmodel.essential.core.tests.TestMask;
import org.essentialplatform.progmodel.essential.runtime.EssentialProgModelRuntimeBuilder;
import org.essentialplatform.runtime.RuntimeBinding;


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
		Binding.setBinding(
			new RuntimeBinding(new EssentialProgModelRuntimeBuilder()));
	}
	
	@Override
	protected void tearDown() throws Exception {
		Binding.reset();
		super.tearDown();
	}

}
