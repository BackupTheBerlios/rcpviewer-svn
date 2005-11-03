package org.essentialplatform.runtime.tests.progmodel.standard.attribute;

import org.eclipse.emf.ecore.EAttribute;
import org.essentialplatform.core.deployment.Deployment;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.progmodel.standard.attribute.CustomerWithWriteOnlyAttribute;
import org.essentialplatform.progmodel.standard.attribute.TestDomainClassAttributes;
import org.essentialplatform.runtime.domain.runtime.RuntimeDeployment;
import org.essentialplatform.runtime.domain.runtime.RuntimeDeployment.RuntimeAttributeBinding;

/**
 * Bind tests in {@link TestDomainClassAttributes} to the runtime environment.
 * @author Dan Haywood
 *
 */
public class TestDomainClassAttributesAtRuntime extends TestDomainClassAttributes {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		new RuntimeDeployment();
	}
	
	@Override
	protected void tearDown() throws Exception {
		Deployment.reset();
		super.tearDown();
	}

	
	/**
	 * This test is now defunct since the RuntimeAttributeBinding which replaces
	 * this functionality no longer exposes whether there is an accessor or a 
	 * mutator (know-how-to instead of know-what). 
	 */
	public void incompletetestCanHandleWriteOnlyAttributes() {
		IDomainClass domainClass = lookupAny(CustomerWithWriteOnlyAttribute.class);
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		// assertNotNull(domainClass.getAccessorOrMutatorFor(eAttribute));
		
		
		RuntimeAttributeBinding binding = (RuntimeAttributeBinding)domainClass.getIAttribute(eAttribute).getBinding();
		// we can't assert anything here really (except perhaps demonstrate 
		// can invoke a mutator but no accessor)?
		
	}

}