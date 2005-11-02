package org.essentialplatform.progmodel.extended;

import org.essentialplatform.RuntimeDomainSpecifics;
import org.essentialplatform.progmodel.standard.EssentialProgModelExtendedSemanticsDomainBuilder;

/**
 * Binds the tests defined in {@link TestFieldLengthOf} to the runtime 
 * environment.
 * 
 * @author Dan Haywood
 *
 */
public class TestFieldLengthOfAtRuntime extends TestFieldLengthOf {

	public TestFieldLengthOfAtRuntime() {
		super(new RuntimeDomainSpecifics(), new EssentialProgModelExtendedSemanticsDomainBuilder());
	}

}
