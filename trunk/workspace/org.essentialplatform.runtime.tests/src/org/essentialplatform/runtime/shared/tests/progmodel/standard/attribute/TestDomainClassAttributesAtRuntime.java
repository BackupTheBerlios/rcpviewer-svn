package org.essentialplatform.runtime.shared.tests.progmodel.standard.attribute;

import org.eclipse.emf.ecore.EAttribute;
import org.essentialplatform.core.deployment.Binding;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.fixture.progmodel.essential.standard.attribute.CustomerWithWriteOnlyAttribute;
import org.essentialplatform.runtime.shared.RuntimeBinding;
import org.essentialplatform.runtime.shared.RuntimeBinding.RuntimeAttributeBinding;
import org.essentialplatform.progmodel.essential.core.tests.TestDomainClassAttributes;
import org.essentialplatform.progmodel.essential.runtime.EssentialProgModelRuntimeBuilder;

/**
 * Bind tests in {@link TestDomainClassAttributes} to the runtime environment.
 * @author Dan Haywood
 *
 */
public class TestDomainClassAttributesAtRuntime extends TestDomainClassAttributes {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		Binding.setBinding(
			new RuntimeBinding(new EssentialProgModelRuntimeBuilder()));
	}
	
	@Override
	protected void tearDown() throws Exception {
		Binding.reset();
		super.tearDown();
	}

	
	/**
	 * This test is now defunct since the RuntimeAttributeBinding which replaces
	 * this functionality no longer exposes whether there is an accessor or a 
	 * mutator (know-how-to instead of know-what). 
	 */
	public void incompletetestCanHandleWriteOnlyAttributes() {
		IDomainClass domainClass = lookupAny(CustomerWithWriteOnlyAttribute.class);
		IDomainClass.IAttribute iAttribute = domainClass.getIAttributeNamed("surname");
		// assertNotNull(domainClass.getAccessorOrMutatorFor(eAttribute));
		
		
		RuntimeAttributeBinding binding = (RuntimeAttributeBinding)iAttribute.getBinding();
		// we can't assert anything here really (except perhaps demonstrate 
		// can invoke a mutator but no accessor)?
		
	}

}
