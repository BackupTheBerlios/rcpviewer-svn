package de.berlios.rcpviewer.progmodel.standard.attribute;

import de.berlios.rcpviewer.IDeploymentSpecifics;
import de.berlios.rcpviewer.RuntimeDomainSpecifics;
import de.berlios.rcpviewer.domain.IDomainAnalyzer;

/**
 * Binds tests in {@link TestDomainClassAttributesOrdering} to runtime.
 * 
 * @author Dan Haywood
 *
 */
public class TestDomainClassAttributesOrderingAtRuntime extends
		TestDomainClassAttributesOrdering {

	public TestDomainClassAttributesOrderingAtRuntime() {
		super(new RuntimeDomainSpecifics(), null);
	}

}
