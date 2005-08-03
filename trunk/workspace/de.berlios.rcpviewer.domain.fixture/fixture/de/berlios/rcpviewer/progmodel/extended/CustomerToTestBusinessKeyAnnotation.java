package de.berlios.rcpviewer.progmodel.extended;

import de.berlios.rcpviewer.progmodel.standard.DescribedAs;
import de.berlios.rcpviewer.progmodel.standard.InDomain;

@InDomain
public class CustomerToTestBusinessKeyAnnotation {


	/**
	 * First (most significant) part of a business key.
	 * @return
	 */
	@BusinessKey("name.1")
	public String getLastName() {
		return lastName;
	}
	private String lastName;

	/**
	 * Second (least significant) part of a business key.
	 * 
	 * @return
	 */
	@BusinessKey("name.2")
	public String getFirstName() {
		return firstName;
	}
	private String firstName;
	
	/**
	 * Business key with only a single component.
	 * 
	 * @return
	 */
	@BusinessKey("email")
	public String getEmail() {
		return email;
	}
	private String email;
	

	/**
	 * Not in any business key.
	 * 
	 * @return
	 */
	public String getFavoriteColour() {
		return favouriteColour;
	}
	private String favouriteColour;
	
	
}