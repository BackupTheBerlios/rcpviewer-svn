package org.essentialplatform.progmodel.standard.domainclass;

import org.essentialplatform.RuntimeDomainSpecifics;
import org.essentialplatform.progmodel.standard.EssentialProgModelExtendedSemanticsDomainBuilder;

/**
 * Binds the tests defined in {@link TestDomainClassImmutable} to the runtime 
 * environment.
 * 
 * @author Dan Haywood
 *
 */
public class TestDomainClassSaveableAtRuntime extends TestDomainClassSaveable {

	public TestDomainClassSaveableAtRuntime() {
		super(new RuntimeDomainSpecifics(), new EssentialProgModelExtendedSemanticsDomainBuilder());
	}

}
