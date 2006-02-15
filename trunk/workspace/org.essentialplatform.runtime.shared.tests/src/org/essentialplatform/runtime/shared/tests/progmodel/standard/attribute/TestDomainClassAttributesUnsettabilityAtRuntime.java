package org.essentialplatform.runtime.shared.tests.progmodel.standard.attribute;

import org.essentialplatform.core.deployment.Binding;
import org.essentialplatform.runtime.client.domain.bindings.RuntimeClientBinding;
import org.essentialplatform.progmodel.essential.core.tests.TestDomainClassAttributesUnsettability;
import org.essentialplatform.progmodel.essential.runtime.EssentialProgModelRuntimeBuilder;


/**
 * Binds tests in {@link TestDomainClassAttributesUnsettability} to runtime.
 * 
 * @author Dan Haywood
 *
 */
public class TestDomainClassAttributesUnsettabilityAtRuntime extends
		TestDomainClassAttributesUnsettability {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		Binding.setBinding(
				new RuntimeClientBinding().initPrimaryBuilder(new EssentialProgModelRuntimeBuilder()));
	}
	
	@Override
	protected void tearDown() throws Exception {
		Binding.reset();
		super.tearDown();
	}

}
