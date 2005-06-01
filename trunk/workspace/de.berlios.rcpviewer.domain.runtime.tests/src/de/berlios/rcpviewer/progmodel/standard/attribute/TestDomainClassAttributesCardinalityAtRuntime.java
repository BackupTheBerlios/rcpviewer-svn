package de.berlios.rcpviewer.progmodel.standard.attribute;

import de.berlios.rcpviewer.IDeploymentSpecifics;
import de.berlios.rcpviewer.RuntimeDomainSpecifics;
import de.berlios.rcpviewer.domain.IDomainBuilder;

/**
 * Binds tests in {@link TestDomainClassAttributesCardinality} to runtime.
 * 
 * @author Dan Haywood
 *
 */
public class TestDomainClassAttributesCardinalityAtRuntime extends
		TestDomainClassAttributesCardinality {

	public TestDomainClassAttributesCardinalityAtRuntime() {
		super(new RuntimeDomainSpecifics(), null);
	}

}
