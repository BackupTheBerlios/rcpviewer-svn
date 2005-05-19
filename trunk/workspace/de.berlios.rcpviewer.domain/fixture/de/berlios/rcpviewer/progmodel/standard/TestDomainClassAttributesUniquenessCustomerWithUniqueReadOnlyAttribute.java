/**
 * 
 */
package de.berlios.rcpviewer.progmodel.standard;

@InDomain
public class TestDomainClassAttributesUniquenessCustomerWithUniqueReadOnlyAttribute {
	String surname;
	@UpperBoundOf(3)
	@Unique
	public String getSurname() {
		return surname;
	}
}