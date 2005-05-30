package de.berlios.rcpviewer.progmodel.standard.attribute;

import de.berlios.rcpviewer.RuntimeDomainSpecifics;

/**
 * Bind tests in {@link TestDomainClassAttributes} to the runtime environment.
 * @author Dan Haywood
 *
 */
public class TestDomainClassAttributesAtRuntime extends TestDomainClassAttributes {

	public TestDomainClassAttributesAtRuntime() {
		super(new RuntimeDomainSpecifics(), null);
	}

}
