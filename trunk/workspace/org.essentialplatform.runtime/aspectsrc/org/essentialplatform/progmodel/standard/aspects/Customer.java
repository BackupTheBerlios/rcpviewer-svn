package org.essentialplatform.progmodel.standard.aspects;

import java.util.ArrayList;
import java.util.List;

import org.essentialplatform.progmodel.essential.app.InDomain;
import org.essentialplatform.progmodel.essential.app.TypeOf;

@InDomain
class Customer {

	public Customer() {
	}
	
	
	private String firstName;
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	private EmailAddress preferredEmailAddress;
	public EmailAddress getPreferredEmailAddress() {
		return preferredEmailAddress;
	}
	public void setPreferredEmailAddress(EmailAddress emailAddress) {
		this.preferredEmailAddress = emailAddress;
	}
	public void associatePreferredEmailAddress(EmailAddress emailAddress) {
		setPreferredEmailAddress(emailAddress);
	}
	public void dissociatePreferredEmailAddress(EmailAddress emailAddress) {
		setPreferredEmailAddress(null);
	}
	
	private List<EmailAddress> emailAddresses = new ArrayList<EmailAddress>();
	@TypeOf(EmailAddress.class)
	public List<EmailAddress> getEmailAddresses() {
		return emailAddresses;
	}
	public void setEmailAddresses(List<EmailAddress> emailAddresses) {
		this.emailAddresses = emailAddresses;
	}
	public void addToEmailAddresses(EmailAddress emailAddress) {
		emailAddresses.add(emailAddress);
	}
	public void removeFromEmailAddresses(EmailAddress emailAddress) {
		getEmailAddresses().remove(emailAddress);
	}
	
	
	public void placeOrder() {}

	
	
}
