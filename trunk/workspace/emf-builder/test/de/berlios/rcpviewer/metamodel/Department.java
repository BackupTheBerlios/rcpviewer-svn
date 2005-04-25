package de.berlios.rcpviewer.metamodel;

import de.berlios.rcpviewer.metamodel.annotations.*;


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
