/**
 * 
 */
package org.essentialplatform.progmodel.standard.attribute;
import org.essentialplatform.progmodel.standard.*;

@InDomain
public class CustomerWithNoUniquenessReadOnlyAttribute {
	String surname;
	public String getSurname() {
		return surname;
	}
}