/**
 * 
 */
package de.berlios.rcpviewer.progmodel.extended;

import de.berlios.rcpviewer.progmodel.standard.InDomain;

@InDomain
public class CustomerWithNoIdentifier {

	private String firstName;
	public String getFirstName() {
		return firstName;
	}
	
	private String lastName;
	public String getLastName() {
		return lastName;
	}
}