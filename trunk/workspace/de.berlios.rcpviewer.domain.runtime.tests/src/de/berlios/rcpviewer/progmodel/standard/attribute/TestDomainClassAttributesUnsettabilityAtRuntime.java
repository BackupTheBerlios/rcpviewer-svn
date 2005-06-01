package de.berlios.rcpviewer.progmodel.standard.attribute;

import de.berlios.rcpviewer.IDeploymentSpecifics;
import de.berlios.rcpviewer.RuntimeDomainSpecifics;
import de.berlios.rcpviewer.domain.IDomainBuilder;

/**
 * Binds tests in {@link TestDomainClassAttributesUnsettability} to runtime.
 * 
 * @author Dan Haywood
 *
 */
public class TestDomainClassAttributesUnsettabilityAtRuntime extends
		TestDomainClassAttributesUnsettability {

	public TestDomainClassAttributesUnsettabilityAtRuntime() {
		super(new RuntimeDomainSpecifics(), null);
	}

}
