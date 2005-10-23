package de.berlios.rcpviewer.progmodel.rcpviewer;

import de.berlios.rcpviewer.RuntimeDomainSpecifics;

/**
 * Binds the tests in {@link TestImageDescriptor} to the runtime environment.
 * 
 * @author Dan Haywood
 *
 */
public class TestImageDescriptorAtRuntime extends TestImageDescriptor {

	public TestImageDescriptorAtRuntime() {
		super(new RuntimeDomainSpecifics(), new RcpViewerProgModelDomainBuilder());
	}

}
