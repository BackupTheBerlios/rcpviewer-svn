package de.berlios.rcpviewer.progmodel.standard.domainclass;

import de.berlios.rcpviewer.RuntimeDomainSpecifics;
import de.berlios.rcpviewer.progmodel.standard.StandardProgModelDomainBuilder;

/**
 * Binds the tests defined in {@link TestDomainClassImmutable} to the runtime 
 * environment.
 * 
 * @author Dan Haywood
 *
 */
public class TestDomainClassSaveableAtRuntime extends TestDomainClassSaveable {

	public TestDomainClassSaveableAtRuntime() {
		super(new RuntimeDomainSpecifics(), new StandardProgModelDomainBuilder());
	}

}