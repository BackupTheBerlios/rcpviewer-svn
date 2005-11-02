package org.essentialplatform.progmodel.extended;

import org.essentialplatform.RuntimeDomainSpecifics;

/**
 * Binds the tests defined in {@link TestMaxLengthOf} to the runtime 
 * environment.
 * 
 * @author Dan Haywood
 *
 */
public class TestMaxLengthOfAtRuntime extends TestMaxLengthOf {

	public TestMaxLengthOfAtRuntime() {
		super(new RuntimeDomainSpecifics(), null);
	}

}
