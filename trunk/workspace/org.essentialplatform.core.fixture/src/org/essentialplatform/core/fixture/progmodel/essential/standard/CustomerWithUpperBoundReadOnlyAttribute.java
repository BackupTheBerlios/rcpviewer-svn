/**
 * 
 */
package org.essentialplatform.core.fixture.progmodel.essential.standard;

import org.essentialplatform.progmodel.essential.app.InDomain;
import org.essentialplatform.progmodel.essential.app.UpperBoundOf;

@InDomain
public class CustomerWithUpperBoundReadOnlyAttribute {
	String surname;
	@UpperBoundOf(3)
	public String getSurname() {
		return surname;
	}
}