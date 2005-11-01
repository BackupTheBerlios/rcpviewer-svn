/**
 * 
 */
package org.essentialplatform.progmodel.standard.attribute;
import org.essentialplatform.progmodel.standard.*;

@InDomain
public class CustomerWithNonUniqueReadOnlyAttribute {
	String surname;
	@UpperBoundOf(3)
	@Unique(false)
	public String getSurname() {
		return surname;
	}
}