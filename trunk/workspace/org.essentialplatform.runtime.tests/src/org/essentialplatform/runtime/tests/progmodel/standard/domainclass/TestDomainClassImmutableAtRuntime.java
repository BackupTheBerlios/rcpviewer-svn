package org.essentialplatform.runtime.tests.progmodel.standard.domainclass;


import org.essentialplatform.core.deployment.Deployment;
import org.essentialplatform.progmodel.standard.domainclass.TestDomainClassImmutable;
import org.essentialplatform.runtime.domain.runtime.RuntimeDeployment;
import org.essentialplatform.runtime.progmodel.standard.EssentialProgModelDomainBuilder;

/**
 * Binds the tests defined in {@link TestDomainClassImmutable} to the runtime 
 * environment.
 * 
 * @author Dan Haywood
 *
 */
public class TestDomainClassImmutableAtRuntime extends TestDomainClassImmutable {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		new RuntimeDeployment();
	}
	
	@Override
	protected void tearDown() throws Exception {
		Deployment.reset();
		super.tearDown();
	}

}