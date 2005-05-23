/**
 * 
 */
package de.berlios.rcpviewer.progmodel.standard.attribute;
import de.berlios.rcpviewer.progmodel.standard.*;

@InDomain
public class CustomerWithoutOrderingReadOnlyAttribute {
	String surname;
	@UpperBoundOf(3)
	@Ordered(false)
	public String getSurname() {
		return surname;
	}
}