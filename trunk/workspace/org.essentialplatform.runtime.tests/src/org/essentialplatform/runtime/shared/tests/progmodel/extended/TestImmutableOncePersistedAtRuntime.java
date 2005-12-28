package org.essentialplatform.runtime.shared.tests.progmodel.extended;

import org.essentialplatform.core.deployment.Binding;
import org.essentialplatform.progmodel.essential.core.tests.TestImmutableOncePersisted;
import org.essentialplatform.progmodel.essential.runtime.EssentialProgModelRuntimeBuilder;
import org.essentialplatform.runtime.shared.RuntimeBinding;


/**
 * Binds the tests defined in {@link TestImmutableOncePersisted} to the runtime 
 * environment.
 * 
 * @author Dan Haywood
 *
 */
public class TestImmutableOncePersistedAtRuntime extends TestImmutableOncePersisted {

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
