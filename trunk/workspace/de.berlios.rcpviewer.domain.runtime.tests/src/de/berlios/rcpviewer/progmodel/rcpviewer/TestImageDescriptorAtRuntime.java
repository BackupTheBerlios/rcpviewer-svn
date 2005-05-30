package de.berlios.rcpviewer.progmodel.rcpviewer;

import de.berlios.rcpviewer.IDeploymentSpecifics;
import de.berlios.rcpviewer.RuntimeDomainSpecifics;
import de.berlios.rcpviewer.domain.IDomainAnalyzer;

/**
 * Binds the tests in {@link TestImageDescriptor} to the runtime environment.
 * 
 * @author Dan Haywood
 *
 */
public class TestImageDescriptorAtRuntime extends TestImageDescriptor {

	public TestImageDescriptorAtRuntime() {
		super(new RuntimeDomainSpecifics(), new RcpViewerExtension());
	}

}
