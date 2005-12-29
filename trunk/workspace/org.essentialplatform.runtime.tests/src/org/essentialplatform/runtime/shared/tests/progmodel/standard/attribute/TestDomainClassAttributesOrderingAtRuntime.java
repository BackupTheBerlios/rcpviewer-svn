package org.essentialplatform.runtime.shared.tests.progmodel.standard.attribute;

import org.essentialplatform.core.deployment.Binding;
import org.essentialplatform.runtime.client.RuntimeClientBinding;
import org.essentialplatform.progmodel.essential.core.tests.TestDomainClassAttributesOrdering;
import org.essentialplatform.progmodel.essential.runtime.EssentialProgModelRuntimeBuilder;


/**
 * Binds tests in {@link TestDomainClassAttributesOrdering} to runtime.
 * 
 * @author Dan Haywood
 *
 */
public class TestDomainClassAttributesOrderingAtRuntime extends
		TestDomainClassAttributesOrdering {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		Binding.setBinding(
			new RuntimeClientBinding(new EssentialProgModelRuntimeBuilder()));
	}
	
	@Override
	protected void tearDown() throws Exception {
		Binding.reset();
		super.tearDown();
	}

}
