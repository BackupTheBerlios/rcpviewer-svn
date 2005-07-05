package de.berlios.rcpviewer.progmodel.standard.domainclass;
import de.berlios.rcpviewer.RuntimeDomainSpecifics;
import de.berlios.rcpviewer.domain.IRuntimeDomainClass;


/**
 * Binds tests in {@link TestDomainClass} in a runtime environment and also
 * tests runtime-specific features.
 * 
 * @author Dan Haywood
 */
public class TestDomainClassAtRuntime extends TestDomainClass {

	public TestDomainClassAtRuntime() {
		super(new RuntimeDomainSpecifics(), null);
	}
	
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testGetJavaClass() {
		IRuntimeDomainClass<?> domainClass = 
			(IRuntimeDomainClass<?>)lookupAny(CustomerWithNoAttributes.class);
		
		assertSame(CustomerWithNoAttributes.class, domainClass.getJavaClass());
	}

	
}
