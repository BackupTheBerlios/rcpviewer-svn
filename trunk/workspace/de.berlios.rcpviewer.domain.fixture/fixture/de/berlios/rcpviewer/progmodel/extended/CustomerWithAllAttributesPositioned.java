/**
 * 
 */
package de.berlios.rcpviewer.progmodel.extended;

import de.berlios.rcpviewer.progmodel.standard.InDomain;

@InDomain
public class CustomerWithAllAttributesPositioned {

	int numberOfOrders;
	@Order(3)
	public int getNumberOfOrders() {
		return numberOfOrders;
	}

	String firstName;
	@Order(2)
	public String getFirstName() {
		return firstName;
	}
	
	String lastName;
	@Order(1)
	public String getLastName() {
		return lastName;
	}
}