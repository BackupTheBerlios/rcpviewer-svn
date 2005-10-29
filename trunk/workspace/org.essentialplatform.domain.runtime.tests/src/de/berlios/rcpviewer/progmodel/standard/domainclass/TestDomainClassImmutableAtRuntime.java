package de.berlios.rcpviewer.progmodel.standard.domainclass;

import de.berlios.rcpviewer.RuntimeDomainSpecifics;
import de.berlios.rcpviewer.progmodel.standard.EssentialProgModelDomainBuilder;

/**
 * Binds the tests defined in {@link TestDomainClassImmutable} to the runtime 
 * environment.
 * 
 * @author Dan Haywood
 *
 */
public class TestDomainClassImmutableAtRuntime extends TestDomainClassImmutable {

	public TestDomainClassImmutableAtRuntime() {
		super(new RuntimeDomainSpecifics(), new EssentialProgModelDomainBuilder());
	}

}
