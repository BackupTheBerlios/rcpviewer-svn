package org.essentialplatform.progmodel.standard.attribute;

import org.essentialplatform.RuntimeDomainSpecifics;

/**
 * Binds tests in {@link TestDomainClassAttributesUnsettability} to runtime.
 * 
 * @author Dan Haywood
 *
 */
public class TestDomainClassAttributesUnsettabilityAtRuntime extends
		TestDomainClassAttributesUnsettability {

	public TestDomainClassAttributesUnsettabilityAtRuntime() {
		super(new RuntimeDomainSpecifics(), null);
	}

}
