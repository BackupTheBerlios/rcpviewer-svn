/**
 * 
 */
package org.essentialplatform.progmodel.standard.attribute;
import org.essentialplatform.progmodel.essential.app.Derived;
import org.essentialplatform.progmodel.essential.app.InDomain;
import org.essentialplatform.progmodel.standard.*;

@InDomain
public class CustomerWithDerivedReadOnlyAttribute {
	@Derived
	public String getSurname() {
		return "foobar";
	}
}