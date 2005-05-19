/**
 * 
 */
package de.berlios.rcpviewer.progmodel.standard;

@InDomain
public class TestDomainClassAttributesCustomerWithWriteOnlyAttribute {
	private String surname;
	public void setSurname(String surname) {
		this.surname = surname;
	}
}