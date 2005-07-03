package de.berlios.rcpviewer.progmodel.standard.attribute;

import org.eclipse.emf.ecore.EAttribute;

import de.berlios.rcpviewer.AbstractTestCase;
import de.berlios.rcpviewer.IDeploymentSpecifics;
import de.berlios.rcpviewer.domain.RuntimeDomain;
import de.berlios.rcpviewer.domain.IDomainBuilder;
import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.progmodel.standard.attribute.CustomerWithNoOrderingReadOnlyAttribute;
import de.berlios.rcpviewer.progmodel.standard.attribute.CustomerWithOrderingReadOnlyAttribute;
import de.berlios.rcpviewer.progmodel.standard.attribute.CustomerWithoutOrderingReadOnlyAttribute;

public abstract class TestDomainClassAttributesOrdering extends AbstractTestCase {

	public TestDomainClassAttributesOrdering(IDeploymentSpecifics domainSpecifics, IDomainBuilder domainAnalyzer) {
		super(domainSpecifics, domainAnalyzer);
	}

	private IDomainClass<?> domainClass;
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testOrderingOfEAttributeWhenNoneSpecified() {
		domainClass = lookupAny(CustomerWithNoOrderingReadOnlyAttribute.class);
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertTrue(eAttribute.isOrdered());
		assertTrue(domainClass.isOrdered(eAttribute));
	}

	public void testOrderingOfEAttributeWhenSpecifiedAsTrue() {
		domainClass = lookupAny(CustomerWithOrderingReadOnlyAttribute.class);
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertTrue(eAttribute.isOrdered());
		assertTrue(domainClass.isOrdered(eAttribute));
	}

	public void testOrderingOfEAttributeWhenSpecifiedAsFalse() {
		domainClass = lookupAny(CustomerWithoutOrderingReadOnlyAttribute.class);
		EAttribute eAttribute = domainClass.getEAttributeNamed("surname");
		assertFalse(eAttribute.isOrdered());
		assertFalse(domainClass.isOrdered(eAttribute));
	}

	public void incompletetestOrderingOfEAttributeWhenSpecifiedWithoutUpperBound() {
		// TODO
	}


}
