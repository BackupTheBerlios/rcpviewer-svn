package de.berlios.rcpviewer.progmodel.standard.attribute;

import de.berlios.rcpviewer.IDeploymentSpecifics;
import de.berlios.rcpviewer.RuntimeDomainSpecifics;
import de.berlios.rcpviewer.domain.IDomainBuilder;

/**
 * Binds tests in {@link TestDomainClassAttributesUniqueness} to runtime.
 * 
 * @author Dan Haywood
 *
 */
public class TestDomainClassAttributesUniquenessAtRuntime extends
		TestDomainClassAttributesUniqueness {

	public TestDomainClassAttributesUniquenessAtRuntime() {
		super(new RuntimeDomainSpecifics(), null);
	}

}
