/**
 * 
 */
package org.essentialplatform.core.fixture.progmodel.essential.standard.attribute;
import org.essentialplatform.progmodel.essential.app.InDomain;

@InDomain
public class CustomerWithNonDerivedReadOnlyAttribute {
	String surname;
	public String getSurname() {
		return surname;
	}
}