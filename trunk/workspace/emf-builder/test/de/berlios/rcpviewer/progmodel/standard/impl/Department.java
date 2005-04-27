package de.berlios.rcpviewer.progmodel.standard.impl;

import de.berlios.rcpviewer.progmodel.standard.Domain;
import de.berlios.rcpviewer.progmodel.standard.impl.DomainMarker;


/**
 * TODO: implementing DomainMarker is a work-around; the annotation should be enough.
 */
@Domain
public class Department implements DomainMarker {
	private String name;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Expect the programming model aspect to intercept.
	 *
	 */
	public void save() {
		
	}
	
	/**
	 * Used as the title.
	 */
	public String toString() {
		return name;
	}
}