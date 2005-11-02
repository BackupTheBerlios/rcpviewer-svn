package org.essentialplatform.progmodel.standard.domainclass;
import org.essentialplatform.RuntimeDomainSpecifics;
import org.essentialplatform.domain.IDomainClass;
import org.essentialplatform.domain.runtime.RuntimeDeployment.RuntimeClassBinding;


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
		IDomainClass domainClass = 
			(IDomainClass)lookupAny(CustomerWithNoAttributes.class);
		
		Class<?> javaClass = ((RuntimeClassBinding)domainClass.getBinding()).getJavaClass();
		assertSame(CustomerWithNoAttributes.class, javaClass);
	}

	
}
