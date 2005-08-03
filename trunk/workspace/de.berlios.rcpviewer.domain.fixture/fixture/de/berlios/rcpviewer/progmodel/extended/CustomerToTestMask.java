package de.berlios.rcpviewer.progmodel.extended;

import de.berlios.rcpviewer.progmodel.standard.DescribedAs;
import de.berlios.rcpviewer.progmodel.standard.InDomain;

@InDomain
public class CustomerToTestMask {


	@Mask("AAAA")
	public String getLastName() {
		return lastName;
	}
	private String lastName;

	
}