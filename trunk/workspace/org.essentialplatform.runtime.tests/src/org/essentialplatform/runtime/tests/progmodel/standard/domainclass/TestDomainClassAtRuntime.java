package org.essentialplatform.runtime.tests.progmodel.standard.domainclass;

import org.essentialplatform.core.deployment.Deployment;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.fixture.progmodel.essential.standard.domainclass.CustomerWithNoAttributes;
import org.essentialplatform.runtime.RuntimeDeployment;
import org.essentialplatform.runtime.RuntimeDeployment.RuntimeClassBinding;
import org.essentialplatform.progmodel.essential.core.tests.TestDomainClass;
import org.essentialplatform.progmodel.essential.runtime.EssentialProgModelRuntimeBuilder;


/**
 * Binds tests in {@link TestDomainClass} in a runtime environment and also
 * tests runtime-specific features.
 * 
 * @author Dan Haywood
 */
public class TestDomainClassAtRuntime extends TestDomainClass {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		new RuntimeDeployment(new EssentialProgModelRuntimeBuilder());
	}
	
	@Override
	protected void tearDown() throws Exception {
		Deployment.reset();
		super.tearDown();
	}
	
	public void testGetJavaClass() {
		IDomainClass domainClass = lookupAny(CustomerWithNoAttributes.class);
		
		RuntimeClassBinding binding = (RuntimeClassBinding)domainClass.getBinding();
		Class<CustomerWithNoAttributes> javaClass = binding.getJavaClass();
		assertSame(CustomerWithNoAttributes.class, javaClass);
	}

	
}
