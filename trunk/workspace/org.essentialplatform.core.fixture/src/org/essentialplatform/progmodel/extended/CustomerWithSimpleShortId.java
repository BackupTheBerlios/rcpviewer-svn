/**
 * 
 */
package org.essentialplatform.progmodel.extended;

import org.essentialplatform.progmodel.essential.app.Id;
import org.essentialplatform.progmodel.essential.app.InDomain;

@InDomain
public class CustomerWithSimpleShortId {

	private Short id;
	@Id
	public Short getId() {
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