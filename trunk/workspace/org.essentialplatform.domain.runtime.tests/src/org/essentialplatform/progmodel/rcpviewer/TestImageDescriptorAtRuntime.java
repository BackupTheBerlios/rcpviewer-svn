package org.essentialplatform.progmodel.rcpviewer;

import org.essentialplatform.domain.Deployment;
import org.essentialplatform.domain.runtime.RuntimeDeployment;


/**
 * Binds the tests in {@link TestImageDescriptor} to the runtime environment.
 * 
 * @author Dan Haywood
 *
 */
public class TestImageDescriptorAtRuntime extends TestImageDescriptor {

	public TestImageDescriptorAtRuntime() {
		super(new RcpViewerProgModelDomainBuilder());
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
