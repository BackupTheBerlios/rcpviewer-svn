package org.essentialplatform.progmodel.extended;

import org.essentialplatform.progmodel.standard.DescribedAs;
import org.essentialplatform.progmodel.standard.InDomain;

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