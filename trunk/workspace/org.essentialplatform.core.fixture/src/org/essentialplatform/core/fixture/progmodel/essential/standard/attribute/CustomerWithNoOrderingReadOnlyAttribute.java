/**
 * 
 */
package org.essentialplatform.core.fixture.progmodel.essential.standard.attribute;
import org.essentialplatform.progmodel.essential.app.InDomain;

@InDomain
public class CustomerWithNoOrderingReadOnlyAttribute {
	String surname;
	public String getSurname() {
		return surname;
	}
}