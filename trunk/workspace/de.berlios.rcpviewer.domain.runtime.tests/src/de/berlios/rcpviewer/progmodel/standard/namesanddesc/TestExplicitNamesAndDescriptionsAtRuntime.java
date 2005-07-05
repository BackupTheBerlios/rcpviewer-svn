package de.berlios.rcpviewer.progmodel.standard.namesanddesc;
import de.berlios.rcpviewer.RuntimeDomainSpecifics;


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
