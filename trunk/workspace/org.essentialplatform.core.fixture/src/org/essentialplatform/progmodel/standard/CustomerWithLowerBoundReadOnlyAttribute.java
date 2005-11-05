/**
 * 
 */
package org.essentialplatform.progmodel.standard;

import org.essentialplatform.progmodel.essential.app.InDomain;
import org.essentialplatform.progmodel.essential.app.LowerBoundOf;

@InDomain
public class CustomerWithLowerBoundReadOnlyAttribute {
	String surname;
	@LowerBoundOf(0)
	public String getSurname() {
		return surname;
	}
}