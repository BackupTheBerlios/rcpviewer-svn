package org.essentialplatform.progmodel.extended;

import org.essentialplatform.RuntimeDomainSpecifics;
import org.essentialplatform.progmodel.standard.EssentialProgModelExtendedSemanticsDomainBuilder;

/**
 * Binds the tests defined in {@link TestMask} to the runtime 
 * environment.
 * 
 * @author Dan Haywood
 *
 */
public class TestMaskAtRuntime extends TestMask {

	public TestMaskAtRuntime() {
		super(new RuntimeDomainSpecifics(), new EssentialProgModelExtendedSemanticsDomainBuilder());
	}

}
