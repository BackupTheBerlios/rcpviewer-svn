package org.essentialplatform.progmodel.essential.core.tests;

import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.domain.builders.IDomainBuilder;
import org.essentialplatform.core.fixture.progmodel.essential.extended.CustomerWithAllAttributesPositioned;
import org.essentialplatform.core.tests.AbstractTestCase;
import org.essentialplatform.progmodel.essential.core.domain.comparators.RelativeOrderAttributeComparator;

/**
 * Tests for the use of the <tt>@RelativeOrder</tt> annotation.
 * 
 * @author Dan Haywood
 */
public abstract class TestRelativeOrderForAttributes extends AbstractTestCase {

	private IDomainClass domainClass;
	
	public void testDomainClassWithAllAttributesPositioned() {
		domainClass = lookupAny(CustomerWithAllAttributesPositioned.class);
		
		List<IDomainClass.IAttribute> orderedAttributes = 
			domainClass.iAttributes(new RelativeOrderAttributeComparator());
		assertEquals("lastName", orderedAttributes.get(0).getName());
		assertEquals("firstName", orderedAttributes.get(1).getName());
		assertEquals("numberOfOrders", orderedAttributes.get(2).getName());
	}

	
}
