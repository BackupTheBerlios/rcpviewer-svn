/**
 * 
 */
package org.essentialplatform.progmodel.extended;

import org.essentialplatform.progmodel.essential.app.Id;
import org.essentialplatform.progmodel.essential.app.InDomain;

@InDomain
public class CustomerWithCompositeId {

	private String firstName;
	@Id(2)
	public String getFirstName() {
		return firstName;
	}
	
	private String lastName;
	@Id(1)
	public String getLastName() {
		return lastName;
	}
}