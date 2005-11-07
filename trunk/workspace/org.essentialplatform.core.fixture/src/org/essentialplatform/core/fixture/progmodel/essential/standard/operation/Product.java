package org.essentialplatform.core.fixture.progmodel.essential.standard.operation;

import org.essentialplatform.progmodel.essential.app.IPrerequisites;
import org.essentialplatform.progmodel.essential.app.InDomain;
import org.essentialplatform.progmodel.essential.app.Prerequisites;

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
