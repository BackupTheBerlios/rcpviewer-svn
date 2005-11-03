/**
 * 
 */
package org.essentialplatform.progmodel.standard.attribute;
import org.essentialplatform.progmodel.standard.*;

@InDomain
public class CustomerWithNonDerivedReadOnlyAttribute {
	String surname;
	public String getSurname() {
		return surname;
	}
}