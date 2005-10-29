package de.berlios.rcpviewer.progmodel.extended;

import de.berlios.rcpviewer.RuntimeDomainSpecifics;
import de.berlios.rcpviewer.progmodel.standard.EssentialProgModelExtendedSemanticsDomainBuilder;

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
