package org.essentialplatform.core.fixture.progmodel.essential.extended;

import org.essentialplatform.progmodel.essential.app.InDomain;
import org.essentialplatform.progmodel.essential.app.RelativeOrder;

@InDomain
public class CustomerWithAllAttributesPositioned {

	int numberOfOrders;
	@RelativeOrder(3)
	public int getNumberOfOrders() {
		return numberOfOrders;
	}

	String firstName;
	@RelativeOrder(2)
	public String getFirstName() {
		return firstName;
	}
	
	String lastName;
	@RelativeOrder(1)
	public String getLastName() {
		return lastName;
	}
}