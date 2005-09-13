package de.berlios.rcpviewer.progmodel.standard.attribute;

import org.eclipse.emf.ecore.EAttribute;

import de.berlios.rcpviewer.RuntimeDomainSpecifics;
import de.berlios.rcpviewer.domain.IRuntimeDomainClass;
import de.berlios.rcpviewer.domain.RuntimeDomain;

/**
 * Bind tests in {@link TestDomainClassAttributes} to the runtime environment.
 * @author Dan Haywood
 *
 */
public class TestDomainClassAttributesAtRuntime extends TestDomainClassAttributes {

	public TestDomainClassAttributesAtRuntime() {
		super(new RuntimeDomainSpecifics(), null);
	}


	
	public void testCanHandleWriteOnlyAttributes() {
		IRuntimeDomainClass<CustomerWithWriteOnlyAttribute> domainClass = 
			RuntimeDomain.instance().lookupAny(CustomerWithWriteOnlyAttribute.class);
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		
		assertNotNull(domainClass.getAccessorOrMutatorFor(eAttribute));
		
	}

}
