package de.berlios.rcpviewer.progmodel.extended;

import de.berlios.rcpviewer.RuntimeDomainSpecifics;

/**
 * Binds the tests defined in {@link TestInvisible} to the runtime 
 * environment.
 * 
 * @author Dan Haywood
 *
 */
public class TestInvisibleAtRuntime extends TestInvisible {

	public TestInvisibleAtRuntime() {
		super(new RuntimeDomainSpecifics(), new ExtendedProgModelDomainBuilder());
	}

}
