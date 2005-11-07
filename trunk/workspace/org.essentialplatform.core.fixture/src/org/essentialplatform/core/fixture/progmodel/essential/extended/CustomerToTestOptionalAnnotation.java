package org.essentialplatform.core.fixture.progmodel.essential.extended;

import org.essentialplatform.progmodel.essential.app.DescribedAs;
import org.essentialplatform.progmodel.essential.app.InDomain;
import org.essentialplatform.progmodel.essential.app.Named;
import org.essentialplatform.progmodel.essential.app.Optional;

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