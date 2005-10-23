package de.berlios.rcpviewer.progmodel.extended;

import de.berlios.rcpviewer.progmodel.standard.DescribedAs;
import de.berlios.rcpviewer.progmodel.standard.InDomain;

@InDomain
public class CustomerToTestOptionalAnnotation {

	/**
	 * A not optional (ie mandatory) attribute.
	 * 
	 * @return
	 */
	public String getFirstName() {
		return firstName;
	}
	private String firstName;

	/**
	 * An optional (ie not mandatory) attribute.
	 * @return
	 */
	@Optional
	public String getLastName() {
		return lastName;
	}
	private String lastName;
	
	
	public void placeOrder(
			@Named("a")
			@DescribedAs("Mandatory parameter 'a'")
			int a, 
			@Named("a")
			@DescribedAs("Optional parameter 'b'")
			@Optional
			int b) {
		
	}
}