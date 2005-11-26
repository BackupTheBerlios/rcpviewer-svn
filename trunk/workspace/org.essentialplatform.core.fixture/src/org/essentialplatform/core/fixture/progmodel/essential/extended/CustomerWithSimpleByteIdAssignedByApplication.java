/**
 * 
 */
package org.essentialplatform.core.fixture.progmodel.essential.extended;

import org.essentialplatform.progmodel.essential.app.AssignmentType;
import org.essentialplatform.progmodel.essential.app.Id;
import org.essentialplatform.progmodel.essential.app.InDomain;

@InDomain
public class CustomerWithSimpleByteIdAssignedByApplication {

	private Byte id;
	@Id(assignedBy=AssignmentType.APPLICATION)
	public Byte getId() {
		return id;
	}
	public void setId(Byte id) {
		this.id = id;
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