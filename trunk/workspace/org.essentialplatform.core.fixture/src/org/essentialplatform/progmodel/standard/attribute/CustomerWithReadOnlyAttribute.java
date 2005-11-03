/**
 * 
 */
package org.essentialplatform.progmodel.standard.attribute;
import org.essentialplatform.progmodel.standard.*;

@InDomain
public class CustomerWithReadOnlyAttribute {
	String surname;
	public String getSurname() {
		return surname;
	}
}