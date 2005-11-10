/**
 * 
 */
package org.essentialplatform.core.fixture.progmodel.essential.standard.attribute;
import org.essentialplatform.progmodel.essential.app.InDomain;

@InDomain
public class CustomerWithReadWriteAttribute {
	String surname;
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
}