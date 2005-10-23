package de.berlios.rcpviewer.progmodel.extended;

import de.berlios.rcpviewer.RuntimeDomainSpecifics;

/**
 * Binds the tests defined in {@link TestOptional} to the runtime 
 * environment.
 * 
 * @author Dan Haywood
 *
 */
public class TestOptionalAtRuntime extends TestOptional {

	public TestOptionalAtRuntime() {
		super(new RuntimeDomainSpecifics(), new ExtendedProgModelDomainBuilder());
	}

}
