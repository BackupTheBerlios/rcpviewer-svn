package de.berlios.rcpviewer.progmodel.standard.domainclass;

import de.berlios.rcpviewer.RuntimeDomainSpecifics;

/**
 * Binds the tests defined in {@link TestDomainClassImmutable} to the runtime 
 * environment.
 * 
 * @author Dan Haywood
 *
 */
public class TestDomainClassSearchableAtRuntime extends TestDomainClassSearchable {

	public TestDomainClassSearchableAtRuntime() {
		super(new RuntimeDomainSpecifics(), null);
	}

}
