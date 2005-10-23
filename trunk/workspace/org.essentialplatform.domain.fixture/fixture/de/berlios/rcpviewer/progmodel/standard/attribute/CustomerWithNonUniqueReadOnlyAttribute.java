/**
 * 
 */
package de.berlios.rcpviewer.progmodel.standard.attribute;
import de.berlios.rcpviewer.progmodel.standard.*;

@InDomain
public class CustomerWithNonUniqueReadOnlyAttribute {
	String surname;
	@UpperBoundOf(3)
	@Unique(false)
	public String getSurname() {
		return surname;
	}
}