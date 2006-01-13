package org.essentialplatform.runtime.shared.tests.remoting.packaging;

import org.essentialplatform.progmodel.essential.app.InDomain;


@InDomain
public class City  {
	private String name;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	

	/**
	 * Used as the title.
	 */
	public String toString() {
		return name;
	}

}
