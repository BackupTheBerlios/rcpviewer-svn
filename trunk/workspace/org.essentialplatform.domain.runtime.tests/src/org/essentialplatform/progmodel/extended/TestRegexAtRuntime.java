package org.essentialplatform.progmodel.extended;

import org.essentialplatform.RuntimeDomainSpecifics;
import org.essentialplatform.progmodel.standard.EssentialProgModelExtendedSemanticsDomainBuilder;

/**
 * Binds the tests defined in {@link TestRegex} to the runtime 
 * environment.
 * 
 * @author Dan Haywood
 *
 */
public class TestRegexAtRuntime extends TestRegex {

	public TestRegexAtRuntime() {
		super(new RuntimeDomainSpecifics(), new EssentialProgModelExtendedSemanticsDomainBuilder());
	}

}
