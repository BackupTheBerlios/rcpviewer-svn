package de.berlios.rcpviewer.progmodel.extended;

import de.berlios.rcpviewer.RuntimeDomainSpecifics;

/**
 * Binds the tests defined in {@link TestFieldLengthOf} to the runtime 
 * environment.
 * 
 * @author Dan Haywood
 *
 */
public class TestFieldLengthOfAtRuntime extends TestFieldLengthOf {

	public TestFieldLengthOfAtRuntime() {
		super(new RuntimeDomainSpecifics(), new ExtendedProgModelDomainBuilder());
	}

}
