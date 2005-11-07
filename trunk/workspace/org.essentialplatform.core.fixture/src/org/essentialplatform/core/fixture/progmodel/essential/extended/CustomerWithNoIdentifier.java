/**
 * 
 */
package org.essentialplatform.core.fixture.progmodel.essential.extended;

import org.essentialplatform.progmodel.essential.app.InDomain;

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