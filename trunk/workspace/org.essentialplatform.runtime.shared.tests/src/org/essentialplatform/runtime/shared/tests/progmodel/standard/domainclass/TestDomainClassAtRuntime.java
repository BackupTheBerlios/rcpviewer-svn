package org.essentialplatform.runtime.shared.tests.progmodel.standard.domainclass;

import org.essentialplatform.core.deployment.Binding;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.fixture.progmodel.essential.standard.domainclass.CustomerWithNoAttributes;
import org.essentialplatform.runtime.client.domain.bindings.RuntimeClientBinding;
import org.essentialplatform.runtime.shared.domain.bindings.IDomainClassRuntimeBinding;
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
		Binding.setBinding(
				new RuntimeClientBinding().initPrimaryBuilder(new EssentialProgModelRuntimeBuilder()));
	}
	
	@Override
	protected void tearDown() throws Exception {
		Binding.reset();
		super.tearDown();
	}
	
	public void testGetJavaClass() {
		IDomainClass domainClass = lookupAny(CustomerWithNoAttributes.class);
		
		IDomainClassRuntimeBinding binding = (IDomainClassRuntimeBinding)domainClass.getBinding();
		Class<CustomerWithNoAttributes> javaClass = binding.getJavaClass();
		assertSame(CustomerWithNoAttributes.class, javaClass);
	}

	
}
