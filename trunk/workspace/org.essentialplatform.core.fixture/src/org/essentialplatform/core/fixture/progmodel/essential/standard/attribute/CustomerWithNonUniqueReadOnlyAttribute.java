/**
 * 
 */
package org.essentialplatform.core.fixture.progmodel.essential.standard.attribute;
import org.essentialplatform.progmodel.essential.app.InDomain;
import org.essentialplatform.progmodel.essential.app.Unique;
import org.essentialplatform.progmodel.essential.app.UpperBoundOf;
import org.essentialplatform.progmodel.standard.*;

@InDomain
public class CustomerWithNonUniqueReadOnlyAttribute {
	String surname;
	@UpperBoundOf(3)
	@Unique(false)
	public String getSurname() {
		return surname;
	}
}