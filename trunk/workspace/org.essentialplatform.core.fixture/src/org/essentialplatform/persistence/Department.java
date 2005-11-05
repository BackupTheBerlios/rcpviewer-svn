package org.essentialplatform.persistence;

import org.essentialplatform.progmodel.essential.app.InDomain;


@InDomain
public class Department  {
	private String name;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
