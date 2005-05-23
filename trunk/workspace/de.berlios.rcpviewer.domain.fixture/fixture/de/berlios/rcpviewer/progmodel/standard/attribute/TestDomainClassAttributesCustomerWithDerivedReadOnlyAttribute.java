/**
 * 
 */
package de.berlios.rcpviewer.progmodel.standard.attribute;
import de.berlios.rcpviewer.progmodel.standard.*;

@InDomain
public class TestDomainClassAttributesCustomerWithDerivedReadOnlyAttribute {
	@Derived
	public String getSurname() {
		return "foobar";
	}
}