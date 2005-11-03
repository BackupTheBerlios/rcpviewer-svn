/**
 * 
 */
package org.essentialplatform.progmodel.standard.attribute;
import org.essentialplatform.progmodel.standard.*;

@InDomain
public class CustomerWithOrderingReadOnlyAttribute {
	String surname;
	@UpperBoundOf(3)
	@Ordered
	public String getSurname() {
		return surname;
	}
}