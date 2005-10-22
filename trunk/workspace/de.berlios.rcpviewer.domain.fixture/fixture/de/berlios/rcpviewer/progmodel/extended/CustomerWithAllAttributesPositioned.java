/**
 * 
 */
package de.berlios.rcpviewer.progmodel.extended;

import de.berlios.rcpviewer.progmodel.standard.InDomain;

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