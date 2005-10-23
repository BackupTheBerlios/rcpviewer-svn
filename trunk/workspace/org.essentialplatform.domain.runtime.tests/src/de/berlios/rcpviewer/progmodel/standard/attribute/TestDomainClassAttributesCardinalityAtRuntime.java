package de.berlios.rcpviewer.progmodel.standard.attribute;

import de.berlios.rcpviewer.RuntimeDomainSpecifics;

/**
 * Binds tests in {@link TestDomainClassAttributesCardinality} to runtime.
 * 
 * @author Dan Haywood
 *
 */
public class TestDomainClassAttributesCardinalityAtRuntime extends
		TestDomainClassAttributesCardinality {

	public TestDomainClassAttributesCardinalityAtRuntime() {
		super(new RuntimeDomainSpecifics(), null);
	}

}
