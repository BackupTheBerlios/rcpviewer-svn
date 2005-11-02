package org.essentialplatform.progmodel.standard.domainclass;

import org.essentialplatform.RuntimeDomainSpecifics;
import org.essentialplatform.progmodel.standard.EssentialProgModelDomainBuilder;

/**
 * Binds the tests defined in {@link TestDomainClassImmutable} to the runtime 
 * environment.
 * 
 * @author Dan Haywood
 *
 */
public class TestDomainClassImmutableAtRuntime extends TestDomainClassImmutable {

	public TestDomainClassImmutableAtRuntime() {
		super(new RuntimeDomainSpecifics(), new EssentialProgModelDomainBuilder());
	}

}
