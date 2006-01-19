package org.essentialplatform.runtime.shared.tests.remoting.packaging;

import org.essentialplatform.progmodel.essential.app.InDomain;


@InDomain
public class DepartmentAttributesOnly  {
	
	private String name;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	private int rank;
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	

	/**
	 * Used as the title.
	 */
	public String toString() {
		return name;
	}

}
