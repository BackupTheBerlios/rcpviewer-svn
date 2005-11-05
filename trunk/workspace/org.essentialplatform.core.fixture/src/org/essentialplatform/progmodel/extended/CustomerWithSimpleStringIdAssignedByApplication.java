/**
 * 
 */
package org.essentialplatform.progmodel.extended;

import org.essentialplatform.progmodel.essential.app.AssignmentType;
import org.essentialplatform.progmodel.essential.app.Id;
import org.essentialplatform.progmodel.essential.app.InDomain;

@InDomain
public class CustomerWithSimpleStringIdAssignedByApplication {

	private String id;
	@Id(assignedBy=AssignmentType.APPLICATION)
	public String getId() {
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