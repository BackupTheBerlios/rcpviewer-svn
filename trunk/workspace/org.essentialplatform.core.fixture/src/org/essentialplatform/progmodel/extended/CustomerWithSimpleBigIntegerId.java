/**
 * 
 */
package org.essentialplatform.progmodel.extended;

import java.math.BigInteger;

import org.essentialplatform.progmodel.essential.app.Id;
import org.essentialplatform.progmodel.essential.app.InDomain;

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