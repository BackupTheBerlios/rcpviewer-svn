/**
 * 
 */
package org.essentialplatform.progmodel.standard;

import org.essentialplatform.progmodel.essential.app.InDomain;

@InDomain
public class CustomerWithNoUpperBoundReadOnlyAttribute {
	String surname;
	public String getSurname() {
		return surname;
	}
}