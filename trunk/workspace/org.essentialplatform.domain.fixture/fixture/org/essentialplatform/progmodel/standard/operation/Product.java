package org.essentialplatform.progmodel.standard.operation;

import org.essentialplatform.progmodel.extended.IPrerequisites;
import org.essentialplatform.progmodel.extended.Prerequisites;
import org.essentialplatform.progmodel.standard.InDomain;

@InDomain
public class Product {


	private String _name;
	public String getName() {
		return _name;
	}
	public void setName(String name) {
		_name = name;
	}

}
