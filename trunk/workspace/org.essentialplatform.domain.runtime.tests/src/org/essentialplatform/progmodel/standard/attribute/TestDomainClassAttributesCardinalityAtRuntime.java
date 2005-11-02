package org.essentialplatform.progmodel.standard.attribute;

import org.essentialplatform.RuntimeDomainSpecifics;

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
