/**
 * 
 */
package de.berlios.rcpviewer.progmodel.extended;

import de.berlios.rcpviewer.progmodel.standard.InDomain;

@InDomain
public class CustomerWithCompositeId {

	private String firstName;
	@Id(2)
	public String getFirstName() {
		return firstName;
	}
	
	private String lastName;
	@Id(1)
	public String getLastName() {
		return lastName;
	}
}