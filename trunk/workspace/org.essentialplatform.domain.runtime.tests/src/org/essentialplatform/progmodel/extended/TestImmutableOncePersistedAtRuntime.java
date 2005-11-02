package org.essentialplatform.progmodel.extended;

import org.essentialplatform.RuntimeDomainSpecifics;
import org.essentialplatform.progmodel.standard.EssentialProgModelExtendedSemanticsDomainBuilder;

/**
 * Binds the tests defined in {@link TestImmutableOncePersisted} to the runtime 
 * environment.
 * 
 * @author Dan Haywood
 *
 */
public class TestImmutableOncePersistedAtRuntime extends TestImmutableOncePersisted {

	public TestImmutableOncePersistedAtRuntime() {
		super(new RuntimeDomainSpecifics(), new EssentialProgModelExtendedSemanticsDomainBuilder());
	}

}
