package org.essentialplatform.runtime.tests.progmodel.standard.attribute;

import org.essentialplatform.core.deployment.Deployment;
import org.essentialplatform.runtime.RuntimeDeployment;
import org.essentialplatform.progmodel.essential.core.tests.TestDomainClassAttributesOrdering;
import org.essentialplatform.progmodel.essential.runtime.EssentialProgModelRuntimeBuilder;


/**
 * Binds tests in {@link TestDomainClassAttributesOrdering} to runtime.
 * 
 * @author Dan Haywood
 *
 */
public class TestDomainClassAttributesOrderingAtRuntime extends
		TestDomainClassAttributesOrdering {

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
