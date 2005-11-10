/**
 * 
 */
package org.essentialplatform.core.fixture.progmodel.essential.standard.attribute;
import org.essentialplatform.progmodel.essential.app.Derived;
import org.essentialplatform.progmodel.essential.app.InDomain;

@InDomain
public class CustomerWithDerivedReadOnlyAttribute {
	@Derived
	public String getSurname() {
		return "foobar";
	}
}