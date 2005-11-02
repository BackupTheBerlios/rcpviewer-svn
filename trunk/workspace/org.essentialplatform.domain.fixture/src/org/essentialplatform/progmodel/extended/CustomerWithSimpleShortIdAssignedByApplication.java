/**
 * 
 */
package org.essentialplatform.progmodel.extended;

import org.essentialplatform.progmodel.standard.InDomain;

@InDomain
public class CustomerWithSimpleShortIdAssignedByApplication {

	private Short id;
	@Id(assignedBy=AssignmentType.APPLICATION)
	public Short getId() {
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