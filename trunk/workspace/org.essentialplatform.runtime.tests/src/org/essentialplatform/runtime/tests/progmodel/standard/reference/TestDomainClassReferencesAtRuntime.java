package org.essentialplatform.runtime.tests.progmodel.standard.reference;

import org.essentialplatform.core.deployment.Deployment;
import org.essentialplatform.runtime.RuntimeDeployment;
import org.essentialplatform.progmodel.essential.core.tests.TestDomainClassReferences;
import org.essentialplatform.progmodel.essential.runtime.EssentialProgModelRuntimeBuilder;


/**
 * Binds tests in {@link TestDomainClassReferences} in a runtime 
 * environment, as well as testing runtime-specific features.
 * 
 * @author Dan Haywood
 */
public class TestDomainClassReferencesAtRuntime extends TestDomainClassReferences {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		new RuntimeDeployment(new EssentialProgModelRuntimeBuilder());
	}
	
	@Override
	protected void tearDown() throws Exception {
		Deployment.reset();
		super.tearDown();
	}

}
