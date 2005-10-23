package de.berlios.rcpviewer.progmodel.standard.operation;

import de.berlios.rcpviewer.progmodel.extended.IPrerequisites;
import de.berlios.rcpviewer.progmodel.extended.Prerequisites;
import de.berlios.rcpviewer.progmodel.standard.InDomain;

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
