package org.essentialplatform.runtime.tests.progmodel.standard.operation;

import org.essentialplatform.core.deployment.Binding;
import org.essentialplatform.runtime.RuntimeBinding;
import org.essentialplatform.progmodel.essential.core.tests.TestDomainClassOperations;
import org.essentialplatform.progmodel.essential.runtime.EssentialProgModelRuntimeBuilder;


/**
 * Binds tests in {@link TestDomainClassOperations} in a runtime 
 * environment, as well as testing runtime-specific features.
 * 
 * @author Dan Haywood
 */
public class TestDomainClassOperationsAtRuntime extends TestDomainClassOperations {

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
