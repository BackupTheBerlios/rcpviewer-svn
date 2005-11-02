/**
 * 
 */
package org.essentialplatform.progmodel.extended;

import org.essentialplatform.progmodel.standard.InDomain;

@InDomain
public class CustomerWithSimpleByteIdAssignedByApplication {

	private Byte id;
	@Id(assignedBy=AssignmentType.APPLICATION)
	public Byte getId() {
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