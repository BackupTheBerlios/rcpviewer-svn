package de.berlios.rcpviewer.progmodel.extended;

import de.berlios.rcpviewer.RuntimeDomainSpecifics;

/**
 * Binds the tests defined in {@link TestOrderForAttributes} to the runtime 
 * environment.
 * 
 * @author Dan Haywood
 *
 */
public class TestOrderForAttributesAtRuntime extends TestOrderForAttributes {

	public TestOrderForAttributesAtRuntime() {
		super(new RuntimeDomainSpecifics(), new ExtendedProgModelDomainBuilder());
	}

}
