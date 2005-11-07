/**
 * 
 */
package org.essentialplatform.core.fixture.progmodel.essential.standard;

import org.essentialplatform.progmodel.essential.app.InDomain;

@InDomain
public class CustomerWithNoLowerBoundReadOnlyAttribute {
	String surname;
	public String getSurname() {
		return surname;
	}
}