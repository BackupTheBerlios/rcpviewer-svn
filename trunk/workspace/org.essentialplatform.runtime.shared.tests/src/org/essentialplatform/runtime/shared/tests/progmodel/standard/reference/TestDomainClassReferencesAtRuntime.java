package org.essentialplatform.runtime.shared.tests.progmodel.standard.reference;

import org.essentialplatform.core.deployment.Binding;
import org.essentialplatform.runtime.client.domain.bindings.RuntimeClientBinding;
import org.essentialplatform.progmodel.essential.core.tests.TestDomainClassReferences;
import org.essentialplatform.progmodel.essential.runtime.EssentialProgModelRuntimeBuilder;


/**
 * Binds tests in {@link TestDomainClassReferences} in a runtime 
 * environment, as well as testing runtime-specific features.
 * 
 * @author Dan Haywood
 */
public class TestDomainClassReferencesAtRuntime extends TestDomainClassReferences {

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
