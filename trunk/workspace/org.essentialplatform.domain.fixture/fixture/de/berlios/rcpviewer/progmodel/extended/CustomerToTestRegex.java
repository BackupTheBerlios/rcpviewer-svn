package de.berlios.rcpviewer.progmodel.extended;

import de.berlios.rcpviewer.progmodel.standard.DescribedAs;
import de.berlios.rcpviewer.progmodel.standard.InDomain;

@InDomain
public class CustomerToTestRegex {


	/**
	 * Has a regex; must begin with a capital then be following by 1 or more
	 * characters.
	 * 
	 * @return
	 */
	@Regex("[A-Z].+")
	public String getLastName() {
		return lastName;
	}
	private String lastName;

	
	/**
	 * Doesn't have a regex.
	 * 
	 * @return
	 */
	public String getFirstName() {
		return firstName;
	}
	private String firstName;

	
}