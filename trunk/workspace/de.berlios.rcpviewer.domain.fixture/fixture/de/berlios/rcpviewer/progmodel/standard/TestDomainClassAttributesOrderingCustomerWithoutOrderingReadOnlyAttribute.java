/**
 * 
 */
package de.berlios.rcpviewer.progmodel.standard;

@InDomain
public class TestDomainClassAttributesOrderingCustomerWithoutOrderingReadOnlyAttribute {
	String surname;
	@UpperBoundOf(3)
	@Ordered(false)
	public String getSurname() {
		return surname;
	}
}