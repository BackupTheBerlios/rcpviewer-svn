package de.berlios.rcpviewer.progmodel.extended;

import de.berlios.rcpviewer.RuntimeDomainSpecifics;
import de.berlios.rcpviewer.progmodel.standard.EssentialProgModelExtendedSemanticsDomainBuilder;

/**
 * Binds the tests defined in {@link TestBusinessKey} to the runtime 
 * environment.
 * 
 * @author Dan Haywood
 *
 */
public class TestBusinessKeyAtRuntime extends TestBusinessKey {

	public TestBusinessKeyAtRuntime() {
		super(new RuntimeDomainSpecifics(), null);
	}

}
