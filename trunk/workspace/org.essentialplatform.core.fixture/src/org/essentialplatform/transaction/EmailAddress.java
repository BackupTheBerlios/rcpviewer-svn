package org.essentialplatform.transaction;


import com.sun.corba.se.spi.legacy.connection.GetEndPointInfoAgainException;

import org.essentialplatform.progmodel.essential.app.InDomain;

@InDomain
public class EmailAddress {

	public EmailAddress() {
		super();
	}

	public EmailAddress(final String email) {
		this.email = email;
	}

	private String email;
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	
	private Customer _customer;
	public Customer getCustomer() {
		return _customer;
	}
	/**
	 * Used by {@link Customer#useEmailAddress(EmailAddress)} for testing 
	 * operations that impact more than one pojo.
	 * 
	 * @return
	 */
	public void setCustomer(Customer customer) {
		this._customer = customer;
	}


}
