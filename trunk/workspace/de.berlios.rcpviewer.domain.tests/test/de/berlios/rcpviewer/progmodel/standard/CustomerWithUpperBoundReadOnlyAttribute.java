/**
 * 
 */
package de.berlios.rcpviewer.progmodel.standard;

@InDomain
public class CustomerWithUpperBoundReadOnlyAttribute {
	String surname;
	@UpperBoundOf(3)
	public String getSurname() {
		return surname;
	}
}