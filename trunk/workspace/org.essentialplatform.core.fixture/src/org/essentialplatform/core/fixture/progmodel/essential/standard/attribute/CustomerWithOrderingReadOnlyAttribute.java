/**
 * 
 */
package org.essentialplatform.core.fixture.progmodel.essential.standard.attribute;
import org.essentialplatform.progmodel.essential.app.InDomain;
import org.essentialplatform.progmodel.essential.app.Ordered;
import org.essentialplatform.progmodel.essential.app.UpperBoundOf;
import org.essentialplatform.progmodel.standard.*;

@InDomain
public class CustomerWithOrderingReadOnlyAttribute {
	String surname;
	@UpperBoundOf(3)
	@Ordered
	public String getSurname() {
		return surname;
	}
}