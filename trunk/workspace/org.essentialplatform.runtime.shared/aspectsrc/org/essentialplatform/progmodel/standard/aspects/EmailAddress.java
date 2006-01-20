package org.essentialplatform.progmodel.standard.aspects;

import org.essentialplatform.progmodel.essential.app.InDomain;

@InDomain
class EmailAddress {

	private String email;
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	private String domain;
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	
	
}
