package org.essentialplatform.progmodel.extended;

import org.essentialplatform.progmodel.standard.DescribedAs;
import org.essentialplatform.progmodel.standard.InDomain;

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