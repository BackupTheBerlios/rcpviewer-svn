/**
 * 
 */
package de.berlios.rcpviewer.progmodel.standard.attribute;
import de.berlios.rcpviewer.progmodel.standard.*;

@InDomain
public class CustomerWithNoUniquenessReadOnlyAttribute {
	String surname;
	public String getSurname() {
		return surname;
	}
}