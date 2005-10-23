/**
 * 
 */
package de.berlios.rcpviewer.progmodel.standard.attribute;
import de.berlios.rcpviewer.progmodel.standard.*;

@InDomain
public class CustomerWithWriteOnlyAttribute {
	private String surname;
	public void setSurname(String surname) {
		this.surname = surname;
	}
}