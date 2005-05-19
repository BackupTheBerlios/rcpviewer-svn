package de.berlios.rcpviewer.progmodel.standard;

import de.berlios.rcpviewer.progmodel.standard.impl.DomainMarker;

/**
 * TODO: DomainMarker is workaround
 */
@InDomain
public class TestDomainClassReferencesName implements DomainMarker {
	public TestDomainClassReferencesName(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}
	String firstName;
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	String lastName;
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
}