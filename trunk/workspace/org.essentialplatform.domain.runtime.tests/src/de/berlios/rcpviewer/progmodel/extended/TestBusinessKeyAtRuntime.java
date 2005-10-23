package de.berlios.rcpviewer.progmodel.extended;

import de.berlios.rcpviewer.RuntimeDomainSpecifics;

/**
 * Binds the tests defined in {@link TestBusinessKey} to the runtime 
 * environment.
 * 
 * @author Dan Haywood
 *
 */
public class TestBusinessKeyAtRuntime extends TestBusinessKey {

	public TestBusinessKeyAtRuntime() {
		super(new RuntimeDomainSpecifics(), new ExtendedProgModelDomainBuilder());
	}

}
