package org.essentialplatform.runtime.shared.tests.domain.handle;

import org.essentialplatform.progmodel.essential.app.InDomain;
@InDomain
public class Employee {
	
	private String firstName;
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	private String surname;
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}

}
