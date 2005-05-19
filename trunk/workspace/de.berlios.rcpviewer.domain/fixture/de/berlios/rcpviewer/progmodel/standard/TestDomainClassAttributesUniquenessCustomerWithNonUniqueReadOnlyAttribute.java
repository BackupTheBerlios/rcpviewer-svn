/**
 * 
 */
package de.berlios.rcpviewer.progmodel.standard;

@InDomain
public class TestDomainClassAttributesUniquenessCustomerWithNonUniqueReadOnlyAttribute {
	String surname;
	@UpperBoundOf(3)
	@Unique(false)
	public String getSurname() {
		return surname;
	}
}