package de.berlios.rcpviewer.progmodel.standard.reference;
import de.berlios.rcpviewer.RuntimeDomainSpecifics;


/**
 * Binds tests in {@link TestDomainClassReferences} in a runtime 
 * environment, as well as testing runtime-specific features.
 * 
 * @author Dan Haywood
 */
public class TestDomainClassReferencesAtRuntime 
					extends TestDomainClassReferences {

	public TestDomainClassReferencesAtRuntime() {
		super(new RuntimeDomainSpecifics(), null);
	}
	
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	

}
