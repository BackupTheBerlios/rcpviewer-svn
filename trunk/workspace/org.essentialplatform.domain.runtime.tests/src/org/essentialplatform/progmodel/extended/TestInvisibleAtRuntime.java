package org.essentialplatform.progmodel.extended;

import org.essentialplatform.RuntimeDomainSpecifics;

/**
 * Binds the tests defined in {@link TestInvisible} to the runtime 
 * environment.
 * 
 * @author Dan Haywood
 *
 */
public class TestInvisibleAtRuntime extends TestInvisible {

	public TestInvisibleAtRuntime() {
		super(new RuntimeDomainSpecifics(), null);
	}

}
