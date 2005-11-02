package org.essentialplatform.progmodel.standard.attribute;

import org.essentialplatform.domain.Deployment;
import org.essentialplatform.domain.runtime.RuntimeDeployment;


/**
 * Binds tests in {@link TestDomainClassAttributesCardinality} to runtime.
 * 
 * @author Dan Haywood
 *
 */
public class TestDomainClassAttributesCardinalityAtRuntime extends
		TestDomainClassAttributesCardinality {

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
