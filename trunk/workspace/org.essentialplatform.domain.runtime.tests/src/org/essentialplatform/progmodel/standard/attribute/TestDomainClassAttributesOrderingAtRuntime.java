package org.essentialplatform.progmodel.standard.attribute;

import org.essentialplatform.RuntimeDomainSpecifics;

/**
 * Binds tests in {@link TestDomainClassAttributesOrdering} to runtime.
 * 
 * @author Dan Haywood
 *
 */
public class TestDomainClassAttributesOrderingAtRuntime extends
		TestDomainClassAttributesOrdering {

	public TestDomainClassAttributesOrderingAtRuntime() {
		super(new RuntimeDomainSpecifics(), null);
	}

}
