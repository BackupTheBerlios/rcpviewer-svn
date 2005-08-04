package de.berlios.rcpviewer.progmodel.standard.aspects;

import de.berlios.rcpviewer.progmodel.standard.InDomain;

@InDomain
class Customer {

	private String firstName;
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public void placeOrder() {}
	
	
}
