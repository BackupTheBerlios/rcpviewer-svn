/**
 * 
 */
package org.essentialplatform.progmodel.standard.attribute;
import org.essentialplatform.progmodel.standard.*;

@InDomain
public class CustomerWithoutOrderingReadOnlyAttribute {
	String surname;
	@UpperBoundOf(3)
	@Ordered(false)
	public String getSurname() {
		return surname;
	}
}