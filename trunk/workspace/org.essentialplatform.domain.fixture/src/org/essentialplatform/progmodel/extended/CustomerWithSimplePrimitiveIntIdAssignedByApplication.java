/**
 * 
 */
package org.essentialplatform.progmodel.extended;

import org.essentialplatform.progmodel.standard.InDomain;

@InDomain
public class CustomerWithSimplePrimitiveIntIdAssignedByApplication {

	private int id;
	@Id(assignedBy=AssignmentType.APPLICATION)
	public int getId() {
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