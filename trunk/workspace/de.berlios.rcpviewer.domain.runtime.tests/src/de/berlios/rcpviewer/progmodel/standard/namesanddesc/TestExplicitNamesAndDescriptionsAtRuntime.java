package de.berlios.rcpviewer.progmodel.standard.namesanddesc;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;

import de.berlios.rcpviewer.AbstractTestCase;
import de.berlios.rcpviewer.IDeploymentSpecifics;
import de.berlios.rcpviewer.RuntimeDomainSpecifics;
import de.berlios.rcpviewer.domain.Domain;
import de.berlios.rcpviewer.domain.IDomainBuilder;
import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.domain.IRuntimeDomainClass;
import de.berlios.rcpviewer.progmodel.standard.CustomerExplicitlyInDefaultDomain;
import de.berlios.rcpviewer.progmodel.standard.CustomerImplicitlyInDefaultDomain;


/**
 * Binds tests in {@link TestExplicitNamesAndDescriptions} in a runtime 
 * environment.
 * 
 * @author Dan Haywood
 */
public class TestExplicitNamesAndDescriptionsAtRuntime 
	extends TestExplicitNamesAndDescriptions {

	public TestExplicitNamesAndDescriptionsAtRuntime() {
		super(new RuntimeDomainSpecifics(), null);
	}
	
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	
}
