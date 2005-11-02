package org.essentialplatform.progmodel.extended;

import org.essentialplatform.progmodel.standard.DescribedAs;
import org.essentialplatform.progmodel.standard.InDomain;

@InDomain
public class CustomerToTestImmutableOncePersistedAnnotation {


	public String getLastName() {
		return lastName;
	}
	private String lastName;

	public String getFirstName() {
		return firstName;
	}
	private String firstName;
	
	/**
	 * Suppose that we used an email to identify a customer: then this 
	 * would need to be immutable once persisted.
	 * 
	 * @return
	 */
	@ImmutableOncePersisted
	public String getEmail() {
		return email;
	}
	private String email;
	

	
}