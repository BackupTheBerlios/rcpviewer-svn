package de.berlios.rcpviewer.progmodel.standard.operation;
import de.berlios.rcpviewer.RuntimeDomainSpecifics;


/**
 * Binds tests in {@link TestDomainClassOperations} in a runtime 
 * environment, as well as testing runtime-specific features.
 * 
 * @author Dan Haywood
 */
public class TestDomainClassOperationsAtRuntime 
					extends TestDomainClassOperations {

	public TestDomainClassOperationsAtRuntime() {
		super(new RuntimeDomainSpecifics(), null);
	}
	
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	

}
