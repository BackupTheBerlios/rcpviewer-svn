package de.berlios.rcpviewer.metamodel.impl;

import de.berlios.rcpviewer.metamodel.annotations.*;
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
	
	private int rank;
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	
}
