package org.essentialplatform.runtime.tests.progmodel.standard.attribute;

import org.essentialplatform.core.deployment.Deployment;
import org.essentialplatform.progmodel.standard.attribute.TestDomainClassAttributesUnsettability;
import org.essentialplatform.runtime.domain.runtime.RuntimeDeployment;


/**
 * Binds tests in {@link TestDomainClassAttributesUnsettability} to runtime.
 * 
 * @author Dan Haywood
 *
 */
public class TestDomainClassAttributesUnsettabilityAtRuntime extends
		TestDomainClassAttributesUnsettability {

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
