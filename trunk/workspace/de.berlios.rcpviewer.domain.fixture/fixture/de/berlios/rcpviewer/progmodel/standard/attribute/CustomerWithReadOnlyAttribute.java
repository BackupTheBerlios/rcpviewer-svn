/**
 * 
 */
package de.berlios.rcpviewer.progmodel.standard.attribute;
import de.berlios.rcpviewer.progmodel.standard.*;

@InDomain
public class CustomerWithReadOnlyAttribute {
	String surname;
	public String getSurname() {
		return surname;
	}
}