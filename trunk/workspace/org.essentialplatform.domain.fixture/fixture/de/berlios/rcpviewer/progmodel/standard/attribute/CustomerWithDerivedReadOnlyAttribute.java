/**
 * 
 */
package de.berlios.rcpviewer.progmodel.standard.attribute;
import de.berlios.rcpviewer.progmodel.standard.*;

@InDomain
public class CustomerWithDerivedReadOnlyAttribute {
	@Derived
	public String getSurname() {
		return "foobar";
	}
}