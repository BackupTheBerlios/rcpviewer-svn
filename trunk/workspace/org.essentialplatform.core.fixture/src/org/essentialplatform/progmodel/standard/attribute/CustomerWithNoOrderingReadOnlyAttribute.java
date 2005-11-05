/**
 * 
 */
package org.essentialplatform.progmodel.standard.attribute;
import org.essentialplatform.progmodel.essential.app.InDomain;
import org.essentialplatform.progmodel.standard.*;

@InDomain
public class CustomerWithNoOrderingReadOnlyAttribute {
	String surname;
	public String getSurname() {
		return surname;
	}
}