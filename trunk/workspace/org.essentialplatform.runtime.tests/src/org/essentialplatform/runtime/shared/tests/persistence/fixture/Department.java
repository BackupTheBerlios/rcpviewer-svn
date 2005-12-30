package org.essentialplatform.runtime.shared.tests.persistence.fixture;

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
