package org.essentialplatform.progmodel.standard.domainclass;

import org.essentialplatform.RuntimeDomainSpecifics;

/**
 * Binds the tests defined in {@link TestDomainClassImmutable} to the runtime 
 * environment.
 * 
 * @author Dan Haywood
 *
 */
public class TestDomainClassInstantiableAtRuntime extends TestDomainClassInstantiable {

	public TestDomainClassInstantiableAtRuntime() {
		super(new RuntimeDomainSpecifics(), null);
	}

}
