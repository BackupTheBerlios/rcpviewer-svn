package de.berlios.rcpviewer.progmodel.standard.attribute;

import de.berlios.rcpviewer.RuntimeDomainSpecifics;

/**
 * Binds tests in {@link TestDomainClassAttributesUniqueness} to runtime.
 * 
 * @author Dan Haywood
 *
 */
public class TestDomainClassAttributesUniquenessAtRuntime extends
		TestDomainClassAttributesUniqueness {

	public TestDomainClassAttributesUniquenessAtRuntime() {
		super(new RuntimeDomainSpecifics(), null);
	}

}
