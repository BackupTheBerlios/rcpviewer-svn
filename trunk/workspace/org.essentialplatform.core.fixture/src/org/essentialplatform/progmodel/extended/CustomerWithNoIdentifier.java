/**
 * 
 */
package org.essentialplatform.progmodel.extended;

import org.essentialplatform.progmodel.standard.InDomain;

@InDomain
public class CustomerWithNoIdentifier {

	private String firstName;
	public String getFirstName() {
		return firstName;
	}
	
	private String lastName;
	public String getLastName() {
		return lastName;
	}
}