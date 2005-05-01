package de.berlios.rcpviewer.metamodel;

import de.berlios.rcpviewer.progmodel.standard.Domain;
import de.berlios.rcpviewer.progmodel.standard.impl.DomainMarker;

/**
 * TODO: implementing DomainMarker is a work-around; the annotation should be enough.
 */
@Domain
public class DepartmentWithoutNoArgConstructor implements DomainMarker {
	
	public DepartmentWithoutNoArgConstructor(final String name) {
		this.name = name;
	}
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
