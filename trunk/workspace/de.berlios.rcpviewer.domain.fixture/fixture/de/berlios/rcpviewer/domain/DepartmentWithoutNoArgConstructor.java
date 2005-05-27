package de.berlios.rcpviewer.domain;

import de.berlios.rcpviewer.progmodel.standard.InDomain;

@InDomain
public class DepartmentWithoutNoArgConstructor {
	
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
