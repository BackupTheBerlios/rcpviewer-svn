package org.essentialplatform.persistence;

import org.essentialplatform.progmodel.standard.InDomain;


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