package de.berlios.rcpviewer.progmodel.extended;

import de.berlios.rcpviewer.progmodel.standard.DescribedAs;
import de.berlios.rcpviewer.progmodel.standard.InDomain;

@InDomain
public class CustomerToTestInvisibleAnnotation {

	/**
	 * A not invisible attribute.
	 * 
	 * @return
	 */
	public String getFirstName() {
		return firstName;
	}
	private String firstName;

	/**
	 * An invisible attribute.
	 * @return
	 */
	@Invisible
	public int getId() {
		return id;
	}
	private int id;
	
}