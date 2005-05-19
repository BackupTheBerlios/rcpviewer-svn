/**
 * 
 */
package de.berlios.rcpviewer.progmodel.extended;

import de.berlios.rcpviewer.progmodel.standard.InDomain;

@InDomain
public class CustomerWithAllAttributesPositioned {

	int numberOfOrders;
	@PositionedAt(3)
	public int getNumberOfOrders() {
		return numberOfOrders;
	}

	String firstName;
	@PositionedAt(2)
	public String getFirstName() {
		return firstName;
	}
	
	String lastName;
	@PositionedAt(1)
	public String getLastName() {
		return lastName;
	}
}