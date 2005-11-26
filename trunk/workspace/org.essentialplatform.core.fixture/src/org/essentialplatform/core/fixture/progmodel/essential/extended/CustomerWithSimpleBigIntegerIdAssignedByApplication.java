/**
 * 
 */
package org.essentialplatform.core.fixture.progmodel.essential.extended;

import java.math.BigInteger;

import org.essentialplatform.progmodel.essential.app.AssignmentType;
import org.essentialplatform.progmodel.essential.app.Id;
import org.essentialplatform.progmodel.essential.app.InDomain;

@InDomain
public class CustomerWithSimpleBigIntegerIdAssignedByApplication {

	private BigInteger id;
	@Id(assignedBy=AssignmentType.APPLICATION)
	public BigInteger getId() {
		return id;
	}
	public void setId(BigInteger id) {
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