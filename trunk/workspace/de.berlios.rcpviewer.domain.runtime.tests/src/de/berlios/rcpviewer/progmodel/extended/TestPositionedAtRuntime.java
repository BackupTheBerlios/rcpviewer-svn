package de.berlios.rcpviewer.progmodel.extended;

import de.berlios.rcpviewer.RuntimeDomainSpecifics;

/**
 * Binds the tests defined in {@link TestPositionedAt} to the runtime 
 * environment.
 * 
 * @author Dan Haywood
 *
 */
public class TestPositionedAtRuntime extends TestPositionedAt {

	public TestPositionedAtRuntime() {
		super(new RuntimeDomainSpecifics(), new ExtendedProgModelDomainBuilder());
	}

}
