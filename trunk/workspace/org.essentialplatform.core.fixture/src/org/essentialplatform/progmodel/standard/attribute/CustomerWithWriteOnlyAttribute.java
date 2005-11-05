/**
 * 
 */
package org.essentialplatform.progmodel.standard.attribute;
import org.essentialplatform.progmodel.essential.app.InDomain;
import org.essentialplatform.progmodel.standard.*;

@InDomain
public class CustomerWithWriteOnlyAttribute {
	private String surname;
	public void setSurname(String surname) {
		this.surname = surname;
	}
}