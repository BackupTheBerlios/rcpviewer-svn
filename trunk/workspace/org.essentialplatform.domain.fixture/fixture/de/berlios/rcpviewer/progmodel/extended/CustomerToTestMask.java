package de.berlios.rcpviewer.progmodel.extended;

import de.berlios.rcpviewer.progmodel.standard.DescribedAs;
import de.berlios.rcpviewer.progmodel.standard.InDomain;

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