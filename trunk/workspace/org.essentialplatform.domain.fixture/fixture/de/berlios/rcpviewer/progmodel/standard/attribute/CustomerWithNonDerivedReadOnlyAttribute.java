/**
 * 
 */
package de.berlios.rcpviewer.progmodel.standard.attribute;
import de.berlios.rcpviewer.progmodel.standard.*;

@InDomain
public class CustomerWithNonDerivedReadOnlyAttribute {
	String surname;
	public String getSurname() {
		return surname;
	}
}