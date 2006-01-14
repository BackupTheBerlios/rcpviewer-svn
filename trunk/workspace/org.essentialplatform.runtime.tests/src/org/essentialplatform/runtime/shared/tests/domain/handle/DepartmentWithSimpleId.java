package org.essentialplatform.runtime.shared.tests.domain.handle;

import org.essentialplatform.progmodel.essential.app.Id;
import org.essentialplatform.progmodel.essential.app.InDomain;


@InDomain
public class DepartmentWithSimpleId  {

	
	private String name;
	/**
	 * Assignment will be dependent upon context, ie 
	 * @return
	 */
	@Id
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
