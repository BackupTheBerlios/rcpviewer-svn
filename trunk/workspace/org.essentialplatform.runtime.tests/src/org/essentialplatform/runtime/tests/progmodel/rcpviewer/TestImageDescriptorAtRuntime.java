package org.essentialplatform.runtime.tests.progmodel.rcpviewer;

import org.essentialplatform.core.deployment.Deployment;
import org.essentialplatform.progmodel.louis.runtime.LouisProgModelRuntimeBuilder;
import org.essentialplatform.progmodel.rcpviewer.TestImageDescriptor;
import org.essentialplatform.runtime.RuntimeDeployment;


/**
 * Binds the tests in {@link TestImageDescriptor} to the runtime environment.
 * 
 * @author Dan Haywood
 *
 */
public class TestImageDescriptorAtRuntime extends TestImageDescriptor {

	public TestImageDescriptorAtRuntime() {
		super(new LouisProgModelRuntimeBuilder());
	}

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
