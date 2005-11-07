package org.essentialplatform.core.fixture.progmodel.essential.standard.impl;

import org.essentialplatform.progmodel.essential.app.InDomain;

@InDomain
public class Department {
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
	 * public to allow value to be read during testing.
	 */
	public boolean movedOffice = false;
	/**
	 * A void operation to be tested.
	 */
	public void moveOffice() {
		movedOffice = true;
	}

	/**
	 * Used as the title.
	 */
	public String toString() {
		return name;
	}
}
