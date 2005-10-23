package de.berlios.rcpviewer.progmodel.extended;

import de.berlios.rcpviewer.RuntimeDomainSpecifics;

/**
 * Binds the tests defined in {@link TestMask} to the runtime 
 * environment.
 * 
 * @author Dan Haywood
 *
 */
public class TestMaskAtRuntime extends TestMask {

	public TestMaskAtRuntime() {
		super(new RuntimeDomainSpecifics(), new ExtendedProgModelDomainBuilder());
	}

}
