package org.essentialplatform.progmodel.extended;

import org.essentialplatform.RuntimeDomainSpecifics;
import org.essentialplatform.progmodel.standard.EssentialProgModelExtendedSemanticsDomainBuilder;

/**
 * Binds the tests defined in {@link TestBusinessKey} to the runtime 
 * environment.
 * 
 * @author Dan Haywood
 *
 */
public class TestBusinessKeyAtRuntime extends TestBusinessKey {

	public TestBusinessKeyAtRuntime() {
		super(new RuntimeDomainSpecifics(), null);
	}

}
