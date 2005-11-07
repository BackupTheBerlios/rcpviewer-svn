package org.essentialplatform.core.fixture.progmodel.essential.extended;

import org.essentialplatform.progmodel.essential.app.BusinessKey;
import org.essentialplatform.progmodel.essential.app.DescribedAs;
import org.essentialplatform.progmodel.essential.app.InDomain;

@InDomain
public class CustomerToTestBusinessKeyAnnotation {


	/**
	 * First (most significant) part of a business key.
	 * @return
	 */
	@BusinessKey(name="name",pos=1)
	public String getLastName() {
		return lastName;
	}
	private String lastName;

	/**
	 * Second (least significant) part of a business key.
	 * 
	 * @return
	 */
	@BusinessKey(name="name",pos=2)
	public String getFirstName() {
		return firstName;
	}
	private String firstName;
	
	/**
	 * Business key with only a single component.
	 * 
	 * @return
	 */
	@BusinessKey(name="email")
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