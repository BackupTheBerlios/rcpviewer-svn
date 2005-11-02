/**
 * 
 */
package org.essentialplatform.progmodel.standard.attribute;
import org.essentialplatform.progmodel.standard.*;

@InDomain
public class CustomerWithUniqueReadOnlyAttribute {
	String surname;
	@UpperBoundOf(3)
	@Unique
	public String getSurname() {
		return surname;
	}
}