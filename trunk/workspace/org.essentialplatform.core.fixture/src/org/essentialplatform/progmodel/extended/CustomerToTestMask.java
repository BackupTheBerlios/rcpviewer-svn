package org.essentialplatform.progmodel.extended;

import org.essentialplatform.progmodel.essential.app.DescribedAs;
import org.essentialplatform.progmodel.essential.app.InDomain;
import org.essentialplatform.progmodel.essential.app.Mask;

@InDomain
public class CustomerToTestMask {


	/**
	 * Has a mask.
	 * 
	 * @return
	 */
	@Mask("AAAA")
	public String getLastName() {
		return lastName;
	}
	private String lastName;

	
	/**
	 * Doesn't have a mask.
	 * 
	 * @return
	 */
	public String getFirstName() {
		return firstName;
	}
	private String firstName;

	
}