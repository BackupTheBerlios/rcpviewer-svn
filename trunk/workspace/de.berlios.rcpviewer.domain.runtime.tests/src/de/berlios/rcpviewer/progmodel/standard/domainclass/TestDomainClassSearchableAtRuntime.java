package de.berlios.rcpviewer.progmodel.standard.domainclass;

import de.berlios.rcpviewer.RuntimeDomainSpecifics;
import de.berlios.rcpviewer.progmodel.extended.ExtendedProgModelDomainBuilder;
import de.berlios.rcpviewer.progmodel.standard.StandardProgModelDomainBuilder;

/**
 * Binds the tests defined in {@link TestDomainClassImmutable} to the runtime 
 * environment.
 * 
 * @author Dan Haywood
 *
 */
public class TestDomainClassSearchableAtRuntime extends TestDomainClassSearchable {

	public TestDomainClassSearchableAtRuntime() {
		super(new RuntimeDomainSpecifics(), new ExtendedProgModelDomainBuilder());
	}

}
