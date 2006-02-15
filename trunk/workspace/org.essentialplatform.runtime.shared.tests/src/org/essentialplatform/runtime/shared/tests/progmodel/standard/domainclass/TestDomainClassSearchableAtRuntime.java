package org.essentialplatform.runtime.shared.tests.progmodel.standard.domainclass;

import org.essentialplatform.core.deployment.Binding;
import org.essentialplatform.runtime.client.domain.bindings.RuntimeClientBinding;
import org.essentialplatform.progmodel.essential.core.tests.TestDomainClassSearchable;
import org.essentialplatform.progmodel.essential.runtime.EssentialProgModelRuntimeBuilder;


/**
 * Binds the tests defined in {@link TestDomainClassImmutable} to the runtime 
 * environment.
 * 
 * @author Dan Haywood
 *
 */
public class TestDomainClassSearchableAtRuntime extends TestDomainClassSearchable {

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
