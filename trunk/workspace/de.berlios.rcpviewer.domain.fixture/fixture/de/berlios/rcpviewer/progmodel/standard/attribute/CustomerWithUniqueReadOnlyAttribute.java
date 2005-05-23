/**
 * 
 */
package de.berlios.rcpviewer.progmodel.standard.attribute;
import de.berlios.rcpviewer.progmodel.standard.*;

@InDomain
public class CustomerWithUniqueReadOnlyAttribute {
	String surname;
	@UpperBoundOf(3)
	@Unique
	public String getSurname() {
		return surname;
	}
}