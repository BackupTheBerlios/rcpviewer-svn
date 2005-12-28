package org.essentialplatform.core.fixture.progmodel.essential.extended;

import org.essentialplatform.progmodel.essential.app.InDomain;
import org.essentialplatform.progmodel.essential.app.Invisible;

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