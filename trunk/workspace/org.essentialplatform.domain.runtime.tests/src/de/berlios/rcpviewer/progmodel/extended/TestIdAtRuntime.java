package de.berlios.rcpviewer.progmodel.extended;

import de.berlios.rcpviewer.RuntimeDomainSpecifics;


/**
 * Binds the tests defined in {@link TestId} to the runtime 
 * environment.
 * 
 * @author Dan Haywood
 *
 */
public class TestIdAtRuntime extends TestId {

	public TestIdAtRuntime() {
		super(new RuntimeDomainSpecifics(), null);
	}

}
