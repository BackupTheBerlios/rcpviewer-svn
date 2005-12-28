package org.essentialplatform.runtime.shared.tests.progmodel.standard.domainclass;

import org.essentialplatform.core.deployment.Binding;
import org.essentialplatform.runtime.shared.RuntimeBinding;
import org.essentialplatform.progmodel.essential.core.tests.TestDomainClassInstantiable;
import org.essentialplatform.progmodel.essential.runtime.EssentialProgModelRuntimeBuilder;

/**
 * Binds the tests defined in {@link TestDomainClassImmutable} to the runtime 
 * environment.
 * 
 * @author Dan Haywood
 *
 */
public class TestDomainClassInstantiableAtRuntime extends TestDomainClassInstantiable {

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
