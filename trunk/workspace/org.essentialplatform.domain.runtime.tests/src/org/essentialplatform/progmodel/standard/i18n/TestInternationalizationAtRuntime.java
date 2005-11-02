package org.essentialplatform.progmodel.standard.i18n;
import org.essentialplatform.RuntimeDomainSpecifics;


/**
 * Binds tests in {@link TestInternationalization} in a runtime environment.
 * 
 * @author Dan Haywood
 */
public class TestInternationalizationAtRuntime extends TestInternationalization {

	public TestInternationalizationAtRuntime() {
		super(new RuntimeDomainSpecifics(), null);
	}
	
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	
}
