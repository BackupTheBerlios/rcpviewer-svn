/**
 * 
 */
package org.essentialplatform.progmodel.standard.attribute;
import org.essentialplatform.progmodel.essential.app.InDomain;
import org.essentialplatform.progmodel.essential.app.Unique;
import org.essentialplatform.progmodel.essential.app.UpperBoundOf;
import org.essentialplatform.progmodel.standard.*;

@InDomain
public class CustomerWithUniqueReadOnlyAttribute {
	String surname;
	@UpperBoundOf(3)
	@Unique
	public String getSurname() {
		return surname;
	}
}