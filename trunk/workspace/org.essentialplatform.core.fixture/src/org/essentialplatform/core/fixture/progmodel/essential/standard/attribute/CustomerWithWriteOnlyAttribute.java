/**
 * 
 */
package org.essentialplatform.core.fixture.progmodel.essential.standard.attribute;
import org.essentialplatform.progmodel.essential.app.InDomain;

@InDomain
public class CustomerWithWriteOnlyAttribute {
	private String surname;
	public void setSurname(String surname) {
		this.surname = surname;
	}
}