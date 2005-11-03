/**
 * 
 */
package org.essentialplatform.progmodel.extended;

import java.math.BigInteger;

import org.essentialplatform.progmodel.standard.InDomain;

@InDomain
public class CustomerWithSimpleBigIntegerIdAssignedByApplication {

	private BigInteger id;
	@Id(assignedBy=AssignmentType.APPLICATION)
	public BigInteger getId() {
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