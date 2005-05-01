package de.berlios.rcpviewer.session;

import de.berlios.rcpviewer.progmodel.standard.Domain;
import de.berlios.rcpviewer.progmodel.standard.impl.DomainMarker;

/**
 * TODO: implementing DomainMarker is a work-around; the annotation should be enough.
 */
@Domain
public class Employee implements DomainMarker{
	
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

	/** used as title */
	public String toString() { return getFirstName() + " " + getSurname(); }

}
