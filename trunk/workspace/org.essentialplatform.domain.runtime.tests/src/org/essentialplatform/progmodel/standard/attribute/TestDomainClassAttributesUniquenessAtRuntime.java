package org.essentialplatform.progmodel.standard.attribute;

import org.essentialplatform.RuntimeDomainSpecifics;

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
