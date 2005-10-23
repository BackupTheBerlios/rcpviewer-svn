/**
 * 
 */
package de.berlios.rcpviewer.progmodel.extended;

import java.math.BigInteger;

import de.berlios.rcpviewer.progmodel.standard.InDomain;

@InDomain
public class CustomerWithSimpleBigIntegerId {

	private BigInteger id;
	@Id
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