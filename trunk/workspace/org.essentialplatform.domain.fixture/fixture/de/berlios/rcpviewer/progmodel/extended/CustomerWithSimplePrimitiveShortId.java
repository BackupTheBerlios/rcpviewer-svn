/**
 * 
 */
package de.berlios.rcpviewer.progmodel.extended;

import de.berlios.rcpviewer.progmodel.standard.InDomain;

@InDomain
public class CustomerWithSimplePrimitiveShortId {

	private short id;
	@Id
	public short getId() {
		return id;
	}
	
	String firstName;
	public String getFirstName() {
		return firstName;
	}
	
	String lastName;
	public String getLastName() {
		return lastName;
	}
}