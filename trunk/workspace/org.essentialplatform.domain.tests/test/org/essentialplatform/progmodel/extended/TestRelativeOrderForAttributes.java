package org.essentialplatform.progmodel.extended;

import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.essentialplatform.AbstractTestCase;
import org.essentialplatform.domain.IDomainBuilder;
import org.essentialplatform.domain.IDomainClass;

/**
 * Tests for the use of the <tt>@RelativeOrder</tt> annotation.
 * 
 * @author Dan Haywood
 */
public abstract class TestRelativeOrderForAttributes extends AbstractTestCase {

	private IDomainClass domainClass;
	
	public void testDomainClassWithAllAttributesPositioned() {
		domainClass = 
			lookupAny(CustomerWithAllAttributesPositioned.class);
		getDomainInstance().addBuilder(getDomainBuilder());
		getDomainInstance().done();
		
		List<EAttribute> orderedAttributes = 
			domainClass.orderedAttributes();
		assertEquals("lastName", orderedAttributes.get(0).getName());
		assertEquals("firstName", orderedAttributes.get(1).getName());
		assertEquals("numberOfOrders", orderedAttributes.get(2).getName());
	}

	
}
