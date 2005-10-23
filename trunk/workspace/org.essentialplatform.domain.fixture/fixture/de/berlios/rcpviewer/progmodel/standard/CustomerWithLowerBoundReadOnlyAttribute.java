/**
 * 
 */
package de.berlios.rcpviewer.progmodel.standard;

@InDomain
public class CustomerWithLowerBoundReadOnlyAttribute {
	String surname;
	@LowerBoundOf(0)
	public String getSurname() {
		return surname;
	}
}