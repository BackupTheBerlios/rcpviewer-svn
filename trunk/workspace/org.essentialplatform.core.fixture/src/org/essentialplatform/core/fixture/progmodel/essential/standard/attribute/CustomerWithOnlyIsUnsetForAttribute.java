/**
 * 
 */
package org.essentialplatform.core.fixture.progmodel.essential.standard.attribute;
import org.essentialplatform.progmodel.essential.app.InDomain;
import org.essentialplatform.progmodel.standard.*;

@InDomain
public class CustomerWithOnlyIsUnsetForAttribute {
	int age;
	/**
	 * Not having unsetXxx means this attribute isn't unsettable.
	 * @return
	 */
	public boolean isUnsetAge() {
		return age == -1;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
}