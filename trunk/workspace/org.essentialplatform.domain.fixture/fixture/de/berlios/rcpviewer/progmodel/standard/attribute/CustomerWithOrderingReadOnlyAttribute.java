/**
 * 
 */
package de.berlios.rcpviewer.progmodel.standard.attribute;
import de.berlios.rcpviewer.progmodel.standard.*;

@InDomain
public class CustomerWithOrderingReadOnlyAttribute {
	String surname;
	@UpperBoundOf(3)
	@Ordered
	public String getSurname() {
		return surname;
	}
}