package org.essentialplatform.progmodel.extended;

import org.essentialplatform.RuntimeDomainSpecifics;

/**
 * Binds the tests defined in {@link TestRelativeOrderForAttributes} to the runtime 
 * environment.
 * 
 * @author Dan Haywood
 *
 */
public class TestRelativeOrderForAttributesAtRuntime extends TestRelativeOrderForAttributes {

	public TestRelativeOrderForAttributesAtRuntime() {
		super(new RuntimeDomainSpecifics(), null);
	}

}
