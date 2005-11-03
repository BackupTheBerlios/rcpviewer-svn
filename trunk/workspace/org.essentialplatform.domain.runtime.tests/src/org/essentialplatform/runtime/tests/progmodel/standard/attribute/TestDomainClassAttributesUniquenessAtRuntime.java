package org.essentialplatform.runtime.tests.progmodel.standard.attribute;

import org.essentialplatform.core.deployment.Deployment;
import org.essentialplatform.progmodel.standard.attribute.TestDomainClassAttributesUniqueness;
import org.essentialplatform.runtime.domain.runtime.RuntimeDeployment;


/**
 * Binds tests in {@link TestDomainClassAttributesUniqueness} to runtime.
 * 
 * @author Dan Haywood
 *
 */
public class TestDomainClassAttributesUniquenessAtRuntime extends
		TestDomainClassAttributesUniqueness {

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
